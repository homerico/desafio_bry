package com.bry.desafio.signature.utils;

import com.bry.desafio.Exceptions.KeyStoreException;

import java.io.IOException;
import java.io.InputStream;

import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static com.bry.desafio.Exceptions.KeyStoreException.*;

/**
 * Utilitário para operações com KeyStore.
 */
public class KeyStoreUtils {

    public static final String keyStoreType = "PKCS12";

    /**
     * Carrega um KeyStore a partir de um InputStream.
     *
     * @param keyStoreData InputStream contendo os dados do KeyStore.
     * @param password     Senha para acessar o KeyStore.
     * @return Instância do KeyStore carregado.
     * @throws KeyStoreException        Se ocorrer um erro ao criar o KeyStore.
     */
    public static KeyStore getKeyStore(InputStream keyStoreData, char[] password) throws KeyStoreException {
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(keyStoreData, password);
        } catch (CertificateException | IOException | NoSuchAlgorithmException | java.security.KeyStoreException e) {
            throw new KeyStoreException(KEYSTORE_LOADING_ERROR, e);
        }
        return keyStore;
    }

    /**
     * Recupera um certificado X.509 do KeyStore.
     *
     * @param keyStore Instância do KeyStore.
     * @param alias    Alias do certificado a ser obtido.
     * @return O certificado X.509 correspondente ao alias fornecido.
     * @throws KeyStoreException           Se ocorrer um erro ao obter o certificado especificado.
     */
    public static X509Certificate getCertificateData(KeyStore keyStore, String alias) throws KeyStoreException {
        try {
            return (X509Certificate) keyStore.getCertificate(alias);
        } catch (java.security.KeyStoreException e) {
            throw new KeyStoreException(CERTIFICATE_RETRIEVAL_ERROR, e);
        }
    }

    /**
     * Recupera a chave privada do KeyStore.
     *
     * @param keyStore Instância do KeyStore.
     * @param alias    Alias da chave privada a ser recuperada.
     * @param password Senha para acessar a chave privada.
     * @return A chave privada correspondente ao alias fornecido.
     * @throws KeyStoreException        Se ocorrer um erro ao obter a chave privada.
     */
    public static PrivateKey getPrivateKeyData(KeyStore keyStore, String alias, char[] password) throws KeyStoreException {
        try {
            return (PrivateKey) keyStore.getKey(alias, password);
        } catch (java.security.KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new KeyStoreException(PRIVATE_KEY_RETRIEVAL_ERROR, e);
        }
    }
}
