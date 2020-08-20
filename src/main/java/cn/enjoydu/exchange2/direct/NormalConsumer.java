package cn.enjoydu.exchange2.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ��wangxg
 * @version ����ͨ��������
 * @program ��rabbitmq
 * @date ��Created in 2020/8/11 19:47
 * @description ��
 */
public class NormalConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        //�������ӣ����ӵ�RabbitMQ
        ConnectionFactory factory = new ConnectionFactory();
        //���������ӹ��������ӵ�ַ��Ĭ�϶˿�5672��
        factory.setHost("111.229.16.229");

        //��������
        Connection connection = factory.newConnection();
        //�����ŵ�
        Channel channel = connection.createChannel();

        //���ŵ������ý�����
        channel.exchangeDeclare(DirectProducer.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //��������
        String queueName="queue-king";
        channel.queueDeclare(queueName,false,false,false,null);

        //��
        String routeKey="king";
        channel.queueBind(queueName,DirectProducer.EXCHANGE_NAME,routeKey);
        System.out.println("waiting for message ......");

        //����һ��������
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received[" + envelope.getRoutingKey() + "]" + message);
            }
        };

        //��������ָ������������
        channel.basicConsume(queueName,true,consumer);
    }
}