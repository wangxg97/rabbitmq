package cn.enjoydu.exchange.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * һ������������԰󶨶��·�ɼ�
 */
public class MultiBindConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        //�������ӣ����ӵ�rabbitmq
        ConnectionFactory connectionFactory=new ConnectionFactory();
        //�������ӹ��������ӵ�ַ,Ĭ�϶˿�5772
        connectionFactory.setHost("111.229.16.229");
        //��������
        Connection connection = connectionFactory.newConnection();
        //�����ŵ�
        Channel channel = connection.createChannel();

        //���ŵ������ý�����(ֱ�ӽ�����)
        channel.exchangeDeclare(DirectProducer.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //����һ���������
        String queueName = channel.queueDeclare().getQueue();

        //���а󶨵���������ʱ��������󶨶Ը�·�ɼ��ģ�Ҳ���Ƕ��ذ�
        String[] routekeys={"king","mark","james"};
        for (String routekey : routekeys) {
            channel.queueBind(queueName,DirectProducer.EXCHANGE_NAME,routekey);
        }
        System.out.println(" [*] Waiting for message: ");


        //��������������
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received" + envelope.getRoutingKey() + ":" + message);
            }
        };

        channel.basicConsume(queueName,true,consumer);

    }
}
