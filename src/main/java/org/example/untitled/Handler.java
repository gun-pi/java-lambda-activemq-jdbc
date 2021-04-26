package org.example.untitled;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.example.untitled.db.DocumentDao;
import org.example.untitled.db.DocumentEntity;
import org.example.untitled.mq.Mq;

public class Handler implements RequestHandler<String, String> {

    private static final DocumentDao documentDao = new DocumentDao();

    private static final ActiveMQConnectionFactory connectionFactory = Mq.createActiveMQConnectionFactory();

    private static final PooledConnectionFactory pooledConnectionFactory
            = Mq.createPooledConnectionFactory(connectionFactory);

    @Override
    public String handleRequest(final String input, final Context context) {
        final LambdaLogger LOG = context.getLogger();
        LOG.log("Request handler is starting with input string: " + input);

        String message = null;
        try {
            Mq.sendMessage(pooledConnectionFactory, input);
            message = Mq.receiveMessage(connectionFactory);
        } catch (Exception e) {
            LOG.log("Exception in handleRequest method occurred during JMS interaction: " + e);
            throw new RuntimeException(e);
        }

        Long id = null;
        try {
            id = documentDao.save(new DocumentEntity(message));
        } catch (Exception e) {
            LOG.log("Exception in handleRequest method occurred during Database interaction: " + e);
            throw new RuntimeException(e);
        }

        LOG.log("Request handler with id " + id + " is finishing with message: " + message);
        return id.toString();
    }
}
