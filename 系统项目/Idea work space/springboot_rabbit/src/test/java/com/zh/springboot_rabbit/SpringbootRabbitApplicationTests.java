package com.zh.springboot_rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.zh.springboot_rabbit.entity.MyBean;
import com.zh.springboot_rabbit.entity.ResponseResult;
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


   /* @Test
    void contextLoads() throws IOException {
        //springboot提供了CorrelationData
        //通过CorrelationData构造唯一标识
        CorrelationData id = new CorrelationData(UUID.randomUUID().toString());

        //convertAndSend发送消息
        //指定exchange   routingKey    消息
        //测试return机制时,只用指定一个不存在的队列就行,例如RoutingKey设置为slow.xxx.dog
        rabbitTemplate.convertAndSend("boot-topic-exchange","slow.red.dog","红色的狗",id);

        System.in.read();

    }*/

    /*@Test
    public void testJsonProperty() throws IOException{
        String jsonBean1 = "{\"JsonName\":\"xyz\",\"age\":\"12\",\"address\":\"china\"}";
        ObjectMapper mapper = new ObjectMapper();
        Object myBean1 = mapper.readerFor(MyBean.class).readValue(jsonBean1);
        ResponseResult<MyBean> res = new ResponseResult<>();
        res.setCode("1");
        res.setData((MyBean) myBean1);
        //System.err.println("Convert json to java: "+ myBean1.toString());
        System.out.println(res.getData());
        MyBean myBean = new MyBean("xyz", "China", 12);
        System.err.println("Conver java to json: " + mapper.writeValueAsString(myBean));
    }*/

    @Test
    public void testFromJeson() throws IOException {
        String jsonBean1 = "{\"Name\":\"xyz\",\"age\":\"12\",\"address\":\"china\"}";
        MyBean myBean = new MyBean("xyz", "China", 12);

        // 注意：写法和@JsonProperty不同
        Gson gson = new Gson();
        MyBean myBean1 = gson.fromJson(jsonBean1, MyBean.class);
        ResponseResult<MyBean> res = new ResponseResult<>();
        res.setCode("1");
        res.setData(myBean1);
//        System.err.println("jesonBean1: "+ myBean1.toString());
        System.out.println(res.getData());
        String toJson = gson.toJson(myBean);
        System.err.println("toJson: " + toJson);
    }


}
