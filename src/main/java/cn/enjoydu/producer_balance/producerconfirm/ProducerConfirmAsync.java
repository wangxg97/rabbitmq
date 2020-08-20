package cn.enjoydu.producer_balance.producerconfirm;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

//生产者-发送方确认模式-异步监听确认
public class ProducerConfirmAsync {
    public final  static  String EXCHANGE_NAME="producer_async_confirm";
    public final  static  String ROUTE_KEY="king";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //创建链接，连接到rabbitmq
        ConnectionFactory connectionFactory=new ConnectionFactory();
        //设置连接工厂的连接地址,默认端口5772
        connectionFactory.setHost("111.229.16.229");
        //创建连接
        Connection connection = connectionFactory.newConnection();
        //创建信道
        Channel channel = connection.createChannel();

        //在信道中设置交换器(直接交换器)
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);


        //失败通知 回调  消息不可路由时会到这里
        channel.addReturnListener(new ReturnListener() {
            public void handleReturn(int replycode, String replyText, String exchange, String routeKey, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
                String message = new String(bytes);
                System.out.println("返回的replycode:"+replycode);
                System.out.println("返回的replyText:"+replyText);
                System.out.println("返回的exchange:"+exchange);
                System.out.println("返回的routeKey:"+routeKey);
            }
        });

        //启动发送者确认模式
        channel.confirmSelect();

        //添加发送者确认监听器
        channel.addConfirmListener(new ConfirmListener() {
            //成功
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("send_ACK:"+deliveryTag+",multiple"+multiple);
            }
            //失败
            public void handleNack(long l, boolean b) throws IOException {

            }
        });

        for (int i=0;i<2;i++){
            String message="Hello World_"+(i+1);
            channel.basicPublish(EXCHANGE_NAME,ROUTE_KEY,true,null,message.getBytes());
            System.out.println("Sent Message:["+ROUTE_KEY+"]:"+message);

        }


        channel.close();
        connection.close();
    }
}
