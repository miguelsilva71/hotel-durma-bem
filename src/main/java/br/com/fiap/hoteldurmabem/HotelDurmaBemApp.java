//Igor Rodrigues de Santana — RM570651
//Diego Gomes Gonçalves de Lima — RM570335
//Miguel Silva — RM572019
//Rafael Santos Mendonça Costa — RM572368

package br.com.fiap.hoteldurmabem;

import br.com.fiap.hoteldurmabem.model.Quarto;
import br.com.fiap.hoteldurmabem.model.Reserva;
import br.com.fiap.hoteldurmabem.model.TipoQuarto;
import br.com.fiap.hoteldurmabem.repository.DatabaseConfig;
import br.com.fiap.hoteldurmabem.repository.QuartoRepository;
import br.com.fiap.hoteldurmabem.repository.ReservaRepository;
import br.com.fiap.hoteldurmabem.service.CheckoutService;
import br.com.fiap.hoteldurmabem.service.DisponibilidadeService;
import br.com.fiap.hoteldurmabem.service.ReservaService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;


public class HotelDurmaBemApp {

    private static final DateTimeFormatter FORMATO_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final ReservaService reservaService = new ReservaService();
    private final DisponibilidadeService disponibilidadeService = new DisponibilidadeService();
    private final CheckoutService checkoutService = new CheckoutService();
    private final ReservaRepository reservaRepository = new ReservaRepository();
    private final QuartoRepository quartoRepository = new QuartoRepository();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        HotelDurmaBemApp app = new HotelDurmaBemApp();
        app.verificarConexao();
        app.exibirMenu();
    }

    private void verificarConexao() {
        System.out.println("=== Hotel Durma Bem ===");
        DatabaseConfig db = DatabaseConfig.getInstance();
        System.out.println("Conectando em: " + db.getUrl() + " usuario=" + db.getUser());
        if (db.testConnection()) {
            System.out.println("Conexao Oracle OK!\n");
        } else {
            System.out.println("AVISO: sem conexao. Ajuste application.properties ou variaveis DB_URL/DB_USER/DB_PASSWORD.\n");
        }
    }

    private void exibirMenu() {
        int opcao;
        do {
            System.out.println("""
                    ---------- MENU ----------
                    1 - Reservar quarto (por tipo)
                    2 - Relatorio de quartos disponiveis (por data)
                    3 - Checkout (por id da reserva)
                    4 - Listar reservas ativas
                    5 - Listar todos os quartos
                    0 - Sair
                    Escolha:\s""");
            String linha = scanner.nextLine();
            opcao = linha.isBlank() ? -1 : Integer.parseInt(linha.trim());
            try {
                switch (opcao) {
                    case 1 -> reservar();
                    case 2 -> relatorioDisponiveis();
                    case 3 -> checkout();
                    case 4 -> listarAtivas();
                    case 5 -> listarQuartos();
                    case 0 -> System.out.println("Saindo...");
                    default -> System.out.println("Opcao invalida.");
                }
            } catch (Exception e) {
                System.out.println("ERRO: " + e.getMessage());
            }
            System.out.println();
        } while (opcao != 0);
    }

    private void reservar() {
        System.out.print("Nome do hospede: ");
        String nome = scanner.nextLine();
        System.out.print("Tipo (SIMPLES/DUPLO/TRIPLO): ");
        TipoQuarto tipo = TipoQuarto.fromString(scanner.nextLine());
        System.out.print("Data entrada (dd/MM/yyyy): ");
        LocalDate entrada = parseData(scanner.nextLine());
        System.out.print("Data saida (dd/MM/yyyy): ");
        LocalDate saida = parseData(scanner.nextLine());

        Reserva r = reservaService.reservarPorTipo(tipo, nome, entrada, saida);
        System.out.println("Reserva criada com sucesso! " + r);
        System.out.println("Quarto reservado. Valor estimado: R$ " + r.getValorTotal());
    }

    private void relatorioDisponiveis() {
        System.out.print("Data (dd/MM/yyyy): ");
        LocalDate data = parseData(scanner.nextLine());
        List<Quarto> livres = disponibilidadeService.consultarDisponiveis(data);
        System.out.println("Quartos disponiveis em " + data.format(FORMATO_BR) + ": " + livres.size());
        for (Quarto q : livres) {
            System.out.printf("  %s | %s | diaria R$ %s%n", q.getNumero(), q.getTipo(), q.getPrecoDiaria());
        }
    }

    private void checkout() {
        System.out.print("ID da reserva: ");
        long id = Long.parseLong(scanner.nextLine().trim());
        var total = checkoutService.fazerCheckout(id);
        System.out.println("Checkout realizado! Valor cobrado: R$ " + total);
    }

    private void listarAtivas() throws Exception {
        List<Reserva> ativas = reservaRepository.listarAtivas();
        System.out.println("Reservas ativas: " + ativas.size());
        for (Reserva r : ativas) {
            System.out.printf("  id=%d | quarto=%s (%s) | %s | %s -> %s | R$ %s%n",
                    r.getId(), r.getQuarto().getNumero(), r.getQuarto().getTipo(),
                    r.getNomeHospede(), r.getDataEntrada().format(FORMATO_BR),
                    r.getDataSaida().format(FORMATO_BR), r.getValorTotal());
        }
    }

    private void listarQuartos() throws Exception {
        for (Quarto q : quartoRepository.listarTodos()) {
            System.out.printf("  id=%d | %s | %s | diaria R$ %s%n",
                    q.getId(), q.getNumero(), q.getTipo(), q.getPrecoDiaria());
        }
    }

    private static LocalDate parseData(String texto) {
        try {
            return LocalDate.parse(texto.trim(), FORMATO_BR);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data invalida. Use o formato dd/MM/yyyy. Ex: 20/09/2026");
        }
    }
}
