package br.unipar.fish.invest.enums;

public enum TipoOperacao {

    APORTE("Aporte"),
    SAQUE("Saque"),
    TRANSFERENCIA("Transferência");

    private final String descricao;

    TipoOperacao(String descricao) {
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