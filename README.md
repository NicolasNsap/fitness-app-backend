![Deploy to AWS](https://github.com/NicolasNsap/fitness-app-backend/actions/workflows/deploy.yml/badge.svg)

# Fitness App Backend

API REST para seguimiento de entrenamientos. Desplegada en AWS con deploy automático.

## Demo

- **API:** http://56.126.6.86:8080
- **Swagger UI:** http://56.126.6.86:8080/swagger-ui.html

---

## Stack

| Categoría | Tecnología |
|-----------|------------|
| Lenguaje | Java 21 |
| Framework | Spring Boot 3.5.9 |
| Seguridad | Spring Security + JWT |
| Base de datos | MySQL 8.0 |
| ORM | Spring Data JPA / Hibernate |
| Documentación | Swagger / OpenAPI 3.0 |
| Contenedores | Docker + Docker Compose |
| CI/CD | GitHub Actions |
| Infraestructura | AWS EC2 + RDS |

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

```
src/main/java/com/fitnessapp/
├── config/              # Configuraciones (Security, Swagger)
├── controller/          # Endpoints REST
├── dto/
│   ├── request/         # DTOs de entrada
│   └── response/        # DTOs de salida
├── entity/              # Entidades JPA
├── exception/           # Excepciones personalizadas
│   └── handler/         # GlobalExceptionHandler
├── mapper/              # Conversión Entity - DTO
├── repository/          # Repositorios Spring Data
├── security/            # JWT Filter y Service
└── service/             # Lógica de negocio
```

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

```
http://localhost:8080/swagger-ui.html
```

---

## Docker (desarrollo local)

```bash
docker compose up -d
```

Esto levanta la app y MySQL en contenedores.

---

## Deploy

El deploy es automático. Al hacer push a `main`, GitHub Actions:

1. Compila el proyecto
2. Construye la imagen Docker
3. La sube a Docker Hub
4. Se conecta a EC2 por SSH
5. Hace pull de la imagen y reinicia el contenedor

### Arquitectura en AWS

```
┌──────────────────────────────────────────────────┐
│                    AWS                           │
│  ┌─────────────────┐     ┌─────────────────┐     │
│  │      EC2        │     │      RDS        │     │
│  │  Docker         │────▶│   MySQL 8.4     │     │
│  │  Docker Compose │     │                 │     │
│  └─────────────────┘     └─────────────────┘     │
└──────────────────────────────────────────────────┘
```

---

## Estado del proyecto

**Completado:**
- Autenticación JWT
- CRUD de Workouts
- Ejercicios y Sets
- Validaciones
- Swagger UI
- Deploy en AWS
- Docker + Docker Compose
- CI/CD con GitHub Actions

**Pendiente:**
- Tests unitarios
- Rutinas y programas
- Estadísticas de progreso
- Frontend React Native

---

## Uso de la API

Registrar:
```bash
curl -X POST http://56.126.6.86:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"usuario","email":"email@test.com","password":"password"}'
```

Login:
```bash
curl -X POST http://56.126.6.86:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"usuario","password":"password"}'
```

Usar el token:
```bash
curl -X GET http://56.126.6.86:8080/api/workouts \
  -H "Authorization: Bearer TU_TOKEN"
```

---

## Autor

**Nicolás Abarca**

- [LinkedIn](https://www.linkedin.com/in/nicolás-abarca)
- [GitHub](https://github.com/NicolasNsap)