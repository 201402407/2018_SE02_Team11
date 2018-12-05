<%@page import="DAO.LectureDAO"%>
<%@ page import="java.util.Stack"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.*" %>
<% request.setCharacterEncoding("euc-kr"); %>

<%
	int lcode = Integer.valueOf(request.getParameter("lectureCode"));
	
	LectureDAO lectureDAO = new LectureDAO();
   
	String result;
	
	try {
		result = lectureDAO.getSyllabusByLCode(lcode);
		
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