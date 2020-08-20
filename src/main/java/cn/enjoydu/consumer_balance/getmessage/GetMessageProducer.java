package cn.enjoydu.consumer_balance.getmessage;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ��wangxg
 * @version ��1.0.0
 * @program ��rabbitmq
 * @date ��Created in 2020/8/10 18:22
 * @description ����ȡ ������
 */
public class GetMessageProducer {

    public final static  String EXCHANGE_NAME="direct_logs";

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
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //�������У��ŵ���������ȥ����

        //����·�ɼ�,��Ϣ��
        for (int i=0;i<3;i++){
           //���͵���Ϣ
            String message="Hello World_"+(i+1);
            channel.basicPublish(EXCHANGE_NAME,"error",null,message.getBytes());
        }
        channel.close();
        connection.close();
    }
}