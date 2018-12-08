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
	int dcode;
	final String rp_dcode = "dcode";
	
	// 1. Install Parameters (���ڿ� ����˻� + ����ȯ���� �˻�)
	try {
		reqnum = Integer.parseInt( req.getParameter(rp_reqnum) );
	} catch (Exception e) {
		//����ȯ����
		OurProcResp.printResp(out, "��û��ȣ�� ����� �Է����ּ���.", rp_reqnum, null);
		return;
	}
	
	try {
		dcode = Integer.parseInt( req.getParameter(rp_dcode) );
	} catch (Exception e) {
		//����ȯ����
		OurProcResp.printResp(out, "�а��ڵ带 ����� �Է����ּ���.", rp_dcode, null);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		StudentIDRequestDAO dao = new StudentIDRequestDAO();
		if ( dao.permitReqSID(reqnum, dcode) == false )
		{
			OurProcResp.printResp(out, "�׷� ��û��ȣ�� �����ϴ�.", rp_reqnum, null);
			return;
		}
		else
		{
			OurProcResp.printResp(out, null, null, null);
			return;
		}
		
	}
	catch(SQLException sqle)
	{
		sqle.printStackTrace();
		OurProcResp.printResp(out, "DB������ �߻��Ͽ����ϴ�. Ȥ�� �߸��� �а���ȣ�� �Է��ϼ̽��ϱ�?", null, null);
		return;
	}
}
%>

<%
request.setCharacterEncoding("euc-kr");
makeMyResponse(request, out);
%>