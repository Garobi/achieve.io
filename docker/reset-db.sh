#!/bin/bash

set -e

echo "ATENÇÃO: Este script vai DELETAR TODOS OS DADOS!"
read -p "Digite 'sim' para confirmar: " confirmacao

if [ "$confirmacao" != "sim" ]; then
    echo "Operação cancelada"
    exit 0
fi

echo "Parando containers..."
docker-compose down

echo "Removendo volumes..."
docker volume rm achieveio_postgres_data 2>/dev/null || true
docker volume rm achieveio_pgadmin_data 2>/dev/null || true

echo "Recriando ambiente..."
docker-compose up -d

echo "Banco resetado com sucesso"
