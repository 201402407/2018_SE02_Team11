<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="se02.team11.AccountDAO" %>
<%!String helloMsg; %>
<%
helloMsg = AccountDAO.test00();
%>

<!DOCTYPE html">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>�ڱ� ������Ʈ ����Ʈ �׽�Ʈ</title>
</head>
<body>
<%=helloMsg %>
</body>
</html>