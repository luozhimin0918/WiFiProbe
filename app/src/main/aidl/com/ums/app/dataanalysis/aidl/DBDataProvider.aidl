package com.ums.app.dataanalysis.aidl;

interface DBDataProvider{

   List<Bundle> getDBData(long startTime,long endTime);
}