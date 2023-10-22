package listeners;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

public class TestFilterListener implements IMethodInterceptor {

    private final Logger logger = LogManager.getLogger(this.getClass().getName());
    private static Set<Pattern> patterns;

    private boolean includeTest ( String testsToInclude, String currentTestName ) {

        boolean result = false;
        if ( patterns == null ) {
            patterns = new HashSet<>();
            String[] testPatterns = testsToInclude.split(",");
            for ( String testPattern:testPatterns ) {
                patterns.add(Pattern.compile(testPattern, Pattern.CASE_INSENSITIVE));
            }
        }

        for ( Pattern pattern:patterns ) {
            if( pattern.matcher(currentTestName).find() ) {
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {

        String testNames = System.getenv("test_names_to_include"); //test_names_to_include
        logger.info("Test names : " + testNames);
        if ( testNames == null || testNames.trim().isEmpty() ) {
            return methods;
        } else {
            if ( includeTest(testNames.trim(), context.getName()) ) {
                return methods;
            } else {
                return new ArrayList<IMethodInstance>();
            }
        }
    }
}
