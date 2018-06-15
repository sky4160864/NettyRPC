package com.newlandframework.test;

import com.newlandframework.rpc.services.ReqMiddleDatasManage;
import com.newlandframework.rpc.services.pojo.ReqMiddleDatas;
import com.newlandframework.rpc.services.pojo.ResMiddleDatas;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Huang Jianhai on 2018/6/14.
 */
public class ReqMiddleDatasManageTest {

    @Test
    public void query(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:rpc-invoke-config-client.xml");
        ReqMiddleDatasManage reqManage = (ReqMiddleDatasManage) context.getBean("midDatasService");
        ReqMiddleDatas req = new ReqMiddleDatas();
        req.setSt("01");
        req.setTp("1");
        req.setMn1("33330861000681");
        req.setBtime("20180101000000");
        req.setEtime("20190101000000");
        ResMiddleDatas rlt = reqManage.query(req);
        System.out.println(rlt.getList().size());
    }
}
