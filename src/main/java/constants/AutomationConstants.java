package constants;

public class AutomationConstants {
	public static boolean allureReporter 			= true;
	public static boolean extendReporter 			= true;
	public static final String sIEDriverPath      	= "/Exes/IEDriverServer.exe";
	public static final String sChromeDriverPath  	= "/Exes/chromedriver.exe";
	public static final String sGeckoDriverPath   	= "/Exes/geckodriver.exe";
	public static final String sEdgeDriverPath		= "/Exes/msedgedriver.exe";
	public static final String sOperaDriverPath		= "/Exes/operadriver.exe";
	public static final String sPhantomJSPath		= "/Exes/phantomjs.exe";
	public static long pageLoadTimeout 				= 20;
	public static long implicitWaitTimeout 			= 10;
	public static int explicitWaitTimeout			= 10;
	public static int intTimeout 					= 10;
	public static long tearDownTimeout 				= 5000L;
}
