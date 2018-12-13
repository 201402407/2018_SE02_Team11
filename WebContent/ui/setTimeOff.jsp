<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>SE02_Team11</title>
  <link href="<%=request.getContextPath() %>/css/setTimeOff.css?ver=1" rel="stylesheet" type="text/css">
  <script src="http://code.jquery.com/jquery-1.6.2.min.js"></script>
  <script src="http://code.jquery.com/ui/1.8.23/jquery-ui.min.js"></script>
  <script>
  /* 결과 담을 Array 선언 */
  var list = []; // 결과담을 배열 선언 
  
  $(document).ready(function(){
	    jQuery.ajaxSettings.traditional = true;
	    getList();
	    
	    $("#menu").children().eq(1).css("background-color", "#00649F");
	    $("#menu").children().eq(1).css("color", "white");
	    
	    /* 관리자 확인 */
	    if(<%=session.getAttribute("isAdmin") == null %>) {
	    	alert("관리자로 로그인 되어있지 않습니다!");
	    	location.href = "login.jsp";
	    }
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
			location.href = "getApplyLectureList.jsp";
			break;
		case 5:
			location.href = "getGradeInfo.jsp";
			break;
		case 6:
			location.href = "addProfessor.jsp";
			break;
		case 7:
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

  /* 리스트출력 */
  function getList() {
	  $("#tablebody").empty(); // 초기화
	    $.ajax({
	    	type : 'post',
	    	url: "<%=request.getContextPath() %>/proc/timeoffrequest_getTimeoffRequestList.jsp",
	        data : {
	        },
	        dataType : "json",
			  success: function(success) {
				  alert(success);
				  alert(success.data);
				  if(success) { // 전송 완료 시.
					  if(success.error != null) { // 실패
						  alert(success.error);
					  }
					  else {
						 var temp = success.data;
						//location.href = "../proc/timeoffrequest_getTimeoffRequestList.jsp";
						alert(temp);
						 /* 수강리스트 출력 */
						  $.each(temp, function(key, arrjson) {
							  	// 수강과목 정보 넣기.
							  	var tempArray = [];
							  	alert(arrjson.changeType);
							  	console.log(arrjson.changeType);
							  	tempArray.push(arrjson.reqNum);
							  	tempArray.push(arrjson.reqDate);
							  	tempArray.push(arrjson.changeType);
							  	tempArray.push(arrjson.startSemester);
							  	tempArray.push(arrjson.endSemester);
							  	tempArray.push(arrjson.reason);
							  	
							  	list.push(tempArray); // 푸시
							  	
							});
							
						
						printlist(list);
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
  /* 리스트 출력 */
  function printlist(list) {
	  $($("#reasonText")).html(" ");
	// 전체 row 갯수
	  for(var i=0; i < list.length; i++) {
		  $("#tablebody").append("<tr class='listRow' id='listIndex" + i + "'></tr>"); // tr 생성
		  
		  if(i+1 == list.length) { // 맨 끝열
			  $("#reasonText").html(list[i][5]);
			  $("#listIndex" + i).append('<button type="button" value="'+ i + '" class="permit" id="permitbtn' + i + '">승인</button>');
			  $("#listIndex" + i).append('<button type="button" value="'+ i + '" class="reject" id="rejectbtn' + i + '">거절</button>');
		  }
		  // 해당하는 row의 column 갯수
		  $("#listIndex" + i).append("<td>"+ list[i][0] + "</td>"); // 요청번호
		  $("#listIndex" + i).append("<td align='center'>"+ list[i][1] + "</td>"); // 요청일자
		  $("#listIndex" + i).append("<td align='center'>"+ list[i][2] + "</td>"); // 휴복학구분
		  $("#listIndex" + i).append("<td align='center'>"+ list[i][3] + "</td>"); // 시작학기
		  $("#listIndex" + i).append("<td align='center'>"+ list[i][4] + "</td>"); // 종료학기
		  
	  }
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
	 <div id="tableArea">
   		<table id="timeoffTable">

		<thead>
        <tr align="center" id="title"> 
            <td width="100" bgcolor="#00649F">요청번호</td>
            <td width="180" bgcolor="#00649F">요청일자</td>
            <td width="90" bgcolor="#00649F">휴복학구분</td>
            <td width="120" bgcolor="#00649F">시작학기</td>
            <td width="120" bgcolor="#00649F">종료학기</td>
            <td width="180" bgcolor="#00649F"></td>
        </tr>
        </thead>
        <tbody id="tablebody">
        </tbody>
    </table>
   </div>
   <div id="reasonArea">
   	<div id="reasonTitle">
   	</div>
   	<div id="reasonTextArea">
   		<div id="reasonText"></div>
   	</div>
   </div>
</body>
</html>