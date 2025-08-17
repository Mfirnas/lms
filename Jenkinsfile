pipeline {
    agent any

    environment {
        REGISTRY = "firnasfz"        // Your DockerHub username
        APP_NAME = "lms"             // App name
        BRANCH   = "develop"         // Branch to trigger
        IMAGE_TAG = "${env.BUILD_NUMBER}"
    }

    triggers {
        // Uncomment this if you can't setup webhook
        // pollSCM('H/2 * * * *')
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: env.BRANCH, url: 'https://github.com/Mfirnas/lms.git'
            }
        }

        stage('Build JAR') {
            steps {
                bat 'mvnw.cmd clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("${REGISTRY}/${APP_NAME}:${IMAGE_TAG}")
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'dockerhub-cred') {
                        docker.image("${REGISTRY}/${APP_NAME}:${IMAGE_TAG}").push()
                        docker.image("${REGISTRY}/${APP_NAME}:${IMAGE_TAG}").push("latest")
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                bat """
                  docker stop ${APP_NAME} || true
                  docker rm ${APP_NAME} || true
                  docker run -d --name ${APP_NAME} -p 8081:8080 ${REGISTRY}/${APP_NAME}:${IMAGE_TAG}
                """
            }
        }
    }
}
