<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%
	try {
		Class c = Class.forName("com.mysql.jdbc.Driver");
		if(c != null) {
			out.println("MySQL JDBC �ε� �Ϸ�");
		}
	}
	catch(ClassNotFoundException e) {
		out.println(e);
	}
%>
<html>
<body>
<h2>�׽�Ʈ</h2>
<form method = "post" action="insertMemberPro.jsp">
      ���̵� : <input type="text" name = "id" maxlength = "12"><br/>
      �н����� : <input type="password" name = "passwd" maxlength="12"><br/>
      �̸� : <input type="text" name = "name" maxlength="10"><br/>
      <input type = "submit" value="ȸ������">
      <input type = "reset" value="�ٽ��Է�">
   </form></body>
</html>
