package com.ums.wifiprobe.utils;

import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by chenzhy on 2017/6/30.
 */

public class TimeUtils {

    //AdShowTime 15分钟1次
    public static long getShowIntervalTime() {
        Calendar calendar = Calendar.getInstance();
        long curTime = calendar.getTimeInMillis();
        int minute = calendar.get(Calendar.MINUTE);
        Calendar calendar2 = Calendar.getInstance();
        if (minute < 15) {
            calendar2.set(Calendar.MINUTE, 15);
        } else if (minute < 30) {
            calendar2.set(Calendar.MINUTE, 30);
        } else if (minute < 45) {
            calendar2.set(Calendar.MINUTE, 45);
        } else {
            calendar2.add(Calendar.HOUR_OF_DAY, 1);
            calendar2.set(Calendar.MINUTE, 0);
        }
        calendar2.set(Calendar.SECOND, 0);
        calendar2.set(Calendar.MILLISECOND, 200);//设置200ms延时
        return calendar2.getTimeInMillis() - curTime;
    }

    //广告上送 10:30、2:30、3:30、9:30 --- 42 58 62 86
    public static long getUploadIntervalTime() {
        Calendar calendar = Calendar.getInstance();
        long curTime = calendar.getTimeInMillis();
        int curStartTime = calendar.get(Calendar.HOUR_OF_DAY) * 4 + calendar.get(Calendar.MINUTE) / 15;
        Calendar calendar2 = Calendar.getInstance();
        if (curStartTime < 42) {
            calendar2.set(Calendar.HOUR_OF_DAY, 10);
            calendar2.set(Calendar.MINUTE, 30);
        } else if (curStartTime < 58) {
            calendar2.set(Calendar.HOUR_OF_DAY, 14);
            calendar2.set(Calendar.MINUTE, 30);
        } else if (curStartTime < 62) {
            calendar2.set(Calendar.HOUR_OF_DAY, 15);
            calendar2.set(Calendar.MINUTE, 30);
        } else if (curStartTime < 86) {
            calendar2.set(Calendar.HOUR_OF_DAY, 21);
            calendar2.set(Calendar.MINUTE, 30);
        } else {
            return -1;
        }
        calendar2.set(Calendar.SECOND, 0);
        calendar2.set(Calendar.MILLISECOND, 0);
        return calendar2.getTimeInMillis() - curTime;
    }

    //以15分钟为单位，返回相应数字，比如1：37返回7
    public static int getShowStartTime() {
        int time;
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        time = hour * 4;
        int minute = calendar.get(Calendar.MINUTE);
        if (minute < 15) {

        } else if (minute < 30) {
            time = time + 1;
        } else if (minute < 45) {
            time = time + 2;
        } else if (minute < 60) {
            time = time + 3;
        }
        return time;
    }

    public static int getUploadStartTime() {
        Calendar calendar = Calendar.getInstance();
        int time = calendar.get(Calendar.HOUR_OF_DAY) * 4 + calendar.get(Calendar.MINUTE) / 15;
        if (time < 42) {

        } else if (time < 58) {
            time = 42;
        } else if (time < 62) {
            time = 58;
        } else if (time < 86) {
            time = 62;
        } else {
            time = 86;
        }
        return time;
    }

    public static String getTimeInSecondsFromStartTime(int startTime) {
        Calendar calendar = Calendar.getInstance();
        if (startTime < 0 && startTime > -96) {
            startTime = -1 * startTime;
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, startTime / 4);
        calendar.set(Calendar.MINUTE, startTime % 4 * 15);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis() / 1000 + "";
    }

