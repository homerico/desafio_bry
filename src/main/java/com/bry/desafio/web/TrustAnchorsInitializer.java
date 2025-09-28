package com.bry.desafio.web;

import com.bry.desafio.Configuration;
import com.bry.desafio.signature.certificate.TrustAnchors;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Classe responsável por inicializar os Trust Anchors na aplicação.
 * Implementa CommandLineRunner para executar a inicialização após o contexto da aplicação ser carregado.
 */
@Component
public class TrustAnchorsInitializer implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        TrustAnchors.loadTrustAnchors(Configuration.trustAnchorPath);
    }
}
