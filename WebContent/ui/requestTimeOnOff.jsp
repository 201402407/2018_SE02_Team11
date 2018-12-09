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
				"sid" : <%=session.getAttribute("sID") %>,
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
  </script>
</head>
<body>
<!-- �˾�â -->
	<div id="myModal">
        <iframe id="popup" src="requestPopup.html" class="popup"></iframe>
  </div>
 <!-- Ȩ�������� �޴� �� -->
   <div id="header">
   <button type="button" class="logout_button" id="logoutButton">�α׾ƿ�</button>
        <span id="nowLoginSID"><%=session.getAttribute("sID") %></span>
        
 </div>
 <!-- ���� �޴� ���� -->
   <div id="studentMenu">
     asdaf
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
	 			�����б�<input type="text" id="startSemesterArea" class="area">
	 	</div>
	 	<div id="thirdline">
	 			�����б�<input type="text" id="endSemesterArea" class="area">
	 	</div>
	 </div>
	 <button type="button" class="button" id="requestButton" onclick="request()">��û</button>
</body>
</html>