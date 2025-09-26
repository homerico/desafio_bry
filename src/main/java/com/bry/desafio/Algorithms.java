package com.bry.desafio;

import java.util.List;
import java.util.stream.Stream;

public class Algorithms {

    public enum SUPPORTED_DIGEST_ALGORITHMS {
        SHA512("SHA-512"),;

        private final String name;

        SUPPORTED_DIGEST_ALGORITHMS(String name) {
            this.name = name;
        }
    }

    public static List<String> getSupportedDigestAlgorithms() {
        return Stream.of(SUPPORTED_DIGEST_ALGORITHMS.values()).map(Enum::name).toList();
    }
}
