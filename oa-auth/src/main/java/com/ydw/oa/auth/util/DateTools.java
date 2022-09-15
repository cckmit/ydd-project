package com.ydw.oa.auth.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.tmsps.fk.common.util.ChkUtil;

public class DateTools {

	// 增加对应天数
	public static Timestamp addDay(Timestamp end, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(end.getTime());
		cal.add(Calendar.DAY_OF_YEAR, day);
		return new Timestamp(cal.getTimeInMillis());

	}

	// 增加对应年数
	public static Timestamp addYear(Timestamp end, int year) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(end.getTime());
		cal.add(Calendar.YEAR, year);
		return new Timestamp(cal.getTimeInMillis());

	}

	// 增加对应秒数
	public static Timestamp addSecond(Timestamp end, int sec) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(end.getTime());
		cal.add(Calendar.SECOND, sec);
		return new Timestamp(cal.getTimeInMillis());
	}

	public static int countDays(Timestamp begin, Timestamp end) {
		long beginTime = begin.getTime();
		long endTime = end.getTime();
		int days = (int) ((endTime - beginTime) / (1000 * 60 * 60 * 24));
		return days;
	}

	public static long strToLong(String date) {
		String pattern = "yyyy-MM-dd HH:mm:ss";
		java.sql.Date date1 = strToDate(date, pattern);
		if (date1 != null) {
			return date1.getTime();
		} else {
			return 0l;
		}
	}

	public static long strToLong(String date, String pattern) {
		java.sql.Date date1 = strToDate(date, pattern);
		if (date1 != null) {
			return date1.getTime();
		} else {
			return 0l;
		}
	}

	public static Timestamp strToDatestamp(String datetime) {
		String pattern = "yyyy-MM-dd HH:mm:ss";
		return strToDatestamp(datetime, pattern);
	}

	public static Timestamp strToDatestamp(String datetime, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			Date d = sdf.parse(datetime);
			return new Timestamp(d.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 字符串转 Timestamp对象
	 *
	 * @param date
	 */

	public static String strDateToStr(String date) {

		return format(strToDate(date));
	}

	public static java.sql.Date strToDate(String date) {
		String pattern = "yyyy-MM-dd";
		return strToDate(date, pattern);
	}

	public static java.sql.Date strNumToDate(String date) {
		String pattern = "yyyyMMdd";
		return strToDate(date, pattern);
	}

	public static java.sql.Date strToDate(String date, String pattern) {
		if (ChkUtil.isNull(date)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			Date d = sdf.parse(date);
			return new java.sql.Date(d.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// 取得当前的年月
	public static String getYearMonth() {
		String pattern = "yyyy-MM";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date(System.currentTimeMillis()));
	}

	// 获取上个月的年月
	public static String getLastYearMonth() {
		Calendar cal = Calendar.getInstance();
		// 取得系统当前时间所在月第一天时间对象
		cal.set(Calendar.DAY_OF_MONTH, 1);
		// 日期减一,取得上月最后一天时间对象
		cal.add(Calendar.DAY_OF_MONTH, -1);
		java.util.Date date = cal.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		return df.format(date);
	}

	public static java.sql.Date getYearMonth(String date) {
		String pattern = "yyyy-MM";
		return strToDate(date, pattern);
	}

	public static int getYear() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		return year;
	}

	public static int getMonth() {
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		return month;
	}

	public static String getToday() {
		Calendar cal = Calendar.getInstance();
		java.util.Date date = cal.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}

	public static String getToday(String reg) {
		Calendar cal = Calendar.getInstance();
		java.util.Date date = cal.getTime();
		SimpleDateFormat df = new SimpleDateFormat(reg);
		return df.format(date);
	}

	public static String format(java.sql.Date date) {
		if (ChkUtil.isNull(date)) {
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}

	public static String format(Date date,String reg) {
		if (ChkUtil.isNull(date)) {
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat(reg);
		return df.format(date);
	}

	public static String format(String longtime) {
		if (ChkUtil.isNull(longtime)) {
			return "";
		}
		long time = Long.parseLong(longtime);
		Date date = new Date();
		date.setTime(time);
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
		return df.format(date);
	}

	public static String getTodayTime() {
		Calendar cal = Calendar.getInstance();
		java.util.Date date = cal.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}

	public static int countDaysVSToday(java.sql.Date start) {
		Timestamp now = new Timestamp(System.currentTimeMillis());

		int days = (int) ((now.getTime() - start.getTime()) / (1000 * 60 * 60 * 24));
		return days;

	}

	// 获取每月最大天数
	// 参数 yyyy-MM 格式
	public static int getDayOfMonth(String yearMonth) {
		java.sql.Date date = getYearMonth(yearMonth);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		int dateOfMonth = cal.getActualMaximum(Calendar.DATE);
		return dateOfMonth;
	}

	// 获取每月最大天数
	public static int getDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);// Java月份才0开始算
		int dateOfMonth = cal.getActualMaximum(Calendar.DATE);
		return dateOfMonth;
	}

	public static String formatDateTime(Timestamp date) {
		if (ChkUtil.isNull(date)) {
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}

	public static String formatDateTime(Date date) {
		if (ChkUtil.isNull(date)) {
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}

	public static String strFormatDateTime(String date) {
		if (ChkUtil.isNull(date)) {
			return "";
		}
		Date da = new Date(Long.parseLong(date));

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(da);
	}

	public static String strFormatDateTimeChinese(String date) {
		if (ChkUtil.isNull(date)) {
			return "";
		}
		Date da = new Date(Long.parseLong(date));

		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
		return df.format(da);
	}

	public static String strFormatDateTimeLimitMinute(String date) {
		if (ChkUtil.isNull(date)) {
			return "";
		}
		Date da = new Date(Long.parseLong(date));

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return df.format(da);
	}

	public static String strFormatDateTime2Chinese(String date) {
		if (ChkUtil.isNull(date)) {
			return "";
		}
		Date da = new Date(Long.parseLong(date));

		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日   HH:mm");
		return df.format(da);
	}

	public static String strFormatDateTime2(String date) {
		if (ChkUtil.isNull(date)) {
			return "";
		}
		Date da = new Date(Long.parseLong(date));

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(da);
	}

	public static String formatDate(Timestamp date) {
		if (ChkUtil.isNull(date)) {
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}

	public static String formatDate(Timestamp date, String patten) {
		if (ChkUtil.isNull(date)) {
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat(patten);
		return df.format(date);
	}

	public static String formatDate(Date date) {
		if (ChkUtil.isNull(date)) {
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}

	// 获取某月的最后一天 month格式：yyyy-MM-dd or yyyy-MM
	public static String getMonthFinalDay(String month) {
		if (month.length() < 10) {
			month = month + "-01";
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateTools.strToDate(month));
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		// 日期减一,取得上月最后一天时间对象
		cal.add(Calendar.DAY_OF_MONTH, -1);
		java.util.Date date = cal.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}

	// 获取距今i天的时间 long型
	public static long getSomeDaysBefore(int i) {
		Calendar c = Calendar.getInstance();
		c.setTime(strToDate(getToday()));
		c.add(Calendar.DAY_OF_YEAR, i);

		return c.getTimeInMillis();
	}

	public static long getYearToLong(int year) {
		java.sql.Date date = strToDate(year + "-01-01");
		return date.getTime();
	}

	public static String getTodayTimeOnly() {
		Calendar cal = Calendar.getInstance();
		java.util.Date date = cal.getTime();
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		return df.format(date);
	}

	public static String getTodayCn() {
		String pattern = "yyyy年MM月dd日HH时mm分";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date(System.currentTimeMillis()));
	}
}
