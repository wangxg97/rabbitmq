package cn.enjoydu.consumer_balance2.qos;

import cn.enjoydu.exchange2.direct.DirectProducer;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ��wangxg
 * @version ��qos������ ��Ԥȡģʽ
 * @program ��rabbitmq
 * @date ��Created in 2020/8/13 12:39
 * @description ��
 */
public class QosConsumerMain {
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
        channel.queueBind(queueName, DirectProducer.EXCHANGE_NAME,routekey);

        System.out.println("waiting for message........");

        /*������һ��������*/
        final Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received["+envelope.getRoutingKey()
                        +"]"+message);
                //TODO ����ȷ��
                //channel.basicAck(envelope.getDeliveryTag(),true);
            }
        };
        //һ����ȡ��150����Ȼ��ȷ��
        channel.basicQos(150,true);
        channel.basicConsume(queueName,false,consumer);
    }
}