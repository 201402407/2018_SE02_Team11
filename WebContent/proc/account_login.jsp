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
		case SUCCESS_STUDENT: // ������ ��� (�л����� �α���)
			break;
		case SUCCESS_ADMIN: // ������ ��� (�����ڷ� �α���)
			break;
		case FAIL_STUDENT:  // �л��� ���� �й��ο��� ���� ����
			break;
		case NOT_FOUND_ID: // ���̵� ã�� �� ���� ���
			break;
		case INCORRECT_PWD: // ����� Ʋ�����
			break;
		}   
   }
   catch(SQLException e) {
	   e.printStackTrace();
   }
   
%>