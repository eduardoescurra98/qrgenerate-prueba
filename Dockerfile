FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
# Agregar permisos de ejecución al script mvnw
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw package -DskipTests
EXPOSE 8080
CMD ["java", "-jar", "target/qr-0.0.1-SNAPSHOT.jar"]