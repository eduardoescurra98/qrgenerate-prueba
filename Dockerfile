FROM eclipse-temurin:21-jdk

# Configurar timezone y locale
ENV TZ=America/Lima \
    LANG=es_ES.UTF-8 \
    LANGUAGE=es_ES:es \
    LC_ALL=es_ES.UTF-8 \
    MAVEN_OPTS="-Dfile.encoding=UTF-8"

# Instalar dependencias necesarias
RUN apt-get update && \
    apt-get install -y locales && \
    sed -i -e 's/# es_ES.UTF-8 UTF-8/es_ES.UTF-8 UTF-8/' /etc/locale.gen && \
    dpkg-reconfigure --frontend=noninteractive locales && \
    update-locale LANG=es_ES.UTF-8 && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copiar archivos Maven con codificación específica
COPY --chown=1000:1000 mvnw .
COPY --chown=1000:1000 .mvn .mvn
COPY --chown=1000:1000 pom.xml .

# Dar permisos y configurar Maven
RUN chmod +x mvnw && \
    echo "export MAVEN_OPTS='-Dfile.encoding=UTF-8'" > ~/.mavenrc

# Copiar el código fuente con codificación específica
COPY --chown=1000:1000 src src

# Compilar la aplicación
RUN ./mvnw clean package \
    -Dfile.encoding=UTF-8 \
    -Dproject.build.sourceEncoding=UTF-8 \
    -Dproject.reporting.outputEncoding=UTF-8 \
    -DskipTests

EXPOSE 8080

ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-jar", "target/qr-0.0.1-SNAPSHOT.jar"]