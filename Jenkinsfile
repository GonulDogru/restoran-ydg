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
                sh 'mvn -DskipTests=true -DskipUnitTests=true package'
            }
        }

        stage('3) Unit Tests') {
            steps {
                sh 'rm -rf target/surefire-reports || true'
                sh 'rm -rf target/surefire-reports-unit || true'

                sh 'mvn -DskipUnitTests=false test'

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

        // ===========================
        // NEW SELENIUM STAGES (6.4 - 6.10)
        // ===========================

        stage('6.4) Selenium - Scenario 4 (Users Create)') {
            steps {
                sh 'rm -rf target/surefire-reports || true'
                sh 'rm -rf target/selenium-reports/users-create || true'

                sh 'mvn -Pselenium -Dtest=UsersCreateScenarioTest test'

                sh '''
                    set -e
                    mkdir -p target/selenium-reports/users-create
                    echo "--- default surefire reports (selenium users create) ---"
                    ls -la target/surefire-reports || true
                    echo "--- copy selenium users create xml ---"
                    cp -v target/surefire-reports/*.xml target/selenium-reports/users-create/ 2>/dev/null || true
                    echo "--- selenium users create reports ---"
                    ls -la target/selenium-reports/users-create || true
                    find target/selenium-reports/users-create -maxdepth 1 -name "*.xml" -print || true
                '''
            }
            post {
                always {
                    junit testResults: '**/target/selenium-reports/users-create/*.xml'
                }
            }
        }

        stage('6.5) Selenium - Scenario 5 (Restaurant Search)') {
            steps {
                sh 'rm -rf target/surefire-reports || true'
                sh 'rm -rf target/selenium-reports/restaurant-search || true'

                sh 'mvn -Pselenium -Dtest=RestaurantSearchScenarioTest test'

                sh '''
                    set -e
                    mkdir -p target/selenium-reports/restaurant-search
                    echo "--- default surefire reports (selenium restaurant search) ---"
                    ls -la target/surefire-reports || true
                    echo "--- copy selenium restaurant search xml ---"
                    cp -v target/surefire-reports/*.xml target/selenium-reports/restaurant-search/ 2>/dev/null || true
                    echo "--- selenium restaurant search reports ---"
                    ls -la target/selenium-reports/restaurant-search || true
                    find target/selenium-reports/restaurant-search -maxdepth 1 -name "*.xml" -print || true
                '''
            }
            post {
                always {
                    junit testResults: '**/target/selenium-reports/restaurant-search/*.xml'
                }
            }
        }

        stage('6.6) Selenium - Scenario 6 (Menu Create)') {
            steps {
                sh 'rm -rf target/surefire-reports || true'
                sh 'rm -rf target/selenium-reports/menu-create || true'

                sh 'mvn -Pselenium -Dtest=MenuCreateScenarioTest test'

                sh '''
                    set -e
                    mkdir -p target/selenium-reports/menu-create
                    echo "--- default surefire reports (selenium menu create) ---"
                    ls -la target/surefire-reports || true
                    echo "--- copy selenium menu create xml ---"
                    cp -v target/surefire-reports/*.xml target/selenium-reports/menu-create/ 2>/dev/null || true
                    echo "--- selenium menu create reports ---"
                    ls -la target/selenium-reports/menu-create || true
                    find target/selenium-reports/menu-create -maxdepth 1 -name "*.xml" -print || true
                '''
            }
            post {
                always {
                    junit testResults: '**/target/selenium-reports/menu-create/*.xml'
                }
            }
        }

        stage('6.7) Selenium - Scenario 7 (Masa Create)') {
            steps {
                sh 'rm -rf target/surefire-reports || true'
                sh 'rm -rf target/selenium-reports/masa-create || true'

                sh 'mvn -Pselenium -Dtest=MasaCreateScenarioTest test'

                sh '''
                    set -e
                    mkdir -p target/selenium-reports/masa-create
                    echo "--- default surefire reports (selenium masa create) ---"
                    ls -la target/surefire-reports || true
                    echo "--- copy selenium masa create xml ---"
                    cp -v target/surefire-reports/*.xml target/selenium-reports/masa-create/ 2>/dev/null || true
                    echo "--- selenium masa create reports ---"
                    ls -la target/selenium-reports/masa-create || true
                    find target/selenium-reports/masa-create -maxdepth 1 -name "*.xml" -print || true
                '''
            }
            post {
                always {
                    junit testResults: '**/target/selenium-reports/masa-create/*.xml'
                }
            }
        }

        stage('6.8) Selenium - Scenario 8 (Odeme Create)') {
            steps {
                sh 'rm -rf target/surefire-reports || true'
                sh 'rm -rf target/selenium-reports/odeme-create || true'

                sh 'mvn -Pselenium -Dtest=OdemeCreateScenarioTest test'

                sh '''
                    set -e
                    mkdir -p target/selenium-reports/odeme-create
                    echo "--- default surefire reports (selenium odeme create) ---"
                    ls -la target/surefire-reports || true
                    echo "--- copy selenium odeme create xml ---"
                    cp -v target/surefire-reports/*.xml target/selenium-reports/odeme-create/ 2>/dev/null || true
                    echo "--- selenium odeme create reports ---"
                    ls -la target/selenium-reports/odeme-create || true
                    find target/selenium-reports/odeme-create -maxdepth 1 -name "*.xml" -print || true
                '''
            }
            post {
                always {
                    junit testResults: '**/target/selenium-reports/odeme-create/*.xml'
                }
            }
        }

        stage('6.9) Selenium - Scenario 9 (Yemek Create)') {
            steps {
                sh 'rm -rf target/surefire-reports || true'
                sh 'rm -rf target/selenium-reports/yemek-create || true'

                sh 'mvn -Pselenium -Dtest=YemekCreateScenarioTest test'

                sh '''
                    set -e
                    mkdir -p target/selenium-reports/yemek-create
                    echo "--- default surefire reports (selenium yemek create) ---"
                    ls -la target/surefire-reports || true
                    echo "--- copy selenium yemek create xml ---"
                    cp -v target/surefire-reports/*.xml target/selenium-reports/yemek-create/ 2>/dev/null || true
                    echo "--- selenium yemek create reports ---"
                    ls -la target/selenium-reports/yemek-create || true
                    find target/selenium-reports/yemek-create -maxdepth 1 -name "*.xml" -print || true
                '''
            }
            post {
                always {
                    junit testResults: '**/target/selenium-reports/yemek-create/*.xml'
                }
            }
        }

        stage('6.10) Selenium - Scenario 10 (Icecek Create)') {
            steps {
                sh 'rm -rf target/surefire-reports || true'
                sh 'rm -rf target/selenium-reports/icecek-create || true'

                sh 'mvn -Pselenium -Dtest=IcecekCreateScenarioTest test'

                sh '''
                    set -e
                    mkdir -p target/selenium-reports/icecek-create
                    echo "--- default surefire reports (selenium icecek create) ---"
                    ls -la target/surefire-reports || true
                    echo "--- copy selenium icecek create xml ---"
                    cp -v target/surefire-reports/*.xml target/selenium-reports/icecek-create/ 2>/dev/null || true
                    echo "--- selenium icecek create reports ---"
                    ls -la target/selenium-reports/icecek-create || true
                    find target/selenium-reports/icecek-create -maxdepth 1 -name "*.xml" -print || true
                '''
            }
            post {
                always {
                    junit testResults: '**/target/selenium-reports/icecek-create/*.xml'
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
