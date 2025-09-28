package com.bry.desafio.web.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Classe que representa os detalhes da verificação de assinatura.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerificationDetails {
    /** Validade da assinatura */
    private String signatureValidity;
    /** Data da assinatura */
    private String signingDate;
    /** Hash do documento assinado */
    private String documentHash;
    /** Algoritmo de hash utilizado */
    private String hashAlgorithm;
    /** Mensagem de erro, se houver */
    private String errorMessage;
    /** Informações extras sobre a assinatura
     * @see CertificateInfos
     */
    private CertificateInfos certificateInfos;

    public String getSignatureValidity() {
        return signatureValidity;
    }

    public String getSigningDate() {
        return signingDate;
    }

    public String getDocumentHash() {
        return documentHash;
    }

    public String getHashAlgorithm() {
        return hashAlgorithm;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setSignatureValidity(String signatureValidity) {
        this.signatureValidity = signatureValidity;
    }

    public void setSigningDate(String signingDate) {
        this.signingDate = signingDate;
    }

    public void setDocumentHash(String documentHash) {
        this.documentHash = documentHash;
    }

    public void setHashAlgorithm(String hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public CertificateInfos getCertificateInfos() {
        return certificateInfos;
    }

    public void setCertificateInfos(CertificateInfos certificateInfos) {
        this.certificateInfos = certificateInfos;
    }
}
