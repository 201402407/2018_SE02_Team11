<%@page import="DAO.AccountDAO.loginResult"%>
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
   
   AccountDAO accountDAO = new AccountDAO();
   
   loginResult result;
   try {
	   result = accountDAO.login(id, passwd, session);
	   
		switch(result) {
		case SUCCESS_STUDENT: // 성공한 경우 (학생으로 로그인)
			break;
		case SUCCESS_ADMIN: // 성공한 경우 (관리자로 로그인)
			break;
		case FAIL_STUDENT:  // 학생이 아직 학번부여를 받지 못함
			break;
		case NOT_FOUND_ID: // 아이디를 찾을 수 없는 경우
			break;
		case INCORRECT_PWD: // 비번이 틀린경우
			break;
		}   
   }
   catch(SQLException e) {
	   e.printStackTrace();
   }
   
%>