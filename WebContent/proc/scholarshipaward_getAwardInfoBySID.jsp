<%@page import="ClassObject.AwardInfoBySID"%>
<%@page import="DAO.ScholarshipAwardDAO"%>
<%@ page import="java.util.Stack"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.*" %>
<% request.setCharacterEncoding("euc-kr"); %>

<%
   int scNum = Integer.valueOf(request.getParameter("scholarshipName"));
   int money = Integer.valueOf(request.getParameter("awardMoney"));
   int sid = Integer.valueOf(request.getParameter("studentID"));
   
   ScholarshipAwardDAO scholarshipAwardDAO = new ScholarshipAwardDAO();
   List<AwardInfoBySID> result = new ArrayList<>();
   try {
	   result = scholarshipAwardDAO.getAwardInfoBySID(p_awardname, p_money, p_sid);
	   
		
   }
   catch(SQLException e) {
	   e.printStackTrace();
   }
%>