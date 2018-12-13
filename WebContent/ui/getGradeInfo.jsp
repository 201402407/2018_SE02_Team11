<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@page import="Util.OurTimes"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>SE02_Team11</title>
  <link href="<%=request.getContextPath() %>/css/getGradeInfo.css?ver=1" rel="stylesheet" type="text/css">
  <script src="http://code.jquery.com/jquery-1.6.2.min.js"></script>
  <script src="http://code.jquery.com/ui/1.8.23/jquery-ui.min.js"></script>
  <script>
  /* 각 강의평가 담을 Array 선언 */
  var gradeList = []; // 각 강의평가별 배열 선언 
  
  $(document).ready(function(){
	    jQuery.ajaxSettings.traditional = true;
	    getTermSelectBox();
	   // getGradeList($("#TermBox option:selected").val());
	    $(function(){
			$("#TermBox").change(function() {
				getGradeList($("#TermBox option:selected").val());
			})
		})
		
	    $("#menu").children().eq(6).css("background-color", "#00649F");
	    $("#menu").children().eq(6).css("color", "white");

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

	/* 학기 선택 박스 옵션 생성 */
	function getTermSelectBox() {
		var date = new Date();
		var year = date.getFullYear();
		var month = date.getMonth() + 1;
		var term = getTerm(month);
		
		for(var i = 18; i > 3; i--) { // /3을 통해 계산. 16까지면 최대 10학기를 구분.
			// 처음 시작 시
			if(i == 18) {
				if(term == 1)	i = 17; // 2감소시킴(continue를 통해 1이 더 감소)
				continue;
			}
			if(i % 3 == 0) { // 1학기, 2학기도 지나면
				year--; // 1년 감소
				continue;
			}
			term = i % 3;
			var temp = year.toString().concat(term.toString());
			// 추가
			console.log(i);
			$("#TermBox").append("<option value='"+ temp + "'>"+ year + "/" + term + "학기</option>")
		}
	}
	
	/* 학기를 리턴하는 함수 */
	function getTerm(month) {
		if(month < 9 && month >= 3) // 1학기
			return 1;
		else
			return 2;
	}
	
  /* 성적리스트 가져오기 */
  function getGradeList(term) {
	  console.log(term);
	  $.ajax({
		  type: 'post',
		  url: "<%=request.getContextPath() %>/proc/attendance_getGradeInfo.jsp",
		  data:  {
			  "sid" : <%=session.getAttribute("sid") %>,
		  	  "semester" : term
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
					alert("성공");
					 /* 성적리스트 출력 */
					  $.each(temp, function(key, arrjson) {
						  	// 성적 정보 넣기.
						  	var tempArray = [];
						  	
						  	tempArray.push(arrjson.isVisible);
						  	tempArray.push(arrjson.subjectName);
						  	tempArray.push(arrjson.grade);
						  	tempArray.push(arrjson.isRetake);
						  	tempArray.push(arrjson.gradeBefore);
						  	
						  	gradeList.push(tempArray); // 푸시
						  	
						});
						
					 displayGradeList(gradeList);
					 
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
  function displayGradeList(list) {
	  
	  // 전체 row 갯수
	  for(var i=0; i < list.length; i++) {
		  $("#tablebody").append("<tr class='listRow' id='listIndex" + i + "'></tr>"); // tr 생성
		  // 해당하는 row의 column 갯수
		  for(var j = 0; j < list[i].length; j++) {
			  if(list[i][0]) {
				  if(j == 2 || j == 4)
					  $("#listIndex" + i).append("<td></td>");
					  continue;
			  }
			  	$("#listIndex" + i).append("<td>"+ list[i][j] + "</td>");

		  }
	  }
	  
	  /* 클릭 이벤트 생성 */
	  $(".request").click(function(event){
		  isLectureEvaluation($(this).val());
		});
	
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
   <div id="selectArea" class="select">
				<select id="TermBox" class="area">
				<option value="not" disabled selected>원하는 학기를 선택하세요.</option>
				</select>
   </div>
   <div id="tableArea">
   		<table id="gradeTable">

		<thead>
        <tr align="center" id="title1"> 
            <td width="150" bgcolor="#00649F" rowspan="2">과목명</td>
            <td width="60" bgcolor="#00649F" rowspan="2">평점</td>
            <td width="70" colspan="2" bgcolor="#00649F">재수강여부</td>            
        </tr>
        <tr align="center" id="title2"> 
            <td width="70" bgcolor="#00649F">과목명</td>
            <td width="100" bgcolor="#00649F">평점</td>
        </tr>
        </thead>
        <tbody id="tablebody">
        </tbody>
    </table>
   </div>
</body>
</html>