package cn.enjoydu.exchange.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class NormalConsumer {
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

        //�������У��ŵ���������ȥ����
        String queueName="queue-king";
        channel.queueDeclare(queueName,false,false,false,null);

        //�󶨣������У�queue-king���뽻����ͨ�� ·�ɼ� ��
        String routeKey="king";
        channel.queueBind(queueName,DirectProducer.EXCHANGE_NAME,routeKey);
        System.out.println("waiting for message...");

        //����һ��������
        Consumer consumer=new DefaultConsumer(channel){

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String  messageage=  new String(body,"UTF-8");
                System.out.println("Received["+envelope.getRoutingKey()+"]"+messageage);
            }
        };

        //��Ϣ����ʽ��ʼ��ָ������������
        channel.basicConsume(queueName,true,consumer);

    }
}
