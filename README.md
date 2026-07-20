# Fitness App Backend

API REST para seguimiento de entrenamientos. Desplegada en Railway.

## Demo

- **API:** https://fitness-app-backend-production-f14c.up.railway.app
- **Swagger UI:** https://fitness-app-backend-production-f14c.up.railway.app/swagger-ui.html

---

## Stack

| Categoría | Tecnología |
|-----------|------------|
| Lenguaje | Java 21 |
| Framework | Spring Boot 3.5.9 |
| Seguridad | Spring Security + JWT |
| Base de datos | MySQL 9.4 |
| ORM | Spring Data JPA / Hibernate |
| Documentación | Swagger / OpenAPI 3.0 |
| Contenedores | Docker |
| Infraestructura | Railway |

---

## Endpoints

### Autenticación (`/api/auth`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/register` | Registrar usuario |
| POST | `/login` | Login (retorna JWT) |

### Workouts (`/api/workouts`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/` | Crear workout |
| GET | `/` | Listar mis workouts |
| GET | `/{id}` | Obtener workout con ejercicios |
| PATCH | `/{id}` | Actualizar workout |
| DELETE | `/{id}` | Eliminar workout |
| PATCH | `/{id}/complete` | Marcar como completado |
| POST | `/{id}/exercises` | Agregar ejercicio |

### Ejercicios (`/api/exercises`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/` | Listar ejercicios disponibles |
| GET | `/{id}` | Obtener ejercicio por ID |

### Usuarios (`/api/users`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/` | Listar usuarios |
| GET | `/{id}` | Obtener usuario por ID |

Todos los endpoints excepto auth requieren JWT.

---

## Estructura del proyecto

src/main/java/com/fitnessapp/
├── config/ # Configuraciones (Security, Swagger)
├── controller/ # Endpoints REST
├── dto/
│ ├── request/ # DTOs de entrada
│ └── response/ # DTOs de salida
├── entity/ # Entidades JPA
├── exception/ # Excepciones personalizadas
│ └── handler/ # GlobalExceptionHandler
├── mapper/ # Conversión Entity - DTO
├── repository/ # Repositorios Spring Data
├── security/ # JWT Filter y Service
└── service/ # Lógica de negocio


---

## Configuración local

### Requisitos

- Java 21+
- MySQL 8.0+
- Maven 3.8+

### 1. Clonar

```bash
git clone https://github.com/NicolasNsap/fitness-app-backend.git
cd fitness-app-backend
```

### 2. Crear base de datos

```sql
CREATE DATABASE fitness_db;
```

### 3. Variables de entorno

Crear archivo `.env` en la raíz:

```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=fitness_db
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_password
JWT_SECRET_KEY=clave_secreta_64_caracteres
JWT_EXPIRATION=86400000
```

### 4. Ejecutar

```bash
./run-dev.sh
```

O con Maven directo:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 5. Swagger

http://localhost:8080/swagger-ui.html


---

## Docker (desarrollo local)

```bash
docker compose up -d
```

Esto levanta la app y MySQL en contenedores.

---

## Deploy

El deploy es automático vía Railway. Al hacer push a `main`, Railway:

1. Detecta cambios en el repositorio
2. Construye la imagen desde el Dockerfile
3. Despliega automáticamente

### Arquitectura en Railway

┌──────────────────────────────────────────────────┐
│ Railway │
│ ┌─────────────────┐ ┌─────────────────┐ │
│ │ Backend │ │ MySQL │ │
│ │ (Docker) │────▶│ 9.4 │ │
│ │ │ │ │ │
│ └─────────────────┘ └─────────────────┘ │
└──────────────────────────────────────────────────┘


---

## Estado del proyecto

**Completado:**
- Autenticación JWT
- CRUD de Workouts
- Ejercicios y Sets
- Rutinas y programas
- Validaciones
- Swagger UI
- Deploy en Railway
- Docker
- Frontend React Native (básico)

**Pendiente:**
- Tests unitarios
- Estadísticas de progreso

---

## Uso de la API

Registrar:
```bash
curl -X POST https://fitness-app-backend-production-f14c.up.railway.app/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"usuario","email":"email@test.com","password":"password"}'
```

Login:
```bash
curl -X POST https://fitness-app-backend-production-f14c.up.railway.app/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail":"usuario","password":"password"}'
```

Usar el token:
```bash
curl -X GET https://fitness-app-backend-production-f14c.up.railway.app/api/workouts \
  -H "Authorization: Bearer TU_TOKEN"
```

---

## Autor

**Nicolás Abarca**

- [LinkedIn](https://www.linkedin.com/in/nicolás-abarca)
- [GitHub](https://github.com/NicolasNsap)
  EOF