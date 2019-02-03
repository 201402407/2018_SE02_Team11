<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>SE02_Team11</title>
  <link href="<%=request.getContextPath() %>/css/getApplyLectureList.css" rel="stylesheet" type="text/css">
  <script src="http://code.jquery.com/jquery-1.6.2.min.js"></script>
  <script src="http://code.jquery.com/ui/1.8.23/jquery-ui.min.js"></script>
  <script>
  $(document).ready(function(){
	    jQuery.ajaxSettings.traditional = true;
	    window.addEventListener("message", messageHandler, true);
	   
	    
	    $("#menu").children().eq(4).css("background-color", "#00649F");
	    $("#menu").children().eq(4).css("color", "white");
	    
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
			location.href = "login.jsp";
			break;
		case 6:
			location.href = "login.jsp";
			break;
		case 7:
			location.href = "login.jsp";
			break;
		case 8:
			location.href = "login.jsp";
			break;
		}
		});
  });
  
  function sendMessage(obj) {
	  
	  document.getElementById("popup").contentWindow.postMessage(obj,"*");
	}
  
  function messageHandler(e) {
	  /* �˾�â���� �ݴ� ��û�� ������ */
	  if(e.data == "close") {
		  document.getElementById("popup").style.display = "none";
	  }
  }
  
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
  
  /* Ư�� ���� �˻� */
  function search() {
	  $("#subArea").empty(); // �ʱ�ȭ
	  alert($("#inputSubjectName").val());
	  $.ajax({
	    	type : 'post',
	    	url: "<%=request.getContextPath() %>/proc/lecture_getLectureByName.jsp",
	        data : {
	        	"sid" : <%=session.getAttribute("sid") %>,
	        	"subjectName" : $("#inputSubjectName").val()
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
						/* ���� : score, profName, dayOfWeek, registerTerm, startTime, endTime, 
						applyNum, lectureCode, subjectName, allNum
								dayOfWeek, startTime, endTime, score 
							push�� shift�� ������� */
								$.each(value, function(element, result) {
									//alert(result);
									temp2.push(result);
								});
							result.push(temp2);
						 });

							for(var i = 0; i < result.length; i++) { // �ε��� 1�� subjectName�̴�.
								$('#subArea').append("<div id='"+ i +"'></div>"); // div�߰�
								$("#" + i).append('<span id="span'+ i +'" name="not">'+ result[i][8] + '</span>');
								$("#" + i).append('<button type="button" name="not" id="btn' + i + '">��û</button>'); // ��ư�߰�
								
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
								$("#span"+i).click(make_function2(result[i]));
								/* �ش���� ������û */
								$("#btn"+i).click(make_function(result[i]));
							}
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
  
  /* ��� �˻� */
  function allsearch() {
	  $("#subArea").empty(); // �ʱ�ȭ
	    $.ajax({
	    	type : 'post',
	    	url: "<%=request.getContextPath() %>/proc/lecture_getApplyLectureList.jsp",
	        data : {
	        	"sid" : <%=session.getAttribute("sid") %>
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
						/* ���� : score, profName, dayOfWeek, registerTerm, startTime, endTime, 
						applyNum, lectureCode, subjectName, allNum
								dayOfWeek, startTime, endTime, score 
							push�� shift�� ������� */
								$.each(value, function(element, result) {
									//alert(result);
									temp2.push(result);
								});
							result.push(temp2);
						 });

							for(var i = 0; i < result.length; i++) { // �ε��� 1�� subjectName�̴�.
								$('#subArea').append("<div id='"+ i +"'></div>"); // div�߰�
								$("#" + i).append('<span id="span'+ i +'" name="not">'+ result[i][8] + '</span>');
								$("#" + i).append('<button type="button" name="not" id="btn' + i + '">��û</button>'); // ��ư�߰�
								
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
								$("#span"+i).click(make_function2(result[i]));
								/* �ش���� ������û */
								$("#btn"+i).click(make_function(result[i]));
							}
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
  //������ ������ ���ο� num�� ���. ������û
  function make_function(lectureArray) {
      return function() { addAttendance(lectureArray); };
  }
  
  //������ ������ ���ο� num�� ���. �˾�
  function make_function2(lectureArray) {
      return function() {  lecturePopup(lectureArray); };
  }
  
  /* �ش�йݻ���ȸ */
  function lecturePopup(lectureArray) {
	  document.getElementById("popup").style.display = "block"; // �˾� ����
	  
	  sendMessage(lectureArray); // �ش� �ε����� �ش��ϴ� �����͸� ����
  }
  /* ������û */
  function addAttendance(lectureArray) {
	  $.ajax({
		  type: 'post',
		  url: "<%=request.getContextPath() %>/proc/attendance_addAttendance.jsp",
		  data:  {
			  "sid" : <%=session.getAttribute("sid") %>,
			  "lcode" : lectureArray[7]
			  },
		  //async: false,
		  dataType : "json",
		  success: function(success) {
			
			  if(success) { // ���� �Ϸ� ��.
				  if(success.error != null) { // ����
					  alert(success.error);
				  }
				  else {
					  alert("���������� ���� ��û�� �Ϸ�Ǿ����ϴ�.");
					  
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
        <iframe id="popup" src="lecturePopup.html" class="popup"></iframe>
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
   <!-- �˻� ���� -->
     <div id="searchArea" class="searchArea">
         <input type="text" name="SubjectName" id="inputSubjectName">
		<button type="button" class="button" name="searchButton" id="searchButton" onclick="search()">��ȸ</button>
		<button type="button" class="button" name="allButton" id="allButton" onclick="allsearch()">��� ����</button>         
       </div>
       <!-- �ֿ�ȭ�� -->
   <div id="mainArea">
   		<div id="subjectNameContext">�����</div>
   		<div id="subArea">
   		</div>
   </div>
</body>
</html>