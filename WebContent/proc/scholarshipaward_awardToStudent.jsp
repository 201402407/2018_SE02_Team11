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
		case SUCCESS: // ������ ���
			break;
		case NULL_IN_DB: // DB�� ����� �����Ͱ� �ϳ��� ���� ���
			break;
		case NOT_FOUND_SCHOLAR: // ������ ã�� �� ���� ���
			break;
		case NOT_FOUND_STUDENT: // �л��� ��ϵǾ� ���� ���� ���
			break;
			default:
				break;
		}   
   }
   catch(SQLException e) {
	   e.printStackTrace();
   }
%>