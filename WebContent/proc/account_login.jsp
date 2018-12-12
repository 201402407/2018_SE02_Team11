<%@page import="java.io.IOException"%>
<%@page import="Util.*"%>
<%@page import="ClassObject.*"%>
<%@page import="DAO.*"%>
<%@ page language="java" contentType="application/json; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.SQLException" %>

<%!
private void makeMyResponse(HttpServletRequest req, JspWriter out, HttpSession session) throws IOException
{	
	//�̹� �α��� ���¸� ����Ұ�
	if(session.getAttribute("accountID") != null)
	{
		OurProcResp.printResp(out, "�̹� �α��εǾ� �ֽ��ϴ�.", null, null);
		return;
	}
	
	String id;
	final String rp_id = "id";
	String pwd;
	final String rp_pwd = "pwd";
	
	// 1. Install Parameters (���ڿ� ����˻� + ����ȯ���� �˻�)
	id = req.getParameter(rp_id);
	if(OurProcResp.reqParamVoidString(id))
	{
		//���ڿ�����
		OurProcResp.printResp(out, "���̵� ������ϴ�" + id, rp_id, null);
		return;
	}
	pwd = req.getParameter(rp_pwd);
	if(OurProcResp.reqParamVoidString(pwd))
	{
		//���ڿ�����
		OurProcResp.printResp(out, "�н����尡 ������ϴ�", rp_pwd, null);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		AccountDAO dao = new AccountDAO();
		switch (dao.login(id, pwd, session) )
		{
		case SUCCESS_ADMIN:
			OurProcResp.printResp(out, null, null, null);
			return;
		case SUCCESS_STUDENT:
			OurProcResp.printResp(out, null, null, null);
			return;
		case FAIL_STUDENT:
			OurProcResp.printResp(out, "���� �й��ο��� ���� ���߽��ϴ�.", null, null);
			return;
		case INCORRECT_PWD:
			OurProcResp.printResp(out, "�н����尡 ���� �ʽ��ϴ�.", rp_pwd, null);
			return;
		case NOT_FOUND_ID:
			OurProcResp.printResp(out, "���� ���̵��Դϴ�.", rp_id, null);
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
request.setCharacterEncoding("utf-8");
makeMyResponse(request, out, session);
%>