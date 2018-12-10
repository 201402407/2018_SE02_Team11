<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>SE02_Team11</title>
  <link href="<%=request.getContextPath() %>/css/thisSemesterSubjectBySID.css?ver=1" rel="stylesheet" type="text/css">
  <script src="http://code.jquery.com/jquery-1.6.2.min.js"></script>
  <script src="http://code.jquery.com/ui/1.8.23/jquery-ui.min.js"></script>
  <script>
  $(document).ready(function(){
	    jQuery.ajaxSettings.traditional = true;
	
	    createTable();
	    
	    $("#menu").children().eq(2).css("background-color", "#00649F");
	    $("#menu").children().eq(2).css("color", "white");
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
  function createTable(){
	     
	    $.ajax({
	    	type : 'post',
	    	url: "<%=request.getContextPath() %>/proc/subject_getThisSemesterSubjectBySID.jsp",
	        data : {
	        	"sid" : <%=session.getAttribute("sid") %>
	        },
	        dataType : "json",
			  success: function(success) {
				  alert(success);
				  if(success) { // 전송 완료 시.
					  if(success.error != null) { // 실패
						  alert(success.error);
					  }
					  else {
						
						  var data = [
								['Name', 'Age', 'Email'],
								['John Doe', '27', 'john@doe.com'],
								['Jane Doe', '29', 'jane@doe.com']
							];

							var table = arrayToTable(data, {
								thead: true,
								attrs: {class: 'table'}
							})

							$('#subjectTable').append(table);
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
  
  var arrayToTable = function (data, options) {

	    "use strict";

	    var table = $('<table />'),
	        thead,
	        tfoot,
	        rows = [],
	        row,
	        i,
	        j,
	        defaults = {
	            th: true, // should we use th elemenst for the first row
	            thead: false, //should we incldue a thead element with the first row
	            tfoot: false, // should we include a tfoot element with the last row
	            attrs: {} // attributes for the table element, can be used to
	        };

	    options = $.extend(defaults, options);

	    table.attr(options.attrs);

	    // loop through all the rows, we will deal with tfoot and thead later
	    for (i = 0; i < data.length; i = i + 1) {
	        row = $('<tr />');
	        for (j = 0; j < data[i].length; j = j + 1) {
	            if (i === 0 && options.th) {
	                row.append($('<th />').html(data[i][j]));
	            } else {
	                row.append($('<td />').html(data[i][j]));
	            }
	        }
	        rows.push(row);
	    }

	    // if we want a thead use shift to get it
	    if (options.thead) {
	        thead = rows.shift();
	        thead = $('<thead />').append(thead);
	        table.append(thead);
	    }

	    // if we want a tfoot then pop it off for later use
	    if (options.tfoot) {
	        tfoot = rows.pop();
	    }

	    // add all the rows
	    for (i = 0; i < rows.length; i = i + 1) {
	        table.append(rows[i]);
	    }

	    // and finally add the footer if needed
	    if (options.tfoot) {
	        tfoot = $('<tfoot />').append(tfoot);
	        table.append(tfoot);
	    }

	    return table;
	};
	
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
   <div id="subjectTable">
   </div>
</body>
</html>