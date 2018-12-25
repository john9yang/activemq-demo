package jhclass.topic;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMapMessage;

public class Publisher {
    private ConnectionFactory factory;
    private Connection connection = null;
    private Session session;
    private String brokerURL = "tcp://192.168.218.128:61617";
    private MessageProducer producer;
    private Destination[] destinations;
    /**
     * 构造函数 创建连接、创建生产者
     *
     * @throws JMSException
     */
    public Publisher() throws JMSException {
        factory = new ActiveMQConnectionFactory(brokerURL);
        connection = factory.createConnection();
        try {
            connection.start();
        } catch (JMSException jmse) {
            connection.close();
            throw jmse;
        }
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        producer = session.createProducer(null);
    }
    /**
     * 设置目的地
     *
     * @param stocks
     *            ：主题名列表
     * @throws JMSException
     */
    protected void setTopics(String[] stocks) throws JMSException {
        destinations = new Destination[stocks.length];
        for (int i = 0; i < stocks.length; i++) {
            destinations[i] = session.createTopic("STOCKS." + stocks[i]);
        }
    }
    /**
     * 创建消息
     *
     * @param stock
     *            :主题名
     * @param session
     * @return
     * @throws JMSException
     */
    protected Message createStockMessage(String stock, Session session)
            throws JMSException {
        MapMessage message = session.createMapMessage();
        message.setString("stock", stock);
        message.setDouble("price", 1.00);
        message.setDouble("offer", 0.01);
        message.setBoolean("up", true);
        return message;
    }

    /**
     * 发送消息
     * 遍历所有主题（目的地），向每个目的地分别发送一个MapMessage
     * @param stocks
     *            ：主题名
     * @throws JMSException
     */
    protected void sendMessage(String[] stocks) throws JMSException {
        //遍历所有主题
        for (int i = 0; i < stocks.length; i++) {
            // 创建消息
            Message message = createStockMessage(stocks[i], session);

            System.out.println("发送: "
                    + ((ActiveMQMapMessage) message).getContentMap()
                    + " on destination: " + destinations[i]);

            // 往目的地发送消息
            producer.send(destinations[i], message);
        }
    }
    public void close() throws JMSException {
        try {
            if (null != connection)
                connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] argss) throws JMSException {
        String[] topics = { "MyTopic1", "MyTopic2", "MyTopic3", "MyTopic4" };
        Publisher publisher = new Publisher();
        publisher.setTopics(topics);
        //每隔1s发送一次消息，共发送5次消息
        for (int i = 0; i < 5; i++) {
            System.out.println("发布者第：" + i + " 次发布消息...");
            publisher.sendMessage(topics);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        publisher.close();
    }
}