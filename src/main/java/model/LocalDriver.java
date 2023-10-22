package model;

import org.openqa.selenium.WebDriver;

public class LocalDriver {
    public static WebDriver localDriver;
    public static WebDriver getLocalDriver() { return localDriver; }
    public static void setLocalDriver(WebDriver localDriver) { LocalDriver.localDriver = localDriver; }
}
