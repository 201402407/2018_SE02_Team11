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
	
	// 1. Install Parameters (DAO에 넣을 수 있게)

	try {
		reqnum = Integer.parseInt(req.getParameter(rp_reqnum));
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "요청번호를 제대로 입력해주세요.", rp_reqnum, null);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		StudentIDRequestDAO dao = new StudentIDRequestDAO();
		if(dao.rejectReqSID(reqnum))
		{
			OurProcResp.printResp(out, null, null, null);
			return;
		}
		else
		{
			OurProcResp.printResp(out, "삭제 실패.", null, null);
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
request.setCharacterEncoding("euc-kr");
makeMyResponse(request, out);
%>