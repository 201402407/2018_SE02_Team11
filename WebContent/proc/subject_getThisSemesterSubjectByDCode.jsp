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
	int dcode;
	final String rp_dcode = "dcode";
	
	// 1. Install Parameters (DAO�� ���� �� �ְ�)
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
		SubjectDAO dao = new SubjectDAO();
		List<Subject> list = dao.getThisSemesterSubjectByDCode(dcode);
				
		if(list == null)
		{
			OurProcResp.printResp(out, "����� ��ȸ���� �Ⱓ�� �ƴմϴ�.", null, null);
			return;
		}
		
		JSONArray listJson = new JSONArray();
		for(Subject elem : list)
		{
			JSONObject elemJson = new JSONObject();
			elemJson.put("subjectCode", elem.getSubjectCode());
			elemJson.put("subjectName", elem.getSubjectName());
			
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