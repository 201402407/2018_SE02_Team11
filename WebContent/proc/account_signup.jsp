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
   int birth = Integer.valueOf(request.getParameter("birth")); // 확인!
   
   AccountDAO accountDAO = new AccountDAO();
   signUpResult result;
   try {

	   result = accountDAO.signUp(id, passwd, name, birth);
	   
	   // 성공
	   if(result == signUpResult.SUCCESS) {
		   
	   }
	   
	   // 유효하지 않은 형식
	   if(result == signUpResult.INVALID_FORM) {
		   
	   }
	   
	   // DB안에 저장된 데이터가 하나도 없는 경우
	   if(result == signUpResult.NULL_IN_DB) {
		   
	   }
   }
   catch(SQLException e) {
	   e.printStackTrace();
   }
%>