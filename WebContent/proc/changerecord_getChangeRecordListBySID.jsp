<%@page import="ClassObject.ChangeRecord"%>
<%@page import="ClassObject.ChangeType"%>
<%@page import="DAO.ChangeRecordDAO"%>
<%@page import="DAO.ChangeRecordDAO.RequestTimeOnOffsResult"%>
<%@ page import="java.util.Stack"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.*" %>
<% request.setCharacterEncoding("euc-kr"); %>

<%
   int sid = Integer.valueOf(request.getParameter("studentID"));
   
   ChangeRecordDAO changeRecordDAO = new ChangeRecordDAO();

   List<ChangeRecord> result = new ArrayList<>();
   
   try {
	   result = changeRecordDAO.getChangeRecordListBySID(sid);
	 
	   // NOT_IN_DB
	  if(result.isEmpty()) {
		  
	  }
	  else {
		  
	  }
   }
   catch(SQLException e) {
	   e.printStackTrace();
   }
%>