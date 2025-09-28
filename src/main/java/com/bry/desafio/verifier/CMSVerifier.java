package com.bry.desafio.verifier;

import com.bry.desafio.TrustAnchors;
import com.bry.desafio.report.Report;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.Selector;
import org.bouncycastle.util.Store;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.*;
import java.util.*;

import static com.bry.desafio.verifier.VerifierException.SIGNATURE_PROCESSING_ERROR;

/**
 * Classe para verificação de assinaturas no formato CMS (Cryptographic Message Syntax).
 * É responsável por validar a integridade da assinatura e a confiança do certificado do assinante,
 * bem como gerar um relatório detalhado do processo de verificação.
 */
public class CMSVerifier {

    private final Report verificationReport = new Report();

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

        // Assume-se que há apenas um signatário, mas poderia ser estendido para múltiplos (coassinaturas)
        Iterator<SignerInformation> iterator = signerCollection.iterator();
        if (iterator.hasNext()) {
            SignerInformation signer = iterator.next();
            Collection<X509CertificateHolder> certCollection = certStore.getMatches(signer.getSID());

            // Verifica a integridade da assinatura
            boolean isIntegrityValid;
            try {
                X509Certificate signerCertificate = new JcaX509CertificateConverter().getCertificate(certCollection.iterator().next());
                isIntegrityValid = signer.verify(new JcaSimpleSignerInfoVerifierBuilder().build(signerCertificate));
            } catch (CertificateException | OperatorCreationException | CMSException e) {
                isIntegrityValid = false;
                verificationReport.setException(e);
            }

            verificationReport.setIntegrityValid(isIntegrityValid);

            try {
                Set<TrustAnchor> trustAnchors = TrustAnchors.getTrustAnchors();
                Set<X509Certificate> intermediateCertificates = TrustAnchors.getIntermediateCertificates();

                List<Certificate> certChain = new ArrayList<>();

                // Adiciona todos os certificados presentes na assinatura
                // (Sabe-se que há apenas o certificado do assinante, mas poderia haver os intermediários também)
                Collection<X509CertificateHolder> holders = certStore.getMatches(new AllCertSelector());
                for (X509CertificateHolder holder : holders) {
                    certChain.add(new JcaX509CertificateConverter().getCertificate(holder));
                }

                // Adiciona os certificados intermediários da cadeia de confiança, caso não estiverem presentes ainda
                for (X509Certificate intermediateCertificate : intermediateCertificates) {
                    if (!certChain.contains(intermediateCertificate)) {
                        certChain.add(intermediateCertificate);
                    }
                }

                CertPath certPath = CertificateFactory.getInstance("X.509").generateCertPath(certChain);

                PKIXParameters pkixParams = new PKIXParameters(trustAnchors);
                pkixParams.setRevocationEnabled(false);

                CertPathValidator validator = CertPathValidator.getInstance("PKIX");
                validator.validate(certPath, pkixParams);

                verificationReport.setCertificateTrusted(true);
            } catch (CertificateException | CertPathValidatorException | InvalidAlgorithmParameterException |
                     NoSuchAlgorithmException e) {
                verificationReport.setCertificateTrusted(false);
                verificationReport.setException(e);
            }

        }
    }

    public Report getVerificationReport() {
        return verificationReport;
    }

    /**
     * Um Selector que seleciona todos os certificados
     */
    private static class AllCertSelector implements Selector<X509CertificateHolder> {

        /**
         * @return Sempre é true, selecionando todos os certificados
         *
         * @see org.bouncycastle.util.Selector#match(java.lang.Object)
         */
        @Override
        public boolean match(X509CertificateHolder holder) {
            return true;
        }

        @Override
        public Object clone() {
            return new AllCertSelector();
        }
    }
}
