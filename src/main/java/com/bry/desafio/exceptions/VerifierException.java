package com.bry.desafio.exceptions;

public class VerifierException extends Exception{

    public static final String SIGNATURE_PROCESSING_ERROR = "Falha ao processar a assinatura CMS.";

    public VerifierException(String message) {
        super(message);
    }

    public VerifierException(String message, Throwable cause) {
        super(message, cause);
    }
}
