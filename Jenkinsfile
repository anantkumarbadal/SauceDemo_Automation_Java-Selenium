pipeline {
    agent any

    environment {
        MAVEN_HOME = tool 'Maven' // Make sure Jenkins has a Maven tool named 'Maven'
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout your repository
                git url: 'https://github.com/anantkumarbadal/SauceDemo_Automation_Java-Selenium.git', branch: 'main'
            }
        }

        stage('Build') {
            steps {
                // Build the project using Maven
                bat "${MAVEN_HOME}/bin/mvn clean compile"
            }
        }

        stage('Test') {
            steps {
                // Run TestNG tests
                bat "${MAVEN_HOME}/bin/mvn test"
            }
        }

        stage('Publish TestNG Results') {
            steps {
                // Archive TestNG results in Jenkins
                junit '**/test-output/testng-results.xml'
            }
        }
    }

    post {
        always {
            // Archive all reports and logs regardless of build result
            archiveArtifacts artifacts: '**/test-output/**/*.*', allowEmptyArchive: true
        }
        failure {
            echo "Build failed. Check logs and reports for details."
        }
    }
}