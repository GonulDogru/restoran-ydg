pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
        skipDefaultCheckout(true)
    }

    environment {
        // Jenkins container'ından host'taki portlara erişim
        APP_BASE_URL = 'http://host.docker.internal:8091'
        SELENIUM_REMOTE_URL = 'http://host.docker.internal:4444/wd/hub'

        // Testcontainers stabilitesi
        TESTCONTAINERS_HOST_OVERRIDE = 'host.docker.internal'
        TESTCONTAINERS_RYUK_DISABLED = 'true'
    }

    stages {

        stage('0) Workspace Temizle') {
            steps {
                deleteDir()
            }
        }

        stage('1) Checkout') {
            steps {
                checkout scm
            }
        }

        stage('2) Build (skip tests)') {
            steps {
                sh 'mvn -DskipTests=true package'
            }
        }

        stage('3) Unit Tests') {
            steps {
                // Unit raporları ayrı klasörde (Selenium ile karışmasın)
                sh 'rm -rf target/surefire-reports-unit || true'
                sh 'mvn -DskipUnitTests=false -Dsurefire.reportsDirectory=target/surefire-reports-unit test'
                sh 'echo "--- unit surefire reports ---" && ls -la target/surefire-reports-unit || true'
                sh 'find target/surefire-reports-unit -maxdepth 1 -name "*.xml" -print || true'
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports-unit/*.xml'
                }
            }
        }

        stage('4) Integration Tests (failsafe)') {
            steps {
                // Integration raporları failsafe altında zaten ayrı
                sh 'rm -rf target/failsafe-reports || true'
                sh 'mvn -DskipUnitTests=true verify'
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

        // 6) Sistem testi: Çalışır sistem üzerinden en az 3 Selenium senaryosu + rapor

        stage('6.1) Selenium - Scenario 1 (Create)') {
            steps {
                sh 'rm -rf target/selenium-reports/create || true'
                sh 'mvn -Pselenium -Dtest=RestaurantCreateScenarioTest -Dsurefire.reportsDirectory=target/selenium-reports/create test'
                sh 'echo "--- selenium create reports ---" && ls -la target/selenium-reports/create || true'
                sh 'find target/selenium-reports/create -maxdepth 1 -name "*.xml" -print || true'
            }
            post {
                always {
                    junit testResults: '**/target/selenium-reports/create/*.xml'
                }
            }
        }

        stage('6.2) Selenium - Scenario 2 (Update)') {
            steps {
                sh 'rm -rf target/selenium-reports/update || true'
                sh 'mvn -Pselenium -Dtest=RestaurantUpdateScenarioTest -Dsurefire.reportsDirectory=target/selenium-reports/update test'
                sh 'echo "--- selenium update reports ---" && ls -la target/selenium-reports/update || true'
                sh 'find target/selenium-reports/update -maxdepth 1 -name "*.xml" -print || true'
            }
            post {
                always {
                    junit testResults: '**/target/selenium-reports/update/*.xml'
                }
            }
        }

        stage('6.3) Selenium - Scenario 3 (Delete)') {
            steps {
                sh 'rm -rf target/selenium-reports/delete || true'
                sh 'mvn -Pselenium -Dtest=RestaurantDeleteScenarioTest -Dsurefire.reportsDirectory=target/selenium-reports/delete test'
                sh 'echo "--- selenium delete reports ---" && ls -la target/selenium-reports/delete || true'
                sh 'find target/selenium-reports/delete -maxdepth 1 -name "*.xml" -print || true'
            }
            post {
                always {
                    junit testResults: '**/target/selenium-reports/delete/*.xml'
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
