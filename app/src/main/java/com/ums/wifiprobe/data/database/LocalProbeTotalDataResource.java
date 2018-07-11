package com.ums.wifiprobe.data.database;

import android.os.SystemClock;
import android.util.Log;

import com.ums.wifiprobe.app.WPApplication;
import com.ums.wifiprobe.data.DataResource;
import com.ums.wifiprobe.data.database.helper.ProbeTotalInfoDaoHelper;
import com.ums.wifiprobe.service.ProbeService;
import com.ums.wifiprobe.service.ProbeServiceConstant;
import com.ums.wifiprobe.service.greendao.MacProbeInfo;
import com.ums.wifiprobe.service.greendao.MacProbeInfoDao;
import com.ums.wifiprobe.service.greendao.MacTotalInfo;
import com.ums.wifiprobe.service.greendao.MacTotalInfoDao;
import com.ums.wifiprobe.service.greendao.RssiInfo;
import com.ums.wifiprobe.utils.TimeUtils;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

/**
 * Created by chenzhy on 2017/9/19.
 */

public class LocalProbeTotalDataResource implements DataResource<MacTotalInfo> {

    //查询周报表/日报表
    //周报表，当前周及上周数据  //周一至周日
    //日报表，当日及前日数据 //00-24时

    //type /周一/周二/////0 1 2 3 4 5   23

    //如果是week的话，约定传周末,,, moonth的话，约定传1号
    @Override
    public void getTask(String scaleValue, String scale, String date, GetTaskCallback callback) {
        Log.e("WiFiProbe-Data", "getTask--LocalProbe-- scaleValue=" + scaleValue + " scale=" + scale + " date=" + date);
        String tempDate = date;
        boolean isDirty = false;//如果是当天或这周数据，则不缓存，因为当天数据会一直在变化
        int curYear = TimeUtils.getYear(System.currentTimeMillis());
        int taskYear = TimeUtils.getYear(TimeUtils.getTimeMillions(date));
        switch (scale) {
            case "months":
                tempDate = TimeUtils.getFirstDateOfMonth(date);//本月1号
                if (curYear == taskYear) {
                    if (TimeUtils.getMonthsOfYear(tempDate) >= TimeUtils.getMonthsOfYear(TimeUtils.getDate(System.currentTimeMillis()))) {
                        isDirty = true;
                    }
                } else if (taskYear > curYear) {
                    isDirty = true;
                }
                break;
            case "weeks":
                tempDate = TimeUtils.getSundayDate(date);//周末 周统计数据
                if (curYear == taskYear) {
                    if (TimeUtils.getWeeksOfYear(tempDate) >= TimeUtils.getWeeksOfYear(TimeUtils.getDate(System.currentTimeMillis()))) {
                        //本周数据，需从原始数据库中读
                        isDirty = true;
                    }
                } else if (taskYear > curYear) {
                    isDirty = true;
                }
                break;
            case "days": //日统计数据
                if (TimeUtils.getTimeMillions(tempDate) >= (TimeUtils.getTimeMillions(TimeUtils.getDate(System.currentTimeMillis())))) {
                    //当天数据，需从原始数据库中读
                    isDirty = true;
                }
                break;
        }
        MacTotalInfo totalInfo = null;
        if (!isDirty) {
            totalInfo = WPApplication.gMacTotalInfoDao.queryBuilder().where(MacTotalInfoDao.Properties.Scale.eq(scale), MacTotalInfoDao.Properties.ScaleValue.eq(scaleValue), MacTotalInfoDao.Properties.Date.eq(tempDate)).unique();

        }
        if (totalInfo != null) {
            callback.OnTaskLoaded(totalInfo);
        } else {
            //若为week 则查询7天统计数据 //若为日，则查询当天数据
            MacTotalInfo info1 = new MacTotalInfo();
            List<RssiInfo> list = new ArrayList<>();
            if (scale.equals("months")) {
                list.addAll(ProbeTotalInfoDaoHelper.getMonthCustomerCount(false, tempDate, ProbeServiceConstant.maxRssi, ProbeServiceConstant.middleRssi, ProbeServiceConstant.minRssi));
                list.addAll(ProbeTotalInfoDaoHelper.getMonthCustomerCount(true, tempDate, ProbeServiceConstant.maxRssi, ProbeServiceConstant.middleRssi, ProbeServiceConstant.minRssi));

            } else if (scale.equals("weeks")) {
                long mondayMillions = TimeUtils.getTimeMillions(tempDate);
                long sundayMillions = TimeUtils.getTimeMillions(tempDate) + 7 * 24 * 60 * 60 * 1000l;
                list.addAll(ProbeTotalInfoDaoHelper.getWeekCustomerCount(false, mondayMillions, sundayMillions, ProbeServiceConstant.maxRssi, ProbeServiceConstant.middleRssi, ProbeServiceConstant.minRssi));
                list.addAll(ProbeTotalInfoDaoHelper.getWeekCustomerCount(true, mondayMillions, sundayMillions, ProbeServiceConstant.maxRssi, ProbeServiceConstant.middleRssi, ProbeServiceConstant.minRssi));

            } else if (scale.equals("days")) {
                list.addAll(ProbeTotalInfoDaoHelper.getDayCustomerCount(false, date, ProbeServiceConstant.maxRssi, ProbeServiceConstant.middleRssi, ProbeServiceConstant.minRssi));
                list.addAll(ProbeTotalInfoDaoHelper.getDayCustomerCount(true, date, ProbeServiceConstant.maxRssi, ProbeServiceConstant.middleRssi, ProbeServiceConstant.minRssi));
            } else {
                callback.onDataNotAvaliable();
                return;
            }
            info1.setDate(date);
            info1.setScale(scale);
            info1.setScaleValue(scaleValue);
            if (!isDirty) {
                long id = WPApplication.gMacTotalInfoDao.insert(info1);//将该条原始数据存储至统计数据库中
                for (RssiInfo info : list) {
                    info.setOwenerId(id);
                }
                WPApplication.gRssiInfoDao.insertInTx(list);
            }
            info1.setRssiInfos(list);
            callback.OnTaskLoaded(info1);
        }
    }

