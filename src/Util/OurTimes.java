package Util;

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
	
	// 학교 규정상의 날짜를 여기에 써넣으면 된다.
	private static LocalDateTime BEGIN_ADD_ATTENDANCE;
	private static LocalDateTime END_ADD_ATTENDANCE;
	
	// 날짜 계산에 필요함
	// 리턴값이 0보다 크다 => dtA가 dtB보다 늦다(미래에 있다).
	private static int compareWithoutYearTo(LocalDateTime dtA, LocalDateTime dtB) {
	    int cmp = (dtA.getMonthValue() - dtB.getMonthValue());
	    if (cmp == 0) {
	        cmp = (dtA.getDayOfMonth() - dtB.getDayOfMonth());
	    }
	    return cmp;
	}
	
	// 현재 날짜 및 시각
	public static LocalDateTime now()
	{
		return LocalDateTime.now();
	}
	
	// 현재가 수강신청 기간인가?
	public static boolean isNowAbleToAddAttendance()
	{
		
	}
	
	// 현재 학기를 구한다.
	// e.g. 20151, 20152, 20181, 20182
	public static int currentTerm()
	{
		
	}
	
	// DB상에서 int로 된 dayOfWeek을 DayOfWeek 타입으로 바꿔준다.
	// 입력 정수가 범위를 벗어나면 null이 된다.
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
	
	// DB상의 java.sql.Time을 LocalTime으로 바꿔준다.
	public static LocalTime sqltimeToLocalTime(Time sqltime)
	{
		
	}
	
}
