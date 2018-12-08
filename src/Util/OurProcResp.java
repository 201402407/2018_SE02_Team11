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
		// data는 null일 수도, boolean일 수도, String일 수도, Integer일 수도, List<어떤 ClassObject>일 수도 있다.
		
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
