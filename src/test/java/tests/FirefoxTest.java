package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class FirefoxTest {

    @Test
    public void FirefoxGoogleTest() {

        // Setting the driver executable
        System.setProperty("webdriver.gecko.driver", "./Exes/geckodriver.exe");
        //System.setProperty("webdriver.gecko.driver", "C:/Tools/WebDrivers/geckodriver.exe");
        //WebDriverManager.firefoxdriver().setup();
        System.out.println(System.getProperty("webdriver.gecko.driver"));

        // Setting chrome options
        FirefoxOptions options = new FirefoxOptions();

        options.setHeadless(false);
        options.isJavascriptEnabled();
        options.setPageLoadStrategy(PageLoadStrategy.NONE);

        options.setCapability("takesScreenshot", true);

        // Initiating your firefox
        WebDriver driver = new FirefoxDriver(options);

        // Applied wait time
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        // Maximize window
        driver.manage().window().maximize();

        // Open browser with desired URL
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
            //driver.close();
            driver.quit();

        } catch (Exception e) {
            e.printStackTrace();
            //driver.close();
            driver.quit();
        }

    }

}
