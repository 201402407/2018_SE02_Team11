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
	/* 생성 시 실행 */
	$(document).ready(function(){
	    jQuery.ajaxSettings.traditional = true;	
	});

	/* 가입 버튼 클릭 시 */
	function signup() {
		// 페이지 이동
		 location.href = "<%=request.getContextPath() %>/ui/signup.jsp"; 
	}
	
	/* 로그인 버튼 클릭 시 발생 */
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
</script>
</head>
<body>
	<!-- 메인 화면 이미지 공간 -->
	<img src="<%=request.getContextPath() %>/image/mainImage.png" id="mainImageSrc">
	<!-- 로그인 공간 -->
   <div id="LoginMenu">
     <!-- ID를 입력받으면 서버로 전송 -->
       <div id="LoginID_css" class="login_area">
         아이디  <input type="text" name="LoginID" id="inputAccountID"> <br />
         비밀번호  <input type="password" name="LoginPwd" id="inputPwd">
       </div>
       <div id="error">
       <%
            // 아이디, 비밀번호가 틀릴경우 화면에 메시지 표시
           // login.jsp에서 로그인 처리 결과에 따른 메시지를 보낸다.
            
        %>
      	</div>
     <!-- 회원가입 버튼 -->
      <button type="button" class="button" name="JoinButton" id="JoinButton" onclick="signup()">회원가입</button>
     <button type="button" class="button" id="loginButton" onclick="login()">로그인</button>
   </div>
</body>
</html>