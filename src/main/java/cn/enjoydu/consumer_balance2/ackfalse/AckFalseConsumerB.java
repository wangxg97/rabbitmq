package cn.enjoydu.consumer_balance2.ackfalse;

import cn.enjoydu.exchange2.direct.DirectProducer;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ��wangxg
 * @version ����Ϣ�߶���Ϣ����ȷ�ϣ��ֶ�ȷ�ϣ�
 * @program ��rabbitmq
 * @date ��Created in 2020/8/13 11:46
 * @description ��
 */
public class AckFalseConsumerB {

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

        //����һ��������
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received[" + envelope.getRoutingKey()
                        + "]" + message);

                //��Ϣȷ��
                System.out.println("�ֶ�ȷ�ϵ�tag:" + envelope.getDeliveryTag());
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };

        //������ ��ʽ��ʼ����ָ�������ϵ�������Ϣ
        channel.basicConsume(queueName,false,consumer);
    }
}