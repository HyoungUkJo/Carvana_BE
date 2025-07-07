# Java 21 환경을 갖춘 경량 JDK 베이스 이미지
FROM eclipse-temurin:21-jdk

# 앱 실행 디렉토리 설정
WORKDIR /app

# Gradle 빌드 결과물(.jar) 복사
COPY build/libs/*.jar app.jar

# Spring Boot 기본 포트
EXPOSE 8080

# Spring Boot 실행 명령
ENTRYPOINT ["java", "-jar", "app.jar"]