    private List<MacTotalInfo> queryMacTotalData(final int index, final int dirty, final String[] periods, final String scale, final String[] scaleValues, final String date) {
        final List<MacTotalInfo> totalInfoList = new ArrayList<>();

        WPApplication.gDaoSession.runInTx(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < periods.length; i++) {
                    //分别查询近/中/远/所有的数据

                    MacTotalInfo macTotalInfo = null;
                    if (dirty == 0 || dirty == 1 && i < index) {
                        macTotalInfo = WPApplication.gMacTotalInfoDao.queryBuilder().where(MacTotalInfoDao.Properties.Scale.eq(scale), MacTotalInfoDao.Properties.ScaleValue.eq(scaleValues[i]), MacTotalInfoDao.Properties.Date.eq(date)).unique();
                    }
                    if (macTotalInfo != null) {
                        totalInfoList.add(macTotalInfo);
                    } else {
                        List<RssiInfo> rssiInfos = new ArrayList<RssiInfo>();
                        if (dirty == 0 || dirty == 1 && i <= index) {
                            //查询该天数据总量
                            //向原始数据库查询？新顾客/老顾客

                            if (scale.equals("hour")) {
                                //向原始数据库查询？新顾客/老顾客
                                rssiInfos.addAll(ProbeTotalInfoDaoHelper.getHourCustomerCount(false, date, periods[i], ProbeServiceConstant.maxRssi, ProbeServiceConstant.middleRssi, ProbeServiceConstant.minRssi));
                                rssiInfos.addAll(ProbeTotalInfoDaoHelper.getHourCustomerCount(true, date, periods[i], ProbeServiceConstant.maxRssi, ProbeServiceConstant.middleRssi, ProbeServiceConstant.minRssi));

                            } else {
                                rssiInfos.addAll(ProbeTotalInfoDaoHelper.getDayCustomerCount(false, periods[i], ProbeServiceConstant.maxRssi, ProbeServiceConstant.middleRssi, ProbeServiceConstant.minRssi));
                                rssiInfos.addAll(ProbeTotalInfoDaoHelper.getDayCustomerCount(true, periods[i], ProbeServiceConstant.maxRssi, ProbeServiceConstant.middleRssi, ProbeServiceConstant.minRssi));

                            }

                            //加上hour的数据 周和月均为查询天 只有天查询小时
                        }
                        macTotalInfo = new MacTotalInfo();
                        macTotalInfo.setDate(date);
                        macTotalInfo.setScale(scale);
                        macTotalInfo.setScaleValue(scaleValues[i]);
                        if (dirty == 0 || dirty == 1 && i < index) {
                            long id = WPApplication.gMacTotalInfoDao.insert(macTotalInfo);//将该条原始数据存储至统计数据库中
                            for (RssiInfo info : rssiInfos) {
                                info.setOwenerId(id);
                            }
                            WPApplication.gRssiInfoDao.insertInTx(rssiInfos);
                        }
                        macTotalInfo.setRssiInfos(rssiInfos);
                        //需要添加统计信息
                        totalInfoList.add(macTotalInfo);
                    }
                }


            }
        });
        return totalInfoList;
    }

    @Override
    public void getTasks(final String scaleValue, final String scale, final String date, final LoadTasksCallback callback) {
        Log.e("WiFiProbe-Data", "getTasks-LocalProbe-- scaleValue=" + scaleValue + " scale=" + scale + " date=" + date);
        List<MacTotalInfo> totalInfoList = new ArrayList<>();
        int isDirty = 0; //0 上个时间段 1当前时间段 2未来时间段        //如果是当天或这周数据，则不缓存，因为当天数据会一直在变化-------只要是在今天或今天之后的都不应该予以保存
        int index = 0;//确定今天是哪天，对于今天之前的数据，从统计表中读，并存，当天的数据只读 后面的数据都初始化为0
        //scale为周或日
        int curYear = TimeUtils.getYear(System.currentTimeMillis());
        int taskYear = TimeUtils.getYear(TimeUtils.getTimeMillions(date));

        switch (scale) {

            case "month":
                int curMonth = TimeUtils.getMonthsOfYear(TimeUtils.getDate(System.currentTimeMillis()));
                if (curYear == taskYear) {
                    //当年的数据
                    if (TimeUtils.getMonthsOfYear(date) == curMonth) {
                        isDirty = 1;//本月
                    } else if (TimeUtils.getMonthsOfYear(date) > curMonth) {
                        isDirty = 2;//下月及以后
                    }
                } else if (taskYear > curYear) {
                    //后面的年份
                    isDirty = 2;
                }

                final String[] monthDates = TimeUtils.getDatesOfMonth(date);
                String curDate0 = TimeUtils.getDate(System.currentTimeMillis());
                for (int i = 0; i < monthDates.length; i++) {
                    if (curDate0.equals(monthDates[i])) {
                        index = i;
                    }
                }

                totalInfoList = queryMacTotalData(index, isDirty, monthDates, "day", monthDates, TimeUtils.getFirstDateOfMonth(date));

                break;

            case "week":
                String curDate = TimeUtils.getDate(System.currentTimeMillis());
                if (curYear == taskYear) {
                    if (TimeUtils.getWeeksOfYear(date) == TimeUtils.getWeeksOfYear(TimeUtils.getDate(System.currentTimeMillis()))) {
                        //本周数据，需从原始数据库中读
                        isDirty = 1;
                    } else if (TimeUtils.getWeeksOfYear(date) > TimeUtils.getWeeksOfYear(TimeUtils.getDate(System.currentTimeMillis()))) {
                        //下周数据，无
                        isDirty = 2;
                    }
                } else if (taskYear > curYear) {
                    isDirty = 2;
                }
                //周一-周日
                final String[] dates = TimeUtils.getWeekDates(date);
                final String[] days = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

                for (int i = 0; i < dates.length; i++) {
                    if (curDate.equals(dates[i]))
                        index = i;
                }
                totalInfoList = queryMacTotalData(index, isDirty, dates, "day", days, TimeUtils.getSundayDate(date));
                break;
            case "day":
                //00-23
                //是否是当天，是否是当前小时
                if (TimeUtils.getTimeMillions(date) == (TimeUtils.getTimeMillions(TimeUtils.getDate(System.currentTimeMillis())))) {
                    //当天数据，需从原始数据库中读
                    isDirty = 1;
                } else if (TimeUtils.getTimeMillions(date) > (TimeUtils.getTimeMillions(TimeUtils.getDate(System.currentTimeMillis())))) {
                    isDirty = 2;//明天及以后的数据
                }
                String curHour = TimeUtils.getHour(System.currentTimeMillis()) + "";
                final String[] hours = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
                for (int i = 0; i < hours.length; i++) {
                    if (curHour.equals(hours[i])) {
                        index = i;
                        break;
                    }
                }
                totalInfoList = queryMacTotalData(index, isDirty, hours, "hour", hours, date);
                break;
        }
        if (totalInfoList.size() >= 1) {
            callback.onTasksLoaded(totalInfoList);
        } else {
            callback.onDataNotAvaliable();
        }

    }

    @Override
    public void saveTask(MacTotalInfo info) {

    }

    @Override
    public void saveTasks(List<MacTotalInfo> list) {

    }

    @Override
    public void clearTasks() {

    }


}
