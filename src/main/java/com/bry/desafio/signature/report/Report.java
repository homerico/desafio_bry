package com.bry.desafio.signature.report;

/**
 * Classe que representa o relatório de verificação da assinatura.
 * Contém informações sobre a integridade da assinatura, a confiança do certificado
 * e quaisquer exceções que possam ter ocorrido durante o processo de verificação.
 */
public class Report {

    public enum Status {
        VALID("Válido"), INVALID("Inválido"),
        TRUSTED("Confiável"), UNTRUSTED("Não Confiável"),
        INDETERMINATE("Indeterminado");

        private final String name;

        Status(String name) {
            this.name = name;
        }
        
        public static Status allValid(Status... statuses) {
            for (Status status : statuses) {
                if (status != VALID && status != TRUSTED) {
                    return INVALID;
                }
            }
            return VALID;
        }
        
        @Override
        public String toString() {
            return name;
        }
    }

    // Status da integridade da assinatura
    private Status isIntegrityValid;
    // Status da confiança do certificação
    private Status isCertificateTrusted;
    // Erro ocorrido durante o processo de verificação, se houver
    private Exception exception;

    public Report() {
        this.isIntegrityValid = Status.INDETERMINATE;
        this.isCertificateTrusted = Status.INDETERMINATE;
    }

    public Status isValid() {
        return Status.allValid(isIntegrityValid, isCertificateTrusted, exception == null ? Status.VALID : Status.INVALID);
    }

    public Status isIntegrityValid() {
        return isIntegrityValid;
    }

    public Status isCertificateTrusted() {
        return isCertificateTrusted;
    }

    public Exception getException() {
        return exception;
    }

    public void setIntegrityValid(boolean integrityValid) {
        isIntegrityValid = integrityValid ? Status.VALID : Status.INVALID;
    }

    public void setCertificateTrusted(boolean certificateTrusted) {
        isCertificateTrusted = certificateTrusted ? Status.TRUSTED : Status.UNTRUSTED;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
