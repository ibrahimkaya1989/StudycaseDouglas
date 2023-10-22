package utils;

import org.testng.log4testng.Logger;

public class LoggerUtil {
    public static Logger logger;
    public static Logger getLogger() { return logger; }
    public static void setLogger(Logger logger) { LoggerUtil.logger = logger; }

    /**
     * <p>This method returns caller method name as string value.</p>
     *
     * @return caller method name.
     */
    public static String getCurrentMethodName () {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }
}
