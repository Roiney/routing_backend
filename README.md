# 🚚 Routing Backend - Plataforma de Roteirização Multi-Plataforma

Backend da plataforma SaaS para gestão de rotas e finanças de entregadores autônomos que trabalham em múltiplas plataformas (Mercado Livre Flex, Loggi, Lalamove, Shopee, Amazon Flex, etc).

## 🎯 Objetivo

Fornecer aos entregadores autônomos uma solução centralizada para:
- Otimizar rotas de entrega
- Gerenciar ganhos e despesas
- Visualizar métricas de desempenho
- Detectar conflitos entre plataformas

---

## 🏗️ Stack Tecnológico

- **Backend:** Java 21 + Spring Boot 3.5.7
- **Banco de Dados:** PostgreSQL 16
- **Cache:** Redis 7
- **Mensageria:** Apache Kafka
- **Segurança:** Spring Security + JWT
- **Documentação:** SpringDoc OpenAPI (Swagger)
- **Containerização:** Docker & Docker Compose

---

## 🚀 Quick Start

### Pré-requisitos

- Java 21
- Docker & Docker Compose
- Maven 3.9+ (incluído via wrapper)

### Instalação

```bash
# 1. Clone o repositório
git clone <repository-url>
cd routing_backend

# 2. Configure as variáveis locais (opcional)
cp src/main/resources/application-local.properties.example \
   src/main/resources/application-local.properties
# Edite application-local.properties com suas chaves de API

# 3. Inicie os serviços (PostgreSQL, Redis, Kafka)
make up
# ou
docker-compose up -d

# 4. Execute a aplicação
make run
# ou
./mvnw spring-boot:run
```

### Verificação

A aplicação estará disponível em:
- **API:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **Health Check:** http://localhost:8080/actuator/health

---

## 📦 Serviços Disponíveis

| Serviço       | Porta | Acesso                    |
|---------------|-------|---------------------------|
| API Backend   | 8080  | http://localhost:8080     |
| PostgreSQL    | 5432  | localhost:5432            |
| Redis         | 6379  | localhost:6379            |
| Kafka         | 9092  | localhost:9092            |
| PgAdmin*      | 5050  | http://localhost:5050     |
| Redis Commander* | 8081 | http://localhost:8081  |
| Kafka UI*     | 8082  | http://localhost:8082     |

\* Ferramentas opcionais - Use `make tools` para iniciar

---

## 🛠️ Comandos Úteis

```bash
# Desenvolvimento
make dev            # Inicia serviços + aplicação
make build          # Build com Maven
make test           # Executa testes
make run            # Executa aplicação

# Docker
make up             # Inicia serviços
make down           # Para serviços
make restart        # Reinicia serviços
make logs           # Ver logs
make clean          # Remove containers e volumes
make tools          # Inicia com ferramentas de management

# Database
make db-shell       # Acessa PostgreSQL CLI
make db-reset       # Reseta banco de dados

# Ajuda
make help           # Lista todos os comandos
```

---

## 📁 Estrutura do Projeto

```
routing_backend/
├── Docs/                           # Documentação
│   ├── ARCHITECTURE.md             # Arquitetura técnica
│   ├── BUSINESS.md                 # Modelo de negócio
│   ├── README.md                   # Visão geral do projeto
│   └── DEVELOPMENT_STRATEGY.md     # Estratégia de desenvolvimento
├── src/main/java/.../routing_backend/
│   ├── api/                        # Controllers REST
│   ├── config/                     # Configurações Spring
│   ├── domain/                     # Entidades e modelos
│   ├── repository/                 # Repositórios JPA
│   ├── service/                    # Lógica de negócio
│   ├── dto/                        # Data Transfer Objects
│   ├── exception/                  # Exceções customizadas
│   └── integration/                # Integrações externas
├── docker-compose.yml              # Configuração Docker
├── Makefile                        # Comandos auxiliares
└── pom.xml                         # Dependências Maven
```

---

## 🔐 Segurança

- **Autenticação:** JWT (JSON Web Tokens)
- **Autorização:** Role-based (DRIVER, ADMIN)
- **Criptografia:** BCrypt para senhas
- **HTTPS:** Recomendado em produção
- **Rate Limiting:** Proteção contra abuso

> **Importante:** Nunca commite credenciais ou chaves de API. Use variáveis de ambiente.

---

## 📊 Monitoramento

A aplicação expõe endpoints do Spring Actuator:

```bash
# Health check
curl http://localhost:8080/actuator/health

# Métricas
curl http://localhost:8080/actuator/metrics

# Prometheus
curl http://localhost:8080/actuator/prometheus
```

---

## 🧪 Testes

```bash
# Todos os testes
./mvnw test

# Testes específicos
./mvnw test -Dtest=RouteServiceTest

# Com cobertura
./mvnw test jacoco:report
```

---

## 📖 Documentação da API

Acesse a documentação interativa Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

Ou baixe a especificação OpenAPI:

```
http://localhost:8080/api-docs
```

---

## 🌍 Ambientes

### Desenvolvimento (dev)
```bash
spring.profiles.active=dev
```
- Banco de dados local (Docker)
- Logs detalhados
- Hot reload ativado
- Swagger disponível

### Produção (prod)
```bash
spring.profiles.active=prod
```
- Variáveis de ambiente para secrets
- SSL/TLS habilitado
- Logs otimizados
- Métricas e monitoramento

---

## 🐛 Troubleshooting

### Porta 8080 em uso
```bash
lsof -ti:8080 | xargs kill -9
```

### Containers não iniciam
```bash
docker-compose ps
docker-compose logs
```

### Limpar cache Maven
```bash
./mvnw clean
./mvnw dependency:purge-local-repository
```

---

## 📚 Documentação Adicional

- [Estratégia de Desenvolvimento](Docs/DEVELOPMENT_STRATEGY.md)
- [Arquitetura Técnica](Docs/ARCHITECTURE.md)
- [Modelo de Negócio](Docs/BUSINESS.md)

---

## 🤝 Contribuindo

1. Crie uma branch: `git checkout -b feature/nova-feature`
2. Commit suas mudanças: `git commit -m 'feat: adiciona nova feature'`
3. Push para a branch: `git push origin feature/nova-feature`
4. Abra um Pull Request

### Convenção de Commits

```
feat: nova funcionalidade
fix: correção de bug
refactor: refatoração
test: adição de testes
docs: atualização de documentação
```

---

## 📄 Licença

[A definir]

---

## 👥 Autores

[A definir]

---

## 📞 Suporte

Em caso de dúvidas ou problemas:
- Abra uma issue no repositório
- Consulte a documentação em `/Docs`
- [Contato]

---

**Status do Projeto:** 🟡 Em Desenvolvimento (MVP)

**Última atualização:** 2025-11-01
