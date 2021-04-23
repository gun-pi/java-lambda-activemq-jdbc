package org.example.untitled;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.example.untitled.db.DocumentDao;
import org.example.untitled.db.DocumentEntity;
import org.example.untitled.mq.Mq;

import javax.jms.JMSException;

public class Handler implements RequestHandler<String, String> {

    private static final DocumentDao documentDao = new DocumentDao();
    private static final ActiveMQConnectionFactory connectionFactory = Mq.createActiveMQConnectionFactory();
    private static final PooledConnectionFactory pooledConnectionFactory
            = Mq.createPooledConnectionFactory(connectionFactory);

    @Override
    public String handleRequest(final String input, final Context context) {

        final LambdaLogger logger = context.getLogger();
        logger.log(input + " is starting its path");

        String message = null;
        try {
            Mq.sendMessage(pooledConnectionFactory, input);
            message = Mq.receiveMessage(connectionFactory);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        //pooledConnectionFactory.stop();

        final Long id = documentDao.save(new DocumentEntity(message));

        logger.log(message + " is finishing its path");
        return id.toString();
    }
}
