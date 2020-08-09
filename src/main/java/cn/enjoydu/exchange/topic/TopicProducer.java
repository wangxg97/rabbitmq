package cn.enjoydu.exchange.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TopicProducer {

    public final static  String EXCHANGE_NAME="topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建链接，连接到rabbitmq
        ConnectionFactory connectionFactory=new ConnectionFactory();
        //设置连接工厂的连接地址,默认端口5772
        connectionFactory.setHost("111.229.16.229");
        //创建连接
        Connection connection = connectionFactory.newConnection();
        //创建信道
        Channel channel = connection.createChannel();

        //在信道中设置交换器(广播交换器)
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);


        //我们的课程，路由键最终格式类似于，king.kafka.A
        String[] teachers={"king","mark","james"};
        for (int i=0;i<3;i++){
            String[] modules={"kafka","jvm","redis"};
            for (int j=0;j<3;j++){
                String[] servers={"A","B","C"};
                for (int k=0;k<3;k++){
                    //发送的消息
                    String message="Hello Topic_["+i+","+j+","+k+"]";
                    String routekey = teachers[i % 3] + "." + modules[j % 3] + "." + servers[k % 3];
                    channel.basicPublish(EXCHANGE_NAME,routekey,null,message.getBytes());
                    System.out.println("[x] Sent '"+routekey+":'"+message+"'");
                }
            }
        }

        channel.close();
        connection.close();
    }

}
