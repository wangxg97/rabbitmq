package cn.enjoydu.producer_balance2.producerconfirm;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ��wangxg
 * @version ��
 * @program ��rabbitmq
 * @date ��Created in 2020/8/13 10:20
 * @description ��������-������ȷ��ģʽ --�첽����ȷ��
 */
public class ProducerConfirmAsync {
    public final static String EXCHANGE_NAME = "producer_async_confirm";

    public static void main(String[] args) throws IOException, TimeoutException {
        /**
         * �����������ӵ�MabbitMQ
         */
        ConnectionFactory factory = new ConnectionFactory();

        // ����MabbitMQ��������ip����������
        factory.setHost("111.229.16.229");
        // ����һ������
        Connection connection = factory.newConnection();
        // ����һ���ŵ�
        Channel channel = connection.createChannel();
        // ָ��ת��
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //���÷�����ȷ��ģʽ
        channel.confirmSelect();

        //��ӷ�����ȷ�ϼ������������첽���� ��û�е����е���Ϣ�����Ƿ�����ȷ������
        channel.addConfirmListener(new ConfirmListener() {
            //�ɹ����͵�������
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("send_ACK:"+deliveryTag+",multiple:"+multiple);
            }
            //ʧ�� ���͵�������
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("Error----send_NACK:"+deliveryTag+",multiple:"+multiple);
            }
        });


        // ���ʧ����֪ͨ  ������Ҳ���·�ɣ�������ȷ��ģʽ�Ƿ�ֹrabbitmq�ڲ��Ĵ����� ���ܵ�ָ��������
        channel.addReturnListener(new ReturnListener() {
            public void handleReturn(int replyCode, String replyText,
                                     String exchange, String routingKey,
                                     AMQP.BasicProperties properties,
                                     byte[] body)
                    throws IOException {
                String message = new String(body);
                System.out.println("RabbitMq·��ʧ��:  "+routingKey+"."+message);
            }
        });

        String[] routekeys={"king","mark"};
        //TODO 6��
        for(int i=0;i<20;i++){
            String routekey = routekeys[i%2];
            //String routekey = "king";
            // ���͵���Ϣ
            String message = "Hello World_"+(i+1)+("_"+System.currentTimeMillis());
            channel.basicPublish(EXCHANGE_NAME, routekey, true,
                    MessageProperties.PERSISTENT_BASIC, message.getBytes());
        }
    }
}