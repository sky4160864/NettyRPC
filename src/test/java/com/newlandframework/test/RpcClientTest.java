package com.newlandframework.test;


import com.newlandframework.rpc.services.PersonManage;
import com.newlandframework.rpc.services.pojo.Person;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Created by Administrator on 2018-06-17.
 */
public class RpcClientTest {

    @Test
    public void clienttest() throws InterruptedException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-rpc-invoke-config-client-test.xml");
        PersonManage manage = (PersonManage) context.getBean("personManage");
        Person p = new Person();
        p.setId(20150811);
        p.setName("XiaoHaoBaby");
        p.setAge(1);
        int result = manage.save(p);

        manage.query(p);

        System.out.println("call pojo rpc result:" + result);

        context.destroy();
    }

    @Test
    public void dowhile2() throws Exception{
        try{
            while(true){
                clienttest();
                Thread.sleep(10000);
            }
        }finally {
        }
    }

    @Test
    public void dowhile() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-rpc-invoke-config-client-test.xml");
        PersonManage manage = (PersonManage) context.getBean("personManage");

        while(true){
            try{
                Person p = new Person();
                p.setId(20150811);
                p.setName("XiaoHaoBaby");
                p.setAge(1);
                int result = manage.save(p);
                System.out.println("call pojo rpc result:" + result);
                manage.query(p);
                Thread.sleep(10000);
            }catch (Exception e){
                System.out.println("-------------------------------------------");
                //manage = (PersonManage) context.getBean("personManage");
                //e.printStackTrace();
            }finally {
                //context.destroy();
            }

        }
    }
}