    public static String getDataFormatFromStartTime(int startTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, startTime / 4);
        calendar.set(Calendar.MINUTE, startTime % 4 * 15);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = new Date();
        date.setTime(calendar.getTimeInMillis());
        return format.format(date);
    }

    public static String getDataFormatFromRecordId(long recordId, int startTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(recordId);//确定具体日期
        calendar.set(Calendar.HOUR_OF_DAY, startTime / 4);
        calendar.set(Calendar.MINUTE, startTime % 4 * 15);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = new Date();
        date.setTime(calendar.getTimeInMillis());
        return format.format(date);
    }

    //获取当前小时
    public static int getHour(long timeMillions) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillions);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    //获取当前星期
    public static int getWeekDay(long timeMillions) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillions);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.SUNDAY) {
            day = 6;
        } else {
            day = day - 2;
        }
        return day;
    }

    public static int getMonthDay(long timeMillions) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillions);
        return calendar.get(Calendar.DAY_OF_MONTH) - 1;
    }

    //获取当前日期
    public static String getDate(long timeMillions) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        date.setTime(timeMillions);
        return format.format(date);
    }

    public static int getYear(long timeMillions) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillions);
        return calendar.get(Calendar.YEAR);
    }

    //获取当前日期
    public static String getTimeInfo(long timeMillions) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        date.setTime(timeMillions);
        return format.format(date);
    }

    //获取当前小时
    public static String getDateTime(long timeMillions) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        date.setTime(timeMillions);
        return format.format(date);
    }

    public static long getTimeMillions(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date tempDate = format.parse(date);
            return tempDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getTimeMillions(String date, String hour) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date tempDate = format.parse(date + " " + hour + ":00:00");
            return tempDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getDefineDayAgoDate(long timeMillions, int days) {
        long agoTimeMillions = timeMillions - days * 24 * 60 * 60 * 1000l;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        date.setTime(agoTimeMillions);
        return format.format(date);
    }

    public static String getDefineMonthAgoDate(String date, int beforeMonths) {
        long agoTimeMillions = TimeUtils.getTimeMillions(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(agoTimeMillions);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, beforeMonths);

        return getDate(calendar.getTimeInMillis());
    }

    public static int getDefineMonthAgo(String date, int beforeMonths) {
        long agoTimeMillions = TimeUtils.getTimeMillions(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(agoTimeMillions);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, beforeMonths);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static long getDefineDayAgoTimeMillions(long timeMillions, int days) {
        long agoTimeMillions = timeMillions - days * 24 * 60 * 60 * 1000l;
        return agoTimeMillions;
    }

    public static String getSundayDate(String date, int before) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date tempdate = format.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(tempdate);
            //返回周一
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                //周日，日历默认为下周，所以需要减1
                calendar.add(Calendar.WEEK_OF_YEAR, -1);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            } else {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            }
            calendar.add(Calendar.WEEK_OF_YEAR, before);
            Date tempDate2 = new Date(calendar.getTimeInMillis());
            return format.format(tempDate2);
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

    public static String getFirstDateOfMonth(String date) {
        return getFirstDateOfMonth(date, 0);
    }

    public static String getFirstDateOfMonth(String date, int before) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date tempdate = format.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(tempdate);
            //返回周一
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.MONTH, before);
            Date tempDate2 = new Date(calendar.getTimeInMillis());
            return format.format(tempDate2);
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

    public static String getSundayDate(String date) {
        return getSundayDate(date, 0);
    }


    //需要考虑跨年怎么处理 如2017/12/31
    public static int getWeeksOfYear(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date tempdate = format.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(tempdate);
            if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            } else {
                calendar.add(Calendar.DAY_OF_YEAR, -1);
            }
            return calendar.get(Calendar.WEEK_OF_YEAR) + 1;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getMonthsOfYear(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date tempdate = format.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(tempdate);

            return calendar.get(Calendar.MONTH) + 1;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;//默认1月份为0 依次递增
        }
    }

    public static String[] getDatesOfMonth(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        int length = getDaysOfMonth(date);
        String[] monthDays = new String[length];
        try {
            Date tempdate = format.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(tempdate);
            int curMonth = getMonthsOfYear(date) - 1;
            calendar.set(Calendar.MONTH, curMonth);
            for (int i = 1; i <= length; i++) {
                calendar.set(Calendar.DAY_OF_MONTH, i);
                monthDays[i - 1] = format.format(calendar.getTimeInMillis());
            }
            return monthDays;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getDaysOfMonth(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date tempdate = format.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(tempdate);
            return calendar.getActualMaximum(Calendar.DATE);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 30;
    }

    public static String getWeekDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date tempdate = null;
        try {
            tempdate = format.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(tempdate);
            int weekday = calendar.get(Calendar.DAY_OF_WEEK);
            String[] weekdays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
            return weekdays[weekday - 1];
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "周一";
    }

    public static String[] getWeekDates(String date) {
        String[] strings = new String[7];
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date tempdate = format.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(tempdate);
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                calendar.add(Calendar.WEEK_OF_YEAR, -1);//周末减一年
            }
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            strings[0] = format.format(new Date(calendar.getTimeInMillis()));
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
            strings[1] = format.format(new Date(calendar.getTimeInMillis()));
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
            strings[2] = format.format(new Date(calendar.getTimeInMillis()));
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
            strings[3] = format.format(new Date(calendar.getTimeInMillis()));
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
            strings[4] = format.format(new Date(calendar.getTimeInMillis()));
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            strings[5] = format.format(new Date(calendar.getTimeInMillis()));
            calendar.add(Calendar.DAY_OF_WEEK, 1);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            strings[6] = format.format(new Date(calendar.getTimeInMillis()));
            return strings;
        } catch (ParseException e) {
            e.printStackTrace();
            return strings;
        }
    }


    //flag 0month 1week 2day
    public static Date getDate(int year, int monthOrWeek, int flag, String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (flag == 2) {
            try {
                return format.parse(date);
            } catch (ParseException e) {
                return new Date();
            }
        } else if (flag == 0) {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(Calendar.YEAR, year);
            calendar1.set(Calendar.MONTH, monthOrWeek - 1);
            return calendar1.getTime();

        } else if (flag == 1) {
            Calendar calendar2 = Calendar.getInstance();
            calendar2.set(Calendar.YEAR, year);
            if (calendar2.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                calendar2.set(Calendar.WEEK_OF_YEAR, monthOrWeek - 1);
            }else{
                calendar2.set(Calendar.WEEK_OF_YEAR, monthOrWeek);
            }
            return calendar2.getTime();
        }
        return new Date();
    }

    /**
     * 根据当前日期获得上周的日期区间（上周周一和周日日期）
     *
     * @return
     * @author zhaoxuepu
     */
    public static String getLastTimeInterval(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        try {
            calendar1.setTime(sdf.parse(time));

            calendar2.setTime(sdf.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
        int offset1 = 1 - dayOfWeek;
        int offset2 = 7 - dayOfWeek;
        calendar1.add(Calendar.DATE, offset1 - 7);
        calendar2.add(Calendar.DATE, offset2 - 7);
        // System.out.println(sdf.format(calendar1.getTime()));// last Monday
        String lastBeginDate = sdf.format(calendar1.getTime());
        // System.out.println(sdf.format(calendar2.getTime()));// last Sunday
        String lastEndDate = sdf.format(calendar2.getTime());
        return lastBeginDate + "," + lastEndDate;
    }
    /**
     * 根据当前日期获得上周的日期区间（上周周一和周日日期）
     *
     * @return
     * @author zhaoxuepu
     */
    public static List<String> getLastWeekIntervalArray(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar   calendar1 =null;

        List<String> weekStrList =new ArrayList<>();
        for(int of=1;of<8;of++){
            calendar1 = Calendar.getInstance();
            try {
                calendar1.setTime(sdf.parse(time));
            } catch (ParseException e) {
                e.printStackTrace();
            }


            int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
            int offset1 = of - dayOfWeek;
            calendar1.add(Calendar.DATE, offset1 -7);
            weekStrList.add(sdf.format(calendar1.getTime()));

        }

        return weekStrList;
    }

    /**
     * 根据当前日期获得所在周的日期区间（周一和周日日期）
     *
     * @return
     * @author zhaoxuepu
     * @throws ParseException
     */
    public static String getTimeInterval(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        String imptimeBegin = sdf.format(cal.getTime());
        // System.out.println("所在周星期一的日期：" + imptimeBegin);
        cal.add(Calendar.DATE, 6);
        String imptimeEnd = sdf.format(cal.getTime());
        // System.out.println("所在周星期日的日期：" + imptimeEnd);
        return imptimeBegin + "," + imptimeEnd;
    }
    public static boolean isDateOneBigger(Date str1) {
        boolean isBigger = false;
        Date dt1 = null;
        Date dt2 = null;
            dt1 = str1;
            dt2 = new Date();
        if (dt1.getTime() > dt2.getTime()) {
            isBigger = true;
        } else if (dt1.getTime() < dt2.getTime()) {
            isBigger = false;
        }
        return isBigger;
    }
    public static boolean isDateOneBiggerTwo(String  str1,String str2) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1);
            dt2 = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() > dt2.getTime()) {
            isBigger = true;
        } else if (dt1.getTime() < dt2.getTime()) {
            isBigger = false;
        }
        return isBigger;
    }
    /*
     * 将时间转换为时间戳
     */
    public static long dateToStamp(String s) throws ParseException{
        long res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        res= date.getTime();
        return res;
    }
    /*       * 将时间戳转换为时间      */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
}
