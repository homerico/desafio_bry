package com.bry.desafio.signature;

import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Classe que define os algoritmos suportados para digest e assinatura.
 */
public class Algorithms {

    private static Map<String, String> digestAlgorithmsMap;

    static {
        digestAlgorithmsMap = Map.of(
                NISTObjectIdentifiers.id_sha256.getId(), "SHA-256",
                NISTObjectIdentifiers.id_sha384.getId(), "SHA-384",
                NISTObjectIdentifiers.id_sha512.getId(), "SHA-512"
        );
    }

    public enum DIGEST_ALGORITHMS {
        SHA512("SHA-512"),;

        private final String name;

        DIGEST_ALGORITHMS(String name) {
            this.name = name;
        }
    }

    public enum SIGNATURE_ALGORITHMS {
        SHA512withRSA("SHA512withRSA"),;

        private final String name;

        SIGNATURE_ALGORITHMS(String name) {
            this.name = name;
        }

    }

    public static String getDigestAlgorithmByOID(String oid) {
        return digestAlgorithmsMap.get(oid);
    }

    public static List<String> getSupportedDigestAlgorithms() {
        return Stream.of(DIGEST_ALGORITHMS.values()).map(Enum::name).toList();
    }

    public static List<String> getSupportedSignatureAlgorithms() {
        return Stream.of(SIGNATURE_ALGORITHMS.values()).map(Enum::name).toList();
    }
}
