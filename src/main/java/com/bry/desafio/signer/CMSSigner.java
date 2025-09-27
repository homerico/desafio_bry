package com.bry.desafio.signer;

import com.bry.desafio.Algorithms;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoGeneratorBuilder;
import org.bouncycastle.operator.OperatorCreationException;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

import static com.bry.desafio.signer.SignerException.*;

public class CMSSigner {

    private final X509Certificate certificate;
    private final PrivateKey privateKey;
    private final String signatureAlgorithm;
    private static final CMSSignedDataGenerator cmsSignedDataGenerator = new CMSSignedDataGenerator();

    /**
     * Construtor.
     *
     * @param certificate        certificado, no padrão X.509, do assinante.
     * @param privateKey         chave privada do assinante.
     * @param signatureAlgorithm algoritmo de assinatura a ser utilizado.
     * @throws SignerException se o algoritmo de assinatura não for suportado.
     */
    public CMSSigner(X509Certificate certificate, PrivateKey privateKey, String signatureAlgorithm) throws SignerException {
        this.certificate = certificate;
        this.privateKey = privateKey;

        if (!Algorithms.getSupportedSignatureAlgorithms().contains(signatureAlgorithm)) {
            throw new SignerException(NOT_SUPPORTED_SIGNATURE_ALGORITHM + signatureAlgorithm);
        }
        this.signatureAlgorithm = signatureAlgorithm;
    }

    /**
     * Gera uma assinatura do tipo CMS.
     *
     * @param content conteúdo que será assinado.
     * @return Documento assinado.
     * @throws SignerException se ocorrer algum erro durante o processo de assinatura.
     */
    public byte[] sign(byte[] content) throws SignerException {
        // Transforma o conteúdo a ser assinado num formato compatível com o da assinatura CMS
        CMSTypedData dataToBeSigned = new CMSProcessableByteArray(content);

        // Gera as informações do assinante na estrutura necessária para ser adicionada na assinatura
        SignerInfoGenerator signerInfoGenerator = null;
        try {
            signerInfoGenerator = new JcaSimpleSignerInfoGeneratorBuilder().setProvider("BC").build(signatureAlgorithm, privateKey, certificate);
        } catch (OperatorCreationException | CertificateEncodingException e) {
            throw new SignerException(SIGNER_INFORMATION_ERROR, e);
        }
        cmsSignedDataGenerator.addSignerInfoGenerator(signerInfoGenerator);

        // Por fim, gera a assinatura
        try {
            return cmsSignedDataGenerator.generate(dataToBeSigned, true).getEncoded();
        } catch (CMSException | IOException e) {
            throw new SignerException(SIGNING_ERROR, e);
        }
    }
}
