<%@page import="ClassObject.StudentIDRequest"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="DAO.StudentIDRequestDAO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Stack"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.Date" %>
<% request.setCharacterEncoding("euc-kr"); %>

<%

   ArrayList<StudentIDRequest> arrayList = new ArrayList<StudentIDRequest>();

   StudentIDRequestDAO studentIDRequestDAO = new StudentIDRequestDAO();
   arrayList = studentIDRequestDAO.getReqSIDList();
   
   
%>
