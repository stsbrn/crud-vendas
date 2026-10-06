BEGIN;
INSERT INTO tb_marca(nome,descricao) VALUES ('Logitech','Periféricos'),('Samsung','Eletrônicos');
INSERT INTO tb_categoria(nome) VALUES ('Informática');
INSERT INTO tb_categoria(nome,categoria_id) SELECT 'Periféricos',id FROM tb_categoria WHERE nome='Informática';
INSERT INTO tb_categoria(nome,categoria_id) SELECT 'Teclados',id FROM tb_categoria WHERE nome='Periféricos';
INSERT INTO tb_produto(nome,descricao,preco,quantidade_estoque,marca_id,categoria_id) SELECT 'Teclado USB','Teclado para uso diário',129.90,15,m.id,c.id FROM tb_marca m CROSS JOIN tb_categoria c WHERE m.nome='Logitech' AND c.nome='Teclados';
COMMIT;
