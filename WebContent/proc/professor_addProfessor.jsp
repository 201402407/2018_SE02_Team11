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
	
	
	// ��� ��ü
	try {
		result = professorDAO.addProfessor(profName);	
		
		// ����
		if(result == AddProfessorResult.SUCCESS) {
			
		}
		
		// �̸��� ���ڰ� �� ���
		if(result == AddProfessorResult.INVALID_NAME) {
			
		}
		
		// DB�� null�� �� ���
		if(result == AddProfessorResult.INSERT_NULL) {
			
		}
		
	}
	catch(SQLException e) {
		e.printStackTrace();
	}
   
%>
