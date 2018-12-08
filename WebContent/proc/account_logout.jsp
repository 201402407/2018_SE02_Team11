<%@page import="java.io.IOException"%>
<%@page import="Util.*"%>
<%@page import="ClassObject.*"%>
<%@page import="DAO.*"%>
<%@ page language="java" contentType="application/json; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.SQLException" %>
<% request.setCharacterEncoding("euc-kr"); %>

<%!
private void makeMyResponse(HttpServletRequest req, JspWriter out, HttpSession session) throws IOException
{	
	session.invalidate();
	OurProcResp.printResp(out, null, null, null);
	return;
}
%>

<%
request.setCharacterEncoding("euc-kr");
makeMyResponse(request, out, session);
%>