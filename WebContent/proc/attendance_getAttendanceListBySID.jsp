<%@page import="ClassObject.AttendanceListBySID"%>
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
	
	String sid = request.getParameter("studentID");
	int sID = Integer.valueOf(sid);
	
	List<AttendanceListBySID> arrayList = new ArrayList<AttendanceListBySID>();
	AttendanceDAO attendanceDAO = new AttendanceDAO();
	
	// ��� ��ü
	try {
		arrayList = attendanceDAO.getAttendanceListBySID(sID);	
		// �ƹ� ���� ���� ���
		if(arrayList == null) {
			
		}
		
	}
	catch(SQLException e) {
		e.printStackTrace();
	}
   
%>
