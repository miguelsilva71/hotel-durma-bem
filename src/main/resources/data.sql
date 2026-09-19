-- ============================================================
-- Hotel Durma Bem - Carga inicial: 10 SIMPLES + 8 DUPLOS + 5 TRIPLOS
-- Diaria: Simples 150, Duplo 250, Triplo 350 (alinado a TipoQuarto.java)
-- ============================================================

-- SIMPLES (10)
INSERT INTO TB_QUARTO (numero, tipo, preco_diaria, ativo) VALUES ('S01', 'SIMPLES', 150.00, 'S');
INSERT INTO TB_QUARTO (numero, tipo, preco_diaria, ativo) VALUES ('S02', 'SIMPLES', 150.00, 'S');
INSERT INTO TB_QUARTO (numero, tipo, preco_diaria, ativo) VALUES ('S03', 'SIMPLES', 150.00, 'S');
INSERT INTO TB_QUARTO (numero, tipo, preco_diaria, ativo) VALUES ('S04', 'SIMPLES', 150.00, 'S');
INSERT INTO TB_QUARTO (numero, tipo, preco_diaria, ativo) VALUES ('S05', 'SIMPLES', 150.00, 'S');
INSERT INTO TB_QUARTO (numero, tipo, preco_diaria, ativo) VALUES ('S06', 'SIMPLES', 150.00, 'S');
INSERT INTO TB_QUARTO (numero, tipo, preco_diaria, ativo) VALUES ('S07', 'SIMPLES', 150.00, 'S');
INSERT INTO TB_QUARTO (numero, tipo, preco_diaria, ativo) VALUES ('S08', 'SIMPLES', 150.00, 'S');
INSERT INTO TB_QUARTO (numero, tipo, preco_diaria, ativo) VALUES ('S09', 'SIMPLES', 150.00, 'S');
INSERT INTO TB_QUARTO (numero, tipo, preco_diaria, ativo) VALUES ('S10', 'SIMPLES', 150.00, 'S');

-- DUPLOS (8)
INSERT INTO TB_QUARTO (numero, tipo, preco_diaria, ativo) VALUES ('D01', 'DUPLO', 250.00, 'S');
INSERT INTO TB_QUARTO (numero, tipo, preco_diaria, ativo) VALUES ('D02', 'DUPLO', 250.00, 'S');
INSERT INTO TB_QUARTO (numero, tipo, preco_diaria, ativo) VALUES ('D03', 'DUPLO', 250.00, 'S');
INSERT INTO TB_QUARTO (numero, tipo, preco_diaria, ativo) VALUES ('D04', 'DUPLO', 250.00, 'S');
INSERT INTO TB_QUARTO (numero, tipo, preco_diaria, ativo) VALUES ('D05', 'DUPLO', 250.00, 'S');
INSERT INTO TB_QUARTO (numero, tipo, preco_diaria, ativo) VALUES ('D06', 'DUPLO', 250.00, 'S');
INSERT INTO TB_QUARTO (numero, tipo, preco_diaria, ativo) VALUES ('D07', 'DUPLO', 250.00, 'S');
INSERT INTO TB_QUARTO (numero, tipo, preco_diaria, ativo) VALUES ('D08', 'DUPLO', 250.00, 'S');

-- TRIPLOS (5)
INSERT INTO TB_QUARTO (numero, tipo, preco_diaria, ativo) VALUES ('T01', 'TRIPLO', 350.00, 'S');
INSERT INTO TB_QUARTO (numero, tipo, preco_diaria, ativo) VALUES ('T02', 'TRIPLO', 350.00, 'S');
INSERT INTO TB_QUARTO (numero, tipo, preco_diaria, ativo) VALUES ('T03', 'TRIPLO', 350.00, 'S');
INSERT INTO TB_QUARTO (numero, tipo, preco_diaria, ativo) VALUES ('T04', 'TRIPLO', 350.00, 'S');
INSERT INTO TB_QUARTO (numero, tipo, preco_diaria, ativo) VALUES ('T05', 'TRIPLO', 350.00, 'S');

COMMIT;
