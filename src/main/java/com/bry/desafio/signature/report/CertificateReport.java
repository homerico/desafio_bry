package com.bry.desafio.signature.report;

/**
 * Classe que representa o relatório de verificação de um certificado.
 */
public class CertificateReport {

    // Status da confiança da certificação
    private Status isCertificateTrusted;

    public CertificateReport() {
        this.isCertificateTrusted = Status.INDETERMINATE;
    }

    public Status getIsCertificateTrusted() {
        return isCertificateTrusted;
    }

    public void setIsCertificateTrusted(boolean certificateTrusted) {
        isCertificateTrusted = certificateTrusted ? Status.TRUSTED : Status.UNTRUSTED;
    }
}
