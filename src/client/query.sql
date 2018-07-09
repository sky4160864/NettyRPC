#查询本地点源最后记录
SELECT T1.T_STATION_ID,max(t3.data)
FROM T_STATION_INFO T1
INNER JOIN T_ENT_INFO T2 ON(T1.T_ENT_ID=T2.T_ENT_ID AND T2.NEED_DATA='1')
LEFT JOIN T_ENT_SHURU T3 ON(T1.T_STATION_ID=T3.T_STATION_ID AND T3.data>=TRUNC(SYSDATE,'YYYY'))
group by T1.T_STATION_ID

#按月查询记录数
SELECT T1.T_STATION_ID,TRUNC(T3.DATA,'MM'),COUNT(1)
FROM T_STATION_INFO T1
INNER JOIN T_ENT_INFO T2 ON(T1.T_ENT_ID=T2.T_ENT_ID AND T2.NEED_DATA='1')
LEFT JOIN T_ENT_SHURU T3 ON(T1.T_STATION_ID=T3.T_STATION_ID AND T3.data>=TRUNC(SYSDATE,'YYYY'))
group by T1.T_STATION_ID,TRUNC(T3.DATA,'MM')

#查询中间库
select * from ps_gas_hour_data t where t.outputcode='33330206000052'
and t.monitortime>=to_date('2018/2/1','yyyy/mm/dd')
and t.monitortime<to_date('2018/3/1','yyyy/mm/dd')


select t.*, t.rowid from t_ent_shuru_status t where t_station_id='33330921013071'
select t.*, t.rowid from t_ent_shuru t where t_station_id='33330921100952' and t.data>=trunc(sysdate,'yyyy')
select t.t_station_id,max(t.data) from t_ent_shuru t,t_ent_shuru_status t2 where t.t_station_id=t2.t_station_id
and t.data>=trunc(sysdate,'yyyy') group by t.t_station_id

select * from (
  select t.t_station_id,trunc(t.data,'mm'),count(1) from t_ent_shuru t,t_ent_info t2,t_station_info t3
  where t.t_station_id=t3.t_station_id and t2.t_ent_id=t3.t_ent_id
  and t.data>=trunc(sysdate,'yyyy')
  group by t.t_station_id,trunc(t.data,'mm')
) order by 1,2



create /* public */ database link dblink_mid_new
  connect to jkdj identified by cgjkdj
  using '(DESCRIPTION =(ADDRESS_LIST =(ADDRESS =(PROTOCOL = TCP)(HOST = 10.33.100.34)(PORT = 1521)))(CONNECT_DATA =(SERVICE_NAME = orcl)))';
--最新中间库
select * from PS_Water_Hour_Data@dblink_mid_new;
select * from PS_gas_Hour_Data@dblink_mid_new;
--老中间库
select * from ps_water_output_hour_data@dblink_mid;
select * from ps_gas_output_hour_data@dblink_mid;

--数据缺失比对
select to_char(trunc(monitortime,'mm'),'yyyy-mm') ym,count(1) from ps_gas_output_hour_data@dblink_mid
where outputcode='33330621002072' and monitortime>=trunc(sysdate,'yyyy')
group by trunc(monitortime,'mm')
select to_char(trunc(monitortime,'mm'),'yyyy-mm') ym,count(1) from PS_gas_Hour_Data@dblink_mid_new
where outputcode='33330621002072' and monitortime>=trunc(sysdate,'yyyy')
group by trunc(monitortime,'mm')


--比较记录数 总量
select aa.*,bb.* from
    (
      select t3.t_station_id,t1.type,count(1) loc_cnt,
      decode(t1.type,1,sum(water),sum(gas)) loc_val1,
      decode(t1.type,1,sum(water*cod),sum(gas*so2)) loc_val2,
      decode(t1.type,1,sum(water*nh3),sum(gas*nox)) loc_val3
      from t_ent_info t1 ,t_station_info t2,t_ent_shuru t3
      where t1.t_ent_id=t2.t_ent_id and t2.t_station_id=t3.t_station_id
      and t1.need_data='1' and t3.data>=trunc(sysdate,'yyyy')
      group by t3.t_station_id,t1.type
    ) aa,

    (
      --water
      select outputcode,count(1) cnt,sum(avgflow) val1,sum(avgflow*cod) val2,sum(avgflow*nh3) val3
      from t_ent_info t1 ,t_station_info t2,PS_Water_Hour_Data@dblink_mid_new t3
      where t1.t_ent_id=t2.t_ent_id and t2.t_station_id=t3.outputcode
      and t1.need_data='1' and t1.type='1'
      and t3.monitortime>=trunc(sysdate,'yyyy')
      group by outputcode

      union
      --gas
      select outputcode,count(1) cnt,sum(avgflow) val1,sum(avgflow*so2) val2,sum(avgflow*nox) val3
      from t_ent_info t1 ,t_station_info t2,PS_gas_Hour_Data@dblink_mid_new t3
      where t1.t_ent_id=t2.t_ent_id and t2.t_station_id=t3.outputcode
      and t1.need_data='1' and t1.type='2'
      and t3.monitortime>=trunc(sysdate,'yyyy')
      group by outputcode
    ) bb
    where aa.t_station_id=bb.outputcode(+)
    and aa.loc_cnt!=cnt --记录数不一致
    --and aa.loc_cnt<cnt --中间库记录数多

--查缺失记录，本地多用left，中间库多用right
select t1.*,t2.*
from t_ent_shuru t1 left join PS_gas_Hour_Data@dblink_mid_new t2
on (t1.t_station_id=t2.outputcode and t1.data=t2.monitortime)
where t1.t_station_id='33330206000012' and t1.data>=trunc(sysdate,'yyyy')
and t2.outputcode is null --本地多
--and t2.t_station_id is null --中间库多


--比对 本地 最新中间库 老中间库
select t2.outputcode,t2.monitortime,t1.outputcode
from PS_gas_Hour_Data@dblink_mid_new t1 right join ps_gas_output_hour_data@dblink_mid t2
on (t1.outputcode=t2.outputcode and t1.monitortime=t2.monitortime)
where t2.outputcode='33330621002072' and t2.monitortime>=trunc(sysdate,'yyyy')
and t1.outputcode is null

select count(1) from ps_gas_output_hour_data@dblink_mid
where outputcode='33330621002072' and monitortime>=trunc(sysdate,'yyyy')
select count(1) from PS_gas_Hour_Data@dblink_mid_new
where outputcode='33330522000062' and monitortime>=trunc(sysdate,'yyyy')


