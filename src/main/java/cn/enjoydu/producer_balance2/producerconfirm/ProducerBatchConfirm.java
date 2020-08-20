package cn.enjoydu.producer_balance2.producerconfirm;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ��wangxg
 * @version ��
 * @program ��rabbitmq
 * @date ��Created in 2020/8/13 9:59
 * @description ��������-����ȷ��ģʽ
 */
public class ProducerBatchConfirm {
    public final static String EXCHANGE_NAME = "producer_wait_batch_confirm";
    private final static String ROUTE_KEY = "king";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        /**
         * �����������ӵ�RabbitMQ
         */
        ConnectionFactory factory = new ConnectionFactory();
        // ����RabbitMQ��������ip����������
        factory.setHost("111.229.16.229");
        // ����һ������
        Connection connection = factory.newConnection();
        // ����һ���ŵ�
        Channel channel = connection.createChannel();
        // ָ��ת��
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        // ���ʧ��֪ͨ������
        channel.addReturnListener(new ReturnListener() {
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body);
                System.out.println("RabbitMq���ص�replyCode:  "+replyCode);
                System.out.println("RabbitMq���ص�replyText:  "+replyText);
                System.out.println("RabbitMq���ص�exchange:  "+exchange);
                System.out.println("RabbitMq���ص�routingKey:  "+routingKey);
                System.out.println("RabbitMq���ص�message:  "+message);
            }
        });

        // ���÷�����ȷ��ģʽ
        channel.confirmSelect();

        //������־�����Լ���
        for(int i=0;i<10;i++){
            // ���͵���Ϣ
            String message = "Hello World_"+(i+1);
            //����1��exchange name
            //����2��routing key
            channel.basicPublish(EXCHANGE_NAME, ROUTE_KEY, true,null, message.getBytes());
            System.out.println(" Sent Message: [" + ROUTE_KEY +"]:'"+ message + "'");
        }

        //���÷�����ȷ��ģʽ
        channel.waitForConfirmsOrDie();
        // �ر�Ƶ��������
        channel.close();
        connection.close();

    }
}
