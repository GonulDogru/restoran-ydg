pipeline {
    agent any

    options { timestamps() }

    environment {
        // Jenkins Windows agent'ta çalışıyorsa localhost kullan:
        APP_BASE_URL = 'http://localhost:8091'
        SELENIUM_REMOTE_URL = 'http://localhost:4444/wd/hub'

        // Eğer Jenkins Docker container içinde çalışıyorsa şu ikisini böyle yap:
        // APP_BASE_URL = 'http://host.docker.internal:8091'
        // SELENIUM_REMOTE_URL = 'http://host.docker.internal:4444/wd/hub'
    }

    stages {

        stage('1) Checkout') {
            steps { checkout scm }
        }

        stage('2) Build (skip tests)') {
            steps {
                script {
                    if (isUnix()) sh 'mvn -q -DskipTests package'
                    else          bat 'mvn -q -DskipTests package'
                }
            }
        }

        stage('3) Unit Tests') {
            steps {
                script {
                    // Sadece Unit testleri
                    if (isUnix()) sh 'mvn -q -Dtest=*UnitTest test'
                    else          bat 'mvn -q -Dtest=*UnitTest test'
                }
            }
            post {
                always { junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml' }
            }
        }

        stage('4) Integration Tests') {
            steps {
                script {
                    // Sadece IT testleri
                    if (isUnix()) sh 'mvn -q -Dtest=*IT test'
                    else          bat 'mvn -q -Dtest=*IT test'
                }
            }
            post {
                always { junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml' }
            }
        }

        stage('5) Docker Up') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'docker compose down -v || true'
                        sh 'docker compose up -d --build'
                    } else {
                        // Windows'ta "|| true" yok: down hata verse de pipeline'ı düşürmemesi için iki komut ayrı
                        bat 'docker compose down -v'
                        bat 'docker compose up -d --build'
                    }
                }
            }
        }

        stage('6.1) Selenium - Scenario (Create)') {
            steps {
                script {
                    if (isUnix()) sh 'mvn -q -Pselenium -Dtest=RestaurantCreateScenarioTest test'
                    else          bat 'mvn -q -Pselenium -Dtest=RestaurantCreateScenarioTest test'
                }
            }
            post {
                always { junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml' }
            }
        }

        stage('6.2) Selenium - Scenario (Update)') {
            steps {
                script {
                    if (isUnix()) sh 'mvn -q -Pselenium -Dtest=RestaurantUpdateScenarioTest test'
                    else          bat 'mvn -q -Pselenium -Dtest=RestaurantUpdateScenarioTest test'
                }
            }
            post {
                always { junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml' }
            }
        }

        stage('6.3) Selenium - Scenario (Delete)') {
            steps {
                script {
                    if (isUnix()) sh 'mvn -q -Pselenium -Dtest=RestaurantDeleteScenarioTest test'
                    else          bat 'mvn -q -Pselenium -Dtest=RestaurantDeleteScenarioTest test'
                }
            }
            post {
                always { junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml' }
            }
        }
    }

    post {
        always {
            script {
                if (isUnix()) sh 'docker compose down -v || true'
                else          bat 'docker compose down -v'
            }
            archiveArtifacts artifacts: 'target/site/jacoco/**', allowEmptyArchive: true
        }
    }
}
