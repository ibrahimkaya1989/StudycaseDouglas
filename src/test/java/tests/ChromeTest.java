package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class ChromeTest {

    @Test
    public void ChromeGoogleTest() {

        //setting the driver executable
        System.setProperty("webdriver.chrome.driver", "./Exes/chromedriver.exe");
        //WebDriverManager.chromedriver().setup();
        System.out.println(System.getProperty("webdriver.chrome.driver"));

        //Setting chrome options
        ChromeOptions chromeOptions = new ChromeOptions();
        //chromeOptions.setBinary("C:/Program Files/Google/Chrome/Application/chrome.exe");

        chromeOptions.setHeadless(false);
        chromeOptions.isJavascriptEnabled();
        chromeOptions.addArguments("test-type");
        chromeOptions.addArguments("disable-translate");
        chromeOptions.addArguments("start-maximized");
        chromeOptions.addArguments("disable-popup-blocking");
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NONE);

        chromeOptions.setCapability("takesScreenshot", true);

        //Initiating your chromedriver
        WebDriver driver = new ChromeDriver(chromeOptions);

        //Applied wait time
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);

        //maximize window
        driver.manage().window().maximize();

        //open browser with desired URL
        driver.get("https://www.google.com/");

        try {

            // button
            WebElement wbButton = driver.findElements(By.xpath("//button")).get(3);
            wbButton.click();

            // search
            WebElement wbSearch = driver.findElement(By.name("q"));
            wbSearch.sendKeys("Douglas Parfum");

            Thread.sleep(1000);

            wbSearch.sendKeys(Keys.ENTER);

            Thread.sleep(5000);

            //closing the browser
            driver.close();

        } catch (Exception e) {
            e.printStackTrace();
            driver.close();
        }

    }

}
