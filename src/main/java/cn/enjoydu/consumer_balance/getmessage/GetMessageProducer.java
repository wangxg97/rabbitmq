package cn.enjoydu.consumer_balance.getmessage;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ：wangxg
 * @version ：1.0.0
 * @program ：rabbitmq
 * @date ：Created in 2020/8/10 18:22
 * @description ：拉取 生产者
 */
public class GetMessageProducer {

    public final static  String EXCHANGE_NAME="direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建链接，连接到rabbitmq
        ConnectionFactory connectionFactory=new ConnectionFactory();
        //设置连接工厂的连接地址,默认端口5772
        connectionFactory.setHost("111.229.16.229");
        //创建连接
        Connection connection = connectionFactory.newConnection();
        //创建信道
        Channel channel = connection.createChannel();

        //在信道中设置交换器(直接交换器)
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //声明队列（放到消费者中去做）

        //申明路由键,消息体
        for (int i=0;i<3;i++){
           //发送的消息
            String message="Hello World_"+(i+1);
            channel.basicPublish(EXCHANGE_NAME,"error",null,message.getBytes());
        }
        channel.close();
        connection.close();
    }
}