pipeline {
    agent any

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'https://github.com/caadiq/talkwave-server'
            }
        }

        stage('Build JAR') {
            steps {
                sh 'chmod +x ./gradlew'
                sh './gradlew clean build -x test'
            }
        }

        stage('Deploy Container') {
            steps {
                script {
                    dir('/var/jenkins_home/workspace/talkwave') {
                        sh 'docker-compose down'
                        sh 'docker-compose up -d'
                    }
                }
            }
        }

        stage('Copy JAR') {
            steps {
                sh 'docker cp /var/jenkins_home/workspace/talkwave/build/libs/talkwave-0.0.1-SNAPSHOT.jar talkwave-springboot:/app/talkwave.jar'
            }
        }

        stage('Copy env') {
            steps {
                sh 'cp /var/jenkins_home/server/env/.env.talkwave /var/jenkins_home/workspace/talkwave/.env'
                sh 'docker cp /var/jenkins_home/server/env/.env.talkwave talkwave-springboot:/app/.env'
            }
        }

        stage('Restart Container') {
            steps {
                sh 'docker restart talkwave-springboot'
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished.'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
