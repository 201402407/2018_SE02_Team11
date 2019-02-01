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

<%!
private void makeMyResponse(HttpServletRequest req, JspWriter out) throws IOException
{	
	int lcode;
	final String rp_lcode = "lcode";
	
	// 1. Install Parameters (DAO에 넣을 수 있게)	
	try {
		lcode = Integer.parseInt( req.getParameter(rp_lcode) );
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "분반코드를 제대로 입력해주세요.", rp_lcode, null);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		AttendanceDAO dao = new AttendanceDAO();
		List<AttendanceListByLCode> list = dao.getAttendanceListbyLCode(lcode);
				
		JSONArray listJson = new JSONArray();
		for(AttendanceListByLCode elem : list)
		{
			JSONObject elemJson = new JSONObject();
			elemJson.put("subjectName", elem.getSubjectName());
			elemJson.put("attendanceNum", elem.getAttendanceNum());
			elemJson.put("isRetake", elem.isRetake());
			elemJson.put("studentID", elem.getStudentID());
			elemJson.put("studentName", elem.getStudentName());
			
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
request.setCharacterEncoding("utf-8");
makeMyResponse(request, out);
%>