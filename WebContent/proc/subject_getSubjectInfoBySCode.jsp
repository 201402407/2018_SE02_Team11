<%@page import="org.json.simple.JSONObject"%>
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
	int scode;
	final String rp_scode = "scode";
	
	// 1. Install Parameters (DAO�� ���� �� �ְ�)
	try {
		scode = Integer.parseInt( req.getParameter(rp_scode) );
	} catch (Exception e) {
		//����ȯ����
		OurProcResp.printResp(out, "�����ڵ带 ����� �Է����ּ���.", rp_scode, null);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		SubjectDAO dao = new SubjectDAO();
		Subject subj = dao.getSubjectInfoBySCode(scode);
		
		if(subj==null)
		{
			OurProcResp.printResp(out, "���� �����ڵ��Դϴ�.", null, null);
			return;
		}
		else
		{
			JSONObject subjJson = new JSONObject();
			subjJson.put("subjectCode", subj.getSubjectCode());
			subjJson.put("subjectName", subj.getSubjectName());
			subjJson.put("score", subj.getScore());
			
			OurProcResp.printResp(out, null, null, subjJson);
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