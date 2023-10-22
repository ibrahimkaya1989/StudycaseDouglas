package constants;

public class LocatorConstants {

    /* General Locators */
    public static String xPathByText = "//*[text()='%s' or contains(text(),'%s')]";
    public static String xPathById = "//*[@id='%s' or contains(@id,'%s')]";
    public static String xPathByDesc = "//*[@desc='%s' or contains(@desc,'%s')]";

    /* Douglas Locators */
    public static String optionsByText = "//*[@class='facet__menu-content']//*[contains(text(), '%s')]";
    public static String tabOptionsByText = "//a[contains(text(),'%s')]";
}
