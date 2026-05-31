package br.unipar.fish.invest.domains;

import br.unipar.fish.invest.enums.StatusOperacao;
import br.unipar.fish.invest.enums.TipoOperacao;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TransacoesEDepositos extends BaseEntity {

    private BigDecimal valorTransacao;
    private TipoOperacao tipoOperacao;
    private String metodoPagamento;
    private LocalDate dataTransacao;
    private StatusOperacao statusOperacao;

    private Cliente cliente;
    private CarteiraInvestimentos carteiraInvestimentos;

    public TransacoesEDepositos() {}

    public TransacoesEDepositos(BigDecimal valorTransacao, TipoOperacao tipoOperacao, String metodoPagamento, LocalDate dataTransacao, StatusOperacao statusOperacao, Cliente cliente, CarteiraInvestimentos carteiraInvestimentos, Integer id) {
        super(id);
        this.valorTransacao = valorTransacao;
        this.tipoOperacao = tipoOperacao;
        this.metodoPagamento = metodoPagamento;
        this.dataTransacao = dataTransacao;
        this.statusOperacao = statusOperacao;
        this.cliente = cliente;
        this.carteiraInvestimentos = carteiraInvestimentos;
    }

    public BigDecimal getValorTransacao() {
        return valorTransacao;
    }

    public void setValorTransacao(BigDecimal valorTransacao) {
        this.valorTransacao = valorTransacao;
    }

    public TipoOperacao getTipoOperacao() {
        return tipoOperacao;
    }

    public void setTipoOperacao(TipoOperacao tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public LocalDate getDataTransacao() {
        return dataTransacao;
    }

    public void setDataTransacao(LocalDate dataTransacao) {
        this.dataTransacao = dataTransacao;
    }

    public StatusOperacao getStatusOperacao() {
        return statusOperacao;
    }

    public void setStatusOperacao(StatusOperacao statusOperacao) {
        this.statusOperacao = statusOperacao;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public CarteiraInvestimentos getCarteiraInvestimentos() {
        return carteiraInvestimentos;
    }

    public void setCarteiraInvestimentos(CarteiraInvestimentos carteiraInvestimentos) {
        this.carteiraInvestimentos = carteiraInvestimentos;
    }

    @Override
    public String toString() {
        return "TransacoesEDepositos{" + "valorTransacao=" + valorTransacao + ", tipoOperacao=" + tipoOperacao + ", metodoPagamento=" + metodoPagamento + ", dataTransacao=" + dataTransacao + ", statusOperacao=" + statusOperacao + ", cliente=" + cliente + ", carteiraInvestimentos=" + carteiraInvestimentos + '}';
    }
}