package com.macro.mall.tiny.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.job.CronProcessJob;
import com.macro.mall.tiny.job.SendEmailJob;
import com.macro.mall.tiny.job.SendMessageJob;
import com.macro.mall.tiny.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 定时任务调度相关接口
 * Created by macro on 2020/9/29.
 */
@Api(tags = "ScheduleController", description = "定时任务调度相关接口")
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation("定时发送邮件")
    @PostMapping("/sendEmail/{type}")
    public CommonResult sendEmail(@PathVariable("type") String type,
            @RequestParam String startTime,@RequestParam String data,@RequestParam String job_id) {
        Date date = DateUtil.parse(startTime, DatePattern.NORM_DATETIME_FORMAT);
        System.out.println("job_id:"+job_id);

        System.out.println("date:"+date);
        String jobName = scheduleService.scheduleFixTimeJob(SendEmailJob.class, date, data,job_id,type);
        return CommonResult.success(jobName);
    }

    @ApiOperation("定时发送站内信")
    @PostMapping("/sendMessage/{type}")
    public CommonResult sendMessage(@PathVariable("type") String type,
                                    @RequestParam String startTime,@RequestParam String data,@RequestParam String job_id) {
        Date date = DateUtil.parse(startTime, DatePattern.NORM_DATETIME_FORMAT);
        String jobName = scheduleService.scheduleFixTimeJob(SendMessageJob.class, date, data, job_id,type);
        return CommonResult.success(jobName);
    }

    @ApiOperation("通过CRON表达式调度任务")
    @PostMapping("/scheduleJob")
    public CommonResult scheduleJob(@RequestParam String cron, @RequestParam String data,@RequestParam String job_id) throws SchedulerException {
        String jobName = scheduleService.scheduleJob(CronProcessJob.class, cron, data,job_id);
        return CommonResult.success(jobName);
    }

    @ApiOperation("取消定时任务")
    @PostMapping("/cancelScheduleJob/{job_id}")
    public CommonResult cancelScheduleJob(@PathVariable("job_id") String job_id
            //@RequestParam String jobName
            ) {
        Boolean success = scheduleService.cancelScheduleJob(job_id);
        return CommonResult.success(success);
    }
}
