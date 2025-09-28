package com.bry.desafio.signature.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

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
     * @throws CertificateException     Se ocorrer um erro ao carregar os certificados.
     * @throws IOException              Se ocorrer um erro de I/O.
     * @throws NoSuchAlgorithmException Se o algoritmo necessário não estiver disponível.
     */
    public static KeyStore getKeyStore(InputStream keyStoreData, char[] password) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException {
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(keyStoreData, password);
        return keyStore;
    }

    /**
     * Recupera um certificado X.509 do KeyStore.
     *
     * @param keyStore Instância do KeyStore.
     * @param alias    Alias do certificado a ser recuperado.
     * @return O certificado X.509 correspondente ao alias fornecido.
     * @throws KeyStoreException           Se ocorrer um erro ao acessar o KeyStore.
     * @throws CertificateEncodingException Se ocorrer um erro ao codificar o certificado.
     */
    public static X509Certificate getCertificateData(KeyStore keyStore, String alias) throws KeyStoreException, CertificateEncodingException {
        return (X509Certificate) keyStore.getCertificate(alias);
    }

    /**
     * Recupera a chave privada do KeyStore.
     *
     * @param keyStore Instância do KeyStore.
     * @param alias    Alias da chave privada a ser recuperada.
     * @param password Senha para acessar a chave privada.
     * @return A chave privada correspondente ao alias fornecido.
     * @throws KeyStoreException        Se ocorrer um erro ao acessar o KeyStore.
     * @throws UnrecoverableKeyException Se a chave não puder ser recuperada (por exemplo, senha incorreta).
     * @throws NoSuchAlgorithmException Se o algoritmo necessário não estiver disponível.
     */
    public static PrivateKey getPrivateKeyData(KeyStore keyStore, String alias, char[] password) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException {
        return (PrivateKey) keyStore.getKey(alias, password);
    }
}
