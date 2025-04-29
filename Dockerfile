# 베이스 이미지 (ARM용 OpenJDK Base Image)
FROM arm64v8/openjdk:17-jdk

# 작업 디렉토리 생성
WORKDIR /app

# JAR 파일 복사 (Maven 빌드 후 생성된 파일)
COPY target/stock-market-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# 컨테이너 실행 시 JAR 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]