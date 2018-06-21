package com.newlandframework.rpc.bootclient.schedul;

import com.newlandframework.rpc.spring.PropertyPlaceholder;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;


/**
 * 任务调度公共类
 *
 * @remark
 * @author iaiai QQ:176291935
 * @time 2015-3-23下午3:04:12
 */
public class QuartzUtil {

    private final static String JOB_GROUP_NAME = "QUARTZ_JOBGROUP_NAME";// 任务组
    private final static String TRIGGER_GROUP_NAME = "QUARTZ_TRIGGERGROUP_NAME";// 触发器组
    private static Logger log = LoggerFactory.getLogger(QuartzUtil.class);// 日志

    public static String JOB1_EXPRESSION = PropertyPlaceholder.getProperty("job1_expression");
    public static String JOB2_EXPRESSION = PropertyPlaceholder.getProperty("job2_expression");
    public static boolean running = false; //单任务不加锁



    static{
        log.info("===================Quartz===================");
        log.info("==JOB1_EXPRESSION: {}",JOB1_EXPRESSION);
        log.info("==JOB2_EXPRESSION: {}",JOB2_EXPRESSION);
        log.info("============================================");
    }
    /**
     * 添加任务的方法
     *
     * @param jobName
     *            任务名
     * @param triggerName
     *            触发器名
     * @param jobClass
     *            执行任务的类
     * @throws SchedulerException
     */
    public static void addJob(String jobName, String triggerName, Class<? extends Job> jobClass,String cronExpression)
            throws SchedulerException {
        // 创建一个SchedulerFactory工厂实例
        SchedulerFactory sf = new StdSchedulerFactory();
        // 通过SchedulerFactory构建Scheduler对象
        //由于使用StdSchedulerFactory（基于properties配置文件创建QuartzScheduler实例），如果在创建SchedulerFactory时没有通过new StdSchedulerFactory().initialize(props)指定特定的配置参数，也没有指定特定的配置文件，则程序使用quartz-2.2.1.jar中org\quartz下的quartz.properties作为默认的配置文件：
        Scheduler sche = sf.getScheduler();
        // 用于描叙Job实现类及其他的一些静态信息，构建一个作业实例
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, JOB_GROUP_NAME).build();
        // 构建一个触发器，规定触发的规则
        Trigger trigger = TriggerBuilder.newTrigger()// 创建一个新的TriggerBuilder来规范一个触发器
                .withIdentity(triggerName, TRIGGER_GROUP_NAME)// 给触发器起一个名字和组名
                .startNow()// 立即执行
//				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(seconds).repeatForever()// 一直执行
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)
                ).build();// 产生触发器new CronTrigger("cronTrigger","triggerGroup");
        // 向Scheduler中添加job任务和trigger触发器
        sche.scheduleJob(jobDetail, trigger);
        // 启动
        sche.start();
    }


    /**
     * 添加任务的方法
     *
     * @param jobName
     *            任务名
     * @param triggerName
     *            触发器名
     * @param jobClass
     *            执行任务的类
     * @param seconds
     *            间隔时间
     * @throws SchedulerException
     */
    public static void addJob(String jobName, String triggerName, Class<? extends Job> jobClass,int seconds)
    {
        try{
            // 创建一个SchedulerFactory工厂实例
            SchedulerFactory sf = new StdSchedulerFactory();
            // 通过SchedulerFactory构建Scheduler对象
            Scheduler sche = sf.getScheduler();
            // 用于描叙Job实现类及其他的一些静态信息，构建一个作业实例
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, JOB_GROUP_NAME).build();
            // 构建一个触发器，规定触发的规则
            Trigger trigger = TriggerBuilder.newTrigger()// 创建一个新的TriggerBuilder来规范一个触发器
                    .withIdentity(triggerName, TRIGGER_GROUP_NAME)// 给触发器起一个名字和组名
                    .startNow()// 立即执行
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(seconds).repeatForever()// 一直执行
                    ).build();// 产生触发器new CronTrigger("cronTrigger","triggerGroup");
            // 向Scheduler中添加job任务和trigger触发器
            sche.scheduleJob(jobDetail, trigger);
            // 启动
            sche.start();
        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }


    public static void jobStart(){
        try {
            QuartzUtil.addJob("job1", "trigger1", JobOne.class,JOB1_EXPRESSION);
            QuartzUtil.addJob("job2", "trigger2", JobTwo.class,JOB2_EXPRESSION);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
