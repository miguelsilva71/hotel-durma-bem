package br.com.fiap.hoteldurmabem.repository;

import br.com.fiap.hoteldurmabem.model.Quarto;
import br.com.fiap.hoteldurmabem.model.TipoQuarto;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class QuartoRepository {

    private final DatabaseConfig db = DatabaseConfig.getInstance();

    public List<Quarto> listarTodos() throws SQLException {
        String sql = "SELECT id, numero, tipo, preco_diaria, ativo FROM TB_QUARTO ORDER BY tipo, numero";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Quarto> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        }
    }

    public Quarto buscarPorId(long id) throws SQLException {
        String sql = "SELECT id, numero, tipo, preco_diaria, ativo FROM TB_QUARTO WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }


    public List<Quarto> listarDisponiveisNaData(LocalDate data) throws SQLException {
        String sql = """
                SELECT q.id, q.numero, q.tipo, q.preco_diaria, q.ativo
                  FROM TB_QUARTO q
                 WHERE q.ativo = 'S'
                   AND NOT EXISTS (
                         SELECT 1 FROM TB_RESERVA r
                          WHERE r.quarto_id = q.id
                            AND r.status = 'ATIVA'
                            AND r.data_entrada <= ?
                            AND r.data_saida > ?
                       )
                 ORDER BY q.tipo, q.numero
                """;
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(data));
            ps.setDate(2, Date.valueOf(data));
            try (ResultSet rs = ps.executeQuery()) {
                List<Quarto> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
                return lista;
            }
        }
    }


    public List<Quarto> listarDisponiveisPorTipoEPeriodo(TipoQuarto tipo, LocalDate entrada, LocalDate saida)
            throws SQLException {
        String sql = """
                SELECT q.id, q.numero, q.tipo, q.preco_diaria, q.ativo
                  FROM TB_QUARTO q
                 WHERE q.ativo = 'S'
                   AND q.tipo = ?
                   AND NOT EXISTS (
                         SELECT 1 FROM TB_RESERVA r
                          WHERE r.quarto_id = q.id
                            AND r.status = 'ATIVA'
                            AND ? < r.data_saida
                            AND ? > r.data_entrada
                       )
                 ORDER BY q.numero
                """;
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tipo.name());
            ps.setDate(2, Date.valueOf(entrada));
            ps.setDate(3, Date.valueOf(saida));
            try (ResultSet rs = ps.executeQuery()) {
                List<Quarto> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
                return lista;
            }
        }
    }

    private Quarto mapear(ResultSet rs) throws SQLException {
        Quarto q = new Quarto();
        q.setId(rs.getLong("id"));
        q.setNumero(rs.getString("numero"));
        q.setTipo(TipoQuarto.fromString(rs.getString("tipo")));
        q.setPrecoDiaria(rs.getBigDecimal("preco_diaria"));
        q.setAtivo("S".equalsIgnoreCase(rs.getString("ativo")));
        return q;
    }
}
