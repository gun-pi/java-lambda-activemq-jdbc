import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLogger implements LambdaLogger {

    private static final Logger LOG = LoggerFactory.getLogger(TestLogger.class);

    public void log(String message) {
        LOG.info(message);
    }

    public void log(byte[] message) {
        LOG.info(new String(message));
    }
}
