package com.bry.desafio.signature;

import java.util.List;
import java.util.stream.Stream;

/**
 * Classe que define os algoritmos suportados para digest e assinatura.
 */
public class Algorithms {

    public enum DIGEST_ALGORITHMS {
        SHA512("SHA-512"),;

        private final String name;

        DIGEST_ALGORITHMS(String name) {
            this.name = name;
        }
    }

    public static List<String> getSupportedDigestAlgorithms() {
        return Stream.of(DIGEST_ALGORITHMS.values()).map(Enum::name).toList();
    }

    public enum SIGNATURE_ALGORITHMS {
        SHA512withRSA("SHA512withRSA"),;

        private final String name;

        SIGNATURE_ALGORITHMS(String name) {
            this.name = name;
        }
    }

    public static List<String> getSupportedSignatureAlgorithms() {
        return Stream.of(SIGNATURE_ALGORITHMS.values()).map(Enum::name).toList();
    }
}
