pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    environment {
        APP_BASE_URL = 'http://host.docker.internal:8091'
        SELENIUM_REMOTE_URL = 'http://host.docker.internal:4444/wd/hub'

        TESTCONTAINERS_HOST_OVERRIDE = 'host.docker.internal'
        TESTCONTAINERS_RYUK_DISABLED = 'true'
    }

    stages {

        stage('0) Workspace Temizle') {
            steps { deleteDir() }
        }

        stage('1) Checkout') {
            steps { checkout scm }
        }

        stage('2) Build (skip tests)') {
            steps {
                // Build aşamasında test istemiyoruz
                sh 'mvn -q -DskipTests=true package'
            }
        }

        stage('3) Unit Tests') {
            steps {
                sh 'rm -rf target/surefire-reports-unit || true'
                // Eğer POM'da skipUnitTests property varsa bunu kapatıyoruz:
                sh 'mvn -q -DskipUnitTests=false -Dtest=*UnitTest -Dsurefire.reportsDirectory=target/surefire-reports-unit test'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports-unit/*.xml'
                }
            }
        }

        stage('4) Integration Tests (failsafe)') {
            steps {
                // Failsafe ile doğru yöntem: verify
                // IT testleri için genelde *IT isimlendirme kullanılır
                sh 'rm -rf target/failsafe-reports || true'
                sh 'mvn -q -DskipUnitTests=true verify'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/target/failsafe-reports/*.xml'
                }
            }
        }

        stage('5) Docker Up') {
            steps {
                sh 'docker compose down -v || true'
                sh 'docker compose up -d --build'
            }
        }

        stage('5.1) Wait App Ready') {
            steps {
                sh '''
                    set -e
                    echo "Waiting for app readiness at: ${APP_BASE_URL}"
                    for i in $(seq 1 60); do
                      if curl -fsS "${APP_BASE_URL}/" >/dev/null 2>&1; then
                        echo "App is ready."
                        exit 0
                      fi
                      sleep 2
                    done
                    echo "App did not become ready in time."
                    docker compose ps || true
                    docker compose logs --no-color --tail=200 || true
                    exit 1
                '''
            }
        }

        stage('6.1) Selenium - Scenario 1 (Create)') {
            steps {
                sh 'rm -rf target/surefire-reports-selenium-create || true'
                sh 'mvn -q -Pselenium -Dtest=RestaurantCreateScenarioTest -Dsurefire.reportsDirectory=target/surefire-reports-selenium-create test'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports-selenium-create/*.xml'
                }
            }
        }

        stage('6.2) Selenium - Scenario 2 (Update)') {
            steps {
                sh 'rm -rf target/surefire-reports-selenium-update || true'
                sh 'mvn -q -Pselenium -Dtest=RestaurantUpdateScenarioTest -Dsurefire.reportsDirectory=target/surefire-reports-selenium-update test'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports-selenium-update/*.xml'
                }
            }
        }

        stage('6.3) Selenium - Scenario 3 (Delete)') {
            steps {
                sh 'rm -rf target/surefire-reports-selenium-delete || true'
                sh 'mvn -q -Pselenium -Dtest=RestaurantDeleteScenarioTest -Dsurefire.reportsDirectory=target/surefire-reports-selenium-delete test'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports-selenium-delete/*.xml'
                }
            }
        }
    }

    post {
        always {
            script {
                try {
                    sh 'docker compose down -v || true'
                } catch (e) {
                    echo "Post cleanup skipped: ${e}"
                }
            }
        }
    }
}
