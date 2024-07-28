# Gradle 빌드 이미지 설정 (JDK 17 사용)
FROM gradle:8.9-jdk17 AS builder

# 작업 디렉토리 설정
WORKDIR /build

# 전체 소스 코드를 복사
COPY . .

# Gradle 의존성들을 다운로드하고 애플리케이션을 빌드
RUN gradle build -x test --no-daemon --info --stacktrac

# 실행 이미지를 설정 (JDK 17 사용)
FROM openjdk:17-jdk-slim

# 빌드 단계에서 빌드된 결과물을 복사
COPY --from=builder /build/build/libs/*.jar /app/app.jar

# 컨테이너 시작 시 실행할 명령을 설정
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# 애플리케이션 포트 설정
EXPOSE 8080
