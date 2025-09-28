package com.bry.desafio.web;

import com.bry.desafio.signature.signer.SignerException;
import com.bry.desafio.web.DTOs.VerificationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Base64;

@RestController
@RequestMapping("/desafio")
public class SignatureController {

    private final SignatureService signatureService;

    public SignatureController(SignatureService signatureService) {
        this.signatureService = signatureService;
    }

    @PostMapping("/assinar")
    public ResponseEntity<String> createSignature(
            @RequestParam("file") MultipartFile file,
            @RequestParam("pkcs12") MultipartFile pkcs12File,
            @RequestParam("password") String password) {

        try {
            byte[] fileContent = file.getBytes();
            byte[] pkcs12Content = pkcs12File.getBytes();

            byte[] signature = signatureService.createCmsSignature(fileContent, pkcs12Content, password);

            // Retorna a assinatura em Base64
            String base64Signature = Base64.getEncoder().encodeToString(signature);
            return ResponseEntity.ok(base64Signature);
        } catch (SignerException e) {
            return ResponseEntity.internalServerError().body("Erro interno - " + e.getMessage());
        } catch (IOException | UnrecoverableKeyException | CertificateException | KeyStoreException |
                 NoSuchAlgorithmException e) {
            return ResponseEntity.badRequest().body("Erro ao processar a solicitação - " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro inesperado - " + e.getMessage());
        }
    }

    @PostMapping("/verificar")
    public ResponseEntity<VerificationResponse> verifySignature(
            @RequestParam("signature") MultipartFile signatureFile) {

        try {
            byte[] signatureContent = signatureFile.getBytes();
            VerificationResponse response = signatureService.verifyCmsSignature(signatureContent);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            VerificationResponse errorResponse = new VerificationResponse();
            errorResponse.setStatus("INVALIDO");
            errorResponse.setErrorMessage(e.getMessage());
            return ResponseEntity.ok(errorResponse);
        }
    }
}