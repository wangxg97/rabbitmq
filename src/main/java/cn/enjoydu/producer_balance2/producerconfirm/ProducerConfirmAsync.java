package cn.enjoydu.producer_balance2.producerconfirm;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ：wangxg
 * @version ：
 * @program ：rabbitmq
 * @date ：Created in 2020/8/13 10:20
 * @description ：生产者-发送者确认模式 --异步监听确认
 */
public class ProducerConfirmAsync {
    public final static String EXCHANGE_NAME = "producer_async_confirm";

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

        //启用发送者确认模式
        channel.confirmSelect();

        //添加发送者确认监听器，用于异步接受 有没有到队列的消息（就是发送者确认啦）
        channel.addConfirmListener(new ConfirmListener() {
            //成功发送到队列中
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("send_ACK:"+deliveryTag+",multiple:"+multiple);
            }
            //失败 发送到队列中
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("Error----send_NACK:"+deliveryTag+",multiple:"+multiple);
            }
        });


        // 添加失败者通知  这个是找不到路由，发送者确认模式是防止rabbitmq内部的错误导致 不能到指定队列中
        channel.addReturnListener(new ReturnListener() {
            public void handleReturn(int replyCode, String replyText,
                                     String exchange, String routingKey,
                                     AMQP.BasicProperties properties,
                                     byte[] body)
                    throws IOException {
                String message = new String(body);
                System.out.println("RabbitMq路由失败:  "+routingKey+"."+message);
            }
        });

        String[] routekeys={"king","mark"};
        //TODO 6条
        for(int i=0;i<20;i++){
            String routekey = routekeys[i%2];
            //String routekey = "king";
            // 发送的消息
            String message = "Hello World_"+(i+1)+("_"+System.currentTimeMillis());
            channel.basicPublish(EXCHANGE_NAME, routekey, true,
                    MessageProperties.PERSISTENT_BASIC, message.getBytes());
        }
    }
}