package com.bry.desafio.report;


import org.json.JSONObject;

/**
 * Classe que representa o relatório de verificação da assinatura.
 * Contém informações sobre a integridade da assinatura, a confiança do certificado
 * e quaisquer exceções que possam ter ocorrido durante o processo de verificação.
 */
public class Report {

    // Status da integridade da assinatura
    private boolean isIntegrityValid;
    // Status da confiança do certificação
    private boolean isCertificateTrusted;
    // Erro ocorrido durante o processo de verificação, se houver
    private Exception exception;

    public Report() {
        this.isIntegrityValid = false;
        this.isCertificateTrusted = false;
    }

    public boolean isIntegrityValid() {
        return isIntegrityValid;
    }

    public boolean isCertificateTrusted() {
        return isCertificateTrusted;
    }

    public Exception getException() {
        return exception;
    }

    public void setIntegrityValid(boolean integrityValid) {
        isIntegrityValid = integrityValid;
    }

    public void setCertificateTrusted(boolean certificateTrusted) {
        isCertificateTrusted = certificateTrusted;
    }


    public void setException(Exception exception) {
        this.exception = exception;
    }

    /**
     * Converte o relatório para um objeto JSON. Facilita a transmissão do relatório pela API.
     *
     * @return JSONObject representando o relatório de verificação.
     */
    public JSONObject toJSONbject() {
        JSONObject report = new JSONObject();
        report.put("Status", isIntegrityValid && isCertificateTrusted ? "Válido" : "Inválido");

        JSONObject json = new JSONObject();
        report.put("Detalhes", json);
        json.put("Integridade", isIntegrityValid ? "Válida" : "Inválida");
        json.put("Certificação", isCertificateTrusted ? "Confiável" : "Não confiável");
        if (exception != null) {
            json.put("Erro: ", exception.getMessage());
        }
        return report;
    }
}
