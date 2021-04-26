package org.example.untitled.mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;

import javax.jms.*;

public class Mq {

    private static final String WIRE_LEVEL_ENDPOINT
            = "ssl://b-34aa8105-1d5e-4de9-9a46-86ec214d2d6f-1.mq.eu-central-1.amazonaws.com:61617";

    private static final String ACTIVE_MQ_USERNAME = "activemq";

    private static final String ACTIVE_MQ_PASSWORD = "exampleexample";

    private static final String QUEUE_NAME = "queue";


    public static void sendMessage(PooledConnectionFactory pooledConnectionFactory, String message) throws JMSException {
        Connection producerConnection = null;
        Session producerSession = null;
        MessageProducer producer = null;
        try {
            producerConnection = pooledConnectionFactory.createConnection();
            producerConnection.start();

            producerSession = producerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            final Destination producerDestination = producerSession.createQueue(QUEUE_NAME);

            producer = producerSession.createProducer(producerDestination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            final TextMessage producerMessage = producerSession.createTextMessage(message);
            producer.send(producerMessage);
        } finally {
            producer.close();
            producerSession.close();
            producerConnection.close();
        }
    }

    public static String receiveMessage(ActiveMQConnectionFactory connectionFactory) throws JMSException {
        Connection consumerConnection = null;
        Session consumerSession = null;
        MessageConsumer consumer = null;
        String text;
        try {
            consumerConnection = connectionFactory.createConnection();
            consumerConnection.start();

            consumerSession = consumerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            final Destination consumerDestination = consumerSession.createQueue(QUEUE_NAME);

            consumer = consumerSession.createConsumer(consumerDestination);
            final TextMessage consumerMessage = (TextMessage) consumer.receive(1000);
            text = consumerMessage.getText();
        } finally {
            consumer.close();
            consumerSession.close();
            consumerConnection.close();
        }
        return text;
    }

    public static PooledConnectionFactory createPooledConnectionFactory(ActiveMQConnectionFactory connectionFactory) {
        final PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setConnectionFactory(connectionFactory);
        pooledConnectionFactory.setMaxConnections(10);
        return pooledConnectionFactory;
    }

    public static ActiveMQConnectionFactory createActiveMQConnectionFactory() {
        final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(WIRE_LEVEL_ENDPOINT);
        connectionFactory.setUserName(ACTIVE_MQ_USERNAME);
        connectionFactory.setPassword(ACTIVE_MQ_PASSWORD);
        return connectionFactory;
    }
}
