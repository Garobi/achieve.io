#!/bin/bash

set -e

BACKUP_DIR="./backups"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
BACKUP_FILE="${BACKUP_DIR}/achieveio_backup_${TIMESTAMP}.sql"

echo "Criando backup..."
mkdir -p "$BACKUP_DIR"

docker-compose exec -T postgres pg_dump -U postgres achieveio > "$BACKUP_FILE"

echo "Backup criado: $BACKUP_FILE"
