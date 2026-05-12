FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/inventario.jar app.jar
EXPOSE 9090
CMD ["java", "-jar", "app.jar"]