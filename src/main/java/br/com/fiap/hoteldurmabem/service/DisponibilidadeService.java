package br.com.fiap.hoteldurmabem.service;

import br.com.fiap.hoteldurmabem.model.Quarto;
import br.com.fiap.hoteldurmabem.repository.QuartoRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;


public class DisponibilidadeService {

    private final QuartoRepository quartoRepository;

    public DisponibilidadeService() {
        this(new QuartoRepository());
    }

    public DisponibilidadeService(QuartoRepository quartoRepository) {
        this.quartoRepository = quartoRepository;
    }

    public List<Quarto> consultarDisponiveis(LocalDate data) {
        if (data == null) {
            throw new IllegalArgumentException("Data e obrigatoria para o relatorio.");
        }
        try {
            return quartoRepository.listarDisponiveisNaData(data);
        } catch (SQLException e) {
            throw new RuntimeException("Erro de banco ao consultar disponibilidade: " + e.getMessage(), e);
        }
    }
}
