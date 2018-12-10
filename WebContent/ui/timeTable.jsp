<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>SE02_Team11</title>
  <link href="<%=request.getContextPath() %>/css/timeTable.css?ver=1" rel="stylesheet" type="text/css">
  <script src="http://code.jquery.com/jquery-1.6.2.min.js"></script>
  <script src="http://code.jquery.com/ui/1.8.23/jquery-ui.min.js"></script>
  <script>
  /* 각 요일별 Array 선언 */
  var MON = new Array();
  var TUE = new Array();
  var WED = new Array();
  var THU = new Array();
  var FRI = new Array();
  var day_list = [MON, TUE, WED, THU, FRI]; // 각 요일별 배열 선언 
  
  $(document).ready(function(){
	    jQuery.ajaxSettings.traditional = true;
	    getTimeList();
  });
  
  function dayOfWeekToInt(dayofweek) {
	switch(dayofweek) {
	case "MONDAY":		return 0;
	case "TUESDAY":		return 1;
	case "WEDNESDAY":	return 2;
	case "THURSDAY":	return 3;
	case "FRIDAY":		return 4;
		default:		return -1;
	}  
  }
  
  function getTimeList() {
	  $.ajax({
			
		  type: 'post',
		  url: "<%=request.getContextPath() %>/proc/attendance_getAttendanceListBySID.jsp",
		  data:  {
			  "sid" : <%=session.getAttribute("sID") %>
			  },
		  //async: false,
		  dataType : "json",
		  success: function(success) {
			  alert(success);
			  if(success) { // 전송 완료 시.
				  if(success.error != null) { // 실패
					  alert(success.error);
				  }
				  else {
					  /* 요일에 맞게 과목명과 시작시간, 종료시간 넣기 */
					  $.each(success.arrjson, function(index, arrjson) {
						  	var dayInt = dayOfWeekToInt(arrjson.dayOfWeek); // index로 변환
						  	// 과목명과 시작시간, 종료시간 넣기.
						  	var starttimesplit[3] = arrjson.startTime.split(":");
						  	var endtimesplit[3] = arrjson.startTime.split(":");
						  	var starttime = starttimesplit[0] - 8;
						  	var endtime = endtimesplit[0] - 8;
						  	if(starttimesplit[1] == "30") {
						  		starttime += 0.5;
						  	}
						  	if(endtimesplit[1] == "30") {
						  		endtime += 0.5;
						  	}
						  	day_list[dayInt].push(arrjson.subjectName);
						  	day_list[dayInt].push(starttime);
						  	day_list[dayInt].push(endtime);
						});
					  // 강의 별 색깔의 차이를 두어 구별
					  var color = ["red" , "yellow", "green", "blue", "pink", "brown", "orange"];
					 // 처음 9시부터니까 -9를 한 뒤 :로 스플릿 해서 뒤에 게 30이면 0.5로 치환
					 // 다음 다 합쳐서 그거 * 2한 값의 id로 가서 해당 요일에 index 색칠
					 
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
   <button type="button" class="logout_button" id="logoutButton">로그아웃</button>
        <span id="nowLoginSID"><%=session.getAttribute("sID") %></span>
        
 </div>
 <!-- 좌측 메뉴 공간 -->
   <div id="studentMenu">
     <ul>
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
   <div id="currentTerm"></div>
   		<table id="timeTable" cellspacing="5" align="center" border="1">

        <tr align="center" id="1"> 
            <td width="50" bgcolor="#D4DDE2"></td>
            <td width="100" bgcolor="#D4DDE2">월</td>
            <td width="100" bgcolor="#D4DDE2">화</td>
            <td width="100" bgcolor="#D4DDE2">수</td>
            <td width="100" bgcolor="#D4DDE2">목</td>
            <td width="100" bgcolor="#D4DDE2">금</td>
        </tr>
        <tr align="center" id="2">
            <td bgcolor="#D4DDE2">1</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
 		<tr align="center" id="3">
            <td bgcolor="#D4DDE2">1.5</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr align="center" id="4">
            <td bgcolor="#D4DDE2">2</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr align="center" id="5">
            <td bgcolor="#D4DDE2">2.5</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr align="center" id="6">
            <td bgcolor="#D4DDE2">3</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr align="center" id="7">
            <td bgcolor="#D4DDE2">3.5</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr align="center" id="8">
            <td bgcolor="#D4DDE2">4</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr align="center" id="9">
            <td bgcolor="#D4DDE2">4.5</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr align="center" id="10">
            <td bgcolor="#D4DDE2">5</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr align="center" id="11">
            <td bgcolor="#D4DDE2">5.5</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr align="center" id="12">
            <td bgcolor="#D4DDE2">6</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr align="center" id="13">
            <td bgcolor="#D4DDE2">6.5</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr align="center" id="14">
            <td bgcolor="#D4DDE2">7</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr align="center" id="15">
            <td bgcolor="#D4DDE2">7.5</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr align="center" id="16">
            <td bgcolor="#D4DDE2">8</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr align="center" id="17">
            <td bgcolor="#D4DDE2">8.5</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr align="center" id="18">
            <td bgcolor="#D4DDE2">9</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr align="center" id="19">
            <td bgcolor="#D4DDE2">9.5</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
    </table>
   </div>
</body>
</html>