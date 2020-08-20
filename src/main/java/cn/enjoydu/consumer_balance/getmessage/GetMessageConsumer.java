package cn.enjoydu.consumer_balance.getmessage;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ：wangxg
 * @version ：1.0.0
 * @program ：rabbitmq
 * @date ：Created in 2020/8/10 18:28
 * @description ：拉取 消费者
 */
public class GetMessageConsumer {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("111.229.16.229");

        //打开链接和创建信道，与发送端一样
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(GetMessageProducer.EXCHANGE_NAME,"direct");

        //申明一个队列
        String queueName="focuserror";
        channel.queueDeclare(queueName,false,false,false,null);

        String routekey="error";
        channel.queueBind(queueName,GetMessageProducer.EXCHANGE_NAME,routekey);

        //无限循环拉取
        while (true){
            // true 拉一条，自动确认的rabbitmq,认为这条消息消费就从队列中删除
            GetResponse getResponse = channel.basicGet(queueName, true);
            if (null !=getResponse){
                System.out.println("received["+getResponse.getEnvelope().getRoutingKey()+"]"
                +new String(getResponse.getBody()));
            }
            Thread.sleep(1000);
        }
    }
}