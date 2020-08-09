package cn.enjoydu.exchange.fatout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ʹ�ù㲥������
 */
public class FatoutProducer {

    public final static  String EXCHANGE_NAME="fatout_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        //�������ӣ����ӵ�rabbitmq
        ConnectionFactory connectionFactory=new ConnectionFactory();
        //�������ӹ��������ӵ�ַ,Ĭ�϶˿�5772
        connectionFactory.setHost("111.229.16.229");
        //��������
        Connection connection = connectionFactory.newConnection();
        //�����ŵ�
        Channel channel = connection.createChannel();

        //���ŵ������ý�����(�㲥������)
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        //�������У��ŵ���������ȥ����

        //����·�ɼ�,��Ϣ��
        String[] routeskeys={"king","mark","james"};
        for (int i=0;i<3;i++){
            String routeskey = routeskeys[i % 3];
            String msg="Hello,RabbitMQ"+(i+1);
            //������Ϣ
            channel.basicPublish(EXCHANGE_NAME,routeskey,null,msg.getBytes());
            System.out.println("Sent"+routeskey+":"+msg);
        }
        channel.close();
        connection.close();
    }
}