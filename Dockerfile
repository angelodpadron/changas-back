# Build stage
FROM gradle:latest AS BUILD_STAGE_IMAGE

WORKDIR /home/gradle/project

COPY --chown=gradle:gradle build.gradle settings.gradle ./
COPY --chown=gradle:gradle src src

RUN gradle build --no-daemon

# Package stage
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=BUILD_STAGE_IMAGE /home/gradle/project/build/libs/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]