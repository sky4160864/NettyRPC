##正常请求## ST:01企业 09污水处理厂    （ST:21企业 29污水处理厂##固定时间取数据##  主要用于跨年补取）
客户端注意事项：时间为EXCHANGETIME ; MN2为新值（替换表）
格式：##ST=01;TP=2;MN1=33330522000082;MN2=;BTIME=20130608110000;ETIME=20130608120000;&&EEEE
1.需要查看替换表
2.根据状态表最后时间（EXCHANGETIME），作为开始请求时间(ReqDatasManageImpl.sql01)
3.数据返回时，返回数据需要根据ST作处理，服务端返回的ST为请求ST
4.用merge处理，SEND_CTL用case when判断是否有更新
5.用最后一条记录更新status表

服务端：
1.根据请求时间，查MONITORTIME为当年的，大于EXCHANGETIME记录，全部返回，数据太多的话是否会出现超时？？？？


##补数据##
时间为MONITORTIME
MN2为新值，格式如下：##ST=11;TP=2;RQ=1;MN1=33330522000082;MN2=;DATATIME=20130719122920;BEGTIME=201701;ENDTIME=201702;COUNT=230;##
1.客户端每天一次请求，从年初至本月初（不包括本月），每月一条请求
2.服务端收到请求后，核对count值，如果不一致,则返回该月记录数
4.返回数据需要根据ST作处理，服务端返回的ST为请求ST



##固定时间取数据##  主要用于跨年补取 （合并在##正常请求## ST不一致而已）
ST:21企业 29污水处理厂
1.设置全部固定、或者填点源编号
2.设置开始结束时间



测试：
1.客户端是否会重连？  不会(20180617改成会重连)
2.服务端的connection是否会失效？ 失效~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
3.ST测试，T_ENT_SHURU_STATUS未更新（已处理0619）
4.有替换值时，入库时用的替换值，需改
5.09测试OK


环境配置 EXE4J_HOME

create table SKPW_REPLACE
(
  old_mn  VARCHAR2(30),
  new_mn  VARCHAR2(30),
  rp_desc VARCHAR2(30)
);


以下为测试环境需要的表
create table T_ENT_SHURU2
(
  MAIN_ID      VARCHAR2(50) not null,
  T_ENT_ID     VARCHAR2(50),
  SO2          NUMBER(18,2),
  NOX          NUMBER(18,2),
  GAS          NUMBER(18,2),
  WATER        NUMBER(18,2),
  COD          NUMBER(18,2),
  NH3          NUMBER(18,2),
  DATA         DATE,
  GUID         VARCHAR2(40) not null,
  P            VARCHAR2(50),
  TYPE         VARCHAR2(50),
  TYPE_NAME    VARCHAR2(50),
  T_STATION_ID VARCHAR2(20),
  RECEIVE_ZX   VARCHAR2(1) default 0,
  SEND_CTL     VARCHAR2(1) default 0,
  RTIME        DATE,
  SEND2S       NUMBER default 0
);
create unique index T_ENT_SHURU2_UK on T_ENT_SHURU2 (T_STATION_ID, DATA);

create table T_ENT_SHURU_STATUS2
(
  MAIN_ID      NUMBER,
  T_STATION_ID VARCHAR2(20),
  EXTIME       DATE,
  UPTTIME      DATE
)
