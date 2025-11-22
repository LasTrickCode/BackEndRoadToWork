FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN chmod +x mvnw

RUN ./mvnw -DskipTests clean install

CMD ["java", "-jar", "target/quarkus-app/quarkus-run.jar"]