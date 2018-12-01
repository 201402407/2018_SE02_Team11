<%@page import="java.sql.Timestamp"%>
<%@page import="DAO.StudentIDRequestDAO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="DAO.AccountDAO.signUpResult"%>
<%@page import="DAO.AccountDAO"%>
<%@ page import="java.util.Stack"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.Date" %>
<% request.setCharacterEncoding("euc-kr"); %>

<%
   String str_date = request.getParameter("reqSIDdate");
   String accountID = request.getParameter("accountID");
   
   Date date = Date.valueOf(str_date); // String -> java.sql.Date 인데, 날짜형식이 yyyy-mm-dd로 되어야 한다. 
    
   StudentIDRequestDAO studentIDRequestDAO = new StudentIDRequestDAO();
   
   // 성공
   if(studentIDRequestDAO.addReqSID(date, accountID)) {
	   
   }
   
   // 실패
   
%>