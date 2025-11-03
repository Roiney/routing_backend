# 🎯 Fluxo do Usuário - Plataforma de Roteirização Multi-Plataforma

## Visão Geral

Este documento detalha a jornada completa do usuário (entregador autônomo) na plataforma, desde o primeiro contato até o uso diário recorrente.

---

## 1. Onboarding (Primeira Experiência)

### 1.1 Descoberta e Cadastro

```
Landing Page → Cadastro Simples
├─ Nome completo
├─ Email
├─ Telefone
├─ Tipo de veículo (Moto/Carro/Van)
├─ Região de atuação
└─ Senha
```

**Tempo estimado:** 2 minutos

**Campos obrigatórios:**
- Nome completo
- Email (validação)
- Telefone (com máscara)
- Tipo de veículo
- Senha (mínimo 8 caracteres)

**Validações:**
- Email único no sistema
- Telefone com formato válido
- Senha segura (letras, números, caracteres especiais)

---

### 1.2 Configuração Inicial

```
Bem-vindo → Tour rápido (skippable)
├─ "Importe suas entregas"
├─ "Veja sua rota otimizada"
├─ "Acompanhe seus ganhos"
└─ Selecionar plataformas que usa
    ├─ ☑ Loggi
    ├─ ☑ Mercado Livre Flex
    ├─ ☑ Amazon Flex
    ├─ ☑ Lalamove
    ├─ ☑ Shopee Xpress
    └─ ☑ Outras
```

**Tempo estimado:** 1 minuto

**Objetivo:** Configurar preferências iniciais e familiarizar o usuário com as principais funcionalidades.

**Opções:**
- Tour pode ser pulado (botão "Pular" visível)
- Usuário pode adicionar/remover plataformas depois
- Seleção de múltiplas plataformas

---

## 2. Fluxo Diário (Uso Recorrente)

### Manhã - Planejamento do Dia

```
📱 Abre o App
↓
🏠 Dashboard Inicial
├─ "Bom dia, João! Você tem 12 entregas hoje"
├─ Resumo rápido:
│   ├─ 5 entregas Loggi (9h-12h)
│   ├─ 4 entregas Flex (13h-16h)
│   └─ 3 entregas Lalamove (17h-19h)
├─ Estatísticas rápidas:
│   ├─ Distância total estimada: 45 km
│   ├─ Tempo estimado: 6h 30min
│   └─ Ganho estimado: R$ 145,00
└─ [+ Adicionar Entregas]
```

**Elementos visuais:**
- Cards coloridos por plataforma
- Indicadores de horário
- Progresso do dia (barra)
- CTA principal destacado

---

### 2.1 Importar Entregas do Dia

```
[+ Adicionar Entregas]
↓
Escolha o método:
├─ 📝 Manual (digitar endereços)
├─ 📄 CSV (upload de arquivo)
├─ 📸 Foto da Tela (OCR) ← Diferencial!
└─ 🔗 API (futuro)
```

#### Opção A: Importação Manual

```
Adicionar Entrega Manual
├─ Selecionar plataforma: [Loggi ▼]
├─ Endereço completo: Rua ABC, 123
├─ Bairro: Centro
├─ Cidade: São Paulo - SP
├─ CEP: 01234-567 (autocomplete)
├─ Complemento: Apto 45
├─ Ponto de referência: Próximo ao metrô
├─ Janela de horário:
│   ├─ Início: 10:00
│   └─ Fim: 12:00
├─ Valor da entrega: R$ 8,50
├─ Observações: (opcional)
└─ [Salvar] [+ Adicionar Outra]
```

**Validações:**
- CEP válido com busca automática de endereço
- Horários sem conflito com entregas existentes
- Valor numérico positivo

**UX:**
- Autocomplete de endereço ao digitar CEP
- Sugestão de endereços recentes
- Opção de salvar endereços favoritos

---

#### Opção B: Importação via CSV

```
Upload de CSV
↓
1. Baixar template [📥 Template.csv]
2. Preencher planilha
3. Upload [Escolher arquivo]
↓
⏳ Processando arquivo...
↓
✅ 15 entregas importadas
├─ 13 válidas ✓
└─ 2 com erro ⚠️
    ├─ Linha 5: CEP inválido
    └─ Linha 12: Horário em conflito

[Corrigir Erros] [Importar Válidas]
```

