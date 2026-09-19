# 🏨 Hotel Durma Bem — Sistema de Reservas
👥 Grupo

Igor Rodrigues de Santana — RM570651
Diego Gomes Gonçalves de Lima — RM570335
Miguel Silva — RM572019
Rafael Santos Mendonça Costa — RM572368

> Sistema de controle de reserva de quartos com persistência em **Oracle**.
> Projeto da disciplina de Java — FIAP.

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Oracle](https://img.shields.io/badge/Oracle-Database-red?style=for-the-badge&logo=oracle)
![Maven](https://img.shields.io/badge/Maven-Build-blue?style=for-the-badge&logo=apachemaven)
![Status](https://img.shields.io/badge/Status-Funcionando-green?style=for-the-badge)

---

## ✨ Funcionalidades

| # | Funcionalidade | Onde está |
|---|---|---|
| 1️⃣ | **Reservar quarto** — só permite se há disponibilidade. A entrada pode coincidir com a saída de outra reserva | `service/ReservaService.java` |
| 2️⃣ | **Relatório de disponíveis** — lista todos os quartos livres na data informada | `service/DisponibilidadeService.java` |
| 3️⃣ | **Checkout** — calcula `diárias × diária do tipo` e finaliza a reserva | `service/CheckoutService.java` |

---

## 🛏️ Quartos e preços

| Tipo | Qtd | Números | Diária | Capacidade |
|:---:|:---:|:---:|:---:|:---:|
| 🛏️ Simples | 10 | S01–S10 | R$ 150,00 | 1 pessoa |
| 🛏️🛏️ Duplo | 8 | D01–D08 | R$ 250,00 | 2 pessoas |
| 🛏️🛏️🛏️ Triplo | 5 | T01–T05 | R$ 350,00 | 3 pessoas |

> 🔧 Valores definidos em `model/TipoQuarto.java` e `resources/data.sql`.

---

## 📁 Estrutura do projeto


**Responsabilidades:** `model` → dados · `repository` → SQL (JDBC) · `service` → regras · `App` → menu.

---

## 🧰 Pré-requisitos

- ☕ JDK 21
- 💡 IntelliJ IDEA
- 🗄️ Acesso ao Oracle `oracle.fiap.com.br:1521:ORCL`
- 📦 Maven (o IntelliJ )
