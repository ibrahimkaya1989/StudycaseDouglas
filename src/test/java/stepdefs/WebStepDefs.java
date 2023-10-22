package stepdefs;

import constants.TestDataConstants;
import framework.CommonLib;
import framework.MyTestNGBaseClass;
import io.cucumber.core.api.Scenario;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.cucumber.java.Before;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WebStepDefs extends MyTestNGBaseClass {

    private final Logger logger = LogManager.getLogger(this.getClass().getName());
    public static CommonLib commonLib = new CommonLib();
    String currentStepDescription = null;
    int currentStepIndex = 0;

    @Before
    public void setReportName(Scenario scenario) {
        commonLib.startTest(scenario.getName());

        System.out.println("\n\n");
        logger.info("----------------------------------------------------------------");
        logger.info("  Testcase name: " + scenario.getName());
        logger.info("---------------------- Begin of Test Case ----------------------");
    }

    @After
    public void finish() {
        //Thread.sleep(AutomationConstants.tearDownTimeout);
        logger.info("---------------------- End of Test Case ------------------------");
    }

    @BeforeStep
    public void beforeStep() {
        System.out.println("\n");
        if ( TestDataConstants.stepDefsTestSteps != null ) {
            currentStepDescription = TestDataConstants.stepDefsTestSteps.get(currentStepIndex).getText();
            logger.info("## Step Definition: " + currentStepDescription);
            currentStepIndex++;
            logger.info("------------ Start of Test Step ------------");
        } else {
            logger.error("## Step Definition could not find. ( Hint: Check the TestRunner. )");
        }
    }

    @AfterStep
    public void afterStep() {
        logger.info("------------- End of Test Step -------------");
    }

    @Given("^Open the (.*) URL$")
    public void iOpenUrl(String URL) {
        //CommonLib.navigateToURL(oDriver, URL);
        commonLib.navigateToURL(URL);
    }

    @And("^I navigate to the URL$")
    public void iNavigateUrl() {
        commonLib.navigateToURL(TestDataConstants.loginURL);
    }

    @Then("^I see (.*) page$")
    public void iSeePage(String page) { commonLib.seePage(page); }

    @And("^I need to just wait$")
    public void iJustWaitAWhile() { commonLib.waitForSeconds(10); }

    @And("I need to wait {int} second(s)")
    public void iNeedWait(int second) {
        logger.info("Sleep for seconds: " + second);
        commonLib.waitForSeconds(second);
    }

    @And("^I wait (.*) element (\\d+) seconds at index (\\d+)$")
    public void iWaitElement(String element, int timeout, int index) {
        commonLib.waitElement(element, timeout, index);
    }

    @When("^(?:I )?click element: (.*) at index (\\d+)$")
    public boolean iClickElementByIndex(String element, int index) {
        return commonLib.clickElement(element, index);
    }

    @And("^I click (.*) element$")
    public boolean iClickElement(String element) {
        return iClickElementByIndex(element, 1);
    }

    @Then("I click {string} if exists")
    public boolean iClickIfExists(String element) {
        return commonLib.clickIfExists(element);
    }

    @Then("I focus {string} element at index {int}")
    public boolean iFocusElement(String element, int index){
        return commonLib.focusElement(element, index);
    }

    @Then("^I clear text to (.*) at index (\\d+)$")
    public boolean iClearText(String element, int index) {
        return commonLib.clearText(element, index);
    }

    @Then("^I enter \"([^\"]*)\" text to (.*) at index (\\d+)$")
    public boolean iEnterText(String text, String element, int index) { return commonLib.enterText(text, element, index); }

    @Then("^I press \"([^\"]*)\" key$")
    public boolean iPressKey(String keyCode) { return commonLib.pressKey(keyCode); }

    @Then("^I press \"([^\"]*)\" keys$")
    public boolean iPressKeys(String keys) { return commonLib.pressKeys(keys); }

    @Then("^I press \"([^\"]*)\" key to (.*) at index (\\d+)$")
    public boolean iPressKeyToElement(String keyCode, String element, int index) { return commonLib.pressKeyToElement(keyCode, element, index); }

    @Then("^I press \"([^\"]*)\" keys to (.*) at index (\\d+)$")
    public boolean iPressKeysToElement(String keys, String element, int index) { return commonLib.pressKeysToElement(keys, element, index); }

    @Then("^I enter unique text to (.*) at index (\\d+)$")
    public boolean iEnterUniqueText(String element, int index) {
        return commonLib.enterUniqueText(element, index);
    }

    @Then("^I verify the page title including \"([^\"]*)\"$")
    public void iVerifyPageTitleIncluding ( String text ) { commonLib.checkPageTitleIncluding(text); }

    @Then("^(?:I )?get the item value: (.*)$")
    public String iGetTheItemValue(String element, int index) {
        return commonLib.getTheItemValue(element, index);
    }

    @Then("^(?:I )?get the css value from \"([^\"]*)\" element by \"([^\"]*)\" attribute at index (\\d+)$")
    public String iGetElementCssAttributeValue(String element, String attribute, int index) {
        return commonLib.getElementCssValue(element, attribute, index);
    }

    @Then("^(?:I )?get the value from \"([^\"]*)\" element by \"([^\"]*)\" attribute at index (\\d+)$")
    public String iGetElementAttributeValue(String element, String attribute, int index) {
        return commonLib.getElementAttributeValue(element, attribute, index);
    }

    @Given("^I switch to alert and OK button through element (.*) at index (\\d+)$")
    public void iSwitchToAlertAndAccept(String element, int index) {
        commonLib.switchToAlertAndAccept(element, index);
    }

    @Then("I scroll to the {string} element")
    public void iScrollToElement(String element) throws Throwable { commonLib.scrollToElement(element, 1); }

    @Then("I go to top of the site")
    public void iScrollTopOfWebsite() { commonLib.scrollTopOfWebsite(); }

    @Then("I see {string} text")
    public boolean iSeeText(String expectedText) { return commonLib.seeText(expectedText); }
}


