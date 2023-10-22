package stepdefs;

import constants.LocatorConstants;
import framework.CommonLib;
import framework.MyTestNGBaseClass;
import io.cucumber.java.en.Then;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class DouglasStepDefs extends MyTestNGBaseClass {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());
    CommonLib commonLib = WebStepDefs.commonLib;

    @Then("^I click \"([^\"]*)\" labelled option$")
    public void iClickProductOptionByText(String text) {

        WebElement element = commonLib.findElement(By.xpath(String.format(LocatorConstants.optionsByText, text)));
        logger.debug(String.format("%s labelled element found. By %s", text, element));
        element.click();
        logger.debug(String.format("%s labelled element clicked.", text));
    }

    @Then("^I click \"([^\"]*)\" labelled tab option$")
    public void iClickTabOptionByText(String text) {

        WebElement element = commonLib.findElement(By.xpath(String.format(LocatorConstants.tabOptionsByText, text)));
        logger.debug(String.format("%s labelled element found. By %s", text, element));
        element.click();
        logger.debug(String.format("%s labelled element clicked.", text));
    }
}
