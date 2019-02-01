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
	String scho_name;
	String rp_scho_name = "scho_name";
	
	// 1. Install Parameters
	scho_name = req.getParameter(rp_scho_name);
	if(OurProcResp.reqParamVoidString(scho_name))
	{
		OurProcResp.printResp(
				out,
				"장학이름이 비었습니다.",
				rp_scho_name,
				null
				);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		ScholarshipDAO dao = new ScholarshipDAO();
		switch( dao.addScholarship(scho_name) )
		{
		case SUCCESS:
			OurProcResp.printResp(out, null, null, null);
			return;
		case INVALID_SCNAME:
			OurProcResp.printResp(out, "올바른 장학명이 아닙니다.", rp_scho_name, null);
			return;
		}
	}
	catch(SQLException sqle)
	{
		sqle.printStackTrace();
		OurProcResp.printResp(out, "DB오류가 발생하였습니다.", null, null);
		return;
	}
}
%>

<%
// Parameter: scho_name=장학이름
// 장학에 추가가 한 줄 이루어진다.
// data 없음
request.setCharacterEncoding("utf-8");
makeMyResponse(request, out);
%>