package cn.enjoydu.consumer_balance.qos;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import sun.plugin2.message.Message;

import java.io.IOException;

/**
 * @author ��wangxg
 * @version ��
 * @program ��rabbitmq
 * @date ��Created in 2020/8/10 19:45
 * @description ������ȷ��-������
 */
public class BatchAckConsumer  extends DefaultConsumer {

    //����
    private  int messageCount=0;
    public BatchAckConsumer(Channel channel) {
        super(channel);
        System.out.println("����������������.....");
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        super.handleDelivery(consumerTag, envelope, properties, body);
        //����Ϣ��������
        String message = new String(body, "UTF-8");
        messageCount++;
        if (messageCount%50==0){
            //basicAck ��������  deliveryTag ����id , multiple �Ƿ�������
            this.getChannel().basicAck(envelope.getDeliveryTag(),true);
            System.out.println("���������߽������ѵ�ȷ��");
        }
        if (message.equals("stop")){
            //��������һ����Ҳ����ȷ��
            this.getChannel().basicAck(envelope.getDeliveryTag(),true);
            System.out.println("");
        }
    }
}