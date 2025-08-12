FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Configurar codificación UTF-8
ENV LANG='es_ES.UTF-8' LANGUAGE='es_ES:es' LC_ALL='es_ES.UTF-8'

# Copiar solo el pom.xml primero
COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw .

# Dar permisos de ejecución y descargar dependencias
RUN chmod +x mvnw && \
    ./mvnw dependency:go-offline -B

# Copiar el código fuente
COPY src/ src/

# Compilar la aplicación con codificación UTF-8
RUN ./mvnw package -DskipTests -Dfile.encoding=UTF-8

# Configurar la ejecución
EXPOSE 8080
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-jar", "target/qr-0.0.1-SNAPSHOT.jar"]