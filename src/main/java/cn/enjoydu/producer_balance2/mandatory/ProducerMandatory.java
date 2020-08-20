package cn.enjoydu.producer_balance2.mandatory;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ��wangxg
 * @version ��������-ʧ��ȷ��ģʽ  ������mandatory Ϊtrue,channel.addConfirmListener ���������� RabbitMQ ���ص���Ϣ��
 * @program ��rabbitmq
 * @date ��Created in 2020/8/12 17:18
 * @description ��
 */
public class ProducerMandatory {
    public final static String EXCHANGE_NAME = "mandatory_test";


    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        /**
         * �����������ӵ�RabbitMQ
         */
        ConnectionFactory factory = new ConnectionFactory();

        // ����MabbitMQ��������ip���������� rabbitMQ ���ڱ������û�������
        factory.setHost("111.229.16.229");
        // ����һ������
        Connection connection = factory.newConnection();
        // ����һ���ŵ�
        Channel channel = connection.createChannel();
        // ָ��Direct������
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //ʧ��֪ͨ���ص�
        channel.addReturnListener(new ReturnListener() {
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body);
                System.out.println("���ص�replycode:"+replyCode);
                System.out.println("���ص�replyText:"+replyText);
                System.out.println("���ص�exchange:"+exchange);
                System.out.println("���ص�routeKey:"+routingKey);
            }
        });

        String[] routekeys={"king","mark","james"};
        for(int i=0;i<3;i++){
            String routekey = routekeys[i%3];
            // ���͵���Ϣ
            String message = "Hello World_"+(i+1)
                    +("_"+System.currentTimeMillis());
            //TODO mandatory ����Ϊtrue ,�ڷ�����Ϣʱ���� mandatory ��־������ RabbitMQ�������Ϣ����·�ɣ�Ӧ�ý���Ϣ���ظ������ߣ���֪ͨʧ��
            channel.basicPublish(EXCHANGE_NAME,routekey,true,null,message.getBytes());
            System.out.println("----------------------------------");
            System.out.println(" Sent Message: [" + routekey +"]:'"
                    + message + "'");
            Thread.sleep(200);
        }

        // �ر�Ƶ��������
        channel.close();
        connection.close();
    }
}