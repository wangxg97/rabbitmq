package cn.enjoydu.exchange2.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ：wangxg
 * @version ：普通的消费者
 * @program ：rabbitmq
 * @date ：Created in 2020/8/11 19:47
 * @description ：
 */
public class NormalConsumer {
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
        channel.exchangeDeclare(DirectProducer.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //申明队列
        String queueName="queue-king";
        channel.queueDeclare(queueName,false,false,false,null);

        //绑定
        String routeKey="king";
        channel.queueBind(queueName,DirectProducer.EXCHANGE_NAME,routeKey);
        System.out.println("waiting for message ......");

        //申明一个消费者
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received[" + envelope.getRoutingKey() + "]" + message);
            }
        };

        //消费者在指定队列上消费
        channel.basicConsume(queueName,true,consumer);
    }
}