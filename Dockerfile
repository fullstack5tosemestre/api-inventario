FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/inventario.jar app.jar
EXPOSE 9090
CMD ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]

# Create image
# docker build -t xdainz/api-inventario .