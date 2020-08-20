package cn.enjoydu.exchange2.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ：wangxg
 * @version ：
 * @program ：rabbitmq
 * @date ：Created in 2020/8/12 15:58
 * @description ：一个链接多个信道,由于有多个信道，rabbitmq链接时通过一个信道链接的，所以消息会收到两遍
 */
public class MultiChannelConsumer {

    static class ConsumerWorker implements Runnable{
        final  Connection connection;

        ConsumerWorker(Connection connection) {
            this.connection = connection;
        }


        public void run() {
            //创建一个信道，每一个 线程单独一个信道
            try {
                Channel channel = connection.createChannel();
                //信道设置交换器类型
                channel.exchangeDeclare(DirectProducer.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
                //声明一个随机队列
                String queue = channel.queueDeclare().getQueue();

                //消费者名字，打印输出用
                final String consumerName =  Thread.currentThread().getName()+"-all";

                /*队列绑定到交换器上时，是允许绑定多个路由键的，也就是多重绑定*/
                String[] routekeys={"king","mark","james"};
                for(String routekey:routekeys){
                    channel.queueBind(queue,DirectProducer.EXCHANGE_NAME,
                            routekey);
                }
                System.out.println("["+consumerName+"] Waiting for messages:");

                // 创建队列消费者
                final Consumer consumerA = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag,
                                               Envelope envelope,
                                               AMQP.BasicProperties
                                                       properties,
                                               byte[] body)
                            throws IOException {
                        String message =
                                new String(body, "UTF-8");
                        System.out.println(consumerName
                                +" Received "  + envelope.getRoutingKey()
                                + ":'" + message + "'");
                    }
                };
                channel.basicConsume(queue, true, consumerA);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



    public static void main(String[] args) throws IOException, TimeoutException {
        //连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //连接rabbitMq的地址
        factory.setHost("111.229.16.229");
        // 打开连接和创建频道，与发送端一样
        Connection connection = factory.newConnection();

        //一个链接多个信道
        for (int i=0;i<2;i++){
            Thread thread = new Thread(new ConsumerWorker(connection));
            thread.start();
        }
    }
}