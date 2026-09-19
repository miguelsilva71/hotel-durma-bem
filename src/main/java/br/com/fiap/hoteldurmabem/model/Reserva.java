package br.com.fiap.hoteldurmabem.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;


public class Reserva {

    private Long id;
    private Long quartoId;
    private String nomeHospede;
    private LocalDate dataEntrada;  // inclusiva (check-in)
    private LocalDate dataSaida;    // exclusiva (check-out: o quarto libera nesse dia)
    private BigDecimal valorTotal;
    private StatusReserva status = StatusReserva.ATIVA;

    // Objeto associado (preenchido pelo DAO em consultas com JOIN)
    private Quarto quarto;

    public Reserva() {
    }

    public Reserva(Long quartoId, String nomeHospede, LocalDate dataEntrada, LocalDate dataSaida) {
        this.quartoId = quartoId;
        this.nomeHospede = nomeHospede;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
    }


    public long getQuantidadeDiarias() {
        if (dataEntrada == null || dataSaida == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(dataEntrada, dataSaida);
    }

    public boolean isPeriodoValido() {
        return dataEntrada != null && dataSaida != null && dataSaida.isAfter(dataEntrada);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuartoId() {
        return quartoId;
    }

    public void setQuartoId(Long quartoId) {
        this.quartoId = quartoId;
    }

    public String getNomeHospede() {
        return nomeHospede;
    }

    public void setNomeHospede(String nomeHospede) {
        this.nomeHospede = nomeHospede;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public LocalDate getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(LocalDate dataSaida) {
        this.dataSaida = dataSaida;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public StatusReserva getStatus() {
        return status;
    }

    public void setStatus(StatusReserva status) {
        this.status = status;
    }

    public Quarto getQuarto() {
        return quarto;
    }

    public void setQuarto(Quarto quarto) {
        this.quarto = quarto;
    }

    @Override
    public String toString() {
        return String.format("Reserva{id=%s, quartoId=%s, hospede='%s', %s -> %s, diarias=%d, total=R$ %s, status=%s}",
                id, quartoId, nomeHospede, dataEntrada, dataSaida,
                getQuantidadeDiarias(), valorTotal, status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reserva reserva)) return false;
        return Objects.equals(id, reserva.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
