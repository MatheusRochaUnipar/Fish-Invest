package br.unipar.fish.invest.services;

import br.unipar.fish.invest.domains.CarteiraInvestimentos;
import br.unipar.fish.invest.domains.Cliente;
import br.unipar.fish.invest.domains.TipoInvestimento;
import br.unipar.fish.invest.domains.TransacoesEDepositos;
import br.unipar.fish.invest.enums.StatusOperacao;
import br.unipar.fish.invest.enums.TipoOperacao;
import br.unipar.fish.invest.repositories.CarteiraInvestimentosRepository;
import br.unipar.fish.invest.repositories.ClienteRepository;
import br.unipar.fish.invest.repositories.TransacoesEDepositosRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TransacoesEDepositosService {

    private static final BigDecimal LIMITE_MAXIMO_APORTE = new BigDecimal("1000000.00");

    private final CarteiraInvestimentosRepository carteiraRepo;
    private final TransacoesEDepositosRepository  transacaoRepo;
    private final ClienteRepository               clienteRepo;

    private boolean processandoAporte = false;

    public TransacoesEDepositosService() {
        this.carteiraRepo  = new CarteiraInvestimentosRepository();
        this.transacaoRepo = new TransacoesEDepositosRepository();
        this.clienteRepo   = new ClienteRepository();
    }

    public String realizarAporte(Cliente clienteLogado,
                                 CarteiraInvestimentos carteira,
                                 String valorTexto) throws Exception {

        if (processandoAporte) {
            throw new Exception(
                "Uma operação já está em andamento. "
              + "Aguarde a conclusão antes de tentar novamente.");
        }

        processandoAporte = true;

        try {
            validarSessao(clienteLogado);
            validarCarteira(carteira);
            validarTipoInvestimento(carteira);

            BigDecimal valor = converterEArredondar(valorTexto);

            validarValorMinimo(valor);
            validarLimiteMaximo(valor);
            validarSaldo(carteira, valor);

            return executarTransacao(clienteLogado, carteira, valor);

        } finally {
            processandoAporte = false;
        }
    }

    private void validarSessao(Cliente cliente) throws Exception {
        if (cliente == null) {
            throw new Exception(
                "Nenhum usuário autenticado. Faça login antes de realizar um aporte.");
        }
        if (cliente.getId() == null || cliente.getId() <= 0) {
            throw new Exception("Sessão inválida. Faça login novamente.");
        }
    }

    private void validarCarteira(CarteiraInvestimentos carteira) throws Exception {
        if (carteira == null) {
            throw new Exception(
                "Selecione um produto de investimento antes de confirmar o aporte.");
        }
    }

    private void validarTipoInvestimento(CarteiraInvestimentos carteira) throws Exception {
        TipoInvestimento tipo = carteira.getTipoInvestimento();
        if (tipo == null) {
            throw new Exception(
                "A carteira selecionada não possui tipo de investimento definido.");
        }
        if (tipo.getNomeTipo() == null || tipo.getNomeTipo().isBlank()) {
            throw new Exception("O tipo de investimento da carteira está inválido.");
        }
    }

    private BigDecimal converterEArredondar(String valorTexto) throws Exception {
        if (valorTexto == null || valorTexto.isBlank()) {
            throw new Exception("O valor do aporte não pode ser vazio.");
        }
        try {
            String normalizado = valorTexto
                    .trim()
                    .replace("R$", "")
                    .replace(" ", "")
                    .replace(".", "")
                    .replace(",", ".");
            return new BigDecimal(normalizado).setScale(2, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            throw new Exception(
                "Valor inválido. Use o formato monetário brasileiro (ex: 1.500,00).");
        }
    }

    private void validarValorMinimo(BigDecimal valor) throws Exception {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("O valor do aporte deve ser maior que R$ 0,00.");
        }
    }

    private void validarLimiteMaximo(BigDecimal valor) throws Exception {
        if (valor.compareTo(LIMITE_MAXIMO_APORTE) > 0) {
            throw new Exception(String.format(
                "O valor R$ %.2f excede o limite máximo permitido por aporte de R$ %.2f.",
                valor, LIMITE_MAXIMO_APORTE));
        }
    }

    private void validarSaldo(CarteiraInvestimentos carteira,
                               BigDecimal valor) throws Exception {
        BigDecimal saldo = carteira.getSaldoTotal();
        if (saldo == null || saldo.compareTo(valor) < 0) {
            throw new Exception(String.format(
                "Saldo insuficiente na carteira '%s'. "
              + "Disponível: R$ %.2f | Aporte solicitado: R$ %.2f.",
                carteira.getNomeEspecifico(),
                saldo != null ? saldo : BigDecimal.ZERO,
                valor));
        }
    }

    private String executarTransacao(Cliente cliente,
                                     CarteiraInvestimentos carteira,
                                     BigDecimal valor) throws Exception {

        LocalDate hoje = LocalDate.now();

        try {
            BigDecimal novoSaldo = carteira.getSaldoTotal()
                    .subtract(valor)
                    .setScale(2, RoundingMode.HALF_UP);
            carteira.setSaldoTotal(novoSaldo);

            BigDecimal rendAtual = carteira.getRendimentoAcumulado() != null
                    ? carteira.getRendimentoAcumulado() : BigDecimal.ZERO;
            BigDecimal novoRendimento = rendAtual
                    .add(valor)
                    .setScale(2, RoundingMode.HALF_UP);
            carteira.setRendimentoAcumulado(novoRendimento);

            TransacoesEDepositos transacao = new TransacoesEDepositos();
            transacao.setCliente(cliente);
            transacao.setCarteiraInvestimentos(carteira);
            transacao.setValorTransacao(valor);
            transacao.setTipoOperacao(TipoOperacao.APORTE);
            transacao.setMetodoPagamento("SALDO");
            transacao.setDataTransacao(hoje);
            transacao.setStatusOperacao(StatusOperacao.CONCLUIDO);

            carteiraRepo.atualizar(carteira);
            transacao = transacaoRepo.inserir(transacao);

            System.out.printf(
                "[LOG] Aporte | Cliente: %s (ID %d) | Carteira: %s | Produto: %s | "
              + "Valor: R$ %.2f | Data: %s | Novo saldo: R$ %.2f%n",
                cliente.getNome(), cliente.getId(),
                carteira.getNomeEspecifico(),
                carteira.getTipoInvestimento().getNomeTipo(),
                valor,
                hoje.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                novoSaldo);

            return gerarComprovante(cliente, carteira, valor,
                                    novoSaldo, novoRendimento, hoje, transacao);

        } catch (SQLException e) {
            throw new Exception(
                "Falha ao persistir a transação. Operação revertida. "
              + "Detalhe: " + e.getMessage());
        }
    }

    private String gerarComprovante(Cliente cliente,
                                    CarteiraInvestimentos carteira,
                                    BigDecimal valor,
                                    BigDecimal novoSaldo,
                                    BigDecimal novoRendimento,
                                    LocalDate data,
                                    TransacoesEDepositos transacao) {

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return  "=========================================\n"
              + "   COMPROVANTE DE APORTE - FishInvest   \n"
              + "=========================================\n"
              + String.format("Data         : %s%n",    data.format(fmt))
              + String.format("Nr Transação : %s%n",    transacao.getId())
              + "-----------------------------------------\n"
              + String.format("Cliente      : %s%n",    cliente.getNome())
              + String.format("E-mail       : %s%n",    cliente.getEmail())
              + "-----------------------------------------\n"
              + String.format("Carteira     : %s%n",    carteira.getNomeEspecifico())
              + String.format("Produto      : %s%n",    carteira.getTipoInvestimento().getNomeTipo())
              + String.format("Status       : %s%n",    StatusOperacao.CONCLUIDO.getDescricao())
              + "-----------------------------------------\n"
              + String.format("Valor Aportado  : R$ %,.2f%n", valor)
              + String.format("Saldo Carteira  : R$ %,.2f%n", novoSaldo)
              + String.format("Total Investido : R$ %,.2f%n", novoRendimento)
              + "=========================================\n"
              + "   Operação realizada com sucesso!";
    }
}