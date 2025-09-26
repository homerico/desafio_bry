package com.bry.desafio.signer;


import com.bry.desafio.Algorithms;
import com.bry.desafio.ToBeSignedContent;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class SimpleCMSSignerTest {

    // Hash SHA-512 de referência do conteúdo em doc.txt obtido com a ferramenta online https://www.dcode.fr/sha512-hash
    private static final String digestReference = "dc1a7de77c59a29f366a4b154b03ad7d99013e36e08beb50d976358bea7b045884fe72111b27cf7d6302916b2691ac7696c1637e1ab44584d8d6613825149e35";
    private static final String digestAlgorithm = Algorithms.SUPPORTED_DIGEST_ALGORITHMS.SHA512.name();
    private static final String docToBeSigned = "arquivos/doc.txt";
    private static byte[] content;

    @BeforeAll
    public static void setup() {
        try (InputStream is = SimpleCMSSignerTest.class.getClassLoader().getResourceAsStream(docToBeSigned)){
            Assertions.assertNotNull(is);
            content = is.readAllBytes();
        } catch (IOException e) {
            fail("Falha ao carregar documento a ser assinado.", e);
        }
    }

    @Test
    public void isDigestValid() {
        ToBeSignedContent toBeSignedContent = new ToBeSignedContent(content);
        byte[] hashedContent = null;
        try {
            hashedContent = toBeSignedContent.getHash(digestAlgorithm);
        } catch (SignerException e) {
            fail(e.getMessage(), e.getCause());
        }
        String hex = Hex.toHexString(hashedContent);
        assertEquals(digestReference, hex);
    }
}
