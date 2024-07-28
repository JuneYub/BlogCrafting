pipeline {
    agent any
    environment {
        FRONTEND_NAME = "blog-frontend"
        BACKEND_NAME = "blog-backend"
        FRONTEND_PORT = "3000"
        BACKEND_PORT = "8080"
        FRONTEND_IMAGE = "${FRONTEND_NAME}:${BUILD_NUMBER}"
        BACKEND_IMAGE = "${BACKEND_NAME}:${BUILD_NUMBER}"
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build Frontend Docker Image') {
            steps {
                dir('BlogCrafting/front') {
                    sh "docker build -t ${FRONTEND_IMAGE} ."
                }
            }
        }
        stage('Build Backend Docker Image') {
            steps {
                sh "docker build -t ${BACKEND_IMAGE} ."
            }
        }
        stage('Deploy Frontend') {
            steps {
                sh """
                    docker stop ${FRONTEND_NAME} || true
                    docker rm ${FRONTEND_NAME} || true
                    docker run -d --name ${FRONTEND_NAME} -p ${FRONTEND_PORT}:3000 ${FRONTEND_IMAGE}
                """
            }
        }
        stage('Deploy Backend') {
            steps {
                sh """
                    docker stop ${BACKEND_NAME} || true
                    docker rm ${BACKEND_NAME} || true
                    docker run -d --name ${BACKEND_NAME} -p ${BACKEND_PORT}:8080 ${BACKEND_IMAGE}
                """
            }
        }
    }
    post {
        failure {
            echo 'The Pipeline failed :('
        }
        success {
            echo 'The Pipeline completed successfully :)'
        }
    }
}