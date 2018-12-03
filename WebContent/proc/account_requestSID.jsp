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
   /*
   result = accountDAO.requestSID(id, p_newStuYear, p_newStuOrder, p_dcode);
   
   // 성공
   if(result == signUpResult.SUCCESS) {
	   
   }
   
   // 유효하지 않은 형식
   if(result == signUpResult.INVALID_FORM) {
	   
   }
   
   // 하나라도 입력 X
   if(result == signUpResult.MISSING_FIELD) {
	   
   }
   */
%>