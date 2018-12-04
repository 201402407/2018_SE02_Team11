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
   int changetype = Integer.valueOf(request.getParameter("changeType"));
   ChangeType changeType = ChangeType.gotChangeType(changetype);
   int start = Integer.valueOf(request.getParameter("startSemester"));
   int end = Integer.valueOf(request.getParameter("endSemester"));
   String reason = request.getParameter("reason");
   int sid = Integer.valueOf(request.getParameter("studentID"));
   
   ChangeRecordDAO changeRecordDAO = new ChangeRecordDAO();
   RequestTimeOnOffsResult result;
   
   try {
	   result = changeRecordDAO.requestTimeOnOffs(changeType, start, end, reason, sid);
	 
	  if(result == RequestTimeOnOffsResult.CURRENTLY_UNAVAILABLE) {
		  
	  }
	  
	  if(result == RequestTimeOnOffsResult.NULL_IN_DB) {
		  
	  }
	  
	  if(result == RequestTimeOnOffsResult.SUCCESS) {
		  
	  }
   }
   catch(SQLException e) {
	   e.printStackTrace();
   }
%>