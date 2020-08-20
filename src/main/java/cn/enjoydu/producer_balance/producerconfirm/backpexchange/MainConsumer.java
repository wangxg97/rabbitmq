package cn.enjoydu.producer_balance.producerconfirm.backpexchange;

import cn.enjoydu.producer_balance.producerconfirm.ProducerConfirmAsync;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ��wangxg
 * @version ��1.0.0
 * @program ��rabbitmq
 * @date ��Created in 2020/8/10 17:13
 * @description ������������
 */
public class MainConsumer {


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
        channel.exchangeDeclare(ProducerConfirmAsync.EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        //�������У��ŵ���������ȥ����
        String queueName="fatchother";
        channel.queueDeclare(queueName,false,false,false,null);

        //�󶨣������У�queue-king���뽻����ͨ�� ·�ɼ� ��
        channel.queueBind(queueName,ProducerConfirmAsync.EXCHANGE_NAME,"#");
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