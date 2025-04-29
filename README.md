### 프로젝트 빌드 커맨드
mvn clean install -DskipTests

### 도커 빌드 커맨드
docker build -t amdp-registry.skala-ai.com/skala25a/sk029-stock-market:1.0.0 .

### harbor 로그인 커맨드
docker login amdp-registry.skala-ai.com

### 도커 push 커맨드
docker push amdp-registry.skala-ai.com/skala25a/sk029-stock-market:1.0.0

### 애플리케이션 실행 커맨드  
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8085"