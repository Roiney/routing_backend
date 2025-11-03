# 📊 Modelo de Dados - Entidades do Sistema

## Visão Geral

Este documento define o modelo de dados completo da plataforma de roteirização, incluindo entidades, atributos, relacionamentos e regras de negócio.

---

## Índice

1. [Entidades Core (MVP)](#1-entidades-core-mvp)
2. [Enumerações](#2-enumerações)
3. [Relacionamentos](#3-relacionamentos)
4. [Diagrama ER](#4-diagrama-er)
5. [Regras de Negócio](#5-regras-de-negócio)
6. [Índices e Performance](#6-índices-e-performance)
7. [Entidades Futuras](#7-entidades-futuras)

---

## 1. Entidades Core (MVP)

### 1.1 Driver (Entregador)

**Descrição:** Representa o usuário entregador autônomo que utiliza a plataforma.

**Tabela:** `drivers`

| Atributo | Tipo | Nullable | Descrição |
|----------|------|----------|-----------|
| id | UUID | NOT NULL | Identificador único (PK) |
| name | VARCHAR(100) | NOT NULL | Nome completo do entregador |
| email | VARCHAR(150) | NOT NULL | Email (unique) |
| phone | VARCHAR(20) | NOT NULL | Telefone com DDD |
| password_hash | VARCHAR(255) | NOT NULL | Hash da senha (BCrypt) |
| vehicle_type | ENUM | NOT NULL | Tipo de veículo (MOTORCYCLE, CAR, VAN) |
| region | VARCHAR(100) | NULL | Região de atuação principal |
| fuel_cost_per_liter | DECIMAL(5,2) | NULL | Custo do combustível em R$/litro |
| vehicle_consumption | DECIMAL(5,2) | NULL | Consumo do veículo em km/litro |
| default_start_location | JSONB | NULL | Localização padrão de início {lat, lng, address} |
| preferred_navigation_app | ENUM | NULL | App de navegação preferido (GOOGLE_MAPS, WAZE) |
| account_status | ENUM | NOT NULL | Status da conta (ACTIVE, INACTIVE, SUSPENDED) |
| subscription_type | ENUM | NOT NULL | Tipo de assinatura (FREE, PREMIUM) |
| subscription_expires_at | TIMESTAMP | NULL | Data de expiração da assinatura Premium |
| email_verified | BOOLEAN | NOT NULL | Se o email foi verificado |
| email_verified_at | TIMESTAMP | NULL | Data de verificação do email |
| created_at | TIMESTAMP | NOT NULL | Data de criação |
| updated_at | TIMESTAMP | NOT NULL | Data de atualização |
| deleted_at | TIMESTAMP | NULL | Data de exclusão (soft delete) |

**Constraints:**
- `UNIQUE(email)`
- `UNIQUE(phone)`
- `CHECK(fuel_cost_per_liter > 0)`
- `CHECK(vehicle_consumption > 0)`

**Índices:**
- `idx_drivers_email` (email)
- `idx_drivers_phone` (phone)
- `idx_drivers_subscription_type` (subscription_type)
- `idx_drivers_account_status` (account_status)

**Validações:**
- Email deve ser válido (regex)
- Telefone deve seguir formato brasileiro
- Senha mínima: 8 caracteres, 1 letra, 1 número
- Nome: 3-100 caracteres

---

### 1.2 Platform (Plataforma de Entrega)

**Descrição:** Representa uma plataforma de entrega onde o entregador trabalha.

**Tabela:** `platforms`

| Atributo | Tipo | Nullable | Descrição |
|----------|------|----------|-----------|
| id | UUID | NOT NULL | Identificador único (PK) |
| driver_id | UUID | NOT NULL | Referência ao entregador (FK) |
| platform_type | ENUM | NOT NULL | Tipo de plataforma (LOGGI, FLEX, AMAZON_FLEX, etc.) |
| is_active | BOOLEAN | NOT NULL | Se a plataforma está ativa para o entregador |
| account_identifier | VARCHAR(100) | NULL | Identificador da conta na plataforma (opcional) |
| created_at | TIMESTAMP | NOT NULL | Data de adição |
| updated_at | TIMESTAMP | NOT NULL | Data de atualização |

**Constraints:**
- `FOREIGN KEY(driver_id) REFERENCES drivers(id) ON DELETE CASCADE`
- `UNIQUE(driver_id, platform_type)` - Um entregador não pode ter a mesma plataforma duplicada

**Índices:**
- `idx_platforms_driver_id` (driver_id)
- `idx_platforms_type` (platform_type)

---

### 1.3 Route (Rota Otimizada)

**Descrição:** Representa uma rota otimizada gerada pelo sistema contendo múltiplas entregas.

**Tabela:** `routes`

| Atributo | Tipo | Nullable | Descrição |
|----------|------|----------|-----------|
| id | UUID | NOT NULL | Identificador único (PK) |
| driver_id | UUID | NOT NULL | Referência ao entregador (FK) |
| name | VARCHAR(100) | NULL | Nome da rota (ex: "Rota Manhã - 01/11") |
| status | ENUM | NOT NULL | Status da rota (PENDING, IN_PROGRESS, COMPLETED, CANCELLED) |
| optimization_type | ENUM | NOT NULL | Tipo de otimização (SHORTEST_DISTANCE, SHORTEST_TIME, RESPECT_TIME_WINDOWS) |
| start_location | JSONB | NOT NULL | Ponto de partida {lat, lng, address} |
| end_location | JSONB | NULL | Ponto final (se diferente do início) |
| total_distance_km | DECIMAL(8,2) | NULL | Distância total em km |
| total_duration_minutes | INTEGER | NULL | Duração total estimada em minutos |
| estimated_fuel_cost | DECIMAL(8,2) | NULL | Custo estimado de combustível |
| estimated_revenue | DECIMAL(8,2) | NULL | Receita estimada total |
| actual_distance_km | DECIMAL(8,2) | NULL | Distância real percorrida |
| actual_duration_minutes | INTEGER | NULL | Duração real |
| actual_fuel_cost | DECIMAL(8,2) | NULL | Custo real de combustível |
| started_at | TIMESTAMP | NULL | Quando iniciou a rota |
| completed_at | TIMESTAMP | NULL | Quando completou a rota |
| optimization_metadata | JSONB | NULL | Metadados da otimização (API usada, parâmetros, etc.) |
| created_at | TIMESTAMP | NOT NULL | Data de criação |
| updated_at | TIMESTAMP | NOT NULL | Data de atualização |

**Constraints:**
- `FOREIGN KEY(driver_id) REFERENCES drivers(id) ON DELETE CASCADE`
- `CHECK(total_distance_km >= 0)`
- `CHECK(total_duration_minutes >= 0)`

**Índices:**
- `idx_routes_driver_id` (driver_id)
- `idx_routes_status` (status)
- `idx_routes_created_at` (created_at DESC)
- `idx_routes_driver_status` (driver_id, status)

---

### 1.4 Address (Endereço)

**Descrição:** Representa um endereço de entrega. Pode ser reutilizado entre múltiplas entregas.

**Tabela:** `addresses`

| Atributo | Tipo | Nullable | Descrição |
|----------|------|----------|-----------|
| id | UUID | NOT NULL | Identificador único (PK) |
| street | VARCHAR(200) | NOT NULL | Logradouro (rua, avenida, etc.) |
| number | VARCHAR(20) | NOT NULL | Número |
| complement | VARCHAR(100) | NULL | Complemento (apto, bloco, etc.) |
| neighborhood | VARCHAR(100) | NOT NULL | Bairro |
| city | VARCHAR(100) | NOT NULL | Cidade |
| state | VARCHAR(2) | NOT NULL | UF (ex: SP, RJ) |
| postal_code | VARCHAR(10) | NOT NULL | CEP (formato: 12345-678) |
| reference_point | VARCHAR(200) | NULL | Ponto de referência |
| latitude | DECIMAL(10,8) | NULL | Latitude (geocoding) |
| longitude | DECIMAL(11,8) | NULL | Longitude (geocoding) |
| formatted_address | VARCHAR(500) | NULL | Endereço formatado completo |
| geocoded_at | TIMESTAMP | NULL | Quando foi geocodificado |
| created_at | TIMESTAMP | NOT NULL | Data de criação |
| updated_at | TIMESTAMP | NOT NULL | Data de atualização |

**Constraints:**
- `CHECK(latitude BETWEEN -90 AND 90)`
- `CHECK(longitude BETWEEN -180 AND 180)`

**Índices:**
- `idx_addresses_postal_code` (postal_code)
- `idx_addresses_city_state` (city, state)
- `idx_addresses_coordinates` (latitude, longitude) - Para buscas geoespaciais

**Observações:**
- Endereços são normalizados para evitar duplicação
- Geocoding é feito assincronamente se não fornecido

---

### 1.5 Delivery (Entrega)

**Descrição:** Representa uma entrega individual dentro de uma rota.

**Tabela:** `deliveries`

| Atributo | Tipo | Nullable | Descrição |
|----------|------|----------|-----------|
| id | UUID | NOT NULL | Identificador único (PK) |
| route_id | UUID | NULL | Referência à rota (FK) - pode ser NULL se não otimizada ainda |
| driver_id | UUID | NOT NULL | Referência ao entregador (FK) |
| address_id | UUID | NOT NULL | Referência ao endereço (FK) |
| platform_id | UUID | NOT NULL | Referência à plataforma (FK) |
| external_id | VARCHAR(100) | NULL | ID da entrega na plataforma externa |
| sequence_order | INTEGER | NULL | Ordem na rota otimizada (1, 2, 3...) |
| status | ENUM | NOT NULL | Status (PENDING, IN_PROGRESS, COMPLETED, FAILED, CANCELLED) |
| time_window_start | TIME | NULL | Janela de entrega - início |
| time_window_end | TIME | NULL | Janela de entrega - fim |
| delivery_value | DECIMAL(8,2) | NOT NULL | Valor da entrega |
| customer_name | VARCHAR(100) | NULL | Nome do cliente |
| customer_phone | VARCHAR(20) | NULL | Telefone do cliente |
| notes | TEXT | NULL | Observações sobre a entrega |
| proof_of_delivery_url | VARCHAR(500) | NULL | URL da foto de comprovante |
| completed_at | TIMESTAMP | NULL | Quando foi concluída |
| failed_reason | VARCHAR(255) | NULL | Motivo da falha (se status = FAILED) |
| created_at | TIMESTAMP | NOT NULL | Data de criação |
| updated_at | TIMESTAMP | NOT NULL | Data de atualização |

**Constraints:**
- `FOREIGN KEY(route_id) REFERENCES routes(id) ON DELETE SET NULL`
- `FOREIGN KEY(driver_id) REFERENCES drivers(id) ON DELETE CASCADE`
- `FOREIGN KEY(address_id) REFERENCES addresses(id) ON DELETE RESTRICT`
- `FOREIGN KEY(platform_id) REFERENCES platforms(id) ON DELETE RESTRICT`
- `CHECK(delivery_value >= 0)`
- `CHECK(time_window_end > time_window_start)`

**Índices:**
- `idx_deliveries_route_id` (route_id)
- `idx_deliveries_driver_id` (driver_id)
- `idx_deliveries_status` (status)
- `idx_deliveries_driver_status` (driver_id, status)
- `idx_deliveries_created_at` (created_at DESC)

---

### 1.6 FinanceRecord (Registro Financeiro)

**Descrição:** Representa um registro financeiro (ganho ou despesa) do entregador.

**Tabela:** `finance_records`

| Atributo | Tipo | Nullable | Descrição |
|----------|------|----------|-----------|
| id | UUID | NOT NULL | Identificador único (PK) |
| driver_id | UUID | NOT NULL | Referência ao entregador (FK) |
| delivery_id | UUID | NULL | Referência à entrega (FK) - se for ganho de entrega |
| platform_id | UUID | NULL | Referência à plataforma (FK) - se aplicável |
| record_type | ENUM | NOT NULL | Tipo (REVENUE, EXPENSE) |
| category | ENUM | NOT NULL | Categoria (DELIVERY_FEE, FUEL, MAINTENANCE, etc.) |
| amount | DECIMAL(10,2) | NOT NULL | Valor (positivo para receita, positivo para despesa também) |
| description | VARCHAR(255) | NULL | Descrição |
| receipt_url | VARCHAR(500) | NULL | URL do comprovante/nota fiscal |
| transaction_date | DATE | NOT NULL | Data da transação |
| created_at | TIMESTAMP | NOT NULL | Data de criação |
| updated_at | TIMESTAMP | NOT NULL | Data de atualização |

**Constraints:**
- `FOREIGN KEY(driver_id) REFERENCES drivers(id) ON DELETE CASCADE`
- `FOREIGN KEY(delivery_id) REFERENCES deliveries(id) ON DELETE SET NULL`
- `FOREIGN KEY(platform_id) REFERENCES platforms(id) ON DELETE SET NULL`
- `CHECK(amount > 0)` - Sempre positivo, o tipo define se é receita ou despesa

**Índices:**
- `idx_finance_driver_id` (driver_id)
- `idx_finance_driver_date` (driver_id, transaction_date DESC)
- `idx_finance_type` (record_type)
- `idx_finance_category` (category)

**Observações:**
- Receitas de entregas são criadas automaticamente ao completar uma entrega
- Despesas são registradas manualmente pelo entregador

---

## 2. Enumerações

### 2.1 VehicleType
```sql
CREATE TYPE vehicle_type AS ENUM (
    'MOTORCYCLE',    -- Moto
    'CAR',          -- Carro
    'VAN',          -- Van
    'BICYCLE'       -- Bicicleta (futuro)
);
```

### 2.2 PlatformType
```sql
CREATE TYPE platform_type AS ENUM (
    'LOGGI',
    'MERCADO_LIVRE_FLEX',
    'AMAZON_FLEX',
    'LALAMOVE',
    'SHOPEE_XPRESS',
    'RAPPI',
    'IFOOD',
    'UBER_FLASH',
    'OTHER'
);
```

### 2.3 AccountStatus
```sql
CREATE TYPE account_status AS ENUM (
    'ACTIVE',       -- Conta ativa
    'INACTIVE',     -- Inativa (usuário pausou)
    'SUSPENDED',    -- Suspensa (violação)
    'PENDING'       -- Aguardando verificação
);
```

### 2.4 SubscriptionType
```sql
CREATE TYPE subscription_type AS ENUM (
    'FREE',         -- Plano gratuito
    'PREMIUM',      -- Plano premium
    'TRIAL'         -- Trial (7 dias)
);
```

### 2.5 RouteStatus
```sql
CREATE TYPE route_status AS ENUM (
    'PENDING',      -- Aguardando início
    'IN_PROGRESS',  -- Em execução
    'COMPLETED',    -- Concluída
    'CANCELLED'     -- Cancelada
);
```

### 2.6 OptimizationType
```sql
CREATE TYPE optimization_type AS ENUM (
    'SHORTEST_DISTANCE',    -- Menor distância
    'SHORTEST_TIME',        -- Menor tempo
    'RESPECT_TIME_WINDOWS', -- Respeitar janelas de horário
    'BALANCED'              -- Balanceado (tempo + distância)
);
```

### 2.7 DeliveryStatus
```sql
CREATE TYPE delivery_status AS ENUM (
    'PENDING',      -- Aguardando
    'IN_PROGRESS',  -- Em andamento
    'COMPLETED',    -- Concluída
    'FAILED',       -- Falhou
    'CANCELLED'     -- Cancelada
);
```

### 2.8 NavigationApp
```sql
CREATE TYPE navigation_app AS ENUM (
    'GOOGLE_MAPS',
    'WAZE',
    'APPLE_MAPS'
);
```

### 2.9 FinanceRecordType
```sql
CREATE TYPE finance_record_type AS ENUM (
    'REVENUE',      -- Receita
    'EXPENSE'       -- Despesa
);
```

### 2.10 FinanceCategory
```sql
CREATE TYPE finance_category AS ENUM (
    -- Receitas
    'DELIVERY_FEE',         -- Taxa de entrega
    'TIP',                  -- Gorjeta
    'BONUS',                -- Bônus da plataforma

    -- Despesas
    'FUEL',                 -- Combustível
    'MAINTENANCE',          -- Manutenção
    'PARKING',              -- Estacionamento
    'TOLL',                 -- Pedágio
    'FOOD',                 -- Alimentação
    'PHONE_DATA',           -- Internet móvel
    'VEHICLE_RENT',         -- Aluguel de veículo
    'INSURANCE',            -- Seguro
    'OTHER'                 -- Outros
);
```

---

## 3. Relacionamentos

### Diagrama de Relacionamentos (Texto)

```
Driver (1) ──────< (N) Platform
  │
  ├──────< (N) Route
  │           │
  │           └──────< (N) Delivery
  │                       │
  ├──────< (N) Delivery   │
  │                       │
  └──────< (N) FinanceRecord
                          │
              (N) >───────┘

Address (1) ──────< (N) Delivery

Platform (1) ──────< (N) Delivery
Platform (1) ──────< (N) FinanceRecord
```

### Descrição dos Relacionamentos

| Entidade Origem | Entidade Destino | Tipo | Descrição |
|-----------------|------------------|------|-----------|
| Driver | Platform | 1:N | Um entregador pode ter múltiplas plataformas |
| Driver | Route | 1:N | Um entregador pode ter múltiplas rotas |
| Driver | Delivery | 1:N | Um entregador pode ter múltiplas entregas |
| Driver | FinanceRecord | 1:N | Um entregador pode ter múltiplos registros financeiros |
| Route | Delivery | 1:N | Uma rota contém múltiplas entregas |
| Address | Delivery | 1:N | Um endereço pode ser usado em múltiplas entregas |
| Platform | Delivery | 1:N | Uma plataforma pode ter múltiplas entregas |
| Platform | FinanceRecord | 1:N | Uma plataforma pode ter múltiplos registros financeiros |
| Delivery | FinanceRecord | 1:1 | Uma entrega pode gerar um registro financeiro |

---

## 4. Diagrama ER

```
┌─────────────────────────────────────────────────────────────────┐
│                            DRIVER                                │
├─────────────────────────────────────────────────────────────────┤
│ PK │ id: UUID                                                    │
│    │ name: VARCHAR(100)                                         │
│    │ email: VARCHAR(150) UNIQUE                                 │
│    │ phone: VARCHAR(20) UNIQUE                                  │
│    │ password_hash: VARCHAR(255)                                │
│    │ vehicle_type: ENUM                                         │
│    │ region: VARCHAR(100)                                       │
│    │ fuel_cost_per_liter: DECIMAL(5,2)                         │
│    │ vehicle_consumption: DECIMAL(5,2)                         │
│    │ default_start_location: JSONB                             │
│    │ preferred_navigation_app: ENUM                            │
│    │ account_status: ENUM                                      │
│    │ subscription_type: ENUM                                   │
│    │ subscription_expires_at: TIMESTAMP                        │
│    │ email_verified: BOOLEAN                                   │
│    │ email_verified_at: TIMESTAMP                              │
│    │ created_at, updated_at, deleted_at                        │
└──────────┬──────────────────────────────────────────────────────┘
           │
           │ 1:N
           │
┌──────────▼──────────────────────────────────────────────────────┐
│                          PLATFORM                                │
├─────────────────────────────────────────────────────────────────┤
│ PK │ id: UUID                                                    │
│ FK │ driver_id: UUID                                            │
│    │ platform_type: ENUM                                        │
│    │ is_active: BOOLEAN                                         │
│    │ account_identifier: VARCHAR(100)                           │
│    │ created_at, updated_at                                     │
└──────────┬──────────────────────────────────────────────────────┘
           │
           │
┌──────────┴──────────────────────────────────────────────────────┐
│                            ROUTE                                 │
├─────────────────────────────────────────────────────────────────┤
│ PK │ id: UUID                                                    │
│ FK │ driver_id: UUID                                            │
│    │ name: VARCHAR(100)                                         │
│    │ status: ENUM                                               │
│    │ optimization_type: ENUM                                    │
│    │ start_location: JSONB                                      │
│    │ end_location: JSONB                                        │
│    │ total_distance_km: DECIMAL(8,2)                           │
│    │ total_duration_minutes: INTEGER                           │
│    │ estimated_fuel_cost: DECIMAL(8,2)                         │
│    │ estimated_revenue: DECIMAL(8,2)                           │
│    │ actual_distance_km: DECIMAL(8,2)                          │
│    │ actual_duration_minutes: INTEGER                          │
│    │ actual_fuel_cost: DECIMAL(8,2)                            │
│    │ started_at, completed_at                                   │
│    │ optimization_metadata: JSONB                              │
│    │ created_at, updated_at                                     │
└──────────┬──────────────────────────────────────────────────────┘
           │
           │ 1:N
           │
┌──────────▼──────────────────────────────────────────────────────┐
│                          DELIVERY                                │
├─────────────────────────────────────────────────────────────────┤
│ PK │ id: UUID                                                    │
│ FK │ route_id: UUID (nullable)                                  │
│ FK │ driver_id: UUID                                            │
│ FK │ address_id: UUID                                           │
│ FK │ platform_id: UUID                                          │
│    │ external_id: VARCHAR(100)                                  │
│    │ sequence_order: INTEGER                                    │
│    │ status: ENUM                                               │
│    │ time_window_start: TIME                                    │
│    │ time_window_end: TIME                                      │
│    │ delivery_value: DECIMAL(8,2)                              │
│    │ customer_name: VARCHAR(100)                                │
│    │ customer_phone: VARCHAR(20)                                │
│    │ notes: TEXT                                                │
│    │ proof_of_delivery_url: VARCHAR(500)                       │
│    │ completed_at: TIMESTAMP                                    │
│    │ failed_reason: VARCHAR(255)                                │
│    │ created_at, updated_at                                     │
└──────────┬──────────────────────────────────────────────────────┘
           │
           │ N:1
           │
┌──────────▼──────────────────────────────────────────────────────┐
│                          ADDRESS                                 │
├─────────────────────────────────────────────────────────────────┤
│ PK │ id: UUID                                                    │
│    │ street: VARCHAR(200)                                       │
│    │ number: VARCHAR(20)                                        │
│    │ complement: VARCHAR(100)                                   │
│    │ neighborhood: VARCHAR(100)                                 │
│    │ city: VARCHAR(100)                                         │
│    │ state: VARCHAR(2)                                          │
│    │ postal_code: VARCHAR(10)                                   │
│    │ reference_point: VARCHAR(200)                              │
│    │ latitude: DECIMAL(10,8)                                    │
│    │ longitude: DECIMAL(11,8)                                   │
│    │ formatted_address: VARCHAR(500)                            │
│    │ geocoded_at: TIMESTAMP                                     │
│    │ created_at, updated_at                                     │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                       FINANCE_RECORD                             │
├─────────────────────────────────────────────────────────────────┤
│ PK │ id: UUID                                                    │
│ FK │ driver_id: UUID                                            │
│ FK │ delivery_id: UUID (nullable)                               │
│ FK │ platform_id: UUID (nullable)                               │
│    │ record_type: ENUM                                          │
│    │ category: ENUM                                             │
│    │ amount: DECIMAL(10,2)                                      │
│    │ description: VARCHAR(255)                                  │
│    │ receipt_url: VARCHAR(500)                                  │
│    │ transaction_date: DATE                                     │
│    │ created_at, updated_at                                     │
└─────────────────────────────────────────────────────────────────┘
```

---

## 5. Regras de Negócio

### 5.1 Driver (Entregador)

**RN001:** Um entregador não pode ter email ou telefone duplicado no sistema.

**RN002:** A senha deve ter no mínimo 8 caracteres, incluindo letras e números.

**RN003:** Um entregador FREE pode ter no máximo 5 entregas ativas por dia.

**RN004:** Um entregador PREMIUM tem entregas ilimitadas.

**RN005:** Ao excluir um entregador (soft delete), todas as suas entidades relacionadas são mantidas para histórico.

**RN006:** O custo de combustível e consumo do veículo são opcionais, mas necessários para cálculo de custos.

---

### 5.2 Route (Rota)

**RN007:** Uma rota só pode ser marcada como IN_PROGRESS se tiver pelo menos 1 entrega associada.

**RN008:** Uma rota COMPLETED deve ter todas as entregas em status COMPLETED, FAILED ou CANCELLED.

**RN009:** Ao completar uma rota, os valores reais (distância, tempo, custo) devem ser preenchidos.

**RN010:** Uma rota não pode ter entregas de diferentes drivers.

**RN011:** A distância total de uma rota é a soma das distâncias entre pontos sequenciais.

---

### 5.3 Delivery (Entrega)

**RN012:** Uma entrega só pode pertencer a uma rota se for do mesmo driver.

**RN013:** Ao completar uma entrega (status = COMPLETED), um FinanceRecord do tipo REVENUE deve ser criado automaticamente.

**RN014:** Uma entrega com status FAILED deve ter um motivo preenchido (failed_reason).

**RN015:** A janela de horário (time_window) é opcional, mas se preenchida, time_window_end deve ser maior que time_window_start.

**RN016:** Uma entrega não pode ser excluída se já estiver COMPLETED (apenas CANCELLED).

**RN017:** Ao marcar como COMPLETED, a data completed_at deve ser preenchida.

---

### 5.4 Address (Endereço)

**RN018:** Ao criar um endereço, o sistema deve tentar geocodificar (obter lat/lng) usando API externa.

**RN019:** Se o geocoding falhar, o endereço pode ser salvo sem coordenadas, mas uma tentativa de geocoding deve ser agendada.

**RN020:** Endereços são normalizados: mesma rua/número/cidade = mesmo registro.

**RN021:** CEP deve seguir o formato brasileiro (12345-678).

---

### 5.5 FinanceRecord (Registro Financeiro)

**RN022:** Receitas de entregas (DELIVERY_FEE) são criadas automaticamente ao completar uma entrega.

**RN023:** Despesas são sempre registradas manualmente pelo entregador.

**RN024:** O campo amount é sempre positivo, independente de ser REVENUE ou EXPENSE.

**RN025:** A data da transação (transaction_date) não pode ser futura.

**RN026:** Um FinanceRecord vinculado a uma entrega não pode ser editado/excluído manualmente.

---

### 5.6 Platform (Plataforma)

**RN027:** Um entregador não pode ter a mesma plataforma duplicada (unique constraint).

**RN028:** Ao desativar uma plataforma (is_active = false), todas as entregas futuras dessa plataforma devem ser alertadas.

**RN029:** Plataformas não podem ser excluídas se houverem entregas ou registros financeiros associados.

---

## 6. Índices e Performance

### 6.1 Índices Compostos Recomendados

```sql
-- Buscar entregas de um driver por status
CREATE INDEX idx_deliveries_driver_status ON deliveries(driver_id, status);

-- Buscar rotas de um driver por data
CREATE INDEX idx_routes_driver_created ON routes(driver_id, created_at DESC);

-- Buscar registros financeiros por driver e período
CREATE INDEX idx_finance_driver_date ON finance_records(driver_id, transaction_date DESC);

-- Buscar entregas por data (para dashboards)
CREATE INDEX idx_deliveries_created_date ON deliveries(created_at::DATE DESC);

-- Buscar endereços por CEP (autocomplete)
CREATE INDEX idx_addresses_postal_code ON addresses(postal_code);

-- Busca geoespacial de endereços
CREATE INDEX idx_addresses_coordinates ON addresses USING GIST(
    point(longitude, latitude)
);
```

### 6.2 Estratégias de Cache (Redis)

**Cache de Dados Frequentes:**
- Dados do driver logado (TTL: 30 min)
- Rotas ativas do dia (TTL: 10 min)
- Configurações de custo de combustível (TTL: 1 hora)

**Cache de Cálculos:**
- Resultados de geocoding (TTL: 7 dias)
- Rotas otimizadas (TTL: 1 hora)
- Estatísticas agregadas (TTL: 15 min)

---

## 7. Entidades Futuras (Pós-MVP)

### 7.1 Notification (Notificação)

```sql
CREATE TABLE notifications (
    id UUID PRIMARY KEY,
    driver_id UUID NOT NULL REFERENCES drivers(id),
    type VARCHAR(50) NOT NULL, -- CONFLICT, SUGGESTION, REMINDER, etc.
    title VARCHAR(100) NOT NULL,
    message TEXT NOT NULL,
    data JSONB, -- Dados adicionais
    is_read BOOLEAN DEFAULT FALSE,
    read_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL
);
```

---

### 7.2 Achievement (Conquista/Gamificação)

```sql
CREATE TABLE achievements (
    id UUID PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    icon_url VARCHAR(500),
    required_value INTEGER, -- Ex: 100 entregas
    reward_description VARCHAR(255)
);

CREATE TABLE driver_achievements (
    id UUID PRIMARY KEY,
    driver_id UUID NOT NULL REFERENCES drivers(id),
    achievement_id UUID NOT NULL REFERENCES achievements(id),
    unlocked_at TIMESTAMP NOT NULL,
    UNIQUE(driver_id, achievement_id)
);
```

---

### 7.3 Subscription (Controle de Assinatura)

```sql
CREATE TABLE subscriptions (
    id UUID PRIMARY KEY,
    driver_id UUID NOT NULL REFERENCES drivers(id),
    plan_type VARCHAR(20) NOT NULL, -- FREE, PREMIUM
    status VARCHAR(20) NOT NULL, -- ACTIVE, CANCELLED, EXPIRED
    started_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP,
    payment_method VARCHAR(50),
    payment_metadata JSONB,
    cancelled_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

---

### 7.4 RouteConflict (Conflito de Rota)

```sql
CREATE TABLE route_conflicts (
    id UUID PRIMARY KEY,
    driver_id UUID NOT NULL REFERENCES drivers(id),
    delivery_a_id UUID NOT NULL REFERENCES deliveries(id),
    delivery_b_id UUID NOT NULL REFERENCES deliveries(id),
    conflict_type VARCHAR(50) NOT NULL, -- TIME_OVERLAP, DISTANCE_ISSUE
    severity VARCHAR(20) NOT NULL, -- LOW, MEDIUM, HIGH
    suggested_resolution TEXT,
    resolved BOOLEAN DEFAULT FALSE,
    resolved_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL
);
```

---

### 7.5 ImportLog (Log de Importações)

```sql
CREATE TABLE import_logs (
    id UUID PRIMARY KEY,
    driver_id UUID NOT NULL REFERENCES drivers(id),
    import_type VARCHAR(20) NOT NULL, -- MANUAL, CSV, OCR
    file_url VARCHAR(500), -- Se CSV ou OCR
    total_records INTEGER NOT NULL,
    successful_records INTEGER NOT NULL,
    failed_records INTEGER NOT NULL,
    errors JSONB, -- Detalhes dos erros
    created_at TIMESTAMP NOT NULL
);
```

---

## 8. Scripts de Criação (SQL)

### Script Completo - Schema Inicial

```sql
-- ============================================================
-- DATABASE SCHEMA - Routing Backend
-- Versão: 1.0.0
-- Data: 2025-11-01
-- ============================================================

-- Extensões necessárias
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "postgis"; -- Para operações geoespaciais

-- ============================================================
-- ENUMS
-- ============================================================

CREATE TYPE vehicle_type AS ENUM ('MOTORCYCLE', 'CAR', 'VAN', 'BICYCLE');
CREATE TYPE platform_type AS ENUM ('LOGGI', 'MERCADO_LIVRE_FLEX', 'AMAZON_FLEX', 'LALAMOVE', 'SHOPEE_XPRESS', 'RAPPI', 'IFOOD', 'UBER_FLASH', 'OTHER');
CREATE TYPE account_status AS ENUM ('ACTIVE', 'INACTIVE', 'SUSPENDED', 'PENDING');
CREATE TYPE subscription_type AS ENUM ('FREE', 'PREMIUM', 'TRIAL');
CREATE TYPE route_status AS ENUM ('PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED');
CREATE TYPE optimization_type AS ENUM ('SHORTEST_DISTANCE', 'SHORTEST_TIME', 'RESPECT_TIME_WINDOWS', 'BALANCED');
CREATE TYPE delivery_status AS ENUM ('PENDING', 'IN_PROGRESS', 'COMPLETED', 'FAILED', 'CANCELLED');
CREATE TYPE navigation_app AS ENUM ('GOOGLE_MAPS', 'WAZE', 'APPLE_MAPS');
CREATE TYPE finance_record_type AS ENUM ('REVENUE', 'EXPENSE');
CREATE TYPE finance_category AS ENUM ('DELIVERY_FEE', 'TIP', 'BONUS', 'FUEL', 'MAINTENANCE', 'PARKING', 'TOLL', 'FOOD', 'PHONE_DATA', 'VEHICLE_RENT', 'INSURANCE', 'OTHER');

-- ============================================================
-- TABELAS
-- ============================================================

-- DRIVERS
CREATE TABLE drivers (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    vehicle_type vehicle_type NOT NULL,
    region VARCHAR(100),
    fuel_cost_per_liter DECIMAL(5,2) CHECK (fuel_cost_per_liter > 0),
    vehicle_consumption DECIMAL(5,2) CHECK (vehicle_consumption > 0),
    default_start_location JSONB,
    preferred_navigation_app navigation_app,
    account_status account_status NOT NULL DEFAULT 'PENDING',
    subscription_type subscription_type NOT NULL DEFAULT 'FREE',
    subscription_expires_at TIMESTAMP,
    email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    email_verified_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE INDEX idx_drivers_email ON drivers(email);
CREATE INDEX idx_drivers_phone ON drivers(phone);
CREATE INDEX idx_drivers_subscription_type ON drivers(subscription_type);
CREATE INDEX idx_drivers_account_status ON drivers(account_status);

-- PLATFORMS
CREATE TABLE platforms (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    driver_id UUID NOT NULL REFERENCES drivers(id) ON DELETE CASCADE,
    platform_type platform_type NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    account_identifier VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(driver_id, platform_type)
);

CREATE INDEX idx_platforms_driver_id ON platforms(driver_id);
CREATE INDEX idx_platforms_type ON platforms(platform_type);

-- ADDRESSES
CREATE TABLE addresses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    street VARCHAR(200) NOT NULL,
    number VARCHAR(20) NOT NULL,
    complement VARCHAR(100),
    neighborhood VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(2) NOT NULL,
    postal_code VARCHAR(10) NOT NULL,
    reference_point VARCHAR(200),
    latitude DECIMAL(10,8) CHECK (latitude BETWEEN -90 AND 90),
    longitude DECIMAL(11,8) CHECK (longitude BETWEEN -180 AND 180),
    formatted_address VARCHAR(500),
    geocoded_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_addresses_postal_code ON addresses(postal_code);
CREATE INDEX idx_addresses_city_state ON addresses(city, state);

-- ROUTES
CREATE TABLE routes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    driver_id UUID NOT NULL REFERENCES drivers(id) ON DELETE CASCADE,
    name VARCHAR(100),
    status route_status NOT NULL DEFAULT 'PENDING',
    optimization_type optimization_type NOT NULL,
    start_location JSONB NOT NULL,
    end_location JSONB,
    total_distance_km DECIMAL(8,2) CHECK (total_distance_km >= 0),
    total_duration_minutes INTEGER CHECK (total_duration_minutes >= 0),
    estimated_fuel_cost DECIMAL(8,2),
    estimated_revenue DECIMAL(8,2),
    actual_distance_km DECIMAL(8,2),
    actual_duration_minutes INTEGER,
    actual_fuel_cost DECIMAL(8,2),
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    optimization_metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_routes_driver_id ON routes(driver_id);
CREATE INDEX idx_routes_status ON routes(status);
CREATE INDEX idx_routes_created_at ON routes(created_at DESC);
CREATE INDEX idx_routes_driver_status ON routes(driver_id, status);

-- DELIVERIES
CREATE TABLE deliveries (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    route_id UUID REFERENCES routes(id) ON DELETE SET NULL,
    driver_id UUID NOT NULL REFERENCES drivers(id) ON DELETE CASCADE,
    address_id UUID NOT NULL REFERENCES addresses(id) ON DELETE RESTRICT,
    platform_id UUID NOT NULL REFERENCES platforms(id) ON DELETE RESTRICT,
    external_id VARCHAR(100),
    sequence_order INTEGER,
    status delivery_status NOT NULL DEFAULT 'PENDING',
    time_window_start TIME,
    time_window_end TIME CHECK (time_window_end > time_window_start),
    delivery_value DECIMAL(8,2) NOT NULL CHECK (delivery_value >= 0),
    customer_name VARCHAR(100),
    customer_phone VARCHAR(20),
    notes TEXT,
    proof_of_delivery_url VARCHAR(500),
    completed_at TIMESTAMP,
    failed_reason VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_deliveries_route_id ON deliveries(route_id);
CREATE INDEX idx_deliveries_driver_id ON deliveries(driver_id);
CREATE INDEX idx_deliveries_status ON deliveries(status);
CREATE INDEX idx_deliveries_driver_status ON deliveries(driver_id, status);
CREATE INDEX idx_deliveries_created_at ON deliveries(created_at DESC);

-- FINANCE_RECORDS
CREATE TABLE finance_records (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    driver_id UUID NOT NULL REFERENCES drivers(id) ON DELETE CASCADE,
    delivery_id UUID REFERENCES deliveries(id) ON DELETE SET NULL,
    platform_id UUID REFERENCES platforms(id) ON DELETE SET NULL,
    record_type finance_record_type NOT NULL,
    category finance_category NOT NULL,
    amount DECIMAL(10,2) NOT NULL CHECK (amount > 0),
    description VARCHAR(255),
    receipt_url VARCHAR(500),
    transaction_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_finance_driver_id ON finance_records(driver_id);
CREATE INDEX idx_finance_driver_date ON finance_records(driver_id, transaction_date DESC);
CREATE INDEX idx_finance_type ON finance_records(record_type);
CREATE INDEX idx_finance_category ON finance_records(category);

-- ============================================================
-- TRIGGERS - Updated At
-- ============================================================

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_drivers_updated_at BEFORE UPDATE ON drivers
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_platforms_updated_at BEFORE UPDATE ON platforms
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_addresses_updated_at BEFORE UPDATE ON addresses
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_routes_updated_at BEFORE UPDATE ON routes
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_deliveries_updated_at BEFORE UPDATE ON deliveries
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_finance_records_updated_at BEFORE UPDATE ON finance_records
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
```

---

## 9. Considerações Finais

### 9.1 Normalização

O modelo segue a **Terceira Forma Normal (3NF)**, garantindo:
- Eliminação de redundâncias
- Integridade referencial
- Facilidade de manutenção

### 9.2 Escalabilidade

- Uso de UUIDs permite distribuição futura
- Índices estratégicos para queries frequentes
- JSONB para dados flexíveis (metadados, localizações)
- Soft deletes para auditoria

### 9.3 Segurança

- Senhas sempre hasheadas (BCrypt)
- Soft delete para preservar histórico
- Foreign keys com ON DELETE apropriados
- Constraints para validação de dados

### 9.4 Próximos Passos

1. Implementar migrations com Flyway
2. Criar entidades JPA correspondentes
3. Implementar repositories com Spring Data
4. Adicionar validações no lado da aplicação
5. Criar DTOs para cada entidade

---

**Versão:** 1.0.0
**Data:** 2025-11-01
**Status:** MVP - Entidades Core Definidas
