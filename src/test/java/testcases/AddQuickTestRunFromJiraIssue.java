package testcases;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utility.VansahNode;

public class AddQuickTestRunFromJiraIssue extends AddTestRunFromJiraIssueScreen{
	
		//Required
		private static WebDriver driver ; 

		//Required
		private VansahNode testExecute;

		//Optional if IssueKey is provided
		private static String testFolderID = null; //TestFolder ID to which test Execution is to be perform
		
		//Required
		private static String testCase = "DEMO-C3";

		//Optional
		private static String environment = "UAT"; //Environment Name to which test execution of a test case is to be perform
		

		//Optional if TestFolder ID is provided
		private static String issueKey = "DEMO-1"; //IssueKey to which test Execution is to be perform
				
		//Optional 
		private static String sprintName = null; //Sprint Name for current sprint for which test execution is to be perform

		//Optional
		private static String releaseName = null; //Release Name linked with the current sprint and to the test case.
		
		
		@Test
		public void addQuickTestFromJiraIssue() throws Exception {
			//ChromeOptions to fix Invalid Status code=403
			ChromeOptions options = new ChromeOptions();
			options.addArguments("start-maximized","--remote-allow-origins=*");
			//To create new Instance of Chrome
			driver = new ChromeDriver(options);
			
			//Provide TestFolder ID
			testExecute = new VansahNode();
			
			//Set TestFolder ID
			testExecute.setJIRA_ISSUE_KEY(issueKey);
			
			//Set Environment 
			testExecute.setENVIRONMENT_NAME(environment);
			
			//Test Case started 
		
			driver.get("https://vansah.com");
			

			//testExecute test step #1 , User should be able to open the vansah.com
			System.out.println(driver.getCurrentUrl());
			if(driver.getCurrentUrl().equals("https://vansah.com/")) {

				//0 = N/A, 1 = FAILED, 2 = PASSED, 3 = UNTESTED
				//Add Quick test for a test case(Test Case Key,ResultID);
				testExecute.addQuickTestFromJiraIssue(testCase, Result.PASSED.id);
							
			}
			else {

				//0 = N/A, 1 = FAILED, 2 = PASSED, 3 = UNTESTED
				//Add Quick test for a test case(Test Case Key,ResultID);
				testExecute.addQuickTestFromJiraIssue(testCase, Result.FAILED.id);
	
			}


			driver.quit();  //Closing the current driver session
			
		}
	
	
	
	
	
	

}
