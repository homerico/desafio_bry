package com.bry.desafio;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.cert.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Classe responsável por carregar e armazenar as âncoras de confiança e certificados intermediários.
 */
public class TrustAnchors {

    private static final Set<TrustAnchor> trustAnchors = new HashSet<>();
    private static final Set<X509Certificate> intermediateCertificates = new HashSet<>();

    public static void loadTrustAnchors(String trustAnchorCertificatePath) throws CertificateException, IOException, URISyntaxException {
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");

        // Pega o Path dos certificados presentes na pasta especificada
        Path trustStorePath = Paths.get(TrustAnchors.class.getClassLoader().getResource(trustAnchorCertificatePath).toURI());
        try (Stream<Path> files = Files.list(trustStorePath)) {
            List<Path> pathList = files.filter(f -> f.toString().endsWith(".crt") || f.toString().endsWith(".cer")).toList();
            // Carrega cada certificado
            for (Path path : pathList) {
                InputStream in = Files.newInputStream(path);
                X509Certificate cert = (X509Certificate) certFactory.generateCertificate(in);
                // Verifica se é uma âncora ou um intermediário
                if (cert.getSubjectX500Principal().equals(cert.getIssuerX500Principal())) {
                    trustAnchors.add(new TrustAnchor(cert, null));
                } else {
                    intermediateCertificates.add(cert);
                }
                in.close();
            }
        }
    }

    public static Set<TrustAnchor> getTrustAnchors() {
        return trustAnchors;
    }

    public static Set<X509Certificate> getIntermediateCertificates() {
        return intermediateCertificates;
    }
}
