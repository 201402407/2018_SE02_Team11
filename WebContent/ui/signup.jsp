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
	/* 생성 시 실행 */
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
	
	/* 회원가입 버튼 클릭 시 발생 */
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
				  if(success) { // 전송 완료 시.
					  if(success.error != null) { // 실패
						  <!--location.href = "<%=request.getContextPath() %>/proc/account_signup.jsp"; -->
						  $("#error").empty(); // 비우기
						  $("#error").append(success.error); // 추가
					  }
					  else {
						  window.location.href = "getStudentInfoBySID.jsp"; // 로그인 페이지로 이동.						 
					  }
				  }
				  else {
					  alert("잠시 후에 시도해주세요.");
				  }
			  },
			  error: function(xhr, request,error) {
				
				  
				  
			  }
			});
	}
	
	/* 년도 추가 */
	function appendYear(){
		var date = new Date();
		var year = date.getFullYear();
		var selectValue = document.getElementById("year");
		var optionIndex = 0;
		for(var i=year-100;i<=year;i++){
				selectValue.add(new Option(i+"년",i),optionIndex++);                        
		}
	}
	
	/* 월 추가 */
	function appendMonth(){
		var selectValue = document.getElementById("month"); 
		var optionIndex = 0;
		for(var i=1;i<=12;i++){
				selectValue.add(new Option(i+"월",i),optionIndex++);
		}
	}
	
	/* 일 추가 */
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
				selectValue.add(new Option(i+"일",i),optionIndex++);
		}
	}
	
</script>
</head>
<body>
	<!-- 메인 화면 이미지 공간 -->
	<img src="<%=request.getContextPath() %>/image/mainImage.png" id="mainImageSrc">
	<!-- 로그인 공간 -->
   <div id="LoginMenu">
     <!-- ID를 입력받으면 서버로 전송 -->
     <form class="Login" action="login.jsp" method="post">
       <div id="LoginID_css" class="sign_area">
   	이름  <input type="text" name="SignupName" id="inputName"> <br />
        생년월일 
	<input type="hidden" name="memBirth"> 
	<select id="year" class="select">
	 <option>----년</option>
	</select>
	<select id="month" class="select">
	<option>--월</option>
	</select>
	<select id="day" class="select">
	<option>--일</option>
	</select>
	<br />
         아이디  <input type="text" name="signupID" id="inputAccountID"> <br />
         비밀번호  <input type="password" name="signupPwd" id="inputPwd">
       </div>
       <div id="error">
       <%
            // 아이디, 비밀번호가 틀릴경우 화면에 메시지 표시
        %>
      	</div>
     <!-- 회원가입 버튼 -->
     </form>
     <button type="button" class="button" id="signupButton" onclick="signup()">회원가입</button>
   </div>
</body>
</html>