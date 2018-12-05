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
   int reqNum = Integer.valueOf(request.getParameter("reqNum"));
   
   TimeoffRequestDAO timeoffRequestDAO = new TimeoffRequestDAO();
   
   try {
	   boolean result = timeoffRequestDAO.permitTimeoffRequest(reqNum);
	  
	   // ¼º°ø
	  if(result) {
		   
	  }
	   
	  else {
		  
	  }
   }
   catch(SQLException e) {
	   e.printStackTrace();
   }
%>