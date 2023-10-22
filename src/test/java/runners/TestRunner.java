package runners;

import constants.TestDataConstants;
import framework.MyTestNGBaseClass;
import io.cucumber.testng.*;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

@CucumberOptions(
        features = { "classpath:features/softpos" },
        tags = { "@Test" },
        plugin = { "pretty", "io.qameta.allure.cucumber4jvm.AllureCucumber4Jvm" },
        glue = { "stepdefs" },
        monochrome = true
        )

public class TestRunner extends MyTestNGBaseClass {
    private io.cucumber.testng.TestNGCucumberRunner testNGCucumberRunner;

    @BeforeClass(alwaysRun = true)
    public void setUpClass() {
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
    }

    @Test(groups = "cucumber", description = "Runs Cucumber Feature", dataProvider = "features")
    public void runScenario(PickleEventWrapper pickleWrapper, CucumberFeatureWrapper featureWrapper) throws Throwable {
        TestDataConstants.stepDefsTestSteps = pickleWrapper.getPickleEvent().pickle.getSteps();
        this.testNGCucumberRunner.runScenario(pickleWrapper.getPickleEvent());
    }

    @Attachment(value = "Page screenshot", type = "image/png")
    public byte[] saveScreenshot(WebDriver oDriver) {
        return ((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES);
    }

    @DataProvider
    public Object[][] features() {
        return testNGCucumberRunner.provideScenarios();
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        testNGCucumberRunner.finish();
    }

}
