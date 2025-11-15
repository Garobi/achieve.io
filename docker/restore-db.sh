#!/bin/bash

set -e

if [ -z "$1" ]; then
    echo "Uso: ./docker/restore-db.sh <arquivo_backup.sql>"
    echo ""
    echo "Backups disponíveis:"
    ls -lh ./backups/*.sql 2>/dev/null || echo "Nenhum backup encontrado"
    exit 1
fi

BACKUP_FILE="$1"

if [ ! -f "$BACKUP_FILE" ]; then
    echo "Arquivo não encontrado: $BACKUP_FILE"
    exit 1
fi

echo "ATENÇÃO: Todos os dados atuais serão substituídos!"
echo "Arquivo: $BACKUP_FILE"
read -p "Digite 'sim' para confirmar: " confirmacao

if [ "$confirmacao" != "sim" ]; then
    echo "Operação cancelada"
    exit 0
fi

echo "Restaurando backup..."

docker-compose exec -T postgres psql -U postgres -c "DROP DATABASE IF EXISTS achieveio;"
docker-compose exec -T postgres psql -U postgres -c "CREATE DATABASE achieveio;"
cat "$BACKUP_FILE" | docker-compose exec -T postgres psql -U postgres achieveio

echo "Backup restaurado com sucesso"
