package com.bry.desafio.signature;

import com.bry.desafio.exceptions.SignerException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.bry.desafio.exceptions.SignerException.INVALID_DIGEST_ALGORITHM;
import static com.bry.desafio.exceptions.SignerException.NOT_SUPPORTED_DIGEST_ALGORITHM;

/**
 * Classe que representa o conteúdo a ser assinado.
 */
public class ToBeSignedContent {

    private final byte[] content;

    public ToBeSignedContent(byte[] content) {
        this.content = content;
    }

    public byte[] getContent() {
        return content;
    }

    /**
     * Gera o hash do conteúdo usando o algoritmo de digest especificado.
     *
     * @param digestAlgorithm O algoritmo de digest a ser usado (ex: "SHA-512").
     * @return O hash do conteúdo.
     * @throws SignerException Se o algoritmo de digest não for suportado ou inválido.
     */
    public byte[] getHash(String digestAlgorithm) throws SignerException {
        if (!Algorithms.getSupportedDigestAlgorithms().contains(digestAlgorithm)) {
            throw new SignerException(NOT_SUPPORTED_DIGEST_ALGORITHM + digestAlgorithm);
        }

        try {
            return MessageDigest.getInstance(digestAlgorithm).digest(content);
        } catch (NoSuchAlgorithmException e) {
            throw new SignerException(INVALID_DIGEST_ALGORITHM, e);
        }
    }
}
