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
	
	String profname;
	final String rp_profname = "profname";
	
	// 1. Install Parameters (DAO�� ���� �� �ְ�)
	 profname= req.getParameter(rp_profname);
	if(OurProcResp.reqParamVoidString(profname))
	{
		//���ڿ�����
		OurProcResp.printResp(out, "�������� ������ϴ�", rp_profname, null);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		ProfessorDAO dao = new ProfessorDAO();
		switch(dao.addProfessor(profname))
		{
		case SUCCESS:
			OurProcResp.printResp(out, null, null, null);
			return;
		case INVALID_NAME:
			OurProcResp.printResp(out, "�ùٸ��� ���� �������Դϴ�.", rp_profname, null);
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