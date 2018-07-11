package com.ums.wifiprobe.data.database.helper;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.ums.wifiprobe.app.WPApplication;
import com.ums.wifiprobe.service.greendao.BrandTotalInfo;
import com.ums.wifiprobe.service.greendao.BrandTotalInfoDao;
import com.ums.wifiprobe.service.greendao.MacBrandInfo;
import com.ums.wifiprobe.service.greendao.MacBrandInfoDao;
import com.ums.wifiprobe.service.greendao.MacProbeInfoDao;
import com.ums.wifiprobe.service.greendao.MacStatisticsInfoDao;
import com.ums.wifiprobe.service.greendao.MacTotalInfo;
import com.ums.wifiprobe.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenzhy on 2017/9/20.
 */

public class BrandTotalInfoDaoHelper {

    public static List<BrandTotalInfo> getWeekOrMonthCustomerCount(final String scaleValue, final String scale, final String date,long startTime,long endTime) {
        String sql = "select MAC_BRAND_INFO.BRAND_NAME ,count (DISTINCT  MAC_PROBE_INFO.MAC ) as brandcount from " + MacProbeInfoDao.TABLENAME + " LEFT JOIN " + MacBrandInfoDao.TABLENAME + " ON MAC_PROBE_INFO.BRAND_MAC= MAC_BRAND_INFO.BRAND_MAC WHERE " + MacProbeInfoDao.Properties.CreateTime.columnName + " >=? AND " + MacProbeInfoDao.Properties.CreateTime.columnName + "<? GROUP BY MAC_BRAND_INFO.BRAND_NAME";
        Cursor cursor = WPApplication.gDaoSession.getDatabase().rawQuery(sql, new String[]{""+startTime,""+endTime});
        List<BrandTotalInfo> list = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                BrandTotalInfo info = new BrandTotalInfo();
                info.setBrandName(cursor.getString(cursor.getColumnIndex(MacBrandInfoDao.Properties.BrandName.columnName)));
                info.setBrandNumber(cursor.getInt(cursor.getColumnIndex("brandcount")));
                info.setScale(scale);
                info.setScaleValue(scaleValue);
                info.setDate(date);
                if (TextUtils.isEmpty(info.getBrandName())) {
                    info.setBrandName("其他品牌");
                }
                list.add(info);
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            cursor.close();
        }
        return list;
    }

    public static List<BrandTotalInfo> getDayCustomerCount(final String scaleValue, final String scale, final String date) {
        String sql = "select MAC_BRAND_INFO.BRAND_NAME ,count (DISTINCT  MAC_PROBE_INFO.MAC ) as brandcount from " + MacProbeInfoDao.TABLENAME + " LEFT JOIN " + MacBrandInfoDao.TABLENAME + " ON MAC_PROBE_INFO.BRAND_MAC= MAC_BRAND_INFO.BRAND_MAC WHERE " + MacProbeInfoDao.Properties.CreateDate.columnName + " =? GROUP BY MAC_BRAND_INFO.BRAND_NAME";
        Cursor cursor = WPApplication.gDaoSession.getDatabase().rawQuery(sql, new String[]{date});
        List<BrandTotalInfo> list = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                BrandTotalInfo info = new BrandTotalInfo();
                info.setBrandName(cursor.getString(cursor.getColumnIndex(MacBrandInfoDao.Properties.BrandName.columnName)));
                info.setBrandNumber(cursor.getInt(cursor.getColumnIndex("brandcount")));
                info.setScale(scale);
                info.setScaleValue(scaleValue);
                info.setDate(date);
                if (TextUtils.isEmpty(info.getBrandName())) {
                    info.setBrandName("其他品牌");
                }
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
