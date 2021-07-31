package com.macro.mall.tiny.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

//创建监听程序
public class TaskListener implements JobListener {
    @Override
    public String getName() {
        return this.getClass().getName();
    }

    //监听逻辑放在该方法中
    @Override
    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
        System.out.println(getName()+"触发对"+jobExecutionContext.getJobDetail().getJobClass()+"的开始执行的监听工作!" +
                "这里可以完成任务前的一些资源准备工作或日志记录");

        int ii = 1;
        System.out.println("ii:"+ii);
        while(true){
            //在此添加具体的监听逻辑

            if(ii % 2 == 1 ){
                ii++;
                System.out.println("停止10秒");
                try {
                    Thread.sleep(10000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }else {
                System.out.println("继续任务");
                break;
            }

        }

    }

    @Override  //
    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {
        System.out.println("被否决执行了,可以做些日志记录");
    }

    @Override
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
        System.out.println(getName()+"触发对"+jobExecutionContext.getJobDetail().getJobClass()+"的结束执行的监听工作!" +
                "这里可以进行资源销毁工作或做一些新闻扒取结果的统计工作");

    }
}
