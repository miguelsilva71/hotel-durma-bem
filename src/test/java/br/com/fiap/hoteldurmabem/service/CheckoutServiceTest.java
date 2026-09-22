package br.com.fiap.hoteldurmabem.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class CheckoutServiceTest {

    @Test
    void deveCobrar3Diarias() {
        BigDecimal total = CheckoutService.calcularValor(
                new BigDecimal("150.00"),
                LocalDate.of(2026, 9, 20),
                LocalDate.of(2026, 9, 23));
        assertEquals(new BigDecimal("450.00"), total);
    }

    @Test
    void entradaIgualSaidaDeveFalhar() {
        assertThrows(Exception.class, () -> CheckoutService.calcularValor(
                new BigDecimal("250.00"),
                LocalDate.of(2026, 9, 20),
                LocalDate.of(2026, 9, 20)));
    }

    @Test
    void umaDiariaDuplo() {
        BigDecimal total = CheckoutService.calcularValor(
                new BigDecimal("250.00"),
                LocalDate.of(2026, 9, 20),
                LocalDate.of(2026, 9, 21));
        assertEquals(new BigDecimal("250.00"), total);
    }
}
