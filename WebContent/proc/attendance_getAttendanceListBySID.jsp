<%@page import="java.time.format.DateTimeFormatter"%>
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
	int sid;
	final String rp_sid = "sid";
	
	// 1. Install Parameters (DAO에 넣을 수 있게)
	try {
		sid = Integer.parseInt( req.getParameter(rp_sid) );
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "학번을 제대로 입력해주세요.", rp_sid, null);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		AttendanceDAO dao = new AttendanceDAO();
		List<AttendanceListBySID> list = dao.getAttendanceListBySID(sid);
				
		JSONArray listJson = new JSONArray();
		for(AttendanceListBySID elem : list)
		{
			JSONObject elemJson = new JSONObject();
			elemJson.put("attNum", elem.getAttNum());
			elemJson.put("lcode", elem.getLcode());
			elemJson.put("subjectName", elem.getSubjectName());
			elemJson.put("isRetake", elem.isRetake());
			elemJson.put("registerTerm", elem.getRegisterTerm());
			elemJson.put("dayOfWeek", elem.getDayOfWeek().toString());
			elemJson.put("startTime", elem.getStartTime().format(DateTimeFormatter.ISO_TIME));
			elemJson.put("endTime", elem.getEndTime().format(DateTimeFormatter.ISO_TIME));
			elemJson.put("score", elem.getScore());
			
			listJson.add(elemJson);
		}
		OurProcResp.printResp(out, null, null, listJson);
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