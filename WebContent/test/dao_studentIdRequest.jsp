<%@page import="java.time.LocalDate"%>
<%@page import="java.sql.SQLException"%>
<%@page import="DAO.*"%>
<%@page import="ClassObject.*"%>
<%@page import="Util.*" %>
<%@page import="org.json.simple.*" %>
<%@ page language="java" contentType="application/json; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%
Object result = null;
String error = null;
StudentIDRequestDAO dao = new StudentIDRequestDAO();

try
{
	
}
catch(SQLException sqle)
{
	error = "sorry";
	sqle.printStackTrace();
}

JSONObject procResponse = new JSONObject();
procResponse.put("error", error);
procResponse.put("result", result);
out.println(procResponse.toJSONString());

// error, obj
%>