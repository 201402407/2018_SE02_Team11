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
	int scholnum;
	final String rp_scholnum= "scholnum";
	int money;
	final String rp_money = "money";
	int sid;
	final String rp_sid = "sid";
	
	// 1. Install Parameters (DAO에 넣을 수 있게)
	try {
		scholnum = Integer.parseInt( req.getParameter(rp_scholnum) );
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "장학번호를 제대로 입력해주세요.", rp_scholnum, null);
		return;
	}
	
	try {
		money = Integer.parseInt( req.getParameter(rp_money) );
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "장학금을 제대로 입력해주세요.", rp_money, null);
		return;
	}
	
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
		ScholarshipAwardDAO dao = new ScholarshipAwardDAO();
		switch(dao.awardToStudent(scholnum, money, sid))
		{
		case SUCCESS:
			OurProcResp.printResp(out, null, null, null);
			return;
		case NOT_FOUND_STUDENT:
			OurProcResp.printResp(out, "존재하지 않는 학생입니다.", rp_sid, null);
			return;
		case NOT_FOUND_SCHOLAR:
			OurProcResp.printResp(out, "존재하지 않는 장학번호입니다.", rp_scholnum, null);
			return;
		}
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