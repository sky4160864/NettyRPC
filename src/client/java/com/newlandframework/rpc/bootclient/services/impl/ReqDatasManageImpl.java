package com.newlandframework.rpc.bootclient.services.impl;

import com.newlandframework.rpc.bootclient.services.ReqDatasManage;
import com.newlandframework.rpc.services.pojo.MiddleData;
import com.newlandframework.rpc.services.pojo.ReqMiddleDatas;
import com.newlandframework.rpc.services.pojo.ResMiddleDatas;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Created by Huang Jianhai on 2018/6/15.
 */
public class ReqDatasManageImpl implements ReqDatasManage {
    private JdbcTemplate jdbcTemplate;
    private String sql01 = "SELECT '01' as ST,T2.TYPE AS TP,T1.T_STATION_ID AS MN1,T4.NEW_MN AS MN2,\n" + "to_char(NVL(T3.EXTIME,TRUNC(SYSDATE,'yyyy')),'yyyymmddhh24miss') AS BTIME,\n" + "to_char(ADD_MONTHS(TRUNC(SYSDATE,'yyyy'),12),'yyyymmddhh24miss') AS ETIME\n" + "FROM T_STATION_INFO T1 \n" + "INNER JOIN T_ENT_INFO T2 ON(T1.T_ENT_ID=T2.T_ENT_ID AND T2.NEED_DATA='1')\n" + "LEFT JOIN T_ENT_SHURU_STATUS T3 ON(T1.T_STATION_ID=T3.T_STATION_ID)\n" + "LEFT JOIN SKPW_REPLACE T4 ON(T1.T_STATION_ID=T4.OLD_MN)";
    private String sql09 = "";
    private String sqlcomp01 = "";
    private String sqlcomp09 = "";
    private String sqlMerge1 = "MERGE INTO T_ENT_SHURU T1\n" + "USING (SELECT ? MN,TO_DATE(?,'yyyymmddhh24miss') MTIME,? WATER,? COD,? NH3 FROM DUAL) T2\n" + "ON(T1.T_STATION_ID=T2.MN AND T1.DATA=T2.MTIME)\n" + "WHEN MATCHED THEN  \n" + "  UPDATE SET WATER=T2.WATER,COD=T2.COD,NH3=T2.NH3,RECEIVE_ZX='1',\n" + "  SEND_CTL=(CASE WHEN T1.WATER!=T2.WATER OR T1.COD!=T2.COD OR T1.NH3!=T2.NH3 THEN '0' ELSE SEND_CTL END)\n" + "WHEN NOT MATCHED THEN  \n" + "  INSERT (main_id,WATER,COD,NH3,t_station_id,data,RECEIVE_ZX,SEND_CTL,guid)\n" + "  VALUES(skpw_shuru.nextval,T2.WATER,T2.COD,T2.NH3,T2.MN,T2.MTIME,'1','0',sys_guid()) ";
    private String sqlMerge2 = "MERGE INTO T_ENT_SHURU T1\n" + "USING (SELECT ? MN,TO_DATE(?,'yyyymmddhh24miss') MTIME,? GAS,? SO2,? NOX FROM DUAL) T2\n" + "ON(T1.T_STATION_ID=T2.MN AND T1.DATA=T2.MTIME)\n" + "WHEN MATCHED THEN  \n" + "  UPDATE SET GAS=T2.GAS,SO2=T2.SO2,NOX=T2.NOX,RECEIVE_ZX='1',\n" + "  SEND_CTL=(CASE WHEN T1.GAS!=T2.GAS OR T1.SO2!=T2.SO2 OR T1.NOX!=T2.NOX THEN '0' ELSE SEND_CTL END)\n" + "WHEN NOT MATCHED THEN  \n" + "  INSERT (main_id,GAS,SO2,NOX,t_station_id,data,RECEIVE_ZX,SEND_CTL,guid)\n" + "  VALUES(skpw_shuru.nextval,T2.GAS,T2.SO2,T2.NOX,T2.MN,T2.MTIME,'1','0',sys_guid()) ";



    @Override
    public List<ReqMiddleDatas> query01() {
        return jdbcTemplate.query(sql01, new BeanPropertyRowMapper(ReqMiddleDatas.class));
    }

    @Override
    public List<ReqMiddleDatas> query09() {
        return jdbcTemplate.query(sql09, new BeanPropertyRowMapper(ReqMiddleDatas.class));
    }

    @Override
    public List<ReqMiddleDatas> compare01() {
        return jdbcTemplate.query(sqlcomp01, new BeanPropertyRowMapper(ReqMiddleDatas.class));
    }

    @Override
    public List<ReqMiddleDatas> compare09() {
        return jdbcTemplate.query(sqlcomp09, new BeanPropertyRowMapper(ReqMiddleDatas.class));
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(ResMiddleDatas datas){
        String mn = datas.getReq().getMn2()==null?datas.getReq().getMn1():datas.getReq().getMn2();
        String sql = "1".equals(datas.getReq().getTp())?sqlMerge1:sqlMerge2;
        List<MiddleData> list = datas.getList();
        for (int i = 0; i < list.size(); i++) {
            MiddleData md = list.get(i);
            jdbcTemplate.update(sql,new Object[]{mn,md.getMtime(),md.getAvgflow(),md.getVal1(),md.getVal2()});
        }
        return 0;
    }
}
