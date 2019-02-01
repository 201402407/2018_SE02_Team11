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
	
	// 1. Install Parameters (DAO�� ���� �� �ְ�)
	try {
		attNum = Integer.parseInt( req.getParameter(rp_attNum) );
	} catch (Exception e) {
		//����ȯ����
		OurProcResp.printResp(out, "������ȣ�� ����� �Է����ּ���.", rp_attNum, null);
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
		OurProcResp.printResp(out, "DB������ �߻��Ͽ����ϴ�.", null, null);
		return;
	}
}
%>

<%
request.setCharacterEncoding("euc-kr");
makeMyResponse(request, out);
%>