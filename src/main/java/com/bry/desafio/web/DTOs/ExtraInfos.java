package com.bry.desafio.web.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExtraInfos {
    private String signerName;
    private String signingDate;
    private String documentHash;
    private String hashAlgorithm;

    public String getSignerName() {
        return signerName;
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

    public void setSignerName(String signerName) {
        this.signerName = signerName;
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
}
