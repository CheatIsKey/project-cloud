FROM amazoncorretto:17-al2023-headless
WORKDIR /app
COPY build/libs/project-cloud-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]