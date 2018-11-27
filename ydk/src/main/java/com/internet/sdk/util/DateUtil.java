package com.internet.sdk.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.internet.sdk.GlobalNames;

/**
 * 日期工具类
 */
public class DateUtil {

	/**
	 * 创建一个sql Date
	 * 
	 * @param udate
	 *            Date
	 * @return Date
	 */
	public static java.sql.Date newSqlDate(int year, int month, int day) {
		return utilDate2SqlDate(newUtilDate(year, month, day));
	}

	/**
	 * 创建一个util Date，年月日都从1开始
	 * 
	 * @param year
	 *            int 年份
	 * @param month
	 *            int
	 * @param day
	 *            int
	 * @return Date
	 */
	public static Date newUtilDate(int year, int month, int day) {
		// 默认月份是从0开始，所以这里减一
		month -= 1;
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		return c.getTime();
	}

	/**
	 * 将java.util.date转换成java.sql.date
	 * 
	 * @param udate
	 *            Date
	 * @return Date
	 */
	public static java.sql.Date utilDate2SqlDate(Date udate) {
		return new java.sql.Date(udate.getTime());
	}

	/**
	 * 将java.sql.date转换成java.util.date
	 * 
	 * @param udate
	 *            Date
	 * @return Date
	 */
	public static Date sqlDate2UtilDate(java.sql.Date sdate) {
		return new Date(sdate.getTime());
	}

	/**
	 * 日期转换成为字符串，注意，格式中的月份为大写M，分钟为小写m
	 * 
	 * @param date
	 *            java.util.Date
	 * @param pattern
	 *            String 转换格式，如："yyyy-MM-dd"
	 * @throws ParseException
	 * @return String
	 */
	public static String date2Str(Date date, String pattern)
			throws ParseException {
		SimpleDateFormat f = new SimpleDateFormat(pattern);

		return f.format(date);
	}

	/**
	 * 字符串转化成util.date
	 * 
	 * @param str
	 *            String 日期字符串
	 * @param pattern
	 *            String 格式
	 * @throws ParseException
	 * @return Date
	 */
	public static Date str2UtilDate(String str, String pattern)
			throws ParseException {
		pattern = pattern.replaceAll("mm", "MM");
		SimpleDateFormat f = new SimpleDateFormat(pattern);

		return f.parse(str);
	}

	/**
	 * 字符串转化成util.date
	 * 
	 * @param str
	 *            String 日期字符串
	 * @param pattern
	 *            String 格式
	 * @throws ParseException
	 * @return Date
	 */
	public static java.sql.Date str2SqlDate(String str, String pattern)
			throws ParseException {
		if (str == null || str.isEmpty()) {
			return null;
		}

		// pattern = pattern.replaceAll("mm", "MM");
		SimpleDateFormat f = new SimpleDateFormat(pattern);

		return utilDate2SqlDate(f.parse(str));
	}

	/**
	 * yyyy-MM-dd -> MM/dd/yyyy 格式 用户 informix date类型转化
	 * 
	 * @param str
	 *            String 日期字符串
	 * @param pattern
	 *            String 格式
	 * @throws ParseException
	 * @return Date
	 */
	public static java.sql.Date str2SqlDateIfx(String str, String pattern)
			throws ParseException {
		// 2017-01-01 MM/dd/YYYY
		if ((str == null) || (str.isEmpty())) {
			return null;
		}
		SimpleDateFormat f = new SimpleDateFormat(GlobalNames.DATA_FORMAT);
		Date date1 = f.parse(str);
		SimpleDateFormat nf = new SimpleDateFormat(GlobalNames.DATA_FORMAT_IFX);
		String newstr = nf.format(date1);
		return str2SqlDate(newstr, GlobalNames.DATA_FORMAT_IFX);
	}

