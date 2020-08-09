package cn.enjoydu.producer_balance.mandatory;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProduceMandatory {

    public final static  String EXCHANGE_NAME="mandatory_test";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //�������ӣ����ӵ�rabbitmq
        ConnectionFactory connectionFactory=new ConnectionFactory();
        //�������ӹ��������ӵ�ַ,Ĭ�϶˿�5772
        connectionFactory.setHost("111.229.16.229");
        //��������
        Connection connection = connectionFactory.newConnection();
        //�����ŵ�
        Channel channel = connection.createChannel();

        //���ŵ������ý�����(ֱ�ӽ�����)
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);


        //ʧ��֪ͨ �ص�
        channel.addReturnListener(new ReturnListener() {
            public void handleReturn(int replycode, String replyText, String exchange, String routeKey, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
                String message = new String(bytes);
                System.out.println("���ص�replycode:"+replycode);
                System.out.println("���ص�replyText:"+replyText);
                System.out.println("���ص�exchange:"+exchange);
                System.out.println("���ص�routeKey:"+routeKey);
            }
        });


        //����·�ɼ�,��Ϣ��
        String[] routeskeys={"king","mark","james"};
        for (int i=0;i<3;i++){
            String routeskey = routeskeys[i % 3];
            String msg="Hello,RabbitMQ"+(i+1);
            //������Ϣ,  ����Ϊtrue��ʾʧ��֪ͨ
            channel.basicPublish(EXCHANGE_NAME,routeskey,true,null,msg.getBytes());
            System.out.println("Sent"+routeskey+":"+msg);
            Thread.sleep(200);
        }
        channel.close();
        connection.close();
    }
}
