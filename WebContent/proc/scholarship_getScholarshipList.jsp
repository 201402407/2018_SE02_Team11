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
	// 1. Install Parameters (DAO�� ���� �� �ְ�)
	// Nope.
	
	// 2. do DAO Job
	try
	{
		ScholarshipDAO dao = new ScholarshipDAO();
		List<ScholarShip> list = dao.getScholarshipList();
				
		JSONArray listJson = new JSONArray();
		for(ScholarShip elem : list)
		{
			JSONObject elemJson = new JSONObject();
			elemJson.put("schoNum", elem.getScholarshipNum());
			elemJson.put("schoName", elem.getScholarshipName());
			
			listJson.add(elemJson);
		}
		OurProcResp.printResp(out, null, null, listJson);
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