package br.unipar.fish.invest.repositories;

import br.unipar.fish.invest.domains.TipoInvestimento;
import br.unipar.fish.invest.infraescture.ConnectionFactory;
import br.unipar.fish.invest.repositories.interfaces.TipoInvestimentoRepositoryInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TipoInvestimentoRepository implements TipoInvestimentoRepositoryInterface {

    private static final String INSERT =
            "INSERT INTO tipo_investimento (nome_tipo) VALUES (?);";

    private static final String UPDATE =
            "UPDATE tipo_investimento SET nome_tipo = ? WHERE id_tipo_investimento = ?;";

    private static final String DELETE =
            "DELETE FROM tipo_investimento WHERE id_tipo_investimento = ?;";

    private static final String FIND_BY_ID =
            "SELECT id_tipo_investimento, nome_tipo "
            + "FROM tipo_investimento WHERE id_tipo_investimento = ?;";

    private static final String FIND_ALL =
            "SELECT id_tipo_investimento, nome_tipo "
            + "FROM tipo_investimento ORDER BY nome_tipo;";

    @Override
    public TipoInvestimento inserir(TipoInvestimento tipo) throws SQLException {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = new ConnectionFactory().getConnection();
            pstm = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            pstm.setString(1, tipo.getNomeTipo());
            pstm.executeUpdate();
            rs = pstm.getGeneratedKeys();
            if (rs.next()) tipo.setId(rs.getInt(1));
        } finally {
            if (rs   != null) rs.close();
            if (pstm != null) pstm.close();
            if (conn != null) conn.close();
        }
        return tipo;
    }

    @Override
    public TipoInvestimento atualizar(TipoInvestimento tipo) throws SQLException {
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = new ConnectionFactory().getConnection();
            pstm = conn.prepareStatement(UPDATE);
            pstm.setString(1, tipo.getNomeTipo());
            pstm.setInt(2, tipo.getId());
            pstm.executeUpdate();
        } finally {
            if (pstm != null) pstm.close();
            if (conn != null) conn.close();
        }
        return tipo;
    }

    @Override
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

    @Override
    public TipoInvestimento findById(Integer id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        TipoInvestimento tipo = null;
        try {
            conn = new ConnectionFactory().getConnection();
            pstm = conn.prepareStatement(FIND_BY_ID);
            pstm.setInt(1, id);
            rs = pstm.executeQuery();
            if (rs.next()) {
                tipo = new TipoInvestimento();
                tipo.setId(rs.getInt("id_tipo_investimento"));
                tipo.setNomeTipo(rs.getString("nome_tipo"));
            }
        } finally {
            if (rs   != null) rs.close();
            if (pstm != null) pstm.close();
            if (conn != null) conn.close();
        }
        return tipo;
    }

    @Override
    public ArrayList<TipoInvestimento> listarTodos() throws SQLException {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ArrayList<TipoInvestimento> lista = new ArrayList<>();
        try {
            conn = new ConnectionFactory().getConnection();
            pstm = conn.prepareStatement(FIND_ALL);
            rs = pstm.executeQuery();
            while (rs.next()) {
                TipoInvestimento tipo = new TipoInvestimento();
                tipo.setId(rs.getInt("id_tipo_investimento"));
                tipo.setNomeTipo(rs.getString("nome_tipo"));
                lista.add(tipo);
            }
        } finally {
            if (rs   != null) rs.close();
            if (pstm != null) pstm.close();
            if (conn != null) conn.close();
        }
        return lista;
    }
}