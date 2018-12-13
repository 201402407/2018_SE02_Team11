<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>SE02_Team11</title>
  <link href="<%=request.getContextPath() %>/css/requestTimeOnOff.css?ver=1" rel="stylesheet" type="text/css">
  <script src="http://code.jquery.com/jquery-1.6.2.min.js"></script>
  <script src="http://code.jquery.com/ui/1.8.23/jquery-ui.min.js"></script>
  <script>
  $(document).ready(function(){
	    jQuery.ajaxSettings.traditional = true;
	    window.addEventListener("message", messageHandlerReason, true);
	
	    $("#menu").children().eq(1).css("background-color", "#00649F");
	    $("#menu").children().eq(1).css("color", "white");
	    
	    /* 좌측 메뉴 클릭 이벤트 */
		$("ul li").click(function(event){
		// 누른 노드가 몇 번째인지 switch로 판단해서 적용
		switch($(this).index()) {
		case 0:
			location.href = "getStudentInfoBySID.jsp";
			break;
		case 1:
			location.href = "requestTimeOnOff.jsp";
			break;
		case 2:
			location.href = "thisSemesterSubjectBySID.jsp";
			break;
		case 3:
			location.href = "timeTable.jsp";
			break;
		case 4:
			location.href = "getApplyLectureList.jsp";
			break;
		case 5:
			location.href = "lectureEvaluation.jsp";
			break;
		case 6:
			location.href = "getGradeInfo.jsp";
			break;
		case 7:
			location.href = "getAwardInfoBySID.jsp";
			break;
		case 8:
			location.href = "getScholarshipList.jsp";
			break;
		}
		});
	    
	    /* 초기 팝업창 숨기기 */
	    document.getElementById("popup").style.display = "none";
		
		/* 휴복학에 따른 종료학기 입력 열고닫기 */
		$(function(){
			$("#isTakeOff").change(function() {
				if($("#isTakeOff option:selected").val() == "takeoff") { // 휴학
					document.getElementById("thirdline").style.display = "block";	
				}
				if($("#isTakeOff option:selected").val() == "resume") { // 복학
					document.getElementById("thirdline").style.display = "none";	
				}
			})
		})
		
	});
  
  function logout() {
		$.ajax({
			
			  type: 'post',
			  url: "<%=request.getContextPath() %>/proc/account_logout.jsp",
			  data:  {
				  },
			  //async: false,
			  dataType : "json",
			  success: function(success) {
				  
				  if(success) { // 전송 완료 시.
					  location.href = "login.jsp";
				  }
				  else {
					  alert("잠시 후에 시도해주세요.");
				  }
			  },
			  error: function(xhr, request,error) {

			  }
			});
	}
  
  function request() {
	  if($("#isTakeOff option:selected").val() == "takeoff") {
		  /* 팝업 창을 활성화 시키고 messageHandler 활성화 */
		  document.getElementById("popup").style.display = "block";
	  }
	  if($("#isTakeOff option:selected").val() == "resume") {
		  requestAjax("resume", null);
	  }
  }
  
  function messageHandlerReason(e) {
	  /* 팝업창에서 닫는 요청이 들어오면 */
	  if(e.data == "close") {
		  document.getElementById("popup").style.display = "none";
	  }
	  /* 사유 적고 확인 눌렀으면 */
	  else {
		  requestAjax("takeoff", e.data);
	  }
  }
  
  /* ajax 전송 */
  function requestAjax(takeoff, reason) {
	  var endSemester = $("#endSemesterArea").val();
	  if(takeoff == "resume") {
		  endSemester = 99999;
	  }
	  $.ajax({
		  type: 'post',
		  url: "<%=request.getContextPath() %>/proc/changerecord_requestTimeOnOffs.jsp",
		  data:  {
			  "changeType" : takeoff,
			  "startSemester" : $("#startSemesterArea").val(),
				"endSemester" : endSemester,
				"sid" : <%=session.getAttribute("sid") %>,
				"reason" : reason // not null 인지?
			  },
		  //async: false,
		  dataType : "json",
		  success: function(success) {
			  if(success) { // 전송 완료 시.
				  if(success.error != null) { // 실패
						alert(success.error);
				  }
				  else { // 성공
						alert("신청완료! 대기해주세요.");
						location.href = "<%=request.getContextPath() %>/ui/requestTimeOnOff.jsp";
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
<!-- 팝업창 -->
	<div id="myModal">
        <iframe id="popup" src="requestPopup.html" class="popup"></iframe>
  </div>
 <!-- 홈페이지의 메뉴 바 -->
   <div id="header">
   <button type="button" class="logout_button" id="logoutButton" onclick="logout()">로그아웃</button>
        <span id="nowLoginSID"><%=session.getAttribute("sid") %></span>
        
 </div>
 <!-- 좌측 메뉴 공간 -->
   <div id="studentMenu">
     <ul id="menu">
        <li id="first">학적상태 조회</li>
        <li>휴복학 요청</li>
        <li>당학기 운영과목 조회</li>
        <li>신청 시간표 조회</li>
        <li>수강신청 가능 리스트 조회</li>
        <li>강의평가</li>
        <li>성적 조회</li>
        <li>학생 장학내역 조회</li>
        <li>전체 장학 목록</li>
	</ul>
   </div>
   <div id="inputArea">
	 	<div id="firstline">
	 			<div id="isTakeOffText" class="select">휴학 / 복학</div>
	 			<select id="isTakeOff" class="area">
	 			<option value='takeoff'>휴학</option>
	 			<option value='resume'>복학</option>
				</select>
	 	</div>
	 	<div id="secondline">
	 			시작학기<input type="text" id="startSemesterArea" class="area">
	 	</div>
	 	<div id="thirdline">
	 			종료학기<input type="text" id="endSemesterArea" class="area">
	 	</div>
	 </div>
	 <button type="button" class="button" id="requestButton" onclick="request()">신청</button>
</body>
</html>