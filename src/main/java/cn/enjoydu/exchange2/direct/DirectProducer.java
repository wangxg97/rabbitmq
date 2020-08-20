package cn.enjoydu.exchange2.direct;

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
 * @date ��Created in 2020/8/11 19:33
 * @description ����ͨ������ ֱ�ӽ�����
 */
public class DirectProducer {

    public final static String EXCHANGE_NAME="direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        //�������ӣ����ӵ�RabbitMQ
        ConnectionFactory factory = new ConnectionFactory();
        //���������ӹ��������ӵ�ַ��Ĭ�϶˿�5672��
        factory.setHost("111.229.16.229");

        //��������
        Connection connection = factory.newConnection();
        //�����ŵ�
        Channel channel = connection.createChannel();

        //���ŵ������ý�����
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);


        //����·�ɼ�/��Ϣ��
        String[] routeKeys={"king","mark","james"};
        for (int i=0;i<6;i++){
            String routeKey = routeKeys[i % 3];
            String msg="Hello,RabbitMQ"+(i+1);

            //������Ϣ
            channel.basicPublish(EXCHANGE_NAME,routeKey,null,msg.getBytes());
            System.out.println("Sent:"+routeKey+":"+msg);

        }
        channel.close();
        connection.close();
    }
}