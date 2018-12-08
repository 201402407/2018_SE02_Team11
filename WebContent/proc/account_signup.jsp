<%@page import="java.time.format.DateTimeParseException"%>
<%@page import="java.time.LocalDate"%>
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
	String id;
	final String rp_id = "id";
	String pwd;
	final String rp_pwd= "pwd";
	String name;
	final String rp_name = "name";
	LocalDate birth;
	final String rp_birth = "birth";
	
	// 1. Install Parameters (���ڿ� ����˻� + ����ȯ���� �˻�)
	id = req.getParameter(rp_id);
	if(OurProcResp.reqParamVoidString(id))
	{
		OurProcResp.printResp(out, "���̵� ������ϴ�.", rp_id, null);
		return;
	}
	pwd = req.getParameter(rp_pwd);
	if(OurProcResp.reqParamVoidString(pwd))
	{
		OurProcResp.printResp(out, "�н����尡 ������ϴ�.", rp_pwd, null);
		return;
	}
	name = req.getParameter(rp_name);
	if(OurProcResp.reqParamVoidString(name))
	{
		OurProcResp.printResp(out, "�̸��� ������ϴ�.", rp_name, null);
		return;
	}
	name = req.getParameter(rp_name);
	if(OurProcResp.reqParamVoidString(name))
	{
		OurProcResp.printResp(out, "�̸��� ������ϴ�.", rp_name, null);
		return;
	}
	try {
		birth = LocalDate.parse( req.getParameter(rp_birth) );
	} catch (Exception e) {
		OurProcResp.printResp(out, "������ ����� �Է����ּ���.", rp_birth, null);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		AccountDAO dao = new AccountDAO();
		switch( dao.signUp(id, pwd, name, birth) )
		{
		case SUCCESS:
			OurProcResp.printResp(out, null, null, null);
			return;
		case INVALID_ID:
			OurProcResp.printResp(out, "���̵� �ùٸ��� �ʽ��ϴ�.", rp_id, null);
			return;
		case INVALID_PWD:
			OurProcResp.printResp(out, "�н����尡 �ùٸ��� �ʽ��ϴ�.", rp_pwd, null);
			return;
		case INVALID_NAME:
			OurProcResp.printResp(out, "�̸��� �ùٸ��� �ʽ��ϴ�.", rp_name, null);
			return;
		case INVALID_BIRTH:
			OurProcResp.printResp(out, "������ �ùٸ��� �ʽ��ϴ�.", rp_birth, null);
			return;
		}
	}
	catch(SQLException sqle)
	{
		sqle.printStackTrace();
		OurProcResp.printResp(out, "DB������ �߻��Ͽ����ϴ�. Ȥ�� ���̵� �ߺ��� �� �ֽ��ϴ�.", null, null);
		return;
	}
}
%>

<%
// Parameter: id, pwd, name, birth
// ���� ���̺� �ϳ� �߰��ȴ�. �й���û ���̺� �ϳ� �߰��ȴ�.
// data ����
request.setCharacterEncoding("euc-kr");
makeMyResponse(request, out);
%>