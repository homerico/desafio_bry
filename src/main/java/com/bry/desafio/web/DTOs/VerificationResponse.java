package com.bry.desafio.web.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Classe que representa a resposta da verificação de assinatura.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerificationResponse {
    /** Status da verificação */
    private String status;
    /** Detalhes da verificação
     * @see VerificationDetails
     */
    private VerificationDetails verificationDetails;

    public String getStatus() {
        return status;
    }

    public VerificationDetails getVerificationDetails() {
        return verificationDetails;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setVerificationDetails(VerificationDetails verificationDetails) {
        this.verificationDetails = verificationDetails;
    }

    public void setErrorMessage(String message) {
        verificationDetails.setErrorMessage(message);
    }
}