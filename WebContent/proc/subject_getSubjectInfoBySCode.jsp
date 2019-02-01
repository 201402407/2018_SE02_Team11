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
	
	// 1. Install Parameters (DAO에 넣을 수 있게)
	try {
		scode = Integer.parseInt( req.getParameter(rp_scode) );
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "과목코드를 제대로 입력해주세요.", rp_scode, null);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		SubjectDAO dao = new SubjectDAO();
		Subject subj = dao.getSubjectInfoBySCode(scode);
		
		if(subj==null)
		{
			OurProcResp.printResp(out, "없는 과목코드입니다.", null, null);
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
		OurProcResp.printResp(out, "DB오류가 발생하였습니다.", null, null);
		return;
	}
}
%>

<%
request.setCharacterEncoding("euc-kr");
makeMyResponse(request, out);
%>