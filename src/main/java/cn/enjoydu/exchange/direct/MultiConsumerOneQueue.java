package cn.enjoydu.exchange.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * һ�����ж�������ߣ������ֳ���Ϣ��������֮�����ѯ����
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
                //�������ӣ����ӵ�rabbitmq
                ConnectionFactory connectionFactory=new ConnectionFactory();
                //�������ӹ��������ӵ�ַ,Ĭ�϶˿�5772
                connectionFactory.setHost("111.229.16.229");
                //��������
                Connection connection = connectionFactory.newConnection();
                //�����ŵ�
                Channel channel = connection.createChannel();

                //���ŵ������ý�����(ֱ�ӽ�����)
                channel.exchangeDeclare(DirectProducer.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

                //���������֣���ӡ�����
                final String  consumerName= Thread.currentThread().getName()+ "-all";

                channel.queueDeclare(queueName,false,false,false,null);

                //���а󶨵���������ʱ��������󶨶Ը�·�ɼ��ģ�Ҳ���Ƕ��ذ�
                String[] routekeys={"king","mark","james"};
                for (String routekey : routekeys) {
                    channel.queueBind(queueName,DirectProducer.EXCHANGE_NAME,routekey);
                }
                System.out.println(" ["+consumerName+"] Waiting for message: ");


                //��������������
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
            //�������ӣ����ӵ�rabbitmq
            ConnectionFactory connectionFactory=new ConnectionFactory();
            //�������ӹ��������ӵ�ַ,Ĭ�϶˿�5772
            connectionFactory.setHost("111.229.16.229");
            //��������
            Connection connection = connectionFactory.newConnection();

            String queteName="focusAll";
            for (int i=0;i<3;i++){
                Thread thread = new Thread(new ConsumerWorker(connection, queteName));
                thread.start();
            }

        }
    }

}
