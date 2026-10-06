# Roteiro de validação local

- [ ] Executar DDL e DML no PostgreSQL vazio sem erros.
- [ ] Construir com `mvn clean verify` e iniciar no Tomcat 11.
- [ ] Abrir início, marcas, categorias e produtos sem erros.
- [ ] Criar, alterar, filtrar, ordenar, paginar e excluir uma marca sem produtos.
- [ ] Tentar repetir uma marca mudando caixa/espaços; receber erro.
- [ ] Criar categorias em três níveis e conferir o caminho completo.
- [ ] Tentar tornar a raiz filha da neta; receber erro.
- [ ] Tentar excluir uma categoria com filhos ou produtos; receber erro.
- [ ] Criar produto com preço positivo e estoque zero; editar e excluir.
- [ ] Tentar preço zero/negativo, estoque negativo, nome vazio e associação vazia.
- [ ] Enviar JPG, PNG e WEBP; salvar e conferir miniatura após reiniciar servidor.
- [ ] Tentar arquivo TXT renomeado e arquivo maior que 5 MB; rejeitar.
- [ ] Substituir imagem, cancelar edição e conferir preservação da anterior.
- [ ] Conferir confirmação antes da exclusão e mensagens amigáveis.
- [ ] Completar nomes no README e incluir três capturas reais.
- [ ] Publicar no GitHub mantendo histórico e enviar o link do repositório no AVA.

As capturas e a validação de interface devem ser feitas na instância executada por você.
