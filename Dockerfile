FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY target/educional-0.0.1-SNAPSHOT.jar app.jar

ENV JAVA_TOOL_OPTIONS="-Xmx512m -Xms256m"

ENTRYPOINT ["java", "-jar", "app.jar"]
