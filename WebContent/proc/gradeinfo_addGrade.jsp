<%@page import="ClassObject.AttendanceListByLCode"%>
<%@page import="java.sql.SQLException"%>
<%@page import="DAO.AttendanceDAO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page import="java.util.Stack"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.Date" %>
<% request.setCharacterEncoding("euc-kr"); %>

<%
	String attendancenum = request.getParameter("attendanceNum");
	String grade = request.getParameter("grade");
	int LCode = Integer.valueOf(attendancenum);
	double Grade = Integer.valueOf(grade);
	
	List<AttendanceListByLCode> arrayList = new ArrayList<AttendanceListByLCode>();
	AttendanceDAO attendanceDAO = new AttendanceDAO();
	
	// 목록 객체
	try {
		arrayList = attendanceDAO.getAttendanceListbyLCode(LCode);	
		// 아무 값도 없는 경우
		if(arrayList == null) {
			
		}
		
	}
	catch(SQLException e) {
		e.printStackTrace();
	}
   
%>
