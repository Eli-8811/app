# 🧩 app - Backend Java con Spring Boot 3.1.0

Este proyecto es una aplicación backend construida con **Spring Boot 3.1.0**, que sigue una arquitectura modularizada por capas (controller, service, repository, model, config, etc.).

## 🚀 Tecnologías utilizadas

- Java 17
- Spring Boot 3.1.0
- Spring Data JPA
- Spring Validation
- Spring Security (opcional)
- Hibernate Validator
- OpenAPI 3 (SpringDoc)
- MySQL Connector
- Lombok
- Logback
- RestTemplate
- JaCoCo (cobertura de pruebas)

## 📦 Módulos incluidos

Este proyecto está estructurado como un proyecto padre tipo `pom` con submódulos Maven:

- `app-repository`
- `app-model`
- `app-common`
- `app-service`
- `app-controller`
- `app-ear`
- `app-coverage`

## 🔧 Requisitos previos

- Java 17
- Maven 3.8.x o superior
- MySQL (o cambiar configuración en `application.yml`)
- Git

## 🛠️ Cómo clonar y correr

```bash
git clone https://github.com/Eli-8811/app.git
cd app
mvn clean install