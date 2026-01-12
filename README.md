Fitness App Backend 🏋️‍♂️

Backend API de una aplicación de fitness diseñada con **enfoque API-first**, arquitectura limpia y pensada desde el inicio para ser consumida por **clientes web y móviles**.

El objetivo del proyecto es construir un backend escalable y mantenible, priorizando el diseño del dominio y la correcta separación de responsabilidades.



🎯 Propósito del proyecto

Este proyecto busca resolver de forma ordenada y profesional:

- Gestión de usuarios y autenticación
- Modelado de planes de entrenamiento y rutinas
- Exposición de una API REST clara y versionada
- Escalabilidad futura sin reescribir lógica de negocio

No es un CRUD de tutorial, sino un backend construido paso a paso, entendiendo cada decisión técnica.


 🧠 Enfoque de diseño

El desarrollo se realiza siguiendo estos principios:

- API-first: el backend no depende del frontend
- Separación estricta de capas
- DTOs explícitos para no exponer entidades
- Lógica de negocio centralizada en servicios
- Preparado para crecimiento progresivo (web + mobile)



 🧱 Arquitectura

Estructura base del proyecto:

src/main/java/
com.yourcompany.fitnessapp
├─ controller # Adaptadores HTTP (delgados)
├─ service # Lógica de negocio
├─ dto # Contratos de entrada/salida
├─ entity # Modelo de dominio
├─ repository # Acceso a datos (JPA)
├─ mapper # Conversión Entity ↔ DTO
├─ security # Autenticación y autorización
├─ exception # Manejo centralizado de errores
└─ config # Configuración general
