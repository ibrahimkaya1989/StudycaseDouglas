package utils;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import framework.CommonLib;
import model.LocalDriver;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtendReportUtil {
    private final static Logger logger = LogManager.getLogger(ExtendReportUtil.class);
    private static ExtentReports oExtentReport;
    private static ExtentTest oExtentTest;
    public static String reportPath;
    public static String reportFolder;
    public static String reportVideoFolder;
    public static String reportScreenshotFolder;

    /**
     * <p>This method is used to add a new test scenario. </p>
     * @param scenarioName  Used to define test scenario name.
     */
    public static void startTest(String scenarioName) {

        // Add Extend step into suite.xml
        oExtentTest = oExtentReport.startTest(scenarioName);
        logger.info("Added Scenario name -> " + scenarioName);
    }

    /**
     * <p>This method is used to finish preparing and creating report file. </p>
     */
    public static void endReport() {
        oExtentReport.endTest(oExtentTest);
        oExtentReport.flush();
    }

    /**
     * <p>This method is used to add a reporting event to the Extent Report for the test running</p>
     *
     * @param status  Used to mark the status of the step in the test case
     * @param message Test Step description
     * @param ssFlag  Flag for Screenshot
     * Note : Screenshots are always taken in case a failure is reported in the step
     */
    public static void reportResult(String status, String message, boolean ssFlag) {

        String imagePath = null;

        if(ssFlag){
            imagePath = getScreenshotAndPath();
            addStepWithScreenshot(status, message, imagePath);
        } else {
            addStep(status, message);
        }

    }

    public static void addStep(String status, String message){
        oExtentTest.log(LogStatus.valueOf(status), message);
    }


    public static void addStepWithScreenshot(String status, String message, String imagePath){
        oExtentTest.log(LogStatus.valueOf(status), message, oExtentTest.addScreenCapture(imagePath));
    }


    public static void addStepWithVideoRecording(String status, String message, String videoPath){
        oExtentTest.log(LogStatus.valueOf(status), message, oExtentTest.addScreencast(videoPath));
    }

    /**
     * <p>This method is used to define report file. </p>
     * @return report's path.
     */
    private static String getReportPath() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return "extend-report/eReport_" + dateFormat.format(new Date());
    }

    /**
     * <p>This method is used to define screenshots' file. </p>
     * @param reportPath  Used to define and create test screenshots' file.
     * @return full path of screenshots' file.
     */
    private static String getReportScreenshotFolder(String reportPath){
        return reportPath + "/Screenshots";
    }

    /**
     * <p>This method is used to define video recordings' file. </p>
     * @param reportPath  Used to define and create test video recordings' file.
     * @return full path of video recordings' file.
     */
    public static String getReportVideoFolder(String reportPath){
        return reportPath + "/VideoRecords";
    }

    public static String getScreenshotAndPath() {
        String dest;
        TakesScreenshot ts = (TakesScreenshot) LocalDriver.getLocalDriver();
        File source = ts.getScreenshotAs(OutputType.FILE);
        dest = System.getProperty("user.dir") + "/" + reportScreenshotFolder + "/" +
                CommonLib.getRandomString(30) + ".png";
        File destination = new File(dest);
        try {
            FileUtils.copyFile(source, destination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dest;
    }

    /**
     * <p>This method is used to create test report files. </p>
     */
    public static void startReport(){

        // Definition of report files
        reportFolder = getReportPath();
        logger.info("Report Folder : " + reportFolder);
        reportPath = System.getProperty("user.dir") + "/" + reportFolder;
        logger.info("Report Path : " + reportPath);
        reportVideoFolder = getReportVideoFolder(reportFolder);
        logger.info("Report Video Folder : " + reportVideoFolder);
        reportScreenshotFolder = getReportScreenshotFolder(reportFolder);
        logger.info("Report Screenshot Folder : " + reportScreenshotFolder);

        // Start the report xml path
        oExtentReport = new ExtentReports(reportFolder + "/TestSuiteReport.html", true);
        oExtentReport.loadConfig(new File("src/main/resources/extend-report-config.xml"));

        // Creation files
        File f = new File(reportFolder);
        File ss = new File(reportScreenshotFolder);
        File v = new File(reportVideoFolder);

        try {
            f.mkdir();
            ss.mkdir();
            v.mkdir();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
