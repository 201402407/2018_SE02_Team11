<%@page import="ClassObject.Subject"%>
<%@page import="DAO.SubjectDAO"%>
<%@ page import="java.util.Stack"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.*" %>
<% request.setCharacterEncoding("euc-kr"); %>

<%
   int scode = Integer.valueOf(request.getParameter("subjectCode"));
   
   SubjectDAO subjectDAO = new SubjectDAO();

   Subject result = new Subject();
   
   try {
	   result = subjectDAO.getSubjectInfoBySCode(scode);
	 
	   // NOT_IN_DB
	  if(result == null) {
		  
	  }
	  else {
		  
	  }
   }
   catch(SQLException e) {
	   e.printStackTrace();
   }
%>