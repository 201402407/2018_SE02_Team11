<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>SE02_Team11</title>
  <link href="<%=request.getContextPath() %>/css/signup.css" rel="stylesheet" type="text/css">
  <link rel="stylesheet" type="text/css" href="loginpage.css?ver=1">
  <script src="http://code.jquery.com/jquery-1.6.2.min.js"></script>
  <script src="http://code.jquery.com/ui/1.8.23/jquery-ui.min.js"></script>
  <script src="js/login.js?ver=1"></script>
  <script>
	/* 생성 시 실행 */
	$(document).ready(function(){
	    jQuery.ajaxSettings.traditional = true;
			
		$.ajaxSetup({
		    scriptCharset: "utf-8",
		    contentType: "application/json; charset=utf-8"
		});
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
	
	/* 회원가입 버튼 클릭 시 발생 */
	function signup() {
		var birth = $("#year option:selected").val() + "-" + $("#month option:selected").val() +
		"-" + $("#day option:selected").val();
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
						  $("#error").empty(); // 비우기
						  $("#error").append(success.error); // 추가
					  }
					  else {
						  <!-- location.href = "<%=request.getContextPath() %>/proc/account_login.jsp"; // 로그인 페이지로 이동.-->						 
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
 <!-- 홈페이지의 메뉴 바 -->
   <div id="header">
        <span id="NowLogin"><%=session.getAttribute("sessionID") %></span>
        <button type="button" class="logout_button" id="logoutButton">로그아웃</button>
 </div>
 <!-- 좌측 메뉴 공간 -->
   <div id="studentMenu">
     
   </div>
	<!-- 메인 화면 이미지 공간 -->
	<img src="<%=request.getContextPath() %>/image/mainImage.png" id="mainImageSrc">
</body>
</html>