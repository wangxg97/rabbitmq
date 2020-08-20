package cn.enjoydu.consumer_balance.qos;

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
 * @date ��Created in 2020/8/10 19:18
 * @description ��qos ������
 */
public class QosProducer {
    public final static  String EXCHANGE_NAME="direct_logss";


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
        channel.exchangeDeclare(QosProducer.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //����210����Ϣ�����е�210����Ϣ��ʾ��������Ϣ�Ľ���
        for (int i=0;i<210;i++){
            String message="Hello World_"+(i+1);
            if (i==209){
                message="stop";
            }
            channel.basicPublish(EXCHANGE_NAME,"error",null,message.getBytes());
        }
        channel.close();
        connection.close();

    }
}