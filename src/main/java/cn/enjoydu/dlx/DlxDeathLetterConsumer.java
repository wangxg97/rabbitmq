package cn.enjoydu.dlx;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ��wangxg
 * @version ��
 * @program ��rabbitmq
 * @date ��Created in 2020/8/17 10:26
 * @description ������������
 */
public class DlxDeathLetterConsumer {
    public final static String DLX_EXCHANGE_NAME = "dlx_accept";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("111.229.16.229");

        // �����Ӻʹ���Ƶ�����뷢�Ͷ�һ��
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        channel.exchangeDeclare(DLX_EXCHANGE_NAME,
                BuiltinExchangeType.TOPIC);

        /*����һ������*/
        String queueName = "dlx_accept";
        channel.queueDeclare(queueName,false,false,
                false,null);

        /*�󶨣������кͽ�����ͨ��·�ɼ����а�*/
        channel.queueBind(queueName,
                DLX_EXCHANGE_NAME,"kind");

        System.out.println("waiting for message........");

        /*������һ������������*/
        final Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received dead letter["
                        +envelope.getRoutingKey()
                        +"]"+message);
            }
        };
        /*��������ʽ��ʼ��ָ��������������Ϣ*/
        channel.basicConsume(queueName,true,consumer);


    }
}