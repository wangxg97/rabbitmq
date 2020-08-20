package cn.enjoydu.exchange2.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ：wangxg
 * @version ：
 * @program ：rabbitmq
 * @date ：Created in 2020/8/12 16:33
 * @description ：一个队列 多个消费者，在消费者之前轮询发送
 */
public class MultiConsumerOneQueue {


    static class ConsumerWorker implements  Runnable{

        final Connection connection;
        final String queueName;

        ConsumerWorker(Connection connection, String queueName) {
            this.connection = connection;
            this.queueName = queueName;
        }


        public void run() {
            try {
                /*创建一个信道，意味着每个线程单独一个信道*/
                final Channel channel = connection.createChannel();
                //信道设置交换器类型(direct)
                channel.exchangeDeclare(DirectProducer.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
                /*声明一个队列,rabbitmq，如果队列已存在，不会重复创建*/
                channel.queueDeclare(queueName, false,false, false,null);
                //消费者名字，打印输出用
                final String consumerName =  Thread.currentThread().getName();

                /*队列绑定到交换器上时，是允许绑定多个路由键的，也就是多重绑定*/
                String[] routekeys={"king","mark","james"};
                for(String routekey:routekeys){
                    channel.queueBind(queueName,DirectProducer.EXCHANGE_NAME,
                            routekey);
                }
                System.out.println(" ["+consumerName+"] Waiting for messages:");

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
                channel.basicConsume(queueName, true, consumerA);
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
        //TODO
        //3个线程，线程之间共享队列,一个队列多个消费者
        String queueName = "focusAll";
        for(int i=0;i<3;i++){
            /*将队列名作为参数，传递给每个线程*/
            Thread worker =new Thread(new ConsumerWorker(connection,queueName));
            worker.start();
        }

    }
}