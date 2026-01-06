pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
        skipDefaultCheckout(true)
    }

    environment {
        APP_BASE_URL = 'http://host.docker.internal:8091'
        SELENIUM_REMOTE_URL = 'http://host.docker.internal:4444/wd/hub'

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
                // Not: Sizde surefire "skipUnitTests" gibi bir property ile kontrol ediliyor olabilir.
                // Bu yüzden ikisini de veriyoruz; build aşamasında test kesin koşmasın.
                sh 'mvn -DskipTests=true -DskipUnitTests=true package'
            }
        }

        stage('3) Unit Tests') {
            steps {
                // Önce default surefire dizinini temizle (raporlar karışmasın)
                sh 'rm -rf target/surefire-reports || true'
                sh 'rm -rf target/surefire-reports-unit || true'

                // Unit testleri çalıştır (raporlar default: target/surefire-reports)
                sh 'mvn -DskipUnitTests=false test'

                // Oluşan raporları unit klasörüne kopyala
                sh '''
                    set -e
                    mkdir -p target/surefire-reports-unit
                    echo "--- default surefire reports ---"
                    ls -la target/surefire-reports || true
                    echo "--- copy unit xml ---"
                    cp -v target/surefire-reports/*.xml target/surefire-reports-unit/ 2>/dev/null || true
                    echo "--- unit surefire reports ---"
                    ls -la target/surefire-reports-unit || true
                    find target/surefire-reports-unit -maxdepth 1 -name "*.xml" -print || true
                '''
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports-unit/*.xml'
                }
            }
        }

        stage('4) Integration Tests (failsafe)') {
            steps {
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

        stage('6.1) Selenium - Scenario 1 (Create)') {
            steps {
                sh 'rm -rf target/surefire-reports || true'
                sh 'rm -rf target/selenium-reports/create || true'

                sh 'mvn -Pselenium -Dtest=RestaurantCreateScenarioTest test'

                sh '''
                    set -e
                    mkdir -p target/selenium-reports/create
                    echo "--- default surefire reports (selenium create) ---"
                    ls -la target/surefire-reports || true
                    echo "--- copy selenium create xml ---"
                    cp -v target/surefire-reports/*.xml target/selenium-reports/create/ 2>/dev/null || true
                    echo "--- selenium create reports ---"
                    ls -la target/selenium-reports/create || true
                    find target/selenium-reports/create -maxdepth 1 -name "*.xml" -print || true
                '''
            }
            post {
                always {
                    junit testResults: '**/target/selenium-reports/create/*.xml'
                }
            }
        }

        stage('6.2) Selenium - Scenario 2 (Update)') {
            steps {
                sh 'rm -rf target/surefire-reports || true'
                sh 'rm -rf target/selenium-reports/update || true'

                sh 'mvn -Pselenium -Dtest=RestaurantUpdateScenarioTest test'

                sh '''
                    set -e
                    mkdir -p target/selenium-reports/update
                    echo "--- default surefire reports (selenium update) ---"
                    ls -la target/surefire-reports || true
                    echo "--- copy selenium update xml ---"
                    cp -v target/surefire-reports/*.xml target/selenium-reports/update/ 2>/dev/null || true
                    echo "--- selenium update reports ---"
                    ls -la target/selenium-reports/update || true
                    find target/selenium-reports/update -maxdepth 1 -name "*.xml" -print || true
                '''
            }
            post {
                always {
                    junit testResults: '**/target/selenium-reports/update/*.xml'
                }
            }
        }

        stage('6.3) Selenium - Scenario 3 (Delete)') {
            steps {
                sh 'rm -rf target/surefire-reports || true'
                sh 'rm -rf target/selenium-reports/delete || true'

                sh 'mvn -Pselenium -Dtest=RestaurantDeleteScenarioTest test'

                sh '''
                    set -e
                    mkdir -p target/selenium-reports/delete
                    echo "--- default surefire reports (selenium delete) ---"
                    ls -la target/surefire-reports || true
                    echo "--- copy selenium delete xml ---"
                    cp -v target/surefire-reports/*.xml target/selenium-reports/delete/ 2>/dev/null || true
                    echo "--- selenium delete reports ---"
                    ls -la target/selenium-reports/delete || true
                    find target/selenium-reports/delete -maxdepth 1 -name "*.xml" -print || true
                '''
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
