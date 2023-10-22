package framework;

import constants.TestDataConstants;
import model.LocalDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.ExtendReportUtil;
import utils.SystemInfoUtil;
import utils.VideoRecorderUtil;

public class MyTestNGBaseClass {
	private final Logger logger = LogManager.getLogger(this.getClass().getName());
	public static WebDriver oDriver;
	public static String sDriverName = "";
	public static String browser;

	@BeforeSuite
	public void beforeSuite(){

		SystemInfoUtil.allInfos();
		ExtendReportUtil.startReport();
	}

	@Parameters({ "browserName", "URL" })
	@BeforeMethod
	public void BeforeMethod(@Optional("") String browserName, @Optional("") String URL) throws Throwable {

		/* Jenkins Parameters */
		browser = browserName;
		logger.info("Browser name parameter is : " + browserName);
		TestDataConstants.loginURL = URL;
		logger.info("URL : " + URL);

		/* Browser selection */
		if (browserName.equalsIgnoreCase("ie")) { sDriverName = "ie"; }
		else if (browserName.equalsIgnoreCase("chrome")) { sDriverName = "chrome"; }
		else if (browserName.equalsIgnoreCase("firefox")) { sDriverName = "firefox"; }
		else if (browserName.equalsIgnoreCase("edge")) { sDriverName = "edge"; }
		else if (browserName.equalsIgnoreCase("opera")) { sDriverName = "opera"; }
		else if (browserName.equalsIgnoreCase("safari")) { sDriverName = "safari"; }
		else if (browserName.equalsIgnoreCase("phantomjs")) { sDriverName = "phantomjs"; }
		else if (browserName.equalsIgnoreCase("htmlunit")) { sDriverName = "htmlunit"; }
		else
			throw new Exception("Unknown driver name = " + sDriverName +
					"! Valid names are: ie,chrome,edge,firefox,opera,safari,phantomjs,htmlunit");

		/* Local Webdriver */
		oDriver = CommonLib.getDriver(sDriverName);
		/*  Remote Webdriver */
		//oDriver = CommonLib.getRemoteDriver("http://192.168.1.38:4444/wd/hub", sDriverName);

		LocalDriver.setLocalDriver(oDriver);
	}

	/**
	 * <p>Start video recording</p>
	 */

	@Parameters({"VIDEO_RECORDING"})
	@BeforeMethod
	public void startRecording(@Optional("false")String VIDEO_RECORDING) throws Exception {

		logger.info("Video recording parameter is : " + VIDEO_RECORDING);
		if (VIDEO_RECORDING.equalsIgnoreCase("true")) {
			VideoRecorderUtil.startRecord("Recording", ExtendReportUtil.reportVideoFolder);
		}
	}

	/**
	 * <p>Stop video recording</p>
	 */

	@Parameters({"VIDEO_RECORDING"})
	@AfterMethod
	public void stopRecording(@Optional("false")String VIDEO_RECORDING, ITestResult result) throws Exception {

		if (VIDEO_RECORDING.equalsIgnoreCase("true")) {

			VideoRecorderUtil.stopRecord();

			String videoPath = CommonLib.getNewestFileInDirectory(ExtendReportUtil.reportVideoFolder);

			ExtendReportUtil.addStepWithVideoRecording("INFO","Testing video over here!", videoPath);
		}
	}

	/**
	 * <p>Terminate actual web driver.</p>
	 * Terminate Web Driver after every test case.
	 */
	@AfterMethod(alwaysRun = true)
	public void teardown(){
		oDriver.quit();
	}

	/**
	 * <p>End of execution</p>
	 * Terminate Extend Report.
	*/
	@AfterSuite(alwaysRun = true)
	public void afterSuite() {

		ExtendReportUtil.endReport();
	}

	/**
	 * <p>This method is used to add a reporting event to the Extent Report for the test running</p>
	 *
	 * @param status  Used to mark the status of the step in the test case
	 * @param message Test Step description
	 * @param ssFlag  Flag for Screenshot
	 * Note: Screenshots are always taken in case a failure is reported in the step
	 */
	public void addStepExtendReport(String status, String message, boolean ssFlag) {

		ExtendReportUtil.reportResult(status, message, ssFlag);
	}

	public void startTest(String scenarioName) {

		ExtendReportUtil.startTest(browser + " --> " + scenarioName);
	}
}
