package testpack;

import java.io.File;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import io.github.bonigarcia.wdm.WebDriverManager;
import utility.VansahNode;

public class BaseTests {

	public WebDriver driver;
	public VansahNode results;
	public static final String TestUrl = "https://selenium.vansah.io";
	public static final String JIRA_ISSUE_KEY = "TEST-7"; //Required
	public static final String sprintName = "SM Sprint 1"; //Optional
	public static final String releaseName = "Release 24"; //Optional
	public static final String environment = "UAT";  //Optional

	@BeforeMethod(alwaysRun = true)
	@SuppressWarnings({ "static-access" })
	public void setUp() throws Exception {

		ChromeOptions options = new ChromeOptions();
		options.addArguments("start-maximized","--remote-allow-origins=*");
		options.addArguments("--headless"); // Run in headless mode
		options.addArguments("--disable-gpu"); // Applicable to Windows environments
		options.addArguments("--window-size=1920,1080"); // Set window size if needed
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
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
		driver.get(TestUrl);

	}     


	@AfterMethod(alwaysRun = true)
	public void tearDown() throws Exception {
		if (driver != null) {
			driver.quit();

		}
	}

}
