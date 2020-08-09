package cn.enjoydu.exchange.topic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 关注 king 所有的课程
 */
public class King_AllConsumer {
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
        channel.exchangeDeclare(TopicProducer.EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        //声明一个随机队列
        String queueName = channel.queueDeclare().getQueue();


        channel.queueBind(queueName,TopicProducer.EXCHANGE_NAME,"king.#");

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
