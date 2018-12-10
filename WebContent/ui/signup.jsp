<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>SE02_Team11</title>
  <link href="<%=request.getContextPath() %>/css/signup.css?ver=1" rel="stylesheet" type="text/css">
  <script src="http://code.jquery.com/jquery-1.6.2.min.js"></script>
  <script src="http://code.jquery.com/ui/1.8.23/jquery-ui.min.js"></script>
  <script>
	/* ���� �� ���� */
	$(document).ready(function(){
	    jQuery.ajaxSettings.traditional = true;
			
		appendYear();
		$(function(){
			$("#year").change(function() {
				$("#month").empty();
				appendMonth();
			})
		})
		
		$(function(){
			$("#month").change(function() {
				$("#day").empty();
				appendDay(this.value);
			})
		})
	});
	
	function pad(n, width) {
		  n = n + '';
		  return n.length >= width ? n : new Array(width - n.length + 1).join('0') + n;
		}
	
	/* ȸ������ ��ư Ŭ�� �� �߻� */
	function signup() {
		var birth = $("#year option:selected").val() + "-" + pad($("#month option:selected").val(), 2) +
		"-" + pad($("#day option:selected").val(), 2);
		$.ajax({
		
			  type: 'post',
			  url: "<%=request.getContextPath() %>/proc/account_signup.jsp",
			  data:  {
				  "name" : $("#inputName").val(),
				  "birth" : birth,
					"id" : $("#inputAccountID").val(),
					"pwd" : $("#inputPwd").val()
				  },
			  //async: false,
			  dataType : "json",
			  success: function(success) {
				  if(success) { // ���� �Ϸ� ��.
					  if(success.error != null) { // ����
						  <!--location.href = "<%=request.getContextPath() %>/proc/account_signup.jsp"; -->
						  $("#error").empty(); // ����
						  $("#error").append(success.error); // �߰�
					  }
					  else {
						  window.location.href = "getStudentInfoBySID.jsp"; // �α��� �������� �̵�.						 
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
	
	/* �⵵ �߰� */
	function appendYear(){
		var date = new Date();
		var year = date.getFullYear();
		var selectValue = document.getElementById("year");
		var optionIndex = 0;
		for(var i=year-100;i<=year;i++){
				selectValue.add(new Option(i+"��",i),optionIndex++);                        
		}
	}
	
	/* �� �߰� */
	function appendMonth(){
		var selectValue = document.getElementById("month"); 
		var optionIndex = 0;
		for(var i=1;i<=12;i++){
				selectValue.add(new Option(i+"��",i),optionIndex++);
		}
	}
	
	/* �� �߰� */
	function appendDay(temp){
		var month = temp;
		var selectValue = document.getElementById("day");
		var optionIndex = 0;
		var day;
		if(month == 2)
			day = 28;
		else if((month == 1) || (month == 3) || (month == 5) || (month == 7) || (month == 8) || (month == 10) || (month == 12))
			day = 31;
		else 
			day = 30;
		for(var i=1;i<=day;i++){
				selectValue.add(new Option(i+"��",i),optionIndex++);
		}
	}
	
</script>
</head>
<body>
	<!-- ���� ȭ�� �̹��� ���� -->
	<img src="<%=request.getContextPath() %>/image/mainImage.png" id="mainImageSrc">
	<!-- �α��� ���� -->
   <div id="LoginMenu">
     <!-- ID�� �Է¹����� ������ ���� -->
     <form class="Login" action="login.jsp" method="post">
       <div id="LoginID_css" class="sign_area">
   	�̸�  <input type="text" name="SignupName" id="inputName"> <br />
        ������� 
	<input type="hidden" name="memBirth"> 
	<select id="year" class="select">
	 <option>----��</option>
	</select>
	<select id="month" class="select">
	<option>--��</option>
	</select>
	<select id="day" class="select">
	<option>--��</option>
	</select>
	<br />
         ���̵�  <input type="text" name="signupID" id="inputAccountID"> <br />
         ��й�ȣ  <input type="password" name="signupPwd" id="inputPwd">
       </div>
       <div id="error">
       <%
            // ���̵�, ��й�ȣ�� Ʋ����� ȭ�鿡 �޽��� ǥ��
        %>
      	</div>
     <!-- ȸ������ ��ư -->
     </form>
     <button type="button" class="button" id="signupButton" onclick="signup()">ȸ������</button>
   </div>
</body>
</html>