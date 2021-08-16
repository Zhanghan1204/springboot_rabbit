package com.zh.springboot_rabbit.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class MyBean {

//    @JsonProperty("JsonName")
    //@SerializedName("JsonName")
    private String Name;
    private String address;
    private int age;

    public MyBean(String name, String address, int age) {
        Name = name;
        this.address = address;
        this.age = age;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "MyBean{" +
                "name='" + Name + '\'' +
                ", address='" + address + '\'' +
                ", age=" + age +
                '}';
    }


}
