package com.bry.desafio.signature.report;

public enum Status {
    VALID("Válido"), INVALID("Inválido"),
    TRUSTED("Confiável"), UNTRUSTED("Não Confiável"),
    INDETERMINATE("Indeterminado");

    private final String name;

    Status(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean toBoolean() {
        return this == VALID || this == TRUSTED;
    }
}
