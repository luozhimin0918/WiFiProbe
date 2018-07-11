package com.ums.wifiprobe.data.database.helper;

import android.database.Cursor;

import com.ums.wifiprobe.app.WPApplication;
import com.ums.wifiprobe.service.greendao.MacProbeInfoDao;
import com.ums.wifiprobe.service.greendao.MacStatisticsInfoDao;
import com.ums.wifiprobe.service.greendao.RssiInfo;
import com.ums.wifiprobe.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenzhy on 2017/9/20.
 */

public class ProbeTotalInfoDaoHelper {

    //以天为单位来进行统计，天去重，月不去重
    public static List<RssiInfo> getMonthCustomerCount(boolean isOld, String date, int maxRssi, int middle, int minRssi) {
        String[] dates = TimeUtils.getDatesOfMonth(date);
        List<RssiInfo> list = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < dates.length; i++) {
            list.addAll(getDayCustomerCount(isOld, dates[i],  maxRssi,  middle,  minRssi));
        }
        return list;
    }

    //以周为单位来进行统计，天去重，周不去重
    public static List<RssiInfo> getWeekCustomerCount(boolean isOld, long before, long after, int maxRssi, int middle, int minRssi) {
        int count = 0;
        long dateLength = (after - before) / (24 * 60 * 60 * 1000l);
        List<RssiInfo> list = new ArrayList<>();
        String[] dates = new String[(int) dateLength];
        for (int i = 0; i < dateLength; i++) {
            dates[i] = TimeUtils.getDate(before + i * 24 * 60 * 60 * 1000l);
            list.addAll(getDayCustomerCount(isOld, dates[i],  maxRssi,  middle,  minRssi));
        }
        return list;
    }

    //rssi选择范围内去重  需要用group by when case查询 -20  -50 -65 -80
    public static List<RssiInfo> getDayCustomerCount(boolean isOld, String date, int maxRssi, int middle, int minRssi) {
        long timeMillions = TimeUtils.getTimeMillions(date);
        String sign = isOld ? "<" : ">";
        String sql = "select count(*) as totalcount1,count(distinct MAC_PROBE_INFO.MAC) as totalcount, count(distinct case  when  MAC_PROBE_INFO.RSSI >" + minRssi + " and MAC_PROBE_INFO.RSSI <=" + middle + " then MAC_PROBE_INFO.MAC else null end)  far ,count(distinct case when MAC_PROBE_INFO.RSSI >" + middle + " and MAC_PROBE_INFO.RSSI <=" + maxRssi + " then MAC_PROBE_INFO.MAC else null end)  normal , count(distinct case  when MAC_PROBE_INFO.RSSI >" + maxRssi + " then MAC_PROBE_INFO.MAC else null end)  close , count(distinct case  when MAC_PROBE_INFO.RSSI <" + minRssi + " then MAC_PROBE_INFO.MAC else null end)  faraway  FROM " + MacProbeInfoDao.TABLENAME + " LEFT JOIN " + MacStatisticsInfoDao.TABLENAME + " ON MAC_PROBE_INFO.MAC= MAC_STATISTICS_INFO.MAC WHERE " + MacStatisticsInfoDao.Properties.CreateTimes.columnName + " " + sign + " " + timeMillions + " AND " + MacProbeInfoDao.Properties.CreateDate.columnName + " =?";
        Cursor cursor = WPApplication.gDaoSession.getDatabase().rawQuery(sql, new String[]{date});
        List<RssiInfo> list = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                int count1 = cursor.getInt(cursor.getColumnIndex("totalcount1"));//非去重总量
                list.add(new RssiInfo(-1000, 0, count1,isOld,false));
                int count = cursor.getInt(cursor.getColumnIndex("totalcount"));//总量
                list.add(new RssiInfo(-1000, 0, count,isOld,true));
                int far = cursor.getInt(cursor.getColumnIndex("far"));
                list.add(new RssiInfo(minRssi, middle, far,isOld,true));
                int normal = cursor.getInt(cursor.getColumnIndex("normal"));
                list.add(new RssiInfo(middle, maxRssi, normal,isOld,true));
                int close = cursor.getInt(cursor.getColumnIndex("close"));
                list.add(new RssiInfo(maxRssi, 0, close,isOld,true));
                int farway = cursor.getInt(cursor.getColumnIndex("faraway"));
                list.add(new RssiInfo(-1000, minRssi, farway,isOld,true));
                return list;
            }

        } finally {
            cursor.close();
        }
        return list;
    }

    public static List<RssiInfo> getHourCustomerCount(boolean isOld, String date, String hour, int maxRssi, int middle, int minRssi) {
        long timeMillions = TimeUtils.getTimeMillions(date);
        long hourStartTimeMillions = TimeUtils.getTimeMillions(date,hour);
        long hourEndTimeMillions=hourStartTimeMillions+60*60*1000l;
        String sign = isOld ? "<" : ">";
        String sql = "select count(*) as totalcount1,count(distinct MAC_PROBE_INFO.MAC) as totalcount, count(distinct case  when  MAC_PROBE_INFO.RSSI >" + minRssi + " and MAC_PROBE_INFO.RSSI <=" + middle + " then MAC_PROBE_INFO.MAC else null end)  far ,count(distinct case when MAC_PROBE_INFO.RSSI >" + middle + " and MAC_PROBE_INFO.RSSI <=" + maxRssi + " then MAC_PROBE_INFO.MAC else null end)  normal , count(distinct case  when MAC_PROBE_INFO.RSSI >" + maxRssi + "  then MAC_PROBE_INFO.MAC else null end)  close , count(distinct case  when MAC_PROBE_INFO.RSSI <" + minRssi + " then MAC_PROBE_INFO.MAC else null end)  faraway   FROM " + MacProbeInfoDao.TABLENAME + " LEFT JOIN " + MacStatisticsInfoDao.TABLENAME + " ON MAC_PROBE_INFO.MAC= MAC_STATISTICS_INFO.MAC WHERE " + MacStatisticsInfoDao.Properties.CreateTimes.columnName + " " + sign + " " + timeMillions + " AND " + MacProbeInfoDao.Properties.CreateDate.columnName + " =? AND " + MacProbeInfoDao.Properties.LastTime.columnName + ">=? AND " + MacProbeInfoDao.Properties.LastTime.columnName + "<?";
        Cursor cursor = WPApplication.gDaoSession.getDatabase().rawQuery(sql, new String[]{date, hourStartTimeMillions+"",hourEndTimeMillions+""});
        List<RssiInfo> list = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                int count1 = cursor.getInt(cursor.getColumnIndex("totalcount1"));//非去重总量
                list.add(new RssiInfo(-1000, 0, count1,isOld,false));
                int count = cursor.getInt(cursor.getColumnIndex("totalcount"));//总量
                list.add(new RssiInfo(-1000, 0, count,isOld,true));
                int far = cursor.getInt(cursor.getColumnIndex("far"));
                list.add(new RssiInfo(minRssi, middle, far,isOld,true));
                int normal = cursor.getInt(cursor.getColumnIndex("normal"));
                list.add(new RssiInfo(middle, maxRssi, normal,isOld,true));
                int close = cursor.getInt(cursor.getColumnIndex("close"));
                list.add(new RssiInfo(maxRssi, 0, close,isOld,true));
                int farway = cursor.getInt(cursor.getColumnIndex("faraway"));
                list.add(new RssiInfo(-1000, minRssi, farway,isOld,true));

//                Log.d("HAHA", "totalCount1="+count1+" totalCount2=" + count + " far=" + far + " normal=" + normal + " close=" + close + " farway=" + farway);
                return list;
            }

        } finally {
            cursor.close();
        }
        return list;
    }

    public static int getHourCustomerCount(boolean isOld, String date, String hour) {
        long timeMillions = TimeUtils.getTimeMillions(date);
        String sign = isOld ? "<" : ">";
        String sql = "select count (DISTINCT  MAC_PROBE_INFO.MAC  )  FROM " + MacProbeInfoDao.TABLENAME + " LEFT JOIN " + MacStatisticsInfoDao.TABLENAME + " ON MAC_PROBE_INFO.MAC= MAC_STATISTICS_INFO.MAC  WHERE " + MacStatisticsInfoDao.Properties.CreateTimes.columnName + " " + sign + " " + timeMillions + " AND " + MacProbeInfoDao.Properties.CreateDate.columnName + " =? AND " + MacProbeInfoDao.Properties.CreateHour.columnName + "=?";
        Cursor cursor = WPApplication.gDaoSession.getDatabase().rawQuery(sql, new String[]{date, hour});
        int count = 0;
        try {
            while (cursor.moveToNext()) {
                count = cursor.getInt(0);

            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            cursor.close();
        }
        return count;
    }
}
