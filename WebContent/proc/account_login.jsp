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
		case SUCCESS: // ������ ���
			break;
		case NULL_IN_DB: // DB�� ����� �����Ͱ� �ϳ��� ���� ���
			break;
		case NOT_FOUND_ID: // ���̵� ã�� �� ���� ���
			break;
		case INCORRECT_PWD: // ����� Ʋ�����
			break;
			default:
				break;
		}   
   }
   catch(SQLException e) {
	   e.printStackTrace();
   }
   
%>