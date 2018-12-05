<%@page import="ClassObject.Subject"%>
<%@page import="DAO.SubjectDAO"%>
<%@page import="DAO.SubjectDAO.AddSubjectResult"%>
<%@ page import="java.util.Stack"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.*" %>
<% request.setCharacterEncoding("euc-kr"); %>

<%
   String sName = request.getParameter("subjectName");
   double score = Double.parseDouble(request.getParameter("score"));
   
   SubjectDAO subjectDAO = new SubjectDAO();

   AddSubjectResult result;
   
   try {
	   result = subjectDAO.addSubject(sName, score);
	 
	   // NOT_IN_DB
	  if(result == AddSubjectResult.NULL_IN_DB) {
		  
	  }
	   
	  // 중복이름 존재할 때
	  if(result == AddSubjectResult.COLLISION_SUBJNAME) {
		  
	  }
	  
	  // 성공
	  if(result == AddSubjectResult.SUCCESS) {
		  
	  }
   }
   catch(SQLException e) {
	   e.printStackTrace();
   }
%>