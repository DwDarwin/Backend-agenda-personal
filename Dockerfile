
# Imagen base con Java 21
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copiar todos los archivos del proyecto
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

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
