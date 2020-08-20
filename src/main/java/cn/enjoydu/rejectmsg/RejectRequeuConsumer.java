package cn.enjoydu.rejectmsg;

import cn.enjoydu.exchange2.direct.DirectProducer;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ：wangxg
 * @version ：
 * @program ：rabbitmq
 * @date ：Created in 2020/8/14 16:47
 * @description ：拒绝消息的消费者
 */
public class RejectRequeuConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("111.229.16.229");

        // 打开连接和创建频道，与发送端一样
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        channel.exchangeDeclare(DirectProducer.EXCHANGE_NAME,
                "direct");

        /*声明一个队列*/
        String queueName = "focuserror";
        channel.queueDeclare(queueName,false,false,
                false,null);

        /*绑定，将队列和交换器通过路由键进行绑定*/
        String routekey = "error";/*表示只关注error级别的日志消息*/
        channel.queueBind(queueName,DirectProducer.EXCHANGE_NAME,routekey);

        System.out.println("waiting for message........");

        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    String message = new String(body, "UTF-8");
                    System.out.println("Received["
                            + envelope.getRoutingKey()
                            + "]" + message);
                    throw new RuntimeException("处理异常" + message);
                } catch (Exception e) {
                    e.printStackTrace();
                    //Reject 方式拒绝（这里第二个参数决定是否重新投递）
                    //channel.basicReject(envelope.getDeliveryTag(),true);

                    //nack 方式的拒绝（第二个参数决定是否批量）
                    channel.basicNack(envelope.getDeliveryTag(), false, true);
                }

            }
        };
        channel.basicConsume(queueName,false,consumer);


    }
}