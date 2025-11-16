#!/bin/bash

set -e

echo "Iniciando ambiente de desenvolvimento..."

if ! docker info > /dev/null 2>&1; then
    echo "Erro: Docker não está rodando"
    exit 1
fi

docker-compose up -d

echo "Aguardando PostgreSQL..."
sleep 5

if docker-compose ps | grep -q "Up"; then
    echo ""
    echo "Ambiente iniciado com sucesso!"
    echo ""
    echo "Serviços:"
    echo "  PostgreSQL: localhost:5432 (achieveio/postgres/postgres)"
    echo "  pgAdmin:    http://localhost:5050 (admin@achieveio.com/admin)"
    echo ""
else
    echo "Erro ao iniciar containers"
    docker-compose logs
    exit 1
fi
