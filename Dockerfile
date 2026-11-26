
# Imagen base con Java 21
FROM eclipse-temurin:21-jdk AS build

# Directorio de trabajo
WORKDIR /app

# Copiar archivos del proyecto
COPY . .

# Dar permisos al wrapper de Gradle
RUN chmod +x gradlew

# Compilar la aplicación
RUN ./gradlew clean bootJar -x test

# Imagen para ejecutar la aplicación
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copiar el JAR generado
COPY --from=build /app/build/libs/*.jar app.jar

# Puerto de la aplicación
EXPOSE 8080

# Ejecutar Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
