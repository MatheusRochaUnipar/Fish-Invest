package br.unipar.fish.invest.repositories.interfaces;

import br.unipar.fish.invest.domains.Cliente;
import java.sql.SQLException;
import java.util.ArrayList;


public interface ClienteRepositoryInterface {

    Cliente inserir(Cliente cliente) throws SQLException;

    Cliente atualizar(Cliente cliente) throws SQLException;

    void deletar(Integer id) throws SQLException;

    Cliente findById(Integer id) throws SQLException;

    Cliente findByEmail(String email) throws SQLException;

    boolean existsByEmail(String email) throws SQLException;

    boolean existsByCpf(String cpf) throws SQLException;

    ArrayList<Cliente> listarTodos() throws SQLException;
}

