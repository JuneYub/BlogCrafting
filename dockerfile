FROM openjdk:17-jdk

WORKDIR /app

COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew build --no-daemon

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/build/libs/*.jar"]