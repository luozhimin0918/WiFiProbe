package com.ums.wifiprobe.data.database.helper;

import android.database.Cursor;
import android.util.Log;

import com.ums.wifiprobe.app.WPApplication;
import com.ums.wifiprobe.service.greendao.MacBrandInfo;
import com.ums.wifiprobe.service.greendao.MacBrandInfoDao;
import com.ums.wifiprobe.service.greendao.MacProbeInfo;
import com.ums.wifiprobe.service.greendao.MacProbeInfoDao;
import com.ums.wifiprobe.service.greendao.MacStatisticsInfo;
import com.ums.wifiprobe.service.greendao.MacStatisticsInfoDao;
import com.ums.wifiprobe.utils.TimeUtils;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenzhy on 2017/10/12.
 */

public class ProbeInfoDaoHelper {
    //去重查询
    public static List<MacProbeInfo> getDayCustomerList(String offset, String count, String date) {
        String sql = "select *,min(" + MacProbeInfoDao.Properties.Id.columnName + "),sum(" + MacProbeInfoDao.Properties.DurationTime.columnName + ") as durations, count (MAC_PROBE_INFO.MAC) as times  FROM " + MacProbeInfoDao.TABLENAME + "   LEFT JOIN  " + MacStatisticsInfoDao.TABLENAME + " ON MAC_PROBE_INFO.MAC= MAC_STATISTICS_INFO.MAC LEFT JOIN " + MacBrandInfoDao.TABLENAME + " ON MAC_PROBE_INFO.BRAND_MAC=MAC_BRAND_INFO.BRAND_MAC WHERE  " + MacProbeInfoDao.Properties.CreateDate.columnName + "=?  group by MAC_PROBE_INFO.MAC order by " + MacProbeInfoDao.Properties.Id.columnName + " asc limit ? , ?";

        Cursor cursor = WPApplication.gDaoSession.getDatabase().rawQuery(sql, new String[]{date, offset, count});
        List<MacProbeInfo> list = new ArrayList<>();

        try {
            while (cursor.moveToNext()) {
                MacProbeInfo info = new MacProbeInfo();
                info.setId(cursor.getLong(cursor.getColumnIndex(MacProbeInfoDao.Properties.Id.columnName)));
                info.setMac(cursor.getString(cursor.getColumnIndex(MacProbeInfoDao.Properties.Mac.columnName)));
                info.setRssi(cursor.getInt(cursor.getColumnIndex(MacProbeInfoDao.Properties.Rssi.columnName)));
                info.setCreateDate(cursor.getString(cursor.getColumnIndex(MacProbeInfoDao.Properties.CreateDate.columnName)));
                info.setCreateTime(cursor.getLong(cursor.getColumnIndex(MacProbeInfoDao.Properties.CreateTime.columnName)));
                info.setCreateHour(cursor.getInt(cursor.getColumnIndex(MacProbeInfoDao.Properties.CreateHour.columnName)));
                info.setLeaveTime(cursor.getLong(cursor.getColumnIndex(MacProbeInfoDao.Properties.LeaveTime.columnName)));
                info.setLastTime(cursor.getLong(cursor.getColumnIndex(MacProbeInfoDao.Properties.LastTime.columnName)));
                info.setLeaveHour(cursor.getInt(cursor.getColumnIndex(MacProbeInfoDao.Properties.LeaveHour.columnName)));
                info.setBrandMac(cursor.getString(cursor.getColumnIndex(MacProbeInfoDao.Properties.BrandMac.columnName)));
                info.setProbeTimes(cursor.getInt(cursor.getColumnIndex("times")));
                info.setDurationTime(cursor.getLong(cursor.getColumnIndex("durations")));
                MacBrandInfo macBrandInfo = new MacBrandInfo();
                macBrandInfo.setBrandMac(info.getBrandMac());
                macBrandInfo.setBrandName(cursor.getString(cursor.getColumnIndex(MacBrandInfoDao.Properties.BrandName.columnName)));
                info.setMacBrandInfo(macBrandInfo);
                if (macBrandInfo.getBrandName() == null) {
                    macBrandInfo.setBrandName("其他品牌");
                }
                MacStatisticsInfo macStatisticsInfo = new MacStatisticsInfo();
                macStatisticsInfo.setProbeTimes(cursor.getInt(cursor.getColumnIndex(MacStatisticsInfoDao.Properties.ProbeTimes.columnName)));
                macStatisticsInfo.setCreateTimes(cursor.getLong(cursor.getColumnIndex(MacStatisticsInfoDao.Properties.CreateTimes.columnName)));
                macStatisticsInfo.setMac(info.getMac());
                info.setMacStatisticsInfo(macStatisticsInfo);
                list.add(info);
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            cursor.close();
        }
        return list;
    }

    //获取新老顾客
    public static List<MacProbeInfo> getDayCustomerList(boolean isOld, String offset, String count, String date) {
        long timeMillions = TimeUtils.getTimeMillions(date);
        String sign = isOld ? "<" : ">";
        String sql = "select *,min(" + MacProbeInfoDao.Properties.Id.columnName + "),sum(" + MacProbeInfoDao.Properties.DurationTime.columnName + ")  as durations , count (MAC_PROBE_INFO.MAC) as times  FROM " + MacProbeInfoDao.TABLENAME + "   LEFT JOIN  " + MacStatisticsInfoDao.TABLENAME + " ON MAC_PROBE_INFO.MAC= MAC_STATISTICS_INFO.MAC LEFT JOIN " + MacBrandInfoDao.TABLENAME + " ON MAC_PROBE_INFO.BRAND_MAC=MAC_BRAND_INFO.BRAND_MAC WHERE  " + MacProbeInfoDao.Properties.CreateDate.columnName + "=?  and " + MacStatisticsInfoDao.Properties.CreateTimes.columnName + " " + sign + " " + timeMillions + " group by MAC_PROBE_INFO.MAC order by " + MacProbeInfoDao.Properties.Id.columnName + " asc limit ? , ?";
        Cursor cursor = WPApplication.gDaoSession.getDatabase().rawQuery(sql, new String[]{date, offset, count});
        List<MacProbeInfo> list = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                MacProbeInfo info = new MacProbeInfo();
                info.setId(cursor.getLong(cursor.getColumnIndex(MacProbeInfoDao.Properties.Id.columnName)));
                info.setMac(cursor.getString(cursor.getColumnIndex(MacProbeInfoDao.Properties.Mac.columnName)));
                info.setRssi(cursor.getInt(cursor.getColumnIndex(MacProbeInfoDao.Properties.Rssi.columnName)));
                info.setCreateDate(cursor.getString(cursor.getColumnIndex(MacProbeInfoDao.Properties.CreateDate.columnName)));
                info.setCreateTime(cursor.getLong(cursor.getColumnIndex(MacProbeInfoDao.Properties.CreateTime.columnName)));
                info.setCreateHour(cursor.getInt(cursor.getColumnIndex(MacProbeInfoDao.Properties.CreateHour.columnName)));
                info.setLeaveTime(cursor.getLong(cursor.getColumnIndex(MacProbeInfoDao.Properties.LeaveTime.columnName)));
                info.setLastTime(cursor.getLong(cursor.getColumnIndex(MacProbeInfoDao.Properties.LastTime.columnName)));
                info.setLeaveHour(cursor.getInt(cursor.getColumnIndex(MacProbeInfoDao.Properties.LeaveHour.columnName)));
                info.setBrandMac(cursor.getString(cursor.getColumnIndex(MacProbeInfoDao.Properties.BrandMac.columnName)));
                info.setProbeTimes(cursor.getInt(cursor.getColumnIndex("times")));
                info.setDurationTime(cursor.getLong(cursor.getColumnIndex("durations")));
                MacBrandInfo macBrandInfo = new MacBrandInfo();
                macBrandInfo.setBrandMac(info.getBrandMac());
                macBrandInfo.setBrandName(cursor.getString(cursor.getColumnIndex(MacBrandInfoDao.Properties.BrandName.columnName)));
                info.setMacBrandInfo(macBrandInfo);
                if (macBrandInfo.getBrandName() == null) {
                    macBrandInfo.setBrandName("其他品牌");
                }
                MacStatisticsInfo macStatisticsInfo = new MacStatisticsInfo();
                macStatisticsInfo.setProbeTimes(cursor.getInt(cursor.getColumnIndex(MacStatisticsInfoDao.Properties.ProbeTimes.columnName)));
                macStatisticsInfo.setCreateTimes(cursor.getLong(cursor.getColumnIndex(MacStatisticsInfoDao.Properties.CreateTimes.columnName)));
                macStatisticsInfo.setMac(info.getMac());
                info.setMacStatisticsInfo(macStatisticsInfo);
                list.add(info);
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            cursor.close();
        }
        return list;
    }

