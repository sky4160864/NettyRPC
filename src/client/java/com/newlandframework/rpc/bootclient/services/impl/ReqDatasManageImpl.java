package com.newlandframework.rpc.bootclient.services.impl;

import com.newlandframework.rpc.bootclient.services.ReqDatasManage;
import com.newlandframework.rpc.exception.InvokeTimeoutException;
import com.newlandframework.rpc.netty.RpcServerLoader;
import com.newlandframework.rpc.services.ReqMiddleDatasManage;
import com.newlandframework.rpc.services.pojo.MiddleData;
import com.newlandframework.rpc.services.pojo.ReqMiddleDatas;
import com.newlandframework.rpc.services.pojo.ResMiddleDatas;
import com.newlandframework.rpc.spring.PropertyPlaceholder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Huang Jianhai on 2018/6/15.
 */
public class ReqDatasManageImpl implements ReqDatasManage {
    private Logger logger = LoggerFactory.getLogger(ReqDatasManageImpl.class);
    private JdbcTemplate jdbcTemplate;
    //注意：ST:01,09     //T_STATION_INFO关联T_ENT_INFO、T_ENT_SHURU_STATUS、SKPW_REPLACE取T_ENT_SHURU_STATUS.extime大于当年的记录
    private String sql01 = "SELECT '01' as ST,T2.TYPE AS TP,T1.T_STATION_ID AS MN1,T4.NEW_MN AS MN2,\n" + "to_char(NVL(T3.EXTIME,TRUNC(SYSDATE,'yyyy')),'yyyymmddhh24miss') AS BTIME,\n" + "to_char(ADD_MONTHS(TRUNC(SYSDATE,'yyyy'),12),'yyyymmddhh24miss') AS ETIME\n" + "FROM T_STATION_INFO T1 \n" + "INNER JOIN T_ENT_INFO T2 ON(T1.T_ENT_ID=T2.T_ENT_ID AND T2.NEED_DATA='1')\n" + "LEFT JOIN T_ENT_SHURU_STATUS T3 ON(T1.T_STATION_ID=T3.T_STATION_ID AND T3.EXTIME>=TRUNC(SYSDATE,'YYYY'))\n" + "LEFT JOIN SKPW_REPLACE T4 ON(T1.T_STATION_ID=T4.OLD_MN)";
    //T_STATION_INFO关联T_ENT_SHURU_STATUS、SKPW_REPLACE取T_ENT_SHURU_STATUS.extime大于当年的记录（无T_ENT_INFO）
    private String sql09 = "SELECT '09' as ST,'1' AS TP,T1.T_STATION_ID AS MN1,T4.NEW_MN AS MN2,\n" + "to_char(NVL(T3.EXTIME,TRUNC(SYSDATE,'yyyy')),'yyyymmddhh24miss') AS BTIME,\n" + "to_char(ADD_MONTHS(TRUNC(SYSDATE,'yyyy'),12),'yyyymmddhh24miss') AS ETIME\n" + "FROM T_STATION_INFO09 T1 \n" + "LEFT JOIN T_ENT_SHURU_STATUS T3 ON(T1.T_STATION_ID=T3.T_STATION_ID and t3.extime>=trunc(sysdate,'yyyy'))\n" + "LEFT JOIN SKPW_REPLACE T4 ON(T1.T_STATION_ID=T4.OLD_MN)";
    //注意：ST:11,19    //sqlcomp01、sqlcomp09区别同sql01、sql09一致  包括当月
    private String sql11 = "WITH X0 AS (\n" + "  SELECT ADD_MONTHS(TRUNC(SYSDATE,'yyyy'),LEVEL-1) BTIME FROM DUAL CONNECT BY LEVEL<=extract(month from sysdate)\n" + "),X1 AS (\n" + "  SELECT T2.TYPE AS TP,T1.T_STATION_ID AS MN1\n" + "  FROM T_STATION_INFO T1 \n" + "  INNER JOIN T_ENT_INFO T2 ON(T1.T_ENT_ID=T2.T_ENT_ID AND T2.NEED_DATA='1')\n" + "),X2 AS (\n" + "  SELECT  MN1,TRUNC(T3.DATA,'MM') MTIME,COUNT(1) AS NUMS FROM X1\n" + "  LEFT JOIN T_ENT_SHURU T3 ON(X1.MN1=T3.T_STATION_ID)\n" + "  WHERE T3.DATA>=TRUNC(SYSDATE,'YYYY')\n" + "  GROUP BY X1.MN1,TRUNC(T3.DATA,'MM')\n" + "),X3 AS( \n" + "  SELECT TP,MN1,BTIME FROM X0,X1\n" + ")\n" + "SELECT '11' AS ST,X3.TP,X3.MN1,NEW_MN AS MN2,\n" + "TO_CHAR(X3.BTIME,'YYYYMMDDHH24MISS') AS BTIME,\n" + "TO_CHAR(ADD_MONTHS(X3.BTIME,1),'YYYYMMDDHH24MISS') AS ETIME,NVL(NUMS,0) AS NUMS\n" + "FROM X3 LEFT JOIN X2 ON(X3.MN1=X2.MN1 AND X3.BTIME=X2.MTIME)\n" + "LEFT JOIN SKPW_REPLACE T5 ON(X3.MN1=T5.OLD_MN)\n" + "ORDER BY X3.MN1,X3.BTIME";
    //注意：ST:11,19    //sqlcomp01、sqlcomp09区别同sql01、sql09一致  包括当月
    private String sql19 = "WITH X0 AS (\n" + "  SELECT ADD_MONTHS(TRUNC(SYSDATE,'yyyy'),LEVEL-1) BTIME FROM DUAL CONNECT BY LEVEL<=extract(month from sysdate)\n" + "),X1 AS (\n" + "  SELECT '1' TP,T1.T_STATION_ID AS MN1 FROM T_STATION_INFO09 T1 \n" + "),X2 AS (\n" + "  SELECT  MN1,TRUNC(T3.DATA,'MM') MTIME,COUNT(1) AS NUMS FROM X1\n" + "  LEFT JOIN T_ENT_SHURU09 T3 ON(X1.MN1=T3.T_STATION_ID)\n" + "  WHERE T3.DATA>=TRUNC(SYSDATE,'YYYY')\n" + "  GROUP BY X1.MN1,TRUNC(T3.DATA,'MM')\n" + "),X3 AS( \n" + "  SELECT TP,MN1,BTIME FROM X0,X1\n" + ")\n" + "SELECT '19' AS ST,X3.TP,X3.MN1,NEW_MN AS MN2,\n" + "TO_CHAR(X3.BTIME,'YYYYMMDDHH24MISS') AS BTIME,\n" + "TO_CHAR(ADD_MONTHS(X3.BTIME,1),'YYYYMMDDHH24MISS') AS ETIME,NVL(NUMS,0) AS NUMS\n" + "FROM X3 LEFT JOIN X2 ON(X3.MN1=X2.MN1 AND X3.BTIME=X2.MTIME)\n" + "LEFT JOIN SKPW_REPLACE T5 ON(X3.MN1=T5.OLD_MN)\n" + "ORDER BY X3.MN1,X3.BTIME";
    private String sqlMerge1 = "MERGE INTO T_ENT_SHURU T1\n" + "USING (SELECT ? MN,TO_DATE(?,'yyyymmddhh24miss') MTIME,? WATER,? COD,? NH3 FROM DUAL) T2\n" + "ON(T1.T_STATION_ID=T2.MN AND T1.DATA=T2.MTIME)\n" + "WHEN MATCHED THEN  \n" + "  UPDATE SET WATER=T2.WATER,COD=T2.COD,NH3=T2.NH3,RECEIVE_ZX='1',RTIME=SYSDATE,\n" + "  SEND_CTL=(CASE WHEN T1.WATER!=T2.WATER OR T1.COD!=T2.COD OR T1.NH3!=T2.NH3 THEN '0' ELSE SEND_CTL END)\n" + "WHEN NOT MATCHED THEN  \n" + "  INSERT (main_id,WATER,COD,NH3,t_station_id,data,RECEIVE_ZX,RTIME,SEND_CTL,guid)\n" + "  VALUES(skpw_shuru.nextval,T2.WATER,T2.COD,T2.NH3,T2.MN,T2.MTIME,'1',SYSDATE,'0',sys_guid()) ";
    private String sqlMerge09 = "MERGE INTO T_ENT_SHURU09 T1\n" + "USING (SELECT ? MN,TO_DATE(?,'yyyymmddhh24miss') MTIME,? WATER,? COD,? NH3 FROM DUAL) T2\n" + "ON(T1.T_STATION_ID=T2.MN AND T1.DATA=T2.MTIME)\n" + "WHEN MATCHED THEN  \n" + "  UPDATE SET WATER=T2.WATER,COD=T2.COD,NH3=T2.NH3,RECEIVE_ZX='1',RTIME=SYSDATE\n" + "WHEN NOT MATCHED THEN  \n" + "  INSERT (main_id,WATER,COD,NH3,t_station_id,data,RECEIVE_ZX,RTIME,SEND_CTL,guid)\n" + "  VALUES(SKPW_SHURU09.NEXTVAL,T2.WATER,T2.COD,T2.NH3,T2.MN,T2.MTIME,'1',SYSDATE,'0',sys_guid()) ";
    private String sqlMerge2 = "MERGE INTO T_ENT_SHURU T1\n" + "USING (SELECT ? MN,TO_DATE(?,'yyyymmddhh24miss') MTIME,? GAS,? SO2,? NOX FROM DUAL) T2\n" + "ON(T1.T_STATION_ID=T2.MN AND T1.DATA=T2.MTIME)\n" + "WHEN MATCHED THEN  \n" + "  UPDATE SET GAS=T2.GAS,SO2=T2.SO2,NOX=T2.NOX,RECEIVE_ZX='1',RTIME=SYSDATE,\n" + "  SEND_CTL=(CASE WHEN T1.GAS!=T2.GAS OR T1.SO2!=T2.SO2 OR T1.NOX!=T2.NOX THEN '0' ELSE SEND_CTL END)\n" + "WHEN NOT MATCHED THEN  \n" + "  INSERT (main_id,GAS,SO2,NOX,t_station_id,data,RECEIVE_ZX,RTIME,SEND_CTL,guid)\n" + "  VALUES(skpw_shuru.nextval,T2.GAS,T2.SO2,T2.NOX,T2.MN,T2.MTIME,'1',SYSDATE,'0',sys_guid()) ";
    private String sqlStatus = "MERGE INTO T_ENT_SHURU_STATUS T1\n" + "USING(SELECT ? MN,TO_DATE(?,'yyyymmddhh24miss') EXTIME FROM DUAL) T2\n" + "ON(T1.T_STATION_ID=T2.MN)\n" + "WHEN MATCHED THEN\n" + " UPDATE SET T1.EXTIME=T2.EXTIME,T1.UPTTIME=SYSDATE\n" + "WHEN NOT MATCHED THEN\n" + " INSERT (T_STATION_ID,EXTIME,UPTTIME)VALUES(T2.MN,T2.EXTIME,SYSDATE)";


