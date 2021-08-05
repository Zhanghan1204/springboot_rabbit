package com.zh.springboot_rabbit.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    //1.创建exchange  采用topic方式  名字任意
    @Bean
    public TopicExchange getTopicExcheng(){
        return new TopicExchange("boot-topic-exchange",true,false);
    }

    //2.创建一个队列queue     如果需要多个队列时,在此处创建多个,然后方法和队列名称不要一样
    @Bean
    public Queue getQueue() {
        return new Queue("boot-queue",true,false,false,null);
    }


    //3.绑定在一起
    @Bean
    public Binding getBinding(TopicExchange topicExchange,Queue queue){
        //绑定一个队列到一个exchange 采用哪种routingKey
        //注意格式为:xxxx.xxxx.xxxx   其中一个*代表一个xxxx,而一个#可以代表多个xxxx.xxxx
        return BindingBuilder.bind(queue).to(topicExchange).with("*.red.*");
    }




}
