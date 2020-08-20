package cn.enjoydu.exchange2.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ：wangxg
 * @version ：
 * @program ：rabbitmq
 * @date ：Created in 2020/8/12 15:44
 * @description ：生产者绑定多个路由键 ,可以接受到 king，mark,james的所有请求
 */
public class MultiBindConsumer {
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

        //声明一个随机队列
        String queueName = channel.queueDeclare().getQueue();

        //队列绑定到交换器 可以绑定多个路由键
        String[] routekeys={"king","mark","james"};
        for (String routekey : routekeys) {
            channel.queueBind(queueName,DirectProducer.EXCHANGE_NAME,routekey);
        }
        System.out.println(" [*] Waiting for messages:");

        //创建队列消费者
        final Consumer consumerA = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties
                                               properties,
                                       byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" Received "
                        + envelope.getRoutingKey() + ":'" + message
                        + "'");
            }
        };

        channel.basicConsume(queueName,true,consumerA);
    }
}