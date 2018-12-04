package Util;

import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 현재 시간, 현재 학기 구하기에 관한 표준안을 줄 헬퍼 클래스
 * e.g. 현재가 *** 가능한 기간인가?
 */
public class OurTimes {
	
	private static int YEAR_DONT_CARE = 0;  //연도가 정해지지 않음. (예컨데 수강신청을 "특정 년도"의 2월 1일~2월 7일에만 해야 한다는 것이 아니다.)
	
	// 학교 규정상의 날짜를 여기에 써넣으면 된다.
	private static LocalDate BEGIN_ADD_ATTENDANCE_SPRING  //제 1학기의 수강신청 시작날짜
		= LocalDate.of(YEAR_DONT_CARE, 2, 1);  //2월 1일
	private static LocalDate END_ADD_ATTENDANCE_SPRING  //제 1학기의 수강신청 끝날짜 (여기서 하루가 지나면 수강신청 불가)
		= LocalDate.of(YEAR_DONT_CARE, 2, 7);  //2월 7일
	private static LocalDate BEGIN_ADD_ATTENDANCE_FALL  //제 2학기의 수강신청 시작날짜
		= LocalDate.of(YEAR_DONT_CARE, 8, 1);  //8월 1일
	private static LocalDate END_ADD_ATTENDANCE_FALL  //제 2학기의 수강신청 끝날짜 (여기서 하루가 지나면 수강신청 불가)
		= LocalDate.of(YEAR_DONT_CARE, 8, 7);  //8월 7일
	
	private static LocalDate BEGIN_TERM_SPRING  //제1학기 시작
		= LocalDate.of(YEAR_DONT_CARE, 3, 1);  //3월 1일
	private static LocalDate END_TERM_SPRING  //제1학기 종료
		= LocalDate.of(YEAR_DONT_CARE, 5, 31);  //5월 31일
	private static LocalDate BEGIN_TERM_FALL  //제2학기 시작
		= LocalDate.of(YEAR_DONT_CARE, 9, 1);  //9월 1일
	private static LocalDate END_TERM_FALL  //제2학기 종료
		= LocalDate.of(YEAR_DONT_CARE, 11, 30);  //11월 30일
	
	
	/**
	 * 년도 구별 없는 날짜 비교를 한다.
	 * @return 0보다 크다 -> dtA가 dtB보다 늦다(미래에 있다).
	 */
	private static int compareDateWithoutYear(LocalDate dA, LocalDate dB) {
	    int cmp = (dA.getMonthValue() - dB.getMonthValue());
	    if (cmp == 0) {
	        cmp = (dA.getDayOfMonth() - dB.getDayOfMonth());
	    }
	    return cmp;
	}
	
	/**
	 * 현재 날짜 및 시각 
	 */
	public static LocalDateTime dateTimeNow()
	{
		return LocalDateTime.now();
	}
	
	/**
	 * 현재 날짜
	 */
	public static LocalDate dateNow()
	{
		return LocalDate.now();
	}
	
	
	
	/**
	 * 현재가 수강신청 기간인가?
	 * @return 현재 수강신청기간이면 true
	 */
	public static boolean isNowAbleToAddAttendance()
	{
		LocalDate today = dateNow();
		return
				(compareDateWithoutYear(today, BEGIN_ADD_ATTENDANCE_SPRING) >= 0
				&& compareDateWithoutYear(today, END_ADD_ATTENDANCE_SPRING) <= 0)  //제1학기
				||
				(compareDateWithoutYear(today, BEGIN_ADD_ATTENDANCE_FALL) >= 0
				&& compareDateWithoutYear(today, END_ADD_ATTENDANCE_FALL) <= 0);  //제2학기
	}
	
	/**
	 * 현재가 학기중인가?<br>
	 * □□□□□□■■■■■■■■■□□□□□□□□□■■■■■■■■■□□□<br>
	 * ▷▷▷▷▷▷제1학기▷▷▷▷▷▷▷▷▷▷▷▷▷▷▷제2학기 <br>
	 * □ = false, ■ = true
	 * @return 현재 학기중이면 true
	 */
	public static boolean isNowOnTerm()
	{
		LocalDate today = dateNow();
		return
				(compareDateWithoutYear(today, BEGIN_TERM_SPRING) >= 0
				&& compareDateWithoutYear(today, END_TERM_SPRING) <= 0)  //제1학기
				||
				(compareDateWithoutYear(today, BEGIN_TERM_FALL) >= 0
				&& compareDateWithoutYear(today, END_TERM_FALL) <= 0);  //제2학기
	}
	
