package cn.enjoydu.exchange2.fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ��wangxg
 * @version ��
 * @program ��rabbitmq
 * @date ��Created in 2020/8/12 16:49
 * @description ���㲥 ������
 */
public class FanoutProducer {
    public final static String EXCHANGE_NAME = "fanout_logs";
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

        //ָ��ת��
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        /*��־��Ϣ������Ϊ·�ɼ�ʹ��*/
        String[] routekeys = {"king","mark","james"};
        for(int i=0;i<3;i++) {
            String routekey = routekeys[i % 3];//ÿһ�η���һ����Ϣ
            // ���͵���Ϣ
            String message = "Hello World_" + (i + 1);
            //����1��exchange name
            //����2��routing key
            channel.basicPublish(EXCHANGE_NAME, routekey,
                    null, message.getBytes());
            System.out.println(" [x] Sent '" + routekey + "':'"
                    + message + "'");
        }
        // �ر�Ƶ��������
        channel.close();
        connection.close();
    }

}