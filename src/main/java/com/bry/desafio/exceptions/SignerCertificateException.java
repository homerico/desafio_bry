package com.bry.desafio.exceptions;

public class SignerCertificateException extends Exception{
    public static final String SIGNER_CERTIFICATE_NOT_FOUND = "Certificado do assinante não foi encontrado.";
    public static final String DIFFERENT_SIGNER_CERTIFICATE_FOUND = "Múltiplos certificados diferentes encontrados para o assinante.";
    public static final String TRUST_ANCHOR_NOT_FOUND = "Autoridade confiável não encontrada.";

    public SignerCertificateException(String message) {
        super(message);
    }

    public SignerCertificateException(String message, Throwable cause) {
        super(message, cause);
    }
}
