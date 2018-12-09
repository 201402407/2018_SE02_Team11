<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>SE02_Team11</title>
  <link href="<%=request.getContextPath() %>/css/login.css" rel="stylesheet" type="text/css">
  <link rel="stylesheet" type="text/css" href="loginpage.css?ver=1">
  <script src="http://code.jquery.com/jquery-1.6.2.min.js"></script>
  <script src="http://code.jquery.com/ui/1.8.23/jquery-ui.min.js"></script>
  <script src="js/login.js?ver=1"></script>
  <script>
	/* ���� �� ���� */
	$(document).ready(function(){
	    jQuery.ajaxSettings.traditional = true;	
	});

	/* ���� ��ư Ŭ�� �� */
	function signup() {
		// ������ �̵�
		 location.href = "<%=request.getContextPath() %>/ui/signup.jsp"; 
	}
	
	/* �α��� ��ư Ŭ�� �� �߻� */
	function login() {
		$.ajax({
			  type: 'post',
			  url: "<%=request.getContextPath() %>/proc/account_login.jsp",
			  data:  {
					"id" : $("#inputAccountID").val(),
					"pwd" : $("#inputPwd").val()
				  },
			  //async: false,
			  dataType : "json",
			  success: function(success) {
				  if(success) { // ���� �Ϸ� ��.
					  if(success.error != null) { // ����
						  $("#error").empty(); // ����
						  $("#error").append(success.error); // �߰�
					  }
					  else {
						  <!-- location.href = "<%=request.getContextPath() %>/proc/account_login.jsp"; // �α��� �������� �̵�.-->						 
					  }
				  }
				  else {
					  alert("��� �Ŀ� �õ����ּ���.");
				  }
			  },
			  error: function(xhr, request,error) {
				
				  
				  
			  }
			});
	}
</script>
</head>
<body>
	<!-- ���� ȭ�� �̹��� ���� -->
	<img src="<%=request.getContextPath() %>/image/mainImage.png" id="mainImageSrc">
	<!-- �α��� ���� -->
   <div id="LoginMenu">
     <!-- ID�� �Է¹����� ������ ���� -->
       <div id="LoginID_css" class="login_area">
         ���̵�  <input type="text" name="LoginID" id="inputAccountID"> <br />
         ��й�ȣ  <input type="password" name="LoginPwd" id="inputPwd">
       </div>
       <div id="error">
       <%
            // ���̵�, ��й�ȣ�� Ʋ����� ȭ�鿡 �޽��� ǥ��
           // login.jsp���� �α��� ó�� ����� ���� �޽����� ������.
            
        %>
      	</div>
     <!-- ȸ������ ��ư -->
      <button type="button" class="button" name="JoinButton" id="JoinButton" onclick="signup()">ȸ������</button>
     <button type="button" class="button" id="loginButton" onclick="login()">�α���</button>
   </div>
</body>
</html>