FROM amazoncorretto:17-alpine

WORKDIR /app

COPY target/url-shortener.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]