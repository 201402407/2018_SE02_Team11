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
	int semester;
	final String rp_semester = "semester";
	
	// 1. Install Parameters (DAO에 넣을 수 있게)
	try {
		sid = Integer.parseInt( req.getParameter(rp_sid) );
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "학번을 제대로 입력해주세요.", rp_sid, null);
		return;
	}
	
	try {
		semester = Integer.parseInt( req.getParameter(rp_semester) );
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "학기를 제대로 입력해주세요.", rp_semester, null);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		AttendanceDAO dao = new AttendanceDAO();
		List<GradeInfoOfTerm> list = dao.getGradeInfo(sid, semester);
				
		JSONArray listJson = new JSONArray();
		for(GradeInfoOfTerm elem : list)
		{
			JSONObject elemJson = new JSONObject();
			elemJson.put("isVisible", elem.isVisibleGrade());
			elemJson.put("subjectName", elem.getSubjectName());
			elemJson.put("grade", elem.getGrade());
			elemJson.put("isRetake", elem.isRetake());
			elemJson.put("gradeBefore", elem.getGradeBefore());
			
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