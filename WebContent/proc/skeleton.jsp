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
	
	@@ ##;
	final String rp_## = "##";
	
	// 1. Install Parameters (DAO�� ���� �� �ְ�)
	## = req.getParameter(rp_##);
	if(OurProcResp.reqParamVoidString(##))
	{
		//���ڿ�����
		OurProcResp.printResp(out, " ������ϴ�", rp_##, null);
		return;
	}
	
	try {
		## =  req.getParameter(rp_##)
	} catch (Exception e) {
		//����ȯ����
		OurProcResp.printResp(out, " ����� �Է����ּ���.", rp_##, null);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		DAO dao = new DAO();
		
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