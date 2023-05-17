package testcases;

import org.testng.annotations.Test;

import utility.VansahNode;

import org.testng.annotations.BeforeTest;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
public class AddTestRunFromJiraIssueScreen {

	//Required
	private static WebDriver driver ;

	//Required
	private VansahNode testExecute;

	//Optional When IssueKey is being used for Test Run
	private static String testFolderID = null; 

	//Optional When TestFolder is being used for Test Run
	private static String issueKey = "DEMO-1";

	//Optional 
	private static String sprintName = null; 

	//Optional
	private static String releaseName = null; 

	//Optional Environment Name to which test execution of a test case is to be perform
	private static String environment = "UAT";

	//Required
	private static String testCase = "DEMO-C1";



	@BeforeTest
	public void beforeTest() {
		System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"//driver//chromedriver.exe");	
	}

	
	@Test
	public void addTestRunFromJiraIssue() throws Exception {
		//ChromeOptions to fix Invalid Status code=403
		ChromeOptions options = new ChromeOptions();
		options.addArguments("start-maximized","--remote-allow-origins=*");
		//To create new Instance of Chrome
		driver = new ChromeDriver(options);
		
		//Provide JIRA Issue
		testExecute = new VansahNode();
		//Set Jira Issue
		testExecute.setJIRA_ISSUE_KEY(issueKey);
		//Set Environment 
		testExecute.setENVIRONMENT_NAME(environment);
		
		//Test Case Execution started 
		//Creating Test Run Identifer
		//Running Test Case for an Issue
		testExecute.addTestRunFromJIRAIssue(testCase);
		
		driver.get("https://vansah.com");
		


		//testExecute test step #1 , User should be able to open the vansah.com
		System.out.println(driver.getCurrentUrl());
		if(driver.getCurrentUrl().equals("https://vansah.com/")) {

			//0 = N/A, 1 = FAILED, 2 = PASSED, 3 = UNTESTED
			//Add logs for each step function(ResultID, ActualResultComment, TestStepID, screenshotTrueorFalse, chromedriver/OtherBroswerdriver);
			testExecute.addTestLog(Result.PASSED.id, "As expected, Url is opened", TestStep.Step_1.number, Screenshot.TRUE.takeScreenshot, driver);
			
		}
		else {

			//0 = N/A, 1 = FAILED, 2 = PASSED, 3 = UNTESTED
			//Add logs for each step function(ResultID, ActualResultComment, TestStepID, screenshotTrueorFalse, chromedriver/OtherBroswerdriver);
			testExecute.addTestLog(Result.FAILED.id, "URL not Found",TestStep.Step_1.number, Screenshot.TRUE.takeScreenshot, driver);
		}


		System.out.println(driver.getTitle());


		//testExecute test step #2 , Title of the page should matched with "Vansah | Jira Test Management App"
		if(driver.getTitle().equals("Vansah | Jira Test Management App")) {

			//0 = N/A, 1 = FAILED, 2 = PASSED, 3 = UNTESTED
			//Add logs for each step function(ResultID, ActualResultComment, TestStepID, screenshotTrueorFalse, chromedriver/OtherBroswerdriver);
			testExecute.addTestLog(Result.PASSED.id, "As expected, title is matched", TestStep.Step_2.number, Screenshot.TRUE.takeScreenshot, driver);

		}
		else {
			//Add logs for each step function(ResultID, ActualResultComment, TestStepID, screenshotTrueorFalse, chromedriver/OtherBroswerdriver);
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


		if(driver.getCurrentUrl().equals("https://marketplace.atlassian.com/apps/1224250/vansah-test-management-for-jira?tab=overview&hosting=cloud")) {

			//0 = N/A, 1 = FAILED, 2 = PASSED, 3 = UNTESTED
			//Add logs for each step function(ResultID, ActualResultComment, TestStepID, screenshotTrueorFalse, chromedriver/OtherBroswerdriver);
			testExecute.addTestLog(Result.PASSED.id, "As expected, url is matched", TestStep.Step_3.number, Screenshot.TRUE.takeScreenshot, driver);

		}
		else {

			//0 = N/A, 1 = FAILED, 2 = PASSED, 3 = UNTESTED
			//Add logs for each step function(ResultID, ActualResultComment, TestStepID, screenshotTrueorFalse, chromedriver/OtherBroswerdriver);
			testExecute.addTestLog(Result.FAILED.id, "Url is not matched", TestStep.Step_3.number, Screenshot.TRUE.takeScreenshot, driver);
		}
		driver.quit();  //Closing the current driver session
	}


}
