# Jenkins Pipeline to implement Vansah-Demo



This Jenkins pipeline is configured to build and test a Maven-based Java project, integrating with Vansah Connect for managing test results. It provides a comprehensive CI/CD workflow, incorporating **Jenkins, Maven, NodeJS, TestNG, Vansah Connect CLI, and the Vansah Test Management app**.
 
 The following key stages are executed:

1. **Build and test the Maven project**.
2. **Install the [@vansah/vansah-connect](https://www.npmjs.com/package/@vansah/vansah-connect) npm package**.
3. **Fetch the Vansah API token from Jenkins credentials**.
4. **Upload TestNG results to Vansah**.
## Prerequisites

### 1. Vansah Test Management Plugin for JIRA
- Ensure the Vansah Test Mangement app is installed in your JIRA workspace.
- Set up your JIRA project and with an Issue and two testcases. Note the **IssueKey / FolderID** and  **two corresponding TestcaseKeys**
  ![Vansah_project](https://github.com/testpointcorp/connect-images/blob/main/JenkinsPipelineForVansahAutomationDemo/Jira_vansahProject.png)
### 2. Jenkins Setup
   - **[Jenkins](https://www.jenkins.io/doc/pipeline/tour/getting-started/)** must be up and running
   - Add the following plugins to your Jenkins by **Jenkins Dashboard > Manage Jenkins > Plugins > Available Plugins**
   
     - **Maven Plugin**: For building Maven projects in Jenkins.
     - **NodeJS Plugin**: For managing Node.js and npm installations in Jenkins.
     - **GitHub Plugins**: For handling projects in GitHub repository.

### 3. **Repository Setup**:
  The `Vansah Automation Demo` project contains two testcases (one **positive** and one **negative**) that verifies the Test website https://selenium.vansah.io.

- Clone [Vansah Automation Demo](https://github.com/testpointcorp/vansahSeleniumJavaDemo/tree/jenkins-job) to your repository.
  Update the `@CustomAttributes`according to your Vansah board in

  - Update `JIRA_ISSUE_KEY` with your **JIRA ISSUE** [BaseTests.java](https://github.com/testpointcorp/vansahSeleniumJavaDemo/blob/jenkins-job/src/test/java/testpack/BaseTests.java) 
    
   
    ```Java
 
    public class BaseTests {

	public WebDriver driver;
	public VansahNode results;
	public static final String TestUrl = "https://selenium.vansah.io";
	public static final String JIRA_ISSUE_KEY = "AS-7";
	public static final String sprintName = ""; //Optional
	public static final String releaseName = "Release 24"; //Optional
	public static final String environment = "UAT";  //Optional
	
	/*
	
	 
	 Test logic
	
	*/

    ```
        


   - [VansahIOTests.java](https://github.com/testpointcorp/vansahSeleniumJavaDemo/blob/jenkins-job/src/test/java/testpack/VansahIOTests.java)
    ```Java
   
   
              public class VansahIOTests extends BaseTests{

                  @Test(groups = { "regression" },attributes = {
			                     @CustomAttribute(name = "Case Key", values = "AS-C15"),
		                      @CustomAttribute(name = "Tested Issue", values = JIRA_ISSUE_KEY),
		                      @CustomAttribute(name = "Tested Sprint", values = sprintName ),
		                      @CustomAttribute(name = "Tested Environment", values = environment)})
		      
                       	/* 
    	
                             Test Logic
    	
         	               */
    	
                   @Test(groups = { "regression" },attributes = {
			                      @CustomAttribute(name = "Case Key", values = "AS-C16"),
		                       @CustomAttribute(name = "Tested Issue", values = JIRA_ISSUE_KEY),
		                       @CustomAttribute(name = "Tested Sprint", values = sprintName ),
		                       @CustomAttribute(name = "Tested Environment", values = environment)})
		    
		    
		                       /*
		  
		                             Test Logic
		  
		                        */
		          }    
   ```
  
## Jenkins Configuration

### Jenkins Plugins

Ensure the following Jenkins Tools are installed in **Jenkins Dashboard > Manage Jenkins > Tools** :
- **Maven Installations**: For building Maven projects. Add a name (E.g: maven1)
  ![maven_installations](https://github.com/testpointcorp/connect-images/blob/main/JenkinsPipelineForVansahAutomationDemo/Tools_Maven.png)
- **NodeJS Installations**: For managing Node.js and npm installations. Add a name (E.g.: Node1)
  ![nodeJS_installation](https://github.com/testpointcorp/connect-images/blob/main/JenkinsPipelineForVansahAutomationDemo/Tools_nodejs.png)
- **Git Installations**
![Git_Installation](https://github.com/testpointcorp/connect-images/blob/main/JenkinsPipelineForVansahAutomationDemo/Tools_git.png)

### Credentials

 **Add Vansah API Token** as Credentials.
 
  [Click to know how to get Vansah API Token](https://community.vansah.com/posts/how-to-generate-a-vansah-api-token-from-jira)
   - Go to **Jenkins Dashboard > Manage Jenkins > Manage Credentials > System > Global credentials (unrestricted) > Add Credentials**.
   - Add a new `Secret text`  credential with your Vansah API token and
     
     provide an **ID** name (E.g. 'VANSAH_API_TOKEN').
   - Use the ID for this credential in the pipeline script.
     ![Add_credential](https://github.com/testpointcorp/connect-images/blob/main/JenkinsPipelineForVansahAutomationDemo/Add_credentials.png)
   
## How to Use

1. **Create a New Pipeline Job**:
   - Go to the Jenkins dashboard.
   - Click on **New Item**.
   -  Provide a name and choose **Pipeline project**.
     ![NewJenkinsItem](https://github.com/testpointcorp/connect-images/blob/main/JenkinsPipelineForVansahAutomationDemo/New_JenkinsItems.png)

2. **Configure Pipeline**:
   - In the pipeline configuration, choose **Pipeline script** and paste the provided pipeline script into the script area. 
   - Make sure to update the **Git URL** as per your repository.

##### Pipeline Script


```groovy
pipeline {
    agent any  // Use any available agent

    tools {
        // Specify Maven and Node.js versions
        maven 'maven1'  // Ensure this version is configured in Jenkins Global Tool Configuration
        nodejs 'Node1'
    }

    stages {
        stage('Checkout') {
            steps {
                // Clone Your Git repository
                git url: 'https://github.com/MerinArangassery/vansahSeleniumJavaDemo.git', branch: 'jenkins-job'
            }
        }

        stage('Build and Test') {
            steps {
                script {
                    try {
                        // Run Maven build
                        bat 'mvn clean install'     //Linux: sh 'mvn clean install'
                        
                        // Run Maven tests if the build succeeds
                        bat 'mvn test'            //Linux: sh 'mvn test'
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
                // Install the vansah-connect npm package globally
                bat 'npm i -g @vansah/vansah-connect'        //Linux:  sh 'npm i -g @vansah/vansah-connect'

                // Fetch Vansah API Token from Jenkins credentials securely
                withCredentials([string(credentialsId: 'VANSAH_API_TOKEN', variable: 'MY_API_TOKEN')]) {
                    // Configure vansah-connect with the API token
                    bat 'vansah-connect -c %MY_API_TOKEN%'     //Linux sh 'vansah-connect -c %MY_API_TOKEN%'
                    
                    // Upload test results to Vansah
                    bat 'vansah-connect -f ./target/surefire-reports/testng-results.xml'    //Linux sh 'vansah-connect -f ./target/surefire-reports/testng-results.xml'
                }
            }
        }
    }
}
```

3. **Save and Build**:
   - Save the configuration and build the pipeline to execute the script.
![PipelineScript](https://github.com/testpointcorp/connect-images/blob/main/JenkinsPipelineForVansahAutomationDemo/PipelineScript.png)

## Post-Build Actions
The post-build actions involve installing the Vansah CLI tool, securely handling the API token for connection, and uploading test results to Vansah using the specified command and file path.

  
   ```shell
   [Pipeline] bat

C:\ProgramData\Jenkins\.jenkins\workspace\TrialProjectVansahDemo>npm i -g @vansah/vansah-connect 

changed 102 packages in 14s

20 packages are looking for funding
  run `npm fund` for details
[Pipeline] withCredentials
Masking supported pattern matches of %MY_API_TOKEN%
[Pipeline] {
[Pipeline] bat

C:\ProgramData\Jenkins\.jenkins\workspace\TrialProjectVansahDemo>vansah-connect -c **** 
TOKEN has been saved successfully
[Pipeline] bat

C:\ProgramData\Jenkins\.jenkins\workspace\TrialProjectVansahDemo>vansah-connect -f ./target/surefire-reports/testng-results.xml 
- ...
- Uploading Results to Vansah...
✔ Results import is completed.
[Pipeline] }

  ```
## Notes

- Due to the negative test case, the Jenkins job will **fail**, but it will upload the results as ‘PASSED’ for one and ‘FAILED’ for the other. 
- Ensure that the file path to the **testng-results.xml** in `vansah-connect -f` command is correct and matches the actual location of your test results.
- Double-check that the `VANSAH_API_TOKEN` credential ID matches the ID used in the Jenkins pipeline script.



