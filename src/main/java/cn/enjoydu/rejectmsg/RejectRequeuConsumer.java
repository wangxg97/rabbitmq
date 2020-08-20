package cn.enjoydu.rejectmsg;

import cn.enjoydu.exchange2.direct.DirectProducer;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ��wangxg
 * @version ��
 * @program ��rabbitmq
 * @date ��Created in 2020/8/14 16:47
 * @description ���ܾ���Ϣ��������
 */
public class RejectRequeuConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("111.229.16.229");

        // �����Ӻʹ���Ƶ�����뷢�Ͷ�һ��
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        channel.exchangeDeclare(DirectProducer.EXCHANGE_NAME,
                "direct");

        /*����һ������*/
        String queueName = "focuserror";
        channel.queueDeclare(queueName,false,false,
                false,null);

        /*�󶨣������кͽ�����ͨ��·�ɼ����а�*/
        String routekey = "error";/*��ʾֻ��עerror�������־��Ϣ*/
        channel.queueBind(queueName,DirectProducer.EXCHANGE_NAME,routekey);

        System.out.println("waiting for message........");

        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    String message = new String(body, "UTF-8");
                    System.out.println("Received["
                            + envelope.getRoutingKey()
                            + "]" + message);
                    throw new RuntimeException("�����쳣" + message);
                } catch (Exception e) {
                    e.printStackTrace();
                    //Reject ��ʽ�ܾ�������ڶ������������Ƿ�����Ͷ�ݣ�
                    //channel.basicReject(envelope.getDeliveryTag(),true);

                    //nack ��ʽ�ľܾ����ڶ������������Ƿ�������
                    channel.basicNack(envelope.getDeliveryTag(), false, true);
                }

            }
        };
        channel.basicConsume(queueName,false,consumer);


    }
}