FROM eclipse-temurin:21-jdk

# Configurar variables de entorno
ENV JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8"
ENV LANG=es_ES.UTF-8
ENV LC_ALL=es_ES.UTF-8
ENV MAVEN_OPTS="-Dfile.encoding=UTF-8"

# Instalar locales
RUN apt-get update && \
    apt-get install -y locales && \
    sed -i -e 's/# es_ES.UTF-8 UTF-8/es_ES.UTF-8 UTF-8/' /etc/locale.gen && \
    locale-gen && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copiar archivos de Maven
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Configurar permisos y Maven
RUN chmod +x ./mvnw && \
    ./mvnw --version && \
    ./mvnw dependency:go-offline

# Copiar código fuente
COPY src ./src

# Compilar la aplicación
RUN ./mvnw clean package \
    -Dspring.profiles.active=prod \
    -Dfile.encoding=UTF-8 \
    -Dproject.build.sourceEncoding=UTF-8 \
    -Dproject.reporting.outputEncoding=UTF-8 \
    -DskipTests

EXPOSE 8080

ENTRYPOINT ["java", \
            "-Dfile.encoding=UTF-8", \
            "-Dspring.profiles.active=prod", \
            "-jar", \
            "target/qr-0.0.1-SNAPSHOT.jar"]