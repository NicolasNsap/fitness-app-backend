# 🏋️ Fitness App Backend 🏋️‍♂️

API REST para aplicación de seguimiento de entrenamientos en gimnasio.

## 🚀 Tecnologías

- **Java 21** (OpenJDK)
- **Spring Boot 3.2.1**
- **MySQL 8.0**
- **Spring Data JPA** (Persistencia)
- **Spring Security** (Autenticación - en desarrollo)
- **BCrypt** (Cifrado de contraseñas)
- **Lombok** (Reducción de boilerplate)

## 📋 Endpoints Disponibles

### 👤 Usuarios (`/api/users`)

| Método | Endpoint | Descripción             | Status |
|--------|----------|-------------------------|--------|
| POST | `/register` | Registrar nuevo usuario | 201 Created |
| GET | `/` | Listar usuarios activos | 200 OK |
| GET | `/{id}` | Obtener usuario por ID  | 200 OK |

### 💪 Ejercicios (`/api/exercises`)

| Método | Endpoint | Descripción               | Status |
|--------|----------|---------------------------|--------|
| POST | `/register` | Registrar nuevo ejercicio | 201 Created |
| GET | `/` | Listar ejercicios activos | 200 OK |
| GET | `/{id}` | Obtener ejercicio por ID  | 200 OK |

## 🗂️ Estructura del Proyecto
```
src/main/java/com/gymtracker/
├── entity/          # Entidades JPA (User, Role, Exercise)
├── repository/      # Repositorios Spring Data
├── dto/             # DTOs Request/Response
├── mapper/          # Conversión Entity ↔ DTO
├── service/         # Lógica de negocio
├── controller/      # Endpoints REST
├── exception/       # Excepciones personalizadas
│   └── handler/     # GlobalExceptionHandler
└── config/          # Configuraciones (Security, etc.)
```

## ⚙️ Configuración

### Requisitos previos:
- Java 21+
- MySQL 8.0+
- Maven 3.8+

### Base de datos:
```sql
CREATE DATABASE fitness_db;
```

### Configurar `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/fitness_db
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

### Ejecutar:
```bash
mvn spring-boot:run
```

## 📝 Estado del Proyecto

**Completado:**
- ✅ Entidades y relaciones JPA
- ✅ Repositories para consultas básicas
- ✅ DTOs Request/Response
- ✅ Exception Handling global
- ✅ Validaciones con @Valid
- ✅ Cifrado de contraseñas (BCrypt)
- ✅ Endpoints básicos de usuarios y ejercicios

**En desarrollo:**
- 🔄 Autenticación JWT
- 🔄 Endpoints de rutinas y planes
- 🔄 Testing unitario

**Próximamente:**
- ⏳ Documentación con Swagger
- ⏳ Integración con frontend

## 👨‍💻 Autor

Nicolas Abarca

## 📄 Licencia

Este proyecto es de código abierto.