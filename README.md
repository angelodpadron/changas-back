# Changas Backend

![CI Status](https://github.com/angelodpadron/changas-back/actions/workflows/ci.yml/badge.svg)
![Coverage Status](https://coveralls.io/repos/github/angelodpadron/changas-back/badge.svg)

## Overview
Este repo contiene el backend de Changas.

## Integrantes
- Leandro Arroyo
- Angelo Padron

## Tecnologías
- Spring Boot
- Swagger
- PostGIS

## Enlaces
- [Repositorio central](http://github.com/angelodpadron/changas-doc)

## Requisitos

### Sin Docker
- JDK 21
- PostgreSQL
- PosGIS

### Con Docker-Compose
- Docker
- Docker-Compose

## Configurar y ejecutar proyecto

### Sin Docker
- Configurar una base de datos en PostgreSQL con el nombre `changas`, así como también un usuario y contraseña para el mismo con el nombre `dev`.
- Ejecutar `./gradlew bootRun --args='--spring.profiles.active=dev'` en el directorio raíz.

### Con Docker-Compose
- Asegúrate de tener Docker y Docker-Compose instalados.
- En el directorio raíz del proyecto, ejecuta `docker-compose up`.
- Esto levantará el servicio de PostgreSQL con PosGIS ya configurado, y la aplicación Spring Boot.
- El servicio estará disponible en `http://localhost:8080`.
