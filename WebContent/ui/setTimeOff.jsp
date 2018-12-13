<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>SE02_Team11</title>
  <link href="<%=request.getContextPath() %>/css/setTimeOff.css?ver=1" rel="stylesheet" type="text/css">
  <script src="http://code.jquery.com/jquery-1.6.2.min.js"></script>
  <script src="http://code.jquery.com/ui/1.8.23/jquery-ui.min.js"></script>
  <script>
  /* ��� ���� Array ���� */
  var list = []; // ������� �迭 ���� 
  
  $(document).ready(function(){
	    jQuery.ajaxSettings.traditional = true;
	    getList();
	    
	    $("#menu").children().eq(1).css("background-color", "#00649F");
	    $("#menu").children().eq(1).css("color", "white");
	    
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

  /* �޺��� ���� ���� */
  function changetypeToKor(changetype) {
	  if(changetype == 0)	return "����";
	  if(changetype == 1)	return "����";
  }
  /* ����Ʈ��� */
  function getList() {
	  $("#tablebody").empty(); // �ʱ�ȭ
	    $.ajax({
	    	type : 'post',
	    	url: "<%=request.getContextPath() %>/proc/timeoffrequest_getTimeoffRequestList.jsp",
	        data : {
	        },
	        dataType : "json",
			  success: function(success) {
				  if(success) { // ���� �Ϸ� ��.
					  if(success.error != null) { // ����
						  alert(success.error);
					  }
					  else {
						 var temp = success.data;
						//location.href = "../proc/timeoffrequest_getTimeoffRequestList.jsp";
						
						 /* ��������Ʈ ��� */
						  $.each(temp, function(key, arrjson) {
							  	// �������� ���� �ֱ�.
							  	var tempArray = [];
							  	
							  	console.log(arrjson.changeType);
							  	
							  	tempArray.push(arrjson.reqNum);
							  	tempArray.push(arrjson.reqDate);
							  	tempArray.push(arrjson.changeType);
							  	tempArray.push(arrjson.startSemester);
							  	tempArray.push(arrjson.endSemester);
							  	tempArray.push(arrjson.reason);
							  	
							  	list.push(tempArray); // Ǫ��
							  	
							});
							
						
						printlist(list);
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
  /* ����Ʈ ��� */
  function printlist(list) {
	  $($("#reasonText")).html(" ");
	// ��ü row ����
	  for(var i=0; i < list.length; i++) {
		  $("#tablebody").append("<tr class='listRow' id='listIndex" + i + "'></tr>"); // tr ����
		  
		  // �ش��ϴ� row�� column ����
		  $("#listIndex" + i).append("<td>"+ list[i][0] + "</td>"); // ��û��ȣ
		  $("#listIndex" + i).append("<td align='center'>"+ list[i][1] + "</td>"); // ��û����
		  $("#listIndex" + i).append("<td align='center'>"+ list[i][2] + "</td>"); // �޺��б���
		  $("#listIndex" + i).append("<td align='center'>"+ list[i][3] + "</td>"); // �����б�
		  $("#listIndex" + i).append("<td align='center'>"+ list[i][4] + "</td>"); // �����б�
		  $("#reasonText").html(list[i][5]);
		  $("#listIndex" + i).append("<td id='"+ i + "'align='center'></td>"); // �����б�
		  $("#"+ i).append('<button type="button" value="'+ list[i][0] + '" class="permit" id="permitbtn' + i + '">����</button>');
		  $("#"+ i).append('<button type="button" value="'+ list[i][0] + '" class="reject" id="rejectbtn' + i + '">����</button>');
	  }
	
	  $(".permit").click(function(event){
			permit($(this).val());
		});
	  
	  $(".reject").click(function(event){
			reject($(this).val());
		});
  }
  
  /* �޺��н�û�㰡 */
  function permit(reqNum) {
	  $.ajax({
		  type: 'post',
		  url: "<%=request.getContextPath() %>/proc/timeoffrequest_permitTimeoffRequest.jsp",
		  data:  {
			  "reqNum" : reqNum
			  },
		  //async: false,
		  dataType : "json",
		  success: function(success) {
			
			  if(success) { // ���� �Ϸ� ��.
				  if(success.error != null) { // ����
					  alert(success.error);
				  }
				  else {
					  alert("�й���û �㰡 �Ϸ�Ǿ����ϴ�.");
					  
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
  
  /* �޺��н�û���� */
  function reject(reqNum) {
	  $.ajax({
		  type: 'post',
		  url: "<%=request.getContextPath() %>/proc/timeoffrequest_rejectTimeoffReq.jsp",
		  data:  {
			  "reqNum" : reqNum
			  },
		  //async: false,
		  dataType : "json",
		  success: function(success) {
			
			  if(success) { // ���� �Ϸ� ��.
				  if(success.error != null) { // ����
					  alert(success.error);
				  }
				  else {
					  alert("�й���û ���� �Ϸ�Ǿ����ϴ�.");
					  
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
	 <div id="tableArea">
   		<table id="timeoffTable">

		<thead>
        <tr align="center" id="title"> 
            <td width="100" bgcolor="#00649F">��û��ȣ</td>
            <td width="180" bgcolor="#00649F">��û����</td>
            <td width="90" bgcolor="#00649F">�޺��б���</td>
            <td width="120" bgcolor="#00649F">�����б�</td>
            <td width="120" bgcolor="#00649F">�����б�</td>
            <td width="180" bgcolor="#00649F"></td>
        </tr>
        </thead>
        <tbody id="tablebody">
        </tbody>
    </table>
   </div>
   <div id="reasonArea">
   	<div id="reasonTitle">��<br/>û<br/>��<br/>��<br/>
   	</div>
   	<div id="reasonTextArea">
   		<div id="reasonText"></div>
   	</div>
   </div>
</body>
</html>