package com.bry.desafio.signer;

public class SignerException extends Exception {

    public static final String NOT_SUPPORTED_DIGEST_ALGORITHM = "Não há suporte para o algoritmo de resumo especificado: ";
    public static final String NOT_SUPPORTED_SIGNATURE_ALGORITHM = "Não há suporte para o algoritmo de assinatura especificado: ";
    public static final String INVALID_DIGEST_ALGORITHM = "Algoritmo de resumo inválido.";
    public static final String SIGNER_INFORMATION_ERROR = "Erro ao criar as informações do assinante.";
    public static final String SIGNING_ERROR = "Erro ao assinar o conteúdo.";

    public SignerException(String message) {
        super(message);
    }

    public SignerException(String message, Throwable cause) {
        super(message, cause);
    }
}
