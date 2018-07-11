package com.ums.wifiprobe.data.database;

import android.util.Log;

import com.ums.wifiprobe.app.WPApplication;
import com.ums.wifiprobe.data.DataResource;
import com.ums.wifiprobe.data.database.helper.BrandTotalInfoDaoHelper;
import com.ums.wifiprobe.data.database.helper.ProbeTotalInfoDaoHelper;
import com.ums.wifiprobe.service.greendao.BrandTotalInfo;
import com.ums.wifiprobe.service.greendao.BrandTotalInfoDao;
import com.ums.wifiprobe.service.greendao.MacProbeInfoDao;
import com.ums.wifiprobe.service.greendao.MacTotalInfo;
import com.ums.wifiprobe.service.greendao.MacTotalInfoDao;
import com.ums.wifiprobe.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by chenzhy on 2017/9/19.
 */

public class LocalBrandTotalDataResource implements DataResource<BrandTotalInfo> {

    //查询周报表/日报表
    //周报表，当前周及上周数据  //周一至周日
    //日报表，当日及前日数据 //00-24时

    //type /周一/周二/////0 1 2 3 4 5   23

    //如果是week的话，约定传周末,,,moonth的话，约定传1号
    @Override
    public void getTask(String scaleValue, String scale, String date, GetTaskCallback callback) {

    }


    @Override
    public void getTasks(final String scaleValue, final String scale, final String date, final LoadTasksCallback callback) {
        List<BrandTotalInfo> totalInfoList = new ArrayList<>();
        boolean isDirty = false;//如果是当天或这周数据，则不缓存，因为当天数据会一直在变化-------只要是在今天或今天之后的都不应该予以保存
        int index = 0;//确定今天是哪天，对于今天之前的数据，从统计表中读，并存，当天的数据只读 后面的数据都初始化为0
        //scale为周或日
        int curYear = TimeUtils.getYear(System.currentTimeMillis());
        int taskYear = TimeUtils.getYear(TimeUtils.getTimeMillions(date));
        switch (scale) {
            case "months":
                if (taskYear == curYear) {
                    if (TimeUtils.getMonthsOfYear(date) >= TimeUtils.getMonthsOfYear(TimeUtils.getDate(System.currentTimeMillis()))) {
                        isDirty = true;
                    }
                } else if (taskYear > curYear) {
                    isDirty = true;
                }
                //直接查询1整月的数据

                if (!isDirty) {
                    //先向统计数据库查询
                    totalInfoList = WPApplication.gBrandTotalInfoDao.queryBuilder().where(BrandTotalInfoDao.Properties.Scale.eq(scale), BrandTotalInfoDao.Properties.ScaleValue.eq(scaleValue), BrandTotalInfoDao.Properties.Date.eq(date)).list();

                }
                if (totalInfoList == null || totalInfoList.size() == 0) {
                    //重新向原始数据库查询
                    String firstDate = TimeUtils.getFirstDateOfMonth(date);
                    long startTimeMillions = TimeUtils.getTimeMillions(firstDate);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(startTimeMillions);
                    calendar.add(Calendar.MONTH, 1);
                    long endTimeMillions = calendar.getTimeInMillis();
                    totalInfoList = BrandTotalInfoDaoHelper.getWeekOrMonthCustomerCount(scaleValue, scale, firstDate, startTimeMillions, endTimeMillions);
                    if (totalInfoList != null && totalInfoList.size() > 0) {
                        if (!isDirty)
                            WPApplication.gBrandTotalInfoDao.insertInTx(totalInfoList);
                    }
                }
                break;

            case "weeks":

                String curDate = TimeUtils.getSundayDate(date);//周末 周统计数据
                if (taskYear == curYear) {
                    if (TimeUtils.getWeeksOfYear(curDate) >= TimeUtils.getWeeksOfYear(TimeUtils.getDate(System.currentTimeMillis()))) {
                        //本周数据，需从原始数据库中读
                        isDirty = true;
                    }
                } else if (taskYear > curYear) {
                    isDirty = true;
                }
                if (!isDirty) {
                    totalInfoList = WPApplication.gBrandTotalInfoDao.queryBuilder().where(BrandTotalInfoDao.Properties.Scale.eq(scale), BrandTotalInfoDao.Properties.ScaleValue.eq(scaleValue), BrandTotalInfoDao.Properties.Date.eq(date)).list();

                }
                if (totalInfoList == null || totalInfoList.size() == 0) {
                    //重新向原始数据库查询
                    String firstDate = TimeUtils.getSundayDate(date);
                    long startTimeMillions = TimeUtils.getTimeMillions(firstDate);
                    long endTimeMillions = startTimeMillions + 7 * 24 * 60 * 60 * 1000l;
                    totalInfoList = BrandTotalInfoDaoHelper.getWeekOrMonthCustomerCount(scaleValue, scale, firstDate, startTimeMillions, endTimeMillions);
                    if (totalInfoList != null && totalInfoList.size() > 0) {
                        if (!isDirty)
                            WPApplication.gBrandTotalInfoDao.insertInTx(totalInfoList);
                    }
                }

                break;
            case "days":
                //00-23
                //是否是当天
                if (TimeUtils.getTimeMillions(date) >= (TimeUtils.getTimeMillions(TimeUtils.getDate(System.currentTimeMillis())))) {
                    //当天数据，需从原始数据库中读
                    isDirty = true;
                }
                if (!isDirty) {
                    //先向统计数据库查询
                    totalInfoList = WPApplication.gBrandTotalInfoDao.queryBuilder().where(BrandTotalInfoDao.Properties.Scale.eq(scale), BrandTotalInfoDao.Properties.ScaleValue.eq(scaleValue), BrandTotalInfoDao.Properties.Date.eq(date)).list();
                }
                if (totalInfoList == null || totalInfoList.size() == 0) {
                    //重新向原始数据库查询
                    totalInfoList = BrandTotalInfoDaoHelper.getDayCustomerCount(scaleValue, scale, date);
                    if (totalInfoList != null && totalInfoList.size() > 0) {
                        if (!isDirty)
                            WPApplication.gBrandTotalInfoDao.insertInTx(totalInfoList);
                    }
                }
                break;
        }
        if (totalInfoList.size() >= 1) {
            callback.onTasksLoaded(totalInfoList);
        } else {
            callback.onDataNotAvaliable();
        }

    }

    @Override
    public void saveTask(BrandTotalInfo info) {

    }

    @Override
    public void saveTasks(List<BrandTotalInfo> list) {

    }

    @Override
    public void clearTasks() {

    }


}
