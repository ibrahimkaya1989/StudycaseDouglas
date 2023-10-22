package listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestStepListener implements ITestListener {

    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Override
    public void onFinish(ITestContext arg0) { logger.info("onFinish!"); }

    @Override
    public void onStart(ITestContext arg0) { logger.info("onStart!"); }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) { logger.info("onTestFailedButWithinSuccessPercentage!"); }

    @Override
    public void onTestFailure(ITestResult arg0) { logger.info("onTestFailure!"); }

    @Override
    public void onTestSkipped(ITestResult arg0) { logger.info("onTestSkipped!"); }

    @Override
    public void onTestStart(ITestResult arg0) { logger.info("onTestStart"); }

    @Override
    public void onTestSuccess(ITestResult arg0) { logger.info("onTestSuccess"); }
}
