# Node.js 스테이지: Vue3 빌드
FROM node:lts AS frontend-build
WORKDIR /app/front
COPY front/package*.json ./
RUN npm install
COPY front .
RUN npm run build

# Spring Boot 빌드 스테이지
FROM openjdk:17-jdk AS backend-build
WORKDIR /app/backend
COPY . .
COPY --from=frontend-build /app/front/dist ./src/main/resources/static
RUN chmod +x ./gradlew
RUN ./gradlew build --no-daemon

# 최종 실행 스테이지
FROM openjdk:17-jdk
WORKDIR /app
COPY --from=backend-build /app/backend/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

# cic