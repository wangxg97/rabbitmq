package cn.enjoydu.exchange2.fanout;

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
 * @date ：Created in 2020/8/12 16:49
 * @description ：广播 生产者
 */
public class FanoutProducer {
    public final static String EXCHANGE_NAME = "fanout_logs";
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

        //指定转发
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        /*日志消息级别，作为路由键使用*/
        String[] routekeys = {"king","mark","james"};
        for(int i=0;i<3;i++) {
            String routekey = routekeys[i % 3];//每一次发送一条消息
            // 发送的消息
            String message = "Hello World_" + (i + 1);
            //参数1：exchange name
            //参数2：routing key
            channel.basicPublish(EXCHANGE_NAME, routekey,
                    null, message.getBytes());
            System.out.println(" [x] Sent '" + routekey + "':'"
                    + message + "'");
        }
        // 关闭频道和连接
        channel.close();
        connection.close();
    }

}