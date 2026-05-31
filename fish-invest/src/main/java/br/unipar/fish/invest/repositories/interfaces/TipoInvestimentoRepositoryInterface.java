package br.unipar.fish.invest.repositories.interfaces;

import br.unipar.fish.invest.domains.TipoInvestimento;
import java.sql.SQLException;
import java.util.ArrayList;

public interface TipoInvestimentoRepositoryInterface {

    TipoInvestimento inserir(TipoInvestimento tipo) throws SQLException;

    TipoInvestimento atualizar(TipoInvestimento tipo) throws SQLException;

    void deletar(Integer id) throws SQLException;

    TipoInvestimento findById(Integer id) throws SQLException;

    ArrayList<TipoInvestimento> listarTodos() throws SQLException;
}