    @Override
    public List<ReqMiddleDatas> query01() {
        return jdbcTemplate.query(sql01, new BeanPropertyRowMapper(ReqMiddleDatas.class));
    }

    @Override
    public List<ReqMiddleDatas> query09() {
        return jdbcTemplate.query(sql09, new BeanPropertyRowMapper(ReqMiddleDatas.class));
    }

    @Override
    public List<ReqMiddleDatas> query21() {
        String fix_fecth = PropertyPlaceholder.getProperty("fix_fecth");
        if(!"1".equals(fix_fecth)){
            return null;
        }
        String btime = PropertyPlaceholder.getProperty("fix_btime");
        String etime = PropertyPlaceholder.getProperty("fix_etime");
        if(btime==null||btime.length()!=14||etime==null||etime.length()!=14){
            logger.error("[FIX_TIME ERR] {}-{}",btime,etime);
            return null;
        }
        List<ReqMiddleDatas> list = new ArrayList<>();
        //水
        String fix_mns = PropertyPlaceholder.getProperty("fix_water_mns");
        if(fix_mns!=null&&fix_mns.length()>0){
            String[] mns = fix_mns.split(",");
            for(String mn:mns){
                list.add(new ReqMiddleDatas("21","1",mn,btime,etime));
            }
        }
        //气
        fix_mns = PropertyPlaceholder.getProperty("fix_gas_mns");
        if(fix_mns!=null&&fix_mns.length()>0){
            String[] mns = fix_mns.split(",");
            for(String mn:mns){
                list.add(new ReqMiddleDatas("21","2",mn,btime,etime));
            }
        }
        //污水处理厂
        fix_mns = PropertyPlaceholder.getProperty("fix_wsc_mns");
        if(fix_mns!=null&&fix_mns.length()>0){
            String[] mns = fix_mns.split(",");
            for(String mn:mns){
                list.add(new ReqMiddleDatas("29","1",mn,btime,etime));
            }
        }

        return list;
    }

