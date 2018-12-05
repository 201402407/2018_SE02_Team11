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
   int dcode = Integer.valueOf(request.getParameter("departmentCode"));
   
   SubjectDAO subjectDAO = new SubjectDAO();

   List<Subject> result = new ArrayList<>();
   
   try {
	   result = subjectDAO.getThisSemesterSubjectByDCode(dcode);
	 
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