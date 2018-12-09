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
	int attendancenum;
	final String rp_attendancenum = "attendancenum";
	double grade;
	final String rp_grade = "grade";
	
	// 1. Install Parameters (DAO에 넣을 수 있게)
	try {
		attendancenum = Integer.parseInt( req.getParameter(rp_attendancenum) );
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "수강번호를 제대로 입력해주세요.", rp_attendancenum, null);
		return;
	}
	
	try {
		grade = Double.parseDouble( req.getParameter(rp_grade) );
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "평점을 제대로 입력해주세요.", rp_grade, null);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		GradeInfoDAO dao = new GradeInfoDAO();
		if(dao.addGrade(attendancenum, grade))
		{
			OurProcResp.printResp(out, null, null, null);
			return;
		}
		else
		{
			OurProcResp.printResp(out, "성적 입력이 제대로 이루어지지 않은 듯 합니다.", null, null);
			return;
		}
	}
	catch(Exception e)
	{
		e.printStackTrace();
		OurProcResp.printResp(out, "DB오류가 발생하였습니다. 수강번호가 올바른지 확인해주세요.", null, null);
		return;
	}
}
%>

<%
request.setCharacterEncoding("euc-kr");
makeMyResponse(request, out);
%>