**Formato CSV esperado:**
```csv
plataforma,endereco,numero,complemento,bairro,cidade,estado,cep,horario_inicio,horario_fim,valor
Loggi,Rua ABC,123,Apto 45,Centro,São Paulo,SP,01234-567,10:00,12:00,8.50
```

---

#### Opção C: Importação via OCR (DIFERENCIAL!)

```
📸 Tirar Foto da Tela do App
↓
[Guia visual mostrando como posicionar]
"Centralize a lista de entregas na tela"
↓
📷 [Capturar]
↓
⏳ Processando imagem com IA...
↓
✅ 5 endereços detectados
├─ 1. Rua ABC, 123 - 10h-12h - R$ 8,50 ✓
├─ 2. Av XYZ, 456 - 11h-13h - R$ 9,00 ✓
├─ 3. Rua DEF, 789 - 14h-16h - R$ 7,50 ✓
├─ 4. [Editar] ⚠️ (baixa confiança)
└─ 5. Praça GHI, 321 - 15h-17h - R$ 10,00 ✓

[Confirmar Tudo] [Revisar]
```

**Features:**
- Detecção automática de endereços
- Extração de horários e valores
- Indicador de confiança da detecção
- Possibilidade de editar antes de confirmar

**Limitações do OCR:**
- Apenas em plano Premium
- Máximo 20 entregas por foto
- Formatos suportados: JPG, PNG

---

### 2.2 Gerar Rota Otimizada

```
[Otimizar Rota] ← CTA Principal
↓
Configurações de otimização:
├─ Priorizar: [Menor distância ▼]
│   ├─ Menor distância
│   ├─ Menor tempo
│   └─ Respeitar horários
├─ Ponto de partida: [Minha localização atual ▼]
└─ Ponto final: [Retornar ao início ▼]

[Calcular Rota]
↓
⏳ Calculando melhor rota...
↓
🗺️ Rota Otimizada Gerada
├─ Distância total: 45 km
├─ Tempo estimado: 3h 20min
├─ Custo estimado combustível: R$ 18,00
├─ Ganho bruto: R$ 145,00
├─ Lucro líquido estimado: R$ 127,00
└─ 💡 Economia vs. sem otimização: 12 km (R$ 5,00 e 25 min)
```

**Visualização da Rota:**

```
🗺️ Mapa Interativo
├─ 📍 Ponto de partida (casa/atual)
├─ 🔴 Parada 1: Rua ABC (Loggi) - 10h
├─ 🔴 Parada 2: Av XYZ (Loggi) - 10h30
├─ 🔵 Parada 3: Rua DEF (Flex) - 11h15
├─ 🟡 Parada 4: Praça GHI (Lalamove) - 12h
└─ 🟢 Parada 5: Rua JKL (Flex) - 13h30

Linha traçada conectando pontos
Cores diferentes por plataforma
```

**Ações disponíveis:**
- [Iniciar Navegação] → Abre Google Maps/Waze
- [Compartilhar Rota] → Link ou imagem
- [Salvar como Favorita]
- [Ajustar Manualmente] → Arrastar pontos

---

### Durante o Dia - Execução

```
🚚 Modo Entrega Ativa
↓
📋 Lista de Entregas (ordenada)
├─ ✓ Entrega 1 - Concluída (11:05)
│   └─ Rua ABC, 123 - R$ 8,50
├─ 🔄 Entrega 2 - Em andamento
│   ├─ Av XYZ, 456 - R$ 9,00
│   ├─ Distância até aqui: 2,3 km
│   ├─ Tempo estimado chegada: 8 min
│   ├─ [🗺️ Navegar]
│   ├─ [✅ Marcar como Concluída]
│   └─ [⚠️ Reportar Problema]
├─ ⏳ Entrega 3 - Próxima (11:45)
└─ ⏳ Entrega 4 - Próxima (12:30)

Indicadores:
├─ Progresso: 1/5 entregas (20%)
├─ Ganhos até agora: R$ 8,50
└─ Tempo em rota: 35 min
```

**Ações em cada entrega:**

```
Entrega #2 - Av XYZ, 456
├─ 📍 Ver no mapa
├─ 🧭 Navegar (Google Maps/Waze)
├─ 📞 Ligar para cliente
│   └─ (11) 98765-4321
├─ ✅ Marcar como concluída
│   ├─ Confirmar valor: R$ 9,00
│   ├─ [📸 Foto do comprovante] (opcional)
│   └─ Observações: (opcional)
└─ ⚠️ Reportar problema
    ├─ Cliente ausente
    ├─ Endereço incorreto
    ├─ Pedido cancelado
    ├─ Acidente/imprevisto
    └─ Outro
```

