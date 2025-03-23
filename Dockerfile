FROM eclipse-temurin:17-jdk

ARG JAR_FILE=target/shortenit-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]