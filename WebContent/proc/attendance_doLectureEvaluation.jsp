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
	int attNum;
	final String rp_attNum = "attNum";
	String evaltext;
	final String rp_evaltext = "evaltext";
	
	// 1. Install Parameters (DAO�� ���� �� �ְ�)
	try {
		attNum = Integer.parseInt( req.getParameter(rp_attNum) );
	} catch (Exception e) {
		//����ȯ����
		OurProcResp.printResp(out, "������ȣ�� ����� �Է����ּ���.", rp_attNum, null);
		return;
	}
	evaltext = req.getParameter(rp_evaltext);
	if(OurProcResp.reqParamVoidString(evaltext))
	{
		//���ڿ�����
		OurProcResp.printResp(out, "������ �ؽ�Ʈ�� ������ϴ�", rp_evaltext, null);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		AttendanceDAO dao = new AttendanceDAO();
		if( dao.doLectureEvaluation(attNum, evaltext) )
		{
			OurProcResp.printResp(out, null, null, null);
			return;
		}
		else
		{
			OurProcResp.printResp(out, "�ۼ��� �����߽��ϴ�. ������ ���� ���� �����ߴ��� Ȯ���� �ּ���.", null, null);
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
request.setCharacterEncoding("utf-8");
makeMyResponse(request, out);
%>