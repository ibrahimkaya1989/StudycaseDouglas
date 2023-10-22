package listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;

import java.util.Map;

public class ParameterListener implements ISuiteListener {

    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Override
    public void onStart(ISuite suite){
        Map<String, String> parameters = suite.getXmlSuite().getParameters();
        for (Map.Entry<String, String> parameter : parameters.entrySet()) {
            String env = System.getenv(parameter.getKey());
            if (env != null && ! env.trim().isEmpty()) {
                parameter.setValue(env);
                logger.info( "Suite on start | Key: " + parameter.getKey() + " Value: " + parameter.getValue() );
            }
        }
    }

    @Override
    public void onFinish(ISuite suite) {
        // do something...
    }
}
