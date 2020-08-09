package cn.enjoydu.producer_balance.mandatory;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProduceMandatory {

    public final static  String EXCHANGE_NAME="mandatory_test";

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


        //失败通知 回调
        channel.addReturnListener(new ReturnListener() {
            public void handleReturn(int replycode, String replyText, String exchange, String routeKey, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
                String message = new String(bytes);
                System.out.println("返回的replycode:"+replycode);
                System.out.println("返回的replyText:"+replyText);
                System.out.println("返回的exchange:"+exchange);
                System.out.println("返回的routeKey:"+routeKey);
            }
        });


        //申明路由键,消息体
        String[] routeskeys={"king","mark","james"};
        for (int i=0;i<3;i++){
            String routeskey = routeskeys[i % 3];
            String msg="Hello,RabbitMQ"+(i+1);
            //发布消息,  设置为true表示失败通知
            channel.basicPublish(EXCHANGE_NAME,routeskey,true,null,msg.getBytes());
            System.out.println("Sent"+routeskey+":"+msg);
            Thread.sleep(200);
        }
        channel.close();
        connection.close();
    }
}
