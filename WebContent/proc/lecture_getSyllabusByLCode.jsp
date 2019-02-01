<%@page import="java.util.List"%>
<%@page import="org.json.simple.JSONArray"%>
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
	int lcode;
	final String rp_lcode = "lcode";
	
	// 1. Install Parameters (DAO�� ���� �� �ְ�)
	try {
		lcode = Integer.parseInt( req.getParameter(rp_lcode) );
	} catch (Exception e) {
		//����ȯ����
		OurProcResp.printResp(out, "�й��ڵ带 ����� �Է����ּ���.", rp_lcode, null);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		LectureDAO dao = new LectureDAO();
		String syltext = dao.getSyllabusByLCode(lcode);
		
		if(syltext == null || syltext.isEmpty())
		{
			OurProcResp.printResp(out, "�ش� ������ ���ǰ�ȹ���� �����ϴ�", null, null);
			return;
		}

		OurProcResp.printResp(out, null, null, syltext);
		return;
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