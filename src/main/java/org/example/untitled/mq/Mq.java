package org.example.untitled.mq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Mq {

    private static final String BROKER_USERNAME = "activemq";
    private static final String BROKER_PASSWORD = "exampleexample";
    private static final String BROKER_URL =
            "ssl://b-fdbb2ab1-acd4-4450-92d1-0a25c9c2eb97-1.mq.eu-central-1.amazonaws.com:61617";
    private static final String BROKER_QUEUE = "queue";

    private static final ActiveMQConnectionFactory connectionFactory;

    static {
        connectionFactory = new ActiveMQConnectionFactory(BROKER_USERNAME, BROKER_PASSWORD, BROKER_URL);
    }

    public static void sendMessage(String message) throws JMSException {
        Connection producerConnection = null;
        try {
            producerConnection = connectionFactory.createConnection();
            producerConnection.start();

            Session producerSession = producerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            final Destination producerDestination = producerSession.createQueue(BROKER_QUEUE);

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
            final Destination consumerDestination = consumerSession.createQueue(BROKER_QUEUE);

            MessageConsumer consumer = consumerSession.createConsumer(consumerDestination);
            final TextMessage consumerMessage = (TextMessage) consumer.receive(1000);
            text = consumerMessage.getText();
        } finally {
            consumerConnection.close();
        }
        return text;
    }
}