   /* @Override
    public List<ReqMiddleDatas> query29() {
        return null;
    }*/

    @Override
    public List<ReqMiddleDatas> query11() {
        return jdbcTemplate.query(sql11, new BeanPropertyRowMapper(ReqMiddleDatas.class));
    }

    @Override
    public List<ReqMiddleDatas> query19() {
        return jdbcTemplate.query(sql19, new BeanPropertyRowMapper(ReqMiddleDatas.class));
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void doReqManage(List<ReqMiddleDatas> list, ReqMiddleDatasManage reqManage) {
        long btime = 0;
        ReqMiddleDatas reqObj = null;
        try {
            if (list == null) return;
            for (int i = 0; i < list.size(); i++) {
                btime = System.currentTimeMillis();
                reqObj = list.get(i);
                ResMiddleDatas rlt = reqManage.query(reqObj);
                if (rlt.getResult() == 1) {
                    int size = rlt.getList() == null ? 0 : rlt.getList().size();
                    if (size > 0) {
                        save(rlt);
                    }
                    logger.info("ST:{} TP:{} MN:{} RP:{} Bwtime:{} Result:{} Size:{} Cost:{}", rlt.getReq().getSt(), rlt.getReq().getTp(), rlt.getReq().getMn1(), rlt.getReq().getMn2(), rlt.getReq().getBtime() + "-" + rlt.getReq().getEtime(), rlt.getResult(), size, System.currentTimeMillis() - btime);
                } else {
                    logger.error("[RES ERR]{}", rlt.getReq());
                }
            }
        }catch (InvokeTimeoutException e){
            logger.error("[InvokeTimeoutException]{} Cost:{}", reqObj,System.currentTimeMillis() - btime);
            //RpcServerLoader.getInstance().reLoad();
        }
    }

    public int save(ResMiddleDatas datas){
        String mn = datas.getReq().getMn1();//只有服务端需要判断mn2
        String st = datas.getReq().getSt();
        String tp = datas.getReq().getTp();
        String sql = "1".equals(tp)?sqlMerge1:sqlMerge2;
        if("09".equals(st)||"19".equals(st)||"29".equals(st)){
            sql = sqlMerge09;
        }
        List<MiddleData> list = datas.getList();
        List<Object[]> batchParams = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            MiddleData md = list.get(i);
            try {
                batchParams.add(new Object[]{mn, md.getMtime(), md.getAvgflow(), md.getVal1(), md.getVal2()});
                //只有01,09是根据exchange_time取数据，需要更新状态表
                if("01".equals(st)||"09".equals(st)) {
                    if (i == list.size() - 1) {
                        jdbcTemplate.update(sqlStatus, new Object[]{mn, md.getExtime()});
                    }
                }
            }catch (Exception e){
                logger.error("[Save Err mn:{},tp:{},mtime:{}, flow:{}, val1:{},val2:{}]",mn,datas.getReq().getTp(), md.getMtime(), md.getAvgflow(), md.getVal1(), md.getVal2());
            }
        }
        try {
            jdbcTemplate.batchUpdate(sql,batchParams);
        }catch (Exception e){
            logger.error("[batchUpdate Err mn:{},tp:{},mtime:{}, flow:{}, val1:{},val2:{}]",mn,datas.getReq().getTp());
        }
        /*for (int i = 0; i < list.size(); i++) {
            MiddleData md = list.get(i);
            try {
                jdbcTemplate.update(sql, new Object[]{mn, md.getMtime(), md.getAvgflow(), md.getVal1(), md.getVal2()});
                //只有01,09是根据exchange_time取数据，需要更新状态表
                if("01".equals(st)||"09".equals(st)) {
                    if (i == list.size() - 1) {
                        jdbcTemplate.update(sqlStatus, new Object[]{mn, md.getExtime()});
                    }
                }
            }catch (Exception e){
                logger.error("[Save Err mn:{},tp:{},mtime:{}, flow:{}, val1:{},val2:{}]",mn,datas.getReq().getTp(), md.getMtime(), md.getAvgflow(), md.getVal1(), md.getVal2());
            }
        }*/
        return 0;
    }
}
