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
	/*
	OurProcResp.printResp(out, null, null, null);
	return;
	*/
	
	int reqNum;
	final String rp_reqNum = "reqNum";
	
	// 1. Install Parameters (DAO에 넣을 수 있게)
	try {
		reqNum = Integer.parseInt( req.getParameter(rp_reqNum) );
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "요청번호를 제대로 입력해주세요.", rp_reqNum, null);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		TimeoffRequestDAO dao = new TimeoffRequestDAO();
		if(dao.rejectTimeoffReq(reqNum))
		{
			OurProcResp.printResp(out, null, null, null);
			return;
		} else {
			OurProcResp.printResp(out, "거절명령이 제대로 이루어지지 않았습니다. 요청번호를 다시 확인해주세요.", null, null);
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