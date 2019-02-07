<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>SE02_Team11</title>
  <link href="<%=request.getContextPath() %>/css/requestTimeOnOff.css?ver=1" rel="stylesheet" type="text/css">
  <script src="http://code.jquery.com/jquery-1.6.2.min.js"></script>
  <script src="http://code.jquery.com/ui/1.8.23/jquery-ui.min.js"></script>
  <script>
  $(document).ready(function(){
	    jQuery.ajaxSettings.traditional = true;
	    window.addEventListener("message", messageHandlerReason, true);
	
	    $("#menu").children().eq(1).css("background-color", "#00649F");
	    $("#menu").children().eq(1).css("color", "white");
	    
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
	    
	    /*�б⸦ �����ϴ� �ڽ�*/
		getTermSelectBox("startSemesterArea");
		getTermSelectBox("endSemesterArea");
	    
	    /* �ʱ� �˾�â ����� */
	    document.getElementById("popup").style.display = "none";
		
		/* �޺��п� ���� �����б� �Է� ����ݱ� */
		$(function(){
			$("#isTakeOff").change(function() {
				if($("#isTakeOff option:selected").val() == "takeoff") { // ����
					document.getElementById("thirdline").style.display = "block";	
				}
				if($("#isTakeOff option:selected").val() == "resume") { // ����
					document.getElementById("thirdline").style.display = "none";	
				}
			})
		})
		
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
  
  
  
  function request() {
	  if($("#isTakeOff option:selected").val() == "takeoff") {
		  /* �˾� â�� Ȱ��ȭ ��Ű�� messageHandler Ȱ��ȭ */
		  document.getElementById("popup").style.display = "block";
	  }
	  if($("#isTakeOff option:selected").val() == "resume") {
		  requestAjax("resume", null);
	  }
  }
  
  function messageHandlerReason(e) {
	  /* �˾�â���� �ݴ� ��û�� ������ */
	  if(e.data == "close") {
		  document.getElementById("popup").style.display = "none";
	  }
	  /* ���� ���� Ȯ�� �������� */
	  else {
		  requestAjax("takeoff", e.data);
	  }
  }
  
  /* ajax ���� */
  function requestAjax(takeoff, reason) {
	  var endSemester = $("#endSemesterArea").val();
	  if(takeoff == "resume") {
		  endSemester = 99999;
	  }
	  $.ajax({
		  type: 'post',
		  url: "<%=request.getContextPath() %>/proc/changerecord_requestTimeOnOffs.jsp",
		  data:  {
			  "changeType" : takeoff,
			  "startSemester" : $("#startSemesterArea").val(),
				"endSemester" : endSemester,
				"sid" : <%=session.getAttribute("sid") %>,
				"reason" : reason // not null ����?
			  },
		  //async: false,
		  dataType : "json",
		  success: function(success) {
			  if(success) { // ���� �Ϸ� ��.
				  if(success.error != null) { // ����
						alert(success.error);
				  }
				  else { // ����
						alert("��û�Ϸ�! ������ּ���.");
						location.href = "<%=request.getContextPath() %>/ui/requestTimeOnOff.jsp";
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
  
  /* �б⸦ �����ϴ� �Լ� */
	function getTerm(month) {
		if(month < 9 && month >= 3) // 1�б�
			return 1;
		else
			return 2;
	}
  
  /* �б� ���� �ڽ� �ɼ� ���� */
	function getTermSelectBox(id) {
		var date = new Date();
		var year = date.getFullYear()+4;
		var month = date.getMonth() + 1;
		var term = getTerm(month);
		
		for(var i = 18; i > 3; i--) { // /3�� ���� ���. 16������ �ִ� 10�б⸦ ����.
			// ó�� ���� ��
			if(i == 18) {
				if(term == 1)	i = 17; // 2���ҽ�Ŵ(continue�� ���� 1�� �� ����)
				continue;
			}
			if(i % 3 == 0) { // 1�б�, 2�б⵵ ������
				year--; // 1�� ����
				continue;
			}
			term = i % 3;
			var temp = year.toString().concat(term.toString());
			// �߰�
			console.log(i);
			$("#" + id).append("<option value='"+ temp + "'>"+ year + "/" + term + "�б�</option>")
		}
	}
  
  </script>
</head>
<body>
<!-- �˾�â -->
	<div id="myModal">
        <iframe id="popup" src="requestPopup.html" class="popup"></iframe>
  </div>
 <!-- Ȩ�������� �޴� �� -->
   <div id="header">
   <button type="button" class="logout_button" id="logoutButton" onclick="logout()">�α׾ƿ�</button>
        <span id="nowLoginSID"><%=session.getAttribute("sid") %></span>
        
 </div>
 <!-- ���� �޴� ���� -->
   <div id="studentMenu">
     <ul id="menu">
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
   <div id="inputArea">
	 	<div id="firstline">
	 			<div id="isTakeOffText" class="select">���� / ����</div>
	 			<select id="isTakeOff" class="area">
	 			<option value='takeoff'>����</option>
	 			<option value='resume'>����</option>
				</select>
	 	</div>
	 	<div id="secondline">
	 			�����б�<!-- <input type="text" id="startSemesterArea" class="area"> -->
	 			<select id="startSemesterArea" class="area">
				<option value="not" disabled selected>���ϴ� �б⸦ �����ϼ���.</option>
				</select>
	 	</div>
	 	<div id="thirdline">
	 			�����б�<!--<input type="text" id="endSemesterArea" class="area"> -->
	 			<select id="endSemesterArea" class="area">
				<option value="not" disabled selected>���ϴ� �б⸦ �����ϼ���.</option>
				</select>
	 	</div>
	 </div>
	 <button type="button" class="button" id="requestButton" onclick="request()">��û</button>
</body>
</html>