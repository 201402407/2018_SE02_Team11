<%@page import="DAO.ScholarshipDAO.addScholarshipResult"%>
<%@page import="DAO.ScholarshipDAO"%>
<%@ page import="java.util.Stack"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.*" %>
<% request.setCharacterEncoding("euc-kr"); %>

<%
   String sName = request.getParameter("scholarshipName");
   
   ScholarshipDAO scholarshipDAO = new ScholarshipDAO();
   addScholarshipResult result;
   try {
	   result = scholarshipDAO.addScholarship(sName);
	   
	   
	   if(result == addScholarshipResult.NULL_IN_DB) {
		   
	   }
	   // ¼º°ø
	   else {
		   
	   }
   }
   catch(SQLException e) {
	   e.printStackTrace();
   }
%>