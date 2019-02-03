<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>SE02_Team11</title>
  <link href="<%=request.getContextPath() %>/css/addGrade.css?ver=1" rel="stylesheet" type="text/css">
  <script src="http://code.jquery.com/jquery-1.6.2.min.js"></script>
  <script src="http://code.jquery.com/ui/1.8.23/jquery-ui.min.js"></script>
  <script>
  /* ��� ���� Array ���� */
  var list = []; // ������� �迭 ���� 
  var col; // ������ �� �ε���
  $(document).ready(function(){
	    jQuery.ajaxSettings.traditional = true;
	    
	    $("#menu").children().eq(7).css("background-color", "#00649F");
	    $("#menu").children().eq(7).css("color", "white");
	    
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
  function retakeToKor(changetype) {
	  if(changetype)	return "Yes";
	  if(!changetype)	return "No";
  }
  
  /* �˻� */
  function search()	{
	  
	  if($("#inputLectureCode").val() == null) {
		  return;
	  }
	  
	  $.ajax({
	    	type : 'post',
	    	url: "<%=request.getContextPath() %>/proc/attendance_getAttendanceListbyLCode.jsp",
	        data : {
	        	"lcode" : $("#inputLectureCode").val()
	        },
	        dataType : "json",
			  success: function(success) {
				  alert(success);
				  alert(success.data);
				  if(success) { // ���� �Ϸ� ��.
					  if(success.error != null) { // ����
						  alert(success.error);
						  $("#inputLectureCode").html(" "); // �������� �ٲ�
					  }
					  else {
						 var temp = success.data;
						alert(temp);
						 /* ��������Ʈ ��� */
						 // ���� : subjectName, attendanceNum, isRetake, studentID, studentName
						  $.each(temp, function(key, arrjson) {
							  	// �������� ���� �ֱ�.
							  	var tempArray = [];
							  	alert(arrjson.subjectName);
							  	
							  	tempArray.push(arrjson.subjectName);
							  	tempArray.push(arrjson.attendanceNum);
							  	tempArray.push(retakeToKor(arrjson.isRetake));
							  	tempArray.push(arrjson.studentID);
							  	tempArray.push(arrjson.studentName);
							  	
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
	  $($("#tablebody")).empty(); // ���̺� �ʱ�ȭ
	  // ��ü row ����
	  for(var i=0; i < list.length; i++) {
		  $("#tablebody").append("<tr class='listRow' id='listIndex" + i + "'></tr>"); // tr ����
		  
		  // �ش��ϴ� row�� column ����
		  $("#listIndex" + i).append("<td>"+ list[i][0] + "</td>"); // �����
		  $("#listIndex" + i).append("<td align='center'>"+ list[i][1] + "</td>"); // ������ȣ
		  $("#listIndex" + i).append("<td align='center'>"+ list[i][2] + "</td>"); // ���������
		  $("#listIndex" + i).append("<td align='center'>"+ list[i][3] + "</td>"); // �й�
		  $("#listIndex" + i).append("<td>"+ list[i][4] + "</td>"); // �л��̸�
	  }
	  
	  
	  // �� Ŭ�� ��
	  $("tbody tr").click(function() {
		  $("tbody tr").css("background-color", "#F1F1F1"); // ���� �͵� ���� �ʱ�ȭ
		  $("tbody tr").css("color", "#707070"); // ���� �͵� ���� �ʱ�ȭ
		  col = $(this).index();
		  // Ŭ��
		  $(this).css("background-color", "#003453");
		  $(this).css("color", "white");
		  alert(col);
			// ���� Ȱ��ȭ
		  $("#gradeBox").show();
			
			$("#addButton").click(function() {
				if(col == null) {
					alert("���Ͻô� ���� ����Ʈ�� �켱 �����ϼ���!");
				}
				if($("#gradeBox option:selected").val() == null) {
					alert("������ �����ϼ���!");
				}
				
				var attendancenum = list[col][1];
				var grade = $("#gradeBox option:selected").val();
				alert(col + ", " + grade);
				alert(list[col][1]);
				add(attendancenum, grade);
			});
	  });
	  
  }
  
  /* ������� */
  function add(attnum, grade) {
	  $.ajax({
		  type: 'post',
		  url: "<%=request.getContextPath() %>/proc/gradeinfo_addGrade.jsp",
		  data:  {
			  "attendancenum" : attnum,
			  "grade" : grade
			  },
		  //async: false,
		  dataType : "json",
		  success: function(success) {
			  if(success) { // ���� �Ϸ� ��.
				  if(success.error != null) { // ����
					  alert(success.error);
				  }
				  else {
					  alert("�����ο��� �Ϸ�Ǿ����ϴ�.");
					  $("#tablebody").empty(); // ���̺� �ʱ�ȭ
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
   <!-- �˻� -->
   <div id="searchArea" class="searchArea">
         <input type="text" name="LectureCode" id="inputLectureCode">
		<button type="button" class="button" name="searchButton" id="searchButton" onclick="search()">��ȸ</button>         
       </div>
	 <div id="tableArea">
   		<table id="gradeTable">
		<thead>
        <tr align="center" id="title"> 
            <td width="150" bgcolor="#00649F">�����</td>
            <td width="120" bgcolor="#00649F">������ȣ</td>
            <td width="120" bgcolor="#00649F">���������</td>
            <td width="150" bgcolor="#00649F">�й�</td>
            <td width="130" bgcolor="#00649F">�л��̸�</td>
        </tr>
        </thead>
        <tbody id="tablebody">
        </tbody>
    </table>
   </div>
   <div id="boxArea">
   	<select id="gradeBox" class="box">
	 	<option value='4.5'>A+</option>
	 	<option value='4.0'>A0</option>
	 	<option value='3.5'>B+</option>
	 	<option value='3.0'>B0</option>
	 	<option value='2.5'>C+</option>
	 	<option value='2.0'>C0</option>
	 	<option value='1.5'>D+</option>
	 	<option value='1.0'>D0</option>
	</select>
   </div>
   <button type="button" class="button" id="addButton" onclick="add()">���</button>
</body>
</html>