	/**
	 * 去掉radio的序号util.date
	 * 
	 * @param str
	 *            String 字段名称
	 * @return String
	 */
	public static String removeradio_serialnumber(String zdmc) {
		String regEx = "_\\d+"; // 表示a或f
		String px = "";

		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(zdmc);
		if (m.find()) {
			px = m.group(0);
			return zdmc.substring(0, zdmc.indexOf(px));
		}

		return zdmc;
	}

	/**
	 * 字符串转化成util.date
	 * 
	 * @param str
	 *            String 日期字符串
	 * @param pattern
	 *            String 格式
	 * @throws ParseException
	 * @return Date
	 */
	public static java.sql.Timestamp str3SqlDate(String str, String pattern)
			throws ParseException {
		if (str == null || str.isEmpty()) {
			return null;
		}

		SimpleDateFormat f = new SimpleDateFormat(pattern);

		return new java.sql.Timestamp(f.parse(str).getTime());
	}

	/**
	 * 加减年份
	 * 
	 * @param date
	 *            Date 日期
	 * @param monthCount
	 *            int 加减年份的数量
	 * @throws ParseException
	 * @return Date
	 */
	public static Date addYear(Date date, int yearNum) throws ParseException {
		return addDate(date, yearNum, Calendar.YEAR);
	}

	public static java.sql.Timestamp str2Timestamp(String strdate, String time)
			throws ParseException {
		java.sql.Timestamp ts = new java.sql.Timestamp(new Date().getTime());

		return ts;
	}

	/**
	 * 加减月份 唐杰 2006-03-07
	 * 
	 * @param date
	 *            Date 日期
	 * @param monthCount
	 *            int 加减月份的数量
	 * @throws ParseException
	 * @return Date
	 */
	public static Date addMonth(Date date, int monthNum) throws ParseException {
		return addDate(date, monthNum, Calendar.MONTH);
	}

	/**
	 * 加减天数
	 * 
	 * @param date
	 *            Date 日期
	 * @param monthCount
	 *            int 加减天的数量
	 * @throws ParseException
	 * @return Date
	 */
	public static Date addDay(Date date, int dayNum) throws ParseException {
		return addDate(date, dayNum, Calendar.DATE);
	}

	private static Date addDate(Date date, int num, int type) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(type, num);

