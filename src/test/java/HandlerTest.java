import com.amazonaws.services.lambda.runtime.Context;
import org.example.untitled.Handler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HandlerTest {

    @Test
    void test() {
        Handler handler = new Handler();
        Context context = new TestContext();
        final String id = handler.handleRequest("test", context);
        Assertions.assertTrue(id.matches("\\d+"));
    }
}