    //去重查询5分钟内顾客 before timeMillions 5分钟=5*60*1000
    public static List<MacProbeInfo> getDayCustomerList(String offset, String count, String date, long before) {
        long beforeCreateTime = System.currentTimeMillis() - before;
        String sql = "select *,min(" + MacProbeInfoDao.Properties.Id.columnName + "),sum(" + MacProbeInfoDao.Properties.DurationTime.columnName + ") as durations, count (MAC_PROBE_INFO.MAC) as times  FROM " + MacProbeInfoDao.TABLENAME + "   LEFT JOIN  " + MacStatisticsInfoDao.TABLENAME + " ON MAC_PROBE_INFO.MAC= MAC_STATISTICS_INFO.MAC LEFT JOIN " + MacBrandInfoDao.TABLENAME + " ON MAC_PROBE_INFO.BRAND_MAC=MAC_BRAND_INFO.BRAND_MAC WHERE  " + MacProbeInfoDao.Properties.CreateDate.columnName + "=? AND  MAC_PROBE_INFO.LAST_TIME>" + beforeCreateTime + "  group by MAC_PROBE_INFO.MAC order by " + MacProbeInfoDao.Properties.Id.columnName + " asc limit ? , ?";

        Cursor cursor = WPApplication.gDaoSession.getDatabase().rawQuery(sql, new String[]{date, offset, count});
        List<MacProbeInfo> list = new ArrayList<>();

        try {
            while (cursor.moveToNext()) {
                MacProbeInfo info = new MacProbeInfo();
                info.setId(cursor.getLong(cursor.getColumnIndex(MacProbeInfoDao.Properties.Id.columnName)));
                info.setMac(cursor.getString(cursor.getColumnIndex(MacProbeInfoDao.Properties.Mac.columnName)));
                info.setRssi(cursor.getInt(cursor.getColumnIndex(MacProbeInfoDao.Properties.Rssi.columnName)));
                info.setCreateDate(cursor.getString(cursor.getColumnIndex(MacProbeInfoDao.Properties.CreateDate.columnName)));
                info.setCreateTime(cursor.getLong(cursor.getColumnIndex(MacProbeInfoDao.Properties.CreateTime.columnName)));
                info.setCreateHour(cursor.getInt(cursor.getColumnIndex(MacProbeInfoDao.Properties.CreateHour.columnName)));
                info.setLeaveTime(cursor.getLong(cursor.getColumnIndex(MacProbeInfoDao.Properties.LeaveTime.columnName)));
                info.setLastTime(cursor.getLong(cursor.getColumnIndex(MacProbeInfoDao.Properties.LastTime.columnName)));
                info.setLeaveHour(cursor.getInt(cursor.getColumnIndex(MacProbeInfoDao.Properties.LeaveHour.columnName)));
                info.setBrandMac(cursor.getString(cursor.getColumnIndex(MacProbeInfoDao.Properties.BrandMac.columnName)));
                info.setProbeTimes(cursor.getInt(cursor.getColumnIndex("times")));
                info.setDurationTime(cursor.getLong(cursor.getColumnIndex("durations")));
                MacBrandInfo macBrandInfo = new MacBrandInfo();
                macBrandInfo.setBrandMac(info.getBrandMac());
                macBrandInfo.setBrandName(cursor.getString(cursor.getColumnIndex(MacBrandInfoDao.Properties.BrandName.columnName)));
                info.setMacBrandInfo(macBrandInfo);
                if (macBrandInfo.getBrandName() == null) {
                    macBrandInfo.setBrandName("其他品牌");
                }
                MacStatisticsInfo macStatisticsInfo = new MacStatisticsInfo();
                macStatisticsInfo.setProbeTimes(cursor.getInt(cursor.getColumnIndex(MacStatisticsInfoDao.Properties.ProbeTimes.columnName)));
                macStatisticsInfo.setCreateTimes(cursor.getLong(cursor.getColumnIndex(MacStatisticsInfoDao.Properties.CreateTimes.columnName)));
                macStatisticsInfo.setMac(info.getMac());
                info.setMacStatisticsInfo(macStatisticsInfo);
                list.add(info);
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            cursor.close();
        }
        return list;
    }

    //去重查询5分钟内顾客 before timeMillions 5分钟=5*60*1000
    public static int getDayCustomerCount(String date, long before) {
        long beforeCreateTime = System.currentTimeMillis() - before;
        String sql = "select count(DISTINCT MAC_PROBE_INFO.MAC)   FROM " + MacProbeInfoDao.TABLENAME + "  WHERE  " + MacProbeInfoDao.Properties.CreateDate.columnName + "=? AND  MAC_PROBE_INFO.LAST_TIME>=" + beforeCreateTime ;

        Cursor cursor = WPApplication.gDaoSession.getDatabase().rawQuery(sql, new String[]{date});
        int counts = 0;
        try {
            while (cursor.moveToNext()) {
                counts = cursor.getInt(0);

            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            cursor.close();
        }
        getDayCustomerCount(date, before, 0);
        return counts;
    }

    //去重查询5分钟内顾客 before timeMillions 5分钟=5*60*1000
    public static List<MacProbeInfo> getDayCustomerCount(String date, long before, int length) {
        long beforeCreateTime = System.currentTimeMillis() - before;
        String sql = "select *  FROM " + MacProbeInfoDao.TABLENAME + "  WHERE  " + MacProbeInfoDao.Properties.CreateDate.columnName + "=? AND MAC_PROBE_INFO.LAST_TIME>=" + beforeCreateTime +" group by MAC_PROBE_INFO.MAC ";
        Cursor cursor = WPApplication.gDaoSession.getDatabase().rawQuery(sql, new String[]{date});
        List<MacProbeInfo> list = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                MacProbeInfo info = new MacProbeInfo();
                info.setId(cursor.getLong(cursor.getColumnIndex(MacProbeInfoDao.Properties.Id.columnName)));
                info.setMac(cursor.getString(cursor.getColumnIndex(MacProbeInfoDao.Properties.Mac.columnName)));
                info.setRssi(cursor.getInt(cursor.getColumnIndex(MacProbeInfoDao.Properties.Rssi.columnName)));
                info.setCreateDate(cursor.getString(cursor.getColumnIndex(MacProbeInfoDao.Properties.CreateDate.columnName)));
                info.setCreateTime(cursor.getLong(cursor.getColumnIndex(MacProbeInfoDao.Properties.CreateTime.columnName)));
                info.setCreateHour(cursor.getInt(cursor.getColumnIndex(MacProbeInfoDao.Properties.CreateHour.columnName)));
                info.setLeaveTime(cursor.getLong(cursor.getColumnIndex(MacProbeInfoDao.Properties.LeaveTime.columnName)));
                info.setLastTime(cursor.getLong(cursor.getColumnIndex(MacProbeInfoDao.Properties.LastTime.columnName)));
                info.setLeaveHour(cursor.getInt(cursor.getColumnIndex(MacProbeInfoDao.Properties.LeaveHour.columnName)));
                info.setBrandMac(cursor.getString(cursor.getColumnIndex(MacProbeInfoDao.Properties.BrandMac.columnName)));
                list.add(info);
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            cursor.close();
        }
        return list;
    }


    //去重查询5分钟内顾客 before timeMillions 5分钟=5*60*1000
    public static List<MacProbeInfo> getDayCustomerCount() {
        String sql = "select *  FROM " + MacProbeInfoDao.TABLENAME + " where MAC_PROBE_INFO.CREATE_TIME in(select MAC_PROBE_INFO.CREATE_TIME from MAC_PROBE_INFO group by MAC_PROBE_INFO.CREATE_TIME having count(MAC_PROBE_INFO.CREATE_TIME) >1)";
        Cursor cursor = WPApplication.gDaoSession.getDatabase().rawQuery(sql, new String[]{});
        List<MacProbeInfo> list = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                MacProbeInfo info = new MacProbeInfo();
                info.setId(cursor.getLong(cursor.getColumnIndex(MacProbeInfoDao.Properties.Id.columnName)));
                info.setMac(cursor.getString(cursor.getColumnIndex(MacProbeInfoDao.Properties.Mac.columnName)));
                info.setRssi(cursor.getInt(cursor.getColumnIndex(MacProbeInfoDao.Properties.Rssi.columnName)));
                info.setCreateDate(cursor.getString(cursor.getColumnIndex(MacProbeInfoDao.Properties.CreateDate.columnName)));
                info.setCreateTime(cursor.getLong(cursor.getColumnIndex(MacProbeInfoDao.Properties.CreateTime.columnName)));
                info.setCreateHour(cursor.getInt(cursor.getColumnIndex(MacProbeInfoDao.Properties.CreateHour.columnName)));
                info.setLeaveTime(cursor.getLong(cursor.getColumnIndex(MacProbeInfoDao.Properties.LeaveTime.columnName)));
                info.setLastTime(cursor.getLong(cursor.getColumnIndex(MacProbeInfoDao.Properties.LastTime.columnName)));
                info.setLeaveHour(cursor.getInt(cursor.getColumnIndex(MacProbeInfoDao.Properties.LeaveHour.columnName)));
                info.setBrandMac(cursor.getString(cursor.getColumnIndex(MacProbeInfoDao.Properties.BrandMac.columnName)));
                list.add(info);
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            cursor.close();
        }
        return list;
    }


}
