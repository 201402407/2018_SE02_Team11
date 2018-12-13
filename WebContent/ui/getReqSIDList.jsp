<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>SE02_Team11</title>
  <link href="<%=request.getContextPath() %>/css/getReqSIDList.css?ver=1" rel="stylesheet" type="text/css">
  <script src="http://code.jquery.com/jquery-1.6.2.min.js"></script>
  <script src="http://code.jquery.com/ui/1.8.23/jquery-ui.min.js"></script>
  <script>
  $(document).ready(function(){
	    jQuery.ajaxSettings.traditional = true;
	    getList();
	    if(<%=session.getAttribute("isAdmin") == null %>) {
	    	alert("�����ڷ� �α��� �Ǿ����� �ʽ��ϴ�!");
	    	location.href = "login.jsp";
	    }
	    $("#menu").children().eq(0).css("background-color", "#00649F");
	    $("#menu").children().eq(0).css("color", "white");
	    
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
			location.href = "awardToStudent.jsp";
			break;
		case 5:
			location.href = "addLecture.jsp";
			break;
		case 6:
			location.href = "addProfessor.jsp";
			break;
		case 7:
			location.href = "login.jsp";
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

  /* ����Ʈ��� */
  function getList() {
	  $("#subArea").empty(); // �ʱ�ȭ
	    $.ajax({
	    	type : 'post',
	    	url: "<%=request.getContextPath() %>/proc/studentidrequest_getReqSIDList.jsp",
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
						
						var result = []; // �迭 ����. ���߿� �ε����� �а�
						$.each(temp, function(key, value){ // JSON �޾ƿ� ���� data ������� ����.
							var temp2 = [];
						/* ���� : reqSIDnum, reqSIDdata, accountID
							push�� shift�� ������� */
								$.each(value, function(element, result) {
									
									temp2.push(result);
								});
							result.push(temp2);
						 });

							for(var i = 0; i < result.length; i++) { // �ε��� 1�� subjectName�̴�.
								/* reqnum */
								$('#subArea').append("<div class='reqdiv' id='num"+ i +"'></div>"); // div�߰�
								$("#num" + i).append('<span class="reqspan" id="numspan'+ i +'" name="not">'+ result[i][0] + '</span>');
								/* reqdate */
								$('#subArea').append("<div class='reqdiv' id='date"+ i +"'></div>"); // div�߰�
								$("#date" + i).append('<span class="reqspan" id="datespan'+ i +'" name="not">'+ result[i][1] + '</span>');
								/* reqid */
								$('#subArea').append("<div class='reqdiv' id='id"+ i +"'></div>"); // div�߰�
								$("#id" + i).append('<span class="reqspan" id="idspan'+ i +'" name="not">'+ result[i][2] + '</span>');
								/* reqbtn */
								$('#subArea').append("<div class='reqdiv' id='ws"+ i +"'></div>"); // div�߰�
								$("#ws" + i).append('<button class="permit" type="button" name="not" id="permitbtn' + i + '">����</button>'); // ��ư�߰�
								$("#ws" + i).append('<button class="reject" type="button" name="not" id="rejectbtn' + i + '">����</button>'); // ��ư�߰�
								
								/*
								$("#" + i).css({
									'width' : '99.9%',
									'height' : '25px',
									'border' : '0.5px solid #ADADAD',
									'color' : '#707070',
									'float' : 'left',
									'text-indent' : '7px'
								})
								
								$("#btn" + i).css({ // ��ư css ����
									'width' : '70px',
									'height' : '20px',
									'background' : '#00649F',
									'color' : 'white',
									'font-size' : '10px',
									'line-height' : '10px',
									'float' : 'right',
									'margint-right' : '30px',
									'border' : '0px',
									'border-radius' : '5px',
								});
								*/
								
								/* ������û ���� ����Ʈ ��ȸ �˾� ���� */
								/*
								$("#"+i).click(function(event){
									if(event.target.name == "not") {
										alert("not");
									}
									else {
										
										make_function2(result[i]);
									}
									
								});
								*/
								
								/* �ش���� ������û */
								$("#permitbtn"+i).click(make_permitfunction(result[i]));
								$("#rejectbtn"+i).click(make_rejectfunction(result[i]));
							}
							
							/* css �߰� */
							$(".reqdiv").css({
								'width' : '24.8%',
								'height' : '25px',
								'border' : '0px',
								//'margin-right' : '-7px',
								'color' : '#707070',
								'float' : 'left',
								'text-indent' : '7px',
								'display' : 'inline-block'
							})
							
							$(".permit" + i).css({ // ��ư css ����
								'width' : '45px',
								'height' : '20px',
								'background' : '#00649F',
								'color' : 'white',
								'font-size' : '10px',
								'line-height' : '10px',
								'float' : 'right',
								'margint-right' : '30px',
								'text-align': 'center',
								'border' : '0px',
								'border-radius' : '5px',
							});
							
							$(".reject" + i).css({ // ��ư css ����
								'width' : '45px',
								'height' : '20px',
								'background' : '#ADADAD',
								'color' : 'white',
								'font-size' : '10px',
								'text-align': 'center',
								'line-height' : '10px',
								'float' : 'right',
								'margint-right' : '30px',
								'border' : '0px',
								'border-radius' : '5px',
							});
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
  /* Ŭ���� */
  //������ ������ ���ο� num�� ���. 
  function make_permitfunction(lectureArray) {
      return function() { permit(lectureArray); };
  }
  function make_rejectfunction(lectureArray) {
      return function() { reject(lectureArray); };
  }
  
  /* �й��ο��㰡 */
  function permit(lectureArray) {
	  $.ajax({
		  type: 'post',
		  url: "<%=request.getContextPath() %>/proc/studentidrequest_permitReqSID.jsp",
		  data:  {
			  "reqnum" : lectureArray[0],
			  "dcode" : $("#inputDepartmentCode").val()
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
					  getList();
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
  
  /* �й��ο����� */
  function reject(lectureArray) {
	  $.ajax({
		  type: 'post',
		  url: "<%=request.getContextPath() %>/proc/studentidrequest_rejectReqSID.jsp",
		  data:  {
			  "reqnum" : lectureArray[0]
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
					  getList();
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
   <!-- �˻� ���� -->
     <div id="searchArea" class="searchArea">
         <input type="text" name="SubjectName" id="inputDepartmentCode">
       </div>
       <!-- �ֿ�ȭ�� -->
   <div id="mainArea">
   		<div id="numContext" class="context">��û��ȣ</div>
   		<div id="dateContext" class="context">��û����</div>
   		<div id="idContext" class="context">���̵�</div>
   		<div id="whitespaceContext" class="context"></div>
   		<div id="subArea">
   		</div>
   		</div>
</body>
</html>