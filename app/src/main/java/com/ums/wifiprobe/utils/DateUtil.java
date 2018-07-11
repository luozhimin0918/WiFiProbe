package com.ums.wifiprobe.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/***
 * 日期工具类，该工具类拥有以下三种功能： 1、日期与字符串间相互转换； 2、判断日期是否位于指定区间； 3、比较日期先后； 4、获取某年某月的最后一天
 * 
 * @author LiuChenShuo
 *
 */
public class DateUtil {

	public static final String format1 = "yyyy-MM-dd HH:mm:ss";// H为24小时制，h为12小时制
	public static final String format2 = "yyyyMMddHHmmss";
	public static final String format3 = "yyyyMMdd";
	private static Calendar cStart = Calendar.getInstance();
	private static Calendar cEnd = Calendar.getInstance();
	private static Calendar cSpec = Calendar.getInstance();

	public DateUtil() {
	}

	/**
	 * 日期转换成字符串
	 * 
	 * @param date
	 *            待转日期
	 * @param dateFormat
	 *            格式字符串
	 * @return str
	 */
	public static String dateToStr(Date date, String dateFormat) {

		SimpleDateFormat format = new SimpleDateFormat(dateFormat,
				Locale.getDefault());
		String str = format.format(date);
		return str;
	}

	/**
	 * 字符串转换成日期
	 * 
	 * @param str
	 *            待转字符串
	 * @param dateFormat
	 *            格式字符串
	 * @return date
	 */
	public static Date strToDate(String str, String dateFormat) {

		SimpleDateFormat format = new SimpleDateFormat(dateFormat,
				Locale.getDefault());
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * long型转字符串日期
	 * @param l
	 * @param dateFormat
	 * @return
	 */
	public static String longToStr(long l, String dateFormat){
		Date date = new Date(l);
		return dateToStr(date, dateFormat);
	}
	
	/***
	 * 判断指定时间是否位于指定的时间区间内
	 * 
	 * @param startDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @param specifiedDate
	 *            指定的日期
	 * @return
	 */
	public static boolean locatePeriodOfTime(Date startDate, Date endDate,
			Date specifiedDate) {

		cStart.setTime(startDate);
		cEnd.setTime(endDate);
		cSpec.setTime(specifiedDate);
		if (cStart.compareTo(cEnd) > 0) {
			throw new IllegalStateException("起始时间不可小于结束时间！");
		}
		int strspec = cStart.compareTo(cSpec);
		int endspec = cEnd.compareTo(cSpec);
		if (strspec > 0 || endspec < 0) {
			return false;
		} else {
			return true;
		}
	}

	/***
	 * 判断指定时间是否位于指定的时间区间内
	 * 
	 * @param start
	 *            起始日期
	 * @param end
	 *            结束日期
	 * @param specified
	 *            指定的日期
	 * @param dateFormat
	 *            前面三个日期参数的格式字符串（前三个日期的格式要一致）
	 * @return
	 */
	public static boolean locatePeriodOfTime(String start, String end,
			String specified, String dateFormat) {

		SimpleDateFormat format = new SimpleDateFormat(dateFormat,
				Locale.getDefault());
		try {
			Date startDate = format.parse(start);
			Date endDate = format.parse(end);
			Date specifiedDate = format.parse(specified);
			return locatePeriodOfTime(startDate, endDate, specifiedDate);
		} catch (ParseException e) {

			e.printStackTrace();
			System.err.println("格式不正确");
			return false;
		}

	}

	/***
	 * 比较日期先后
	 * 
	 * @param startDate
	 *            待比较的日期1
	 * @param endDate
	 *            待比较的日期2
	 * @return 返回负数说明“cStart < cEnd”；返回零说明“cStart = cEnd”，返回正数说明“cStart > cEnd”
	 */
	public static int compareDate(Date startDate, Date endDate) {
		cStart.setTime(startDate);
		cEnd.setTime(endDate);
		return cStart.compareTo(cEnd);
	}

	/***
	 * 比较日期先后
	 * 
	 * @param start
	 *            待比较的日期1
	 * @param end
	 *            待比较的日期2
	 * @param dateFormat
	 *            待比较的日期格式，前两个日期参数的格式必须一致
	 * @return 返回负数说明“cStart < cEnd”；返回零说明“cStart = cEnd”，返回正数说明“cStart > cEnd”
	 */
	public static int compareDate(String start, String end, String dateFormat) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat,
				Locale.getDefault());
		Date startDate = new Date();
		Date endDate = new Date();
		try {
			startDate = format.parse(start);
			endDate = format.parse(end);
		} catch (ParseException e) {

			e.printStackTrace();
			System.err.println("格式不正确");
		}
		return compareDate(startDate, endDate);
	}

	/***
	 * 获取某年某月的最后一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getLastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		// 设置年份
		cal.set(Calendar.YEAR, year);
		// 设置月份
		cal.set(Calendar.MONTH, month - 1);
		// 获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		return lastDay;
	}

	/***
	 * 是否在30天以内
	 * 
	 * @param startDate
	 *            起始时间，格式必须是"yyyyMMdd"
	 * @param endDate
	 *            结束时间，格式必须是"yyyyMMdd"
	 * @return
	 */
	public static boolean isIn30Days(String startDate, String endDate) {

		String start, end;
		start = getFirst30Days();
		end = getToday();

		if (compareDate(start, startDate, format3) <= 0
				&& compareDate(end, endDate, format3) >= 0) {
			return true;
		} else {
			return false;
		}
	}

	/** 获取距离今天前30天的日期，返回的日期格式为"yyyyMMdd" */
	public static String getFirst30Days() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat(format3,
				Locale.getDefault());
		int curDays = cal.get(Calendar.DAY_OF_MONTH);
		String start;
		if (curDays - 30 >= 0) {
			// 设置天
			cal.set(Calendar.DAY_OF_MONTH, (curDays - 30 + 1));
			start = format.format(cal.getTime());
		} else {
			int lastDay = getLastDayOfMonth(cal.get(Calendar.YEAR),
					cal.get(Calendar.MONTH));
			int startDay = (curDays - 30) + lastDay + 1;
			Calendar calTmp = Calendar.getInstance();
			calTmp.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) - 1,
					startDay);
			start = format.format(calTmp.getTime());
		}

		return start;
	}

	/** 获取今天的日期，返回的日期格式为"yyyyMMdd" */
	public static String getToday() {
		SimpleDateFormat format = new SimpleDateFormat(format3,
				Locale.getDefault());
		String end = format.format(new Date());
		return end;
	}

	public static void main(String[] args) {
		// String date = "20150601";
		// String time = "61710";
		// String datetime = date+time;
		// System.out.println(DateToStr(StrToDate(datetime, format2), format1));
		// System.out.println(locatePeriodOfTime("20101213-131810",
		// "20150601-161930", date+"-"+time, "yyyyMMdd-HHmmss"));

		// String lastDay = getLastDayOfMonth(2015,6);
		// System.out.println("获取当前月的最后一天：" + lastDay);
	}
}
