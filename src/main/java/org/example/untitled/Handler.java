package org.example.untitled;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.example.untitled.db.DocumentDao;
import org.example.untitled.db.DocumentEntity;
import org.example.untitled.mq.Mq;

public class Handler implements RequestHandler<String, String> {

    @Override
    public String handleRequest(final String input, final Context context) {
        final LambdaLogger LOG = context.getLogger();
        LOG.log("Request handler is starting with input string: " + input);

        String message;
        try {
            Mq.sendMessage(input);
            message = Mq.receiveMessage();
        } catch (Exception e) {
            LOG.log("Exception in handleRequest method occurred during JMS interaction: " + e);
            throw new RuntimeException(e);
        }

        Long id;
        try {
            id = DocumentDao.save(new DocumentEntity(message));
        } catch (Exception e) {
            LOG.log("Exception in handleRequest method occurred during Database interaction: " + e);
            throw new RuntimeException(e);
        }

        LOG.log("Request handler with id " + id + " is finishing with message: " + message);
        return id.toString();
    }
}
