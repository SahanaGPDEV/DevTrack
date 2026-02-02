# Build Stage
FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
WORKDIR /app/devtrack-backend
RUN mvn clean package -DskipTests

# Run Stage
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/devtrack-backend/target/devtrack-backend-1.0.0.jar app.jar
ENV PORT=8085
EXPOSE 8085
ENTRYPOINT ["java","-jar","app.jar"]
