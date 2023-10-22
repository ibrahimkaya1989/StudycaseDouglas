package framework;

import com.sun.javafx.PlatformUtil;
import constants.AutomationConstants;
import constants.LocatorConstants;
import constants.StepResultType;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.OperatingSystem;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.shrinkwrap.resolver.api.maven.InvalidEnvironmentException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utils.LoggerUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class CommonLib extends MyTestNGBaseClass {
    private static final Logger logger = LogManager.getLogger(CommonLib.class.getName());
    private static WebDriver oDriver;
    private String page = "common";
    public String uuid = UUID.randomUUID().toString();
    int timeout = AutomationConstants.intTimeout;
    Parser parser = new Parser();
    Actions actions = new Actions(oDriver);
    WebDriverWait wait = new WebDriverWait(oDriver, AutomationConstants.explicitWaitTimeout);

    public void navigateToURL(String URL) {
        oDriver.manage().window().maximize();
        oDriver.navigate().to(URL);
        //oDriver.manage().window().fullscreen();
        logger.debug("Webdriver navigated to " + URL);
    }

    public boolean clickElement(String elementName, int index) {
        WebElement object = findElement(elementName, index);
        boolean flag = false;
        try {
            if (object != null) {
                object.click();
                logger.debug("Clicked on object --> " + elementName);
                automationReporter(StepResultType.PASS, "I clicked the element: " + elementName, true);
                flag = true;
            }
        } catch (ElementClickInterceptedException e) {
            logger.error("ElementClickInterceptedException caught to click the element.");
            automationReporter(StepResultType.FAIL, "Could not clicked the element: " + elementName, true);
            //clickElementByAction(object);
        } catch (Exception e) {
            e.printStackTrace();
            automationReporter(StepResultType.FAIL, "Could not clicked the element: " + elementName, true);
        }
        return flag;
    }

    public boolean clickElement(By elementByXPath) {
        WebElement object = findElement(elementByXPath);
        boolean flag = false;
        try {
            if (object != null) {
                object.click();
                logger.debug("Clicked on object --> " + elementByXPath);
                automationReporter(StepResultType.PASS, "I clicked the element: " + elementByXPath, true);
                flag = true;
            }
        } catch (ElementClickInterceptedException e) {
            e.printStackTrace();
            logger.error("ElementClickInterceptedException caught to click the element.");
            automationReporter(StepResultType.FAIL, "Could not clicked the element: " + elementByXPath, true);
            //clickElementByAction(object);
        } catch (Exception e) {
            e.printStackTrace();
            automationReporter(StepResultType.FAIL, "Could not clicked the element: " + elementByXPath, true);
        }
        return flag;
    }

    public boolean clickElementByAction(WebElement webElement) {
        boolean flag = false;
        try {
            actions = new Actions(oDriver);
            actions.moveToElement(webElement).click().perform();
            logger.debug("Web element clicked by {} -> {}", LoggerUtil.getCurrentMethodName(), webElement.getTagName());
            flag = true;
        } catch (Exception e) {
            logger.debug("Exception caught to click the element by clickElementByAction.");
            clickElementByJsExecutor(webElement);
        }
        return flag;
    }

    public boolean clickElementByJsExecutor(WebElement webElement) {
        boolean flag = false;
        try {
            ((JavascriptExecutor) oDriver).executeScript("arguments[0].click();", webElement);
            logger.debug("Web element clicked by {} -> {}", LoggerUtil.getCurrentMethodName(), webElement.getTagName());
            waitForSeconds(5);
            flag = true;
        } catch (Exception e) {
            logger.debug("Exception caught to click the element by clickElementByAction.");
            automationReporter(StepResultType.FAIL, "Could not clicked the element.", true);
        }
        return flag;
    }

    public boolean clickIfExists(String element) {
        try {
            WebElement elementToSee = checkElement(element, 1);
            if (elementToSee != null) {
                elementToSee.click();
                logger.debug("Found and Clicked on object --> " + element);
                automationReporter(StepResultType.PASS, "I clicked the element: " + element, true);
                return true;
            } else {
                logger.info("No more object left --> " + element);
                automationReporter(StepResultType.INFO, "Element does not exist: " + element, true);
                return true;
            }
        } catch (Exception e) {
            logger.info("No more object left --> " + element);
            automationReporter(StepResultType.INFO, "Element does not exist: " + element, true);
            return true;
        }
    }

    public boolean focusElement(String element, int index){
        WebElement object = findElement(element, index);
        boolean flag = false;
        try {
            if (object != null) {
                actions.moveToElement(object).perform();
                logger.debug("Focused on object --> " + element);
                automationReporter(StepResultType.PASS, "I focused the element: " + element, true);
                flag = true;
            }
        } catch (Exception e) {
            automationReporter(StepResultType.FAIL, "I can NOT focused on the element: " + element, true);
            flag = false;
        }
        return flag;
    }

    public boolean enterText(String text, String elementName, int index) {
        WebElement object;
        //object = waitElement(elementName, timeout, index);
        object = waitPresentElement(elementName, timeout, index);
        boolean flag = false;
        try {
            if (object != null) {
                object.click();
                object.clear();
                object.sendKeys(text);
                logger.debug("The text has been entered: " + text);
                automationReporter(StepResultType.PASS, "I entered the text: " + text, true);

                flag = true;
            }
        } catch (InvalidElementStateException e) {
            logger.error("Could not entered the text: " + text);
            automationReporter(StepResultType.FAIL, "Could NOT enter the text: " + text, true);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Unknown error. Could not entered the text: " + text);
            automationReporter(StepResultType.FAIL, "Could NOT enter the text: " + text, true);
        }
        return flag;
    }

    public boolean pressKey (String keyCode){
        try {
            actions.sendKeys(Keys.valueOf(keyCode)).perform();
            logger.debug("Press {} of key code", keyCode);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean pressKeys (String keys){
        try {
            for ( char key : keys.toCharArray() ) {
                actions.sendKeys(String.valueOf(key)).perform();
                Thread.sleep(200);
                logger.debug("Press {} keys.", keys);
            }
            return true;
        } catch (Exception e) {
            logger.error("Press {} keys got error!", keys);
            return false;
        }
    }

    public boolean pressKeyToElement (String keyCode, String elementName, int index){
        WebElement object;
        object = waitElement(elementName, AutomationConstants.intTimeout, index);
        try {
            if( object != null ){
                actions.sendKeys(object, Keys.valueOf(keyCode)).perform();
                logger.debug("Press {} of key code on {} element", keyCode, elementName);
            }
            else return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean pressKeysToElement (String keys, String elementName, int index){
        WebElement object;
        object = waitElement(elementName, AutomationConstants.intTimeout, index);
        try {
            if( object != null ){
                for ( char key : keys.toCharArray() ) {
                    actions.sendKeys(object, String.valueOf(key)).perform();
                    Thread.sleep(200);
                }
                logger.debug("Press {} keys on {} element", keys, elementName);
            }
            else return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void scrollTopOfWebsite() {
        ((JavascriptExecutor) oDriver).executeScript("window.scrollTo(document.body.scrollHeight, 0)");
    }

    public void scrollToElement(String elementName, int index) throws InterruptedException {
        WebElement elementToSee = findElement(elementName, index);
        ((JavascriptExecutor) oDriver).executeScript("arguments[0].scrollIntoView(true);", elementToSee);
        Thread.sleep(1000);
    }

    public boolean enterUniqueText(String elementName, int index) {
        WebElement object;
        object = waitElement(elementName, timeout, index);
        String text = "automation" + uuid;
        boolean flag = false;
        try {
            if (object != null) {
                object.sendKeys(text);
                logger.debug("The text has been entered.");
                automationReporter(StepResultType.PASS, "I entered the text: " + text, true);
                flag = true;
            }
        } catch (Exception e) {
            automationReporter(StepResultType.FAIL, "I cannot entered the element: " + text, true);
            flag = false;
        }
        return flag;
    }

    public boolean clearText(String element, int index) {
        WebElement object;
        object = waitElement(element, timeout, index);
        boolean flag = false;
        try {
            if (object != null) {
                object.click();
                object.sendKeys(Keys.CONTROL, "a");
                object.sendKeys(Keys.DELETE);
                logger.debug("The text has been deleted.");
                automationReporter(StepResultType.PASS, "The text has been deleted.", true);
                flag = true;
            }
        } catch (Exception e) {
            logger.debug("The text has not been deleted.");
            automationReporter(StepResultType.FAIL, "The text has not been deleted.", true);
            flag = false;
        }
        return flag;
    }

    public WebElement findElement(String elementName) { return getWebElement(elementName); }
    public WebElement findElement(By elementLocator) { return getWebElement(elementLocator); }
    public WebElement findElement(String elementName, int index) { return getWebElements(elementName).get(index - 1); }
    public List<WebElement> findElements(String elementName) { return getWebElements(elementName); }
    public List<WebElement> findElements(By elementsLocator) { return getWebElements(elementsLocator); }
    public WebElement checkElement(String elementName, int index) { return getWebElements(elementName).get(index - 1); }

    public WebDriverWait getWebDriverWaiter(int second){
        return (WebDriverWait) new WebDriverWait(oDriver, second).pollingEvery(Duration.ofSeconds(2));
    }

    public By getLocator(String elementName){

        String elementValue = parser.getElement(page, elementName);

        try {
            if (elementValue != null) {
                if (elementValue.startsWith("//") || elementValue.startsWith("(//")) {
                    logger.debug(elementName + " element value found : " + elementValue);
                    return By.xpath(elementValue);
                } else if (elementValue.startsWith("#") || elementValue.startsWith(".")) {
                    logger.debug(elementName + " element value found : " + elementValue);
                    return By.cssSelector(elementValue);
                } else {
                    logger.debug(elementName + " element value found : " + elementValue);
                    return By.id(elementValue);
                }
            } else if (elementValue == null) {
                logger.warn("Element value NOT found by elementName.");
                By xpath = By.xpath(String.format(LocatorConstants.xPathByText, elementName, elementName));
                logger.debug("Element value returns " + xpath);
                return xpath;
            }
        } catch (Exception e) {
            logger.debug("Elements NOT found: " + elementName);
            automationReporter(StepResultType.FAIL, "There is no such elements. " + elementName, true);
            return null;
        }
        return null;
    }

    public WebElement getWebElement(By elementLocator){
        WebElement webElement;

        try {
            webElement = oDriver.findElement(elementLocator);
            return webElement;
        } catch (Exception e) {
            logger.error("Element NOT found: " + elementLocator);
            automationReporter(StepResultType.FAIL, "There is no such element. By: " + elementLocator, true);
            return null;
        }
    }

    public WebElement getWebElement(String elementName){
        return getWebElement(getLocator(elementName));
    }

    public List<WebElement> getWebElements(By elementsLocator){
        List<WebElement> elementList;

        try {
            elementList = oDriver.findElements(elementsLocator);
            return elementList;
        } catch (Exception e) {
            logger.error("Elements NOT found. By: " + elementsLocator);
            automationReporter(StepResultType.FAIL, "There is no such elements. By: " + elementsLocator, true);
            return null;
        }
    }

    public List<WebElement> getWebElements(String elementName){
        return  getWebElements(getLocator(elementName));
    }

    public String getTooltipText(String element, int index) {
        String tooltipMsg = getElementAttributeValue(element, "title", index);
        logger.debug("Tooltip title message : {}", tooltipMsg);
        return tooltipMsg;
    }

    public String getTheItemValue(String elem, int index) {
        String elementText = findElement(elem, index).getText();
        logger.debug("Element getText: {}", elementText);
        return elementText;
    }

    public String getElementAttributeValue(String elem, String attribute, int index) {

        String attributeValue = wait.until(ExpectedConditions.visibilityOf(findElement(elem, index))).getAttribute(attribute);
        logger.debug("{} attribute value: {}", attribute, attributeValue);
        return attributeValue;
    }

    public String getElementCssValue(String elem, String attribute, int index) {

        String cssValue = wait.until(ExpectedConditions.visibilityOf(findElement(elem, index))).getCssValue(attribute);
        logger.debug("{} CSS value: {}", attribute, cssValue);
        return cssValue;
    }

    public String getPageTitle() { return oDriver.getTitle(); }

    public void checkPageTitleIncluding(String text) {
        String title = getPageTitle();
        logger.debug("Actual title   : " + title);
        logger.debug("Expected title : " + text);
        Assert.assertTrue(title.contains(text));
    }

    public String seePage(String page) {
        List<String> returnValue = parser.isPageExist(page);

        try {
            if (returnValue.get(0).equalsIgnoreCase(page)) {
                logger.debug(page + " page found!");
                this.page = page;

                if (returnValue.get(1).length() > 0) {
                    waitElement(returnValue.get(1), timeout, 1);
                }
                automationReporter(StepResultType.PASS, "I see " + page + " page. (Page found)", false);
                return page;
            }
        } catch (Exception e) {
            automationReporter(StepResultType.FAIL, "I see " + page + " page. (Page NOT found)", true);
        }
        return null;
    }

    public boolean seeText(String expectedText){
        By xpath = By.xpath(String.format(LocatorConstants.xPathByText, expectedText, expectedText));
        String text = oDriver.findElement(xpath).getText();

        if (text != null) {
            automationReporter(StepResultType.PASS, "I see text: " + expectedText , true);
            logger.debug("Expected text is shown. By: " + xpath);
            return true;
        } else {
            automationReporter(StepResultType.INFO, "I can not see text: " + expectedText , true);
            logger.error("Expected text is NOT shown. By: " + xpath);
            return false;
        }
    }

    public WebElement waitElement(String elementName, int timeout, int index) {
        return waitPresentElement(elementName, timeout, index);
    }

    /**
     * <p>This method returns Web element.</p>
     *
     * @param elementName name of element in json file.
     * @param timeout timeout duration in seconds.
     * @param index element index
     * @return Web element if found.
     */
    public WebElement waitPresentElement(String elementName, int timeout, int index) {
        WebElement object = null;
        try {
            object = findElement(elementName, index);
            if ( getWebDriverWaiter(timeout).until(ExpectedConditions.visibilityOf(object)).isDisplayed() ) {
                object = getWebDriverWaiter(timeout).until(ExpectedConditions.elementToBeClickable(object));
            }
        } catch (Exception e) {
            logger.error("Element could NOT find.");
            automationReporter(StepResultType.FAIL, "Element could NOT find.", true);
        }
        return object;
    }

    /**
     * <p>This method returns random chars as many as required.</p>
     *
     * @param count number of chars to return
     * @return random string value
     */
    public static String getRandomString(int count) {
        String randomChars = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < count) { // length of the random string.
            int index = (int) (rnd.nextFloat() * randomChars.length());
            salt.append(randomChars.charAt(index));
        }
        return salt.toString();

    }

    /**
     * <p>Define the method of Desired capability of the browsers</p>
     * javascript = true and set the Proxy for the browser
     */
    public static DesiredCapabilities getCapability() {
        DesiredCapabilities oCapability = new DesiredCapabilities();
        oCapability.setJavascriptEnabled(true);
        oCapability.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, PageLoadStrategy.NORMAL);
        //oCapability.setCapability(CapabilityType.PROXY, getProxy());
        return oCapability;
    }


    /**
     * <p>Set InterExplorerOptions by merging the Desired Capability</p>
     *
     * @return InterExplorerOptions
     */
    public static InternetExplorerOptions getIEOptions() {
        InternetExplorerOptions oIEOptions = new InternetExplorerOptions();
        oIEOptions.merge(getCapability());
        oIEOptions.ignoreZoomSettings();
        oIEOptions.introduceFlakinessByIgnoringSecurityDomains();

        return oIEOptions;
    }

    /**
     * <p>Set EdgeOptions by merging the Desired Capability</p>
     *
     * @return EdgeOptions
     */
    public static EdgeOptions getEdgeOptions() {
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.merge(getCapability());

        if (PlatformUtil.isLinux()) {
            logger.warn("Edge capabilities added for Linux!!!!!!");

            edgeOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
            List<String> args = Arrays.asList("--no-sandbox", "--headless", "--disable-dev-shm-usage",
                    "--disable-extensions", "disable-infobars", "--remote-debugging-port=9222",
                    "--window-size=1920,1080");

            Map<String, Object> map = new HashMap<>();
            map.put("args", args);
            edgeOptions.setCapability("ms:edgeOptions", map);
        }

        return edgeOptions;
    }

    /**
     * <p>Set ChromeOptions by merging the Desired Capability</p>
     *
     * @return ChromeOptions
     */
    public static ChromeOptions getChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.merge(getCapability());

        if (PlatformUtil.isWindows()) { // OperatingSystem.WIN.matchOs(System.getProperty("os.name"))
            logger.warn("Chrome capabilities added for Windows!!!!!");
            chromeOptions.addArguments("test-type");
            chromeOptions.addArguments("disable-translate"); // Dil çevirme penceresini kapattırma.
            chromeOptions.addArguments("start-maximized"); // Browser tam ekranda gösterilir.
            //chromeOptions.addArguments("--headless"); // Arkaplanda test yapar.
            chromeOptions.addArguments("--disable-popup-blocking"); // Pop-uplar bloklanır.
            chromeOptions.addArguments("--disable-extensions"); // Uzantılar kapatılır.
            chromeOptions.addArguments("--disable-notifications"); // Bildirimler kapatılır.
            chromeOptions.addArguments("--disable-gpu"); // Added for Linux fail - applicable to windows os only
            //chromeOptions.setPageLoadStrategy(PageLoadStrategy.NONE);

        } else if (PlatformUtil.isLinux()) {
            logger.warn("Chrome capabilities added for Linux!!!!!!");
            chromeOptions.addArguments("--no-sandbox"); // Added for Linux fail - Bypass OS security model
            chromeOptions.addArguments("--headless"); // Arkaplanda test yapar.
            chromeOptions.addArguments("--disable-dev-shm-usage"); // Added for Linux fail - overcome limited resource problems
            chromeOptions.addArguments("--disable-extensions"); // Uzantılar kapatılır.
            chromeOptions.addArguments("disable-infobars"); // Added for Linux fail - disabling infobars
            chromeOptions.addArguments("--remote-debugging-port=9222");
        }

        chromeOptions.addArguments("--window-size=1920,1080");
        return chromeOptions;
    }

    /**
     * <p>Set FirefoxOptions by merging the Desired Capability</p>
     *
     * @return FirefoxOptions
     */
    public static FirefoxOptions getFirefoxOptions() {
        FirefoxOptions oFirefoxOptions = new FirefoxOptions();
        oFirefoxOptions.merge(getCapability());
        //oFirefoxOptions.addArguments("-headless");
        //oFirefoxOptions.addArguments("-devtools");

        if (PlatformUtil.isLinux()) {
            logger.warn("Firefox capabilities added for Linux!!!!!!");
            oFirefoxOptions.addArguments("--no-sandbox"); // Bypass OS security model
            oFirefoxOptions.addArguments("-headless");
            oFirefoxOptions.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
            oFirefoxOptions.addArguments("--disable-extensions");
            oFirefoxOptions.addArguments("disable-infobars");
        }

        //oFirefoxOptions.addArguments("--window-size=1920,1080");
        oFirefoxOptions.setCapability("marionette", true);
        oFirefoxOptions.setLogLevel(FirefoxDriverLogLevel.DEBUG);
        System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "logs/geckodriver.log");

        return oFirefoxOptions;
    }

    /**
     * <p>Set FirefoxOptions by merging the Desired Capability</p>
     *
     * @return FirefoxOptions
     */
    public static OperaOptions getOperaOptions() {
        OperaOptions oOperaOptions = new OperaOptions();
        oOperaOptions.merge(getCapability());

        if (PlatformUtil.isLinux()) {
            logger.warn("Opera capabilities added for Linux!!!!!!");
            oOperaOptions.addArguments("--no-sandbox"); // Added for Linux fail - Bypass OS security model
            oOperaOptions.addArguments("--headless"); // Arkaplanda test yapar.
            oOperaOptions.addArguments("--disable-dev-shm-usage"); // Added for Linux fail - overcome limited resource problems
            oOperaOptions.addArguments("--disable-extensions"); // Uzantılar kapatılır.
            oOperaOptions.addArguments("--remote-debugging-port=9222");
        }

        //oOperaOptions.addArguments("--headless");
        oOperaOptions.addArguments("--disable-extensions");
        oOperaOptions.addArguments("--window-size=1920,1080");

        return oOperaOptions;
    }

    /**
     * <p>Set getPhantomJSDriver by merging PhantomJSDriverService and Desired Capability</p>
     *
     * @return getPhantomJSDriver
     */
    public static PhantomJSDriver getPhantomJSDriver() {

        DesiredCapabilities caps = new DesiredCapabilities();
        String[] phantomArgs = {"--webdriver-loglevel=DEBUG"};
        PhantomJSDriverService driverService = new PhantomJSDriverService.Builder()
                .usingPhantomJSExecutable(new File(System.getProperty("user.dir") + AutomationConstants.sPhantomJSPath))
                .usingAnyFreePort()
                //.withProxy(proxy)
                .usingCommandLineArguments(phantomArgs)
                .withLogFile(new File(System.getProperty("user.dir") + "/logs/phantomjsdriver.log"))
                .build();
        return new PhantomJSDriver(driverService, caps);
    }

    /**
     * <p>Set SafariOptions by merging the Desired Capability</p>
     *
     * @return SafariOptions
     */
    public static SafariOptions getSafariOptions() {
        if (OperatingSystem.WIN.isMac()) {
            SafariOptions options = new SafariOptions();
            options.setUseTechnologyPreview(true);
            return options;
        } else {
            throw new InvalidEnvironmentException("Safari Browser is only for MAC OS. Your OS: "
                    + System.getProperty("os.name"));
        }
    }

    /**
     * <p>This method defines web browser type.</p>
     *
     * @param sBrowserName name of web browser.
     * @return browser Id as integer.
     */
    public static int getBrowserId(String sBrowserName) {

        if (sBrowserName.equalsIgnoreCase("ie")) return 1;
        else if (sBrowserName.equalsIgnoreCase("chrome")) return 2;
        else if (sBrowserName.equalsIgnoreCase("firefox")) return 3;
        else if (sBrowserName.equalsIgnoreCase("edge")) return 4;
        else if (sBrowserName.equalsIgnoreCase("opera")) return 5;
        else if (sBrowserName.equalsIgnoreCase("safari")) return 6;
        else if (sBrowserName.equalsIgnoreCase("phantomjs")) return 7;
        else if (sBrowserName.equalsIgnoreCase("htmlunit")) return 8;
        else
            return -1;
    }

    /**
     * <p>This method defines WebDriver type</p>
     *
     * @param sBrowserName name of web browser as string.
     * @return WebDriver
     */

    public static WebDriver getDriver(String sBrowserName) throws Exception {

        logger.debug("getDriver service is getting started. | sBrowserName: " + sBrowserName);
        switch (getBrowserId(sBrowserName)) {
            case 1:
                System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + AutomationConstants.sIEDriverPath);
                WebDriverManager.iedriver().setup();
                oDriver = new InternetExplorerDriver(getIEOptions());
                break;

            case 2:
                System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + AutomationConstants.sChromeDriverPath);
                System.setProperty("webdriver.chrome.logfile", "logs/chromedriver.log");
                System.setProperty("webdriver.chrome.verboseLogging", "true");
                //WebDriverManager.chromedriver().setup();
                oDriver = new ChromeDriver(getChromeOptions());
                break;

            case 3:
                System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + AutomationConstants.sGeckoDriverPath);
                //WebDriverManager.firefoxdriver().setup();
                oDriver = new FirefoxDriver(getFirefoxOptions());
                break;

            case 4:
                System.setProperty("webdriver.edge.driver", System.getProperty("user.dir") + AutomationConstants.sEdgeDriverPath);
                System.setProperty("webdriver.edge.logfile", "logs/edgedriver.log");
                System.setProperty("webdriver.edge.verboseLogging", "true");
                WebDriverManager.edgedriver().setup();
                oDriver = new EdgeDriver(getEdgeOptions());
                break;

            case 5:
                System.setProperty("webdriver.opera.driver", System.getProperty("user.dir") + AutomationConstants.sOperaDriverPath);
                System.setProperty("webdriver.opera.logfile", "logs/operadriver.log");
                System.setProperty("webdriver.opera.verboseLogging", "true");
                WebDriverManager.operadriver().setup();
                oDriver = new OperaDriver(getOperaOptions());
                break;

            case 6:
                oDriver = new SafariDriver(getSafariOptions());
                break;

            case 7:
                System.setProperty("webdriver.phantomjs.driver", System.getProperty("user.dir") + AutomationConstants.sPhantomJSPath);
                //WebDriverManager.phantomjs().setup();
                oDriver = getPhantomJSDriver();
                break;

            case 8:
                oDriver = new HtmlUnitDriver();
                break;

            default:
                throw new Exception("Unknown browsername =" + sBrowserName +
                        " valid names are: ie,chrome,firefox,edge,opera,phantomjs,htmlunit");
        }

        oDriver.manage().deleteAllCookies();
        oDriver.manage().timeouts().pageLoadTimeout(AutomationConstants.pageLoadTimeout, TimeUnit.SECONDS);
        oDriver.manage().timeouts().implicitlyWait(AutomationConstants.implicitWaitTimeout, TimeUnit.SECONDS);

        return oDriver;

    }

    /**
     * <p>Define remoteDriver type</p>
     */

    public static WebDriver getRemoteDriver(String sHubUrl, String sBrowserName) throws Exception {

        logger.debug("getRemoteDriver service is started...");
        DesiredCapabilities oCapability = getCapability();

        switch (getBrowserId(sBrowserName)) {

            case 1:
                oCapability.setBrowserName("internet explorer");
                break;
            case 2:
                oCapability.setBrowserName("chrome");
                break;
            case 3:
                oCapability.setBrowserName("firefox");
                break;
            case 4:
                oCapability.setBrowserName("MicrosoftEdge");
                break;
            case 5:
                oCapability.setBrowserName("opera");
                break;
            case 6:
                oCapability.setBrowserName("safari");
                break;
            case 7:
                oCapability.setBrowserName("phantomjs");
                break;
            case 8:
                oCapability.setBrowserName("htmlunit");
                break;
            default:
                throw new Exception("Unknown browsername = " + sBrowserName +
                        "  valid names are: ie,chrome,safari,firefox,edge,opera,phantomjs,htmlunit");
        }

        oCapability.setPlatform(Platform.WINDOWS);
        //oCapability.setPlatform(Platform.MAC);

        oDriver = new RemoteWebDriver(new URL(sHubUrl), oCapability);

        //if (getBrowserId(sBrowserName) != 8) { oDriver.manage().window().maximize(); }

        oDriver.manage().deleteAllCookies();
        oDriver.manage().timeouts().pageLoadTimeout(AutomationConstants.pageLoadTimeout, TimeUnit.SECONDS);
        oDriver.manage().timeouts().implicitlyWait(AutomationConstants.implicitWaitTimeout, TimeUnit.SECONDS);

        return oDriver;
    }

    /**
     * <p>This method allows to wait on the page for as long as required.</p>
     *
     * @param sec second of waiter
     */
    public void waitForSeconds(int sec) {
        try {
            logger.debug("Sleep for sec(s): " + sec);
            Thread.sleep(sec * 1000L);
        } catch (InterruptedException e) {
            logger.error("An error occured while waiting...");
            logger.error(e.getMessage());
        }
    }

    public void switchToAlertAndAccept(String element, int index) {
        WebElement object = findElement(element, index);
        object.click();
        String windowHandle = oDriver.getWindowHandle(); // get the window name
        Alert alert = oDriver.switchTo().alert();
        logger.debug("Alert Popup Message : " + alert.getText()); // Print Alert popup
        //alert.accept(); // Close Alert popup
        alert.dismiss(); // Reject Alert popup
        oDriver.switchTo().window(windowHandle); // back to main window
    }

    /**
     * <p>This method allows to get screenshot.</p>
     *
     * @return screenshot as byte.
     */
    public static ByteArrayInputStream takeScreenshot() {
        return new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES));
    }

    /**
     * <p>This method returns caller method name as string value.</p>
     *
     * @return caller method name.
     */
    public static String getActualMethodName(){
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    /**
     * <p>This method returns newest file in directory.</p>
     *
     * @param path full path of directory.
     * @return file path.
     */
    public static String getNewestFileInDirectory(String path) {
        try {
            File directory = new File(path);
            File[] files = directory.listFiles(File::isFile);
            long lastModifiedTime = Long.MIN_VALUE;
            File chosenFile = null;

            if (files != null) {
                for (File file : files) {
                    if (file.lastModified() > lastModifiedTime) {
                        chosenFile = file;
                        lastModifiedTime = file.lastModified();
                    }
                }

                return Objects.requireNonNull(chosenFile).getAbsolutePath();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void automationReporter(StepResultType result, String message, boolean ssFlag){

        /* Allure Report */
        if(AutomationConstants.allureReporter) allureReport(result, message, ssFlag);
        /* Extend Report */
        if(AutomationConstants.extendReporter) extendReport(result, message, ssFlag);
    }

    public void allureReport(StepResultType result, String message, boolean ssFlag) {

        /* Add screenshots */
        try {
            if (ssFlag) {
                Allure.addAttachment("Screenshot : " + message, takeScreenshot() );
            }
        } catch (Exception e) {
            logger.info("Screenshot could NOT taken.");
            logger.error(e.getCause().getMessage());
        }

        /* Add steps and assertion errors */
        switch (result.toString().toLowerCase()) {
            case "pass":
                Allure.step(message, Status.PASSED);
                break;
            case "info":
            case "skip":
                Allure.step(message, Status.SKIPPED);
                break;
            case "fail":
                Allure.step(message, Status.FAILED);
                Assert.fail(message);
                break;
            case "broken":
                Allure.step(message, Status.BROKEN);
                Assert.fail(message);
                break;
            default:
                Allure.step(message);
                break;
        }
    }

    public void extendReport(StepResultType result, String message, boolean ssFlag) {
        addStepExtendReport(result.name(), message, ssFlag);
    }
}