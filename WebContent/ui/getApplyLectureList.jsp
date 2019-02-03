<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>SE02_Team11</title>
  <link href="<%=request.getContextPath() %>/css/getApplyLectureList.css" rel="stylesheet" type="text/css">
  <script src="http://code.jquery.com/jquery-1.6.2.min.js"></script>
  <script src="http://code.jquery.com/ui/1.8.23/jquery-ui.min.js"></script>
  <script>
  $(document).ready(function(){
	    jQuery.ajaxSettings.traditional = true;
	    window.addEventListener("message", messageHandler, true);
	   
	    
	    $("#menu").children().eq(4).css("background-color", "#00649F");
	    $("#menu").children().eq(4).css("color", "white");
	    
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
			location.href = "login.jsp";
			break;
		case 6:
			location.href = "login.jsp";
			break;
		case 7:
			location.href = "login.jsp";
			break;
		case 8:
			location.href = "login.jsp";
			break;
		}
		});
  });
  
  function sendMessage(obj) {
	  
	  document.getElementById("popup").contentWindow.postMessage(obj,"*");
	}
  
  function messageHandler(e) {
	  /* 팝업창에서 닫는 요청이 들어오면 */
	  if(e.data == "close") {
		  document.getElementById("popup").style.display = "none";
	  }
  }
  
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
  
  /* 특정 과목 검색 */
  function search() {
	  $("#subArea").empty(); // 초기화
	  alert($("#inputSubjectName").val());
	  $.ajax({
	    	type : 'post',
	    	url: "<%=request.getContextPath() %>/proc/lecture_getLectureByName.jsp",
	        data : {
	        	"sid" : <%=session.getAttribute("sid") %>,
	        	"subjectName" : $("#inputSubjectName").val()
	        },
	        dataType : "json",
			  success: function(success) {
				  
				  if(success) { // 전송 완료 시.
					  if(success.error != null) { // 실패
						  alert(success.error);
					  }
					  else {
						
						var temp = success.data;
						
						var result = []; // 배열 생성. 나중에 인덱스로 읽게
						$.each(temp, function(key, value){ // JSON 받아온 것의 data 순서대로 읽음.
							var temp2 = [];
						/* 순서 : score, profName, dayOfWeek, registerTerm, startTime, endTime, 
						applyNum, lectureCode, subjectName, allNum
								dayOfWeek, startTime, endTime, score 
							push와 shift를 사용하자 */
								$.each(value, function(element, result) {
									//alert(result);
									temp2.push(result);
								});
							result.push(temp2);
						 });

							for(var i = 0; i < result.length; i++) { // 인덱스 1은 subjectName이다.
								$('#subArea').append("<div id='"+ i +"'></div>"); // div추가
								$("#" + i).append('<span id="span'+ i +'" name="not">'+ result[i][8] + '</span>');
								$("#" + i).append('<button type="button" name="not" id="btn' + i + '">신청</button>'); // 버튼추가
								
								$("#" + i).css({
									'width' : '99.9%',
									'height' : '25px',
									'border' : '0.5px solid #ADADAD',
									'color' : '#707070',
									'float' : 'left',
									'text-indent' : '7px'
								})
								
								$("#btn" + i).css({ // 버튼 css 설정
									'width' : '70px',
									'height' : '20px',
									'background' : '#00649F',
									'color' : 'white',
									'font-size' : '10px',
									'line-height' : '10px',
									'float' : 'right',
									'margint-right' : '30px',
									'border' : '0px',
									'border-radius' : '5px',
								});
								
								/* 수강신청 가능 리스트 조회 팝업 전송 */
								/*
								$("#"+i).click(function(event){
									if(event.target.name == "not") {
										alert("not");
									}
									else {
										
										make_function2(result[i]);
									}
									
								});
								*/
								$("#span"+i).click(make_function2(result[i]));
								/* 해당과목 수강신청 */
								$("#btn"+i).click(make_function(result[i]));
							}
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
  
  /* 모두 검색 */
  function allsearch() {
	  $("#subArea").empty(); // 초기화
	    $.ajax({
	    	type : 'post',
	    	url: "<%=request.getContextPath() %>/proc/lecture_getApplyLectureList.jsp",
	        data : {
	        	"sid" : <%=session.getAttribute("sid") %>
	        },
	        dataType : "json",
			  success: function(success) {
				  
				  if(success) { // 전송 완료 시.
					  if(success.error != null) { // 실패
						  alert(success.error);
					  }
					  else {
						
						var temp = success.data;
						
						var result = []; // 배열 생성. 나중에 인덱스로 읽게
						$.each(temp, function(key, value){ // JSON 받아온 것의 data 순서대로 읽음.
							var temp2 = [];
						/* 순서 : score, profName, dayOfWeek, registerTerm, startTime, endTime, 
						applyNum, lectureCode, subjectName, allNum
								dayOfWeek, startTime, endTime, score 
							push와 shift를 사용하자 */
								$.each(value, function(element, result) {
									//alert(result);
									temp2.push(result);
								});
							result.push(temp2);
						 });

							for(var i = 0; i < result.length; i++) { // 인덱스 1은 subjectName이다.
								$('#subArea').append("<div id='"+ i +"'></div>"); // div추가
								$("#" + i).append('<span id="span'+ i +'" name="not">'+ result[i][8] + '</span>');
								$("#" + i).append('<button type="button" name="not" id="btn' + i + '">신청</button>'); // 버튼추가
								
								$("#" + i).css({
									'width' : '99.9%',
									'height' : '25px',
									'border' : '0.5px solid #ADADAD',
									'color' : '#707070',
									'float' : 'left',
									'text-indent' : '7px'
								})
								
								$("#btn" + i).css({ // 버튼 css 설정
									'width' : '70px',
									'height' : '20px',
									'background' : '#00649F',
									'color' : 'white',
									'font-size' : '10px',
									'line-height' : '10px',
									'float' : 'right',
									'margint-right' : '30px',
									'border' : '0px',
									'border-radius' : '5px',
								});
								
								/* 수강신청 가능 리스트 조회 팝업 전송 */
								/*
								$("#"+i).click(function(event){
									if(event.target.name == "not") {
										alert("not");
									}
									else {
										
										make_function2(result[i]);
									}
									
								});
								*/
								$("#span"+i).click(make_function2(result[i]));
								/* 해당과목 수강신청 */
								$("#btn"+i).click(make_function(result[i]));
							}
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
  /* 클로저 */
  //리턴할 때마다 새로운 num을 사용. 수강신청
  function make_function(lectureArray) {
      return function() { addAttendance(lectureArray); };
  }
  
  //리턴할 때마다 새로운 num을 사용. 팝업
  function make_function2(lectureArray) {
      return function() {  lecturePopup(lectureArray); };
  }
  
  /* 해당분반상세조회 */
  function lecturePopup(lectureArray) {
	  document.getElementById("popup").style.display = "block"; // 팝업 열기
	  
	  sendMessage(lectureArray); // 해당 인덱스에 해당하는 데이터를 전송
  }
  /* 수강신청 */
  function addAttendance(lectureArray) {
	  $.ajax({
		  type: 'post',
		  url: "<%=request.getContextPath() %>/proc/attendance_addAttendance.jsp",
		  data:  {
			  "sid" : <%=session.getAttribute("sid") %>,
			  "lcode" : lectureArray[7]
			  },
		  //async: false,
		  dataType : "json",
		  success: function(success) {
			
			  if(success) { // 전송 완료 시.
				  if(success.error != null) { // 실패
					  alert(success.error);
				  }
				  else {
					  alert("정상적으로 수강 신청이 완료되었습니다.");
					  
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
        <iframe id="popup" src="lecturePopup.html" class="popup"></iframe>
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
   <!-- 검색 라인 -->
     <div id="searchArea" class="searchArea">
         <input type="text" name="SubjectName" id="inputSubjectName">
		<button type="button" class="button" name="searchButton" id="searchButton" onclick="search()">조회</button>
		<button type="button" class="button" name="allButton" id="allButton" onclick="allsearch()">모두 보기</button>         
       </div>
       <!-- 주요화면 -->
   <div id="mainArea">
   		<div id="subjectNameContext">과목명</div>
   		<div id="subArea">
   		</div>
   </div>
</body>
</html>