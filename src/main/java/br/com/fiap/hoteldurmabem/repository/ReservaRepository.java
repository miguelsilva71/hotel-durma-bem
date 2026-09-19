package br.com.fiap.hoteldurmabem.repository;

import br.com.fiap.hoteldurmabem.model.Quarto;
import br.com.fiap.hoteldurmabem.model.Reserva;
import br.com.fiap.hoteldurmabem.model.StatusReserva;
import br.com.fiap.hoteldurmabem.model.TipoQuarto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class ReservaRepository {

    private final DatabaseConfig db = DatabaseConfig.getInstance();


    public int contarConflitos(long quartoId, LocalDate entrada, LocalDate saida) throws SQLException {
        String sql = """
                SELECT COUNT(*)
                  FROM TB_RESERVA
                 WHERE quarto_id = ?
                   AND status = 'ATIVA'
                   AND ? < data_saida
                   AND ? > data_entrada
                """;
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, quartoId);
            ps.setDate(2, Date.valueOf(entrada));
            ps.setDate(3, Date.valueOf(saida));
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    public Reserva inserir(Reserva reserva) throws SQLException {
        String sql = """
                INSERT INTO TB_RESERVA (quarto_id, nome_hospede, data_entrada, data_saida, valor_total, status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"ID"})) {
            ps.setLong(1, reserva.getQuartoId());
            ps.setString(2, reserva.getNomeHospede());
            ps.setDate(3, Date.valueOf(reserva.getDataEntrada()));
            ps.setDate(4, Date.valueOf(reserva.getDataSaida()));
            if (reserva.getValorTotal() != null) {
                ps.setBigDecimal(5, reserva.getValorTotal());
            } else {
                ps.setNull(5, java.sql.Types.NUMERIC);
            }
            ps.setString(6, reserva.getStatus().name());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    reserva.setId(keys.getLong(1));
                }
            }
            return reserva;
        }
    }

    public Reserva buscarPorId(long id) throws SQLException {
        String sql = """
                SELECT r.id, r.quarto_id, r.nome_hospede, r.data_entrada, r.data_saida,
                       r.valor_total, r.status,
                       q.numero, q.tipo, q.preco_diaria, q.ativo
                  FROM TB_RESERVA r
                  JOIN TB_QUARTO q ON q.id = r.quarto_id
                 WHERE r.id = ?
                """;
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapearComQuarto(rs) : null;
            }
        }
    }

    public List<Reserva> listarAtivas() throws SQLException {
        String sql = """
                SELECT r.id, r.quarto_id, r.nome_hospede, r.data_entrada, r.data_saida,
                       r.valor_total, r.status,
                       q.numero, q.tipo, q.preco_diaria, q.ativo
                  FROM TB_RESERVA r
                  JOIN TB_QUARTO q ON q.id = r.quarto_id
                 WHERE r.status = 'ATIVA'
                 ORDER BY r.data_entrada
                """;
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Reserva> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapearComQuarto(rs));
            }
            return lista;
        }
    }

    /** Marca checkout: atualiza valor_total e status. Retorna linhas afetadas. */
    public int registrarCheckout(long reservaId, BigDecimal valorTotal) throws SQLException {
        String sql = "UPDATE TB_RESERVA SET valor_total = ?, status = 'CHECKOUT' WHERE id = ? AND status = 'ATIVA'";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, valorTotal);
            ps.setLong(2, reservaId);
            return ps.executeUpdate();
        }
    }

    public int cancelar(long reservaId) throws SQLException {
        String sql = "UPDATE TB_RESERVA SET status = 'CANCELADA' WHERE id = ? AND status = 'ATIVA'";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, reservaId);
            return ps.executeUpdate();
        }
    }

    private Reserva mapearComQuarto(ResultSet rs) throws SQLException {
        Reserva r = new Reserva();
        r.setId(rs.getLong("id"));
        r.setQuartoId(rs.getLong("quarto_id"));
        r.setNomeHospede(rs.getString("nome_hospede"));
        r.setDataEntrada(rs.getDate("data_entrada").toLocalDate());
        r.setDataSaida(rs.getDate("data_saida").toLocalDate());
        r.setValorTotal(rs.getBigDecimal("valor_total"));
        r.setStatus(StatusReserva.valueOf(rs.getString("status")));

        Quarto q = new Quarto();
        q.setId(rs.getLong("quarto_id"));
        q.setNumero(rs.getString("numero"));
        q.setTipo(TipoQuarto.fromString(rs.getString("tipo")));
        q.setPrecoDiaria(rs.getBigDecimal("preco_diaria"));
        q.setAtivo("S".equalsIgnoreCase(rs.getString("ativo")));
        r.setQuarto(q);
        return r;
    }
}
