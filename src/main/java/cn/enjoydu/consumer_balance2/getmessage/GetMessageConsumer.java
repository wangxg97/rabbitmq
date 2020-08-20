package cn.enjoydu.consumer_balance2.getmessage;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ：wangxg
 * @version ：消费者 -拉取模式 （循环拉取）
 * @program ：rabbitmq
 * @date ：Created in 2020/8/13 11:07
 * @description ：
 */
public class GetMessageConsumer {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("111.229.16.229");

        // 打开连接和创建频道，与发送端一样
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.exchangeDeclare(GetMessageProducer.EXCHANGE_NAME,
                "direct");
        // 声明一个队列
        String queueName = "focuserror";
        channel.queueDeclare(queueName,
                false,false,
                false,null);

        String routekey="error";//只关注error级别的日志，然后记录到文件中去。
        channel.queueBind(queueName,
                GetMessageProducer.EXCHANGE_NAME, routekey);
        System.out.println(" [*] Waiting for messages......");

        //无线循环拉取
        while (true){
            //拉一条
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