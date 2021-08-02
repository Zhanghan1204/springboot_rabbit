1.创建SpringBoot工程  
2.导入依赖  
`<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
    <version>2.1.8.RELEASE</version>
</dependency>`  
3.编写配置文件  
`#rabbit环境
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: test
    password: test
    virtual-host: /test`  
4.编写配置类,声明exchange和queue,并绑定到一个(文件:`com.zh.springboot_rabbit.config.RabbitMQConfig`)   
5.通过RabbitTemplate发布消息到RabbitMQ(文件:`com.zh.springboot_rabbit.SpringbootRabbitApplicationTests`)
通过convertAndSend方法进行消息的发布  
6.创建消费者监听消息(文件:`com.zh.springboot_rabbit.SpringbootRabbitApplication`)
通过@RabbitListener监听队列  
  

**手动ACK**  
文件:`com.zh.springboot_rabbit.SpringbootRabbitApplication`  
在application.yml中添加  
` #指定ACK方式  
 listener:  
   simple:  
     acknowledge-mode: manual   #手动方式`  
使用手动ACK的原因是:如果采用自动,等一接收到消息的时候,就会给rabbitMQ反馈消费完了,但如果方法中存在异常时,
实际消息消费失败了,但rabbitMQ以为消费成功,造成消息的丢失,因此采用手动ACK的方式




