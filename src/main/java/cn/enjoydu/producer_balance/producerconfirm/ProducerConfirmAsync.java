package cn.enjoydu.producer_balance.producerconfirm;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

//������-���ͷ�ȷ��ģʽ-�첽����ȷ��
public class ProducerConfirmAsync {
    public final  static  String EXCHANGE_NAME="producer_async_confirm";
    public final  static  String ROUTE_KEY="king";

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


        //ʧ��֪ͨ �ص�  ��Ϣ����·��ʱ�ᵽ����
        channel.addReturnListener(new ReturnListener() {
            public void handleReturn(int replycode, String replyText, String exchange, String routeKey, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
                String message = new String(bytes);
                System.out.println("���ص�replycode:"+replycode);
                System.out.println("���ص�replyText:"+replyText);
                System.out.println("���ص�exchange:"+exchange);
                System.out.println("���ص�routeKey:"+routeKey);
            }
        });

        //����������ȷ��ģʽ
        channel.confirmSelect();

        //��ӷ�����ȷ�ϼ�����
        channel.addConfirmListener(new ConfirmListener() {
            //�ɹ�
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("send_ACK:"+deliveryTag+",multiple"+multiple);
            }
            //ʧ��
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
