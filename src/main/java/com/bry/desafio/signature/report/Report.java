package com.bry.desafio.signature.report;

/**
 * Classe que representa o relatório de verificação da assinatura.
 * Contém informações sobre a integridade da assinatura, a confiança do certificado
 * e quaisquer exceções que possam ter ocorrido durante o processo de verificação.
 */
public class Report {


    // Status da integridade da assinatura
    private Status isIntegrityValid;
    // Data da assinatura
    private String signingDate;
    // Hash do documento assinado
    private String documentHash;
    // Algoritmo de hash utilizado
    private String hashAlgorithm;
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

    public CertificateReport getCertificateReport() {
        return certificateReport;
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

    public String getSigningDate() {
        return signingDate;
    }

    public String getDocumentHash() {
        return documentHash;
    }

    public String getHashAlgorithm() {
        return hashAlgorithm;
    }

    public String getSignerName() {
        return certificateReport.getSignerName();
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

    public void setSigningDate(String signingDate) {
        this.signingDate = signingDate;
    }

    public void setDocumentHash(String documentHash) {
        this.documentHash = documentHash;
    }

    public void setHashAlgorithm(String hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
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
