<%@page import="ClassObject.AttendanceTimeList"%>
<%@page import="java.sql.SQLException"%>
<%@page import="DAO.AttendanceDAO"%>
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
	
	List<AttendanceTimeList> arrayList = new ArrayList<AttendanceTimeList>();
	AttendanceDAO attendanceDAO = new AttendanceDAO();
	
	// ��� ��ü
	try {
		arrayList = attendanceDAO.getAttendanceTimeListBySID(sID);
		
		// �ƹ� ���� ���� ���
		if(arrayList == null) {
			
		}
		
	}
	catch(SQLException e) {
		e.printStackTrace();
	}
   
%>
