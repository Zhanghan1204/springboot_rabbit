package com.zh.springboot_rabbit.listen;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Consumer {

    //监听哪个队列
    @RabbitListener(queues = "boot-queue")
    public void getMessage(String msg, Channel channel, Message message) throws IOException {

        System.out.println("接收到的消息时:"+message);
        //手动ACK,手动告诉rabbitMQ已经消费完了
        /*使用手动ACK的原因是:如果采用自动,等一接收到消息的时候,就会给rabbitMQ反馈消费完了,但如果方法中存在异常时,
            实际消息消费失败了,但rabbitMQ以为消费成功,造成消息的丢失,因此采用手动ACK的方式*/
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);

    }


}
