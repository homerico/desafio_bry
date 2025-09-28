package com.bry.desafio.signature.report;

/**
 * Classe que representa o relatório de verificação da assinatura.
 * Contém informações sobre a integridade da assinatura, a confiança do certificado
 * e quaisquer exceções que possam ter ocorrido durante o processo de verificação.
 */
public class Report {


    // Status da integridade da assinatura
    private Status isIntegrityValid;
    // Erro ocorrido durante o processo de verificação, se houver
    private String exception;
    private CertificateReport certificateReport;

    public Report() {
        this.isIntegrityValid = Status.INDETERMINATE;
        this.certificateReport = new CertificateReport();
    }

    public Status isValid() {
        return allValid(isIntegrityValid, certificateReport.getIsCertificateTrusted(), exception == null ? Status.VALID : Status.INVALID);
    }

    public Status isIntegrityValid() {
        return isIntegrityValid;
    }

    public Status isCertificateTrusted() {
        return certificateReport.getIsCertificateTrusted();
    }

    public String getException() {
        return exception;
    }

    public void setIntegrityValid(boolean integrityValid) {
        isIntegrityValid = integrityValid ? Status.VALID : Status.INVALID;
    }

    public void setCertificateTrusted(boolean certificateTrusted) {
        certificateReport.setIsCertificateTrusted(certificateTrusted);
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public static Status allValid(Status... statuses) {
        for (Status status : statuses) {
            if (status != Status.VALID && status != Status.TRUSTED) {
                return Status.INVALID;
            }
        }
        return Status.VALID;
    }
}
