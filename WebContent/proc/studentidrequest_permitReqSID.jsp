<%@page import="java.sql.Timestamp"%>
<%@page import="DAO.StudentIDRequestDAO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Stack"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.Date" %>
<% request.setCharacterEncoding("euc-kr"); %>

<%
   int reqSIDnum = Integer.valueOf(request.getParameter("reqSIDnum"));
   int dCode = Integer.valueOf(request.getParameter("dcode"));
    
   StudentIDRequestDAO studentIDRequestDAO = new StudentIDRequestDAO();
   
   // ����
   if(studentIDRequestDAO.permitReqSID(reqSIDnum, dCode)) {
	   
   }
   
   // ����
   else {
	   
   }
%>
