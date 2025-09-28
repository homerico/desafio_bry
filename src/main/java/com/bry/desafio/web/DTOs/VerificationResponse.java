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
     * @see Details
     */
    private Details details;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

    public void setErrorMessage(String message) {
        details.setErrorMessage(message);
    }
}