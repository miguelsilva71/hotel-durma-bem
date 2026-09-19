package br.com.fiap.hoteldurmabem.service;

import br.com.fiap.hoteldurmabem.model.Reserva;
import br.com.fiap.hoteldurmabem.model.StatusReserva;
import br.com.fiap.hoteldurmabem.repository.ReservaRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


public class CheckoutService {

    private final ReservaRepository reservaRepository;

    public CheckoutService() {
        this(new ReservaRepository());
    }

    public CheckoutService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }


    public static BigDecimal calcularValor(BigDecimal diaria, LocalDate entrada, LocalDate saida) {
        if (diaria == null || entrada == null || saida == null) {
            throw new ReservaInvalidaException("Diaria e datas sao obrigatorias para o checkout.");
        }
        long dias = ChronoUnit.DAYS.between(entrada, saida);
        if (dias <= 0) {
            throw new ReservaInvalidaException("Periodo invalido para checkout.");
        }
        return diaria.multiply(BigDecimal.valueOf(dias));
    }


    public BigDecimal fazerCheckout(long reservaId) {
        try {
            Reserva reserva = reservaRepository.buscarPorId(reservaId);
            if (reserva == null) {
                throw new ReservaInvalidaException("Reserva id=" + reservaId + " nao encontrada.");
            }
            if (reserva.getStatus() != StatusReserva.ATIVA) {
                throw new ReservaInvalidaException("Somente reservas ATIVAS podem fazer checkout.");
            }
            BigDecimal total = calcularValor(
                    reserva.getQuarto().getPrecoDiaria(),
                    reserva.getDataEntrada(),
                    reserva.getDataSaida());
            int linhas = reservaRepository.registrarCheckout(reservaId, total);
            if (linhas == 0) {
                throw new ReservaInvalidaException("Checkout nao efetuado (reserva ja finalizada?).");
            }
            return total;
        } catch (SQLException e) {
            throw new RuntimeException("Erro de banco no checkout: " + e.getMessage(), e);
        }
    }
}
