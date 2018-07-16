package com.ums.app.dataanalysis.aidl;

interface DBDataProvider{

//按照交易类型返回数据
   List<Bundle> getDBData(long startTime,long endTime);

//每小时返回一个数据
   List<Bundle> getDBDataEachHour(long startTime,long endTime);
}