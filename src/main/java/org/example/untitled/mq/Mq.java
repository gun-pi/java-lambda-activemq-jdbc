package org.example.untitled.mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;

import javax.jms.*;

public class Mq {

    private static final String WIRE_LEVEL_ENDPOINT
            = "ssl://b-3eb87df2-ac2e-4db8-b0ba-6ce90f4f2a9e-1.mq.eu-central-1.amazonaws.com:61617";
    private static final String ACTIVE_MQ_USERNAME = "activemq";
    private static final String ACTIVE_MQ_PASSWORD = "exampleexample";
    private static final String QUEUE_NAME = "queue";

    private static final ActiveMQConnectionFactory connectionFactory;

    private static final PooledConnectionFactory pooledConnectionFactory;


    static {
        connectionFactory = new ActiveMQConnectionFactory(WIRE_LEVEL_ENDPOINT);
        connectionFactory.setUserName(ACTIVE_MQ_USERNAME);
        connectionFactory.setPassword(ACTIVE_MQ_PASSWORD);

        pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setConnectionFactory(connectionFactory);
        pooledConnectionFactory.setMaxConnections(10);
    }

    public static void sendMessage(String message) throws JMSException {
        Connection producerConnection = null;
        try {
            producerConnection = pooledConnectionFactory.createConnection();
            producerConnection.start();

            Session producerSession = producerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            final Destination producerDestination = producerSession.createQueue(QUEUE_NAME);

            MessageProducer producer = producerSession.createProducer(producerDestination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            final TextMessage producerMessage = producerSession.createTextMessage(message);
            producer.send(producerMessage);
        } finally {
            producerConnection.close();
        }
    }

    public static String receiveMessage() throws JMSException {
        Connection consumerConnection = null;
        String text;
        try {
            consumerConnection = connectionFactory.createConnection();
            consumerConnection.start();

            Session consumerSession = consumerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            final Destination consumerDestination = consumerSession.createQueue(QUEUE_NAME);

            MessageConsumer consumer = consumerSession.createConsumer(consumerDestination);
            final TextMessage consumerMessage = (TextMessage) consumer.receive(1000);
            text = consumerMessage.getText();
        } finally {
            consumerConnection.close();
        }
        return text;
    }
}
