FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copiar solo el pom.xml primero
COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw .

# Dar permisos de ejecución y descargar dependencias
RUN chmod +x mvnw && \
    ./mvnw dependency:go-offline -B

# Copiar el código fuente
COPY src/ src/

# Compilar la aplicación
RUN ./mvnw package -DskipTests

# Configurar la ejecución
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "target/qr-0.0.1-SNAPSHOT.jar"]