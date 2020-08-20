package cn.enjoydu.producer_balance2.producerconfirm;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ：wangxg
 * @version ：
 * @program ：rabbitmq
 * @date ：Created in 2020/8/13 9:45
 * @description ：生产者-发送放确认模式-单条确认
 */
public class ProducerConfirm {
    public final static String EXCHANGE_NAME = "producer_confirm";
    private final static String ROUTE_KEY = "king";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        /**
         * 创建连接连接到RabbitMQ
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

        //添加失败通知监听器
        channel.addReturnListener(new ReturnListener() {
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body);
                System.out.println("返回的replycode:"+replyCode);
                System.out.println("返回的replyText:"+replyText);
                System.out.println("返回的exchange:"+exchange);
                System.out.println("返回的routeKey:"+routingKey);
            }
        });


        //启动发送者确认模式
        channel.confirmSelect();

        //发送消息
        for (int i=0;i<2;i++){
            // 发送的消息
            String message = "Hello World_"+(i+1);

            channel.basicPublish(EXCHANGE_NAME,ROUTE_KEY,null,message.getBytes());
            System.out.println(" Sent Message: [" + ROUTE_KEY +"]:'"+ message + "'");

            //确认是否成功 单次确认
            if (channel.waitForConfirms()){
                System.out.println("send success");
            }else{
                System.out.println("send failure");
            }
        }
    }

}