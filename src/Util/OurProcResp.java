package Util;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import org.json.simple.*;

public class OurProcResp {
	
	public static boolean reqParamVoidString(String str)
	{
		return str == null || str.length() == 0;
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
