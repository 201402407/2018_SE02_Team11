<%@page import="DAO.AccountDAO.signUpResult"%>
<%@page import="DAO.AccountDAO"%>
<%@ page import="java.util.Stack"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.*" %>
<% request.setCharacterEncoding("euc-kr"); %>

<%
   String id = request.getParameter("accountID");
   String passwd = request.getParameter("pwd");
   String name = request.getParameter("accountName");
   int birth = Integer.valueOf(request.getParameter("birth")); // Ȯ��!
   
   AccountDAO accountDAO = new AccountDAO();
   signUpResult result;
   try {

	   result = accountDAO.signUp(id, passwd, name, birth);
	   
	   // ����
	   if(result == signUpResult.SUCCESS) {
		   
	   }
	   
	   // ��ȿ���� ���� ����
	   if(result == signUpResult.INVALID_FORM) {
		   
	   }
	   
	   // DB�ȿ� ����� �����Ͱ� �ϳ��� ���� ���
	   if(result == signUpResult.NULL_IN_DB) {
		   
	   }
   }
   catch(SQLException e) {
	   e.printStackTrace();
   }
%>