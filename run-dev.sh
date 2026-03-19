#!/bin/bash
# ═══════════════════════════════════════════════════════════════════════════
# FITNESS APP - Script de Desarrollo
# ═══════════════════════════════════════════════════════════════════════════

# Colores para mensajes
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # Sin color

echo ""
echo "════════════════════════════════════════════════════════════════"
echo "          🏋️ FITNESS APP - Modo Desarrollo"
echo "════════════════════════════════════════════════════════════════"
echo ""

# Verificar que existe .env
if [ ! -f .env ]; then
    echo -e "${RED}❌ Error: Archivo .env no encontrado${NC}"
    echo ""
    echo "Crea el archivo .env copiando el ejemplo:"
    echo "  cp .env.example .env"
    echo ""
    echo "Luego edita .env con tus valores reales."
    exit 1
fi

# Cargar variables desde .env
export $(grep -v '^#' .env | grep -v '^\s*$' | xargs)

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

# Ejecutar la aplicación
echo -e "${GREEN}🚀 Iniciando Spring Boot...${NC}"
echo ""
./mvnw spring-boot:run
