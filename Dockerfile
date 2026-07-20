# ---- Build stage ----
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# Copy Maven wrapper and project descriptor first for better layer caching
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -B dependency:go-offline

# Copy sources and build the executable jar (skip tests for faster, deterministic builds)
COPY src/ src/
RUN ./mvnw -B clean package -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/fitness-backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

