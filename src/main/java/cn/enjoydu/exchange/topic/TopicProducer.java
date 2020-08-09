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
        //�������ӣ����ӵ�rabbitmq
        ConnectionFactory connectionFactory=new ConnectionFactory();
        //�������ӹ��������ӵ�ַ,Ĭ�϶˿�5772
        connectionFactory.setHost("111.229.16.229");
        //��������
        Connection connection = connectionFactory.newConnection();
        //�����ŵ�
        Channel channel = connection.createChannel();

        //���ŵ������ý�����(�㲥������)
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);


        //���ǵĿγ̣�·�ɼ����ո�ʽ�����ڣ�king.kafka.A
        String[] teachers={"king","mark","james"};
        for (int i=0;i<3;i++){
            String[] modules={"kafka","jvm","redis"};
            for (int j=0;j<3;j++){
                String[] servers={"A","B","C"};
                for (int k=0;k<3;k++){
                    //���͵���Ϣ
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
