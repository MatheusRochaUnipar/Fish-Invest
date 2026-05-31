package br.unipar.fish.invest.repositories.interfaces;

import br.unipar.fish.invest.domains.TransacoesEDepositos;
import java.sql.SQLException;
import java.util.ArrayList;

public interface TransacoesEDepositosRepositoryInterface {

    TransacoesEDepositos inserir(TransacoesEDepositos transacao) throws SQLException;

    TransacoesEDepositos atualizar(TransacoesEDepositos transacao) throws SQLException;

    void deletar(Integer id) throws SQLException;

    TransacoesEDepositos findById(Integer id) throws SQLException;

    ArrayList<TransacoesEDepositos> listarTodos() throws SQLException;
}
