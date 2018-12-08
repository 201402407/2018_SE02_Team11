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
	int reqnum;
	final String rp_reqnum = "reqnum";
	
	// 1. Install Parameters (DAO�� ���� �� �ְ�)

	try {
		reqnum = Integer.parseInt(req.getParameter(rp_reqnum));
	} catch (Exception e) {
		//����ȯ����
		OurProcResp.printResp(out, "��û��ȣ�� ����� �Է����ּ���.", rp_reqnum, null);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		StudentIDRequestDAO dao = new StudentIDRequestDAO();
		if(dao.rejectReqSID(reqnum))
		{
			OurProcResp.printResp(out, null, null, null);
			return;
		}
		else
		{
			OurProcResp.printResp(out, "���� ����.", null, null);
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