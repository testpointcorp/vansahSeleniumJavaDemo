package com.testpoint.tests;

import org.testng.annotations.Test;
import com.testpoint.vansah.VansahNode;
import org.testng.annotations.BeforeTest;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;

enum Result {

	PASSED(2,"PASSED"),
	FAILED(1,"FAILED");
	final int id;
	final String name;
	Result(int id, String name) {
		this.id = id;
		this.name = name;	
	}

}
enum TestStep{
	
	Step_1(1),
	Step_2(2),
	Step_3(3);
	final int number;
	TestStep(int number){
		this.number = number;
		
	}
	
	
	
	
}
enum Screenshot {

	TRUE(true),FALSE(false);
	boolean takeScreenshot;
	Screenshot(boolean takeScreenshot){
		this.takeScreenshot = takeScreenshot;
	}


}
public class TestCases {

	//Required
	private static WebDriver driver ;  // Required if screenshot is needed

	//Required
	private VansahNode testExecute;

	//Optional if IssueKey is provided
	private static String testFolderID = "1ba1372f-54ed-11ed-8e52-5658ef8eadd5"; //TestFolder ID to which test Execution is to be perform

	//Optional if TestFolder ID is provided
	private static String issueKey = "VAN-1"; //IssueKey to which test Execution is to be perform

	//Optional 
	private static String sprintName = "VAN Sprint 1"; //Sprint Name for current sprint for which test execution is to be perform

	//Optional
	private static String releaseName = "v1"; //Release Name linked with the current sprint and to the test case.

	//Optional
	private static String environment = "SYS"; //Environment Name to which test execution of a test case is to be perform

	//Required
	private static String testCase = "VAN-C3";



	@BeforeTest
	public void beforeTest() {
		System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"//driver//chromedriver.exe");

		driver = new ChromeDriver();
		//Provide TestFolder ID , JIRA Issue, Sprint Key, Sprint Release and Environment
		testExecute = new VansahNode(testFolderID,issueKey,sprintName,releaseName,environment);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void TestCase_01() throws Exception {

		//Test Case started 
		//Creating Test Run Identifer
		//Running Test Case for an Issue
		testExecute.addTestRunFromJIRAIssue(testCase);
		//System.out.println("Test Steps Count " + testExecute.testStepCount(testCase));
		driver.get("https://vansah.com");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

		//System.out.print(Screenshot.TRUE.takeScreenshot);

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




		// Store the current window handle
		@SuppressWarnings("unused")
		String winHandleBefore = driver.getWindowHandle();

		//Clicking on TRY NOW button
		WebElement element = driver.findElement(By.xpath("//*[@id=\"slider-7-slide-8-layer-10\"]"));
		element.click();

		// Perform the click operation that opens new window

		// Switch to new window opened
		for(String winHandle : driver.getWindowHandles()){
			driver.switchTo().window(winHandle);
		}

		// Perform the actions on new window

		// Continue with original browser (first window)

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
	}



	@AfterTest
	public void afterTest() {

		driver.quit();
	}

}
