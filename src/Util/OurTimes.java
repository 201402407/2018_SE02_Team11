package Util;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * ���� �ð�, ���� �б� ���ϱ⿡ ���� ǥ�ؾ��� �� ���� Ŭ����
 * e.g. ���簡 *** ������ �Ⱓ�ΰ�?
 */
public class OurTimes {
	
	// �б� �������� ��¥�� ���⿡ ������� �ȴ�.
	private static LocalDateTime BEGIN_ADD_ATTENDANCE;
	private static LocalDateTime END_ADD_ATTENDANCE;
	
	// ��¥ ��꿡 �ʿ���
	// ���ϰ��� 0���� ũ�� => dtA�� dtB���� �ʴ�(�̷��� �ִ�).
	private static int compareWithoutYearTo(LocalDateTime dtA, LocalDateTime dtB) {
	    int cmp = (dtA.getMonthValue() - dtB.getMonthValue());
	    if (cmp == 0) {
	        cmp = (dtA.getDayOfMonth() - dtB.getDayOfMonth());
	    }
	    return cmp;
	}
	
	// ���� ��¥ �� �ð�
	public static LocalDateTime now()
	{
		return LocalDateTime.now();
	}
	
	// ���簡 ������û �Ⱓ�ΰ�?
	public static boolean isNowAbleToAddAttendance()
	{
		
	}
	
	// ���� �б⸦ ���Ѵ�.
	// e.g. 20151, 20152, 20181, 20182
	public static int currentTerm()
	{
		
	}
	
	// DB�󿡼� int�� �� dayOfWeek�� DayOfWeek Ÿ������ �ٲ��ش�.
	// �Է� ������ ������ ����� null�� �ȴ�.
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
	
	// DB���� java.sql.Time�� LocalTime���� �ٲ��ش�.
	public static LocalTime sqltimeToLocalTime(Time sqltime)
	{
		
	}
	
}
