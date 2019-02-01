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
	
	// 1. Install Parameters (DAO에 넣을 수 있게)
	try {
		lcode = Integer.parseInt( req.getParameter(rp_lcode) );
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "분반코드를 제대로 입력해주세요.", rp_lcode, null);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		LectureDAO dao = new LectureDAO();
		String syltext = dao.getSyllabusByLCode(lcode);
		
		if(syltext == null || syltext.isEmpty())
		{
			OurProcResp.printResp(out, "해당 과목은 강의계획서가 없습니다", null, null);
			return;
		}

		OurProcResp.printResp(out, null, null, syltext);
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