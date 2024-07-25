package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

public class FooterSection {

	WebDriver driver;
	WebElement element;
	By footerSection = By.className("xs-footer-section");
	By copyRightText = By.className("copyright-text"); //© 2022 Vansah®. All rights reserved. 

	public FooterSection(WebDriver currentDriver) {
		this.driver = currentDriver;
	}

	public void scrollToFooter() {

		element = driver.findElement(footerSection);

		// Use Actions class to scroll to the element
		Actions actions = new Actions(driver);
		actions.moveToElement(element);
		actions.perform();

	}
	public void verifyCopyRightText(String exepectedText) {

		element = driver.findElement(copyRightText);
		String[] currentText = element.getText().split(".\n");		
		try {
			Assert.assertEquals(currentText[0], exepectedText);
		}catch(AssertionError e) {
			System.out.println(e.getMessage());
			Assert.fail();
		}

	}
}
