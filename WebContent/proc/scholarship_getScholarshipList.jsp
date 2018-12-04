<%@page import="ClassObject.ScholarShip"%>
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
   List<ScholarShip> result = new ArrayList<>();
   try {
	   result = scholarshipDAO.getScholarshipList();
	   
	   // NULL_IN_DB
	   if(result.isEmpty()) {
		   
	   }
	   
	   // ¼º°ø
	   else {
		   
	   }
   }
   catch(SQLException e) {
	   e.printStackTrace();
   }
%>