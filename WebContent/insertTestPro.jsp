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
      String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/SE02"; // DB 저장주소
      String dbId = "SE02_11"; // 아이디
      String dbPass = "2018"; // 비밀번호
      
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(jdbcUrl,dbId,dbPass); // DB연결
      
      String sql = "insert into example2 values(?,?,?,?)"; // example2는 임의로 만든 테이블 이름.
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,id);
      pstmt.setString(2,passwd);
      pstmt.setString(3,name);
      pstmt.setString(4,"test");
      pstmt.executeUpdate();
      
      str = "member 테이블에 새로운 레코드를 추가했습니다.";
   }catch(Exception e) {
      e.printStackTrace();
      str = "member 테이블에 새로운 레코드를 추가에 실패했습니다.";
   }finally {
      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
      if(conn != null) try{conn.close();}catch(SQLException sqle){}
   }
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>레코드 추가</title>
</head>
<body>
   <%=str %>
</body>
</html>