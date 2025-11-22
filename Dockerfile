# ====== STAGE 1: BUILD ======
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copiar somente arquivos essenciais primeiro (melhora cache)
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Baixa dependências
RUN ./mvnw dependency:go-offline -B

# Copia o restante do projeto
COPY src ./src

# Build
RUN ./mvnw clean package -DskipTests

# ====== STAGE 2: RUNTIME ======
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copiar apenas o quarkus-app gerado
COPY --from=build /app/target/quarkus-app ./quarkus-app

# Run
CMD ["java", "-jar", "quarkus-app/quarkus-run.jar"]