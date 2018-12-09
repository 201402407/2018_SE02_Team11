<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>SE02_Team11</title>
  <link href="<%=request.getContextPath() %>/css/ThisSemesterSubjectByDCode.css?ver=1" rel="stylesheet" type="text/css">
  <script src="http://code.jquery.com/jquery-1.6.2.min.js"></script>
  <script src="http://code.jquery.com/ui/1.8.23/jquery-ui.min.js"></script>
  <script>
  function createTable(){
	     
	    $.ajax({
	    	url: "<%=request.getContextPath() %>/proc/subject_getThisSemesterSubjectByDCode.jsp",
	        data : {
	        	
	        },
	        type : 'post',
	        success : function(data){
	            var results = data.boardList;
	            var str = '<TR>';
	            $.each(results , function(i){
	                str += '<TD>' + results[i].bdTitl + '</TD><TD>' + results[i].bdWriter + '</TD><TD>' + results[i].bdRgDt + '</TD>';
	                str += '</TR>';
	           });
	           $("#boardList").append(str); 
	        },
	        error : function(){
	            alert("error");
	        }
	    });
	}
  
  function getDCode() {
	  $.ajax({
	    	url: "<%=request.getContextPath() %>/proc/subject_getThisSemesterSubjectByDCode.jsp",
	        data : {
	        	
	        },
	        type : 'post',
	        success : function(data){
	            var results = data.boardList;
	            var str = '<TR>';
	            $.each(results , function(i){
	                str += '<TD>' + results[i].bdTitl + '</TD><TD>' + results[i].bdWriter + '</TD><TD>' + results[i].bdRgDt + '</TD>';
	                str += '</TR>';
	           });
	           $("#boardList").append(str); 
	        },
	        error : function(){
	            alert("error");
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
   <table id="subjectTable" border="1px">
   		<tr>
   			<th>과목명</th>
   			<th>과목코드</th>
   		</tr>
   </table>
</body>
</html>