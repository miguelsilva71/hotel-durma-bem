package br.com.fiap.hoteldurmabem.model;

import java.math.BigDecimal;
import java.util.Objects;

/** Representa um quarto fisico do hotel (linha da TB_QUARTO). */
public class Quarto {

    private Long id;
    private String numero;          // ex: "S01", "D03", "T02"
    private TipoQuarto tipo;
    private BigDecimal precoDiaria;
    private boolean ativo = true;

    public Quarto() {
    }

    public Quarto(Long id, String numero, TipoQuarto tipo, BigDecimal precoDiaria, boolean ativo) {
        this.id = id;
        this.numero = numero;
        this.tipo = tipo;
        this.precoDiaria = precoDiaria;
        this.ativo = ativo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public TipoQuarto getTipo() {
        return tipo;
    }

    public void setTipo(TipoQuarto tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getPrecoDiaria() {
        return precoDiaria;
    }

    public void setPrecoDiaria(BigDecimal precoDiaria) {
        this.precoDiaria = precoDiaria;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return String.format("Quarto{id=%s, numero='%s', tipo=%s, diaria=R$ %s, ativo=%s}",
                id, numero, tipo, precoDiaria, ativo ? "Sim" : "Não");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quarto quarto)) return false;
        return Objects.equals(id, quarto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
