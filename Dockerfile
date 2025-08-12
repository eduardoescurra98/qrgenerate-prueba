FROM eclipse-temurin:21.0.2_13-jre-jammy

# Configurar timezone y locale
ENV TZ=America/Lima
ENV LANG=es_ES.UTF-8
ENV LANGUAGE=es_ES:es
ENV LC_ALL=es_ES.UTF-8

# Instalar locales
RUN apt-get update && apt-get install -y locales && \
    sed -i -e 's/# es_ES.UTF-8 UTF-8/es_ES.UTF-8 UTF-8/' /etc/locale.gen && \
    locale-gen && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copiar archivos Maven
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Dar permisos y descargar dependencias
RUN chmod +x mvnw && \
    ./mvnw dependency:go-offline -B

# Copiar código fuente
COPY src src

# Compilar con configuración específica de codificación
RUN ./mvnw clean package \
    -Dfile.encoding=UTF-8 \
    -Dmaven.compiler.source=21 \
    -Dmaven.compiler.target=21 \
    -Dproject.build.sourceEncoding=UTF-8 \
    -Dproject.reporting.outputEncoding=UTF-8 \
    -DskipTests

EXPOSE 8080

ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-jar", "target/qr-0.0.1-SNAPSHOT.jar"]