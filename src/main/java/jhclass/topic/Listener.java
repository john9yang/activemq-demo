package jhclass.topic;

import java.text.DecimalFormat;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

public class Listener implements MessageListener {

    /**
     * 异步接收
     * 当有消息时，就会触发该事件
     */
    public void onMessage(Message message) {
        try {
            MapMessage map = (MapMessage)message;
            String stock = map.getString("stock");
            double price = map.getDouble("price");
            double offer = map.getDouble("offer");
            boolean up = map.getBoolean("up");

            DecimalFormat df = new DecimalFormat( "#,###,###,##0.00" );
            System.out.println("接收消息："+stock + "\t" + df.format(price) + "\t" + df.format(offer) + "\t" + (up?"up":"down"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

