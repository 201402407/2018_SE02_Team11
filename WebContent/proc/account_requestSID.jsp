<%@page import="DAO.AccountDAO"%>
<%@ page import="java.util.Stack"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.*" %>
<% request.setCharacterEncoding("euc-kr"); %>

<%
   String id = request.getParameter("accountID");
   String newStuYear = request.getParameter("newStuYear");
   String newStuOrder = request.getParameter("newStuOrder");
   String dCode = request.getParameter("departmentCode");
   
   int pNewStuYear = Integer.valueOf(newStuYear);
   int pNewStuOrder = Integer.valueOf(newStuOrder);
   int pDCode = Integer.valueOf(dCode);
   
   AccountDAO accountDAO = new AccountDAO();
   
   try {

	   int result = accountDAO.requestSID(id, pNewStuYear, pNewStuOrder, pDCode);
	   
	   // 실패
	   if(result == -1) {
		   
	   }
	   // 성공
	   else {
		   
	   }
   }
   catch(SQLException e) {
	   e.printStackTrace();
   }
   
%>