package cn.enjoydu.dlx;

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
 * @date ��Created in 2020/8/17 10:11
 * @description �����Ŷ���ʵ����ʱ����  ������
 */
public class DlxProducer {
    public final  static  String EXCHANGE_NAME="dlx_make";

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

        String[] routekeys = {"king","mark","james"};
        for (int i=0;i<3;i++){
            String routekey = routekeys[i % 3];
            String msg = "Hellol,RabbitMq"+(i+1);
            System.out.println(routekey+" "+msg);
            channel.basicPublish(EXCHANGE_NAME,routekey,null,msg.getBytes());
        }
        // �ر�Ƶ��������
        channel.close();
        connection.close();
    }
}