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
	
	// 1. Install Parameters (DAO�� ���� �� �ְ�)
	try {
		scholnum = Integer.parseInt( req.getParameter(rp_scholnum) );
	} catch (Exception e) {
		//����ȯ����
		OurProcResp.printResp(out, "���й�ȣ�� ����� �Է����ּ���.", rp_scholnum, null);
		return;
	}
	
	try {
		money = Integer.parseInt( req.getParameter(rp_money) );
	} catch (Exception e) {
		//����ȯ����
		OurProcResp.printResp(out, "���б��� ����� �Է����ּ���.", rp_money, null);
		return;
	}
	
	try {
		sid = Integer.parseInt( req.getParameter(rp_sid) );
	} catch (Exception e) {
		//����ȯ����
		OurProcResp.printResp(out, "�й��� ����� �Է����ּ���.", rp_sid, null);
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
			OurProcResp.printResp(out, "�������� �ʴ� �л��Դϴ�.", rp_sid, null);
			return;
		case NOT_FOUND_SCHOLAR:
			OurProcResp.printResp(out, "�������� �ʴ� ���й�ȣ�Դϴ�.", rp_scholnum, null);
			return;
		}
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