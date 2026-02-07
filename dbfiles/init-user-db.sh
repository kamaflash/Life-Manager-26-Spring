#!/bin/bash
set -e

echo "🚀 Iniciando script de inicialización de PostgreSQL..."
echo "👤 Usuario: $POSTGRES_USER"

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
DO \$\$
BEGIN
    IF NOT EXISTS (
        SELECT FROM pg_database WHERE datname = 'career'
    ) THEN
        RAISE NOTICE '🆕 Creando base de datos career...';
        CREATE DATABASE career;
        GRANT ALL PRIVILEGES ON DATABASE career TO $POSTGRES_USER;
        RAISE NOTICE '✅ Base de datos career creada correctamente';
    ELSE
        RAISE NOTICE 'ℹ️ La base de datos career ya existe, no se crea de nuevo';
    END IF;
END
\$\$;
EOSQL

echo "🎉 Script de inicialización finalizado"
