package com.zh.springboot_rabbit;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.UUID;

@SpringBootTest
class SpringbootRabbitApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;  //可以用RabbitTemplate发送一个消息


    @Test
    void contextLoads() throws IOException {
        //springboot提供了CorrelationData
        //通过CorrelationData构造唯一标识
        CorrelationData id = new CorrelationData(UUID.randomUUID().toString());

        //convertAndSend发送消息
        //指定exchange   routingKey    消息
        //测试return机制时,只用指定一个不存在的队列就行,例如RoutingKey设置为slow.xxx.dog
        rabbitTemplate.convertAndSend("boot-topic-exchange","slow.red.dog","红色的狗",id);

        System.in.read();

    }

}
