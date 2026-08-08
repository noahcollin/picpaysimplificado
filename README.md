# 💳 PicPay Simplificado - API RESTful

API RESTful desenvolvida em **Java 17** com **Spring Boot 3** que simula uma plataforma de pagamentos simplificada entre usuários comuns e lojistas.

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 17
- **Framework:** Spring Boot 3
- **Persistência:** Spring Data JPA / H2 Database (em memória)
- **Comunicação HTTP:** RestTemplate
- **Testes:** JUnit 5 + Mockito
- **Produtividade:** Lombok

---

## 📌 Funcionalidades & Regras de Negócio

- [x] **Cadastro de Usuários e Lojistas:** Validação de unicidade de CPF/CNPJ e E-mail.
- [x] **Regra de Transação:** Usuários comuns enviam e recebem; Lojistas apenas recebem.
- [x] **Validação de Saldo:** Verificação prévia do saldo do pagador.
- [x] **Segurança de Autotransferência:** Bloqueio de transferências para a própria conta.
- [x] **Consulta ao Autorizador Externo:** Integração com mock via verbo `GET`.
- [x] **Transacionalidade (ACID):** Rollback automático via `@Transactional` em caso de falha.
- [x] **Notificação Resiliente:** Disparo de notificação via mock `POST` com isolamento de falhas (try-catch).
- [x] **Endpoint de Depósito:** Permite adicionar saldo à carteira do usuário.

---

## 🚀 Endpoints da API

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/users` | Cadastra um novo usuário ou lojista |
| `GET` | `/users` | Lista todos os usuários cadastrados |
| `POST` | `/users/{id}/deposit` | Realiza um depósito na conta |
| `POST` | `/transfer` | Realiza uma transferência entre contas |

---

## 🧪 Como Executar o Projeto

1. Clone o repositório:
   ```bash
   git clone [https://github.com/noahcollin/picpaysimplificado.git](https://github.com/noahcollin/picpaysimplificado.git)
2. Abra o projeto na sua IDE (IntelliJ IDEA recomendada).
3. Execute a classe principal `PicpaysimplificadoApplication.java`.
4. A API estará rodando em `http://localhost:8080`.
