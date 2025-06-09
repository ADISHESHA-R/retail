# Build stage
FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /app
COPY . /app
RUN mvn -f /app/pom.xml clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
