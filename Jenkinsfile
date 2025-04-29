pipeline {
    agent any

    environment {
        GIT_URL = 'https://github.com/imclaremont/stock-market.git'
        GIT_BRANCH = 'main' // 또는 master
        GIT_ID = 'skala-github-id' // GitHub PAT credential ID
        IMAGE_REGISTRY = 'amdp-registry.skala-ai.com/skala25a'
        IMAGE_NAME = 'sk029-stock-market'
        IMAGE_TAG = '1.0'
        DOCKER_CREDENTIAL_ID = 'skala-image-registry-id'  // Harbor 인증 정보 ID
    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: "${GIT_BRANCH}",
                    url: "${GIT_URL}",
                    credentialsId: "${GIT_ID}"   // GitHub PAT credential ID
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Docker Build & Push') {
            steps {
                script {
                    docker.withRegistry("https://${IMAGE_REGISTRY}", "${DOCKER_CREDENTIAL_ID}") {
                        def appImage = docker.build("${IMAGE_REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}", "--platform=linux/amd64 .")
                        appImage.push()
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                sh '''
                    # 1. Ingress 먼저 삭제 (존재하지 않아도 무시)
                    kubectl delete -f k8s/ingress.yaml --ignore-not-found
                    
                    # 2. 기본 리소스 배포
                    kubectl apply -f k8s/deployment.yaml
                    kubectl apply -f k8s/service.yaml
                    kubectl apply -f k8s/configmap.yaml
                    kubectl apply -f k8s/hpa.yaml
                    
                    # 3. Ingress 마지막에 배포
                    kubectl apply -f k8s/ingress.yaml
                    
                    # 4. 배포 상태 확인
                    kubectl rollout status deployment/sk029-stock-market-deployment
                '''
            }
        }
    }
}
