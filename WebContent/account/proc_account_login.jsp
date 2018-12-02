<%@page import="DAO.AccountDAO.loginResult"%>
<%@page import="DAO.AccountDAO.signUpResult"%>
<%@page import="DAO.AccountDAO"%>
<%@ page import="java.util.Stack"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.*" %>
<% request.setCharacterEncoding("euc-kr"); %>

<%
   String id = request.getParameter("accountID");
   String passwd = request.getParameter("pwd");
   
   loginResult result;
   result = new AccountDAO().login(id, passwd);
   
%>