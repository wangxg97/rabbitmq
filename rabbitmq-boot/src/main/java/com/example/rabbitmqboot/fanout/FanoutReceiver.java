package com.example.rabbitmqboot.fanout;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author ：wangxg
 * @version ：
 * @program ：rabbitmq
 * @date ：Created in 2020/8/17 17:21
 * @description ：fanout消费者
 */
@Component
@RabbitListener(queues = "sb.fanout.A")
public class FanoutReceiver {
    @RabbitHandler
    public void process(String hello) {
        System.out.println("FanoutReceiver : " + hello);
    }
}