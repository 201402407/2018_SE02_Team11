<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>SE02_Team11</title>
  <link href="<%=request.getContextPath() %>/css/getReqSIDList.css?ver=1" rel="stylesheet" type="text/css">
  <script src="http://code.jquery.com/jquery-1.6.2.min.js"></script>
  <script src="http://code.jquery.com/ui/1.8.23/jquery-ui.min.js"></script>
  <script>
  $(document).ready(function(){
	    jQuery.ajaxSettings.traditional = true;
	    getList();
	    if(<%=session.getAttribute("isAdmin") == null %>) {
	    	alert("관리자로 로그인 되어있지 않습니다!");
	    	location.href = "login.jsp";
	    }
	    $("#menu").children().eq(0).css("background-color", "#00649F");
	    $("#menu").children().eq(0).css("color", "white");
	    
	    /* 좌측 메뉴 클릭 이벤트 */
		$("ul li").click(function(event){
		// 누른 노드가 몇 번째인지 switch로 판단해서 적용
		switch($(this).index()) {
		case 0:
			location.href = "getReqSIDList.jsp";
			break;
		case 1:
			location.href = "setTimeOff.jsp";
			break;
		case 2:
			location.href = "addSubject.jsp";
			break;
		case 3:
			location.href = "addScholarship.jsp";
			break;
		case 4:
			location.href = "awardToStudent.jsp";
			break;
		case 5:
			location.href = "addLecture.jsp";
			break;
		case 6:
			location.href = "addProfessor.jsp";
			break;
		case 7:
			location.href = "login.jsp";
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

  /* 리스트출력 */
  function getList() {
	  $("#subArea").empty(); // 초기화
	    $.ajax({
	    	type : 'post',
	    	url: "<%=request.getContextPath() %>/proc/studentidrequest_getReqSIDList.jsp",
	        data : {
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
						/* 순서 : reqSIDnum, reqSIDdata, accountID
							push와 shift를 사용하자 */
								$.each(value, function(element, result) {
									
									temp2.push(result);
								});
							result.push(temp2);
						 });

							for(var i = 0; i < result.length; i++) { // 인덱스 1은 subjectName이다.
								/* reqnum */
								$('#subArea').append("<div class='reqdiv' id='num"+ i +"'></div>"); // div추가
								$("#num" + i).append('<span class="reqspan" id="numspan'+ i +'" name="not">'+ result[i][0] + '</span>');
								/* reqdate */
								$('#subArea').append("<div class='reqdiv' id='date"+ i +"'></div>"); // div추가
								$("#date" + i).append('<span class="reqspan" id="datespan'+ i +'" name="not">'+ result[i][1] + '</span>');
								/* reqid */
								$('#subArea').append("<div class='reqdiv' id='id"+ i +"'></div>"); // div추가
								$("#id" + i).append('<span class="reqspan" id="idspan'+ i +'" name="not">'+ result[i][2] + '</span>');
								/* reqbtn */
								$('#subArea').append("<div class='reqdiv' id='ws"+ i +"'></div>"); // div추가
								$("#ws" + i).append('<button class="permit" type="button" name="not" id="permitbtn' + i + '">승인</button>'); // 버튼추가
								$("#ws" + i).append('<button class="reject" type="button" name="not" id="rejectbtn' + i + '">거절</button>'); // 버튼추가
								
								/*
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
								*/
								
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
								
								/* 해당과목 수강신청 */
								$("#permitbtn"+i).click(make_permitfunction(result[i]));
								$("#rejectbtn"+i).click(make_rejectfunction(result[i]));
							}
							
							/* css 추가 */
							$(".reqdiv").css({
								'width' : '24.8%',
								'height' : '25px',
								'border' : '0px',
								//'margin-right' : '-7px',
								'color' : '#707070',
								'float' : 'left',
								'text-indent' : '7px',
								'display' : 'inline-block'
							})
							
							$(".permit" + i).css({ // 버튼 css 설정
								'width' : '45px',
								'height' : '20px',
								'background' : '#00649F',
								'color' : 'white',
								'font-size' : '10px',
								'line-height' : '10px',
								'float' : 'right',
								'margint-right' : '30px',
								'text-align': 'center',
								'border' : '0px',
								'border-radius' : '5px',
							});
							
							$(".reject" + i).css({ // 버튼 css 설정
								'width' : '45px',
								'height' : '20px',
								'background' : '#ADADAD',
								'color' : 'white',
								'font-size' : '10px',
								'text-align': 'center',
								'line-height' : '10px',
								'float' : 'right',
								'margint-right' : '30px',
								'border' : '0px',
								'border-radius' : '5px',
							});
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
  //리턴할 때마다 새로운 num을 사용. 
  function make_permitfunction(lectureArray) {
      return function() { permit(lectureArray); };
  }
  function make_rejectfunction(lectureArray) {
      return function() { reject(lectureArray); };
  }
  
  /* 학번부여허가 */
  function permit(lectureArray) {
	  $.ajax({
		  type: 'post',
		  url: "<%=request.getContextPath() %>/proc/studentidrequest_permitReqSID.jsp",
		  data:  {
			  "reqnum" : lectureArray[0],
			  "dcode" : $("#inputDepartmentCode").val()
			  },
		  //async: false,
		  dataType : "json",
		  success: function(success) {
			
			  if(success) { // 전송 완료 시.
				  if(success.error != null) { // 실패
					  alert(success.error);
				  }
				  else {
					  alert("학번요청 허가 완료되었습니다.");
					  getList();
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
  
  /* 학번부여거절 */
  function reject(lectureArray) {
	  $.ajax({
		  type: 'post',
		  url: "<%=request.getContextPath() %>/proc/studentidrequest_rejectReqSID.jsp",
		  data:  {
			  "reqnum" : lectureArray[0]
			  },
		  //async: false,
		  dataType : "json",
		  success: function(success) {
			
			  if(success) { // 전송 완료 시.
				  if(success.error != null) { // 실패
					  alert(success.error);
				  }
				  else {
					  alert("학번요청 거절 완료되었습니다.");
					  getList();
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
   <button type="button" class="logout_button" id="logoutButton" onclick="logout()">로그아웃</button>
        <span id="nowLoginSID">관리자</span>
        
 </div>
 <!-- 좌측 메뉴 공간 -->
   <div id="adminMenu">
    <ul id="menu">
        <li id="first">학번 부여</li>
        <li>휴복학 승인</li>
        <li>교과목 추가</li>
        <li>장학 추가</li>
        <li>분반 추가</li>
        <li>장학 수여</li>
        <li>교수 추가</li>
        <li>성적 부여</li>
	</ul>
   </div>
   <!-- 검색 라인 -->
     <div id="searchArea" class="searchArea">
         <input type="text" name="SubjectName" id="inputDepartmentCode">
       </div>
       <!-- 주요화면 -->
   <div id="mainArea">
   		<div id="numContext" class="context">요청번호</div>
   		<div id="dateContext" class="context">요청일자</div>
   		<div id="idContext" class="context">아이디</div>
   		<div id="whitespaceContext" class="context"></div>
   		<div id="subArea">
   		</div>
   		</div>
</body>
</html>