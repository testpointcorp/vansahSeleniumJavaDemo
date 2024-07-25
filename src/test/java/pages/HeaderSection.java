package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class HeaderSection {

	WebDriver driver;
	WebElement element;

	By logoAltText = By.xpath("//*[@class='xs-logo']/img");

	public HeaderSection(WebDriver currentDriver) {
		this.driver = currentDriver;
	}

	public void verifylogoAltText(String exepectedText) {
		element = driver.findElement(logoAltText);
		String currentText = element.getAttribute("alt");
		try {
			Assert.assertEquals(currentText, exepectedText);
		}catch(AssertionError e) {
			System.out.println(e.getMessage());
			Assert.fail();
		}
	}
}
