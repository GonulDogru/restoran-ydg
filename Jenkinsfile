pipeline {
    agent any

    options { timestamps() }

    environment {
        // Jenkins Docker container içinde çalıştığı için:
        APP_BASE_URL = 'http://host.docker.internal:8091'
        SELENIUM_REMOTE_URL = 'http://host.docker.internal:4444/wd/hub'
    }

    stages {

        stage('1) Checkout') {
            steps { checkout scm }
        }

        stage('2) Build (skip tests)') {
            steps { sh 'mvn -q -DskipTests package' }
        }

        stage('3) Unit Tests') {
            steps { sh 'mvn -q -Dtest=*UnitTest test' }
            post {
                always { junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml' }
            }
        }

        stage('4) Integration Tests') {
            steps { sh 'mvn -q -Dtest=*IT test' }
            post {
                always { junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml' }
            }
        }

        stage('5) Docker Up') {
            steps {
                sh 'docker compose down -v || true'
                sh 'docker compose up -d --build'
            }
        }

        stage('6.1) Selenium - Scenario 1 (Create)') {
            steps { sh 'mvn -q -Pselenium -Dtest=RestaurantCreateScenarioTest test' }
            post { always { junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml' } }
        }

        stage('6.2) Selenium - Scenario 2 (Update)') {
            steps { sh 'mvn -q -Pselenium -Dtest=RestaurantUpdateScenarioTest test' }
            post { always { junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml' } }
        }

        stage('6.3) Selenium - Scenario 3 (Delete)') {
            steps { sh 'mvn -q -Pselenium -Dtest=RestaurantDeleteScenarioTest test' }
            post { always { junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml' } }
        }
    }

    post {
        always {
            sh 'docker compose down -v || true'
            archiveArtifacts artifacts: 'target/site/jacoco/**', allowEmptyArchive: true
        }
    }
}
