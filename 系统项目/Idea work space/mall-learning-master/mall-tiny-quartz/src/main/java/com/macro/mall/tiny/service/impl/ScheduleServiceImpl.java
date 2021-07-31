package com.macro.mall.tiny.service.impl;

import cn.hutool.core.date.DateUtil;
import com.macro.mall.tiny.TypeEnum;
import com.macro.mall.tiny.listener.TaskListener;
import com.macro.mall.tiny.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.KeyMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.soap.SAAJMetaFactory;
import java.util.Date;

/**
 * Quartz定时任务操作实现类
 * Created by macro on 2020/9/27.
 */
@Slf4j
@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private Scheduler scheduler;
    private String defaultGroup = "default_group";

    private String JOB_NAME = "job_";



    public TriggerKey getTrigger_name(String job_name,String defaultGroup){
        return TriggerKey.triggerKey(job_name,defaultGroup);
    }

    public JobKey getJob_name(String job_name,String defaultGroup) {
        return JobKey.jobKey(job_name,defaultGroup);
    }

    //根据任务名称获取scheduler中的触发器
    public  CronTrigger getCronTrigger(String job_name,String defaultGroup) throws SchedulerException {
        return (CronTrigger) scheduler.getTrigger(getTrigger_name(job_name,defaultGroup));
    }

    @Override
    public String scheduleFixTimeJob(Class<? extends Job> jobBeanClass, Date startTime, String data, String job_id, String type) {
        String returnData = null;
        //日期转CRON表达式
        /*String startCron = String.format("%d %d %d %d %d ? %d",
                DateUtil.second(startTime),
                DateUtil.minute(startTime),
                DateUtil.hour(startTime, true),
                DateUtil.dayOfMonth(startTime),
                DateUtil.month(startTime) + 1,
                DateUtil.year(startTime));*/
        //每天到点执行
        String startCron = String.format("%d %d %d",
                DateUtil.second(startTime),
                DateUtil.minute(startTime),
                DateUtil.hour(startTime, true)) +" * * ?";

        System.out.println("cron表达式:"+startCron);
        if(TypeEnum.CREATE.getType().equals(type)){
            returnData = scheduleJob(jobBeanClass, startCron, data,job_id);
        }else if(TypeEnum.UPDATE.getType().equals(type)){
            returnData = updatescheduleJob(jobBeanClass, startCron, data,job_id);
        }else if(TypeEnum.DELETE.getType().equals(type)){
            returnData = String.valueOf(cancelScheduleJob(job_id));
        }else if(TypeEnum.RUNJOB.getType().equals(type)){
            returnData = String.valueOf(runJob(job_id));

        }

        return returnData;
    }

    //创建任务
    public String scheduleJob(Class<? extends Job> jobBeanClass, String cron, String data, String job_id){
        // 创建需要执行的任务
        String jobName = JOB_NAME + job_id;//该名字自定义
        JobDetail jobDetail = JobBuilder.newJob(jobBeanClass)
                .withIdentity(getJob_name(jobName, defaultGroup))
                .usingJobData("data", data)
                .build();
        //创建触发器，指定任务执行时间
        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity(getTrigger_name(jobName, defaultGroup))
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();

        //定时任务具体执行的内容  Taskimpl为具体任务(在此以字符串形式传入,也可传其他类型数据) key定义为JOB_CONTEXT
        jobDetail.getJobDataMap().put("JOB_CONTEXT","com.macro.mall.tiny.task.impl.Taskimpl");

        int ismonitor = 1;
        //添加监听
        if(ismonitor == 1){     //判断监听的条件
            KeyMatcher<JobKey> keyMatcher = KeyMatcher.keyEquals(jobDetail.getKey());
            try {
                scheduler.getListenerManager().addJobListener(new TaskListener(),keyMatcher);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }

        //使用调度器进行任务调度
        try {
            //创建定时任务
            scheduler.scheduleJob(jobDetail, cronTrigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.info("创建定时任务失败！");
        }
        return jobName;
    }



    //更新任务
    private String updatescheduleJob(Class<? extends Job> jobBeanClass, String cron, String data, String job_id)  {
        // 创建需要执行的任务
        String jobName = JOB_NAME + job_id;//该名字自定义
        try {
            //根据名字获取触发器
            CronTrigger cronTrigger = getCronTrigger(jobName,defaultGroup);
            cronTrigger = cronTrigger.getTriggerBuilder()
                    .withIdentity(cronTrigger.getKey())
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                    .build();
            int ismonitor = 1;
            KeyMatcher<JobKey> keyMatcher = KeyMatcher.keyEquals(getJob_name(jobName,defaultGroup));
            //判断是否有监听
            if(ismonitor == 1){     //判断监听的条件
                //先移除
                scheduler.getListenerManager().removeJobListenerMatcher(TaskListener.class.getName(),keyMatcher);
                //再添加
                scheduler.getListenerManager().addJobListener(new TaskListener(),keyMatcher);
            }else{
                //否则直接移除
                scheduler.getListenerManager().removeJobListenerMatcher(TaskListener.class.getName(),keyMatcher);
            }


            //根据触发器名称重置
            scheduler.rescheduleJob(getTrigger_name(jobName,defaultGroup), cronTrigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.info("创建定时任务失败！");
        }
        return jobName;

    }




    //暂停任务
    public Boolean pauseScheduleJob(String job_id) {
        String jobName = JOB_NAME + job_id;//该名字自定义
        boolean success = false;
        try {
            // 暂停触发器
            scheduler.pauseJob(getJob_name(jobName,defaultGroup));

            success = true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return success;
    }

    //立即执行任务
    public Boolean runJob(String job_id) {
        String jobName = JOB_NAME + job_id;//该名字自定义
        boolean success = false;
        try {
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("JOB_CONTEXT","com.macro.mall.tiny.task.impl.Taskimpl");
            scheduler.triggerJob(getJob_name(jobName,defaultGroup),jobDataMap);
            success = true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return success;
    }


    @Override
    public Boolean cancelScheduleJob(String job_id) {
        String jobName = JOB_NAME + job_id;//该名字自定义
        boolean success = false;
        try {
            // 暂停触发器
            scheduler.pauseTrigger(getTrigger_name(jobName, defaultGroup));
            // 移除触发器中的任务
            scheduler.unscheduleJob(getTrigger_name(jobName, defaultGroup));
            // 删除任务
            scheduler.deleteJob(getJob_name(jobName, defaultGroup));
            success = true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return success;
    }
}
