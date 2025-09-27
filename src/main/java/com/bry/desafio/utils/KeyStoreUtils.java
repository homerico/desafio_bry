package com.bry.desafio.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class KeyStoreUtils {

    public static final String keyStoreType = "PKCS12";

    public static KeyStore getKeyStore(InputStream keyStoreData, char[] password) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException {
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(keyStoreData, password);
        return keyStore;
    }

    public static X509Certificate getCertificateData(KeyStore keyStore, String alias) throws KeyStoreException, CertificateEncodingException {
        return (X509Certificate) keyStore.getCertificate(alias);
    }

    public static PrivateKey getPrivateKeyData(KeyStore keyStore, String alias, char[] password) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException {
        return (PrivateKey) keyStore.getKey(alias, password);
    }
}
