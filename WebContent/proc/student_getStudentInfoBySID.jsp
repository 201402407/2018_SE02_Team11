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
	int sid;
	final String rp_sid = "sid";
	
	// 1. Install Parameters (문자열 공백검사 + 형변환실패 검사)
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
		StudentDAO dao = new StudentDAO();
		List<StudentInfo> list = dao.getStudentInfoBySID(sid);
		
		if(list.isEmpty())
		{
			OurProcResp.printResp(out, "주어진 학번으로 학적상태를 찾지 못했습니다.", null, null);
			return;
		}
		StudentInfo elem = list.get(0);
		JSONObject resultJson = new JSONObject();
		resultJson.put("name", elem.getStudentName());
		resultJson.put("departmentName", elem.getDepartmentName());
		resultJson.put("semester", elem.getSemester());
		resultJson.put("isTimeOff", elem.isTimeOff());
		resultJson.put("isGraduate", elem.isGraduate());
		resultJson.put("year", elem.getYear());
		
		OurProcResp.printResp(out, null, null, resultJson);
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