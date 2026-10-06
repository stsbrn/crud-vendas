# Verificações realizadas

- JDK 17 e Maven 3.9.9: `mvn clean verify` concluído com BUILD SUCCESS.
- Cinco testes JUnit passaram, sem falhas ou erros, usando H2 em modo PostgreSQL.
- Apache Tomcat 11.0.0: aplicação inicializou com Faces, Weld e PrimeFaces.
- GET HTTP das quatro páginas: todas retornaram 200.
- POST HTTP dos formulários de marca, categoria e produto: registros persistidos, mensagem de sucesso e renderização da tabela conferidos.
- XML e XHTML verificados como bem formados.

## Limites da verificação

O teste HTTP utilizou uma cópia temporária do WAR com H2 e criação automática de tabelas; a distribuição continua configurada para PostgreSQL e validação do esquema. O PostgreSQL, o trigger SQL, a interação visual no navegador e o upload via AJAX devem ser conferidos pelo roteiro de validação local. Não foram incluídas capturas de tela simuladas.

Os commits foram produzidos nesta sessão, por etapa de implementação, com autoria Codex. Não representam desenvolvimento anterior do aluno. Continue versionando seus ajustes, testes locais e capturas antes de entregar.
