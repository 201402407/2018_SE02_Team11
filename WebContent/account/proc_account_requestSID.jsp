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
   /*
   result = accountDAO.requestSID(id, p_newStuYear, p_newStuOrder, p_dcode);
   
   // ����
   if(result == signUpResult.SUCCESS) {
	   
   }
   
   // ��ȿ���� ���� ����
   if(result == signUpResult.INVALID_FORM) {
	   
   }
   
   // �ϳ��� �Է� X
   if(result == signUpResult.MISSING_FIELD) {
	   
   }
   */
%>