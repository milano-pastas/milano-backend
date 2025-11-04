# --- Stage 1: Build ---
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos pom.xml y descargamos dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el código fuente y construimos el jar
COPY src ./src
RUN mvn clean package -DskipTests

# --- Stage 2: Run ---
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copiamos solo el jar construido
COPY --from=build /app/target/*.jar app.jar

# Puerto que expone el backend
EXPOSE 8080

# Comando para correr Spring Boot
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
