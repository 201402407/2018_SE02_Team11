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
				"sid" : <%=session.getAttribute("sID") %>,
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
   <button type="button" class="logout_button" id="logoutButton">로그아웃</button>
        <span id="nowLoginSID"><%=session.getAttribute("sID") %></span>
        
 </div>
 <!-- 좌측 메뉴 공간 -->
   <div id="studentMenu">
     asdaf
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