<%@page import="java.util.Stack"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.*" %>
<% request.setCharacterEncoding("euc-kr"); %>

<%
   String id = request.getParameter("id");
   String passwd = request.getParameter("passwd");
   String name = request.getParameter("name");
   Timestamp register = new Timestamp(System.currentTimeMillis());
   
   Connection conn = null;
   PreparedStatement pstmt = null;
   String str = "";
   
   try{
      String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/SE02"; // DB �����ּ�
      String dbId = "SE02_11"; // ���̵�
      String dbPass = "2018"; // ��й�ȣ
      
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(jdbcUrl,dbId,dbPass); // DB����
      
      String sql = "insert into example2 values(?,?,?,?)"; // example2�� ���Ƿ� ���� ���̺� �̸�.
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,id);
      pstmt.setString(2,passwd);
      pstmt.setString(3,name);
      pstmt.setString(4,"test");
      pstmt.executeUpdate();
      
      str = "member ���̺� ���ο� ���ڵ带 �߰��߽��ϴ�.";
   }catch(Exception e) {
      e.printStackTrace();
      str = "member ���̺� ���ο� ���ڵ带 �߰��� �����߽��ϴ�.";
   }finally {
      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
      if(conn != null) try{conn.close();}catch(SQLException sqle){}
   }
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>���ڵ� �߰�</title>
</head>
<body>
   <%=str %>
</body>
</html>