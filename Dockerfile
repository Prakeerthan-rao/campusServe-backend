# ---------- Stage 1: Build ----------
FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# ---------- Stage 2: Run ----------
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

# Don't hardcode EXPOSE — Render controls the port
EXPOSE 8080

# Use shell form so $PORT env var is resolved at runtime
ENTRYPOINT ["sh", "-c", "java -Dserver.port=$PORT -jar app.jar"]
