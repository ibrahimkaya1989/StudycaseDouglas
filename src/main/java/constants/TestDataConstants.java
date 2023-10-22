package constants;

import gherkin.pickles.PickleStep;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class TestDataConstants {
    public static WebDriver oDriver;
    public static TouchActions actions;
    public static WebDriverWait wait;

    public static String loginURL   = "";
    public static String username   = "";
    public static String password   = "";

    public static List<PickleStep> stepDefsTestSteps = null;
}
