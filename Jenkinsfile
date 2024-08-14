pipeline {
    agent any  // Use any available agent
     
    tools {
        // Specify Maven version
        maven 'maven1'  // Ensure this version is configured in Jenkins Global Tool Configuration
        nodejs 'Node1'
        
    }

    stages {
        stage('Checkout') {
            steps {
                // Clone the Git repository
                git url: 'https://github.com/MerinArangassery/vansahSeleniumJavaDemo.git', branch: 'jenkins-job'
            }
        }

       stage('Build and Test') {
            steps {
                script {
                    try {
                        // Run Maven build
                        bat 'mvn clean install'
                        
                        // Run Maven tests if the build succeeds
                        bat 'mvn test'
                    } catch (Exception e) {
                        echo "Build or test failed: ${e.getMessage()}"
                        currentBuild.result = 'FAILURE'
                        throw e
                    }
                }
            }
        }
    }


    post {
        always {
            script {
                
                //installing vansah-connect npm package
                    bat 'npm i -g @vansah/vansah-connect'
                
                //script to fetch Vansah API Token from credentials securely
                withCredentials([string(credentialsId: 'VANSAH_API_TOKEN', variable: 'MY_API_TOKEN')]) {
                    bat 'vansah-connect -c %MY_API_TOKEN%'
                    //uploading results to vansah
                     bat 'vansah-connect -f ./target/surefire-reports/testng-results.xml'
                }

                
            }
        }
    }

}