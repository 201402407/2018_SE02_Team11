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
  /* �� ������ ���� Array ���� */
  var evaluationList = []; // �� �����򰡺� �迭 ���� 
  
  $(document).ready(function(){
	    jQuery.ajaxSettings.traditional = true;
	    getAttendanceList();
	    
	    $("#menu").children().eq(5).css("background-color", "#00649F");
	    $("#menu").children().eq(5).css("color", "white");
	    	    
	    window.addEventListener("message", messageHandler, true);
	    /* �ʱ� �˾�â ����� */
	    document.getElementById("popup").style.display = "none";

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
	
  function dayOfWeekToKor(dayofweek) {
	switch(dayofweek) {
	case "MONDAY":		return "������";
	case "TUESDAY":		return "ȭ����";
	case "WEDNESDAY":	return "������";
	case "THURSDAY":	return "�����";
	case "FRIDAY":		return "�ݿ���";
		default:		return -1;
	}  
  }
  
  function retakeToString(isRetake) {
	  if(isRetake)	return "Yes";
	  else			return "No";
  }
  

	/**
	 * registerTerm �� ���ڷ� �޾� �б⸦ ���.
	 * ex) 20181 �̸� 1�б�, 20182�̸� 2�б⸦ ����
	 *  */
	function getTerm(registerTerm) {
		// 1�̸�
		if(registerTerm.toString().substring(4) == "1") {
			return "1�б�";
		}
		// 2�̸�
		if(registerTerm.toString().substring(4) == "2") {
			return "2�б�";
		}
		return null;
	}
	 
  /* ��������Ʈ �������� */
  function getAttendanceList() {
	  $.ajax({
		  type: 'post',
		  url: "<%=request.getContextPath() %>/proc/attendance_getAttendanceListBySID.jsp",
		  data:  {
			  "sid" : <%=session.getAttribute("sid") %>
			  },
		  //async: false,
		  dataType : "json",
		  success: function(success) {
			 // alert(success);
			  if(success) { // ���� �Ϸ� ��.
				  if(success.error != null) { // ����
					  alert(success.error);
				  }
				  else {
					var temp = success.data;
					
					  /* ��������Ʈ ��� */
					  $.each(temp, function(key, arrjson) {
						  	// �������� ���� �ֱ�.
						  	var tempArray = [];
						  	
						  	tempArray.push(arrjson.subjectName);
						  	tempArray.push(getTerm(arrjson.registerTerm));
						  	tempArray.push(retakeToString(arrjson.isRetake));
						  	tempArray.push(dayOfWeekToKor(arrjson.dayOfWeek));
						  	tempArray.push(arrjson.startTime);
						  	tempArray.push(arrjson.endTime);
						  	tempArray.push(arrjson.score);
						  	tempArray.push(arrjson.attNum);
						  	
						  	evaluationList.push(tempArray); // Ǫ��
						  	
						});
						
					 displayAttendanceList(evaluationList);
					 
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
  
  /* ȭ�鿡 ��� */
  function displayAttendanceList(list) {
	  
	  // ��ü row ����
	  for(var i=0; i < list.length; i++) {
		  $("#tablebody").append("<tr class='listRow' id='listIndex" + i + "'></tr>"); // tr ����
		  // �ش��ϴ� row�� column ����
		  for(var j = 0; j < list[i].length; j++) {
				if(j + 1 == list[i].length) {
					$("#listIndex" + i).append('<button type="button" value="'+ list[i][7] + '" class="request" id="btn' + i + '">��</button>');
					break;
			  	}
				
			  	$("#listIndex" + i).append("<td>"+ list[i][j] + "</td>");

		  }
	  }
	  
	  /* Ŭ�� �̺�Ʈ ���� */
	  $(".request").click(function(event){
		  isLectureEvaluation($(this).val());
		});
	
  }

  /* �����򰡿��� Ȯ�� */
  function isLectureEvaluation(attNum) {
	  $.ajax({
		  type: 'post',
		  url: "<%=request.getContextPath() %>/proc/attendance_getLectureEvaluationByCode.jsp",
		  data:  {
			  "attNum" : attNum
			  },
		  //async: false,
		  dataType : "json",
		  success: function(success) {
			  if(success) { // ���� �Ϸ� ��.
				  if(success.error != null) { // ����
					  alert(success.error);
				  }
				  else {
					  if(success.data) {
						  alert("�̹� �����򰡰� �Ϸ�� �����Դϴ�.");
						  return;
					  }
					  document.getElementById("popup").style.display = "block";
					  sendMessage(attNum);
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
  
  function sendMessage(obj) {
	  document.getElementById("popup").contentWindow.postMessage(obj,"*");
	}
  
  function messageHandler(e) {
	  /* �˾�â���� �ݴ� ��û�� ������ */
	  if(e.data == "close") {
		  document.getElementById("popup").style.display = "none";
	  }
  }

  </script>
</head>
<body>
<!-- �˾�â -->
	<div id="myModal">
        <iframe id="popup" src="writeEvaluationPopup.html" class="popup"></iframe>
  </div>
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
   		<table id="evaluationTable">

		<thead>
        <tr align="center" id="title"> 
            <td width="150" bgcolor="#00649F">�����</td>
            <td width="60" bgcolor="#00649F">����б�</td>
            <td width="70" bgcolor="#00649F">���������</td>
            <td width="100" bgcolor="#00649F">���ǿ���</td>
            <td width="100" bgcolor="#00649F">���� ���۽ð�</td>
            <td width="100" bgcolor="#00649F">���� ����ð�</td>
            <td width="70" bgcolor="#00649F">����</td>
            <td width="70" bgcolor="#00649F"></td>
        </tr>
        </thead>
        <tbody id="tablebody">
        </tbody>
    </table>
   </div>
</body>
</html>