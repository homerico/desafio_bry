package com.bry.desafio;

import com.bry.desafio.signature.Algorithms;

/**
 * Classe responsável por armazenar as configurações da aplicação.
 */
public class Configuration {
    public static final String certificateAlias = "{e2618a8b-20de-4dd2-b209-70912e3177f4}";
    public static final String digestAlgorithm = Algorithms.DIGEST_ALGORITHMS.SHA512.name();
    public static final String signatureAlgorithm = Algorithms.SIGNATURE_ALGORITHMS.SHA512withRSA.name();
    public static final String trustAnchorPath = "cadeia/";
    public static final String certificatePassword = "bry123456";
    public static final String keyStoreFile = "pkcs12/certificado_teste_hub.pfx";
    public static final String docToBeSigned = "arquivos/doc.txt";
}
