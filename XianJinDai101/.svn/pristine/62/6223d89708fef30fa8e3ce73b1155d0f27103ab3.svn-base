package com.ryx.swiper.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtil {
	private static SimpleDateFormat dateformat;

	/**
	 * yyyy-MM-dd HH:mm:ss example：2010-11-22 13:14:55
	 */
	public static SimpleDateFormat NOW_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static SimpleDateFormat NOW_TIME2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	/**
	 * yyyy-MM-dd example：2010-01-02,2010-1-2
	 */
	public static SimpleDateFormat NOW_DATE = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * yyyyMMddHHmmsssss example：20101122134455666
	 */
	public static SimpleDateFormat STR_TIME = new SimpleDateFormat("yyyyMMddHHmmss");
	/**
	 * yyyyMMdd example：20101122
	 */
	public static SimpleDateFormat STR_DATE = new SimpleDateFormat("yyyyMMdd");
	public static SimpleDateFormat STR_SHORT_DATE = new SimpleDateFormat("yyyyMM");
	/**
	 * yyyy-MM example：2010-2
	 */
	public static SimpleDateFormat STR_NODATE = new SimpleDateFormat("yyyy-MM");
	/**
	 * EEE example：星期一
	 */
	public static SimpleDateFormat WEEK_TIME = new SimpleDateFormat("EEE");

	public static SimpleDateFormat TIME = new SimpleDateFormat("HH:mm:ss");

	public static SimpleDateFormat TIME_H = new SimpleDateFormat("HH:mm");

	public static SimpleDateFormat TIME_NOHOURS = new SimpleDateFormat("mm:ss");

	public static DecimalFormat AMOUNT_TEXT = new DecimalFormat("##0.00");

	/**
	 * 获取当前时间，并根据格式返回时间字符串
	 * 
	 * @param time
	 * @param format
	 * @return
	 */
	public static String getNowDateToString(String format) {
		return getDateToString(new Date(), format);
	}

	/**
	 * 获得Date的字符串显示
	 * 
	 * @param format
	 *            :NOW_TIME,NOW_DATE,STR_TIME,WEEK_TIME in DateUtil
	 * @param date
	 *            Date
	 * @return
	 */
	public static String getDateString(Date date, SimpleDateFormat sdf) {
		String s = null;

		try {
			s = sdf.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 获得当天Date的字符串显示
	 * 
	 * @param format
	 * @return
	 */
	public static String getDateString(SimpleDateFormat format) {
		Date date = new Date();
		return format.format(date);
	}

	/**
	 * 获得当天Date的字符串显示
	 * 
	 * @param format
	 * @return
	 */
	public static String getCurrentDateString(String format) {
		Date date = new Date();
		SimpleDateFormat sd = new SimpleDateFormat(format);
		return sd.format(date);
	}
	
	/**
	 * 根据指定格式返回时间字符串
	 * 
	 * @param time
	 * @param format
	 * @return
	 */
	public static String getDateToString(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}

	/**
	 * 根据指定格式返回时间字符串
	 * 
	 * @param time
	 * @param format
	 * @return
	 */
	public static String getDateToString(String date, String format) {
		Date day = convertStringToDate(date);
		return new SimpleDateFormat(format).format(day);
	}

	/**
	 * 根据指定格式返回时间字符串
	 * 
	 * @param time
	 * @param format
	 * @return
	 */
	public static String getTimeToString(String time, String format) {
		String t = null;
		int len, h = 0, m = 0, s = 0;
		len = time.length();
		if (len >= 2) {
			t = time.substring(0, 2);
			h = Integer.parseInt(t);
		}
		if (len >= 4) {
			t = time.substring(2, 4);
			m = Integer.parseInt(t);
		}
		if (len >= 6) {
			t = time.substring(4, 6);
			s = Integer.parseInt(t);
		}
		Date day = new Date(0, 0, 1, h, m, s);
		return new SimpleDateFormat(format).format(day);
	}

	/**
	 * 将字符串格式的时间转成Date
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static Date convertStringToDate(String date) {
		String t = null;
		int len, y = 0, M = 0, d = 1, h = 0, m = 0, s = 0;
		len = date.length();
		if (len >= 4) {
			t = date.substring(0, 4);
			y = Integer.parseInt(t);
			if (y > 1900)
				y = y - 1900;
		}
		if (len >= 6) {
			t = date.substring(4, 6);
			M = Integer.parseInt(t) - 1;
		}
		if (len >= 8) {
			t = date.substring(6, 8);
			d = Integer.parseInt(t);
		}
		if (len >= 10) {
			t = date.substring(8, 10);
			h = Integer.parseInt(t);
		}
		if (len >= 12) {
			t = date.substring(10, 12);
			m = Integer.parseInt(t);
		}
		if (len >= 14) {
			t = date.substring(12, 14);
			s = Integer.parseInt(t);
		}
		return new Date(y, M, d, h, m, s);
	}

	/**
	 * 获取两个时间的间隔(毫秒)
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long getTimeSpan(String date1, String date2) {
		return getTimeSpan(convertStringToDate(date1), convertStringToDate(date2));
	}

	/**
	 * 获取两个时间的间隔(毫秒)
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long getTimeSpan(Date date1, Date date2) {
		return Math.abs(date1.getTime() - date2.getTime());
	}

	private static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	/**
	 * 获得当前日期时间
	 * <p>
	 * 日期时间格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String currentDatetime() {
		return datetimeFormat.format(now());
	}

	/**
	 * 格式化日期时间
	 * <p>
	 * 日期时间格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String formatDatetime(Date date) {
		return datetimeFormat.format(date);
	}

	/**
	 * 格式化日期时间
	 * 
	 * @param date
	 * @param pattern
	 *            格式化模式，详见{@link SimpleDateFormat}构造器
	 *            <code>SimpleDateFormat(String pattern)</code>
	 * @return
	 */
	public static String formatDatetime(Date date, String pattern) {
		SimpleDateFormat customFormat = (SimpleDateFormat) datetimeFormat.clone();
		customFormat.applyPattern(pattern);
		return customFormat.format(date);
	}

	/**
	 * 获得当前日期
	 * <p>
	 * 日期格式yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String currentDate() {
		return dateFormat.format(now());
	}

	/**
	 * 格式化日期
	 * <p>
	 * 日期格式yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String formatDate(Date date) {
		return dateFormat.format(date);
	}

	/**
	 * 获得当前时间
	 * <p>
	 * 时间格式HH:mm:ss
	 * 
	 * @return
	 */
	public static String currentTime() {
		return timeFormat.format(now());
	}

	/**
	 * 格式化时间
	 * <p>
	 * 时间格式HH:mm:ss
	 * 
	 * @return
	 */
	public static String formatTime(Date date) {
		return timeFormat.format(date);
	}

	/**
	 * 获得当前时间的<code>java.util.Date</code>对象
	 * 
	 * @return
	 */
	public static Date now() {
		return new Date();
	}

	public static Calendar calendar() {
		Calendar cal = GregorianCalendar.getInstance(Locale.CHINESE);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		return cal;
	}

	/**
	 * 获得当前时间的毫秒数
	 * <p>
	 * 详见{@link System#currentTimeMillis()}
	 * 
	 * @return
	 */
	public static long millis() {
		return System.currentTimeMillis();
	}

	/**
	 * 
	 * 获得当前Chinese月份
	 * 
	 * @return
	 */
	public static int month() {
		return calendar().get(Calendar.MONTH) + 1;
	}

	/**
	 * 获得月份中的第几天
	 * 
	 * @return
	 */
	public static int dayOfMonth() {
		return calendar().get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 今天是星期的第几天
	 * 
	 * @return
	 */
	public static int dayOfWeek() {
		return calendar().get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 今天是年中的第几天
	 * 
	 * @return
	 */
	public static int dayOfYear() {
		return calendar().get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 判断原日期是否在目标日期之前
	 * 
	 * @param src
	 * @param dst
	 * @return
	 */
	public static boolean isBefore(Date src, Date dst) {
		return src.before(dst);
	}

	/**
	 * 判断原日期是否在目标日期之后
	 * 
	 * @param src
	 * @param dst
	 * @return
	 */
	public static boolean isAfter(Date src, Date dst) {
		return src.after(dst);
	}

	/**
	 * 判断两日期是否相同
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isEqual(Date date1, Date date2) {
		return date1.compareTo(date2) == 0;
	}

	/**
	 * 判断某个日期是否在某个日期范围
	 * 
	 * @param beginDate
	 *            日期范围开始
	 * @param endDate
	 *            日期范围结束
	 * @param src
	 *            需要判断的日期
	 * @return
	 */
	public static boolean between(Date beginDate, Date endDate, Date src) {
		return beginDate.before(src) && endDate.after(src);
	}

	/**
	 * 获得当前月的最后一天
	 * <p>
	 * HH:mm:ss为0，毫秒为999
	 * 
	 * @return
	 */
	public static Date lastDayOfMonth() {
		Calendar cal = calendar();
		cal.set(Calendar.DAY_OF_MONTH, 0); // M月置零
		cal.set(Calendar.HOUR_OF_DAY, 0);// H置零
		cal.set(Calendar.MINUTE, 0);// m置零
		cal.set(Calendar.SECOND, 0);// s置零
		cal.set(Calendar.MILLISECOND, 0);// S置零
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);// 月份+1
		cal.set(Calendar.MILLISECOND, -1);// 毫秒-1
		return cal.getTime();
	}

	/**
	 * 获得当前月的第一天
	 * <p>
	 * HH:mm:ss SS为零
	 * 
	 * @return
	 */
	public static Date firstDayOfMonth() {
		Calendar cal = calendar();
		cal.set(Calendar.DAY_OF_MONTH, 1); // M月置1
		cal.set(Calendar.HOUR_OF_DAY, 0);// H置零
		cal.set(Calendar.MINUTE, 0);// m置零
		cal.set(Calendar.SECOND, 0);// s置零
		cal.set(Calendar.MILLISECOND, 0);// S置零
		return cal.getTime();
	}

	private static Date weekDay(int week) {
		Calendar cal = calendar();
		cal.set(Calendar.DAY_OF_WEEK, week);
		return cal.getTime();
	}

	/**
	 * 获得周五日期
	 * <p>
	 * 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
	 * 
	 * @return
	 */
	public static Date friday() {
		return weekDay(Calendar.FRIDAY);
	}

	/**
	 * 获得周六日期
	 * <p>
	 * 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
	 * 
	 * @return
	 */
	public static Date saturday() {
		return weekDay(Calendar.SATURDAY);
	}

	/**
	 * 获得周日日期
	 * <p>
	 * 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
	 * 
	 * @return
	 */
	public static Date sunday() {
		return weekDay(Calendar.SUNDAY);
	}

	/**
	 * 将字符串日期时间转换成java.util.Date类型
	 * <p>
	 * 日期时间格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @param datetime
	 * @return
	 */
	public static Date parseDatetime(String datetime) throws ParseException {
		return datetimeFormat.parse(datetime);
	}

	/**
	 * 将字符串日期转换成java.util.Date类型
	 * <p>
	 * 日期时间格式yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String date) throws ParseException {
		return dateFormat.parse(date);
	}

	/**
	 * 将字符串日期转换成java.util.Date类型
	 * <p>
	 * 时间格式 HH:mm:ss
	 * 
	 * @param time
	 * @return
	 * @throws ParseException
	 */
	public static Date parseTime(String time) throws ParseException {
		return timeFormat.parse(time);
	}

	/**
	 * 根据自定义pattern将字符串日期转换成java.util.Date类型
	 * 
	 * @param datetime
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDatetime(String datetime, String pattern) throws ParseException {
		SimpleDateFormat format = (SimpleDateFormat) datetimeFormat.clone();
		format.applyPattern(pattern);
		return format.parse(datetime);
	}

	public static String getDayBefore() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String specifiedDay = sdf.format(date);
		return getSpecifiedDayBefore(specifiedDay);
	}

	public static String getToday() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String specifiedDay = sdf.format(date);
		return getSpecifiedToday(specifiedDay);
	}

	public static String getDayAfter() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String specifiedDay = sdf.format(date);
		return getSpecifiedDayAfter(specifiedDay);
	}

	public static String getTodayMD() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		String specifiedDay = sdf.format(date);
		return getSpecifiedTodayMD(specifiedDay);
	}

	/**
	 * 获得指定日期的天 如201307-19
	 * 
	 * @param specifiedDay
	 * @return
	 * @throws Exception
	 */
	public static String getSpecifiedTodayMD(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day);

		String dayBefore = new SimpleDateFormat("MM-dd").format(c.getTime());
		return dayBefore;
	}

	/**
	 * 获得指定日期的前一天
	 * 
	 * @param specifiedDay
	 * @return
	 * @throws Exception
	 */
	public static String getSpecifiedToday(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayBefore;
	}

	/**
	 * 获得指定日期的前一天
	 * 
	 * @param specifiedDay
	 * @return
	 * @throws Exception
	 */
	public static String getSpecifiedDayBefore(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayBefore;
	}

	/**
	 * 获得指定日期的后一天
	 * 
	 * @param specifiedDay
	 * @return
	 */
	public static String getSpecifiedDayAfter(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + 1);

		String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayAfter;
	}

	/**
	 * 判断当前日期是星期几<br>
	 * <br>
	 * 
	 * @param pTime
	 *            修要判断的时间<br>
	 * @return dayForWeek 判断结果<br>
	 * @Exception 发生异常<br>
	 */
	public static int dayForWeek(String pTime) {
		SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(date.parse(pTime));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}

	public static String dayForWeekLabel(String pTime) {
		int week = dayForWeek(pTime);
		String ret = "";
		switch (week) {
		case 0:
			ret = "日";
		case 7:
			ret = "日";
			break;
		case 1:
			ret = "一";
			break;
		case 2:
			ret = "二";
			break;
		case 3:
			ret = "三";
			break;
		case 4:
			ret = "四";
			break;
		case 5:
			ret = "五";
			break;
		case 6:
			ret = "六";
			break;
		}
		return ret;
	}

	public static String monthForWeekLable(String date) {
		String str=date.substring(4);
		if(str.charAt(0)=='0')
		{
			str=str.substring(1);
		}
		int month = Integer.valueOf(str).intValue();
		String result = "";
		switch (month) {
		case 1:
			result = "一月";
			break;

		case 2:
			result = "二月";
			break;
		case 3:
			result = "三月";
			break;
		case 4:
			result = "四月";
			break;
		case 5:
			result = "五月";
			break;
		case 6:
			result = "六月";
			break;
		case 7:
			result = "七月";
			break;
		case 8:
			result = "八月";
			break;
		case 9:
			result = "九月";
			break;
		case 10:
			result = "十月";
			break;
		case 11:
			result = "十一月";
			break;
		case 12:
			result = "十二月";
			break;
		default:
			break;
		}
		return result;
	}

	/**
	 * 获取本月日期
	 * 
	 * @return
	 */
	public static String getMonth() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		String time = format.format(c.getTime());
		System.out.println(time);
		return time;
	}

	/**
	 * 获取本月日期 2013-13
	 * 
	 * @return
	 */
	public static String getPreMonth(String specifiedDay) {

		Calendar c = Calendar.getInstance();

		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.MONTH);
		c.set(Calendar.MONTH, day - 1);

		String month = new SimpleDateFormat("yyyy-MM").format(c.getTime());
		System.out.println("premonth>>>" + month);
		return month;
	}

	// 如i= -6 ，往前数第6个月
	public static String getPreSpecialMonth(int i) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		// String end = df.format(calendar.getTime());
		calendar.add(Calendar.MONTH, i);
		String start = df.format(calendar.getTime());
		return start;
	}

	// 如i= -10 ，往前数第10tian
	public static String getPreSpecialDay(int i) {
		SimpleDateFormat dayf = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(new Date());
		// String end2 = dayf.format(calendar2.getTime());
		calendar2.add(Calendar.DAY_OF_MONTH, i);
		String start2 = dayf.format(calendar2.getTime());
		return start2;
	}
	
	// 如i= -10 ，往前数第10tian
	public static String getPreSpecialDay(int i,String format) {
		SimpleDateFormat dayf = new SimpleDateFormat(format);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(new Date());
		// String end2 = dayf.format(calendar2.getTime());
		calendar2.add(Calendar.DAY_OF_MONTH, i);
		String start2 = dayf.format(calendar2.getTime());
		return start2;
	}
	
	/**
	 * 格式化时间字符串
	 * 如 20140822 095110,yyyyMMdd HHmmss,yyyy-MM-dd HH:mm:ss
	 * @param date_str
	 *            需要转换的时间格式的字符串
	 * @param format_date
	 *            需要转换的时间格式的字符串的格式
	 * @param fromat_to_date
	 *            需要转换成的时间格式的字符创格式
	 * @return
	 */
	public static String fromStrToStr(String date_str, String format_date,
	        String fromat_to_date) {
		String date_res = "";

		try {
			Date date = formatStrToDate(date_str, format_date);
			date_res = formatDateToStr(date, fromat_to_date);

		} catch (Exception e) {
			date_res = date_str;
		}
		
		return date_res;
	}

	/**
	 * 字符串转换成日期
	 * 
	 * @param str
	 * @return date
	 */
	public static Date formatStrToDate(String str, String format_date) {

		SimpleDateFormat format = new SimpleDateFormat(format_date);
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 日期转换成字符串
	 * 
	 * @param date
	 * @return str
	 */
	public static String formatDateToStr(Date date, String fromat_to_date) {

		SimpleDateFormat format = new SimpleDateFormat(fromat_to_date);
		if (date != null) {
			return format.format(date);
		} else {
			return "";
		}
	}
	
	/**
	 * 获取两个日志相减的天数
	 * @param date01
	 * @param date02
	 * @return
	 */
	public static long getDaySub(Date date01,Date date02) {
		long day = 0; 
		 day = (date01.getTime()-date02.getTime())/(24*60*60*1000)>0 ? (date01.getTime()-date02.getTime())/(24*60*60*1000):
			   (date02.getTime()-date01.getTime())/(24*60*60*1000);
		return day;
	}
	
	/**
	 * 获取两个日志相减的天数
	 * @param date01
	 * @param date02
	 * @return
	 */
	public static long getDaySub(String date01_str,String date02_str) {
		long day = 0;
		try {
			Date date01 = new SimpleDateFormat("yyyy-MM-dd").parse(date01_str);
			Date date02 = new SimpleDateFormat("yyyy-MM-dd").parse(date02_str);
			 day = (date01.getTime()-date02.getTime())/(24*60*60*1000)>0 ? (date01.getTime()-date02.getTime())/(24*60*60*1000):
				   (date02.getTime()-date01.getTime())/(24*60*60*1000);
		} catch (Exception e) {
 		}
		 
		return day;
	}
	
	/**
	 * 获取两个日期的大小 true表示date01 > date02 否则反之
	 * @param date01
	 * @param date02
	 * @return
	 */
	public static boolean isGRTZero(String date01_str,String date02_str) {
		long day = 0;
		try { 
			Date date01 = new SimpleDateFormat("yyyy-MM-dd").parse(date01_str);
			Date date02 = new SimpleDateFormat("yyyy-MM-dd").parse(date02_str);
			day = (date01.getTime()-date02.getTime())/(24*60*60*1000);
		} catch (Exception e) {
 		}
		boolean flag = false;
		if(day > 0) {
			flag = true;
		}
		return flag;
	}
}
