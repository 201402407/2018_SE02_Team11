<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@page import="Util.OurTimes"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>SE02_Team11</title>
  <link href="<%=request.getContextPath() %>/css/lectureEvaluation.css?ver=1" rel="stylesheet" type="text/css">
  <script src="http://code.jquery.com/jquery-1.6.2.min.js"></script>
  <script src="http://code.jquery.com/ui/1.8.23/jquery-ui.min.js"></script>
  <script>
  /* 각 강의평가 담을 Array 선언 */
  var evaluationList = []; // 각 강의평가별 배열 선언 
  
  $(document).ready(function(){
	    jQuery.ajaxSettings.traditional = true;
	    getAttendanceList();
	    
	    $("#menu").children().eq(5).css("background-color", "#00649F");
	    $("#menu").children().eq(5).css("color", "white");
	    	    
	    window.addEventListener("message", messageHandler, true);
	    /* 초기 팝업창 숨기기 */
	    document.getElementById("popup").style.display = "none";

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
	
  function dayOfWeekToKor(dayofweek) {
	switch(dayofweek) {
	case "MONDAY":		return "월요일";
	case "TUESDAY":		return "화요일";
	case "WEDNESDAY":	return "수요일";
	case "THURSDAY":	return "목요일";
	case "FRIDAY":		return "금요일";
		default:		return -1;
	}  
  }
  
  function retakeToString(isRetake) {
	  if(isRetake)	return "Yes";
	  else			return "No";
  }
  

	/**
	 * registerTerm 을 인자로 받아 학기를 출력.
	 * ex) 20181 이면 1학기, 20182이면 2학기를 리턴
	 *  */
	function getTerm(registerTerm) {
		// 1이면
		if(registerTerm.toString().substring(4) == "1") {
			return "1학기";
		}
		// 2이면
		if(registerTerm.toString().substring(4) == "2") {
			return "2학기";
		}
		return null;
	}
	 
  /* 수강리스트 가져오기 */
  function getAttendanceList() {
	  $.ajax({
		  type: 'post',
		  url: "<%=request.getContextPath() %>/proc/attendance_getAttendanceListBySID.jsp",
		  data:  {
			  "sid" : <%=session.getAttribute("sid") %>
			  },
		  //async: false,
		  dataType : "json",
		  success: function(success) {
			 // alert(success);
			  if(success) { // 전송 완료 시.
				  if(success.error != null) { // 실패
					  alert(success.error);
				  }
				  else {
					var temp = success.data;
					
					  /* 수강리스트 출력 */
					  $.each(temp, function(key, arrjson) {
						  	// 수강과목 정보 넣기.
						  	var tempArray = [];
						  	
						  	tempArray.push(arrjson.subjectName);
						  	tempArray.push(getTerm(arrjson.registerTerm));
						  	tempArray.push(retakeToString(arrjson.isRetake));
						  	tempArray.push(dayOfWeekToKor(arrjson.dayOfWeek));
						  	tempArray.push(arrjson.startTime);
						  	tempArray.push(arrjson.endTime);
						  	tempArray.push(arrjson.score);
						  	tempArray.push(arrjson.attNum);
						  	
						  	evaluationList.push(tempArray); // 푸시
						  	
						});
						
					 displayAttendanceList(evaluationList);
					 
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
  
  /* 화면에 출력 */
  function displayAttendanceList(list) {
	  
	  // 전체 row 갯수
	  for(var i=0; i < list.length; i++) {
		  $("#tablebody").append("<tr class='listRow' id='listIndex" + i + "'></tr>"); // tr 생성
		  // 해당하는 row의 column 갯수
		  for(var j = 0; j < list[i].length; j++) {
				if(j + 1 == list[i].length) {
					$("#listIndex" + i).append('<button type="button" value="'+ list[i][7] + '" class="request" id="btn' + i + '">평가</button>');
					break;
			  	}
				
			  	$("#listIndex" + i).append("<td>"+ list[i][j] + "</td>");

		  }
	  }
	  
	  /* 클릭 이벤트 생성 */
	  $(".request").click(function(event){
		  isLectureEvaluation($(this).val());
		});
	
  }

  /* 강의평가여부 확인 */
  function isLectureEvaluation(attNum) {
	  $.ajax({
		  type: 'post',
		  url: "<%=request.getContextPath() %>/proc/attendance_getLectureEvaluationByCode.jsp",
		  data:  {
			  "attNum" : attNum
			  },
		  //async: false,
		  dataType : "json",
		  success: function(success) {
			  if(success) { // 전송 완료 시.
				  if(success.error != null) { // 실패
					  alert(success.error);
				  }
				  else {
					  if(success.data) {
						  alert("이미 강의평가가 완료된 과목입니다.");
						  return;
					  }
					  document.getElementById("popup").style.display = "block";
					  sendMessage(attNum);
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
  
  function sendMessage(obj) {
	  document.getElementById("popup").contentWindow.postMessage(obj,"*");
	}
  
  function messageHandler(e) {
	  /* 팝업창에서 닫는 요청이 들어오면 */
	  if(e.data == "close") {
		  document.getElementById("popup").style.display = "none";
	  }
  }

  </script>
</head>
<body>
<!-- 팝업창 -->
	<div id="myModal">
        <iframe id="popup" src="writeEvaluationPopup.html" class="popup"></iframe>
  </div>
<!-- 홈페이지의 메뉴 바 -->
   <div id="header">
   <button type="button" class="logout_button" id="logoutButton" onclick="logout()">로그아웃</button>
        <span id="nowLoginSID"><%=session.getAttribute("sid") %></span>
        
 </div>
 <!-- 좌측 메뉴 공간 -->
   <div id="studentMenu">
     <ul id ="menu">
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
   <div id="tableArea">
   		<table id="evaluationTable">

		<thead>
        <tr align="center" id="title"> 
            <td width="150" bgcolor="#00649F">과목명</td>
            <td width="60" bgcolor="#00649F">등록학기</td>
            <td width="70" bgcolor="#00649F">재수강여부</td>
            <td width="100" bgcolor="#00649F">강의요일</td>
            <td width="100" bgcolor="#00649F">강의 시작시간</td>
            <td width="100" bgcolor="#00649F">강의 종료시간</td>
            <td width="70" bgcolor="#00649F">학점</td>
            <td width="70" bgcolor="#00649F"></td>
        </tr>
        </thead>
        <tbody id="tablebody">
        </tbody>
    </table>
   </div>
</body>
</html>