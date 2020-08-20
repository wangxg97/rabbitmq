package cn.enjoydu.consumer_balance.qos;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ：wangxg
 * @version ：
 * @program ：rabbitmq
 * @date ：Created in 2020/8/10 19:18
 * @description ：qos 生产者
 */
public class QosProducer {
    public final static  String EXCHANGE_NAME="direct_logss";


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
        channel.exchangeDeclare(QosProducer.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //发送210条消息，其中第210条消息表示本批次消息的结束
        for (int i=0;i<210;i++){
            String message="Hello World_"+(i+1);
            if (i==209){
                message="stop";
            }
            channel.basicPublish(EXCHANGE_NAME,"error",null,message.getBytes());
        }
        channel.close();
        connection.close();

    }
}