package cn.enjoydu.exchange.fatout;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * 广播交换器，与绑定路由键无关，无论绑定的路由键是否存在在发送的路由键中，否可以到该队列中
 */
public class Consumer1{
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
        channel.exchangeDeclare(FatoutProducer.EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        //声明一个随机队列
        String queueName = channel.queueDeclare().getQueue();

        //队列绑定到交换器上时，是允许绑定对歌路由键的，也就是多重绑定
        String[] routekeys={"king","mark","james"};
        for (String routekey : routekeys) {
            channel.queueBind(queueName,FatoutProducer.EXCHANGE_NAME,routekey);
        }
        System.out.println(" [*] Waiting for message: ");


        //创建队列消费者
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received" + envelope.getRoutingKey() + ":" + message);
            }
        };

        channel.basicConsume(queueName,true,consumer);

    }
}
