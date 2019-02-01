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
	int attNum;
	final String rp_attNum = "attNum";
	
	// 1. Install Parameters (DAO에 넣을 수 있게)
	try {
		attNum = Integer.parseInt( req.getParameter(rp_attNum) );
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "수강번호를 제대로 입력해주세요.", rp_attNum, null);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		AttendanceDAO dao = new AttendanceDAO();
		boolean eval = dao.getLectureEvaluationByCode(attNum);
		OurProcResp.printResp(out, null, null, eval);
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