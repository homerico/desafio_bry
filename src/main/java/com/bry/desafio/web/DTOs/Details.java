package com.bry.desafio.web.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Classe que representa os detalhes da verificação de assinatura.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Details {
    /** Validade da assinatura */
    private String signatureValidity;
    /** Confiança do certificado */
    private String certificateTrust;
    /** Mensagem de erro, se houver */
    private String errorMessage;
    /** Informações extras sobre a assinatura
     * @see ExtraInfos
     */
    private ExtraInfos extraInfos;

    public String getSignatureValidity() {
        return signatureValidity;
    }

    public String getCertificateTrust() {
        return certificateTrust;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setSignatureValidity(String signatureValidity) {
        this.signatureValidity = signatureValidity;
    }

    public void setCertificateTrust(String certificateTrust) {
        this.certificateTrust = certificateTrust;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ExtraInfos getExtraInfos() {
        return extraInfos;
    }

    public void setExtraInfos(ExtraInfos extraInfos) {
        this.extraInfos = extraInfos;
    }
}
