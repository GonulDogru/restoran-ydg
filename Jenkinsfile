pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
        // Checkout'u özellikle Stage 1'de yaptığımız için
        skipDefaultCheckout(true)
    }

    environment {
        // Uygulama ve Selenium container'larına Jenkins container'ından erişim
        APP_BASE_URL = 'http://host.docker.internal:8091'
        SELENIUM_REMOTE_URL = 'http://host.docker.internal:4444/wd/hub'

        // Testcontainers stabilitesi için
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
                sh 'mvn -q -DskipTests=true package'
            }
        }

        stage('3) Unit Tests') {
            steps {
                // Raporlar karışmasın (default surefire yolu)
                sh 'rm -rf target/surefire-reports || true'

                // POM: surefire includes => **/*UnitTest.java
                sh 'mvn -q -DskipUnitTests=false test'

                // Debug: raporlar gerçekten üretildi mi?
                sh 'echo "--- surefire reports ---" && ls -la target/surefire-reports || true'
                sh 'find target/surefire-reports -maxdepth 1 -name "*.xml" -print || true'
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('4) Integration Tests (failsafe)') {
            steps {
                sh 'rm -rf target/failsafe-reports || true'

                // POM: failsafe includes => **/*IT.java (verify ile çalışır)
                sh 'mvn -q -DskipUnitTests=true verify'

                sh 'echo "--- failsafe reports ---" && ls -la target/failsafe-reports || true'
                sh 'find target/failsafe-reports -maxdepth 1 -name "*.xml" -print || true'
            }
            post {
                always {
                    junit testResults: '**/target/failsafe-reports/*.xml'
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

        // 6) Sistem testi: En az 3 senaryo + raporlama
        stage('6.1) Selenium - Scenario 1 (Create)') {
            steps {
                sh 'rm -rf target/surefire-reports || true'
                sh 'mvn -q -Pselenium -Dtest=RestaurantCreateScenarioTest test'
                sh 'echo "--- surefire reports (selenium create) ---" && ls -la target/surefire-reports || true'
                sh 'find target/surefire-reports -maxdepth 1 -name "*.xml" -print || true'
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('6.2) Selenium - Scenario 2 (Update)') {
            steps {
                sh 'rm -rf target/surefire-reports || true'
                sh 'mvn -q -Pselenium -Dtest=RestaurantUpdateScenarioTest test'
                sh 'echo "--- surefire reports (selenium update) ---" && ls -la target/surefire-reports || true'
                sh 'find target/surefire-reports -maxdepth 1 -name "*.xml" -print || true'
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('6.3) Selenium - Scenario 3 (Delete)') {
            steps {
                sh 'rm -rf target/surefire-reports || true'
                sh 'mvn -q -Pselenium -Dtest=RestaurantDeleteScenarioTest test'
                sh 'echo "--- surefire reports (selenium delete) ---" && ls -la target/surefire-reports || true'
                sh 'find target/surefire-reports -maxdepth 1 -name "*.xml" -print || true'
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports/*.xml'
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
