package com.bry.desafio.web.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Classe que representa informações extras sobre a assinatura.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CertificateInfos {
    /** Confiança do certificado */
    private String certificateTrust;
    /** Nome do assinante */
    private String signerName;

    public String getSignerName() {
        return signerName;
    }

    public String getCertificateTrust() {
        return certificateTrust;
    }

    public void setSignerName(String signerName) {
        this.signerName = signerName;
    }

    public void setCertificateTrust(String certificateTrust) {
        this.certificateTrust = certificateTrust;
    }


}
