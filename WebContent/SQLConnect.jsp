<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%
	try {
		Class c = Class.forName("com.mysql.jdbc.Driver");
		if(c != null) {
			out.println("MySQL JDBC 로딩 완료");
		}
	}
	catch(ClassNotFoundException e) {
		out.println(e);
	}
%>
<html>
<body>
<h2>테스트</h2>
<form method = "post" action="insertMemberPro.jsp">
      아이디 : <input type="text" name = "id" maxlength = "12"><br/>
      패스워드 : <input type="password" name = "passwd" maxlength="12"><br/>
      이름 : <input type="text" name = "name" maxlength="10"><br/>
      <input type = "submit" value="회원가입">
      <input type = "reset" value="다시입력">
   </form></body>
</html>
