<%@page import="DAO.ScholarshipAwardDAO.AwardToStudentResult"%>
<%@page import="DAO.ScholarshipAwardDAO"%>
<%@ page import="java.util.Stack"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.*" %>
<% request.setCharacterEncoding("euc-kr"); %>

<%
   int scNum = Integer.valueOf(request.getParameter("scholarshipNum"));
   int money = Integer.valueOf(request.getParameter("awardMoney"));
   int sid = Integer.valueOf(request.getParameter("studentID"));
   
   ScholarshipAwardDAO scholarshipAwardDAO = new ScholarshipAwardDAO();
   AwardToStudentResult result;
   try {
	   result = scholarshipAwardDAO.awardToStudent(scNum, money, sid);
	   
		switch(result) {
		case SUCCESS: // 성공한 경우
			break;
		case NULL_IN_DB: // DB에 저장된 데이터가 하나도 없는 경우
			break;
		case NOT_FOUND_SCHOLAR: // 장학을 찾을 수 없는 경우
			break;
		case NOT_FOUND_STUDENT: // 학생이 등록되어 있지 않은 경우
			break;
			default:
				break;
		}   
   }
   catch(SQLException e) {
	   e.printStackTrace();
   }
%>