<%@page import="DAO.LectureDAO"%>
<%@page import="ClassObject.LectureDetail"%>
<%@ page import="java.util.Stack"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.*" %>
<% request.setCharacterEncoding("euc-kr"); %>

<%
	int sid = Integer.valueOf(request.getParameter("studentID"));
	String sName = request.getParameter("subjectName");
	
	LectureDAO lectureDAO = new LectureDAO();
   
	List<LectureDetail> result;
	
	try {
		result = lectureDAO.getLectureByName(sid, sName);
		
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