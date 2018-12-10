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
	    
	    <% if(OurTimes.currentTerm() == 0){
	    	%> 
	    	$("#currentTerm").html("�б����� �ƴմϴ�.");
	    	<%
	    }
	    else {
	    	String year = String.valueOf(OurTimes.currentTerm()).substring(0, 4);
	    	String term = String.valueOf(OurTimes.currentTerm()).substring(4);
	    	%> 
	    	$("#currentTerm").html(<%= year %> + "��/" + <%= term %> + "�б�");
	    	<%
	    }
	    %>
	    
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
			  if(success) { // ���� �Ϸ� ��.
				  if(success.error != null) { // ����
					  alert(success.error);
				  }
				  else {
					  /* ���Ͽ� �°� ������ ���۽ð�, ����ð� �ֱ� */
					  $.each(success.arrjson, function(index, arrjson) {
						  	var dayInt = dayOfWeekToInt(arrjson.dayOfWeek); // index�� ��ȯ
						  	// ������ ���۽ð�, ����ð� �ֱ�.
						  	var starttimesplit = arrjson.startTime.split(":");
						  	var endtimesplit = arrjson.startTime.split(":");
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
					  // ���� �� ������ ���̸� �ξ� ����
					  var color = ["red" , "yellow", "green", "blue", "pink", "brown", "orange"];
					 // ó�� 9�ú��ʹϱ� -9�� �� �� :�� ���ø� �ؼ� �ڿ� �� 30�̸� 0.5�� ġȯ
					 // ���� �� ���ļ� �װ� * 2�� ���� id�� ���� �ش� ���Ͽ� index ��ĥ
					 searchList(day_list, color);
					 mergeTable();
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
	        	while(index <= end) {
	        		
	        		$("#" + index).children().eq(i+1).css("bgcolor", color[i]);
		        	$("#" + index).children().eq(i+1).html(subject);
		        	index++;
	        	}
	        	j += 3;
	        }
	      }
	    }
  
  function mergeTable() {
	  var mergeItem = ""; // ������ ����
	  var mergeCount = 0; // ������ ����
	  var mergeRowNum = 0; // rowSpan ������ tr index ����.
	  
	  $('tr', 'table').each(function(row) {
		  if(row > 2) {
			  var thisTr = $(this);
			  var item = $(':first-child',thisTr).html();
			  
			  if(mergeItem != item) {
				  mergeCount = 1;
				  mergeItem = item;
				  mergeRowNum = Number(row);
			  }
			  else {
				  mergeCount = Number(mergeCount) + 1;
				  $("tr:eq("+mergeRowNum+") > td:first-child").attr("rowspan", mergeCount);
				  $("td:first-child", thisTr).remove(); // �����ؼ� �ش� ���� ù ��° ����
			  }
		  }
	  })
  }

  </script>
</head>
<body>
<!-- Ȩ�������� �޴� �� -->
   <div id="header">
   <button type="button" class="logout_button" id="logoutButton">�α׾ƿ�</button>
        <span id="nowLoginSID"><%=session.getAttribute("sID") %></span>
        
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