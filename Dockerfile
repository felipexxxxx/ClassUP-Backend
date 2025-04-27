# Etapa 1 - Build
FROM maven:3.9.6-eclipse-temurin-17 as builder

WORKDIR /app
COPY . .

RUN ./mvnw clean package -DskipTests

# Etapa 2 - Runtime
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app
COPY --from=builder /app/target/educional-0.0.1-SNAPSHOT.jar app.jar

ENV JAVA_TOOL_OPTIONS="-Xmx512m -Xms256m"

ENTRYPOINT ["java", "-jar", "app.jar"]
