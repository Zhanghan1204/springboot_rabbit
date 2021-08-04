package com.zh.springboot_rabbit.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

//因为要实现接口,所以使用component,而不是用Configuration
//想让confirm和return生效,只用加上这个配置类就可以
@Component
public class ConfirmAndReturnConfig implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback{

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //init-method
    @PostConstruct
    public void initMethod(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    //confirm机制,确认消息从生产者发送到exchange
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        if (b){
            System.out.println("消息已经送到exchange!");
        }else {
            System.out.println("消息未送到exchange!");
        }
    }

    //return机制,确认消息从exchange发送到queue,发送不成功时,回调该函数
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        System.out.println("消息未送到queue");

    }

}
