<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@page import="Util.OurTimes"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>SE02_Team11</title>
  <link href="<%=request.getContextPath() %>/css/timeTable.css?ver=1" rel="stylesheet" type="text/css">
  <script src="http://code.jquery.com/jquery-1.6.2.min.js"></script>
  <script src="http://code.jquery.com/ui/1.8.23/jquery-ui.min.js"></script>
  <script>
  /* �� ���Ϻ� Array ���� */
  var MON = new Array();
  var TUE = new Array();
  var WED = new Array();
  var THU = new Array();
  var FRI = new Array();
  var day_list = [MON, TUE, WED, THU, FRI]; // �� ���Ϻ� �迭 ���� 
  
  $(document).ready(function(){
	    jQuery.ajaxSettings.traditional = true;
	    getTimeList();
	    
	    $("#menu").children().eq(3).css("background-color", "#00649F");
	    $("#menu").children().eq(3).css("color", "white");
	    
	    /* ���� �޴� Ŭ�� �̺�Ʈ */
		$("ul li").click(function(event){
		// ���� ��尡 �� ��°���� switch�� �Ǵ��ؼ� ����
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
	    
	    
	    <%
	    String year, term;
	    if(OurTimes.isNowOnTerm()) {
	    	year = String.valueOf(OurTimes.currentTerm()).substring(0, 4);
	    	term = String.valueOf(OurTimes.currentTerm()).substring(4);
	    	}
	    else {
	    	year = String.valueOf(OurTimes.closestFutureTerm()).substring(0, 4);
	    	term = String.valueOf(OurTimes.closestFutureTerm()).substring(4);
	    	%> 
	    	<%
	    }
	    %>
	    $("#currentTerm").html(<%= year %> + "��/" + <%= term %> + "�б�");
	    
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
				
				  if(success) { // ���� �Ϸ� ��.
					  location.href = "login.jsp";
				  }
				  else {
					  alert("��� �Ŀ� �õ����ּ���.");
				  }
			  },
			  error: function(xhr, request,error) {

			  }
			});
	}
	
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
			  "sid" : <%=session.getAttribute("sid") %>
			  },
		  //async: false,
		  dataType : "json",
		  success: function(success) {
			  
			  if(success) { // ���� �Ϸ� ��.
				  if(success.error != null) { // ����
					  alert(success.error);
				  }
				  else {
					  
					  /* ���Ͽ� �°� ������ ���۽ð�, ����ð� �ֱ� */
					  $.each(success.data, function(index, arrjson) {
						  	
						  	var dayInt = dayOfWeekToInt(arrjson.dayOfWeek); // index�� ��ȯ
						  	// ������ ���۽ð�, ����ð� �ֱ�.
						  	var starttimesplit = arrjson.startTime.split(":");
						  	var endtimesplit = arrjson.endTime.split(":");
						  	var starttime = starttimesplit[0] - 8;
						  	var endtime = endtimesplit[0] - 8;
						  	if(starttimesplit[1] == "30") {
						  		starttime += 0.5;
						  	}
						  	if(endtimesplit[1] == "30") {
						  		endtime += 0.5;
						  	}
						  	//alert(starttime + " , " + endtime);
						  	
						  	/* �����, ���۽ð�, ����ð� �ֱ� */
						  	day_list[dayInt].push(arrjson.subjectName);
						  	day_list[dayInt].push(starttime);
						  	day_list[dayInt].push(endtime);
						});
					  // ���� �� ������ ���̸� �ξ� ����
					  var color = ["red" , "yellow", "green", "blue", "pink", "brown", "orange"];
					 // ó�� 9�ú��ʹϱ� -9�� �� �� :�� ���ø� �ؼ� �ڿ� �� 30�̸� 0.5�� ġȯ
					 // ���� �� ���ļ� �װ� * 2�� ���� id�� ���� �ش� ���Ͽ� index ��ĥ
					 searchList(day_list, color);
					 
				  }
			  }
			  else {
				  alert("��� �Ŀ� �õ����ּ���.");
			  }
		  },
		  error: function(xhr, request,error) {

		  }
		});
  }
  
  function searchList(day_list, color) {
	  	  // ��~�ݱ��� ������
	      for(var i = 0; i < day_list.length; i++) {
	        var temp = day_list[i];
	        var j = 0;
	        // �� ���Ϻ� ���� �� �ð� ������
	        while(j < temp.length) { // j = �����, j+1 = ���۽ð�, j+2 = ����ð�
	        	var subject = temp[j];
	        	var start = Math.floor(temp[j+1] * 2);
	        	var end = Math.floor(temp[j+2] * 2);
	        	var index = start;
	        	while(index < end) {
	        		/* ���� �� ���� �߰� */
	        		$("#" + index).children().eq(i+1).css("background-color", color[i]);
		        	$("#" + index).children().eq(i+1).html(subject);
		        	index++;
	        	}
	        	j += 3;
	        }
	       
	        $('#timeTable').rowspan(i);
	      }
	    }
  
  /* 
   * 
   * ���� ���� �ִ� ���� ������
   * 
   * ���� : $('#���̺� ID').rowspan(0);
   * 
   */   
  $.fn.rowspan = function(colIdx, isStats) {       
      return this.each(function(){      
          var that;     
          colIdx = colIdx+1;
          $('tr', this).each(function(row) {      
              $('td:eq('+colIdx+')', this).filter(':visible').each(function(col) {
                    
                  if ($(this).html() == $(that).html()
                      && (!isStats 
                              || isStats && $(this).prev().html() == $(that).prev().html()
                              )
                      ) {
                	 
                	  
                      	
                      	
              		  
              		  
                      rowspan = $(that).attr("rowspan") || 1;
                      rowspan = Number(rowspan)+1;
    
                      $(that).attr("rowspan",rowspan);
                        
                      // do your action for the colspan cell here            
                      $(this).hide();
                      //alert($(this).html());
                      //$(this).remove(); 
                      // do your action for the old cell here
                      
                	  
                  } else {            
                      that = this;         
                  }          
                    
                  // set the that if not already set
                  that = (that == null) ? this : that;      
              });     
          });    
      });  
  }; 

  </script>
</head>
<body>
<!-- Ȩ�������� �޴� �� -->
   <div id="header">
   <button type="button" class="logout_button" id="logoutButton" onclick="logout()">�α׾ƿ�</button>
        <span id="nowLoginSID"><%=session.getAttribute("sid") %></span>
        
 </div>
 <!-- ���� �޴� ���� -->
   <div id="studentMenu">
     <ul id ="menu">
        <li id="first">�������� ��ȸ</li>
        <li>�޺��� ��û</li>
        <li>���б� ����� ��ȸ</li>
        <li>��û �ð�ǥ ��ȸ</li>
        <li>������û ���� ����Ʈ ��ȸ</li>
        <li>������</li>
        <li>���� ��ȸ</li>
        <li>�л� ���г��� ��ȸ</li>
        <li>��ü ���� ���</li>
	</ul>
   </div>
   <div id="tableArea">
   <div id="currentTerm"></div>
   		<table id="timeTable" cellspacing="5" align="center" border="1">

        <tr align="center" id="1"> 
            <td width="50" bgcolor="#D4DDE2"></td>
            <td width="100" bgcolor="#D4DDE2">��</td>
            <td width="100" bgcolor="#D4DDE2">ȭ</td>
            <td width="100" bgcolor="#D4DDE2">��</td>
            <td width="100" bgcolor="#D4DDE2">��</td>
            <td width="100" bgcolor="#D4DDE2">��</td>
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