<%@page import="java.util.List"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="java.io.IOException"%>
<%@page import="Util.*"%>
<%@page import="ClassObject.*"%>
<%@page import="DAO.*"%>
<%@ page language="java" contentType="application/json; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.SQLException" %>
<% request.setCharacterEncoding("euc-kr"); %>

<%!
private void makeMyResponse(HttpServletRequest req, JspWriter out) throws IOException
{	
	;
	final String rp_ = "";
	
	// 1. Install Parameters (DAO에 넣을 수 있게)
	 = req.getParameter(rp_);
	if(OurProcResp.reqParamVoidString())
	{
		//문자열공백
		OurProcResp.printResp(out, " 비었습니다", rp_, null);
		return;
	}
	
	try {
		 =  req.getParameter(rp_)
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, " 제대로 입력해주세요.", rp_, null);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		DAO dao = new DAO();
		List<$$> list = dao.;
				
		JSONArray listJson = new JSONArray();
		for($$ elem : list)
		{
			JSONObject elemJson = new JSONObject();
			elemJson.put("", elem.);
			elemJson.put("", elem.);
			
			listJson.add(elemJson);
		}
		OurProcResp.printResp(out, null, null, list);
		return;
	}
	catch(Exception e)
	{
		e.printStackTrace();
		OurProcResp.printResp(out, "DB오류가 발생하였습니다.", null, null);
		return;
	}
}
%>

<%
request.setCharacterEncoding("euc-kr");
makeMyResponse(request, out);
%>