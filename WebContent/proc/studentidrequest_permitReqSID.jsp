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
	int reqnum;
	final String rp_reqnum = "reqnum";
	int dcode;
	final String rp_dcode = "dcode";
	
	// 1. Install Parameters (문자열 공백검사 + 형변환실패 검사)
	try {
		reqnum = Integer.parseInt( req.getParameter(rp_reqnum) );
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "요청번호를 제대로 입력해주세요.", rp_reqnum, null);
		return;
	}
	
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
		StudentIDRequestDAO dao = new StudentIDRequestDAO();
		if ( dao.permitReqSID(reqnum, dcode) == false )
		{
			OurProcResp.printResp(out, "그런 요청번호는 없습니다.", rp_reqnum, null);
			return;
		}
		else
		{
			OurProcResp.printResp(out, null, null, null);
			return;
		}
		
	}
	catch(SQLException sqle)
	{
		sqle.printStackTrace();
		OurProcResp.printResp(out, "DB오류가 발생하였습니다. 혹시 잘못된 학과번호를 입력하셨습니까?", null, null);
		return;
	}
}
%>

<%
request.setCharacterEncoding("euc-kr");
makeMyResponse(request, out);
%>