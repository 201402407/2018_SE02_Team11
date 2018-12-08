<%@page import="java.sql.SQLException"%>
<%@page import="DAO.*"%>
<%@page import="ClassObject.*"%>
<%@page import="Util.*" %>
<%@page import="org.json.simple.*" %>
<%@ page language="java" contentType="application/json; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%
int result = -1;
String error = null;
DepartmentDAO dao = new DepartmentDAO();

try
{
	result = dao.getDCodeBySID(201302458);
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
%>