package jhclass.amazon;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class AmzConsumer {
    public static void main(String[] args) {
        final ActiveMQConnectionFactory connectionFactory
                = new ActiveMQConnectionFactory("ssl://b-27999888-7993-43f3-a7f8-e1e2ab28ba59-1.mq.ca-central-1.amazonaws.com:61617");
        connectionFactory.setUserName("admin");
        connectionFactory.setPassword("Changeme_123");


        try{
            // Establish a connection for the consumer.
            final Connection consumerConnection = connectionFactory.createConnection();
            consumerConnection.start();

            // Create a session.
            final Session consumerSession = consumerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create a queue named "MyQueue".
            final Destination consumerDestination = consumerSession.createQueue("MyQueue");

            // Create a message consumer from the session to the queue.
            final MessageConsumer consumer = consumerSession.createConsumer(consumerDestination);

            // Begin to wait for messages.
            final Message consumerMessage = consumer.receive(1000);

            // Receive the message when it arrives.
            final TextMessage consumerTextMessage = (TextMessage) consumerMessage;
            System.out.println("Message received: " + consumerTextMessage.getText());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
