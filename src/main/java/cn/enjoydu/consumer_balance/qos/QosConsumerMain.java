package cn.enjoydu.consumer_balance.qos;

import cn.enjoydu.consumer_balance.getmessage.GetMessageProducer;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ��wangxg
 * @version ��
 * @program ��rabbitmq
 * @date ��Created in 2020/8/10 19:18
 * @description ��qos ������
 */
public class QosConsumerMain {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("111.229.16.229");

        //�����Ӻʹ����ŵ����뷢�Ͷ�һ��
        Connection connection = connectionFactory.newConnection();
        final Channel channel = connection.createChannel();

        channel.exchangeDeclare(QosProducer.EXCHANGE_NAME,"direct");

        //����һ������
        String queueName="focuserror";
        channel.queueDeclare(queueName,false,false,false,null);

        String routekey="error";
        channel.queueBind(queueName,QosProducer.EXCHANGE_NAME,routekey);


        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received[" + envelope.getRoutingKey() + "]" + message);
                //����ȷ��
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        //150��Ԥȡ (150��ȡ����)
        channel.basicQos(150,true);
    //����
        channel.basicConsume(queueName,false,consumer);
    }
}