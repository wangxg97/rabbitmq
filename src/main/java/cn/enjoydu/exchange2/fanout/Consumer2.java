package cn.enjoydu.exchange2.fanout;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ��wangxg
 * @version ��
 * @program ��rabbitmq
 * @date ��Created in 2020/8/12 16:53
 * @description ��fanout������--��һ�������ڵ�·�ɼ�����Ȼ���Ա����ѣ�
 */
public class Consumer2 {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("111.229.16.229");


        // �����Ӻʹ���Ƶ�����뷢�Ͷ�һ��
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.exchangeDeclare(FanoutProducer.EXCHANGE_NAME,
                BuiltinExchangeType.FANOUT);
        // ����һ���������
        String queueName = channel.queueDeclare().getQueue();

        //����һ�������ڵ�·�ɼ�
        String routekey="xxx";
        channel.queueBind(queueName, FanoutProducer.EXCHANGE_NAME, routekey);

        System.out.println(" [*] Waiting for messages......");

        // ��������������
        final Consumer consumerB = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                //��¼��־���ļ���
                System.out.println( "Received ["+ envelope.getRoutingKey()
                        + "] "+message);
            }
        };

        channel.basicConsume(queueName, true, consumerB);
    }
}