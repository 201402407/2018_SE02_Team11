package Util;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.jsp.JspWriter;

import org.json.simple.*;

import ClassObject.ChangeType;

public class OurProcResp {
	
	/**
	 * ���ڿ��� ����ְų� null���� Ȯ��
	 */
	public static boolean reqParamVoidString(String str)
	{
		return str == null || str.length() == 0;
	}
	
	/**
	 * ���ڿ��� ���� �б⸦ �ùٸ���
	 */
	public static int getValidSemester(String rp_semester) throws Exception
	{
		if(rp_semester == null)
			throw new NullPointerException("semester parameter in null");
		if(rp_semester.length() != 5)
			throw new NumberFormatException("semester parameter must be 5 letters");
		return Integer.parseInt(rp_semester);
	}
	
	/**
	 * ���ڿ��� ���� �޺��б����� �ùٸ���
	 */
	public static ChangeType getValidChangeType(String rp_changeType) throws Exception
	{
		if(rp_changeType == null)
			throw new NullPointerException("changeType parameter is null");
		
		if(rp_changeType.equals("takeoff"))
			return ChangeType.TAKEOFF;
		else if(rp_changeType.equals("resume"))
			return ChangeType.RESUME;
		else
			throw new IllegalArgumentException("neither 'takeoff' nor 'resume'");
	}
	
	/**
	 * ���ڿ��� ���� ������ �ùٸ���
	 * ������ ��~������ ���ѵȴ�.
	 */
	public static DayOfWeek getValidDayOfWeek(String rp_dayofweek) throws Exception
	{
		if(rp_dayofweek == null)
			throw new NullPointerException("dayofweek parameter is null");
		
		DayOfWeek dow;
		dow = DayOfWeek.valueOf(rp_dayofweek);
		switch(dow)
		{
		case FRIDAY:
		case MONDAY:
		case THURSDAY:
		case TUESDAY:
		case WEDNESDAY:
			break;
		default:
			throw new IllegalArgumentException("Day of week should be MONDAY~FRIDAY");
		
		}
		return dow;
	}
	
	/**
	 * ���ڿ��� ���� �ð��� �ùٸ���
	 * hh:mm:ss �����̾�� �Ѵ�
	 */
	public static LocalTime getValidLocalTime(String rp_time) throws Exception
	{
		if(rp_time == null)
			throw new NullPointerException("time parameter is null");
		
		return LocalTime.parse(rp_time, DateTimeFormatter.ISO_LOCAL_TIME);
	}
	
	public static String respToJSONString(String error, String attention_field, Object data)
	{
		// data�� null�� ����, boolean�� ����, String�� ����, Integer�� ����, List<� ClassObject>�� ���� �ִ�.
		
		JSONObject respObj = new JSONObject();
		respObj.put("error", error);
		respObj.put("attention_field", attention_field);
		respObj.put("data", data);
		return respObj.toJSONString();
	}
	
	public static void printResp(JspWriter out, String error, String attention_field, Object data) throws IOException
	{
		out.println(OurProcResp.respToJSONString(error, attention_field, data));
	}
	
}