**Navegação:**
- Botão [Navegar] abre app de navegação preferido
- Integração com Google Maps/Waze
- Possibilidade de navegar para próxima parada automaticamente

---

### Noite - Fechamento do Dia

```
🏁 Fim do Expediente
[Encerrar Dia]
↓
📊 Resumo do Dia

Entregas:
├─ Total: 12 entregas
├─ Concluídas: 11 ✓
├─ Problemas: 1 ⚠️
└─ Taxa de sucesso: 91%

Operação:
├─ Distância percorrida: 48 km
├─ Tempo trabalhado: 7h 15min
├─ Tempo em trânsito: 4h 20min
└─ Tempo em entregas: 2h 55min

Financeiro:
├─ Ganhos brutos: R$ 145,00
│   ├─ Loggi: R$ 62,00
│   ├─ Flex: R$ 53,00
│   └─ Lalamove: R$ 30,00
├─ Custos: R$ 24,00
│   └─ Combustível: R$ 24,00
├─ Lucro líquido: R$ 121,00
└─ Taxa horária: R$ 16,70/hora

[Adicionar Despesas] [Ver Detalhes] [Compartilhar]
```

**Registrar Despesas:**

```
[+ Adicionar Despesa]
├─ Tipo: [Combustível ▼]
│   ├─ Combustível
│   ├─ Manutenção
│   ├─ Estacionamento
│   ├─ Alimentação
│   ├─ Pedágio
│   └─ Outros
├─ Valor: R$ 24,00
├─ Data: [Hoje ▼]
├─ Observação: "Gasolina Posto BR"
├─ [📸 Foto do cupom] (opcional)
└─ [Salvar]
```

**Histórico de despesas:**
- Lista de todas as despesas do dia
- Possibilidade de editar/excluir
- Categorização automática
- Anexo de comprovantes

---

## 3. Funcionalidades Secundárias

### 3.1 Dashboard Financeiro

```
💰 Finanças
├─ Filtros:
│   ├─ Período: [Esta semana ▼]
│   │   ├─ Hoje
│   │   ├─ Esta semana
│   │   ├─ Este mês
│   │   ├─ Últimos 30 dias
│   │   └─ Personalizado
│   └─ Plataforma: [Todas ▼]
│
├─ 📊 Visão Geral
│   ├─ Ganhos: R$ 890,00
│   ├─ Custos: R$ 165,00
│   ├─ Lucro: R$ 725,00
│   └─ Margem: 81,5%
│
├─ 📈 Por Plataforma
│   ├─ Loggi: R$ 380,00 (42%)
│   ├─ Flex: R$ 310,00 (35%)
│   └─ Lalamove: R$ 200,00 (23%)
│
├─ 📉 Custos Detalhados
│   ├─ Combustível: R$ 120,00 (73%)
│   ├─ Manutenção: R$ 30,00 (18%)
│   └─ Outros: R$ 15,00 (9%)
│
└─ 📅 Evolução no Tempo
    └─ Gráfico de linhas (ganhos, custos, lucro)
```

**Gráficos disponíveis:**
1. Ganhos por plataforma (pizza)
2. Evolução temporal (linha)
3. Custos por categoria (barra)
4. Comparativo semanal (barra agrupada)

**Exportação:**
- [📥 Exportar CSV]
- [📥 Exportar PDF]
- [📤 Compartilhar]

---

### 3.2 Análises e Insights

```
📈 Desempenho

Métricas Operacionais:
├─ Entregas/dia: 14,2 (média)
├─ Taxa de sucesso: 94%
├─ Km/dia: 52 km (média)
├─ Entregas/hora: 1,8
└─ Tempo médio/entrega: 33 min

Métricas Financeiras:
├─ Ganho/hora: R$ 18,50
├─ Ganho/km: R$ 3,20
├─ Ganho/entrega: R$ 12,30
├─ Custo/km: R$ 0,52
└─ Margem de lucro: 79%

Padrões Identificados:
├─ 💡 Dias mais lucrativos: Seg, Qui, Sex
├─ 💡 Horário mais produtivo: 10h-14h
├─ 💡 Região mais rentável: Zona Sul
└─ 💡 Plataforma mais eficiente: Loggi (R$ 19/h)

Recomendações (IA):
├─ 🎯 "Aumente seu ganho/hora em 15% focando em Loggi às segundas"
├─ 🎯 "Evite entregas na Zona Leste após 18h (baixa taxa de sucesso)"
└─ 🎯 "Agrupe entregas no Centro para reduzir custo de combustível"
```

