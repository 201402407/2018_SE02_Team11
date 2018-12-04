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
	   result = accountDAO.login(id, passwd);
	   
		switch(result) {
		case SUCCESS: // 성공한 경우
			break;
		case NULL_IN_DB: // DB에 저장된 데이터가 하나도 없는 경우
			break;
		case NOT_FOUND_ID: // 아이디를 찾을 수 없는 경우
			break;
		case INCORRECT_PWD: // 비번이 틀린경우
			break;
			default:
				break;
		}   
   }
   catch(SQLException e) {
	   e.printStackTrace();
   }
   
%>