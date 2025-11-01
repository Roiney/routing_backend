# 🚀 Estratégia de Desenvolvimento - Routing Backend

## 1. Visão Geral

Este documento define a estratégia de desenvolvimento do backend da plataforma de roteirização multi-plataforma, estabelecendo práticas, fluxos de trabalho e prioridades para o desenvolvimento do MVP e fases subsequentes.

---

## 2. Configuração do Ambiente

### 2.1 Pré-requisitos

- **Java 21** (JDK)
- **Maven 3.9+** (incluído via wrapper)
- **Docker & Docker Compose**
- **Git**
- **IDE**: IntelliJ IDEA ou VS Code com extensões Java

### 2.2 Setup Inicial

```bash
# 1. Clone o repositório
git clone <repository-url>
cd routing_backend

# 2. Inicie os serviços de infraestrutura
make up
# ou
docker-compose up -d

# 3. Instale as dependências
./mvnw dependency:resolve

# 4. Execute a aplicação
make run
# ou
./mvnw spring-boot:run
```

### 2.3 Variáveis de Ambiente

Crie um arquivo `src/main/resources/application-local.properties` baseado no template:

```bash
cp src/main/resources/application-local.properties.example \
   src/main/resources/application-local.properties
```

Edite e adicione suas chaves de API:
- Google Maps API Key
- Mapbox API Key (opcional)

---

## 3. Estrutura do Projeto

```
routing_backend/
├── src/main/java/com/example/routing_backend/
│   ├── api/                    # Controllers REST
│   │   ├── controller/         # Endpoints REST
│   │   ├── filter/             # Filtros HTTP
│   │   └── interceptor/        # Interceptadores
│   ├── config/                 # Configurações Spring
│   │   ├── security/           # JWT, Spring Security
│   │   ├── database/           # JPA, Hibernate
│   │   ├── cache/              # Redis
│   │   └── kafka/              # Kafka Producer/Consumer
│   ├── domain/                 # Camada de domínio
│   │   ├── entity/             # Entidades JPA
│   │   ├── enums/              # Enumerações
│   │   └── model/              # Value Objects
│   ├── repository/             # Repositórios JPA
│   ├── service/                # Lógica de negócio
│   │   ├── auth/               # Autenticação
│   │   ├── route/              # Roteirização
│   │   ├── finance/            # Gestão financeira
│   │   ├── notification/       # Notificações
│   │   └── analytics/          # Análises e métricas
│   ├── dto/                    # Data Transfer Objects
│   ├── exception/              # Exceções customizadas
│   └── integration/            # Integrações externas
│       ├── maps/               # Google Maps, Mapbox
│       ├── kafka/              # Mensageria
│       └── redis/              # Cache
└── src/main/resources/
    ├── application.properties
    ├── application-dev.properties
    ├── application-prod.properties
    └── db/migration/           # Flyway migrations (futuro)
```

---

## 4. Roadmap de Desenvolvimento (MVP)

### Fase 1: Fundação (Semanas 1-2)

**Objetivo:** Estabelecer a base da aplicação com autenticação e infraestrutura.

**Entregas:**
- [x] Configuração inicial do projeto
- [x] Docker Compose com PostgreSQL, Redis, Kafka
- [x] Estrutura de pacotes
- [ ] Configuração de segurança (JWT + Spring Security)
- [ ] Entidades base (Driver, Route, Finance)
- [ ] Repositórios JPA
- [ ] Health check endpoints

**Prioridade:** ALTA

---

### Fase 2: Core - Roteirização (Semanas 3-4)

**Objetivo:** Implementar a funcionalidade central de roteirização.

**Entregas:**
- [ ] Integração com Google Maps API
- [ ] Service de otimização de rotas
- [ ] Endpoints para importação de rotas (manual, CSV)
- [ ] Cálculo de distância, tempo e custo
- [ ] Validações de endereços
- [ ] Testes unitários e de integração

**APIs:**
- `POST /api/v1/routes/import` - Importar rotas
- `POST /api/v1/routes/optimize` - Otimizar rota
- `GET /api/v1/routes` - Listar rotas
- `GET /api/v1/routes/{id}` - Detalhes da rota

**Prioridade:** ALTA

---

### Fase 3: Gestão Financeira (Semanas 5-6)

**Objetivo:** Permitir que entregadores registrem ganhos e despesas.

**Entregas:**
- [ ] Service de registro financeiro
- [ ] Endpoints de finanças
- [ ] Cálculo de métricas (lucro/hora, custo/km)
- [ ] Dashboard de desempenho
- [ ] Relatórios agregados (diário, semanal, mensal)

