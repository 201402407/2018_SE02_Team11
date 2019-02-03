<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>SE02_Team11</title>
  <link href="<%=request.getContextPath() %>/css/addGrade.css?ver=1" rel="stylesheet" type="text/css">
  <script src="http://code.jquery.com/jquery-1.6.2.min.js"></script>
  <script src="http://code.jquery.com/ui/1.8.23/jquery-ui.min.js"></script>
  <script>
  /* 결과 담을 Array 선언 */
  var list = []; // 결과담을 배열 선언 
  var col; // 선택한 행 인덱스
  $(document).ready(function(){
	    jQuery.ajaxSettings.traditional = true;
	    
	    $("#menu").children().eq(7).css("background-color", "#00649F");
	    $("#menu").children().eq(7).css("color", "white");
	    
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

  /* 휴복학 문자 변경 */
  function retakeToKor(changetype) {
	  if(changetype)	return "Yes";
	  if(!changetype)	return "No";
  }
  
  /* 검색 */
  function search()	{
	  
	  if($("#inputLectureCode").val() == null) {
		  return;
	  }
	  
	  $.ajax({
	    	type : 'post',
	    	url: "<%=request.getContextPath() %>/proc/attendance_getAttendanceListbyLCode.jsp",
	        data : {
	        	"lcode" : $("#inputLectureCode").val()
	        },
	        dataType : "json",
			  success: function(success) {
				  alert(success);
				  alert(success.data);
				  if(success) { // 전송 완료 시.
					  if(success.error != null) { // 실패
						  alert(success.error);
						  $("#inputLectureCode").html(" "); // 공백으로 바꿈
					  }
					  else {
						 var temp = success.data;
						alert(temp);
						 /* 수강리스트 출력 */
						 // 순서 : subjectName, attendanceNum, isRetake, studentID, studentName
						  $.each(temp, function(key, arrjson) {
							  	// 수강과목 정보 넣기.
							  	var tempArray = [];
							  	alert(arrjson.subjectName);
							  	
							  	tempArray.push(arrjson.subjectName);
							  	tempArray.push(arrjson.attendanceNum);
							  	tempArray.push(retakeToKor(arrjson.isRetake));
							  	tempArray.push(arrjson.studentID);
							  	tempArray.push(arrjson.studentName);
							  	
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
	  $($("#tablebody")).empty(); // 테이블 초기화
	  // 전체 row 갯수
	  for(var i=0; i < list.length; i++) {
		  $("#tablebody").append("<tr class='listRow' id='listIndex" + i + "'></tr>"); // tr 생성
		  
		  // 해당하는 row의 column 갯수
		  $("#listIndex" + i).append("<td>"+ list[i][0] + "</td>"); // 과목명
		  $("#listIndex" + i).append("<td align='center'>"+ list[i][1] + "</td>"); // 수강번호
		  $("#listIndex" + i).append("<td align='center'>"+ list[i][2] + "</td>"); // 재수강여부
		  $("#listIndex" + i).append("<td align='center'>"+ list[i][3] + "</td>"); // 학번
		  $("#listIndex" + i).append("<td>"+ list[i][4] + "</td>"); // 학생이름
	  }
	  
	  
	  // 행 클릭 시
	  $("tbody tr").click(function() {
		  $("tbody tr").css("background-color", "#F1F1F1"); // 기존 것들 전부 초기화
		  $("tbody tr").css("color", "#707070"); // 기존 것들 전부 초기화
		  col = $(this).index();
		  // 클릭
		  $(this).css("background-color", "#003453");
		  $(this).css("color", "white");
		  alert(col);
			// 평점 활성화
		  $("#gradeBox").show();
			
			$("#addButton").click(function() {
				if(col == null) {
					alert("원하시는 수강 리스트를 우선 선택하세요!");
				}
				if($("#gradeBox option:selected").val() == null) {
					alert("학점을 선택하세요!");
				}
				
				var attendancenum = list[col][1];
				var grade = $("#gradeBox option:selected").val();
				alert(col + ", " + grade);
				alert(list[col][1]);
				add(attendancenum, grade);
			});
	  });
	  
  }
  
  /* 성적등록 */
  function add(attnum, grade) {
	  $.ajax({
		  type: 'post',
		  url: "<%=request.getContextPath() %>/proc/gradeinfo_addGrade.jsp",
		  data:  {
			  "attendancenum" : attnum,
			  "grade" : grade
			  },
		  //async: false,
		  dataType : "json",
		  success: function(success) {
			  if(success) { // 전송 완료 시.
				  if(success.error != null) { // 실패
					  alert(success.error);
				  }
				  else {
					  alert("성적부여가 완료되었습니다.");
					  $("#tablebody").empty(); // 테이블 초기화
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
   <!-- 검색 -->
   <div id="searchArea" class="searchArea">
         <input type="text" name="LectureCode" id="inputLectureCode">
		<button type="button" class="button" name="searchButton" id="searchButton" onclick="search()">조회</button>         
       </div>
	 <div id="tableArea">
   		<table id="gradeTable">
		<thead>
        <tr align="center" id="title"> 
            <td width="150" bgcolor="#00649F">과목명</td>
            <td width="120" bgcolor="#00649F">수강번호</td>
            <td width="120" bgcolor="#00649F">재수강여부</td>
            <td width="150" bgcolor="#00649F">학번</td>
            <td width="130" bgcolor="#00649F">학생이름</td>
        </tr>
        </thead>
        <tbody id="tablebody">
        </tbody>
    </table>
   </div>
   <div id="boxArea">
   	<select id="gradeBox" class="box">
	 	<option value='4.5'>A+</option>
	 	<option value='4.0'>A0</option>
	 	<option value='3.5'>B+</option>
	 	<option value='3.0'>B0</option>
	 	<option value='2.5'>C+</option>
	 	<option value='2.0'>C0</option>
	 	<option value='1.5'>D+</option>
	 	<option value='1.0'>D0</option>
	</select>
   </div>
   <button type="button" class="button" id="addButton" onclick="add()">등록</button>
</body>
</html>