**Comparações:**
- Você vs. Média dos entregadores da sua região
- Semana atual vs. Semana passada
- Mês atual vs. Mês passado

---

### 3.3 Alertas Inteligentes

```
🔔 Notificações

Alertas Ativos:
├─ ⚠️ Conflito de horário detectado!
│   "Entregas Loggi e Flex se sobrepõem às 14h"
│   └─ [Ver Detalhes] [Resolver]
│
├─ 💡 Sugestão de otimização
│   "Você pode economizar 8 km agrupando estas 3 entregas"
│   └─ [Ver Rota] [Aplicar]
│
├─ 📊 Relatório semanal pronto
│   "Seu resumo da semana está disponível"
│   └─ [Ver Relatório]
│
├─ ⏰ Lembrete
│   "Próxima entrega em 15 minutos"
│   └─ [OK]
│
└─ 🎉 Conquista desbloqueada!
    "100 entregas concluídas! Continue assim!"
    └─ [Ver Conquistas]
```

**Tipos de alertas:**
- Conflitos de horário (crítico)
- Sugestões de otimização (info)
- Lembretes de entregas (info)
- Metas atingidas (celebração)
- Problemas detectados (aviso)

**Configurações de notificações:**
- Ativar/desativar por tipo
- Horário de silêncio
- Sons personalizados
- Vibração

---

## 4. Fluxo de Upgrade (Freemium → Premium)

### 4.1 Limites do Plano Gratuito

```
⚠️ Limite Atingido
├─ "Você atingiu o limite de 5 entregas/dia"
├─ "Desbloqueie entregas ilimitadas!"
│
└─ [Ver Planos] → Tela de Upgrade
```

### 4.2 Tela de Planos

```
💎 Escolha seu Plano

┌─────────────────────────┐  ┌─────────────────────────┐
│      GRATUITO           │  │      PREMIUM            │
├─────────────────────────┤  ├─────────────────────────┤
│ R$ 0/mês                │  │ R$ 19,90/mês            │
├─────────────────────────┤  ├─────────────────────────┤
│ ✓ Até 5 entregas/dia    │  │ ✓ Rotas ilimitadas      │
│ ✓ Roteirização básica   │  │ ✓ OCR de telas          │
│ ✓ Dashboard simples     │  │ ✓ Relatórios avançados  │
│ ✗ OCR                   │  │ ✓ Alertas inteligentes  │
│ ✗ Relatórios avançados  │  │ ✓ Exportação CSV/PDF    │
│ ✗ Suporte prioritário   │  │ ✓ Suporte prioritário   │
│                         │  │ ✓ API de integração     │
│                         │  │ ✓ Sem anúncios          │
│                         │  │                         │
│ [Plano Atual]           │  │ [Assinar Agora]         │
└─────────────────────────┘  └─────────────────────────┘

🎁 Primeira semana grátis!

Formas de pagamento:
├─ 💳 Cartão de crédito
├─ 💳 Cartão de débito
└─ 🔲 PIX
```

**Garantias:**
- 7 dias de teste grátis
- Cancele a qualquer momento
- Sem multa de cancelamento

---

## 5. Fluxos de Exceção

### 5.1 Problema Durante Entrega

```
⚠️ Reportar Problema
Entrega: Av XYZ, 456

Selecione o tipo de problema:
├─ 🚫 Cliente ausente
│   └─ [Reagendar] [Devolver] [Deixar com vizinho]
│       ├─ Nova data/hora: [Escolher]
│       ├─ Observações: (opcional)
│       └─ [Confirmar]
│
├─ 📍 Endereço incorreto
│   └─ [Atualizar Endereço] [Contatar Cliente]
│       ├─ Novo endereço: [Digite]
│       ├─ [📞 Ligar para cliente]
│       └─ [Salvar]
│
├─ ❌ Pedido cancelado
│   └─ [Confirmar Cancelamento]
│       ├─ Motivo: [Digite]
│       ├─ Recebeu taxa? [Sim/Não]
│       └─ Valor: R$ [Se sim]
│
├─ 🚗 Acidente/Imprevisto
│   └─ [Registrar Ocorrência]
│       ├─ Descrição: [Digite]
│       ├─ [📸 Foto] (opcional)
│       └─ [Enviar]
│
└─ 🔧 Outro
    └─ Descrição: [Digite]
        └─ [Enviar]
```

