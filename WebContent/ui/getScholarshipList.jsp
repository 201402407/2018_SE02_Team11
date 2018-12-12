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
  /* 각 장학내역 담을 Array 선언 */
  var scholarshipList = []; // 각 장학내역별 배열 선언 
  
  $(document).ready(function(){
	    jQuery.ajaxSettings.traditional = true;
	    getScholarshipList();
	    
	    $("#menu").children().eq(8).css("background-color", "#00649F");
	    $("#menu").children().eq(8).css("color", "white");
	    	    
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
	 
  /* 전체장학리스트 가져오기 */
  function getScholarshipList() {
	  $.ajax({
		  type: 'post',
		  url: "<%=request.getContextPath() %>/proc/scholarship_getScholarshipList.jsp",
		  data:  {
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
						  	
						  	tempArray.push(arrjson.schoName);
						  	tempArray.push(arrjson.schoNum);
						  	
						  	scholarshipList.push(tempArray); // 푸시
						  	
						});
						
					 displayScholarshipList(scholarshipList);
					 
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
  function displayScholarshipList(list) {
	  
	  // 전체 row 갯수
	  for(var i=0; i < list.length; i++) {
		  $("#tablebody").append("<tr class='listRow' id='listIndex" + i + "'></tr>"); // tr 생성
		  // 해당하는 row의 column 갯수
		  $("#listIndex" + i).append("<td>"+ list[i][0] + "</td>"); // 장학이름
		  $("#listIndex" + i).append("<td>"+ list[i][1] + "</td>"); // 장학금
	  }
	  
  }

  </script>
</head>
<body>
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
            <td width="320" bgcolor="#00649F">장학 이름</td>
            <td width="130" bgcolor="#00649F">장학 번호</td>
        </tr>
        </thead>
        <tbody id="tablebody">
        </tbody>
    </table>
   </div>
</body>
</html>