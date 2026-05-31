package br.unipar.fish.invest.repositories.interfaces;

import br.unipar.fish.invest.domains.CarteiraInvestimentos;
import java.sql.SQLException;
import java.util.ArrayList;

public interface CarteiraInvestimentosRepositoryInterface {

    CarteiraInvestimentos inserir(CarteiraInvestimentos carteira) throws SQLException;

    CarteiraInvestimentos atualizar(CarteiraInvestimentos carteira) throws SQLException;

    void deletar(Integer id) throws SQLException;

    CarteiraInvestimentos findById(Integer id) throws SQLException;

    ArrayList<CarteiraInvestimentos> listarTodos() throws SQLException;
}