package com.weplaylabs.mysterybox.common;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

public class TimeCalculation {
    public static long SERVER_TIME_VALUE = 0;  			// DB서버와의 시간차
	public static long SERVER_TIME_REFRESH_TIME = 0; 	// DB서버와의 시간차이를 갱신한 마지막 시간 한시간마다 체크한다.

	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
	private static SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
	private static SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd HH");
	private static SimpleDateFormat intMonthFormat = new SimpleDateFormat("yyyyMM");
	private static SimpleDateFormat intDayFormat = new SimpleDateFormat("yyyyMMdd");

    private static Calendar getCurrentCalendar()
	{
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis()-SERVER_TIME_VALUE);
		return cal;
	}

    // 현재 UnixTime
	public static int getCurrentUnixTime()
	{
		return (int)((System.currentTimeMillis()-SERVER_TIME_VALUE)/1000);
	}

    // LocalDateTime -> UnixTime
    public static long LocalDateTimeToUnixTime(LocalDateTime localDateTime) {
        return (long) localDateTime.toEpochSecond(ZoneOffset.UTC);
    }

    /*
	 * 게임 내에서 사용하는 한주의 개념 (시작 : 월요일, 종료 : 일요일)
	 * java 에서 사용되는 요일  
	 * 1 : 일요일 
	 * 2 : 월요일
	 * 3 : 화요일
	 * 4 : 수요일
	 * 5 : 목요일
	 * 6 : 금요일
	 * 7 : 토요일
	 */
	public static int dayOfWeek()
	{
		Calendar cal =  getCurrentCalendar();
		return cal.get(Calendar.DAY_OF_WEEK);
	}
	
	public static int getMonth()
	{
		Calendar today =  getCurrentCalendar();
		int month = today.get(Calendar.MONTH) + 1;

		return month;
	}
	
	public static int getDay()
	{
		Calendar today =  getCurrentCalendar();
		int day = today.get(Calendar.DAY_OF_YEAR);

		return day;
	}

	public static int getHour()
	{
		Calendar today =  getCurrentCalendar();
		int hour = today.get(Calendar.HOUR_OF_DAY);

		return hour;
	}
	
	public static int getMinute()
	{
		Calendar today =  getCurrentCalendar();
		int minute = today.get(Calendar.MINUTE);

		return minute;
	}

	
	public static int getCurrentDay()
	{
		Calendar today =  getCurrentCalendar();
		int year = today.get(Calendar.YEAR) * 10000;
		int month = (today.get(Calendar.MONTH) + 1) * 100;
		int day = today.get(Calendar.DAY_OF_MONTH);

		return year + month + day;
	}


	// 다음날 시간 (20180321 00:00:00 )
	// calVal가 0 이라면 오늘 정시
	// calVal가 24라면 다음날 정시
	public static long getNextDayUnixTime(int calVal)
	{
		Calendar cal = getCurrentCalendar();
		cal.add(Calendar.HOUR, calVal);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTimeInMillis() / 1000;
	}

	
	// unixTime -> dataTime
	public static String getUnixTimeToDate(long unixTime)
	{
		return formatter.format(new Date(unixTime * 1000));
	}


	// 현재 시간에서 시작 시간의 차이를 반환 (초단위)
	public static long diffOfUnixTime(long endUnixTime)
	{
		return endUnixTime - getCurrentUnixTime();
	}
	
	
	public static long diffOfUnixTime(long startUnixTime, long endUnixTime)
	{
		return endUnixTime - startUnixTime;
	}
	
	// 흘러간 일자 계산
	public static long diffOfDays(long startUnixTime, long endUnixTime)
	{
		long calDate = endUnixTime - startUnixTime;
		long calDays = calDate / ConstantVal.DAY_OF_SECOND;
		calDays = Math.abs(calDays);
		return calDays;
	}
	
	public static int doDateUnixTime(byte tmType, int calVal)
	{
		Calendar cal = getCurrentCalendar();
		
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		switch (tmType)
		{
		case ConstantVal.DATE_SECTION_YEAR:
			cal.add(Calendar.YEAR, calVal);
			cal.set(Calendar.MONTH, 0);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			break;
			
		case ConstantVal.DATE_SECTION_MONTH:
			cal.add(Calendar.MONTH, calVal);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			break;
			
		case ConstantVal.DATE_SECTION_DAY:
			cal.add(Calendar.DAY_OF_MONTH, calVal);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			break;
			
		case ConstantVal.DATE_SECTION_HOUR:
			cal.add(Calendar.HOUR_OF_DAY, calVal);
			cal.set(Calendar.MINUTE, 0);
			break;
			
		case ConstantVal.DATE_SECTION_HOUR_FULL:
			cal.add(Calendar.HOUR_OF_DAY, calVal);
			break;
			
		case ConstantVal.DATE_SECTION_MINUTE:
			cal.add(Calendar.MINUTE, calVal);
			break;
			
		default:
			break;
		}

		return (int)(cal.getTimeInMillis() / 1000);
	}
	
	
	// 지정된 날짜의 형식으로만 리턴 (year : 2018, month : 10, day : 30)
	public static int getCurCalendar(byte tmType, int calVal)
	{
		Calendar cal = getCurrentCalendar();

		if (tmType == ConstantVal.DATE_SECTION_YEAR)
		{
			cal.add(Calendar.YEAR, calVal);
			return cal.get(Calendar.YEAR);
		} 
		else if (tmType == ConstantVal.DATE_SECTION_MONTH)
		{
			cal.add(Calendar.MONTH, calVal);
			return cal.get(Calendar.MONTH) + 1;
		} 
		else
		{
			cal.add(Calendar.DATE, calVal);
			return cal.get(Calendar.DAY_OF_MONTH);
		}
	}
	
	// 년.월.일을 숫자형으로 리턴 - 20180121
	public static int getIntTime()
	{
		int intTime = TimeCalculation.getCurCalendar(ConstantVal.DATE_SECTION_YEAR, 0) * 10000;
		intTime += TimeCalculation.getCurCalendar(ConstantVal.DATE_SECTION_MONTH, 0) * 100;
		intTime += TimeCalculation.getCurCalendar(ConstantVal.DATE_SECTION_DAY, 0);

		return intTime;
	}
	
	// 어제 날자를 숫자형으로 리턴 - 20180121
	public static int getPrevIntTime()
	{
		int intTime = TimeCalculation.getCurCalendar(ConstantVal.DATE_SECTION_YEAR, 0) * 10000;
		intTime += TimeCalculation.getCurCalendar(ConstantVal.DATE_SECTION_MONTH, 0) * 100;
		intTime += TimeCalculation.getCurCalendar(ConstantVal.DATE_SECTION_DAY, -1);

		return intTime;
	}
	
	// 년.월.일을 숫자형으로 리턴 - 20180121 - 연,월,일 (시간 지정)
	public static int getIntTime(int year, int month, int day)
	{
		int intTime = TimeCalculation.getCurCalendar(ConstantVal.DATE_SECTION_YEAR, year) * 10000;
		intTime += TimeCalculation.getCurCalendar(ConstantVal.DATE_SECTION_MONTH, month) * 100;
		intTime += TimeCalculation.getCurCalendar(ConstantVal.DATE_SECTION_DAY, day);

		return intTime;
	}
	

	// 인자로 받은 날짜가 오늘이냐 아니냐.
	public static boolean checkToday(int date) throws Exception
	{
		int todayInitTm = doDateUnixTime(ConstantVal.DATE_SECTION_DAY, 0);
		int nextDayTm = todayInitTm + ConstantVal.DAY_OF_SECOND;
		
		if (todayInitTm <= date && date < nextDayTm)
			return true;

		return false;
	}
	
	
	
	
	public static boolean checkTimeNow(long startDate, long endDate) throws Exception
	{
		long curTime = getCurrentUnixTime();
		
		// 시작과 종료 시간이 0이라면 무조건 진행 중..
		if(startDate == 0 && endDate == 0)
			return true;
		
		// 종료 기간이 없다. - 무제한
		if(endDate == 0)
		{
			// 시작 시간이 현재시간 크다 - 시작하지 않음
			if(startDate > curTime)
				return false;
			
			return true;
		}
		else
		{
			// 시작되지 않았거나 종료되었음.
			if(endDate <= curTime || startDate > curTime)
				return false;
			
			return true;
		}
	}
	
	public static boolean checkTimeNow(long checkTime, long startDate, long endDate) throws Exception
	{
		// 시작과 종료 시간이 0이라면 무조건 진행 중..
		if(startDate == 0 && endDate == 0)
			return true;
		
		// 종료 기간이 없다. - 무제한
		if(endDate == 0)
		{
			// 시작 시간이 현재시간 크다 - 시작하지 않음
			if(startDate > checkTime)
				return false;
			
			return true;
		}
		else
		{
			// 시작되지 않았거나 종료되었음.
			if(endDate <= checkTime || startDate > checkTime)
				return false;
			
			return true;
		}
	}

	
	// true : limit Time 남아있음
	// false: Time Over
	public static boolean checkLimitTimeNow(int limitDate) throws Exception
	{
		if (limitDate >= getCurrentUnixTime())
			return true;

		return false;
	}
	

	// 현재연도 + 해당주로 계산됨 ( ex-201852 )
	public static int getWeekOfYear()
	{
		Calendar cal = getCurrentCalendar();
		
		int weekDay = cal.get(Calendar.DAY_OF_WEEK);
		if(weekDay == Calendar.SUNDAY)
		{
			// 1주일 전 주차로 처리한다. (월화수목금토일이 동일 주차 계산 - 월요일부터 시작)
			cal.add(Calendar.WEEK_OF_YEAR, -1);
		}
		
		int year = cal.get(Calendar.YEAR);
		int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
		if(weekOfYear == 1)
		{
			// 1주차일때 작년도 연말일 수 있다. (해당 연도 확인)
			// 다음주와 연도가 같은지 확인하여 처리한다. 2020-12-31일은 01주차로 나오기 때문에 202101로 처리한다. 
			Calendar tmpCal = getCurrentCalendar();
			tmpCal.add(Calendar.WEEK_OF_YEAR, 1);
			
			int tmpYear = tmpCal.get(Calendar.YEAR);
			if(tmpYear != year)
				year = tmpYear;
		}
		
		return (year * 100) + weekOfYear;
	}
	
	// 특정 시간값을 입력받아 해당 주로 계산됨
	public static int getWeekOfYear(long unixTime)
	{
		Calendar cal = getCurrentCalendar();
		cal.setTimeInMillis(unixTime * 1000);
		
		int weekDay = cal.get(Calendar.DAY_OF_WEEK);
		if(weekDay == Calendar.SUNDAY)
		{
			// 1주일 전 주차로 처리한다. (월화수목금토일이 동일 주차 계산 - 월요일부터 시작)
			cal.add(Calendar.WEEK_OF_YEAR, -1);
		}
		
		int year = cal.get(Calendar.YEAR);
		int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
		if(weekOfYear == 1)
		{
			// 1주차일때 작년도 연말일 수 있다. (해당 연도 확인)
			// 다음주와 연도가 같은지 확인하여 처리한다. 2020-12-31일은 01주차로 나오기 때문에 202101로 처리한다. 
			Calendar tmpCal = getCurrentCalendar();
			tmpCal.add(Calendar.WEEK_OF_YEAR, 1);
			
			int tmpYear = tmpCal.get(Calendar.YEAR);
			if(tmpYear != year)
				year = tmpYear;
		}
		
		return (year * 100) + weekOfYear;
	}
	
	// 시작시간 부터 현재시간까지의 주차
	public static int getWeekOfYearToStartTime(long startTime)
	{
		// StartTime으로 설정한 Calendar
		Calendar startCal = Calendar.getInstance();
		startCal.setTimeInMillis(startTime * 1000);
		
		int startWeekDay = startCal.get(Calendar.DAY_OF_WEEK);
		if(startWeekDay == Calendar.SUNDAY)
			startCal.add(Calendar.WEEK_OF_YEAR, -1);
		
		int startYear = startCal.get(Calendar.YEAR);
		int startWeek = startCal.get(Calendar.WEEK_OF_YEAR);
		
		// 현재 시간 Calendar
		Calendar curCal = getCurrentCalendar();
		
		int curWeekDay = curCal.get(Calendar.DAY_OF_WEEK);
		if(curWeekDay == Calendar.SUNDAY)
			curCal.add(Calendar.WEEK_OF_YEAR, -1);
		
		int curYear = curCal.get(Calendar.YEAR);
		int curWeek = curCal.get(Calendar.WEEK_OF_YEAR);
		
		int weeks = ConstantVal.DEFAULT_ZERO;
		if(startYear == curYear && curWeek >= startWeek)
		{
			weeks = (curWeek - startWeek) + 1;
		}
		else
		{
			// 흘러간 주차 계산 시 연도가 변경된 경우, 연말까지의 주차를 계산한다.
			Calendar tmpCal = Calendar.getInstance();
			tmpCal.set(Calendar.YEAR, startYear);
			tmpCal.set(Calendar.MONTH, Calendar.DECEMBER);
			tmpCal.set(Calendar.DAY_OF_MONTH, tmpCal.getActualMaximum(Calendar.DAY_OF_MONTH));
			
			int tmpWeekDay = tmpCal.get(Calendar.DAY_OF_WEEK);
			if(tmpWeekDay == Calendar.SUNDAY)
				tmpCal.add(Calendar.WEEK_OF_YEAR, -1);
			
			int tmpWeek = tmpCal.get(Calendar.WEEK_OF_YEAR);
			if(tmpWeek < startWeek)
				tmpCal.add(Calendar.WEEK_OF_YEAR, -1);
			
			tmpWeek = tmpCal.get(Calendar.WEEK_OF_YEAR);

			// 현재 주차 + 작년의 총 주차를 더한다.
			curWeek += tmpWeek;
			
			weeks = (curWeek - startWeek) + 1;
		}
			
		return weeks;
	}
	
	
	// 특정 주의 특정요일의 unixTime
	// calWeek : 특정주 설정(-1:전주, 0:현재주, 1:다음주)
	// day : ConstantVal.MONDAY ~ ConstantVal.SUNDAY
	public static int getWeekDayToUnixTime(int calWeek, int day)
	{
		Calendar cal = getCurrentCalendar();
		
		if(day == ConstantVal.SUNDAY)
			calWeek += 1;
		
		cal.add(Calendar.WEEK_OF_YEAR, calWeek);
		cal.set(Calendar.DAY_OF_WEEK, day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return (int)(cal.getTimeInMillis() / 1000);
	}

	
	// 현재연도 + 해당주로 계산됨 ( ex-201803 )
	public static int getMonthOfYear()
	{
		Calendar cal = getCurrentCalendar();
		
		return cal.get(Calendar.YEAR) * 100 + (cal.get(Calendar.MONTH) + 1);
	}
	
	
	//입력 날짜의 다음달까지의 남은 초를 반환.
	public static int diffOfNextMonth(int date) throws Exception
	{
		Date date2=intMonthFormat.parse(date+"");
		Calendar cal=Calendar.getInstance();
		cal.setTime(date2);
		cal.add(Calendar.MONTH,1);
		
		long unixTime = cal.getTime().getTime();
		return (int)(unixTime/1000-getCurrentUnixTime());
	}

	
	// 지정된 tmType 까지만 표시 (ex - day : 20190301 month : 201803, year : 2018)
	public static int getIntDate(byte tmType, int calVal)
	{
		Calendar cal = getCurrentCalendar();

		String str = null;

		if (tmType == ConstantVal.DATE_SECTION_DAY)
		{
			cal.add(Calendar.DATE, calVal);
			str = intDayFormat.format(cal.getTime());
		}
		else if (tmType == ConstantVal.DATE_SECTION_MONTH)
		{
			cal.add(Calendar.MONTH, calVal);
			str = intMonthFormat.format(cal.getTime());
		} 
		else if (tmType == ConstantVal.DATE_SECTION_YEAR)
		{
			cal.add(Calendar.DAY_OF_YEAR, calVal);
			str = yearFormat.format(cal.getTime());
		}

		if (str == null)
			return ConstantVal.DEFAULT_VALUE;

		return Integer.parseInt(str);
	}
	
	public static int getEndOfWeekUT() throws Exception
	{
		int dayOfWeek = dayOfWeek();					// 요일 확인 
		int todayTime = (int)getNextDayUnixTime(0); 	// 시작 시간 확인  00:00:00 초
		int currentTime = getCurrentUnixTime();			// 현재 시간 확인 
		
		if (dayOfWeek == 1) 	// 일요일
			return todayTime + ConstantVal.DAY_OF_SECOND - currentTime;
		
		int endOfWeek = todayTime + ((9 - dayOfWeek) * ConstantVal.DAY_OF_SECOND); 
		return endOfWeek - currentTime;
	}
	
	// 매달 시작과 말일 계산
	/*public static CurrentMonth getMonthUT() throws Exception
	{
		CurrentMonth month = new CurrentMonth();
		month.start = doDateUnixTime(ConstantVal.DATE_SECTION_MONTH, 0);			// 1일 00:00:00 
		month.end = doDateUnixTime(ConstantVal.DATE_SECTION_MONTH, 1) - 1; 			// 말일 23:59:59
		return month;
	}*/
	
	public static int getEndOfMonthUT() throws Exception
	{
		int nextMonth = doDateUnixTime(ConstantVal.DATE_SECTION_MONTH, 1); 			// 다음달 1일 00:00:00초 시간 확인  
		int currentTime = getCurrentUnixTime();										// 현재 시간 확인
		
		return nextMonth - currentTime;
	}
	
	public static long StringDateToUnixTime(String date) throws Exception
	{
		Date dateTime = intDayFormat.parse(date);
		long timeStamp = dateTime.getTime();
		return timeStamp;
		
	}
	
	//////////////////////////////////////////////////////////////////////
	//																	//
	//  DEBUG FUNCTION													//
	//																	//
	//////////////////////////////////////////////////////////////////////
	
	
	public static String getEndOfWeek() throws Exception
	{
		try
		{
			Calendar cal = getCurrentCalendar();
			cal.add(Calendar.DATE, 7 - cal.get(Calendar.DAY_OF_WEEK));

			return formatter.format(cal.getTime());
		} catch (Exception e)
		{
			System.out.println("getEndOfWeek");
			e.printStackTrace();
		}

		throw new WeException(5555);
	}
	
	
	// 인자값의 요일 (1:일, 2:월, 3:화, 4:수, 5:목, 6:금, 7:토) 
	public static int dayOfWeek(String date) throws Exception
	{
		if (date == null || date.isEmpty())
			return ConstantVal.DEFAULT_VALUE;
		
		try
		{
			Date nDate;
			synchronized(formatter) {
				nDate = formatter.parse(date);
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(nDate);

			return cal.get(Calendar.DAY_OF_WEEK);
		} catch (Exception e)
		{
			System.out.println("dayOfWeek : [" + date + "]");
			e.printStackTrace();
		}

		throw new WeException(5555);
	}
	
	
	public static String getEndOfMonth() throws Exception
	{
		try
		{
			Calendar cal = getCurrentCalendar();
			
			return monthFormat.format(cal.getTime()) +"-"+ cal.getActualMaximum(Calendar.DAY_OF_MONTH) + " 23:59:59";
		} catch (Exception e)
		{
			System.out.println("getEndOfMonth");
			e.printStackTrace();
		}

		throw new WeException(5555);
	}
	
	
	public static String currentDate()
	{
		return formatter.format(getCurrentCalendar().getTime());
	}
	
	
	// 날짜 형식으로 온 dateTime 값을 unix 타임으로 변경 후 현재시간과의 차이를 전달
	public static int diffOfDate(String dateTime) throws Exception
	{
		return (int) diffOfUnixTime(getDateToUnixTime(dateTime));
	}
	
	
	/*
	 * 두 날짜의 시간 차이 확인 startDate : 시작 날짜 (날짜 포맷 : 년-월-일 시:분:초) endDate : 종료 날짜
	 * (날짜 포맷 : 년-월-일 시:분:초) return : 두 날짜의 시간 간격을 초로 환산해서 전달
	 */
	public static long diffOfDate(String startDate, String endDate)
	{
		try
		{
			Date startTime = formatter.parse(startDate);
			Date endTime = formatter.parse(endDate);

			long diff = endTime.getTime() - startTime.getTime();
			return (diff / 1000);
		} catch (Exception e)
		{

		}

		return 0;
	}
	
	
	// dataTime -> unixTime
	public static long getDateToUnixTime(String dateTime) throws Exception
	{
		if (dateTime == null || dateTime.isEmpty())
			return ConstantVal.DEFAULT_VALUE;
		
		try
		{
			long unixTime;
			synchronized(formatter) {
				unixTime = formatter.parse(dateTime).getTime();
			}
			
			return (unixTime / 1000);
		} catch (Exception e)
		{
			System.out.println("==========================");
			System.out.println("getUnixTimeToDate : [" + dateTime + "]");
			System.out.println("==========================");
			e.printStackTrace();
		}

		throw new WeException(5555);
	}
	
	
	/*
	 * 현재 시간에서 년, 월, 일, 시를 증감/차감 한다. 
	 * tmType : 증/차감 할 시간 종류 ( 1:년, 2:월, 3:일, 4:시 ) 
	 * calVAl : 증/차감 할 양 (tmType이 1일 경우 1이면 미래 1년 후 시간 -일 경우에는 1년 전 시간)
	 */
	public static String doDateString(byte tmType, int calVal)
	{
		Calendar cal = getCurrentCalendar();

		switch (tmType)
		{
		case ConstantVal.DATE_SECTION_YEAR:
			cal.add(Calendar.YEAR, calVal);
			return yearFormat.format(cal.getTime()) + "-01-01 00:00:00";
			
		case ConstantVal.DATE_SECTION_MONTH:
			cal.add(Calendar.MONTH, calVal);
			return monthFormat.format(cal.getTime()) + "-01 00:00:00";
			
		case ConstantVal.DATE_SECTION_DAY:
			cal.add(Calendar.DAY_OF_YEAR , calVal);
			return dayFormat.format(cal.getTime()) + " 00:00:00";
			
		case ConstantVal.DATE_SECTION_HOUR:
			cal.add(Calendar.HOUR, calVal);
			return hourFormat.format(cal.getTime()) + ":00:00";
			
		case ConstantVal.DATE_SECTION_HOUR_FULL:
			cal.add(Calendar.HOUR, calVal);
			return formatter.format(cal.getTime());
			
		case ConstantVal.DATE_SECTION_MINUTE:
			cal.add(Calendar.MINUTE, calVal);
			return formatter.format(cal.getTime());
			
		default:
			break;
		}

		return dayFormat.format(cal.getTime()) +" 00:00:00";
	}
}
