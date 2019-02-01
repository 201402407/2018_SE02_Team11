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
	int sid;
	final String rp_sid = "sid";
	
	// 1. Install Parameters (DAO�� ���� �� �ְ�)
	try {
		sid = Integer.parseInt( req.getParameter(rp_sid) );
	} catch (Exception e) {
		//����ȯ����
		OurProcResp.printResp(out, "�й��� ����� �Է����ּ���.", rp_sid, null);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		// dcode�� ���Ѵ�.
		DepartmentDAO depDao = new DepartmentDAO();
		int dcode = depDao.getDCodeBySID(sid);
		if(dcode == -1)
		{
			OurProcResp.printResp(out, "���� �й��Դϴ�.", rp_sid, null);
			return;
		}
		
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