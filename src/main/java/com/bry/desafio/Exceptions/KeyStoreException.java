package com.bry.desafio.Exceptions;

public class KeyStoreException extends Exception{
    public static final String KEYSTORE_LOADING_ERROR = "Erro ao carregar o KeyStore.";
    public static final String CERTIFICATE_RETRIEVAL_ERROR = "Erro ao obter o certificado do KeyStore.";
    public static final String PRIVATE_KEY_RETRIEVAL_ERROR = "Erro ao obter a chave privada do KeyStore.";

    public KeyStoreException(String message) {
        super(message);
    }

    public KeyStoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
