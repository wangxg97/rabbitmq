package cn.enjoydu.exchange2.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ��wangxg
 * @version ��
 * @program ��rabbitmq
 * @date ��Created in 2020/8/12 15:58
 * @description ��һ�����Ӷ���ŵ�,�����ж���ŵ���rabbitmq����ʱͨ��һ���ŵ����ӵģ�������Ϣ���յ�����
 */
public class MultiChannelConsumer {

    static class ConsumerWorker implements Runnable{
        final  Connection connection;

        ConsumerWorker(Connection connection) {
            this.connection = connection;
        }


        public void run() {
            //����һ���ŵ���ÿһ�� �̵߳���һ���ŵ�
            try {
                Channel channel = connection.createChannel();
                //�ŵ����ý���������
                channel.exchangeDeclare(DirectProducer.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
                //����һ���������
                String queue = channel.queueDeclare().getQueue();

                //���������֣���ӡ�����
                final String consumerName =  Thread.currentThread().getName()+"-all";

                /*���а󶨵���������ʱ��������󶨶��·�ɼ��ģ�Ҳ���Ƕ��ذ�*/
                String[] routekeys={"king","mark","james"};
                for(String routekey:routekeys){
                    channel.queueBind(queue,DirectProducer.EXCHANGE_NAME,
                            routekey);
                }
                System.out.println("["+consumerName+"] Waiting for messages:");

                // ��������������
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
        //���ӹ���
        ConnectionFactory factory = new ConnectionFactory();
        //����rabbitMq�ĵ�ַ
        factory.setHost("111.229.16.229");
        // �����Ӻʹ���Ƶ�����뷢�Ͷ�һ��
        Connection connection = factory.newConnection();

        //һ�����Ӷ���ŵ�
        for (int i=0;i<2;i++){
            Thread thread = new Thread(new ConsumerWorker(connection));
            thread.start();
        }
    }
}