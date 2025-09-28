package com.bry.desafio.web;

import com.bry.desafio.Configuration;
import com.bry.desafio.signature.report.Report;
import com.bry.desafio.signature.signer.CMSSigner;
import com.bry.desafio.signature.signer.SignerException;
import com.bry.desafio.signature.utils.KeyStoreUtils;
import com.bry.desafio.signature.verifier.CMSVerifier;
import com.bry.desafio.signature.verifier.VerifierException;
import com.bry.desafio.web.DTOs.Details;
import com.bry.desafio.web.DTOs.VerificationResponse;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Serviço para criação e verificação de assinaturas.
 */
@Service
public class SignatureService {

    public byte[] createCmsSignature(byte[] content, byte[] pkcs12Content, String password) throws SignerException, CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, UnrecoverableKeyException {
        KeyStore keyStore = KeyStoreUtils.getKeyStore(new ByteArrayInputStream(pkcs12Content), password.toCharArray());

        // Como o alias não é fornecido, assumi-se que é o mesmo passado pelo desafio
        PrivateKey privateKey = KeyStoreUtils.getPrivateKeyData(keyStore, Configuration.certificateAlias, password.toCharArray());

        X509Certificate x509Certificate = KeyStoreUtils.getCertificateData(keyStore, Configuration.certificateAlias);

        CMSSigner cmsSigner = new CMSSigner(x509Certificate, privateKey, Configuration.signatureAlgorithm);
        return cmsSigner.sign(content);
    }

    public VerificationResponse verifyCmsSignature(byte[] signatureContent) throws VerifierException {
        VerificationResponse response = new VerificationResponse();

        CMSVerifier signatureVerifier = new CMSVerifier();
        signatureVerifier.verify(signatureContent);

        Report report = signatureVerifier.getVerificationReport();

        response.setStatus(report.isValid().toString());

        Details details = new Details();
        details.setSignatureValidity(report.isIntegrityValid().toString());
        details.setCertificateTrust(report.isCertificateTrusted().toString());
        if (report.getException() != null) {
            details.setErrorMessage(report.getException().getMessage());
        }
        response.setDetails(details);

        return response;
    }
}
