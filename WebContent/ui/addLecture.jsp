<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>SE02_Team11</title>
  <link href="<%=request.getContextPath() %>/css/addLecture.css?ver=1" rel="stylesheet" type="text/css">
  <script src="http://code.jquery.com/jquery-1.6.2.min.js"></script>
  <script src="http://code.jquery.com/ui/1.8.23/jquery-ui.min.js"></script>
  <script>
  $(document).ready(function(){
	    jQuery.ajaxSettings.traditional = true;

	    $("#menu").children().eq(4).css("background-color", "#00649F");
	    $("#menu").children().eq(4).css("color", "white");
	    
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
			location.href = "addLecture.jsp";
			break;
		case 5:
			location.href = "awardToStudent.jsp";
			break;
		case 6:
			location.href = "addProfessor.jsp";
			break;
		case 7:
			location.href = "addGrade.jsp";
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

  /* ajax 전송 */
  function add() {
	  $.ajax({
		  type: 'post',
		  url: "<%=request.getContextPath() %>/proc/lecture_addLecture.jsp",
		  data:  {
			  "subjcode" :  $("#subjcodeArea").val(),
			  "profcode" : $("#profcodeArea").val(),
			  "depcode" :  $("#depcodeArea").val(),
			  "registerterm" : $("#registertermArea").val(),
			  "allnum" :  $("#allnumArea").val(),
			  "dayofweek" : $("#dayofweekArea").val(),
			  "starttime" :  $("#starttimeArea").val(),
			  "endtime" : $("#endtimeArea").val(),
			  "syltext" : $("#syltextArea").val()
			  },
		  //async: false,
		  dataType : "json",
		  success: function(success) {
			  if(success) { // 전송 완료 시.
				  if(success.error != null) { // 실패
						alert(success.error);
				  }
				  else { // 성공
						alert("정상적으로 추가되었습니다.");
						location.href = "addLecture.jsp";
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
   <div id="inputArea">
	 	<div id="firstline">
	 			과목코드<input type="text" id="subjcodeArea" class="area">
	 			교수등록번호<input type="text" id="profcodeArea" class="area">
	 	</div>
	 	<div id="secondline">
	 			학과코드<input type="text" id="depcodeArea" class="area">
	 			등록학기<input type="text" id="registertermArea" class="area">
	 	</div>
	 	<div id="thirdline">
	 			전체인원<input type="text" id="allnumArea" class="area">
	 			강의 요일<input type="text" id="dayofweekArea" class="area">
	 	</div>
	 	<div id="fourthline">
	 			강의 시작시간<input type="text" id="starttimeArea" class="area">
	 			강의 종료시간<input type="text" id="endtimeArea" class="area">
	 	</div>
	 	<div id="fifthline">
	 			<textarea id="syltextArea" placeholder="강의계획서를 입력하세요" class="area"></textarea>
	 	</div>
	 </div>
	 <button type="button" class="button" id="addButton" onclick="add()">등록</button>
</body>
</html>