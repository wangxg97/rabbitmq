package cn.enjoydu.exchange.fatout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 使用广播交换器
 */
public class FatoutProducer {

    public final static  String EXCHANGE_NAME="fatout_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建链接，连接到rabbitmq
        ConnectionFactory connectionFactory=new ConnectionFactory();
        //设置连接工厂的连接地址,默认端口5772
        connectionFactory.setHost("111.229.16.229");
        //创建连接
        Connection connection = connectionFactory.newConnection();
        //创建信道
        Channel channel = connection.createChannel();

        //在信道中设置交换器(广播交换器)
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        //声明队列（放到消费者中去做）

        //申明路由键,消息体
        String[] routeskeys={"king","mark","james"};
        for (int i=0;i<3;i++){
            String routeskey = routeskeys[i % 3];
            String msg="Hello,RabbitMQ"+(i+1);
            //发布消息
            channel.basicPublish(EXCHANGE_NAME,routeskey,null,msg.getBytes());
            System.out.println("Sent"+routeskey+":"+msg);
        }
        channel.close();
        connection.close();
    }
}