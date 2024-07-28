# Gradle 빌드 이미지 설정 (JDK 17 사용)
FROM gradle:8.9-jdk17 AS builder

WORKDIR /app
 # gradlew 복사
COPY gradlew .

# gradle 복사
COPY gradle gradle
# build.gradle 복사
COPY build.gradle .
# settings.gradle 복사
COPY settings.gradle .
# 웹 어플리케이션 소스 복사
COPY src src

# Gradle wrapper 생성
RUN gradle wrapper

# gradlew 실행권한 부여
RUN chmod +x ./gradlew
# gradlew를 사용하여 실행 가능한 jar 파일 생성
RUN ./gradlew bootJar --stacktrace --info

# 실행 이미지를 설정 (JDK 17 사용)
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar # builder 이미지에서 build/libs/*.jar 파일을 app.jar로 복사

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"] # jar 파일 실행
