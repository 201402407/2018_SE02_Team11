<%@page import="org.json.simple.JSONArray"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="java.util.List"%>
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
	// 1. Install Parameters (문자열 공백검사 + 형변환실패 검사)
	// 없음
	
	// 2. do DAO Job
	try
	{
		TimeoffRequestDAO dao = new TimeoffRequestDAO();
		List<TimeoffRequest> list = dao.getTimeoffRequestList();
		
		JSONArray listJson = new JSONArray();
		for(TimeoffRequest elem : list)
		{
			JSONObject elemJson = new JSONObject();
			elemJson.put("reqNum", elem.getReqNum());
			elemJson.put("reqDate", elem.getReqDate());
			elemJson.put("changeType", elem.getChangeType());
			elemJson.put("startSemester", elem.getStartSemester());
			elemJson.put("endSemester", elem.getEndSemester());
			elemJson.put("reason", elem.getReason());
			elemJson.put("sid", elem.getStudentID());
			 
			listJson.add(elemJson);
		}
		OurProcResp.printResp(out, null, null, listJson);
		return;
	}
	catch(SQLException sqle)
	{
		sqle.printStackTrace();
		OurProcResp.printResp(out, "DB오류가 발생하였습니다.", null, null);
		return;
	}
}
%>

<%
request.setCharacterEncoding("euc-kr");
makeMyResponse(request, out);
%>