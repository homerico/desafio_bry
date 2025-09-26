package com.bry.desafio.signer;

public class SignerException extends Exception {

    public static final String NOT_SUPPORTED_DIGEST_ALGORITHM = "Não há suporte para o algoritmo de resumo especificado: ";
    public static final String INVALID_DIGEST_ALGORITHM = "Algoritmo de resumo inválido.";

    public SignerException(String message) {
        super(message);
    }

    public SignerException(String message, Throwable cause) {
        super(message, cause);
    }
}
