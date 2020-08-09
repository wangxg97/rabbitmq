package cn.enjoydu.producer_balance.mandatory;

import cn.enjoydu.exchange.direct.DirectProducer;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

//消费者指消费了king,剩余的mark  James 将会走失败通知
public class ConsumerProducerMandatory {
    public static void main(String[] args) throws IOException, TimeoutException {
        //创建链接，连接到rabbitmq
        ConnectionFactory connectionFactory=new ConnectionFactory();
        //设置连接工厂的连接地址,默认端口5772
        connectionFactory.setHost("111.229.16.229");
        //创建连接
        Connection connection = connectionFactory.newConnection();
        //创建信道
        Channel channel = connection.createChannel();

        //在信道中设置交换器(直接交换器)
        channel.exchangeDeclare(ProduceMandatory.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //声明队列（放到消费者中去做）
        String queueName="queue-king";
        channel.queueDeclare(queueName,false,false,false,null);

        //绑定，将队列（queue-king）与交换器通过 路由键 绑定
        String routeKey="king";
        channel.queueBind(queueName,ProduceMandatory.EXCHANGE_NAME,routeKey);
        System.out.println("waiting for message...");

        //申明一个消费者
        Consumer consumer=new DefaultConsumer(channel){

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String  messageage=  new String(body,"UTF-8");
                System.out.println("Received["+envelope.getRoutingKey()+"]"+messageage);
            }
        };

        //消息者正式开始在指定队列上消费
        channel.basicConsume(queueName,true,consumer);

    }
}
