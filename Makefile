.PHONY: help dev-up dev-down dev-restart logs db-reset db-backup db-restore db-shell clean

DOCKER_COMPOSE = sudo docker compose
JAVA_PATHS = /opt/homebrew/opt/openjdk@21:/usr/local/opt/openjdk@21

define find_java
	@for path in $(subst :, ,$(JAVA_PATHS)); do \
		if [ -d "$$path" ]; then \
			export JAVA_HOME="$$path"; \
			export PATH="$$JAVA_HOME/bin:$$PATH"; \
			break; \
		fi; \
	done
endef

define run_mvn
	$(find_java) && ./mvnw $(1)
endef

help:
	@echo "Achieve.io - Comandos disponíveis:"
	@echo ""
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "  %-15s %s\n", $$1, $$2}'
	@echo ""

dev-up: ## Inicia o ambiente de desenvolvimento
	@echo "Iniciando ambiente..."
	@$(DOCKER_COMPOSE) up -d
	@echo "Ambiente iniciado!"
	@echo ""
	@echo "PostgreSQL: localhost:5432 (achieveio/postgres/postgres)"
	@echo "pgAdmin:    http://localhost:5050 (admin@achieveio.com/admin)"
	@echo ""

dev-down: ## Para o ambiente
	@$(DOCKER_COMPOSE) stop

dev-restart: ## Reinicia o ambiente
	@$(DOCKER_COMPOSE) restart

logs: ## Mostra os logs (Ctrl+C para sair)
	@$(DOCKER_COMPOSE) logs -f --tail=100

logs-db: ## Mostra logs do PostgreSQL
	@$(DOCKER_COMPOSE) logs -f --tail=100 postgres

status: ## Status dos containers
	@$(DOCKER_COMPOSE) ps

db-reset: ## Reseta o banco (CUIDADO: deleta dados!)
	@echo "ATENÇÃO: Vai DELETAR TODOS OS DADOS!"
	@read -p "Digite 'sim' para confirmar: " confirmacao; \
	if [ "$$confirmacao" = "sim" ]; then \
		$(DOCKER_COMPOSE) down; \
		docker volume rm achieveio_postgres_data 2>/dev/null || true; \
		docker volume rm achieveio_pgadmin_data 2>/dev/null || true; \
		$(DOCKER_COMPOSE) up -d; \
		echo "Banco resetado"; \
	else \
		echo "Cancelado"; \
	fi

db-backup: ## Cria backup do banco
	@mkdir -p backups
	@TIMESTAMP=$$(date +"%Y%m%d_%H%M%S"); \
	BACKUP_FILE="backups/achieveio_backup_$$TIMESTAMP.sql"; \
	$(DOCKER_COMPOSE) exec -T postgres pg_dump -U postgres achieveio > $$BACKUP_FILE && \
	echo "Backup criado: $$BACKUP_FILE"

db-restore: ## Restaura backup (uso: make db-restore FILE=arquivo.sql)
	@if [ -z "$(FILE)" ]; then \
		echo "Uso: make db-restore FILE=backups/arquivo.sql"; \
		ls -lh backups/*.sql 2>/dev/null || echo "Nenhum backup encontrado"; \
		exit 1; \
	fi
	@if [ ! -f "$(FILE)" ]; then \
		echo "Arquivo não encontrado: $(FILE)"; \
		exit 1; \
	fi
	@echo "ATENÇÃO: Dados atuais serão substituídos!"
	@read -p "Digite 'sim' para confirmar: " confirmacao; \
	if [ "$$confirmacao" = "sim" ]; then \
		$(DOCKER_COMPOSE) exec -T postgres psql -U postgres -c "DROP DATABASE IF EXISTS achieveio;"; \
		$(DOCKER_COMPOSE) exec -T postgres psql -U postgres -c "CREATE DATABASE achieveio;"; \
		cat $(FILE) | $(DOCKER_COMPOSE) exec -T postgres psql -U postgres achieveio; \
		echo "Backup restaurado"; \
	else \
		echo "Cancelado"; \
	fi

db-shell: ## Abre shell do PostgreSQL
	@$(DOCKER_COMPOSE) exec postgres psql -U postgres -d achieveio

db-migrate: ## Executa migrations do Flyway
	@$(call run_mvn,flyway:migrate)

clean: ## Remove containers, volumes e networks
	@echo "ATENÇÃO: Vai remover TUDO!"
	@read -p "Digite 'sim' para confirmar: " confirmacao; \
	if [ "$$confirmacao" = "sim" ]; then \
		$(DOCKER_COMPOSE) down -v; \
		echo "Ambiente limpo"; \
	else \
		echo "Cancelado"; \
	fi

prepare-mvn: ## Prepara o Maven Wrapper
	@chmod +x mvnw 2>/dev/null || true

run: prepare-mvn ## Executa a aplicação Spring Boot
	@echo "Iniciando aplicação..."
	@$(call run_mvn,spring-boot:run)

build: prepare-mvn ## Compila o projeto
	@echo "Compilando..."
	@$(call run_mvn,clean install -DskipTests)

test: prepare-mvn ## Executa os testes
	@echo "Executando testes..."
	@$(call run_mvn,test)

package: prepare-mvn ## Gera o JAR
	@echo "Gerando JAR..."
	@$(call run_mvn,clean package)

setup: dev-up ## Configuração inicial
	@echo "Setup completo!"

.DEFAULT_GOAL := help
