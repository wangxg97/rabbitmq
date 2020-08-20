package cn.enjoydu.exchange.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 一个队列多个消费者，则会表现出消息在消费者之间的轮询发送
 */
public class MultiConsumerOneQueue {

    private  static  class  ConsumerWorker implements Runnable{
        private Connection connection;
        private String queueName;

        public ConsumerWorker(Connection connection, String queueName) {
            this.connection = connection;
            this.queueName = queueName;
        }

        public void run() {

            try {
                //创建链接，连接到rabbitmq
                ConnectionFactory connectionFactory=new ConnectionFactory();
                //设置连接工厂的连接地址,默认端口5772
                connectionFactory.setHost("111.229.16.229");
                //创建连接
                Connection connection = connectionFactory.newConnection();
                //创建信道
                Channel channel = connection.createChannel();

                //在信道中设置交换器(直接交换器)
                channel.exchangeDeclare(DirectProducer.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

                //消费者名字，打印输出用
                final String  consumerName= Thread.currentThread().getName()+ "-all";

                channel.queueDeclare(queueName,false,false,false,null);

                //队列绑定到交换器上时，是允许绑定对歌路由键的，也就是多重绑定
                String[] routekeys={"king","mark","james"};
                for (String routekey : routekeys) {
                    channel.queueBind(queueName,DirectProducer.EXCHANGE_NAME,routekey);
                }
                System.out.println(" ["+consumerName+"] Waiting for message: ");


                //创建队列消费者
                DefaultConsumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        String message = new String(body, "UTF-8");
                        System.out.println(consumerName + envelope.getRoutingKey() + ":" + message);
                    }
                };

                channel.basicConsume(queueName,true,consumer);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }


        public static void main(String[] args) throws IOException, TimeoutException {
            //创建链接，连接到rabbitmq
            ConnectionFactory connectionFactory=new ConnectionFactory();
            //设置连接工厂的连接地址,默认端口5772
            connectionFactory.setHost("111.229.16.229");
            //创建连接
            Connection connection = connectionFactory.newConnection();

            String queteName="focusAll";
            for (int i=0;i<3;i++){
                Thread thread = new Thread(new ConsumerWorker(connection, queteName));
                thread.start();
            }

        }
    }

}
