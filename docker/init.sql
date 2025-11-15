-- Script de inicialização do banco de dados
-- Executado automaticamente na primeira vez que o container é criado

-- Criar extensão UUID (já será criada pelo Flyway, mas garantindo aqui também)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Log de inicialização
DO $$
BEGIN
    RAISE NOTICE 'Banco de dados Achieve.io inicializado com sucesso!';
    RAISE NOTICE 'Aguardando migrações do Flyway...';
END $$;
