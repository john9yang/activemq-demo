package jhclass.topic;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ConsumerAsyn {
    private ActiveMQConnectionFactory factory;
    private String brokerURL = "tcp://192.168.218.128:61617";
    private Connection connection = null;
    private Session session;

    public ConsumerAsyn() throws JMSException {
        factory = new ActiveMQConnectionFactory(brokerURL);
        connection = factory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }
    public Session getSession() {
        return session;
    }
    public static void main(String[] args) throws JMSException {h
        String[] topics = { "MyTopic1", "MyTopic2", "MyTopic3", "MyTopic4" };
        ConsumerAsyn consumer = new ConsumerAsyn();
        for (String stock : topics) {
            //创建目的地
            Destination destination = consumer.getSession().createTopic(
                    "STOCKS." + stock);

            //创建消费者
            MessageConsumer messageConsumer = consumer.getSession()
                    .createConsumer(destination);
            MessageConsumer messageConsumer2 = consumer.getSession()
                    .createConsumer(destination);

            //设置监听器
            messageConsumer.setMessageListener(new Listener());
            messageConsumer2.setMessageListener(new Listener());
        }
    }
}
