package br.com.fiap.hoteldurmabem.model;

import java.math.BigDecimal;


public enum TipoQuarto {
    SIMPLES("Simples", 1, new BigDecimal("150.00")),
    DUPLO("Duplo", 2, new BigDecimal("250.00")),
    TRIPLO("Triplo", 3, new BigDecimal("350.00"));

    private final String descricao;
    private final int capacidade;
    private final BigDecimal diaria;

    TipoQuarto(String descricao, int capacidade, BigDecimal diaria) {
        this.descricao = descricao;
        this.capacidade = capacidade;
        this.diaria = diaria;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public BigDecimal getDiaria() {
        return diaria;
    }

    public static TipoQuarto fromString(String value) {
        for (TipoQuarto t : values()) {
            if (t.name().equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Tipo de quarto invalido: " + value);
    }
}
