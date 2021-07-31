package com.macro.mall.tiny;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public enum TypeEnum {
    CREATE("1","创建任务"),
    UPDATE("2","更新任务"),
    RUNJOB("3","立即运行"),
    DELETE("4","删除任务");

    private String type;

    private String name;

    TypeEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
