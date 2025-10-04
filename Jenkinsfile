pipeline {
    agent any

    environment {
        MAVEN_HOME = tool 'Maven'
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/anantkumarbadal/SauceDemo_Automation_Java-Selenium.git', branch: 'main'
            }
        }
        stage('Build') {
            steps {
                bat "${MAVEN_HOME}\\bin\\mvn clean compile"
            }
        }
        stage('Test') {
            steps {
                bat "${MAVEN_HOME}\\bin\\mvn test"
            }
        }
        stage('Report') {
            steps {
                junit '**/test-output/testng-results.xml'
            }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: '**/test-output/**/*.*', allowEmptyArchive: true
        }
        failure {
            echo "Build failed. Check logs and reports for details."
        }
    }
}