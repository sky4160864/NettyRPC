package com.newlandframework.rpc.services.impl;

import com.newlandframework.rpc.bootclient.schedul.JobOne;
import com.newlandframework.rpc.services.ReqMiddleDatasManage;
import com.newlandframework.rpc.services.pojo.MiddleData;
import com.newlandframework.rpc.services.pojo.ReqMiddleDatas;
import com.newlandframework.rpc.services.pojo.ResMiddleDatas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by Huang Jianhai on 2018/6/14.
 */
public class ReqMiddleDatasManageImpl implements ReqMiddleDatasManage {
    private Logger logger = LoggerFactory.getLogger(ReqMiddleDatasManageImpl.class);
    private DataSource dataSource;

    private String sql1 = "select to_char(MONITORTIME,'yyyymmddhh24miss') as mtime,AVGFLOW||'' as avgflow " +
            " ,COD||'' as val1,NH3||'' as val2 from PS_Water_Hour_Data where  outputcode=?" +
            " and MONITORTIME>=trunc(sysdate,'yyyy')"+
            " and EXCHANGETIME>=to_date(?,'yyyymmddhh24miss')" +//固定取数据时需要=号，正常取会多取一条开始记录
            " and EXCHANGETIME<to_date(?,'yyyymmddhh24miss')" +//中间库出现断档数据时，数据取不到，默认设置为下年初，固定取数据则为设置时间（结束时间不包括）
            //" and rownum<1001 "+ //改为服务端每次返回1000条
            " ORDER BY EXCHANGETIME";

    private String sql2 = "select to_char(MONITORTIME,'yyyymmddhh24miss'),AVGFLOW||''" +
            " ,SO2||'',NOX||'' from PS_GAS_Hour_Data where  outputcode=?" +
            " and MONITORTIME>=trunc(sysdate,'yyyy')"+
            " and EXCHANGETIME>=to_date(?,'yyyymmddhh24miss')" +//固定取数据时需要=号，正常取会多取一条开始记录
            " and EXCHANGETIME<to_date(?,'yyyymmddhh24miss')" +//中间库出现断档数据时，数据取不到，默认设置为下年初，固定取数据则为设置时间（结束时间不包括）
            //" and rownum<1001 "+ //改为服务端每次返回1000条
            " ORDER BY EXCHANGETIME";

    private String sql3 = "select to_char(MONITORTIME,'yyyymmddhh24miss'),AVGFLOW||'',COD||''" +
            " ,NH3||'' from  PS_WATER_Hour_Data where  outputcode=?" +
            " and MONITORTIME>=to_date(?,'yyyymmdd')" +
            " and MONITORTIME<to_date(?,'yyyymmdd')" ;

    private String sql4 = "select to_char(MONITORTIME,'yyyymmddhh24miss'),AVGFLOW||'',SO2||''" +
            " ,NOX||'' from  PS_Gas_Hour_Data where  outputcode=?" +
            " and MONITORTIME>=to_date(?,'yyyymmdd')" +
            " and MONITORTIME<to_date(?,'yyyymmdd')" ;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ResMiddleDatas query(ReqMiddleDatas req) {
        JdbcTemplate template = new JdbcTemplate(this.dataSource);
        List<MiddleData> list = null;
        //请求数据 ST:01企业 09污水处理厂   (21企业、29污水处理厂 ##固定时间取数据##  主要用于跨年补取)
        if("01".equals(req.getSt())||"09".equals(req.getSt())
                ||"21".equals(req.getSt())||"29".equals(req.getSt())){
            if("1".equals(req.getTp())){
                list = template.query(sql1,new Object[]{req.getMn1(),req.getBtime(),req.getEtime()}, new BeanPropertyRowMapper(MiddleData.class));
            }else if ("2".equals(req.getTp())){
                list = template.query(sql2,new Object[]{req.getMn1(),req.getBtime(),req.getEtime()}, new BeanPropertyRowMapper(MiddleData.class));
            }
            return new ResMiddleDatas(1,req,list);

        //比较数据完整性，缺失则补数据
        }else if("11".equals(req.getSt())||"19".equals(req.getSt())) {
            if ("1".equals(req.getTp())) {
                list = template.query(sql3,new Object[]{req.getMn1(),req.getBtime(),req.getEtime()}, new BeanPropertyRowMapper(MiddleData.class));
            } else if ("2".equals(req.getTp())) {
                list = template.query(sql4,new Object[]{req.getMn1(),req.getBtime(),req.getEtime()}, new BeanPropertyRowMapper(MiddleData.class));
            }
            if(list!=null){
                if(list.size()>req.getNums()){
                    return new ResMiddleDatas(1,req,list,"[equal]:req["+req.getNums()+"]res["+list.size()+"]");
                }else{
                    return new ResMiddleDatas(1,req,null,"[differ]:req["+req.getNums()+"]res["+list.size()+"]");
                }
            }
        }
        return new ResMiddleDatas(-1,req,null);
    }

    @Override
    public ResMiddleDatas compare(ReqMiddleDatas req) {
        return null;
    }
}