<%@page import="ClassObject.TimeoffRequest"%>
<%@page import="DAO.TimeoffRequestDAO"%>
<%@ page import="java.util.Stack"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.*" %>
<% request.setCharacterEncoding("euc-kr"); %>

<%
   int sid = Integer.valueOf(request.getParameter("studentID"));
   
   TimeoffRequestDAO timeoffRequestDAO = new TimeoffRequestDAO();

   List<TimeoffRequest> result = new ArrayList<TimeoffRequest>();
   
   try {
	   result = timeoffRequestDAO.getTimeoffRequestList();
	 
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