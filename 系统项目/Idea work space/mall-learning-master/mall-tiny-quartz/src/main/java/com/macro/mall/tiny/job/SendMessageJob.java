package com.macro.mall.tiny.job;

import com.macro.mall.tiny.service.ScheduleService;
import com.macro.mall.tiny.task.Task;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * 发送站内信定时任务执行器
 * Created by macro on 2020/9/27.
 */
@Slf4j
@Component
public class SendMessageJob extends QuartzJobBean {
    @Autowired
    private ScheduleService scheduleService;

    //定时任务,重写executeInternal方法
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Trigger trigger = jobExecutionContext.getTrigger();
        JobDetail jobDetail = jobExecutionContext.getJobDetail();

        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        String data = jobDataMap.getString("data");
        log.info("定时发送站内信操作：{}",data);


        //完成后删除触发器和任务
        scheduleService.cancelScheduleJob(trigger.getKey().getName());
    }
}