**Após reportar:**
- Sistema ajusta automaticamente a rota
- Notifica plataforma de origem (se integrado)
- Salva histórico do problema
- Atualiza estatísticas

---

### 5.2 Conflito de Horários

```
⚠️ Conflito Detectado!

2 entregas agendadas para o mesmo horário:
├─ 🔴 Entrega A - Loggi
│   ├─ Rua ABC, 123
│   ├─ 14:00 - 15:00
│   └─ R$ 8,50
│
└─ 🔵 Entrega B - Flex
    ├─ Av XYZ, 789
    ├─ 14:00 - 16:00
    └─ R$ 12,00

Distância entre pontos: 8 km (~20 min)

Sugestões:
├─ 💡 Reagendar Entrega A para 13:30
│   └─ [Aplicar]
├─ 💡 Reagendar Entrega B para 15:00
│   └─ [Aplicar]
└─ 💡 Rejeitar uma das entregas
    └─ [Escolher qual]

[Resolver Manualmente] [Ignorar Alerta]
```

**Prevenção:**
- Alerta ao adicionar nova entrega com conflito
- Sugestão de horários alternativos
- Visualização no mapa da distância entre pontos

---

### 5.3 Problemas Técnicos

```
❌ Erro ao Calcular Rota

Não foi possível otimizar a rota.

Possíveis causas:
├─ Sem conexão com internet
├─ Serviço temporariamente indisponível
└─ Endereços inválidos detectados

[Tentar Novamente] [Ver Entregas] [Suporte]
```

**Modo Offline:**
- Salva entregas localmente
- Sincroniza quando conectar
- Permite visualizar rotas salvas anteriormente
- Notifica quando voltar online

---

## 6. Perfil e Configurações

```
👤 Meu Perfil

Informações Pessoais:
├─ Nome: João da Silva
├─ Email: joao@email.com
├─ Telefone: (11) 98765-4321
├─ Veículo: Moto
└─ [Editar]

Plataformas Ativas:
├─ ☑ Loggi
├─ ☑ Mercado Livre Flex
├─ ☑ Amazon Flex
└─ [Gerenciar]

Plano Atual:
├─ Premium - R$ 19,90/mês
├─ Próxima cobrança: 15/12/2025
└─ [Gerenciar Assinatura]

Preferências:
├─ Ponto de partida padrão: [Minha casa]
├─ App de navegação: [Google Maps ▼]
├─ Custo do combustível: R$ 5,80/litro
├─ Consumo do veículo: 30 km/litro
└─ [Salvar]

Notificações:
├─ ☑ Alertas de conflito
├─ ☑ Lembretes de entrega
├─ ☑ Relatórios semanais
└─ ☐ Promoções e novidades

Privacidade e Segurança:
├─ [Alterar Senha]
├─ [Excluir Conta]
└─ [Política de Privacidade]

[Sair]
```

---

## 7. Gamificação e Engajamento

### 7.1 Sistema de Conquistas

```
🏆 Conquistas

Desbloqueadas:
├─ ✅ Primeira Entrega
│   "Complete sua primeira entrega"
│   └─ Recompensa: +10 XP
├─ ✅ Maratonista
│   "Complete 100 entregas"
│   └─ Recompensa: Badge especial
└─ ✅ Eficiente
    "Mantenha 95% de taxa de sucesso por 1 mês"
    └─ Recompensa: Desconto 10% próximo mês

Em Progresso:
├─ 🔒 Mestre da Rota (75/200 entregas)
└─ 🔒 Economista (R$ 850/R$ 1000 economizados)

Bloqueadas:
└─ 🔒 Lendário
    "Complete 1000 entregas com 98% de sucesso"
    └─ Recompensa: 1 mês Premium grátis
```

### 7.2 Ranking e Comparações

```
🏅 Ranking Regional

Sua posição: #47 de 523 (São Paulo - SP)

Top 5:
1. Carlos M. - 287 entregas - R$ 4.320
2. Ana P. - 264 entregas - R$ 3.980
3. Roberto S. - 251 entregas - R$ 3.750
4. ...
5. ...

Você vs. Média:
├─ Entregas/semana: 85 (média: 67) ↑
├─ Taxa de sucesso: 94% (média: 91%) ↑
├─ Ganho/hora: R$ 18,50 (média: R$ 17,20) ↑
└─ Eficiência rota: 87% (média: 82%) ↑

[Ver Ranking Completo] [Compartilhar]
```

