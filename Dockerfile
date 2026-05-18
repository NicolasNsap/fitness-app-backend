#imagen base con java 21
FROM eclipse-temurin:21-jdk-jammy

#carpeta de trabajo dentro del contenedor
WORKDIR /app

#copiar el JAR al contenedor
COPY target/fitness-backend-0.0.1-SNAPSHOT.jar app.jar

#puerto que usa la aplicacion
EXPOSE 8080

#comando que se ejecuta al iniciar el contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]

