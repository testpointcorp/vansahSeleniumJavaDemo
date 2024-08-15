## Vansah Automation Demo

This Selenium TestNG Automation framework is just a demo and quick start to Automated Testing with Vansah - Test Management for Jira.

## Vansah's APIs


Vansah's APIs enable the integration of Vansah Test Management App with various tools, automation frameworks, and third-party applications. Its primary purpose is to allow customers to integrate automated tests and submit test results to Vansah. For a comprehensive list of other tasks you can perform with the API, please refer to this [documentation](https://apidoc.vansah.com/).


## Tools
<ul>
  <li>Vansah Test Management App for JIRA</li>
  <li>Maven - Build Tool</li>
  <li>TestNG</li>
  <li>Selenium 3</li>
  <li>ChromeDriver v107.x.x.x</li>
</ul>

## Prerequisites
  - Ensure the **[Vansah Test Mangement App](https://marketplace.atlassian.com/apps/1224250/vansah-test-management-for-jira?hosting=cloud&tab=overview)** is installed in your JIRA workspace.
- Set up your JIRA project and with an Issue and two testcases. Note the **IssueKey / FolderID** and  **two corresponding TestcaseKeys**
  ![Vansah_project](https://github.com/testpointcorp/connect-images/blob/main/JenkinsPipelineForVansahAutomationDemo/Jira_vansahProject.png)
- Setup **Vansah API Token**
       
  - Add Vansah API Connect Token as **System Environment Variable** `VANSAH_TOKEN`
    This token should be obtained from your Jira Workspace
    
    <a href="https://community.vansah.com/posts/how-to-generate-a-vansah-api-token-from-jira">Click to Know How to obtain your Vansah API Connect Token</a>


## How to Run

  This project contains two testcases (one **positive** and one **negative**) that verifies the Test website https://selenium.vansah.io.

**Step 1:** Clone [Vansah Automation Demo](https://github.com/testpointcorp/vansahSeleniumJavaDemo/tree/jenkins-job) to your repository.

**Step 2:** Open Eclipse and Import the Project as Maven Project.

**Step 3:** Update `JIRA_ISSUE_KEY` with your **JIRA ISSUE** in  [BaseTests.java](https://github.com/testpointcorp/vansahSeleniumJavaDemo/blob/jenkins-job/src/test/java/testpack/BaseTests.java) 
    
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
        


**Step 4:**  Update the `@CustomAttributes`according to your Vansah board in [VansahIOTests.java](https://github.com/testpointcorp/vansahSeleniumJavaDemo/blob/jenkins-job/src/test/java/testpack/VansahIOTests.java) 
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
      ```
    




**Step 5:** Run as Maven test
    

## Running as a Jenkins Pipline
To execute this project as a Jenkins Pipeline, follow the steps outlined [here](https://github.com/testpointcorp/vansahSeleniumJavaDemo/blob/jenkins-job/Jenkins_Pipeline.md). 


    

