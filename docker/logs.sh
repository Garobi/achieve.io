#!/bin/bash

set -e

echo "Visualizando logs (Ctrl+C para sair)..."
echo ""
docker-compose logs -f --tail=100
