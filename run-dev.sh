#!/bin/bash

# FITNESS APP - Script de Desarrollo


# Colores para mensajes
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

echo "          🏋️ FITNESS APP - Modo Desarrollo"

# Verificar que existe .env
if [ ! -f .env ]; then
    echo -e "${RED}❌ Error: Archivo .env no encontrado${NC}"
    exit 1
fi

# Cargar variables de entorno
set -a
source .env
set +a

echo -e "${GREEN}✅ Variables de entorno cargadas${NC}"
echo ""
echo "📦 Configuración:"
echo "   DB_HOST:     $DB_HOST"
echo "   DB_PORT:     $DB_PORT"
echo "   DB_NAME:     $DB_NAME"
echo "   DB_USERNAME: $DB_USERNAME"
echo "   JWT_EXP:     $JWT_EXPIRATION ms"
echo ""
echo "════════════════════════════════════════════════════════════════"
echo ""
echo -e "${GREEN}🚀 Iniciando Spring Boot con perfil DEV...${NC}"
echo ""

# Ejecutar con perfil dev
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
