package Util;

import java.sql.Date;
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
	
	private static int YEAR_DONT_CARE = 0;  //������ �������� ����. (������ ������û�� "Ư�� �⵵"�� 2�� 1��~2�� 7�Ͽ��� �ؾ� �Ѵٴ� ���� �ƴϴ�.)
	
	// �б� �������� ��¥�� ���⿡ ������� �ȴ�.
	private static LocalDate BEGIN_ADD_ATTENDANCE_SPRING  //�� 1�б��� ������û ���۳�¥
		= LocalDate.of(YEAR_DONT_CARE, 2, 1);  //2�� 1��
	private static LocalDate END_ADD_ATTENDANCE_SPRING  //�� 1�б��� ������û ����¥ (���⼭ �Ϸ簡 ������ ������û �Ұ�)
		= LocalDate.of(YEAR_DONT_CARE, 2, 7);  //2�� 7��
	private static LocalDate BEGIN_ADD_ATTENDANCE_FALL  //�� 2�б��� ������û ���۳�¥
		= LocalDate.of(YEAR_DONT_CARE, 8, 1);  //8�� 1��
	private static LocalDate END_ADD_ATTENDANCE_FALL  //�� 2�б��� ������û ����¥ (���⼭ �Ϸ簡 ������ ������û �Ұ�)
		= LocalDate.of(YEAR_DONT_CARE, 8, 7);  //8�� 7��
	
	private static LocalDate BEGIN_TERM_SPRING  //��1�б� ����
		= LocalDate.of(YEAR_DONT_CARE, 3, 1);  //3�� 1��
	private static LocalDate END_TERM_SPRING  //��1�б� ����
		= LocalDate.of(YEAR_DONT_CARE, 5, 31);  //5�� 31��
	private static LocalDate BEGIN_TERM_FALL  //��2�б� ����
		= LocalDate.of(YEAR_DONT_CARE, 9, 1);  //9�� 1��
	private static LocalDate END_TERM_FALL  //��2�б� ����
		= LocalDate.of(YEAR_DONT_CARE, 11, 30);  //11�� 30��
	
	
	/**
	 * �⵵ ���� ���� ��¥ �񱳸� �Ѵ�.
	 * @return 0���� ũ�� -> dtA�� dtB���� �ʴ�(�̷��� �ִ�).
	 */
	private static int compareDateWithoutYear(LocalDate dA, LocalDate dB) {
	    int cmp = (dA.getMonthValue() - dB.getMonthValue());
	    if (cmp == 0) {
	        cmp = (dA.getDayOfMonth() - dB.getDayOfMonth());
	    }
	    return cmp;
	}
	
	/**
	 * ���� ��¥ �� �ð� 
	 */
	public static LocalDateTime dateTimeNow()
	{
		return LocalDateTime.now();
	}
	
	/**
	 * ���� ��¥
	 */
	public static LocalDate dateNow()
	{
		return LocalDate.now();
	}
	
	
	
	/**
	 * ���簡 ������û �Ⱓ�ΰ�?
	 * @return ���� ������û�Ⱓ�̸� true
	 */
	public static boolean isNowAbleToAddAttendance()
	{
		LocalDate today = dateNow();
		return
				(compareDateWithoutYear(today, BEGIN_ADD_ATTENDANCE_SPRING) >= 0
				&& compareDateWithoutYear(today, END_ADD_ATTENDANCE_SPRING) <= 0)  //��1�б�
				||
				(compareDateWithoutYear(today, BEGIN_ADD_ATTENDANCE_FALL) >= 0
				&& compareDateWithoutYear(today, END_ADD_ATTENDANCE_FALL) <= 0);  //��2�б�
	}
	
	/**
	 * ���簡 �б����ΰ�?<br>
	 * �������������������������������������<br>
	 * ��������������1�б⢹������������������������������2�б� <br>
	 * �� = false, �� = true
	 * @return ���� �б����̸� true
	 */
	public static boolean isNowOnTerm()
	{
		LocalDate today = dateNow();
		return
				(compareDateWithoutYear(today, BEGIN_TERM_SPRING) >= 0
				&& compareDateWithoutYear(today, END_TERM_SPRING) <= 0)  //��1�б�
				||
				(compareDateWithoutYear(today, BEGIN_TERM_FALL) >= 0
				&& compareDateWithoutYear(today, END_TERM_FALL) <= 0);  //��2�б�
	}
	
	/**
	 * �б⸦ ǥ���ϴ� ����(��: 20151 = 2015�� ��1�б�)�� �����ش�. �����ͺ��̽��� �� �б� ���ĵ� �̷��� �Ѵ�.
	 * @param year �⵵
	 * @param sprOrFall 1=��1�б�, 2=��2�б�
	 * @throws IllegalArgumentException sprOrFall�� 1�� 2 �� �ϳ��� �ƴϴ�.
	 */
	public static int termValue(int year, int sprOrFall)
	{
		if( !(sprOrFall == 1 || sprOrFall==2) )
			throw new IllegalArgumentException("sprOrFall�� 1�� 2 �� �ϳ��� �ƴϴ�.");
		
		return year*10 + sprOrFall;
	}
	
	/**
	 * ���翡�� ������ �ٰ��� �б� �� ���� ����� ��
	 * @return e.g. ���簡 2015�⵵ 1�б��� ����� 20152.
	 * ���簡 2015�⵵ ���������̶�� ����� 20152.
	 * ���簡 2015�⵵ 2�б��� ����� 20161.
	 */
	public static int closestFutureTerm()
	{
		LocalDate today = dateNow();
		
		int termCurrYearSpring = termValue(today.getYear(), 1);
		int termCurrYearFall = termValue(today.getYear(), 2);
		int termNextYearSpring = termValue(today.getYear()+1, 1);
		
		//       ��1�б����                 ��2�б����
		// �������������������������������������
		if(compareDateWithoutYear(today, BEGIN_TERM_SPRING) < 0)
			return termCurrYearSpring;
		else if(compareDateWithoutYear(today, BEGIN_TERM_FALL) < 0)
			return termCurrYearFall;
		else
			return termNextYearSpring;
	}
	
	/**
	 * ���翡 �ش��ϴ� �б�, �б����� �ƴ϶�� ����� 0
	 * @return e.g. ���簡 2015�⵵ 1�б��� ����� 20151.
	 * ���簡 2015�⵵ ���������̶�� ����� 0.
	 * ���簡 2015�⵵ 2�б��� ����� 20152.
	 */
	public static int currentTerm()
	{
		if(!isNowOnTerm())  //�б� ���� �ƴϴ�.
			return 0;
		
		LocalDate today = dateNow();
		
		int termCurrYearSpring = termValue(today.getYear(), 1);
		int termCurrYearFall = termValue(today.getYear(), 2);
		// �������������������������������������
		
		if(compareDateWithoutYear(today, BEGIN_TERM_FALL) < 0)
			return termCurrYearSpring;
		else 
			return termCurrYearFall;
	}
	
	/**
	 * DB�󿡼� int�� �� dayOfWeek�� �ڹ��� DayOfWeek Ÿ������ �ٲ��ش�. �Է� ������ ������ ����� null�� �ȴ�.
	 * @param dow (1=��, 2=ȭ, 3=��, 4=��, 5=��)
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
	 * DB���� java.sql.Date�� LocalTime���� �ٲ��ش�.
	 * @param sqldate ResultSet.getDate()���� ���ؿ��� java.sql.Date ��ü
	 */
	public static LocalDate sqlDateToLocalDate(Date sqldate)
	{
		return sqldate.toLocalDate();
	}
	
	/**
	 * DB���� java.sql.Time�� LocalTime���� �ٲ��ش�.
	 * @param sqltime ResultSet.getTime()���� ���ؿ��� java.sql.Time ��ü
	 */
	public static LocalTime sqlTimeToLocalTime(Time sqltime)
	{
		return sqltime.toLocalTime();
	}
	
	/** 
	 * Java ���� LocalDate�� java.sql.Date���� �����Ѵ�.
	 * sql�� insert�� ���� ���� �Լ�
	 * @param LocalDate javalocaldate*/
	public static Date LocalDateTosqlDate(LocalDate javalocaldate) {
		return Date.valueOf(javalocaldate);
	}
	
	/** 
	 * Java ���� LocalTime�� java.sql.Time���� �����Ѵ�.
	 * sql�� insert�� ���� ���� �Լ�
	 * @param LocalDate javalocaltime*/
	public static Time LocalDateTosqlDate(LocalTime javalocaltime) {
		return Time.valueOf(javalocaltime);
	}
	
}
