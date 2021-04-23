package org.example.untitled.mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;

import javax.jms.*;

public class Mq {

    private final static String WIRE_LEVEL_ENDPOINT
            = "ssl://b-10c21e5e-3fc5-4262-90c1-4df8f2671845-1.mq.eu-central-1.amazonaws.com:61617";

    private final static String ACTIVE_MQ_USERNAME = "activemq";

    private final static String ACTIVE_MQ_PASSWORD = "exampleexample";

    private final static String QUEUE_NAME = "queue";


    public static void sendMessage(PooledConnectionFactory pooledConnectionFactory, String message) throws JMSException {
        final Connection producerConnection = pooledConnectionFactory
                .createConnection();
        producerConnection.start();

        final Session producerSession = producerConnection
                .createSession(false, Session.AUTO_ACKNOWLEDGE);

        final Destination producerDestination = producerSession
                .createQueue(QUEUE_NAME);

        // Create a producer from the session to the queue.
        final MessageProducer producer = producerSession
                .createProducer(producerDestination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        // Create a message.
        final TextMessage producerMessage = producerSession
                .createTextMessage(message);

        // Send the message.
        producer.send(producerMessage);

        // Clean up the producer.
        producer.close();
        producerSession.close();
        producerConnection.close();
    }

    public static String receiveMessage(ActiveMQConnectionFactory connectionFactory) throws JMSException {
        // Establish a connection for the consumer.
        // Note: Consumers should not use PooledConnectionFactory.
        final Connection consumerConnection = connectionFactory.createConnection();
        consumerConnection.start();

        // Create a session.
        final Session consumerSession = consumerConnection
                .createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create a queue named "MyQueue".
        final Destination consumerDestination = consumerSession
                .createQueue(QUEUE_NAME);

        // Create a message consumer from the session to the queue.
        final MessageConsumer consumer = consumerSession
                .createConsumer(consumerDestination);

        // Begin to wait for messages.
        final Message consumerMessage = consumer.receive(1000);

        // Receive the message when it arrives.
        final TextMessage consumerTextMessage = (TextMessage) consumerMessage;
        final String text = consumerTextMessage.getText();

        // Clean up the consumer.
        consumer.close();
        consumerSession.close();
        consumerConnection.close();

        return text;
    }

    public static PooledConnectionFactory createPooledConnectionFactory(ActiveMQConnectionFactory connectionFactory) {
        final PooledConnectionFactory pooledConnectionFactory =
                new PooledConnectionFactory();
        pooledConnectionFactory.setConnectionFactory(connectionFactory);
        pooledConnectionFactory.setMaxConnections(10);
        return pooledConnectionFactory;
    }

    public static ActiveMQConnectionFactory createActiveMQConnectionFactory() {
        final ActiveMQConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory(WIRE_LEVEL_ENDPOINT);
        connectionFactory.setUserName(ACTIVE_MQ_USERNAME);
        connectionFactory.setPassword(ACTIVE_MQ_PASSWORD);
        return connectionFactory;
    }
}
