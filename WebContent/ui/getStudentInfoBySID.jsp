<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>SE02_Team11</title>
  <link href="<%=request.getContextPath() %>/css/getStudentInfoBySID.css?ver=1" rel="stylesheet" type="text/css">
  <link rel="stylesheet" type="text/css" href="loginpage.css?ver=1">
  <script src="http://code.jquery.com/jquery-1.6.2.min.js"></script>
  <script src="http://code.jquery.com/ui/1.8.23/jquery-ui.min.js"></script>
  <script src="js/login.js?ver=1"></script>
  <script>
  
	/* 생성 시 실행 */
	$(document).ready(function(){
	    jQuery.ajaxSettings.traditional = true;
		getStudentInfo();
	});
	
	/* 페이지 들어올 시 바로 실행 */
	function getStudentInfo() {
		$.ajax({
		
			  type: 'post',
			  url: "<%=request.getContextPath() %>/proc/student_getStudentInfoBySID.jsp",
			  data:  {
				  "sid" : <%=session.getAttribute("sID") %>
				  },
			  //async: false,
			  dataType : "json",
			  success: function(success) {
				  alert(success);
				  if(success) { // 전송 완료 시.
					  if(success.error != null) { // 실패
						  
					  }
					  else {
						  var name = success.name;
						  var departmentName = success.departmentName;	
						  var semester = success.semester;
						  var isTimeOff = success.isTimeOff;
						  var isGraduate = success.isGraduate;
						  
						  $("#nameArea").append(name);
						  $("#departnameArea").append(departmentName);
						  $("#yearArea").append(year);
						  $("#startSemesterArea").append(semester);
						  $("#isTakeoffArea").append(isTimeOff);
						  $("#isGraduateArea").append(isGraduate);
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
 <!-- 홈페이지의 메뉴 바 -->
   <div id="header">
   <button type="button" class="logout_button" id="logoutButton">로그아웃</button>
        <span id="nowLoginSID"><%=session.getAttribute("sID") %></span>
        
 </div>
 <!-- 좌측 메뉴 공간 -->
   <div id="studentMenu">
     asdaf
   </div>
	<!-- 메인 화면 이미지 공간 -->
	 <img src="<%=request.getContextPath() %>/image/mainImage.png" id="mainImageSrc"> 
	 <div id="studentInfoArea">
	 	<div id="firstline">
	 			학년<div id="yearArea" class="area"></div>
	 			이름<div id="nameArea" class="area"></div>
	 			휴복학여부<div id="isTakeoffArea" class="area"></div>
	 	</div>
	 	<div id="secondline">
	 			학과명<div id="departnameArea" class="area"></div>
	 			이수학기<div id="startSemesterArea" class="area"></div>
	 			졸업여부<div id="isGraduateArea" class="area"></div>
	 	</div>
	 </div>
</body>
</html>