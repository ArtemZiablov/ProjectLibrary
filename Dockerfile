FROM openjdk:17-alpine
WORKDIR /app
COPY mvnw pom.xml ./
COPY .mvn .mvn
COPY src src
RUN chmod +x ./mvnw
RUN apk add --no-cache dos2unix && dos2unix mvnw
RUN ./mvnw clean package -DskipTests
EXPOSE 8080
CMD ["java", "-jar", "target/ProjectLibrary-0.0.1-SNAPSHOT.jar"]
