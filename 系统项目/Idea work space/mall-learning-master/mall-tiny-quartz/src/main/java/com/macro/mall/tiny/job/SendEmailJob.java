package com.macro.mall.tiny.job;

import com.macro.mall.tiny.service.ScheduleService;
import com.macro.mall.tiny.task.Task;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * 发送邮件定时任务执行器
 * Created by macro on 2020/9/27.
 */
@Slf4j
@Component
public class SendEmailJob extends QuartzJobBean {
    @Autowired
    private ScheduleService scheduleService;

    //定时任务,重写executeInternal方法
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Trigger trigger = jobExecutionContext.getTrigger();
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        //根据key获取任务类,必须通过该函数获取
        String taskImpl = (String)jobExecutionContext.getMergedJobDataMap().get("JOB_CONTEXT");
        String data = jobDataMap.getString("data");
        log.info("定时发送邮件操作：{}",data);

        try {
            //java反射,根据类名获取接口
            Class<Task> task = (Class<Task>)Class.forName(taskImpl);
            Task ta = task.newInstance();//创建类的实例
            ta.execute(data);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        //完成后删除触发器和任务
       // scheduleService.cancelScheduleJob(trigger.getKey().getName());
    }
}
