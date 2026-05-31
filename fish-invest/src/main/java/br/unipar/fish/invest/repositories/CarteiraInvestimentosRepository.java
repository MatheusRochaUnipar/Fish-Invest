package br.unipar.fish.invest.repositories;

import br.unipar.fish.invest.domains.CarteiraInvestimentos;
import br.unipar.fish.invest.domains.Cliente;
import br.unipar.fish.invest.domains.SegurancaAcesso;
import br.unipar.fish.invest.domains.TipoInvestimento;
import br.unipar.fish.invest.infraescture.ConnectionFactory;
import br.unipar.fish.invest.repositories.interfaces.CarteiraInvestimentosRepositoryInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CarteiraInvestimentosRepository implements CarteiraInvestimentosRepositoryInterface{

    private static final String INSERT =
            "INSERT INTO carteira_investimentos (id_cliente, id_tipo_investimento, "
            + "nome_especifico, saldo_total, rendimento_acumulado) "
            + "VALUES (?, ?, ?, ?, ?);";

    private static final String UPDATE =
            "UPDATE carteira_investimentos SET id_cliente = ?, id_tipo_investimento = ?, "
            + "nome_especifico = ?, saldo_total = ?, rendimento_acumulado = ? "
            + "WHERE id_carteira = ?;";

    private static final String DELETE =
            "DELETE FROM carteira_investimentos WHERE id_carteira = ?;";

    private static final String FIND_BY_ID =
            "SELECT ci.id_carteira, ci.nome_especifico, ci.saldo_total, ci.rendimento_acumulado, "
            + "c.id_cliente, c.nome, c.email, c.senha, c.telefone, "
            + "c.pin_acesso, c.biometria_ativada, c.data_cadastro, "
            + "t.id_tipo_investimento, t.nome_tipo "
            + "FROM carteira_investimentos ci "
            + "INNER JOIN cliente c ON ci.id_cliente = c.id_cliente "
            + "INNER JOIN tipo_investimento t ON ci.id_tipo_investimento = t.id_tipo_investimento "
            + "WHERE ci.id_carteira = ?;";

    private static final String FIND_ALL =
            "SELECT ci.id_carteira, ci.nome_especifico, ci.saldo_total, ci.rendimento_acumulado, "
            + "c.id_cliente, c.nome, c.email, c.senha, c.telefone, "
            + "c.pin_acesso, c.biometria_ativada, c.data_cadastro, "
            + "t.id_tipo_investimento, t.nome_tipo "
            + "FROM carteira_investimentos ci "
            + "INNER JOIN cliente c ON ci.id_cliente = c.id_cliente "
            + "INNER JOIN tipo_investimento t ON ci.id_tipo_investimento = t.id_tipo_investimento "
            + "ORDER BY c.nome;";

    public CarteiraInvestimentos inserir(CarteiraInvestimentos carteira) throws SQLException {

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            conn = new ConnectionFactory().getConnection();

            pstm = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            pstm.setInt(1, carteira.getCliente().getId());
            pstm.setInt(2, carteira.getTipoInvestimento().getId());
            pstm.setString(3, carteira.getNomeEspecifico());
            pstm.setBigDecimal(4, carteira.getSaldoTotal());
            pstm.setBigDecimal(5, carteira.getRendimentoAcumulado());

            pstm.executeUpdate();

            rs = pstm.getGeneratedKeys();
            if (rs.next()) carteira.setId(rs.getInt(1));

        } finally {
            if (rs != null) rs.close();
            if (pstm != null) pstm.close();
            if (conn != null) conn.close();
        }

        return carteira;
    }

    public CarteiraInvestimentos atualizar(CarteiraInvestimentos carteira) throws SQLException {

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = new ConnectionFactory().getConnection();

            pstm = conn.prepareStatement(UPDATE);
            pstm.setInt(1, carteira.getCliente().getId());
            pstm.setInt(2, carteira.getTipoInvestimento().getId());
            pstm.setString(3, carteira.getNomeEspecifico());
            pstm.setBigDecimal(4, carteira.getSaldoTotal());
            pstm.setBigDecimal(5, carteira.getRendimentoAcumulado());
            pstm.setInt(6, carteira.getId());

            pstm.executeUpdate();

        } finally {
            if (pstm != null) pstm.close();
            if (conn != null) conn.close();
        }

        return carteira;
    }

    public void deletar(Integer id) throws SQLException {

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = new ConnectionFactory().getConnection();

            pstm = conn.prepareStatement(DELETE);
            pstm.setInt(1, id);

            pstm.executeUpdate();

        } finally {
            if (pstm != null) pstm.close();
            if (conn != null) conn.close();
        }
    }

    public CarteiraInvestimentos findById(Integer id) throws SQLException {

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        CarteiraInvestimentos carteira = null;

        try {
            conn = new ConnectionFactory().getConnection();

            pstm = conn.prepareStatement(FIND_BY_ID);
            pstm.setInt(1, id);

            rs = pstm.executeQuery();

            if (rs.next()) {
                carteira = montarCarteira(rs);
            }

        } finally {
            if (rs != null) rs.close();
            if (pstm != null) pstm.close();
            if (conn != null) conn.close();
        }

        return carteira;
    }

    public ArrayList<CarteiraInvestimentos> listarTodos() throws SQLException {

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ArrayList<CarteiraInvestimentos> lista = new ArrayList<>();

        try {
            conn = new ConnectionFactory().getConnection();

            pstm = conn.prepareStatement(FIND_ALL);

            rs = pstm.executeQuery();

            while (rs.next()) {
                lista.add(montarCarteira(rs));
            }

        } finally {
            if (rs != null) rs.close();
            if (pstm != null) pstm.close();
            if (conn != null) conn.close();
        }

        return lista;
    }

    private CarteiraInvestimentos montarCarteira(ResultSet rs) throws SQLException {

        SegurancaAcesso seguranca = new SegurancaAcesso();
        seguranca.setPinAcesso(rs.getString("pin_acesso"));
        seguranca.setBiometriaAtiva(rs.getBoolean("biometria_ativada"));

        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("id_cliente"));
        cliente.setNome(rs.getString("nome"));
        cliente.setEmail(rs.getString("email"));
        cliente.setSenha(rs.getString("senha"));
        cliente.setTelefone(rs.getString("telefone"));
        cliente.setDataCadastro(rs.getDate("data_cadastro").toLocalDate());
        cliente.setSegurancaAcesso(seguranca);

        TipoInvestimento tipo = new TipoInvestimento();
        tipo.setId(rs.getInt("id_tipo_investimento"));
        tipo.setNomeTipo(rs.getString("nome_tipo"));

        CarteiraInvestimentos carteira = new CarteiraInvestimentos();
        carteira.setId(rs.getInt("id_carteira"));
        carteira.setNomeEspecifico(rs.getString("nome_especifico"));
        carteira.setSaldoTotal(rs.getBigDecimal("saldo_total"));
        carteira.setRendimentoAcumulado(rs.getBigDecimal("rendimento_acumulado"));
        carteira.setCliente(cliente);
        carteira.setTipoInvestimento(tipo);

        return carteira;
    }
}