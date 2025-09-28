package com.bry.desafio.web;

import com.bry.desafio.Configuration;
import com.bry.desafio.signature.TrustAnchors;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TrustAnchorsInitializer implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        TrustAnchors.loadTrustAnchors(Configuration.trustAnchorPath);
    }
}
