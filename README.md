# Changas Backend

## Overview
Este repo contiene el backend de Changas. 

## Integrantes
- Leandro Arroyo
- Angelo Padron

## Tecnologias
- Spring Boot
- Firebase
- Swagger

## Enlaces
- [Repositorio central](http://github.com/angelodpadron/changas-doc)

## Requisitos
- Proyecto Firebase (Auth y Firestore)
- JDK 21

## Configurar y ejecutar proyecto
- Configurar una variable de entorno `GOOGLE_APPLICATION_CREDENTIALS` que apunte a un archivo `.json` con las credenciales del proyecto Firebase.
- En caso de utilizar un emulador, configurar el mismo para Authentication y Firestore en la carpeta raiz del proyecto, y configurar el mismo para iniciar con el perfil `dev`
- Ejecutar `./gradlew bootRun`, o `./gradlew bootRun --args='--spring.profiles.active=dev` para el punto anterior (recordar tener el emulador de Firebase corriendo)
