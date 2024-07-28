pipeline {
    agent any
    environment {
        APP_NAME = "blog"
        DOCKER_IMAGE = "${APP_NAME}:${BUILD_NUMBER}"
	APP_PORT ="8081"
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE} ."
            }
        }
        stage('Deploy') {
            steps {
                sh """
                    docker stop ${APP_NAME} || true
                    docker rm ${APP_NAME} || true
                    docker run -d --name ${APP_NAME} -p ${APP_PORT}:8080 ${DOCKER_IMAGE}
                """
            }
        }
    }
    post {
        failure {
            echo 'The Pipeline failed :('
        }
    }
}