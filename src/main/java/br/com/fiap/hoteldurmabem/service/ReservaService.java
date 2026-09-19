package br.com.fiap.hoteldurmabem.service;

import br.com.fiap.hoteldurmabem.model.Quarto;
import br.com.fiap.hoteldurmabem.model.Reserva;
import br.com.fiap.hoteldurmabem.model.StatusReserva;
import br.com.fiap.hoteldurmabem.model.TipoQuarto;
import br.com.fiap.hoteldurmabem.repository.QuartoRepository;
import br.com.fiap.hoteldurmabem.repository.ReservaRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;


public class ReservaService {

    private final QuartoRepository quartoRepository;
    private final ReservaRepository reservaRepository;

    public ReservaService() {
        this(new QuartoRepository(), new ReservaRepository());
    }

    public ReservaService(QuartoRepository quartoRepository, ReservaRepository reservaRepository) {
        this.quartoRepository = quartoRepository;
        this.reservaRepository = reservaRepository;
    }


    public Reserva reservar(long quartoId, String nomeHospede, LocalDate entrada, LocalDate saida) {
        validarDados(nomeHospede, entrada, saida);
        try {
            Quarto quarto = quartoRepository.buscarPorId(quartoId);
            if (quarto == null || !quarto.isAtivo()) {
                throw new ReservaInvalidaException("Quarto id=" + quartoId + " nao existe ou esta inativo.");
            }
            int conflitos = reservaRepository.contarConflitos(quartoId, entrada, saida);
            if (conflitos > 0) {
                throw new QuartoIndisponivelException(
                        "Quarto " + quarto.getNumero() + " indisponivel de " + entrada + " a " + saida + ".");
            }
            Reserva reserva = new Reserva(quartoId, nomeHospede.trim(), entrada, saida);
            reserva.setStatus(StatusReserva.ATIVA);
            // Valor estimado no ato da reserva (sera confirmado no checkout)
            reserva.setValorTotal(CheckoutService.calcularValor(
                    quarto.getPrecoDiaria(), entrada, saida));
            return reservaRepository.inserir(reserva);
        } catch (SQLException e) {
            throw new RuntimeException("Erro de banco ao reservar: " + e.getMessage(), e);
        }
    }


    public Reserva reservarPorTipo(TipoQuarto tipo, String nomeHospede, LocalDate entrada, LocalDate saida) {
        validarDados(nomeHospede, entrada, saida);
        try {
            List<Quarto> livres = quartoRepository.listarDisponiveisPorTipoEPeriodo(tipo, entrada, saida);
            if (livres.isEmpty()) {
                throw new QuartoIndisponivelException(
                        "Nenhum quarto " + tipo + " disponivel de " + entrada + " a " + saida + ".");
            }
            Quarto escolhido = livres.get(0);
            return reservar(escolhido.getId(), nomeHospede, entrada, saida);
        } catch (SQLException e) {
            throw new RuntimeException("Erro de banco ao reservar por tipo: " + e.getMessage(), e);
        }
    }

    private void validarDados(String nomeHospede, LocalDate entrada, LocalDate saida) {
        if (nomeHospede == null || nomeHospede.isBlank()) {
            throw new ReservaInvalidaException("Nome do hospede e obrigatorio.");
        }
        if (entrada == null || saida == null) {
            throw new ReservaInvalidaException("Datas de entrada e saida sao obrigatorias.");
        }
        if (!saida.isAfter(entrada)) {
            throw new ReservaInvalidaException("Data de saida deve ser posterior a data de entrada.");
        }
    }
}
