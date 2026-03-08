FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY . .
RUN ./gradlew jar --no-daemon

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/sample-java-project-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
