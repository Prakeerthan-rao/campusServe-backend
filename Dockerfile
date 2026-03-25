# ---------- Stage 1: Build ----------
FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml and download dependencies first (for caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the jar
RUN mvn clean package -DskipTests

# ---------- Stage 2: Run ----------
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copy jar from builder stage (explicit name — no glob ambiguity)
COPY --from=builder /app/target/campus-service-0.0.1-SNAPSHOT.jar app.jar

# Expose port (Render uses $PORT env var, defaults to 8080)
EXPOSE 8080

# Run the application
# Pass Spring's server.port from Render's $PORT env var at runtime
ENTRYPOINT ["java", "-jar", "app.jar"]
