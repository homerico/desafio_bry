package com.bry.desafio.web;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Security;

@SpringBootApplication(scanBasePackages = "com.bry.desafio")
public class Application {

	static {
		Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
