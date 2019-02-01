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
	
	// 1. Install Parameters (DAO에 넣을 수 있게)
	try {
		dcode = Integer.parseInt( req.getParameter(rp_dcode) );
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "학과코드를 제대로 입력해주세요.", rp_dcode, null);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		SubjectDAO dao = new SubjectDAO();
		List<Subject> list = dao.getThisSemesterSubjectByDCode(dcode);
				
		if(list == null)
		{
			OurProcResp.printResp(out, "현재는 조회가능 기간이 아닙니다.", null, null);
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
		OurProcResp.printResp(out, "DB오류가 발생하였습니다.", null, null);
		return;
	}
}
%>

<%
request.setCharacterEncoding("euc-kr");
makeMyResponse(request, out);
%>