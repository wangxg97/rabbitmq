package cn.enjoydu.exchange2.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ��wangxg
 * @version ��
 * @program ��rabbitmq
 * @date ��Created in 2020/8/12 15:44
 * @description �������߰󶨶��·�ɼ� ,���Խ��ܵ� king��mark,james����������
 */
public class MultiBindConsumer {
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

        //����һ���������
        String queueName = channel.queueDeclare().getQueue();

        //���а󶨵������� ���԰󶨶��·�ɼ�
        String[] routekeys={"king","mark","james"};
        for (String routekey : routekeys) {
            channel.queueBind(queueName,DirectProducer.EXCHANGE_NAME,routekey);
        }
        System.out.println(" [*] Waiting for messages:");

        //��������������
        final Consumer consumerA = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties
                                               properties,
                                       byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" Received "
                        + envelope.getRoutingKey() + ":'" + message
                        + "'");
            }
        };

        channel.basicConsume(queueName,true,consumerA);
    }
}