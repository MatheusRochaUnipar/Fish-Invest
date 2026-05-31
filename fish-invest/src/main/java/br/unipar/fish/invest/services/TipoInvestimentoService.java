package br.unipar.fish.invest.services;

import br.unipar.fish.invest.domains.TipoInvestimento;
import br.unipar.fish.invest.exceptions.NegocioException;
import br.unipar.fish.invest.exceptions.RepositorioException;
import br.unipar.fish.invest.repositories.TipoInvestimentoRepository;

import java.sql.SQLException;
import java.util.ArrayList;

public class TipoInvestimentoService {

    private static final int MAX_NOME = 80;

    private final TipoInvestimentoRepository tipoRepo;

    public TipoInvestimentoService() {
        this.tipoRepo = new TipoInvestimentoRepository();
    }

    public TipoInvestimento inserir(TipoInvestimento tipo)
            throws NegocioException, RepositorioException {

        validarNome(tipo);
        validarNomeDuplicado(tipo.getNomeTipo(), null);

        try {
            return tipoRepo.inserir(tipo);
        } catch (SQLException e) {
            throw new RepositorioException(
                "Erro ao salvar tipo de investimento: " + e.getMessage(), e);
        }
    }

    public TipoInvestimento atualizar(TipoInvestimento tipo)
            throws NegocioException, RepositorioException {

        if (tipo.getId() == null || tipo.getId() <= 0) {
            throw new NegocioException("Tipo de investimento inválido para atualização.");
        }

        validarNome(tipo);
        validarNomeDuplicado(tipo.getNomeTipo(), tipo.getId());

        try {
            return tipoRepo.atualizar(tipo);
        } catch (SQLException e) {
            throw new RepositorioException(
                "Erro ao atualizar tipo de investimento: " + e.getMessage(), e);
        }
    }

    public void deletar(Integer id) throws NegocioException, RepositorioException {

        if (id == null || id <= 0) {
            throw new NegocioException("ID de tipo de investimento inválido.");
        }

        try {
            TipoInvestimento tipo = tipoRepo.findById(id);
            if (tipo == null) {
                throw new NegocioException(
                    "Tipo de investimento não encontrado com ID: " + id);
            }
            tipoRepo.deletar(id);
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("foreign key")) {
                throw new NegocioException(
                    "Não é possível excluir este tipo pois existem carteiras vinculadas a ele.");
            }
            throw new RepositorioException(
                "Erro ao deletar tipo de investimento: " + e.getMessage(), e);
        }
    }

    public TipoInvestimento buscarPorId(Integer id)
            throws NegocioException, RepositorioException {

        if (id == null || id <= 0) {
            throw new NegocioException("ID inválido.");
        }
        try {
            TipoInvestimento tipo = tipoRepo.findById(id);
            if (tipo == null) {
                throw new NegocioException(
                    "Tipo de investimento não encontrado com ID: " + id);
            }
            return tipo;
        } catch (SQLException e) {
            throw new RepositorioException(
                "Erro ao buscar tipo de investimento: " + e.getMessage(), e);
        }
    }

    public ArrayList<TipoInvestimento> listarTodos() throws RepositorioException {
        try {
            return tipoRepo.listarTodos();
        } catch (SQLException e) {
            throw new RepositorioException(
                "Erro ao listar tipos de investimento: " + e.getMessage(), e);
        }
    }

    private void validarNome(TipoInvestimento tipo) throws NegocioException {
        if (tipo.getNomeTipo() == null || tipo.getNomeTipo().isBlank()) {
            throw new NegocioException("O nome do tipo de investimento é obrigatório.");
        }
        if (tipo.getNomeTipo().length() > MAX_NOME) {
            throw new NegocioException(
                "O nome não pode ultrapassar " + MAX_NOME + " caracteres.");
        }
    }

    private void validarNomeDuplicado(String nome, Integer idIgnorar)
            throws NegocioException, RepositorioException {
        try {
            ArrayList<TipoInvestimento> todos = tipoRepo.listarTodos();
            for (TipoInvestimento t : todos) {
                if (t.getNomeTipo().equalsIgnoreCase(nome.trim())) {
                    if (idIgnorar != null && t.getId().equals(idIgnorar)) continue;
                    throw new NegocioException(
                        "Já existe um tipo de investimento com o nome '"
                        + nome + "'.");
                }
            }
        } catch (SQLException e) {
            throw new RepositorioException(
                "Erro ao verificar duplicidade: " + e.getMessage(), e);
        }
    }
}

