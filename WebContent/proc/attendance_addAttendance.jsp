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
	int lcode;
	final String rp_lcode = "lcode";
	int sid;
	final String rp_sid = "sid";
	
	// 1. Install Parameters (DAO�� ���� �� �ְ�)
	try {
		lcode = Integer.parseInt( req.getParameter(rp_lcode) );
	} catch (Exception e) {
		//����ȯ����
		OurProcResp.printResp(out, "�й��ڵ带 ����� �Է����ּ���.", rp_lcode, null);
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
		AttendanceDAO dao = new AttendanceDAO();
		switch(dao.addAttendance(lcode, sid))
		{
		case SUCCESS:
			OurProcResp.printResp(out, null, null, null);
			return;
		case NO_SUCH_STUDENT:
			OurProcResp.printResp(out, "�������� �ʴ� �й��Դϴ�.", null, null);
			return;
		case UNABLE_ADD_ATTENDANCE:
			OurProcResp.printResp(out, "������û �Ұ��Ⱓ�̰ų� �������̽ʴϴ�.", null, null);
			return;
		case NOT_ENOUGH_NUM:
			OurProcResp.printResp(out, "�� �̻� ������ �����ϴ�. �˼��մϴ�.", null, null);
			return;
		case NOT_ENOUGH_SCORE:
			OurProcResp.printResp(out, "���� �������� ���� �� �̻��� ������ �Ұ��մϴ�.", null, null);
			return;
		case COLLISION_TIMETABLE:
			OurProcResp.printResp(out, "�ð�ǥ �浹!", null, null);
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