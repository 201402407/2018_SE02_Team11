<%@page import="java.io.IOException"%>
<%@page import="Util.*"%>
<%@page import="ClassObject.*"%>
<%@page import="DAO.*"%>
<%@ page language="java" contentType="application/json; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.SQLException" %>

<%!
private void makeMyResponse(HttpServletRequest req, JspWriter out) throws IOException
{	
	
	String profname;
	final String rp_profname = "profname";
	
	// 1. Install Parameters (DAO에 넣을 수 있게)
	 profname= req.getParameter(rp_profname);
	if(OurProcResp.reqParamVoidString(profname))
	{
		//문자열공백
		OurProcResp.printResp(out, "교수명이 비었습니다", rp_profname, null);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		ProfessorDAO dao = new ProfessorDAO();
		switch(dao.addProfessor(profname))
		{
		case SUCCESS:
			OurProcResp.printResp(out, null, null, null);
			return;
		case INVALID_NAME:
			OurProcResp.printResp(out, "올바르지 못한 교수명입니다.", rp_profname, null);
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
request.setCharacterEncoding("utf-8");
makeMyResponse(request, out);
%>