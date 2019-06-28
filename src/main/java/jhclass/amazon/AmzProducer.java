package jhclass.amazon;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;

import javax.jms.*;

public class AmzProducer {

    public static void main(String[] args) {
        final ActiveMQConnectionFactory connectionFactory
                = new ActiveMQConnectionFactory("ssl://b-27999888-7993-43f3-a7f8-e1e2ab28ba59-1.mq.ca-central-1.amazonaws.com:61617");
        connectionFactory.setUserName("admin");
        connectionFactory.setPassword("Changeme_123");

        final PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setConnectionFactory(connectionFactory);
        pooledConnectionFactory.setMaxConnections(10);

        try{
            final Connection producerConnection = pooledConnectionFactory.createConnection();
            producerConnection.start();

            final Session producerSession = producerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            final Destination producerDestination = producerSession.createQueue("MyQueue");

            final MessageProducer producer = producerSession.createProducer(producerDestination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            final String text="Hello from Amazon MQ!";
            TextMessage producerMessage = producerSession.createTextMessage(text);

            producer.send(producerMessage);
            System.out.println("Message sent.");

            producer.close();
            producerSession.close();
            producerConnection.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
