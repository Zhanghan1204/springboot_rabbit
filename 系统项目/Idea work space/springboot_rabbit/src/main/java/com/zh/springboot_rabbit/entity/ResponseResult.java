package com.zh.springboot_rabbit.entity;

import java.io.Serializable;

public class ResponseResult<T> implements Serializable {

    private String code;

    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseResult{" +
                "code='" + code + '\'' +
                ", data=" + data +
                '}';
    }
}
