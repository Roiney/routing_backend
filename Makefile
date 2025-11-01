.PHONY: help up down restart logs clean build test run dev tools

# Default target
help:
	@echo "====================================="
	@echo "  Routing Backend - Makefile Helper"
	@echo "====================================="
	@echo ""
	@echo "Docker Commands:"
	@echo "  make up          - Start all services (PostgreSQL, Redis, Kafka)"
	@echo "  make down        - Stop all services"
	@echo "  make restart     - Restart all services"
	@echo "  make logs        - Show logs from all services"
	@echo "  make clean       - Stop and remove all containers, volumes"
	@echo "  make tools       - Start services with management tools"
	@echo ""
	@echo "Application Commands:"
	@echo "  make build       - Build the application with Maven"
	@echo "  make test        - Run tests"
	@echo "  make run         - Run the Spring Boot application"
	@echo "  make dev         - Start services and run application in dev mode"
	@echo ""
	@echo "Database Commands:"
	@echo "  make db-shell    - Connect to PostgreSQL shell"
	@echo "  make db-reset    - Reset database (drop and recreate)"
	@echo ""

# Docker Commands
up:
	docker-compose up -d
	@echo "✓ Services started successfully!"
	@echo "PostgreSQL: localhost:5432"
	@echo "Redis: localhost:6379"
	@echo "Kafka: localhost:9092"

down:
	docker-compose down
	@echo "✓ Services stopped"

restart:
	docker-compose restart
	@echo "✓ Services restarted"

logs:
	docker-compose logs -f

clean:
	docker-compose down -v
	@echo "✓ Services stopped and volumes removed"

tools:
	docker-compose --profile tools up -d
	@echo "✓ Services started with management tools!"
	@echo "PostgreSQL: localhost:5432"
	@echo "Redis: localhost:6379"
	@echo "Kafka: localhost:9092"
	@echo "PgAdmin: http://localhost:5050"
	@echo "Redis Commander: http://localhost:8081"
	@echo "Kafka UI: http://localhost:8082"

# Application Commands
build:
	./mvnw clean package -DskipTests
	@echo "✓ Build completed"

test:
	./mvnw test
	@echo "✓ Tests completed"

run:
	./mvnw spring-boot:run

dev: up
	@echo "⏳ Waiting for services to be ready..."
	@sleep 10
	./mvnw spring-boot:run

# Database Commands
db-shell:
	docker-compose exec postgres psql -U postgres -d routing_dev

db-reset:
	docker-compose exec postgres psql -U postgres -c "DROP DATABASE IF EXISTS routing_dev;"
	docker-compose exec postgres psql -U postgres -c "CREATE DATABASE routing_dev;"
	@echo "✓ Database reset completed"

# Installation
install:
	@echo "Installing dependencies..."
	./mvnw dependency:resolve
	@echo "✓ Dependencies installed"
