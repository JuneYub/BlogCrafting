FROM openjdk:17-jdk

WORKDIR /app

COPY . .
RUN chmod +x ./gradlew

# 의존성 다운로드
RUN ./gradlew dependencies --no-daemon

RUN ./gradlew build --no-daemon -x test

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/build/libs/*.jar"]