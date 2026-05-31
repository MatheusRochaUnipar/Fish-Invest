package br.unipar.fish.invest.repositories;

import br.unipar.fish.invest.domains.CarteiraInvestimentos;
import br.unipar.fish.invest.domains.Cliente;
import br.unipar.fish.invest.domains.SegurancaAcesso;
import br.unipar.fish.invest.domains.TipoInvestimento;
import br.unipar.fish.invest.domains.TransacoesEDepositos;
import br.unipar.fish.invest.enums.StatusOperacao;
import br.unipar.fish.invest.enums.TipoOperacao;
import br.unipar.fish.invest.infraescture.ConnectionFactory;
import br.unipar.fish.invest.repositories.interfaces.TransacoesEDepositosRepositoryInterface;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TransacoesEDepositosRepository implements TransacoesEDepositosRepositoryInterface {

    private static final String INSERT =
            "INSERT INTO transacoes_e_depositos (id_cliente, id_carteira, "
            + "valor_transacao, tipo_operacao, metodo_pagamento, "
            + "data_transacao, status_operacao) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?);";

    private static final String UPDATE =
            "UPDATE transacoes_e_depositos SET id_cliente = ?, id_carteira = ?, "
            + "valor_transacao = ?, tipo_operacao = ?, metodo_pagamento = ?, "
            + "data_transacao = ?, status_operacao = ? "
            + "WHERE id_transacao = ?;";

    private static final String DELETE =
            "DELETE FROM transacoes_e_depositos WHERE id_transacao = ?;";

    private static final String FIND_BY_ID =
            "SELECT t.id_transacao, t.valor_transacao, t.tipo_operacao, "
            + "t.metodo_pagamento, t.data_transacao, t.status_operacao, "
            + "c.id_cliente, c.nome, c.email, c.senha, c.telefone, "
            + "c.pin_acesso, c.biometria_ativada, c.data_cadastro, "
            + "ci.id_carteira, ci.nome_especifico, ci.saldo_total, ci.rendimento_acumulado, "
            + "ti.id_tipo_investimento, ti.nome_tipo "
            + "FROM transacoes_e_depositos t "
            + "INNER JOIN cliente c ON t.id_cliente = c.id_cliente "
            + "INNER JOIN carteira_investimentos ci ON t.id_carteira = ci.id_carteira "
            + "INNER JOIN tipo_investimento ti ON ci.id_tipo_investimento = ti.id_tipo_investimento "
            + "WHERE t.id_transacao = ?;";

    private static final String FIND_ALL =
            "SELECT t.id_transacao, t.valor_transacao, t.tipo_operacao, "
            + "t.metodo_pagamento, t.data_transacao, t.status_operacao, "
            + "c.id_cliente, c.nome, c.email, c.senha, c.telefone, "
            + "c.pin_acesso, c.biometria_ativada, c.data_cadastro, "
            + "ci.id_carteira, ci.nome_especifico, ci.saldo_total, ci.rendimento_acumulado, "
            + "ti.id_tipo_investimento, ti.nome_tipo "
            + "FROM transacoes_e_depositos t "
            + "INNER JOIN cliente c ON t.id_cliente = c.id_cliente "
            + "INNER JOIN carteira_investimentos ci ON t.id_carteira = ci.id_carteira "
            + "INNER JOIN tipo_investimento ti ON ci.id_tipo_investimento = ti.id_tipo_investimento "
            + "ORDER BY t.data_transacao DESC;";


    @Override
    public TransacoesEDepositos inserir(TransacoesEDepositos transacao) throws SQLException {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = new ConnectionFactory().getConnection();
            pstm = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            pstm.setInt(1, transacao.getCliente().getId());
            pstm.setInt(2, transacao.getCarteiraInvestimentos().getId());
            pstm.setBigDecimal(3, transacao.getValorTransacao());
            pstm.setString(4, transacao.getTipoOperacao().name());
            pstm.setString(5, transacao.getMetodoPagamento());
            pstm.setDate(6, Date.valueOf(transacao.getDataTransacao()));
            pstm.setString(7, transacao.getStatusOperacao().name());
            pstm.executeUpdate();
            rs = pstm.getGeneratedKeys();
            if (rs.next()) transacao.setId(rs.getInt(1));
        } finally {
            if (rs   != null) rs.close();
            if (pstm != null) pstm.close();
            if (conn != null) conn.close();
        }
        return transacao;
    }


    @Override
    public TransacoesEDepositos atualizar(TransacoesEDepositos transacao) throws SQLException {
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = new ConnectionFactory().getConnection();
            pstm = conn.prepareStatement(UPDATE);
            pstm.setInt(1, transacao.getCliente().getId());
            pstm.setInt(2, transacao.getCarteiraInvestimentos().getId());
            pstm.setBigDecimal(3, transacao.getValorTransacao());
            pstm.setString(4, transacao.getTipoOperacao().name());
            pstm.setString(5, transacao.getMetodoPagamento());
            pstm.setDate(6, Date.valueOf(transacao.getDataTransacao()));
            pstm.setString(7, transacao.getStatusOperacao().name());
            pstm.setInt(8, transacao.getId());
            pstm.executeUpdate();
        } finally {
            if (pstm != null) pstm.close();
            if (conn != null) conn.close();
        }
        return transacao;
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
    public TransacoesEDepositos findById(Integer id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        TransacoesEDepositos transacao = null;
        try {
            conn = new ConnectionFactory().getConnection();
            pstm = conn.prepareStatement(FIND_BY_ID);
            pstm.setInt(1, id);
            rs = pstm.executeQuery();
            if (rs.next()) transacao = montarTransacao(rs);
        } finally {
            if (rs   != null) rs.close();
            if (pstm != null) pstm.close();
            if (conn != null) conn.close();
        }
        return transacao;
    }

    @Override
    public ArrayList<TransacoesEDepositos> listarTodos() throws SQLException {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ArrayList<TransacoesEDepositos> lista = new ArrayList<>();
        try {
            conn = new ConnectionFactory().getConnection();
            pstm = conn.prepareStatement(FIND_ALL);
            rs = pstm.executeQuery();
            while (rs.next()) lista.add(montarTransacao(rs));
        } finally {
            if (rs   != null) rs.close();
            if (pstm != null) pstm.close();
            if (conn != null) conn.close();
        }
        return lista;
    }

    private TransacoesEDepositos montarTransacao(ResultSet rs) throws SQLException {

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

        TransacoesEDepositos transacao = new TransacoesEDepositos();
        transacao.setId(rs.getInt("id_transacao"));
        transacao.setValorTransacao(rs.getBigDecimal("valor_transacao"));
        transacao.setTipoOperacao(TipoOperacao.valueOf(rs.getString("tipo_operacao")));
        transacao.setMetodoPagamento(rs.getString("metodo_pagamento"));
        transacao.setDataTransacao(rs.getDate("data_transacao").toLocalDate());
        transacao.setStatusOperacao(StatusOperacao.valueOf(rs.getString("status_operacao")));
        transacao.setCliente(cliente);
        transacao.setCarteiraInvestimentos(carteira);

        return transacao;
    }
}