package cn.enjoydu.dlx;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author ：wangxg
 * @version ：
 * @program ：rabbitmq
 * @date ：Created in 2020/8/17 10:21
 * @description ：死信队列实现延时订单  消费者
 */
public class DlxMakeConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("111.229.16.229");

        // 打开连接和创建频道，与发送端一样
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        channel.exchangeDeclare(DlxProducer.EXCHANGE_NAME,
                BuiltinExchangeType.DIRECT);

        //绑定死信交换器
        String queueName="dlx_queue";
        Map<String,Object> arg=new HashMap<String, Object>();
        arg.put("x-dead-letter-exchange",DlxDeathLetterConsumer.DLX_EXCHANGE_NAME);
        arg.put("x-message-ttl",5*1000);
        channel.queueDeclare(queueName,false,false,false,arg);


        //绑定
        channel.queueBind(queueName,DlxProducer.EXCHANGE_NAME,"king");

        System.out.println("waiting for message........");

        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(message);
                //channel.basicAck(envelope.getDeliveryTag(),  false);
                channel.basicNack(envelope.getDeliveryTag(),false,false);
            }
        };
        /*消费者正式开始在指定队列上消费消息*/
        channel.basicConsume(queueName,false,consumer);

    }
}