package com.zh.springboot_rabbit.listen;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class Consumer {

    @Autowired  //StringRedisTemplate该方式就不用指定泛型了
    private StringRedisTemplate redisTemplate;

    //监听哪个队列
    @RabbitListener(queues = "boot-queue")
    public void getMessage(String msg, Channel channel, Message message) throws IOException {
        //1.设置key到Redis,先通过header获取生产者的id,herder名称必须写成
        String id  = message.getMessageProperties().getHeader("spring_returned_message_correlation");
        //setIfAbsent就是setnx
        boolean boo = redisTemplate.opsForValue().setIfAbsent(id,"0",10, TimeUnit.SECONDS);

        if(boo){
            //2.消费消息
            System.out.println("接收到的消息时:"+message);

            //3.设置key的value为1,如果想到期消除,那set时,还需将过期时间set进去
            redisTemplate.opsForValue().set(id,"1");
            //redisTemplate.opsForValue().set(id,"1",10, TimeUnit.SECONDS);

            //4.手动ACK,手动告诉rabbitMQ已经消费完了
        /*使用手动ACK的原因是:如果采用自动,等一接收到消息的时候,就会给rabbitMQ反馈消费完了,但如果方法中存在异常时,
            实际消息消费失败了,但rabbitMQ以为消费成功,造成消息的丢失,因此采用手动ACK的方式*/
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }

        //5.获取Redis的key的value,如果是1,则手动ack;如果是0,则不用做任何事
        else {
            if("1".equalsIgnoreCase(redisTemplate.opsForValue().get(id))){
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);

            }
        }
    }


}
