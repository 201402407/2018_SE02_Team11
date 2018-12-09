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
	
	// 1. Install Parameters (DAO�� ���� �� �ְ�)
	try {
		attendancenum = Integer.parseInt( req.getParameter(rp_attendancenum) );
	} catch (Exception e) {
		//����ȯ����
		OurProcResp.printResp(out, "������ȣ�� ����� �Է����ּ���.", rp_attendancenum, null);
		return;
	}
	
	try {
		grade = Double.parseDouble( req.getParameter(rp_grade) );
	} catch (Exception e) {
		//����ȯ����
		OurProcResp.printResp(out, "������ ����� �Է����ּ���.", rp_grade, null);
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
			OurProcResp.printResp(out, "���� �Է��� ����� �̷������ ���� �� �մϴ�.", null, null);
			return;
		}
	}
	catch(Exception e)
	{
		e.printStackTrace();
		OurProcResp.printResp(out, "DB������ �߻��Ͽ����ϴ�. ������ȣ�� �ùٸ��� Ȯ�����ּ���.", null, null);
		return;
	}
}
%>

<%
request.setCharacterEncoding("euc-kr");
makeMyResponse(request, out);
%>