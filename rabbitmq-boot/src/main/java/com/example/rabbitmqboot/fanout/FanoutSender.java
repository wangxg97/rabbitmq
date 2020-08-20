package com.example.rabbitmqboot.fanout;

import com.example.rabbitmqboot.RmConst;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ：wangxg
 * @version ：
 * @program ：rabbitmq
 * @date ：Created in 2020/8/17 17:19
 * @description ：fanout生产者
 */
public class FanoutSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String msg){
        String sendMsg = msg +"---"+ System.currentTimeMillis();;
        System.out.println("FanoutSender : " + sendMsg);
        rabbitTemplate.convertAndSend(RmConst.EXCHANGE_FANOUT,"",msg);
    }
}