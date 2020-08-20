package cn.enjoydu.consumer_balance2.getmessage;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ��wangxg
 * @version �������� -��ȡģʽ ��ѭ����ȡ��
 * @program ��rabbitmq
 * @date ��Created in 2020/8/13 11:07
 * @description ��
 */
public class GetMessageConsumer {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("111.229.16.229");

        // �����Ӻʹ���Ƶ�����뷢�Ͷ�һ��
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.exchangeDeclare(GetMessageProducer.EXCHANGE_NAME,
                "direct");
        // ����һ������
        String queueName = "focuserror";
        channel.queueDeclare(queueName,
                false,false,
                false,null);

        String routekey="error";//ֻ��עerror�������־��Ȼ���¼���ļ���ȥ��
        channel.queueBind(queueName,
                GetMessageProducer.EXCHANGE_NAME, routekey);
        System.out.println(" [*] Waiting for messages......");

        //����ѭ����ȡ
        while (true){
            //��һ��
            GetResponse getResponse = channel.basicGet(queueName, true);
            if(null!=getResponse){
                System.out.println("received["
                        +getResponse.getEnvelope().getRoutingKey()+"]"
                        +new String(getResponse.getBody()));
            }
            Thread.sleep(1000);
        }

    }
}