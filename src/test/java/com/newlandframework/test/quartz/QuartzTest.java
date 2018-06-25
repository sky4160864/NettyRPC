package com.newlandframework.test.quartz;

import com.newlandframework.rpc.bootclient.schedul.QuartzUtil;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 任务调度公共类
 *
 * @remark
 * @author iaiai QQ:176291935
 * @time 2015-3-23下午3:04:12
 */
public class QuartzTest {

    private final static String JOB_GROUP_NAME = "QUARTZ_JOBGROUP_NAME2";// 任务组
    private final static String TRIGGER_GROUP_NAME = "QUARTZ_TRIGGERGROUP_NAME2";// 触发器组
    private static Logger log = LoggerFactory.getLogger(QuartzTest.class);// 日志

    public static String JOB1_EXPRESSION = "0/2 * * * * ?";
    public static String JOB2_EXPRESSION = "0 * * * * ?";

    public static boolean running = false;

    static{
        log.info("===================Quartz StudentTest===================");
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
        System.out.println("111111111111");
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
//				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(seconds).repeatForever()// 一直执行
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)
                ).build();// 产生触发器new CronTrigger("cronTrigger","triggerGroup");
        // 向Scheduler中添加job任务和trigger触发器
        System.out.println("2222222222222");
        sche.scheduleJob(jobDetail, trigger);
        // 启动
        sche.start();
        System.out.println("2222222222222");
        try {
            Thread.sleep(2*60*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sche.shutdown(true);
    }

    public static void jobStart(){
        try {
            QuartzTest.addJob("job1222", "trigger1222", JobTest.class,JOB1_EXPRESSION);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void dddo(){
        QuartzTest.jobStart();
        //QuartzUtil.jobStart();
    }


}
