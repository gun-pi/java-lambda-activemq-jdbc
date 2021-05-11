package org.example.untitled;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.example.untitled.db.DocumentDao;
import org.example.untitled.db.DocumentEntity;
import org.example.untitled.mq.Mq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Handler implements RequestHandler<String, String> {

    private static final Logger LOG = LoggerFactory.getLogger(Handler.class);

    static {
        try {
            Class.forName(Mq.class.getName());
        } catch (Exception e) {
            LOG.error("Exception occurred during initialization: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String handleRequest(final String input, final Context context) {
        LOG.info("Request handler is starting with input string: " + input);

        String message;
        try {
            Mq.sendMessage(input);
            message = Mq.receiveMessage();
        } catch (Exception e) {
            LOG.error("Exception in handleRequest method occurred during JMS interaction: " + e);
            throw new RuntimeException(e);
        }

        Long id;
        try {
            id = DocumentDao.save(new DocumentEntity(message));
        } catch (Exception e) {
            LOG.error("Exception in handleRequest method occurred during Database interaction: " + e);
            throw new RuntimeException(e);
        }

        LOG.info("Request handler with id " + id + " is finishing with message: " + message);
        return id.toString();
    }
}