**APIs:**
- `POST /api/v1/finance/record` - Registrar transação
- `GET /api/v1/finance/summary` - Resumo financeiro
- `GET /api/v1/finance/analytics` - Análises detalhadas

**Prioridade:** MÉDIA

---

### Fase 4: Notificações e Alertas (Semanas 7-8)

**Objetivo:** Implementar sistema de notificações.

**Entregas:**
- [ ] Kafka producer/consumer
- [ ] Service de detecção de conflitos
- [ ] Notificações push (Firebase)
- [ ] Alertas de rota

**Prioridade:** BAIXA (para MVP)

---

## 5. Práticas de Desenvolvimento

### 5.1 Git Flow

```
main          # Produção - sempre estável
  └── develop # Desenvolvimento ativo
       ├── feature/nome-da-feature
       ├── fix/nome-do-bug
       └── hotfix/nome-do-hotfix
```

**Convenção de Commits:**
```
feat: adiciona endpoint de importação de rotas
fix: corrige cálculo de distância
refactor: reorganiza service de roteirização
test: adiciona testes para RouteService
docs: atualiza documentação da API
```

### 5.2 Code Review

- Toda alteração deve passar por Pull Request
- Mínimo de 1 aprovação
- Testes devem estar passando
- Cobertura de código > 70%

### 5.3 Testes

```bash
# Executar todos os testes
make test

# Executar testes específicos
./mvnw test -Dtest=RouteServiceTest
```

**Tipos de testes:**
- **Unitários:** Lógica de negócio isolada
- **Integração:** Testes com banco de dados em memória (H2)
- **E2E:** Testes de ponta a ponta (futuro)

### 5.4 Documentação da API

A API é documentada automaticamente com SpringDoc OpenAPI:

```
http://localhost:8080/swagger-ui.html
http://localhost:8080/api-docs
```

---

## 6. Boas Práticas

### 6.1 Segurança

- ✅ Nunca commitar credenciais ou chaves de API
- ✅ Usar variáveis de ambiente para secrets
- ✅ Validar todos os inputs
- ✅ Sanitizar dados antes de salvar no banco
- ✅ Implementar rate limiting
- ✅ Logs sem informações sensíveis

### 6.2 Performance

- ✅ Usar cache (Redis) para dados frequentemente acessados
- ✅ Otimizar queries N+1 com JPA
- ✅ Implementar paginação em listas
- ✅ Usar índices no banco de dados
- ✅ Batch processing para operações em massa

### 6.3 Código Limpo

- ✅ Usar Lombok para reduzir boilerplate
- ✅ MapStruct para conversão DTO ↔ Entity
- ✅ Seguir princípios SOLID
- ✅ Métodos pequenos e coesos
- ✅ Nomenclatura clara e descritiva

---

## 7. Monitoramento e Observabilidade

### 7.1 Métricas

```
http://localhost:8080/actuator/health
http://localhost:8080/actuator/metrics
http://localhost:8080/actuator/prometheus
```

### 7.2 Logs

Configuração estruturada:
- **INFO:** Operações normais
- **WARN:** Situações incomuns mas recuperáveis
- **ERROR:** Erros que precisam atenção
- **DEBUG:** Detalhes técnicos (apenas dev)

---

## 8. Comandos Úteis

```bash
# Iniciar ambiente de desenvolvimento
make dev

# Ver logs dos containers
make logs

# Resetar banco de dados
make db-reset

# Acessar shell do PostgreSQL
make db-shell

# Build da aplicação
make build

# Limpar ambiente
make clean

# Iniciar com ferramentas de management
make tools
```

---

## 9. Troubleshooting

### Problema: Porta 8080 já em uso
```bash
# Encontrar e matar o processo
lsof -ti:8080 | xargs kill -9
```

### Problema: Containers não iniciam
```bash
# Verificar status
docker-compose ps

# Ver logs específicos
docker-compose logs postgres
```

### Problema: Dependências não resolvem
```bash
# Limpar cache do Maven
./mvnw dependency:purge-local-repository
```

---

## 10. Próximos Passos

1. ✅ Setup inicial do ambiente
2. ⏳ Implementar autenticação JWT
3. ⏳ Criar entidades de domínio
4. ⏳ Desenvolver Route Service
5. ⏳ Integrar com Google Maps API

---

## 11. Recursos Úteis

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [Google Maps API](https://developers.google.com/maps)
- [Docker Compose](https://docs.docker.com/compose/)
- [PostgreSQL](https://www.postgresql.org/docs/)

---

**Última atualização:** 2025-11-01
**Versão:** 1.0.0
