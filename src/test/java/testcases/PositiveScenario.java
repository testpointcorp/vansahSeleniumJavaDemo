package testcases;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.CustomAttribute;
import org.testng.annotations.Test;

public class PositiveScenario extends Tests {

	/* Test Case Headline: Clicking "Try Now" Button Opens Jira Marketplace
	 * 
	 * Test Pass Criteria: 
	 * The Jira Marketplace page opens successfully upon clicking the "Try Now" button.
	 * The user can browse and explore the Jira Marketplace without any errors or interruptions.
	 */
	private String TestCaseKey = "TEST-C1";    //Required 

	@Test(attributes = {
			 @CustomAttribute(name = "Case Key", values = "TSTNG-C2"),
		      @CustomAttribute(name = "Tested Issue", values = "TSTNG-1"),
		      @CustomAttribute(name = "Tested Sprint", values = "SM Sprint 1"),
		      @CustomAttribute(name = "Tested Environment", values = "SYS")})
	public void Positive_Test_TryNow_Button() throws Exception{
		
		//A New Test Run is being created 
		results.addTestRunFromJIRAIssue(TestCaseKey);

		try {
			driver.get(TestUrl);//Launch the web browser and opens the "TestUrl"

			results.addTestLog("PASSED", "As expected, Url is opened", 1, true, driver);

		}catch(Exception e) {
			results.updateTestLog("FAILED", e.getMessage(), true, driver);
			throw e;
		}

		
		try {
			WebElement TryNow_Button = driver.findElement(By.id("vansah-trynow"));  // Locate the "Try Now" button on the webpage.

			TryNow_Button.click(); //Click on the "Try Now" button
			
			Duration duration = Duration.ofSeconds(40);
			WebDriverWait wait = new WebDriverWait(driver,duration);
			wait.until(ExpectedConditions.urlToBe("https://marketplace.atlassian.com/apps/1224250/vansah-test-management-for-jira?tab=overview&hosting=cloud"));
			
			results.addTestLog("PASSED", "As expected User is directed to the Jira Marketplace landing page", 2, true, driver);

		}catch(Exception e){
			results.updateTestLog("FAILED", e.getMessage(), true, driver);
			throw e;
		}

	}
}
