Crie um sistema CRUD (Create, Read, Update, Delete) de funcionários usando Java com as seguintes características:

Estrutura do projeto

Projeto em Java utilizando Spring Boot para o backend.

Banco de dados H2 (em memória) para desenvolvimento, podendo ser substituído por MySQL ou PostgreSQL.

Estrutura MVC: Controller, Service, Repository, Model.

Entidade Funcionário

Campos: id (Long, autoincrement), nome (String), cpf (String), cargo (String), salario (BigDecimal), dataAdmissao (LocalDate).

Validações básicas: CPF único, nome obrigatório, salário positivo.

Operações CRUD

Create: criar um novo funcionário via endpoint POST /funcionarios.

Read: listar todos os funcionários ou buscar por ID via GET /funcionarios e /funcionarios/{id}.

Update: atualizar dados de um funcionário via PUT /funcionarios/{id}.

Delete: remover funcionário via DELETE /funcionarios/{id}.

Funcionalidades adicionais

Possibilidade de buscar funcionários por nome ou cargo via query parameters.

Tratamento de exceções com respostas claras (ex: 404 se funcionário não existir).

Uso de DTOs para separar a camada de apresentação da entidade.

Front-end (opcional)

Uma interface simples em HTML + Thymeleaf ou React/Vue para testar o CRUD.

Tabela de funcionários com botões de editar e deletar.

Formulário para criar e atualizar funcionários.

Extras (opcional)

Paginação e ordenação da lista de funcionários.

Autenticação básica com Spring Security.

Logs de operações realizadas (criação, atualização e exclusão).
