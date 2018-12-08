package Util;

import org.json.simple.*;

public class OurProcResp {
	
	public static boolean reqParamVoidString(String str)
	{
		return str == null || str.length() == 0;
	}
	
	public static void printResp(String error, String attention_field, Object data)
	{
		
	}
	
}
