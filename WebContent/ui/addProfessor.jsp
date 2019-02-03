<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>SE02_Team11</title>
  <link href="<%=request.getContextPath() %>/css/addProfessor.css?ver=1" rel="stylesheet" type="text/css">
  <script src="http://code.jquery.com/jquery-1.6.2.min.js"></script>
  <script src="http://code.jquery.com/ui/1.8.23/jquery-ui.min.js"></script>
  <script>
  $(document).ready(function(){
	    jQuery.ajaxSettings.traditional = true;

	    $("#menu").children().eq(6).css("background-color", "#00649F");
	    $("#menu").children().eq(6).css("color", "white");
	    
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
		  url: "<%=request.getContextPath() %>/proc/professor_addProfessor.jsp",
		  data:  {
			  "profname" :  $("#professorArea").val()
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
	 			���� �̸�<input type="text" id="professorArea" class="area">
	 	</div>
	 </div>
	 <button type="button" class="button" id="addButton" onclick="add()">���</button>
</body>
</html>