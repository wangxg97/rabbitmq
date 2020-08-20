package cn.enjoydu.exchange2.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ��wangxg
 * @version ��
 * @program ��rabbitmq
 * @date ��Created in 2020/8/12 16:33
 * @description ��һ������ ��������ߣ���������֮ǰ��ѯ����
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
                /*����һ���ŵ�����ζ��ÿ���̵߳���һ���ŵ�*/
                final Channel channel = connection.createChannel();
                //�ŵ����ý���������(direct)
                channel.exchangeDeclare(DirectProducer.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
                /*����һ������,rabbitmq����������Ѵ��ڣ������ظ�����*/
                channel.queueDeclare(queueName, false,false, false,null);
                //���������֣���ӡ�����
                final String consumerName =  Thread.currentThread().getName();

                /*���а󶨵���������ʱ��������󶨶��·�ɼ��ģ�Ҳ���Ƕ��ذ�*/
                String[] routekeys={"king","mark","james"};
                for(String routekey:routekeys){
                    channel.queueBind(queueName,DirectProducer.EXCHANGE_NAME,
                            routekey);
                }
                System.out.println(" ["+consumerName+"] Waiting for messages:");

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
                channel.basicConsume(queueName, true, consumerA);
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
        //TODO
        //3���̣߳��߳�֮�乲�����,һ�����ж��������
        String queueName = "focusAll";
        for(int i=0;i<3;i++){
            /*����������Ϊ���������ݸ�ÿ���߳�*/
            Thread worker =new Thread(new ConsumerWorker(connection,queueName));
            worker.start();
        }

    }
}