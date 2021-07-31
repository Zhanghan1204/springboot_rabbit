package com.macro.mall.tiny.task.impl;

import com.macro.mall.tiny.task.Task;

public class Taskimpl implements Task {
    @Override
    public void execute(String data) {
        System.out.println("定时任务中具体的任务详情:"+data);
    }
}
