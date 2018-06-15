package com.newlandframework.rpc.bootclient.services;


import com.newlandframework.rpc.services.pojo.ReqMiddleDatas;
import com.newlandframework.rpc.services.pojo.ResMiddleDatas;

import java.util.List;

/**
 * Created by Huang Jianhai on 2018/6/15.
 */
public interface ReqDatasManage {
    //##正常请求## ST:01企业 09污水处理厂    （ST:21企业 29污水处理厂##固定时间取数据##  主要用于跨年补取）
    List<ReqMiddleDatas> query01();
    List<ReqMiddleDatas> query09();

    List<ReqMiddleDatas> compare01();
    List<ReqMiddleDatas> compare09();
    int save(ResMiddleDatas datas);
}
