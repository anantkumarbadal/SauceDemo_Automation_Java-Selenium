pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/anantkumarbadal/SauceDemo_Automation_Java-Selenium.git', branch: 'main'
            }
        }
        stage('Build') {
            steps {
                bat 'mvn clean compile'  // If using Maven
            }
        }
        stage('Test') {
            steps {
                bat 'mvn test'  // Or use Gradle/other commands as needed
            }
        }
        stage('Report') {
            steps {
                // Archive test reports or publish results
                junit '**/target/surefire-reports/*.xml'
            }
        }
    }
}