package cn.enjoydu.consumer_balance.qos;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import sun.plugin2.message.Message;

import java.io.IOException;

/**
 * @author ：wangxg
 * @version ：
 * @program ：rabbitmq
 * @date ：Created in 2020/8/10 19:45
 * @description ：批量确认-消费者
 */
public class BatchAckConsumer  extends DefaultConsumer {

    //计数
    private  int messageCount=0;
    public BatchAckConsumer(Channel channel) {
        super(channel);
        System.out.println("批量消费者启动了.....");
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        super.handleDelivery(consumerTag, envelope, properties, body);
        //把消息体拉出来
        String message = new String(body, "UTF-8");
        messageCount++;
        if (messageCount%50==0){
            //basicAck 两个参数  deliveryTag 代表id , multiple 是否是批量
            this.getChannel().basicAck(envelope.getDeliveryTag(),true);
            System.out.println("批量消费者进行消费的确认");
        }
        if (message.equals("stop")){
            //如果是最后一条，也进行确认
            this.getChannel().basicAck(envelope.getDeliveryTag(),true);
            System.out.println("");
        }
    }
}