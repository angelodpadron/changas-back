# Changas Backend

![CI Status](https://github.com/angelodpadron/changas-back/actions/workflows/ci.yml/badge.svg)
![Coverage Status](https://coveralls.io/repos/github/angelodpadron/changas-back/badge.svg)

## Overview
Este repo contiene el backend de Changas. 

## Integrantes
- Leandro Arroyo
- Angelo Padron

## Tecnologias
- Spring Boot
- Swagger

## Enlaces
- [Repositorio central](http://github.com/angelodpadron/changas-doc)

## Requisitos
- JDK 21
- PostgreSQL

## Configurar y ejecutar proyecto
- Configurar una base de datos en PostgreSQL con el nombre `changas`, asi como tambien un usuario y contraseña para el mismo con el nombre `dev`.
- Ejecutar `./gradlew bootRun --args='--spring.profiles.active=dev` en el directorio raiz.