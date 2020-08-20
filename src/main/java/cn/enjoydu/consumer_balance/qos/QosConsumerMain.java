package cn.enjoydu.consumer_balance.qos;

import cn.enjoydu.consumer_balance.getmessage.GetMessageProducer;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ：wangxg
 * @version ：
 * @program ：rabbitmq
 * @date ：Created in 2020/8/10 19:18
 * @description ：qos 消费者
 */
public class QosConsumerMain {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("111.229.16.229");

        //打开链接和创建信道，与发送端一样
        Connection connection = connectionFactory.newConnection();
        final Channel channel = connection.createChannel();

        channel.exchangeDeclare(QosProducer.EXCHANGE_NAME,"direct");

        //申明一个队列
        String queueName="focuserror";
        channel.queueDeclare(queueName,false,false,false,null);

        String routekey="error";
        channel.queueBind(queueName,QosProducer.EXCHANGE_NAME,routekey);


        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received[" + envelope.getRoutingKey() + "]" + message);
                //单条确认
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        //150跳预取 (150都取出来)
        channel.basicQos(150,true);
    //消费
        channel.basicConsume(queueName,false,consumer);
    }
}