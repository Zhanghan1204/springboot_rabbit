**定时任务**  
1.ScheduleController 定义具体的访问接口  
2.ScheduleServiceImpl 用于创建 销毁 停止任务  
3.TaskListener 创建的监听程序,在定时任务创建时,需要加进去 
4.SendEmailJob 为定义的具体的定时job
5.Taskimpl  在此定义具体的定时任务详情(在定时任务创建时,需要加进去),
利用java的反射机制,在SendEmailJob调用该任务


任务在创建后,会添加到三张表中,分别为:  
qrtz_cron_triggers  
qrtz_triggers
qrtz_job_details  
从数据库删除任务时,需要按照这三张表的顺序进行删除,因为这三张表间有外键关联