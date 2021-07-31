package com.macro.mall.tiny.service;

import org.quartz.Job;
import org.quartz.SchedulerException;

import java.util.Date;

/**
 * Quartz定时任务操作类
 * Created by macro on 2020/9/27.
 */
public interface ScheduleService {
    /**
     * 通过CRON表达式调度任务
     */
    String scheduleJob(Class<? extends Job> jobBeanClass, String cron, String data,String job_id) throws SchedulerException;

    /**
     * 调度指定时间的任务
     */
    String scheduleFixTimeJob(Class<? extends Job> jobBeanClass, Date startTime, String data, String job_id, String type);

    /**
     * 取消定时任务
     */
    Boolean cancelScheduleJob(String jobName);
}
