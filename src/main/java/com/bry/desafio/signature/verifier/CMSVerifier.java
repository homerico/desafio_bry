package com.bry.desafio.signature.verifier;

import com.bry.desafio.exceptions.SignerCertificateException;
import com.bry.desafio.exceptions.VerifierException;
import com.bry.desafio.signature.certificate.SignerCertificateWrapper;
import com.bry.desafio.signature.certificate.TrustAnchors;
import com.bry.desafio.signature.report.CertificateReport;
import com.bry.desafio.signature.report.Report;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.Store;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.*;
import java.util.*;

import static com.bry.desafio.exceptions.VerifierException.SIGNATURE_PROCESSING_ERROR;

/**
 * Classe para verificação de assinaturas no formato CMS (Cryptographic Message Syntax).
 * É responsável por validar a integridade da assinatura e a confiança do certificado do assinante,
 * bem como gerar um relatório detalhado do processo de verificação.
 */
public class CMSVerifier {

    private final Report verificationReport = new Report();
    private SignerCertificateWrapper signerCertificateWrapper;

    /**
     * Verifica a integridade e a confiança de uma assinatura.
     *
     * @param signatureData Os dados da assinatura CMS a serem verificados.
     * @throws VerifierException Se ocorrer um erro durante o processamento da assinatura.
     */
    public void verify(byte[] signatureData) throws VerifierException {
        CMSSignedData signature;
        try {
            signature = new CMSSignedData(signatureData);
        } catch (CMSException e) {
            throw new VerifierException(SIGNATURE_PROCESSING_ERROR, e);
        }

        // Pega os certificados e as infos dos assinadores da assinatura
        Store<X509CertificateHolder> certStore = signature.getCertificates();
        SignerInformationStore signers = signature.getSignerInfos();
        Collection<SignerInformation> signerCollection = signers.getSigners();

        // Assume-se que pode ter apenas um signatário, mas poderia ser estendido para múltiplos (coassinaturas)
        Iterator<SignerInformation> iterator = signerCollection.iterator();
        if (iterator.hasNext()) {
            SignerInformation signer = iterator.next();
            try {
                this.signerCertificateWrapper = new SignerCertificateWrapper(certStore, signer);
            } catch (SignerCertificateException | CertificateException e) {
                throw new RuntimeException(e);
            }

            // Verifica a integridade da assinatura
            boolean isIntegrityValid;
            try {
                isIntegrityValid = signer.verify(new JcaSimpleSignerInfoVerifierBuilder().build(signerCertificateWrapper.getSignerCertificate()));
            } catch (OperatorCreationException | CMSException e) {
                isIntegrityValid = false;
                verificationReport.setException(e.getMessage());
            }

            verificationReport.setIntegrityValid(isIntegrityValid);

            // Verifica a confiança do certificado do assinante
            try {
                signerCertificateWrapper.validate(certStore);

                verificationReport.setCertificateTrusted(true);
            } catch (CertificateException | CertPathValidatorException | InvalidAlgorithmParameterException |
                     NoSuchAlgorithmException e) {
                verificationReport.setCertificateTrusted(false);
                verificationReport.setException(e.getMessage());
            }

        }
    }

    public Report getVerificationReport() {
        return verificationReport;
    }
}
