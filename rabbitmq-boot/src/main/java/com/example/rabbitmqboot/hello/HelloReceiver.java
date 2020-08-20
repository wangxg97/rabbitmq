package com.example.rabbitmqboot.hello;

import com.example.rabbitmqboot.RmConst;
import com.sun.prism.impl.QueuedPixelSource;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ：wangxg
 * @version ：
 * @program ：rabbitmq
 * @date ：Created in 2020/8/17 17:00
 * @description ：默认交换器 消费者
 */
@Component
@RabbitListener(queues = "sb.hello")
public class HelloReceiver {
    @RabbitHandler
    public void process2(String hello) {
        System.out.println("HelloReceiver : " + hello);
    }

}