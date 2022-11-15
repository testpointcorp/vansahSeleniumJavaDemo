package testcases;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import utility.VansahNode;

public class AddTestRunFromTestFolderScreen extends AddTestRunFromJiraIssueScreen{
	
		//Required
		private static WebDriver driver ; 

		//Required
		private VansahNode testExecute;

		//Optional if IssueKey is provided
		private static String testFolderID = "cb1a7eb2-6499-11ed-8e52-5658ef8eadd5"; //TestFolder ID to which test Execution is to be perform
		
		//Required
		private static String testCase = "DEMO-C2";

		//Optional
		private static String environment = "UAT"; //Environment Name to which test execution of a test case is to be perform
		

		//Optional if TestFolder ID is provided
		private static String issueKey = "DEMO-1"; //IssueKey to which test Execution is to be perform
				
		//Optional 
		private static String sprintName = null; //Sprint Name for current sprint for which test execution is to be perform

		//Optional
		private static String releaseName = null; //Release Name linked with the current sprint and to the test case.
		
		@Test
		public void addTestRunFromTestFolder() throws Exception {
			
			//To create new Instance of Chrome
			driver = new ChromeDriver();
			
			//Provide TestFolder ID , JIRA Issue, Sprint Key, Sprint Release and Environment
			testExecute = new VansahNode(testFolderID,issueKey);
			
			//Set Environment 
			testExecute.setENVIRONMENT_NAME(environment);
			
			//Test Case started 
			//Creating Test Run Identifer
			//Running Test Case for an Issue
			testExecute.addTestRunFromJIRAIssue(testCase);
		
			driver.get("https://vansah.com");
			driver.manage().window().maximize();

			//testExecute test step #1 , User should be able to open the vansah.com
			System.out.println(driver.getCurrentUrl());
			if(driver.getCurrentUrl().equals("https://vansah.com//")) {

				//0 = N/A, 1 = FAILED, 2 = PASSED, 3 = UNTESTED
				//Add logs for each step function(ResultID, AcutalResultComment, TestStepID, screenshotTrueorFalse, chromedriver/OtherBroswerdriver);
				testExecute.addTestLog(Result.PASSED.id, "As expected, Url is opened", TestStep.Step_1.number, Screenshot.TRUE.takeScreenshot, driver);
				
			}
			else {

				//0 = N/A, 1 = FAILED, 2 = PASSED, 3 = UNTESTED
				//Add logs for each step function(ResultID, AcutalResultComment, TestStepID, screenshotTrueorFalse, chromedriver/OtherBroswerdriver);
				testExecute.addTestLog(Result.FAILED.id, "URL not Found",TestStep.Step_1.number, Screenshot.TRUE.takeScreenshot, driver);
			}


			System.out.println(driver.getTitle());


			//testExecute test step #2 , Title of the page should matched with "Vansah | Jira Test Management App"
			if(driver.getTitle().equals("Vansah | Jira Test Management App")) {

				//0 = N/A, 1 = FAILED, 2 = PASSED, 3 = UNTESTED
				//Add logs for each step function(ResultID, AcutalResultComment, TestStepID, screenshotTrueorFalse, chromedriver/OtherBroswerdriver);
				testExecute.addTestLog(Result.PASSED.id, "As expected, title is matched", TestStep.Step_2.number, Screenshot.TRUE.takeScreenshot, driver);

			}
			else {
				//Add logs for each step function(ResultID, AcutalResultComment, TestStepID, screenshotTrueorFalse, chromedriver/OtherBroswerdriver);
				testExecute.addTestLog(Result.FAILED.id, "Title is not matched", TestStep.Step_2.number, Screenshot.TRUE.takeScreenshot, driver);
			}

			//Clicking on TRY NOW button
			WebElement element = driver.findElement(By.xpath("//*[@id=\"slider-7-slide-8-layer-10\"]"));
			element.click();

			// Perform the click operation that opens new window

			// Switch to new window opened
			for(String winHandle : driver.getWindowHandles()){
				driver.switchTo().window(winHandle);
			}

			// Perform the actions on new window
			//testExecute test step #3 , URL of new window"
			Duration duration = Duration.ofSeconds(40);
			WebDriverWait wait = new WebDriverWait(driver,duration);
			wait.until(ExpectedConditions.urlToBe("https://marketplace.atlassian.com/apps/1224250/vansah-test-management-for-jira?tab=overview&hosting=cloud"));


			if(driver.getCurrentUrl().equals("https://marketplace.atlassian.com/apps/1224250/vansah-test-management-for-jira?tab=overview&hosting=cloud/")) {

				//0 = N/A, 1 = FAILED, 2 = PASSED, 3 = UNTESTED
				//Add logs for each step function(ResultID, AcutalResultComment, TestStepID, screenshotTrueorFalse, chromedriver/OtherBroswerdriver);
				testExecute.addTestLog(Result.PASSED.id, "As expected, url is matched", TestStep.Step_3.number, Screenshot.TRUE.takeScreenshot, driver);

			}
			else {

				//0 = N/A, 1 = FAILED, 2 = PASSED, 3 = UNTESTED
				//Add logs for each step function(ResultID, AcutalResultComment, TestStepID, screenshotTrueorFalse, chromedriver/OtherBroswerdriver);
				testExecute.addTestLog(Result.FAILED.id, "Url is not matched", TestStep.Step_3.number, Screenshot.TRUE.takeScreenshot, driver);
			}
			
			driver.quit();  //Closing the current driver session
			
		}
	
	
	
	
	
	

}
