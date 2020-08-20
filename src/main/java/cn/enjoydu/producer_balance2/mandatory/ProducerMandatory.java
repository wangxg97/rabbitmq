package cn.enjoydu.producer_balance2.mandatory;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ：wangxg
 * @version ：生产者-失败确认模式  ，开启mandatory 为true,channel.addConfirmListener 则用来监听 RabbitMQ 发回的信息。
 * @program ：rabbitmq
 * @date ：Created in 2020/8/12 17:18
 * @description ：
 */
public class ProducerMandatory {
    public final static String EXCHANGE_NAME = "mandatory_test";


    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        /**
         * 创建连接连接到RabbitMQ
         */
        ConnectionFactory factory = new ConnectionFactory();

        // 设置MabbitMQ所在主机ip或者主机名 rabbitMQ 是在本机，用户、密码
        factory.setHost("111.229.16.229");
        // 创建一个连接
        Connection connection = factory.newConnection();
        // 创建一个信道
        Channel channel = connection.createChannel();
        // 指定Direct交换器
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //失败通知，回调
        channel.addReturnListener(new ReturnListener() {
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body);
                System.out.println("返回的replycode:"+replyCode);
                System.out.println("返回的replyText:"+replyText);
                System.out.println("返回的exchange:"+exchange);
                System.out.println("返回的routeKey:"+routingKey);
            }
        });

        String[] routekeys={"king","mark","james"};
        for(int i=0;i<3;i++){
            String routekey = routekeys[i%3];
            // 发送的消息
            String message = "Hello World_"+(i+1)
                    +("_"+System.currentTimeMillis());
            //TODO mandatory 设置为true ,在发送消息时设置 mandatory 标志，告诉 RabbitMQ，如果消息不可路由，应该将消息返回给发送者，并通知失败
            channel.basicPublish(EXCHANGE_NAME,routekey,true,null,message.getBytes());
            System.out.println("----------------------------------");
            System.out.println(" Sent Message: [" + routekey +"]:'"
                    + message + "'");
            Thread.sleep(200);
        }

        // 关闭频道和连接
        channel.close();
        connection.close();
    }
}