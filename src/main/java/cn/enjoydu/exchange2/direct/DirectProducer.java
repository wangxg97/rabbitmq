package cn.enjoydu.exchange2.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ：wangxg
 * @version ：
 * @program ：rabbitmq
 * @date ：Created in 2020/8/11 19:33
 * @description ：普通生产者 直接交换器
 */
public class DirectProducer {

    public final static String EXCHANGE_NAME="direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建链接，链接到RabbitMQ
        ConnectionFactory factory = new ConnectionFactory();
        //设置下链接工厂的链接地址（默认端口5672）
        factory.setHost("111.229.16.229");

        //创建链接
        Connection connection = factory.newConnection();
        //创建信道
        Channel channel = connection.createChannel();

        //在信道中设置交换器
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);


        //申明路由键/消息体
        String[] routeKeys={"king","mark","james"};
        for (int i=0;i<6;i++){
            String routeKey = routeKeys[i % 3];
            String msg="Hello,RabbitMQ"+(i+1);

            //发布消息
            channel.basicPublish(EXCHANGE_NAME,routeKey,null,msg.getBytes());
            System.out.println("Sent:"+routeKey+":"+msg);

        }
        channel.close();
        connection.close();
    }
}