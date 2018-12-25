package jhclass.helloworld;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Consumer {

    //默认连接用户名
    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
    //默认连接密码
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
    //默认连接地址
    private static final String BROKER_URL = ActiveMQConnection.DEFAULT_BROKER_URL;

    public static void main(String[] args) {
        //连接工厂
        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory(USERNAME, PASSWORD,"failover://tcp://192.168.218.128:61617");
        Connection connection = null;
        Session session = null;
        try {
            //连接
            connection = connectionFactory.createConnection();
            //启动连接
            connection.start();
            //创建session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //消息目的地
            Destination destination = session.createQueue("helloworldQueue");
            //消息消费者
            MessageConsumer consumer = session.createConsumer(destination);
            while (true) {
                TextMessage message = (TextMessage) consumer.receive(3000);
                if (message != null) {
                    System.out.println("接收到消息： " + message.getText());
                } else {
                    break;
                }
            }

            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        finally {

        }
    }

}