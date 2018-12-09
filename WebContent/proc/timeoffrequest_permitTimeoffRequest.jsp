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
	/*
	OurProcResp.printResp(out, null, null, null);
	return;
	*/
	
	int reqNum;
	final String rp_reqNum = "reqNum";
	
	// 1. Install Parameters (DAO�� ���� �� �ְ�)
	try {
		reqNum = Integer.parseInt( req.getParameter(rp_reqNum) );
	} catch (Exception e) {
		//����ȯ����
		OurProcResp.printResp(out, "��û��ȣ�� ����� �Է����ּ���.", rp_reqNum, null);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		TimeoffRequestDAO dao = new TimeoffRequestDAO();
		if(dao.permitTimeoffRequest(reqNum))
		{
			OurProcResp.printResp(out, null, null, null);
			return;
		} else
		{
			OurProcResp.printResp(out, "��û ����� ��ְ� �־����ϴ�. Ȥ�� ���� ��ȣ�Դϱ�?", null, null);
			return;
		}
	}
	catch(Exception sqle)
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