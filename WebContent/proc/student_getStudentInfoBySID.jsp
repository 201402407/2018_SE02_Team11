<%@page import="java.sql.SQLException"%>
<%@page import="DAO.StudentDAO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="ClassObject.StudentInfo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Stack"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.Date" %>
<% request.setCharacterEncoding("euc-kr"); %>

<%
	
	String sid = request.getParameter("studentID");
	int sID = Integer.valueOf(sid);
	
	List<StudentInfo> arrayList = new ArrayList<StudentInfo>();
	StudentDAO studentDAO = new StudentDAO();
	
	// 목록 객체
	try {
		arrayList = studentDAO.getStudentInfoBySID(sID);	
		// 아무 값도 없는 경우
		if(arrayList == null) {
			
		}
		
	}
	catch(SQLException e) {
		e.printStackTrace();
	}
   
%>
