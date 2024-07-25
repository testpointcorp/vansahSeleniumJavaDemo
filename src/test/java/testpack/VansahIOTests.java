package testpack;

import org.testng.annotations.CustomAttribute;
import org.testng.annotations.Test;

import pages.FooterSection;
import pages.HeaderSection;

public class VansahIOTests extends BaseTests{
	
	@Test(groups = { "regression" },attributes = {
		      @CustomAttribute(name = "Case Key", values = "TEST-C12"),
		      @CustomAttribute(name = "Tested Issue", values = JIRA_ISSUE_KEY),
		      @CustomAttribute(name = "Tested Sprint", values = sprintName ),
		      @CustomAttribute(name = "Tested Environment", values = environment)})
	public void FooterSectionTest() {
		String expectedCopyRightText = "© 2022 Vansah®. All rights reserved";
		FooterSection footer = new FooterSection(driver);		
		footer.scrollToFooter();
		footer.verifyCopyRightText(expectedCopyRightText);
		
	}
	
	@Test(groups = { "regression" },attributes = {
		      @CustomAttribute(name = "Case Key", values = "TEST-11"),
		      @CustomAttribute(name = "Tested Issue", values = JIRA_ISSUE_KEY),
		      @CustomAttribute(name = "Tested Sprint", values = sprintName ),
		      @CustomAttribute(name = "Tested Environment", values = environment)})
	public void HeaderSectionTest() {
		String expectedAltLogoText = "";		// Kept Empty to Fail the result , Actual Value = "Selenium Website Testing Page"
		HeaderSection header = new HeaderSection(driver);
		header.verifylogoAltText(expectedAltLogoText);
	}
	

}
