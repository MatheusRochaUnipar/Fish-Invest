package br.unipar.fish.invest.domains;

import java.math.BigDecimal;

public class CarteiraInvestimentos extends BaseEntity {
    
    private String nomeEspecifico;
    private BigDecimal saldoTotal;
    private BigDecimal rendimentoAcumulado;
    
    private Cliente cliente;
          
    private TipoInvestimento tipoInvestimento;

    public CarteiraInvestimentos() {
        super();
    }

    public CarteiraInvestimentos(String nomeEspecifico, BigDecimal saldoTotal, BigDecimal rendimentoAcumulado, Cliente cliente, TipoInvestimento tipoInvestimento, Integer id) {
        super(id);
        this.nomeEspecifico = nomeEspecifico;
        this.saldoTotal = saldoTotal;
        this.rendimentoAcumulado = rendimentoAcumulado;
        this.cliente = cliente;
        this.tipoInvestimento = tipoInvestimento;
    }

    public String getNomeEspecifico() {
        return nomeEspecifico;
    }

    public void setNomeEspecifico(String nomeEspecifico) {
        this.nomeEspecifico = nomeEspecifico;
    }

    public BigDecimal getSaldoTotal() {
        return saldoTotal;
    }

    public void setSaldoTotal(BigDecimal saldoTotal) {
        this.saldoTotal = saldoTotal;
    }

    public BigDecimal getRendimentoAcumulado() {
        return rendimentoAcumulado;
    }

    public void setRendimentoAcumulado(BigDecimal rendimentoAcumulado) {
        this.rendimentoAcumulado = rendimentoAcumulado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public TipoInvestimento getTipoInvestimento() {
        return tipoInvestimento;
    }

    public void setTipoInvestimento(TipoInvestimento tipoInvestimento) {
        this.tipoInvestimento = tipoInvestimento;
    }

    @Override
    public String toString() {
        return "CarteiraInvestimentos{" + "nomeEspecifico=" + nomeEspecifico + ", saldoTotal=" + saldoTotal + ", rendimentoAcumulado=" + rendimentoAcumulado + ", cliente=" + cliente + ", tipoInvestimento=" + tipoInvestimento + '}';
    }    
}