---

## 8. Wireframe Mental das Telas Principais

```
Arquitetura de Navegação:

Bottom Navigation:
├─ 🏠 Home (Dashboard)
├─ 🗺️ Rotas
├─ 💰 Finanças
├─ 📈 Análises
└─ 👤 Perfil

1. [Home] → Dashboard
   ├─ Resumo do dia
   ├─ Entregas pendentes
   ├─ CTA "Adicionar Entregas"
   └─ Estatísticas rápidas

2. [Rotas] → Gerenciamento de Rotas
   ├─ Mapa com visualização
   ├─ Lista de entregas
   ├─ Botão "Otimizar"
   └─ Status de cada entrega

3. [Finanças] → Dashboard Financeiro
   ├─ Gráficos
   ├─ Resumos por período
   ├─ Registro de despesas
   └─ Exportação de relatórios

4. [Análises] → Insights e Performance
   ├─ Métricas operacionais
   ├─ Padrões identificados
   ├─ Recomendações IA
   └─ Comparações

5. [Perfil] → Configurações
   ├─ Dados pessoais
   ├─ Plataformas
   ├─ Plano/Assinatura
   └─ Preferências
```

---

## 9. Princípios de UX/UI

### 9.1 Design Mobile-First

- Interface otimizada para uso com uma mão
- Botões grandes e acessíveis
- Informações hierarquizadas por importância
- Modo escuro disponível (uso durante condução)

### 9.2 Velocidade e Simplicidade

- Máximo 3 toques para qualquer ação principal
- Carregamento rápido (< 2 segundos)
- Offline-first quando possível
- Feedback visual imediato

### 9.3 Segurança na Condução

- Não exigir interação durante condução
- Notificações sonoras para alertas importantes
- Integração com assistentes de voz (futuro)
- Modo "Em Rota" com tela simplificada

### 9.4 Acessibilidade

- Tamanhos de fonte ajustáveis
- Alto contraste para leitura sob sol
- Suporte a leitores de tela
- Ícones claros e universais

---

## 10. Métricas de Sucesso (KPIs de Produto)

### Adoção e Engajamento

- **DAU/MAU ratio:** > 60% (uso diário)
- **Session length:** 15-20 min/sessão (média)
- **Entregas cadastradas/usuário:** > 10/dia
- **Taxa de retorno D1:** > 70%
- **Taxa de retorno D7:** > 50%

### Conversão

- **Free → Premium:** 8-12%
- **Trial → Pago:** > 60%
- **Churn mensal:** < 5%

### Satisfação

- **NPS (Net Promoter Score):** > 50
- **App Store Rating:** > 4.5 estrelas
- **Taxa de suporte/usuário:** < 5%

### Performance

- **Tempo de carregamento inicial:** < 2s
- **Tempo para otimizar rota:** < 5s
- **Uptime:** > 99.5%
- **Taxa de erro:** < 0.1%

---

## 11. Roadmap de Features (Próximas Versões)

### v1.1 (Q1 2026)

- [ ] Integração com Waze
- [ ] Modo offline completo
- [ ] Widget para iOS/Android
- [ ] Suporte a múltiplos idiomas

### v1.2 (Q2 2026)

- [ ] Assistente de voz
- [ ] IA de recomendação de rotas lucrativas
- [ ] Integração com bancos (Open Banking)
- [ ] Relatórios fiscais automatizados

### v2.0 (Q3 2026)

- [ ] API pública para integrações
- [ ] Modo colaborativo (equipes)
- [ ] Marketplace de serviços
- [ ] Programa de fidelidade

---

## Conclusão

Este fluxo foi desenhado para maximizar a eficiência operacional do entregador autônomo, reduzindo atrito e aumentando a rentabilidade através de:

1. **Simplificação:** Menos toques, mais resultados
2. **Inteligência:** IA sugerindo melhorias constantemente
3. **Centralização:** Tudo em um único app
4. **Transparência:** Visibilidade total de ganhos e custos
5. **Engajamento:** Gamificação e conquistas

O diferencial está em **resolver a dor real** do entregador multi-app: **caos operacional e imprevisibilidade financeira**.
