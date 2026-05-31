package br.unipar.fish.invest;

import br.unipar.fish.invest.domains.CarteiraInvestimentos;
import br.unipar.fish.invest.domains.Cliente;
import br.unipar.fish.invest.domains.SegurancaAcesso;
import br.unipar.fish.invest.domains.TipoInvestimento;
import br.unipar.fish.invest.exceptions.NegocioException;
import br.unipar.fish.invest.exceptions.RepositorioException;
import br.unipar.fish.invest.services.CarteiraInvestimentosService;
import br.unipar.fish.invest.services.ClienteService;
import br.unipar.fish.invest.services.TransacoesEDepositosService;
import javax.swing.JOptionPane;
import java.math.BigDecimal;
import java.util.ArrayList;

public class FishInvest {

    private static final ClienteService clienteService = new ClienteService();
    private static final CarteiraInvestimentosService carteiraService = new CarteiraInvestimentosService();
    private static final TransacoesEDepositosService transacaoService = new TransacoesEDepositosService(); 
    
    private static Cliente clienteLogado = null;

    public static void main(String[] args) {
        
        while (true) {
            if (clienteLogado == null) {
                exibirMenuInicial();
            } else {
                exibirMenuPrincipal();
            }
        }
    }

    private static void exibirMenuInicial() {

        String[] opcoes = {"Fazer Login", "Criar Conta", "Sair"};

        int escolha = JOptionPane.showOptionDialog(
                null,
                "Bem-vindo ao FishInvest!\nEscolha uma opção:",
                "FishInvest — Início",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcoes,
                opcoes[0]);

        switch (escolha) {
            case 0 -> fazerLogin();
            case 1 -> criarConta();
            default -> {
                JOptionPane.showMessageDialog(null,
                        "Até logo!",
                        "FishInvest",
                        JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        }
    }

    private static void exibirMenuPrincipal() {

        BigDecimal saldoTotal      = BigDecimal.ZERO;
        BigDecimal totalInvestido  = BigDecimal.ZERO;

        try {
            ArrayList<CarteiraInvestimentos> carteiras =
                    carteiraService.listarPorCliente(clienteLogado);
            for (CarteiraInvestimentos c : carteiras) {
                if (c.getSaldoTotal() != null)
                    saldoTotal = saldoTotal.add(c.getSaldoTotal());
                if (c.getRendimentoAcumulado() != null)
                    totalInvestido = totalInvestido.add(c.getRendimentoAcumulado());
            }
        } catch (NegocioException | RepositorioException e) {
            
        }

        String mensagem = String.format(
                "Olá, %s!\n\n" +
                "Saldo Atual: R$ %,.2f\n" +
                "Total Investido: R$ %,.2f\n\n" +
                "O que deseja fazer?",
                clienteLogado.getNome(),
                saldoTotal,
                totalInvestido);

        String[] opcoes = {"Realizar Aporte", "Sair da Conta"};

        int escolha = JOptionPane.showOptionDialog(
                null,
                mensagem,
                "FishInvest — Menu Principal",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcoes,
                opcoes[0]);

        switch (escolha) {
            case 0 -> realizarAporte();
            default -> {
                clienteLogado = null;
                JOptionPane.showMessageDialog(null,
                        "Sessão encerrada com sucesso.",
                        "FishInvest",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private static void criarConta() {

        String nome = JOptionPane.showInputDialog(null,
                "Nome completo:", "Criar Conta — Nome", JOptionPane.PLAIN_MESSAGE);
        if (nome == null) return;

        String cpf = JOptionPane.showInputDialog(null,
                "CPF (formato: 000.000.000-00):", "Criar Conta — CPF", JOptionPane.PLAIN_MESSAGE);
        if (cpf == null) return;

        String email = JOptionPane.showInputDialog(null,
                "E-mail:", "Criar Conta — E-mail", JOptionPane.PLAIN_MESSAGE);
        if (email == null) return;

        String senha = JOptionPane.showInputDialog(null,
                "Senha (mínimo 8 caracteres, letras e números):",
                "Criar Conta — Senha", JOptionPane.PLAIN_MESSAGE);
        if (senha == null) return;

        String telefone = JOptionPane.showInputDialog(null,
                "Telefone:", "Criar Conta — Telefone", JOptionPane.PLAIN_MESSAGE);
        if (telefone == null) return;

        int termos = JOptionPane.showConfirmDialog(null,
                "Li e aceito os Termos de Uso do FishInvest.",
                "Termos de Uso",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        boolean aceitouTermos = (termos == JOptionPane.YES_OPTION);

        Cliente novoCliente = new Cliente();
        novoCliente.setNome(nome.trim());
        novoCliente.setCpf(cpf.trim());
        novoCliente.setEmail(email.trim());
        novoCliente.setSenha(senha);
        novoCliente.setTelefone(telefone.trim());

        SegurancaAcesso seguranca = new SegurancaAcesso();
        seguranca.setPinAcesso(null);
        seguranca.setBiometriaAtiva(false);
        novoCliente.setSegurancaAcesso(seguranca);

        try {
            clienteService.criarConta(novoCliente, aceitouTermos);
            JOptionPane.showMessageDialog(null,
                    "Conta criada com sucesso!\nBem-vindo(a), " + novoCliente.getNome() + "!",
                    "Conta Criada",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Erro ao Criar Conta",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void fazerLogin() {

        String email = JOptionPane.showInputDialog(null,
                "E-mail:", "Login", JOptionPane.PLAIN_MESSAGE);
        if (email == null) return;

        String senha = JOptionPane.showInputDialog(null,
                "Senha:", "Login", JOptionPane.PLAIN_MESSAGE);
        if (senha == null) return;

        try {
            clienteLogado = clienteService.efetuarLogin(email.trim(), senha);
            JOptionPane.showMessageDialog(null,
                    "Login realizado com sucesso!\nBem-vindo(a), " + clienteLogado.getNome() + ".",
                    "Login",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Erro no Login",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void realizarAporte() {

        ArrayList<CarteiraInvestimentos> carteiras;
        try {
            carteiras = carteiraService.listarPorCliente(clienteLogado);
        } catch (NegocioException | RepositorioException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao buscar carteiras: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (carteiras == null || carteiras.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Você não possui carteiras de investimento cadastradas.\n"
                  + "Entre em contato com o suporte para criar uma carteira.",
                    "Sem Carteiras",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] nomesCarteiras = new String[carteiras.size()];
        for (int i = 0; i < carteiras.size(); i++) {
            CarteiraInvestimentos c = carteiras.get(i);
            TipoInvestimento tipo  = c.getTipoInvestimento();
            nomesCarteiras[i] = String.format(
                    "%s  |  Tipo: %s  |  Saldo: R$ %,.2f",
                    c.getNomeEspecifico(),
                    tipo != null ? tipo.getNomeTipo() : "—",
                    c.getSaldoTotal() != null ? c.getSaldoTotal() : BigDecimal.ZERO);
        }

        String escolhida = (String) JOptionPane.showInputDialog(
                null,
                "Selecione a carteira para o aporte:",
                "Realizar Aporte — Carteira",
                JOptionPane.PLAIN_MESSAGE,
                null,
                nomesCarteiras,
                nomesCarteiras[0]);

        if (escolhida == null) return;

        CarteiraInvestimentos carteiraSelecionada = null;
        for (int i = 0; i < nomesCarteiras.length; i++) {
            if (nomesCarteiras[i].equals(escolhida)) {
                carteiraSelecionada = carteiras.get(i);
                break;
            }
        }

        if (carteiraSelecionada == null) return;

        String valorTexto = JOptionPane.showInputDialog(null,
                "Informe o valor do aporte (ex: 1.500,00):",
                "Realizar Aporte — Valor",
                JOptionPane.PLAIN_MESSAGE);

        if (valorTexto == null || valorTexto.isBlank()) return;

        int confirmacao = JOptionPane.showConfirmDialog(null,
                String.format(
                    "Confirma o aporte na carteira '%s'?\n\nValor digitado: %s",
                    carteiraSelecionada.getNomeEspecifico(), valorTexto),
                "Confirmar Aporte",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmacao != JOptionPane.YES_OPTION) return;

        try {
            String comprovante = transacaoService.realizarAporte(
                    clienteLogado, carteiraSelecionada, valorTexto);
            JOptionPane.showMessageDialog(null,
                    comprovante,
                    "Aporte Realizado com Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Erro no Aporte",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}