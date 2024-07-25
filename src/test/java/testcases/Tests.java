package testcases;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.Augmenter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import io.github.bonigarcia.wdm.WebDriverManager;
import utility.VansahNode;

public class Tests {
	
	 public WebDriver driver;
	 public VansahNode results;
	 public String TestUrl = "https://selenium.vansah.io";
	 public String JIRA_ISSUE_KEY = "TEST-1"; //Required
	 public String sprintName = "SM Sprint 1"; //Optional
	 public String releaseName = "Release 24"; //Optional
	 public String environment = "UAT";  //Optional

	    @BeforeMethod(alwaysRun = true)
	    @SuppressWarnings({ "unchecked", "static-access" })
	    public void setUp() throws Exception {
	    	
	        ChromeOptions options = new ChromeOptions();
	        options.addArguments("start-maximized","--remote-allow-origins=*");
	        WebDriverManager.chromedriver().setup();
	        
	        /* This code snippet retrieves an authentication token required for interacting with the Vansah platform.
	         * The authentication token is fetched from environment variables, ensuring security and flexibility.
	         * Once obtained, this token will be utilized to securely send results or data to the Vansah platform.
	         * */
	        results.setVansahToken(System.getenv("VANSAH_TOKEN")); 
	        
	        results = new VansahNode(); 
	        /*Vansah Test Run Properties
			 * */
			results.setJIRA_ISSUE_KEY(JIRA_ISSUE_KEY);
			results.setSPRINT_NAME(sprintName);
			results.setRELEASE_NAME(releaseName);
			results.setENVIRONMENT_NAME(environment);
			
	        driver = new ChromeDriver(options);
	    }
	    
	    public File takess() {
	    	File screenshotFile = null;
	    	try {
	             screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	            FileUtils.copyFile(screenshotFile, new File("screenshot.png"));
	            System.out.println("Screenshot saved successfully.");
	        } catch (Exception e) {
	            System.out.println("Failed to take screenshot: " + e.getMessage());
	        }
	    	//return null;
	    	return screenshotFile;
	    	//return	new File("C:\\Users\\onesh\\OneDrive\\Documents\\GitHub\\vansahSeleniumJavaDemo\\screenshot2132312312312321312.png");
	    }

	    @AfterMethod(alwaysRun = true)
	    public void tearDown() throws Exception {
	    	driver.quit();
	    }

}
