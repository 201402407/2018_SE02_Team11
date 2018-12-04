<%@page import="DAO.ProfessorDAO.AddProfessorResult"%>
<%@page import="java.sql.SQLException"%>
<%@page import="DAO.ProfessorDAO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page import="java.util.Stack"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.Date" %>
<% request.setCharacterEncoding("euc-kr"); %>

<%
	String profName = request.getParameter("professorName");
	
	ProfessorDAO professorDAO = new ProfessorDAO();
	AddProfessorResult result;
	
	
	// 목록 객체
	try {
		result = professorDAO.addProfessor(profName);	
		
		// 성공
		if(result == AddProfessorResult.SUCCESS) {
			
		}
		
		// 이름에 숫자가 들어간 경우
		if(result == AddProfessorResult.INVALID_NAME) {
			
		}
		
		// DB에 null이 들어간 경우
		if(result == AddProfessorResult.INSERT_NULL) {
			
		}
		
	}
	catch(SQLException e) {
		e.printStackTrace();
	}
   
%>
