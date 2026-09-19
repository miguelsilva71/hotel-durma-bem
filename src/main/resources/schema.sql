-- ============================================================
-- Hotel Durma Bem - DDL Oracle
-- Rode este script uma vez no SQL Developer / SQL*Plus.
-- Ordem: schema.sql primeiro, depois data.sql
-- ============================================================

-- Apaga se ja existir (para reexecucao em ambiente de estudo)
BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE TB_RESERVA CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/
BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE TB_QUARTO CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE TB_QUARTO (
  id           NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  numero       VARCHAR2(10)  NOT NULL UNIQUE,
  tipo         VARCHAR2(10)  NOT NULL CHECK (tipo IN ('SIMPLES','DUPLO','TRIPLO')),
  preco_diaria NUMBER(10,2)  NOT NULL CHECK (preco_diaria > 0),
  ativo        CHAR(1)       DEFAULT 'S' NOT NULL CHECK (ativo IN ('S','N'))
);

CREATE TABLE TB_RESERVA (
  id           NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  quarto_id    NUMBER         NOT NULL REFERENCES TB_QUARTO(id),
  nome_hospede VARCHAR2(100)  NOT NULL,
  data_entrada DATE           NOT NULL,
  data_saida   DATE           NOT NULL,
  valor_total  NUMBER(10,2),
  status       VARCHAR2(20)   DEFAULT 'ATIVA' NOT NULL
               CHECK (status IN ('ATIVA','CHECKOUT','CANCELADA')),
  CONSTRAINT ck_reserva_periodo CHECK (data_saida > data_entrada)
);

CREATE INDEX idx_reserva_quarto_periodo ON TB_RESERVA (quarto_id, status, data_entrada, data_saida);
