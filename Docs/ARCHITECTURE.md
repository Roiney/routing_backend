## ⚙️ Documento Técnico — Plataforma de Roteirização Multi-Plataforma

### 1. Visão Geral Técnica

O sistema será desenvolvido como uma *plataforma modular baseada em microserviços, com foco em escalabilidade, segurança e baixa latência. A aplicação será composta por um **backend em Java (Spring Boot)* e um *frontend mobile em React Native*, com comunicação via APIs RESTful e mensageria assíncrona.

O principal objetivo técnico é permitir o *gerenciamento de rotas multi-plataforma* e *cálculo inteligente de eficiência operacional* para entregadores autônomos.

---

### 2. Arquitetura do Sistema

*Camadas principais:*

1. *Frontend Mobile (React Native):* Interface de uso diário pelos entregadores.
2. *API Gateway (Spring Cloud Gateway):* Intermedia as requisições REST, roteando para microserviços específicos.
3. *Microserviços Java:*

    * *Auth Service:* Autenticação e controle de permissões (JWT + OAuth2).
    * *Route Service:* Cálculo e otimização de rotas com integração a APIs externas (Google Maps, Mapbox, OSRM).
    * *Finance Service:* Registro e análise de ganhos, despesas e métricas de rentabilidade.
    * *Notification Service:* Envio de push notifications e alertas de conflito de rota.
    * *Analytics Service:* Motor de análise e relatórios, com agregações diárias e semanais.
4. *Database Layer:* PostgreSQL (operacional) + Redis (cache) + S3/Cloud Storage (para arquivos CSV e logs).
5. *Observabilidade:* Prometheus + Grafana para métricas e alertas.

---

### 3. Diagrama de Arquitetura (Descrição)


[React Native App]
↓
[API Gateway]
↓
┌───────────────────────────────┐
│  Auth Service     (Spring)   │
│  Route Service    (Spring)   │
│  Finance Service  (Spring)   │
│  Notification Svc (Spring)   │
│  Analytics Svc    (Spring)   │
└───────────────────────────────┘
↓
[PostgreSQL] — [Redis] — [S3 Storage]


---

### 4. Tecnologias Utilizadas

| Camada                   | Tecnologia                     | Função                                      |
| ------------------------ | ------------------------------ | ------------------------------------------- |
| *Backend*              | Java 21 + Spring Boot 3        | Estrutura principal de serviços             |
| *Segurança*            | Spring Security + JWT + OAuth2 | Autenticação e autorização                  |
| *Banco de Dados*       | PostgreSQL 16                  | Dados relacionais e históricos              |
| *Cache*                | Redis                          | Armazenamento temporário de rotas e sessões |
| *Mensageria*           | Apache Kafka                   | Processamento assíncrono e logs de eventos  |
| *APIs de Roteirização* | Google Maps / Mapbox / OSRM    | Cálculo de rotas e otimização               |
| *Infraestrutura*       | Docker + AWS ECS/Fargate       | Deploy e escalabilidade                     |
| *CI/CD*                | GitHub Actions                 | Pipeline de build, testes e deploy          |
| *Monitoramento*        | Prometheus + Grafana           | Métricas e alertas                          |
| *Mobile App*           | React Native (Expo)            | Interface de entregador                     |

---

### 5. Estrutura de Pacotes (Java)


com.lastmile
├── api              # Controladores REST
├── config           # Configurações Spring e Beans
├── domain           # Entidades e agregados de domínio
├── repository       # Interfaces JPA
├── service          # Lógica de negócio
├── dto              # Objetos de transferência de dados
├── exception        # Exceções e handlers globais
└── integration      # Integrações externas (Map APIs, OCR, CSV)


---

### 6. Modelo de Dados (simplificado)

#### Entregador


id: UUID
name: String
vehicleType: ENUM (MOTO, CAR, VAN)
region: String
createdAt: DateTime


#### Rota


id: UUID
driverId: UUID
platform: ENUM (LOGGI, FLEX, LALAMOVE, SHOPEE, AMAZON)
addresses: JSONB
optimizedRoute: JSONB
startTime: Timestamp
endTime: Timestamp
status: ENUM (PENDING, IN_PROGRESS, COMPLETED)


#### Finance


id: UUID
driverId: UUID
date: Date
platform: ENUM
revenue: DECIMAL(10,2)
cost: DECIMAL(10,2)
profit: DECIMAL(10,2)


---

### 7. Fluxos Principais

#### a) Cálculo de Rota

1. Usuário importa endereços (manual, CSV, ou OCR).
2. Backend valida e normaliza os dados.
3. Route Service consulta API externa (Mapbox/Google).
4. Resultado é armazenado no PostgreSQL e cacheado no Redis.
5. Notificação enviada ao app com rota otimizada.

#### b) Controle Financeiro

1. Entregador registra ganhos e despesas.
2. Finance Service calcula lucro líquido e custo/km.
3. Dados são agregados diariamente no Analytics Service.

#### c) Notificações de Conflito

1. Route Service detecta sobreposição de rotas.
2. Gera evento Kafka → Notification Service.
3. Push é enviado via Firebase Cloud Messaging.

---

### 8. Segurança e Conformidade

* Autenticação baseada em *JWT* com expiração curta e refresh token.
* Criptografia de dados sensíveis (AES-256 no banco).
* Logs estruturados e mascaramento de informações pessoais (LGPD).
* Mecanismo de rate limiting e monitoramento de requisições suspeitas.
* Backups diários automatizados (PostgreSQL + S3).

---

### 9. Estratégia de Deploy e Escalabilidade

* *CI/CD:* GitHub Actions → Docker Build → AWS ECR → Deploy automático via ECS.
* *Horizontal scaling:* Cada microserviço é escalável independentemente (ECS Tasks).
* *Load Balancing:* AWS Application Load Balancer.
* *Auto-recovery:* Health checks automáticos e rollback em falhas.
* *Observabilidade:* logs centralizados com Loki + Promtail.

---

### 10. Roadmap Técnico

| Fase             | Entrega                                                |
| ---------------- | ------------------------------------------------------ |
| *Fase 1 (MVP)* | Auth + Route Service + Mobile básico                   |
| *Fase 2*       | Finance + Notification Service + painel analítico      |
| *Fase 3*       | OCR + integração CSV + IA de recomendação de rotas     |
| *Fase 4*       | API pública + integrações B2B + expansão internacional |

---

### 11. Visão de Futuro Técnico

* Implementar *IA de recomendação de rotas lucrativas* com TensorFlow Lite.
* Criar *módulo de telemetria* para entender padrões de deslocamento.
* Oferecer *API para transportadoras e hubs logísticos*.
* Migrar para arquitetura *event-driven com Kafka Streams*.
* Implementar *mecanismo de otimização offline-first* para baixa conectividade.

---

Com esta base técnica, a plataforma estará preparada para operar de forma escalável, segura e eficiente, suportando alto volume de entregas, múltiplas integrações e evolução contínua.