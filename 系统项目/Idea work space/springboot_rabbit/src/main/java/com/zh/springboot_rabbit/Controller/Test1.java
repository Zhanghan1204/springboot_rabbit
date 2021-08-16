package com.zh.springboot_rabbit.Controller;


import com.zh.springboot_rabbit.entity.ResponseResult;
import com.zh.springboot_rabbit.entity.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test1/")
public class Test1 {

    @PostMapping("getName")
    public void getName(){


    }

    public static ResponseResult<User> m1(){
        User user = new User();
        user.setPassword("123");
        user.setUsername("abc");
        ResponseResult<User>  responseResult = new ResponseResult<>();
        responseResult.setCode("1");
        responseResult.setData(user);
        System.out.println(responseResult);
        return responseResult;

    }


    public static void main(String[] args) {
        ResponseResult<User>  responseResult = Test1.m1();
        System.out.println(responseResult);
        User user = responseResult.getData();
        System.out.println(user);
        System.out.println(user.getUsername());

    }

}
