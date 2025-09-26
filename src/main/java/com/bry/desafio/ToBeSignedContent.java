package com.bry.desafio;

import com.bry.desafio.signer.SignerException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.bry.desafio.signer.SignerException.INVALID_DIGEST_ALGORITHM;
import static com.bry.desafio.signer.SignerException.NOT_SUPPORTED_DIGEST_ALGORITHM;

public class ToBeSignedContent {

    private final byte[] content;

    public ToBeSignedContent(byte[] content) {
        this.content = content;
    }

    public byte[] getContent() {
        return content;
    }

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
