package com.bry.desafio.web;

import com.bry.desafio.exceptions.KeyStoreException;
import com.bry.desafio.exceptions.SignerException;
import com.bry.desafio.web.DTOs.VerificationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

/**
 * Controlador REST para operações de assinatura e verificação.
 */
@RestController
@RequestMapping("/desafio")
public class SignatureController {

    private final SignatureService signatureService;

    public SignatureController(SignatureService signatureService) {
        this.signatureService = signatureService;
    }

    /**
     * Endpoint para criar uma assinatura.
     *
     * @param file        O arquivo a ser assinado.
     * @param pkcs12File O KeyStore PKCS#12 contendo o certificado e a chave privada.
     * @param password    A senha do arquivo PKCS#12.
     * @return A assinatura em Base64 ou uma mensagem de erro.
     */
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
        } catch (SignerException | KeyStoreException e) {
            return ResponseEntity.badRequest().body("Erro de processamento: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro inesperado: " + e.getMessage());
        }
    }

    /**
     * Endpoint para verificar uma assinatura.
     *
     * @param signatureFile O arquivo de assinatura a ser verificado.
     * @return O resultado da verificação.
     */
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