package com.agendaedu.educacional.Entities;

public enum PresenceStatus {
    PENDENTE("Pendente"),
    CONFIRMADO("Confirmado"),
    CANCELADO("Cancelado");

    private final String descricao;

    PresenceStatus(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
