FROM eclipse-temurin:21-jdk

# Configurar timezone y locale
ENV TZ=America/Lima
ENV LANG=es_ES.UTF-8
ENV LANGUAGE=es_ES:es
ENV LC_ALL=es_ES.UTF-8

# Instalar dependencias necesarias
RUN apt-get update && \
    apt-get install -y locales && \
    locale-gen es_ES.UTF-8 && \
    update-locale LANG=es_ES.UTF-8 && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copiar solo los archivos necesarios para dependencias
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Dar permisos y descargar dependencias
RUN chmod +x mvnw && \
    ./mvnw dependency:resolve

# Copiar el resto del código
COPY src ./src

# Compilar la aplicación
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-jar", "target/qr-0.0.1-SNAPSHOT.jar"]