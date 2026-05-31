package br.unipar.fish.invest.services;

import br.unipar.fish.invest.domains.CarteiraInvestimentos;
import br.unipar.fish.invest.domains.Cliente;
import br.unipar.fish.invest.domains.TipoInvestimento;
import br.unipar.fish.invest.exceptions.NegocioException;
import br.unipar.fish.invest.exceptions.RepositorioException;
import br.unipar.fish.invest.repositories.CarteiraInvestimentosRepository;
import br.unipar.fish.invest.repositories.TipoInvestimentoRepository;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

public class CarteiraInvestimentosService {

    private static final int MAX_NOME = 100;

    private final CarteiraInvestimentosRepository carteiraRepo;
    private final TipoInvestimentoRepository      tipoRepo;

    public CarteiraInvestimentosService() {
        this.carteiraRepo = new CarteiraInvestimentosRepository();
        this.tipoRepo     = new TipoInvestimentoRepository();
    }

    public CarteiraInvestimentos criarCarteira(CarteiraInvestimentos carteira,
                                               Cliente clienteLogado)
            throws NegocioException, RepositorioException {

        validarCliente(clienteLogado);

        if (carteira.getNomeEspecifico() == null
                || carteira.getNomeEspecifico().isBlank()) {
            throw new NegocioException(
                "O nome da carteira é obrigatório.");
        }

        if (carteira.getNomeEspecifico().length() > MAX_NOME) {
            throw new NegocioException(
                "O nome da carteira não pode ultrapassar "
              + MAX_NOME + " caracteres.");
        }

        validarTipoInvestimento(carteira.getTipoInvestimento());

        if (carteira.getSaldoTotal() == null) {
            carteira.setSaldoTotal(BigDecimal.ZERO);
        }
        if (carteira.getSaldoTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new NegocioException(
                "O saldo total inicial não pode ser negativo.");
        }

        if (carteira.getRendimentoAcumulado() == null) {
            carteira.setRendimentoAcumulado(BigDecimal.ZERO);
        }
        if (carteira.getRendimentoAcumulado().compareTo(BigDecimal.ZERO) < 0) {
            throw new NegocioException(
                "O rendimento acumulado inicial não pode ser negativo.");
        }

        carteira.setCliente(clienteLogado);

        try {
            return carteiraRepo.inserir(carteira);
        } catch (SQLException e) {
            throw new RepositorioException(
                "Erro ao salvar carteira no banco: " + e.getMessage(), e);
        }
    }

    public CarteiraInvestimentos atualizarCarteira(CarteiraInvestimentos carteira,
                                                   Cliente clienteLogado)
            throws NegocioException, RepositorioException {

        validarCliente(clienteLogado);

        if (carteira.getId() == null || carteira.getId() <= 0) {
            throw new NegocioException("Carteira inválida para atualização.");
        }

        if (carteira.getNomeEspecifico() == null
                || carteira.getNomeEspecifico().isBlank()) {
            throw new NegocioException(
                "O nome da carteira é obrigatório.");
        }

        if (carteira.getNomeEspecifico().length() > MAX_NOME) {
            throw new NegocioException(
                "O nome da carteira não pode ultrapassar "
              + MAX_NOME + " caracteres.");
        }

        validarTipoInvestimento(carteira.getTipoInvestimento());

        try {
            return carteiraRepo.atualizar(carteira);
        } catch (SQLException e) {
            throw new RepositorioException(
                "Erro ao atualizar carteira no banco: " + e.getMessage(), e);
        }
    }

    public void deletarCarteira(Integer id) throws NegocioException, RepositorioException {

        if (id == null || id <= 0) {
            throw new NegocioException("ID de carteira inválido.");
        }

        try {
            CarteiraInvestimentos carteira = carteiraRepo.findById(id);
            if (carteira == null) {
                throw new NegocioException("Carteira não encontrada com ID: " + id);
            }

            if (carteira.getSaldoTotal() != null
                    && carteira.getSaldoTotal().compareTo(BigDecimal.ZERO) > 0) {
                throw new NegocioException(
                    "Não é possível excluir uma carteira com saldo disponível. "
                  + "Saldo atual: R$ " + String.format("%,.2f", carteira.getSaldoTotal()));
            }

            carteiraRepo.deletar(id);

        } catch (SQLException e) {
            throw new RepositorioException(
                "Erro ao deletar carteira: " + e.getMessage(), e);
        }
    }

    public CarteiraInvestimentos buscarPorId(Integer id)
            throws NegocioException, RepositorioException {

        if (id == null || id <= 0) {
            throw new NegocioException("ID inválido.");
        }
        try {
            CarteiraInvestimentos carteira = carteiraRepo.findById(id);
            if (carteira == null) {
                throw new NegocioException("Carteira não encontrada com ID: " + id);
            }
            return carteira;
        } catch (SQLException e) {
            throw new RepositorioException(
                "Erro ao buscar carteira: " + e.getMessage(), e);
        }
    }

    public ArrayList<CarteiraInvestimentos> listarTodos() throws RepositorioException {
        try {
            return carteiraRepo.listarTodos();
        } catch (SQLException e) {
            throw new RepositorioException(
                "Erro ao listar carteiras: " + e.getMessage(), e);
        }
    }

    public ArrayList<CarteiraInvestimentos> listarPorCliente(Cliente clienteLogado)
            throws NegocioException, RepositorioException {

        validarCliente(clienteLogado);

        ArrayList<CarteiraInvestimentos> todas = listarTodos();
        ArrayList<CarteiraInvestimentos> doCliente = new ArrayList<>();

        for (CarteiraInvestimentos c : todas) {
            if (c.getCliente() != null
                    && c.getCliente().getId().equals(clienteLogado.getId())) {
                doCliente.add(c);
            }
        }
        return doCliente;
    }

    private void validarCliente(Cliente cliente) throws NegocioException {
        if (cliente == null || cliente.getId() == null || cliente.getId() <= 0) {
            throw new NegocioException(
                "Nenhum cliente autenticado. Faça login antes de continuar.");
        }
    }

    private void validarTipoInvestimento(TipoInvestimento tipo)
            throws NegocioException, RepositorioException {

        if (tipo == null || tipo.getId() == null || tipo.getId() <= 0) {
            throw new NegocioException(
                "Selecione um tipo de investimento válido.");
        }
        try {
            TipoInvestimento encontrado = tipoRepo.findById(tipo.getId());
            if (encontrado == null) {
                throw new NegocioException(
                    "Tipo de investimento não encontrado no banco.");
            }
        } catch (SQLException e) {
            throw new RepositorioException(
                "Erro ao validar tipo de investimento: " + e.getMessage(), e);
        }
    }
}