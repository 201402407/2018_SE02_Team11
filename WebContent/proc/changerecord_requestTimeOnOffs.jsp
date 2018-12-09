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
	ChangeType changeType;
	final String rp_changeType = "changeType";
	int startSemester;
	final String rp_startSemester = "startSemester";
	int endSemester;
	final String rp_endSemester = "endSemester";
	String reason;
	final String rp_reason = "reason";
	int sid;
	final String rp_sid = "sid";
	
	// 1. Install Parameters (DAO�� ���� �� �ְ�)
	try {
		changeType = OurProcResp.getValidChangeType(
				req.getParameter(rp_changeType)
				);
	} catch (Exception e) {
		//����ȯ����
		OurProcResp.printResp(out, "�޺��б����� ����� �Է����ּ���.", rp_changeType, null);
		return;
	}
	
	try {
		startSemester = OurProcResp.getValidSemester(req.getParameter(rp_startSemester));
	} catch (Exception e) {
		//����ȯ����
		OurProcResp.printResp(out, "�����б⸦ ����� �Է����ּ���.", rp_startSemester, null);
		return;
	}
	
	try {
		endSemester = OurProcResp.getValidSemester( req.getParameter(rp_endSemester) );
	} catch (Exception e) {
		//����ȯ����
		OurProcResp.printResp(out, "�����б⸦ ����� �Է����ּ���.", rp_endSemester, null);
		return;
	}
	
	reason = req.getParameter(rp_reason);
	if(OurProcResp.reqParamVoidString(reason))
	{
		//���ڿ�����
		OurProcResp.printResp(out, "���������� ������ ������ϴ�", rp_reason, null);
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
		ChangeRecordDAO dao = new ChangeRecordDAO();
		switch(dao.requestTimeOnOffs(changeType, startSemester, endSemester, reason, sid))
		{
		case SUCCESS:
			OurProcResp.printResp(out, null, null, null);
			return;
		case CURRENTLY_UNAVAILABLE:
			OurProcResp.printResp(out, "�б��߿��� �޺��н�û�� �Ұ��մϴ�.", null, null);
			return;
		case INVALID_SEMESTER:
			OurProcResp.printResp(out, "�����б�� �����б⸦ �ٽ� Ȯ�����ּ���.", null, null);
			return;
		case RESUME_ON_TIMEON:
			OurProcResp.printResp(out, "�����߿��� ���н�û�� �Ұ��մϴ�.", null, null);
			return;
		case TAKEOFF_ON_TIMEOFF:
			OurProcResp.printResp(out, "�����߿��� ���н�û�� �Ұ��մϴ�.", null, null);
			return;
		}
	}
	catch(SQLException sqle)
	{
		sqle.printStackTrace();
		OurProcResp.printResp(out, "DB������ �߻��Ͽ����ϴ�.", null, null);
		return;
	}
}
%>

<%
request.setCharacterEncoding("euc-kr");
makeMyResponse(request, out);
%>