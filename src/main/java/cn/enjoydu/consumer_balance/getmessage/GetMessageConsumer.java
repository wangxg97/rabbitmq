package cn.enjoydu.consumer_balance.getmessage;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ��wangxg
 * @version ��1.0.0
 * @program ��rabbitmq
 * @date ��Created in 2020/8/10 18:28
 * @description ����ȡ ������
 */
public class GetMessageConsumer {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("111.229.16.229");

        //�����Ӻʹ����ŵ����뷢�Ͷ�һ��
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(GetMessageProducer.EXCHANGE_NAME,"direct");

        //����һ������
        String queueName="focuserror";
        channel.queueDeclare(queueName,false,false,false,null);

        String routekey="error";
        channel.queueBind(queueName,GetMessageProducer.EXCHANGE_NAME,routekey);

        //����ѭ����ȡ
        while (true){
            // true ��һ�����Զ�ȷ�ϵ�rabbitmq,��Ϊ������Ϣ���ѾʹӶ�����ɾ��
            GetResponse getResponse = channel.basicGet(queueName, true);
            if (null !=getResponse){
                System.out.println("received["+getResponse.getEnvelope().getRoutingKey()+"]"
                +new String(getResponse.getBody()));
            }
            Thread.sleep(1000);
        }
    }
}