package cn.enjoydu.producer_balance2.producerconfirm;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ��wangxg
 * @version ��
 * @program ��rabbitmq
 * @date ��Created in 2020/8/13 9:45
 * @description ��������-���ͷ�ȷ��ģʽ-����ȷ��
 */
public class ProducerConfirm {
    public final static String EXCHANGE_NAME = "producer_confirm";
    private final static String ROUTE_KEY = "king";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        /**
         * �����������ӵ�RabbitMQ
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

        //���ʧ��֪ͨ������
        channel.addReturnListener(new ReturnListener() {
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body);
                System.out.println("���ص�replycode:"+replyCode);
                System.out.println("���ص�replyText:"+replyText);
                System.out.println("���ص�exchange:"+exchange);
                System.out.println("���ص�routeKey:"+routingKey);
            }
        });


        //����������ȷ��ģʽ
        channel.confirmSelect();

        //������Ϣ
        for (int i=0;i<2;i++){
            // ���͵���Ϣ
            String message = "Hello World_"+(i+1);

            channel.basicPublish(EXCHANGE_NAME,ROUTE_KEY,null,message.getBytes());
            System.out.println(" Sent Message: [" + ROUTE_KEY +"]:'"+ message + "'");

            //ȷ���Ƿ�ɹ� ����ȷ��
            if (channel.waitForConfirms()){
                System.out.println("send success");
            }else{
                System.out.println("send failure");
            }
        }
    }

}