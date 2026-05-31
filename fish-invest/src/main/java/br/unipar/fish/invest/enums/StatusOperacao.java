package br.unipar.fish.invest.enums;

public enum StatusOperacao {

    CONCLUIDO("Concluído"),
    PENDENTE("Pendente"),
    CANCELADO("Cancelado");

    private final String descricao;

    StatusOperacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}