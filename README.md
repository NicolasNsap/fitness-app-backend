# 🏋️ Fitness App Backend

API REST para seguimiento de entrenamientos, desplegada en producción en AWS.

## 🌐 Demo en Vivo

**🔗 API:** http://56.126.6.86:8080

**📖 Swagger UI:** http://56.126.6.86:8080/swagger-ui.html

---

## 🚀 Tecnologías

| Categoría | Tecnología |
|-----------|------------|
| Lenguaje | Java 21 |
| Framework | Spring Boot 3.5.9 |
| Seguridad | Spring Security + JWT |
| Base de datos | MySQL 8.0 |
| ORM | Spring Data JPA / Hibernate |
| Documentación | Swagger / OpenAPI 3.0 |
| Deploy | AWS EC2 + RDS |
| Otros | Lombok, BCrypt, Maven |

---

## 📋 Endpoints Disponibles

### 🔐 Autenticación (`/api/auth`)

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| POST | `/register` | Registrar usuario | No |
| POST | `/login` | Iniciar sesión (retorna JWT) | No |

### 🏋️ Workouts (`/api/workouts`)

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| POST | `/` | Crear nuevo workout | JWT |
| GET | `/` | Listar mis workouts | JWT |
| GET | `/{id}` | Obtener workout con ejercicios | JWT |
| PATCH | `/{id}` | Actualizar workout | JWT |
| DELETE | `/{id}` | Eliminar workout | JWT |
| PATCH | `/{id}/complete` | Marcar como completado | JWT |
| POST | `/{id}/exercises` | Agregar ejercicio al workout | JWT |

### 💪 Ejercicios (`/api/exercises`)

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| GET | `/` | Listar ejercicios disponibles | JWT |
| GET | `/{id}` | Obtener ejercicio por ID | JWT |

### 👤 Usuarios (`/api/users`)

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| GET | `/` | Listar usuarios | JWT |
| GET | `/{id}` | Obtener usuario por ID | JWT |

---

## 🗂️ Arquitectura del Proyecto

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
├── mapper/              # Conversión Entity ↔ DTO
├── repository/          # Repositorios Spring Data
├── security/            # JWT Filter y Service
└── service/             # Lógica de negocio
```

**Patrones implementados:**
- Arquitectura en capas (Controller → Service → Repository)
- DTO Pattern (separación de entidades y transferencia)
- Repository Pattern
- Global Exception Handling

---

## ⚙️ Configuración Local

### Requisitos

- Java 21+
- MySQL 8.0+
- Maven 3.8+

### 1. Clonar el repositorio

```bash
git clone https://github.com/NicolasNsap/fitness-app-backend.git
cd fitness-app-backend
```

### 2. Crear base de datos

```sql
CREATE DATABASE fitness_db;
```

### 3. Configurar variables de entorno

Crear archivo `.env` en la raíz del proyecto:

```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=fitness_db
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_password
JWT_SECRET_KEY=tu_clave_secreta_de_64_caracteres
JWT_EXPIRATION=86400000
```

### 4. Ejecutar en modo desarrollo

```bash
./run-dev.sh
```

O manualmente:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 5. Acceder a Swagger UI

```
http://localhost:8080/swagger-ui.html
```

---

## 🚀 Deploy en Producción

La aplicación está desplegada en AWS con la siguiente arquitectura:

```
┌─────────────────────────────────────────────────────────────┐
│                         AWS Cloud                           │
│  ┌─────────────────┐         ┌─────────────────────────┐    │
│  │      EC2        │         │          RDS            │    │
│  │  Ubuntu 22.04   │────────▶│      MySQL 8.4          │    │
│  │  Spring Boot    │         │   (Base de datos)       │    │
│  │  systemd        │         │                         │    │
│  └─────────────────┘         └─────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

**Características del deploy:**
- ✅ Servicio systemd con auto-restart
- ✅ Base de datos separada en RDS
- ✅ Perfiles Spring Boot (dev/prod)
- ✅ Variables de entorno seguras
- ✅ Security Groups configurados

---

## 📊 Estado del Proyecto

### ✅ Completado

- [x] Autenticación JWT (registro, login, tokens)
- [x] CRUD completo de Workouts
- [x] Gestión de ejercicios por workout
- [x] Sets con peso, repeticiones y notas
- [x] Arquitectura en capas
- [x] Exception Handling global
- [x] Validaciones con Bean Validation
- [x] Documentación Swagger UI
- [x] Deploy en AWS (EC2 + RDS)
- [x] Servicio systemd

### 🔄 En desarrollo

- [ ] Docker y Docker Compose
- [ ] CI/CD con GitHub Actions
- [ ] Tests unitarios (JUnit)

### ⏳ Próximamente

- [ ] Rutinas y programas de entrenamiento
- [ ] Estadísticas y progreso
- [ ] Frontend React Native

---

## 🔐 Autenticación

La API usa JWT (JSON Web Tokens). Para acceder a endpoints protegidos:

1. **Registrar usuario:**
```bash
curl -X POST http://56.126.6.86:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"tu_usuario","email":"tu@email.com","password":"tu_password"}'
```

2. **Login (obtener token):**
```bash
curl -X POST http://56.126.6.86:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"tu_usuario","password":"tu_password"}'
```

3. **Usar token en peticiones:**
```bash
curl -X GET http://56.126.6.86:8080/api/workouts \
  -H "Authorization: Bearer TU_TOKEN_JWT"
```

---

## 👨‍💻 Autor

**Nicolás Abarca**

- LinkedIn: [linkedin.com/in/nicolás-abarca](https://www.linkedin.com/in/nicolás-abarca)
- GitHub: [github.com/NicolasNsap](https://github.com/NicolasNsap)

---

## 📄 Licencia

Este proyecto es de código abierto bajo licencia MIT.