	/**
	 * 학기를 표현하는 정수(예: 20151 = 2015년 제1학기)를 구해준다. 데이터베이스에 들어갈 학기 형식도 이렇게 한다.
	 * @param year 년도
	 * @param sprOrFall 1=제1학기, 2=제2학기
	 * @throws IllegalArgumentException sprOrFall이 1과 2 중 하나가 아니다.
	 */
	public static int termValue(int year, int sprOrFall)
	{
		if( !(sprOrFall == 1 || sprOrFall==2) )
			throw new IllegalArgumentException("sprOrFall이 1과 2 중 하나가 아니다.");
		
		return year*10 + sprOrFall;
	}
	
	/**
	 * 현재에서 앞으로 다가올 학기 중 가장 가까운 것
	 * @return e.g. 현재가 2015년도 1학기라면 결과는 20152.
	 * 현재가 2015년도 여름방학이라면 결과는 20152.
	 * 현재가 2015년도 2학기라면 결과는 20161.
	 */
	public static int closestFutureTerm()
	{
		LocalDate today = dateNow();
		
		int termCurrYearSpring = termValue(today.getYear(), 1);
		int termCurrYearFall = termValue(today.getYear(), 2);
		int termNextYearSpring = termValue(today.getYear()+1, 1);
		
		//       ▽1학기시작                 ▽2학기시작
		// □□□□□□■■■■■■■■■□□□□□□□□□■■■■■■■■■□□□
		if(compareDateWithoutYear(today, BEGIN_TERM_SPRING) < 0)
			return termCurrYearSpring;
		else if(compareDateWithoutYear(today, BEGIN_TERM_FALL) < 0)
			return termCurrYearFall;
		else
			return termNextYearSpring;
	}
	
	/**
	 * 현재에 해당하는 학기, 학기중이 아니라면 결과는 0
	 * @return e.g. 현재가 2015년도 1학기라면 결과는 20151.
	 * 현재가 2015년도 여름방학이라면 결과는 0.
	 * 현재가 2015년도 2학기라면 결과는 20152.
	 */
	public static int currentTerm()
	{
		if(!isNowOnTerm())  //학기 중이 아니다.
			return 0;
		
		LocalDate today = dateNow();
		
		int termCurrYearSpring = termValue(today.getYear(), 1);
		int termCurrYearFall = termValue(today.getYear(), 2);
		// □□□□□□■■■■■■■■■□□□□□□□□□■■■■■■■■■□□□
		
		if(compareDateWithoutYear(today, BEGIN_TERM_FALL) < 0)
			return termCurrYearSpring;
		else 
			return termCurrYearFall;
	}
	
	/**
	 * DB상에서 int로 된 dayOfWeek을 자바의 DayOfWeek 타입으로 바꿔준다. 입력 정수가 범위를 벗어나면 null이 된다.
	 * @param dow (1=월, 2=화, 3=수, 4=목, 5=금)
	 */
	public static DayOfWeek intToDayOfWeek(int dow)
	{
		switch(dow)
		{
		case 1:
			return DayOfWeek.MONDAY;
		case 2:
			return DayOfWeek.TUESDAY;
		case 3:
			return DayOfWeek.WEDNESDAY;
		case 4:
			return DayOfWeek.THURSDAY;
		case 5:
			return DayOfWeek.FRIDAY;
		default:
			return null;
		}
	}
	
	/**
	 * DB상의 java.sql.Date을 LocalTime으로 바꿔준다.
	 * @param sqldate ResultSet.getDate()으로 구해왔을 java.sql.Date 객체
	 */
	public static LocalDate sqlDateToLocalDate(Date sqldate)
	{
		return sqldate.toLocalDate();
	}
	
	/**
	 * DB상의 java.sql.Time을 LocalTime으로 바꿔준다.
	 * @param sqltime ResultSet.getTime()으로 구해왔을 java.sql.Time 객체
	 */
	public static LocalTime sqlTimeToLocalTime(Time sqltime)
	{
		return sqltime.toLocalTime();
	}
	
	/** 
	 * Java 상의 LocalDate를 java.sql.Date으로 변경한다.
	 * sql에 insert를 위한 변경 함수
	 * @param LocalDate javalocaldate*/
	public static Date LocalDateTosqlDate(LocalDate javalocaldate) {
		return Date.valueOf(javalocaldate);
	}
	
	/** 
	 * Java 상의 LocalTime를 java.sql.Time으로 변경한다.
	 * sql에 insert를 위한 변경 함수
	 * @param LocalDate javalocaltime*/
	public static Time LocalDateTosqlDate(LocalTime javalocaltime) {
		return Time.valueOf(javalocaltime);
	}
	
}
