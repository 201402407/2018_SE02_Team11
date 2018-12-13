<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>SE02_Team11</title>
  <link href="<%=request.getContextPath() %>/css/addLecture.css?ver=1" rel="stylesheet" type="text/css">
  <script src="http://code.jquery.com/jquery-1.6.2.min.js"></script>
  <script src="http://code.jquery.com/ui/1.8.23/jquery-ui.min.js"></script>
  <script>
  $(document).ready(function(){
	    jQuery.ajaxSettings.traditional = true;

	    $("#menu").children().eq(4).css("background-color", "#00649F");
	    $("#menu").children().eq(4).css("color", "white");
	    
	    /* ������ Ȯ�� */
	    if(<%=session.getAttribute("isAdmin") == null %>) {
	    	alert("�����ڷ� �α��� �Ǿ����� �ʽ��ϴ�!");
	    	location.href = "login.jsp";
	    }
	    /* ���� �޴� Ŭ�� �̺�Ʈ */
		$("ul li").click(function(event){
		// ���� ��尡 �� ��°���� switch�� �Ǵ��ؼ� ����
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

  /* ajax ���� */
  function add() {
	  $.ajax({
		  type: 'post',
		  url: "<%=request.getContextPath() %>/proc/lecture_addLecture.jsp",
		  data:  {
			  "subjcode" :  $("#subjcodeArea").val(),
			  "profcode" : $("#profcodeArea").val(),
			  "depcode" :  $("#depcodeArea").val(),
			  "registerterm" : $("#registertermArea").val(),
			  "allnum" :  $("#allnumArea").val(),
			  "dayofweek" : $("#dayofweekArea").val(),
			  "starttime" :  $("#starttimeArea").val(),
			  "endtime" : $("#endtimeArea").val(),
			  "syltext" : $("#syltextArea").val()
			  },
		  //async: false,
		  dataType : "json",
		  success: function(success) {
			  if(success) { // ���� �Ϸ� ��.
				  if(success.error != null) { // ����
						alert(success.error);
				  }
				  else { // ����
						alert("���������� �߰��Ǿ����ϴ�.");
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
 <!-- Ȩ�������� �޴� �� -->
   <div id="header">
   <button type="button" class="logout_button" id="logoutButton" onclick="logout()">�α׾ƿ�</button>
        <span id="nowLoginSID">������</span>
        
 </div>
 <!-- ���� �޴� ���� -->
   <div id="adminMenu">
    <ul id="menu">
        <li id="first">�й� �ο�</li>
        <li>�޺��� ����</li>
        <li>������ �߰�</li>
        <li>���� �߰�</li>
        <li>�й� �߰�</li>
        <li>���� ����</li>
        <li>���� �߰�</li>
        <li>���� �ο�</li>
	</ul>
   </div>
   <div id="inputArea">
	 	<div id="firstline">
	 			�����ڵ�<input type="text" id="subjcodeArea" class="area">
	 			������Ϲ�ȣ<input type="text" id="profcodeArea" class="area">
	 	</div>
	 	<div id="secondline">
	 			�а��ڵ�<input type="text" id="depcodeArea" class="area">
	 			����б�<input type="text" id="registertermArea" class="area">
	 	</div>
	 	<div id="thirdline">
	 			��ü�ο�<input type="text" id="allnumArea" class="area">
	 			���� ����<input type="text" id="dayofweekArea" class="area">
	 	</div>
	 	<div id="fourthline">
	 			���� ���۽ð�<input type="text" id="starttimeArea" class="area">
	 			���� ����ð�<input type="text" id="endtimeArea" class="area">
	 	</div>
	 	<div id="fifthline">
	 			<textarea id="syltextArea" placeholder="���ǰ�ȹ���� �Է��ϼ���" class="area"></textarea>
	 	</div>
	 </div>
	 <button type="button" class="button" id="addButton" onclick="add()">���</button>
</body>
</html>