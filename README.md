# Sistema de Vendas — Catálogo

Atividade prática de **Programação WEB 2**, professor **Danilo Souza Almeida**.

## Integrantes

- Bruno Ribeiro Silva


## Solução

CRUD de marcas, categorias hierárquicas e produtos com imagens JPG, PNG e WEBP. Interface JSF/PrimeFaces com tabelas paginadas, ordenação, filtros, mensagens e confirmação de exclusão. Marca tem nome único também sem diferença entre maiúsculas e minúsculas. Categorias exibem o caminho completo e rejeitam ciclos. Exclusões com vínculos são bloqueadas. Imagens recebem nome UUID, validação de assinatura e limite de 5 MB; ficam fora da aplicação, preservadas ao atualizar o WAR.

## Pré-requisitos

- JDK 17, Maven 3.9+, PostgreSQL 15+ e **Apache Tomcat 11**.
- NetBeans com suporte a Maven e Tomcat, se desejar executar pela IDE.
- Não usar Tomcat 9/10 com esta configuração: o projeto utiliza Faces 4.1 e Servlet 6.1.

## Banco de dados

Crie um banco vazio `vendas` pelo pgAdmin ou terminal:

```sh
createdb -U postgres vendas
psql -U postgres -d vendas -f sql/01-schema.sql
psql -U postgres -d vendas -f sql/02-dados.sql
```

No pgAdmin, execute os scripts pelo Query Tool, na ordem acima. O DDL deve ser executado uma única vez em um banco vazio. A carga inicial contém duas marcas, três níveis de categorias e um produto.

Defina as variáveis no processo que inicia o Tomcat (reinicie a IDE/servidor após ajustar):

| Variável | Padrão de desenvolvimento |
| --- | --- |
| `VENDAS_DB_URL` | `jdbc:postgresql://localhost:5432/vendas` |
| `VENDAS_DB_USER` | `postgres` |
| `VENDAS_DB_PASSWORD` | `postgres` |
| `VENDAS_UPLOAD_DIR` | pasta `vendas-imagens` no diretório do usuário |

Use uma pasta absoluta de uploads com permissão de escrita. Não envie senha real ao GitHub. Para configurar o Windows no PowerShell antes de iniciar o servidor:

```powershell
$env:VENDAS_DB_PASSWORD = 'SUA_SENHA_LOCAL'
$env:VENDAS_UPLOAD_DIR = 'C:\vendas-imagens'
```

Os padrões são apenas para desenvolvimento local. O Hibernate valida o esquema, sem apagar ou recriar dados.

## Executar

```sh
mvn clean verify
```

Copie `target/vendas.war` para `webapps` do Tomcat 11. Inicie o servidor e abra `http://localhost:8080/vendas/`.

No NetBeans: Arquivo → Abrir Projeto → selecionar a pasta que contém `pom.xml`; cadastrar Tomcat 11 em Serviços → Servidores; selecionar JDK 17; Limpar e Construir; executar. Se a IDE não reconhecer Tomcat 11, gere o WAR e faça a implantação manual indicada acima.

## Utilização

1. Cadastre uma marca.
2. Cadastre uma categoria raiz e depois subcategorias selecionando o pai.
3. Cadastre um produto associando marca e categoria.
4. Escolha uma imagem, aguarde a mensagem de recebimento e clique em Salvar.
5. Use os filtros da tabela para localizar e os botões para editar/excluir.

A imagem enviada fica pendente até Salvar. Cancelar descarta a imagem pendente. Imagens de produtos excluídos ou substituídas são removidas. Marca inativa é registrada e continua disponível para associação, pois o enunciado não exige bloqueio.

## Arquitetura

- `model`: entidades JPA e Bean Validation.
- `repository`: EntityManager e acesso aos dados.
- `service`: validações, transações e arquivos de imagem.
- `controller`: beans CDI de sessão.
- `converter`: conversão de Marca/Categoria nos menus JSF.
- `web`: servlet para servir imagens.
- `sql`: DDL e carga de teste.

O exemplo JDBC de usuários do projeto base foi substituído pelo domínio de vendas. Foram adicionados Hibernate e validação. A configuração utiliza transações RESOURCE_LOCAL, próprias de Tomcat, sem depender de um servidor Jakarta EE completo. Cada operação abre e fecha seu EntityManager. A aplicação é acadêmica e não possui autenticação; execute localmente para demonstração.

## Testes

`mvn test` executa testes automatizados de persistência com H2 em modo PostgreSQL: CRUD, unicidade de marca, ciclos, exclusões vinculadas, valores inválidos e validação de arquivos. H2 não substitui o teste de PostgreSQL e dos componentes JSF no Tomcat.

Antes da entrega, execute também o roteiro em `docs/VALIDACAO.md` e adicione capturas reais em `docs/screenshots/`.

## Capturas de tela

Ainda precisam ser capturadas na execução local. Adicione `marcas.png`, `categorias.png` e `produtos.png` em `docs/screenshots/` e inclua abaixo os links Markdown correspondentes. Não há capturas simuladas neste repositório.

## Entrega no GitHub

O professor exige histórico progressivo. Preserve os commits do repositório incluído, continue fazendo commits das suas alterações e publique pelo Git, sem enviar apenas o ZIP pela página do GitHub.

Crie um repositório vazio no GitHub, sem README automático. Na pasta do projeto:

```sh
git remote add origin https://github.com/SEU_USUARIO/crud-vendas.git
git push -u origin main
```

Autentique pelo Git Credential Manager do seu computador; não coloque token no código. Confira os arquivos e commits na página do repositório. Envie no AVA o endereço `https://github.com/SEU_USUARIO/crud-vendas`, conforme o prazo informado em sala.

## Referências

- Enunciado fornecido pelo professor e projeto base da disciplina.
- Hibernate ORM: https://hibernate.org/orm/documentation/6.6/
- PrimeFaces: https://primefaces.github.io/primefaces/15_0_0/
