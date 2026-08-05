# Build stage
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/eco-park-1.0.0.jar app.jar
EXPOSE 8082
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8082} -jar app.jar"]
