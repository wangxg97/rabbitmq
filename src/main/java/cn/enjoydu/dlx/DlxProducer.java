package cn.enjoydu.dlx;

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
 * @date ：Created in 2020/8/17 10:11
 * @description ：死信队列实现延时订单  生产者
 */
public class DlxProducer {
    public final  static  String EXCHANGE_NAME="dlx_make";

    public static void main(String[] args) throws IOException, TimeoutException {
        /**
         * 创建连接连接到MabbitMQ
         */
        ConnectionFactory factory = new ConnectionFactory();
        // 设置MabbitMQ所在主机ip或者主机名
        factory.setHost("111.229.16.229");

        // 创建一个连接
        Connection connection = factory.newConnection();
        // 创建一个信道
        Channel channel = connection.createChannel();
        // 指定转发
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        String[] routekeys = {"king","mark","james"};
        for (int i=0;i<3;i++){
            String routekey = routekeys[i % 3];
            String msg = "Hellol,RabbitMq"+(i+1);
            System.out.println(routekey+" "+msg);
            channel.basicPublish(EXCHANGE_NAME,routekey,null,msg.getBytes());
        }
        // 关闭频道和连接
        channel.close();
        connection.close();
    }
}