		return c.getTime();
	}

	/**
	 * 加减年份 2006-03-07
	 * 
	 * @param date
	 *            Date 日期
	 * @param monthCount
	 *            int 加减年份的数量
	 * @throws ParseException
	 * @return Date
	 */
	public static java.sql.Date addYear(java.sql.Date date, int yearNum)
			throws ParseException {
		return utilDate2SqlDate(addDate(date, yearNum, Calendar.YEAR));
	}

	/**
	 * 加减月份 2006-03-07
	 * 
	 * @param date
	 *            Date 日期
	 * @param monthCount
	 *            int 加减月份的数量
	 * @throws ParseException
	 * @return Date
	 */
	public static java.sql.Date addMonth(java.sql.Date date, int monthNum)
			throws ParseException {
		return utilDate2SqlDate(addDate(date, monthNum, Calendar.MONTH));
	}

	/**
	 * 加减天数 唐杰 2006-03-07
	 * 
	 * @param date
	 *            Date 日期
	 * @param monthCount
	 *            int 加减天的数量
	 * @throws ParseException
	 * @return Date
	 */
	public static java.sql.Date addDay(java.sql.Date date, int dayNum)
			throws ParseException {
		return utilDate2SqlDate(addDate(date, dayNum, Calendar.DATE));
	}

	/**
	 * 取两个日期之间相隔的天数，date1和date2将会被截断天后面的时间 2006-03-07
	 * 
	 * @param date1
	 *            Date
	 * @param date2
	 *            Date
	 * @return Date
	 */
	public static long getDaysBetweenTwoDate(Date date1, Date date2) {
		long ld1 = date1.getTime();
		long ld2 = date2.getTime();

		// 截断天数后的时间如：2006-2-3 9:34:23 后的9:34:23将被截断
		ld1 = ld1 / 86400000;
		ld2 = ld2 / 86400000;

		long reslut = ld1 - ld2;
		reslut = reslut < 0 ? -reslut : reslut;

		return reslut;
	}

	/**
	 * 取两个日期之间相隔的天数，date1和date2将会被截断天后面的时间 2006-03-07
	 * 
	 * @param date1
	 *            Date
	 * @param date2
	 *            Date
	 * @return Date
	 */
	public static long getDaysBetweenTwoDate_f(Date date1, Date date2) {
		long ld1 = date1.getTime();
		long ld2 = date2.getTime();

		// 截断天数后的时间如：2006-2-3 9:34:23 后的9:34:23将被截断
		ld1 = ld1 / 86400000;
		ld2 = ld2 / 86400000;

		long reslut = ld1 - ld2;
		// reslut = reslut < 0 ? -reslut : reslut;

		return reslut;
	}

	/**
	 * 根据一个日期取年份
	 * 
	 * @param dateStr
	 *            String 格式为yyyy-MM-dd的日期，
	 * @return String
	 */
	public static String getYear(String dateStr) {
		return dateStr.substring(dateStr.indexOf("-"));
	}

	/**
	 * 获取当前日期
	 * 
	 * @return String
	 */
	public static String getCurrentDate() throws ParseException {
		return DateUtil.date2Str(new Date(), "yyyy-MM-dd");
	}

	/**
	 * 获取当前年月
	 * 
	 * @param pattern
	 *            String
	 * @return String
	 */
	public static String getCurrentYearMonth(String pattern)
			throws ParseException {
		return DateUtil.date2Str(new Date(), pattern);
	}

	/**
	 * 获取当前年份
	 * 
	 * @return String
	 */
	public static String getCurrentYear() throws ParseException {
		String dateStr = DateUtil.date2Str(new Date(), "yyyy-MM-dd");

		return dateStr.substring(0, dateStr.indexOf("-"));
	}

	/**
	 * 获取当前月份
	 * 
	 * @return String
	 */
	public static String getCurrentMonth() throws ParseException {
		String dateStr = DateUtil.date2Str(new Date(), "yyyy-MM-dd");

		return dateStr.substring(dateStr.indexOf("-") + 1,
				dateStr.lastIndexOf("-"));
	}

	/**
	 * 根据身份证，获取生日
	 * 
	 * @param pid
	 *            String 身份证号码 15或者18位
	 * @return String 生日，按照格式yyyy-MM-dd返回
	 */
	public static String getBirthDay(String pid) {
		String birthDay = null;
		if (pid.length() == 15) {
			String year = pid.substring(6, 8);
			String month = pid.substring(8, 10);
			String day = pid.substring(10, 12);
			int iy = Integer.parseInt(year);
			if (iy > 30) {
				year = "19" + year;
			} else {
				year = "20" + year;
			}

			birthDay = year + "-" + month + "-" + day;
		} else if (pid.length() == 18) {
			String year = pid.substring(6, 10);
			String month = pid.substring(10, 12);
			String day = pid.substring(12, 14);

			birthDay = year + "-" + month + "-" + day;
		}

		return birthDay;
	}

	/**
	 * 根据身份证，获取年龄
	 * 
	 * @param pid
	 *            String 身份证号码 15或者18位
	 * @return int 年龄
	 */
	public static int getAge(String pid) throws ParseException {
		int age = 0;
		/**
		 * 根据身份证取生日年份、月份、日期
		 */
		String year = null;
		String month = null;
		String day = null;
		if (pid.length() == 15) {
			year = pid.substring(6, 8);
			month = pid.substring(8, 10);
			day = pid.substring(10, 12);
			int iy = Integer.parseInt(year);
			if (iy > 30) {
				year = "19" + year;
			} else {
				year = "20" + year;
			}
		} else if (pid.length() == 18) {
			year = pid.substring(6, 10);
			month = pid.substring(10, 12);
			day = pid.substring(12, 14);
		}

		if (year != null && month != null) {
			/**
			 * 取当前年份，月份，日期
			 */
			String cy = DateUtil.getCurrentYear();
			String cm = DateUtil.getCurrentMonth();
			String cd = DateUtil.getCurrentMonth();
			/**
			 * 所有年月日转换成int
			 */
			int iyear = Integer.parseInt(year);
			int imonth = Integer.parseInt(month);
			int iday = Integer.parseInt(day);
			int icy = Integer.parseInt(cy);
			int icm = Integer.parseInt(cm);
			int icd = Integer.parseInt(cd);
			/**
			 * 计算年龄
			 */
			age = icy - iyear;

			if (icm < imonth) {
				age--;
			} else if (icm == imonth) {
				if (icd <= iday) {
					age--;
				}
			}
		}

		return age;
	}

	// 判断是否是日期格式文本
	public static boolean isDate(String str) {
		/*
		 * Pattern pattern = Pattern.compile(
		 * "^([0-9]{4})((0([1-9]{1}))|(1[0-2]))(([0-2]([0-9]{1}))|(3[0|1]))(([0-1]([0-9]{1}))|(2[0-4]))([0-5]([0-9]{1}))([0-5]([0-9]{1}))"
		 * ); Matcher matcher = pattern.matcher(str); boolean bool =
		 * matcher.matches(); return bool;
		 */
		try {
			Date date = str2UtilDate(str, "yyyy-MM-dd");
			date.getTime();
			return true;
		} catch (ParseException e) {
			return false;
			// e.printStackTrace();
		}
	}
	
	/**
	 * 是否是时间
	 * @param tem
	 * @return 0 不是时间类型  1 日期  2 时间
	 * @author tzx
	 * 2018年3月2日
	 * @throws ParseException 
	 */
	public static int isDateTime(String dateStr) throws ParseException {
		if(StringUtils.isBlank(dateStr)|| !isDate(dateStr)){
			return 0;
		}
		//2017-10-10 
		if( dateStr.trim().length()==10){
			return 1;
		}else if(dateStr.trim().length()>=19){
			//2017-10-10 10:10:10 
			return 2;
		}
		return 0;
	}
	

	/**
	 * 功能：得当前系统时间的时间截
	 */
	public static Timestamp GetCurrentTime() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * 功能：得当前系统时间格式转换为字符后字符串：如2003-03-18 18:28:38.123
	 */
	public static String GetCurrentTimeStr() {
		return new Timestamp(System.currentTimeMillis()).toString();
	}

	/**
	 * 功能：得当时间格式转换为字符后字符串：如2003-03-18 18:28:38.123
	 */
	public static String GetDateTimeStr(Date d) {
		return new Timestamp(d.getTime()).toString();
	}

	/**
	 * 得到精确到分不带空格的时间 format int 日期和时间是否有空格
	 * 
	 * @param format
	 *            int 1、yyyy年MM月dd日HH时mm分 2、yyyy年MM月dd日 HH时mm分
	 * @author 许奎
	 * @return String
	 */
	public static String getYearMonthMinute(int format) {
		SimpleDateFormat formatter = null;
		switch (format) {
		case 1:
			formatter = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
			break;
		default:
			formatter = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
			break;
		}

		return formatter.format(new Date());
	}

	/**
	 * 比较指定的日期与当前日期顺序
	 * 
	 * @param specDate
	 *            String 指定日期
	 * @author 许奎
	 * @return int =0相等、=-1在当前日期前、=1在当前日期后
	 */
	public static int compareTo(String specDate) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date sDate = df.parse(specDate);
			Date cDate = df.parse(df.format(new Date()));
			return sDate.compareTo(cDate);
		} catch (ParseException pex) {
		}

		return -1;
	}

	/**
	 * 比较指定的日期与当前日期相差的月数
	 * 
	 * @param specDate
	 *            String 指定日期
	 * @author 许奎
	 * @return int
	 */
	public static int differToMonth(String specDate) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar aCalendar = Calendar.getInstance();
		try {
			Date sDate = df.parse(specDate);
			aCalendar.setTime(sDate);
			int month1 = aCalendar.get(Calendar.MONTH);
			Date cDate = df.parse(df.format(new Date()));
			aCalendar.setTime(cDate);
			int month2 = aCalendar.get(Calendar.MONTH);

			return month2 - month1;
		} catch (ParseException pex) {
		}

		return 0;
	}

	/**
	 * 比较指定的日期与当前日期相差的天数
	 * 
	 * @param specDate
	 *            String 指定日期
	 * @author 许奎
	 * @return int
	 */
	public static int differToDay(String specDate) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar aCalendar = Calendar.getInstance();
		try {
			Date sDate = df.parse(specDate);
			aCalendar.setTime(sDate);
			int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
			Date cDate = df.parse(df.format(new Date()));
			aCalendar.setTime(cDate);
			int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

			return day2 - day1;
		} catch (ParseException pex) {
		}

		return 0;
	}

	/**
	 * 获得指定日期的后一天
	 * 
	 * @param specDate
	 *            指定的日期
	 * @return
	 */
	public static String getYearMonthDateBySpecDate(String specDate, int year,
			int month, int day) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = formatter.parse(specDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(date);
		gregorianCalendar.add(GregorianCalendar.YEAR, year);
		gregorianCalendar.add(GregorianCalendar.MONTH, month);
		gregorianCalendar.add(GregorianCalendar.DAY_OF_YEAR, day);

		return formatter.format(gregorianCalendar.getTime());
	}

	/**
	 * 获得指定日期的后一天
	 * 
	 * @param specDate
	 *            指定的日期
	 * @return
	 */
	public static String getDateBySpecDate(String specDate, int year) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = formatter.parse(specDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(date);
		gregorianCalendar.add(GregorianCalendar.YEAR, year);
		gregorianCalendar.add(GregorianCalendar.DAY_OF_YEAR, -1);

		return formatter.format(gregorianCalendar.getTime());
	}

	/**
	 * 获得指定日期的后一天
	 * 
	 * @param specDate
	 *            指定的日期
	 * @return
	 */
	public static String getMonthDateBySpecDate(String specDate, int month) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = formatter.parse(specDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(date);
		gregorianCalendar.add(GregorianCalendar.MONTH, month);
		gregorianCalendar.add(GregorianCalendar.DAY_OF_YEAR, -1);

		return formatter.format(gregorianCalendar.getTime());
	}

	/**
	 * 获得指定日期的后一天
	 * 
	 * @param specDate
	 *            指定的日期
	 * @return
	 */
	public static String getYearMonthBySpecDate(String specDate, int year,
			int month) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = formatter.parse(specDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(date);
		gregorianCalendar.add(GregorianCalendar.YEAR, year);
		gregorianCalendar.add(GregorianCalendar.MONTH, month);
		gregorianCalendar.add(GregorianCalendar.DAY_OF_YEAR, -1);

		return formatter.format(gregorianCalendar.getTime());
	}

	/**
	 * 获得指定日期的后一天
	 * 
	 * @param specDate
	 *            指定的日期
	 * @return
	 */
	public static String getMonthDateBySpecDate(String specDate, int month,
			int day) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = formatter.parse(specDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(date);
		gregorianCalendar.add(GregorianCalendar.MONTH, month);
		gregorianCalendar.add(GregorianCalendar.DAY_OF_YEAR, day);

		return formatter.format(gregorianCalendar.getTime());
	}

	/**
	 * 获得指定日期的后一天
	 * 
	 * @param specDate
	 *            指定的日期
	 * @return
	 */
	public static String getDayDateBySpecDate(String specDate, int day) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = formatter.parse(specDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(date);
		gregorianCalendar.add(GregorianCalendar.DAY_OF_YEAR, day);

		return formatter.format(gregorianCalendar.getTime());
	}

	/**
	 * 得到某年某周的第一天
	 * 
	 * @param year
	 * @param week
	 * @return
	 */
	public static Date getFirstDayOfWeek(int year, int week) {
		week = week - 1;
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.DATE, 1);
		Calendar cal = (Calendar) calendar.clone();
		cal.add(Calendar.DATE, week * 7);

		return getFirstDayOfWeek(cal.getTime());
	}

	/**
	 * 得到某年某周的最后一天
	 * 
	 * @param year
	 * @param week
	 * @return
	 */
	public static Date getLastDayOfWeek(int year, int week) {
		week = week - 1;
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.DATE, 1);
		Calendar cal = (Calendar) calendar.clone();
		cal.add(Calendar.DATE, week * 7);

		return getLastDayOfWeek(cal.getTime());
	}

	/**
	 * 取得当前日期所在周的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.SUNDAY);
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek()); // Sunday

		return calendar.getTime();
	}

	/**
	 * 取得当前日期所在周的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.SUNDAY);
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6); // Saturday

		return calendar.getTime();
	}

	/**
	 * 取得当前日期所在周的前一周最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfLastWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return getLastDayOfWeek(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.WEEK_OF_YEAR) - 1);
	}

	/**
	 * 返回指定日期的月的第一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date getFirstDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				1);
		return calendar.getTime();
	}

	/**
	 * 返回指定年月的月的第一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date getFirstDayOfMonth(Integer year, Integer month) {
		Calendar calendar = Calendar.getInstance();
		if (year == null) {
			year = calendar.get(Calendar.YEAR);
		}
		if (month == null) {
			month = calendar.get(Calendar.MONTH);
		}
		calendar.set(year, month, 1);

		return calendar.getTime();
	}

	/**
	 * 返回指定日期的月的最后一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date getLastDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				1);
		calendar.roll(Calendar.DATE, -1);

		return calendar.getTime();
	}

	/**
	 * 返回指定年月的月的最后一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date getLastDayOfMonth(Integer year, Integer month) {
		Calendar calendar = Calendar.getInstance();
		if (year == null) {
			year = calendar.get(Calendar.YEAR);
		}
		if (month == null) {
			month = calendar.get(Calendar.MONTH);
		} else {
			month--;
		}
		calendar.set(year, month, 1);
		calendar.roll(Calendar.DATE, -1);

		return calendar.getTime();
	}

	/**
	 * 返回指定日期的上个月的最后一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date getLastDayOfLastMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH) - 1, 1);
		calendar.roll(Calendar.DATE, -1);

		return calendar.getTime();
	}

	/**
	 * 返回指定日期的季的第一天
	 * 
	 * @param year
	 * @param quarter
	 * @return
	 */
	public static Date getFirstDayOfQuarter(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return getFirstDayOfQuarter(calendar.get(Calendar.YEAR),
				getQuarterOfYear(date));
	}

	/**
	 * 返回指定年季的季的第一天
	 * 
	 * @param year
	 * @param quarter
	 * @return
	 */
	public static Date getFirstDayOfQuarter(Integer year, Integer quarter) {
		Calendar calendar = Calendar.getInstance();
		Integer month = new Integer(0);
		if (quarter == 1) {
			month = 1 - 1;
		} else if (quarter == 2) {
			month = 4 - 1;
		} else if (quarter == 3) {
			month = 7 - 1;
		} else if (quarter == 4) {
			month = 10 - 1;
		} else {
			month = calendar.get(Calendar.MONTH);
		}

		return getFirstDayOfMonth(year, month);
	}

	/**
	 * 返回指定日期的季的最后一天
	 * 
	 * @param year
	 * @param quarter
	 * @return
	 */
	public static Date getLastDayOfQuarter(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return getLastDayOfQuarter(calendar.get(Calendar.YEAR),
				getQuarterOfYear(date));
	}

	/**
	 * 返回指定年季的季的最后一天
	 * 
	 * @param year
	 * @param quarter
	 * @return
	 */
	public static Date getLastDayOfQuarter(Integer year, Integer quarter) {
		Calendar calendar = Calendar.getInstance();
		Integer month = new Integer(0);
		if (quarter == 1) {
			month = 3 - 1;
		} else if (quarter == 2) {
			month = 6 - 1;
		} else if (quarter == 3) {
			month = 9 - 1;
		} else if (quarter == 4) {
			month = 12 - 1;
		} else {
			month = calendar.get(Calendar.MONTH);
		}

		return getLastDayOfMonth(year, month);
	}

	/**
	 * 返回指定日期的上一季的最后一天
	 * 
	 * @param year
	 * @param quarter
	 * @return
	 */
	public static Date getLastDayOfLastQuarter(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return getLastDayOfLastQuarter(calendar.get(Calendar.YEAR),
				getQuarterOfYear(date));
	}

	/**
	 * 返回指定年季的上一季的最后一天
	 * 
	 * @param year
	 * @param quarter
	 * @return
	 */
	public static Date getLastDayOfLastQuarter(Integer year, Integer quarter) {
		Calendar calendar = Calendar.getInstance();
		Integer month = new Integer(0);
		if (quarter == 1) {
			month = 12 - 1;
		} else if (quarter == 2) {
			month = 3 - 1;
		} else if (quarter == 3) {
			month = 6 - 1;
		} else if (quarter == 4) {
			month = 9 - 1;
		} else {
			month = calendar.get(Calendar.MONTH);
		}

		return getLastDayOfMonth(year, month);
	}

	/**
	 * 返回指定日期的季度
	 * 
	 * @param date
	 * @return
	 */
	public static int getQuarterOfYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar.get(Calendar.MONTH) / 3 + 1;
	}
	
	/**
	 * 获取格式化后的日期时间字符串
	 * 
	 * @Title: formatDateToString
	 * @Description: 得到指定格式进行格式化后的日期字符串－－供其它public公共函数调用
	 * @param objDate
	 *            日期对象
	 * @param format
	 *            格式字符串
	 * @return
	 */
	public static String formatDateToString(java.util.Date objDate,
			String format) {
		if (objDate == null)
			return "";
		if (format == null || "".equals(format))
			format = "yyyy-MM-dd";

		try {
			return new SimpleDateFormat(format).format(objDate);
		} catch (Exception e) {
			return "";
		}
	}
	
	public static String format(Date date, String format) {
		try {
			return new SimpleDateFormat(format).format(date);
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 获取指定格式的日期时间字符串
	 * 
	 * @param lngDate
	 * @param format
	 * @return
	 */
	public static String getStringDate(long lngDate, String format) {
		return format(new Date(lngDate), format);
	}

	/**
	 * 获取XML格式(yyyy-MM-dd HH:mm:ss)的日期时间字符串,不带毫秒
	 */
	public static String getXmlDateTime(long lngDate) {
		return getStringDate(lngDate, "yyyy-MM-dd HH:mm:ss").replace(' ', 'T');
	}

	/**
	 * 获取XML格式(yyyy-MM-dd HH:mm:ss.SSS)的日期时间字符串,带毫秒
	 */
	public static String getXmlDateTimeMillis(long lngDate) {
		return getStringDate(lngDate, "yyyy-MM-dd HH:mm:ss.SSS").replace(' ',
				'T');
	}

	/**
	 * 获取文件｜目录(yyyyMMddTHHmmss.SSS)的日期时间字符串，带毫秒,不带日期时间分隔符
	 */
	public static String getFolderDateTimeMillis(long lngDate) {
		return getStringDate(lngDate, "yyyyMMdd HHmmss.SSS").replace(' ', 'T');
	}

	/**
	 * 得到当前日期，精确到毫秒(格式:yyyy-MM-dd HH:mm:ss.SSS)
	 * 
	 * @return String
	 */
	public static String getStringDate(long lngDate) {
		return formatDateToString(new Date(lngDate), "yyyy-MM-dd HH:mm:ss.SSS");
	}

	/**
	 * 得到当前日期，精确到秒(格式:yyyy-MM-dd HH:mm:ss)
	 * 
	 * @return String
	 */
	public static String getStringDate() {
		return formatDateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
	}
}
