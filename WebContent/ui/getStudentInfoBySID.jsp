<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>SE02_Team11</title>
  <link href="<%=request.getContextPath() %>/css/getStudentInfoBySID.css?ver=1" rel="stylesheet" type="text/css">
  <script src="http://code.jquery.com/jquery-1.6.2.min.js"></script>
  <script src="http://code.jquery.com/ui/1.8.23/jquery-ui.min.js"></script>
  <script>
  
	/* ���� �� ���� */
	$(document).ready(function(){
	    jQuery.ajaxSettings.traditional = true;
		getStudentInfo();
		
		$("#menu").children().eq(0).css("background-color", "#00649F");
		$("#menu").children().eq(0).css("color", "white");
		
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
	/* ������ ���� �� �ٷ� ���� */
	function getStudentInfo() {
		$.ajax({
		
			  type: 'post',
			  url: "<%=request.getContextPath() %>/proc/student_getStudentInfoBySID.jsp",
			  data:  {
				  "sid" : <%=session.getAttribute("sid") %>
				  },
			  //async: false,
			  dataType : "json",
			  success: function(success) {
				  
				  if(success) { // ���� �Ϸ� ��.
					  if(success.error != null) { // ����
						  alert(success.error);
					  }
					  else {
						  /* JSON -> Object �Ľ� */
						  //var list = JSON.stringify(success);
						 var result = success.data;
						  //var listLen = list.length;
						  var name, departmentName, semester, isTimeOff, isGraduate, year;
						  
						  /* ������ �������� */
							name = result.name;
							departmentName = result.departmentName;
							semester = result.semester;
							isTimeOff = result.isTimeOff;
							
							/* ġȯ */
							if(!result.isTimeOff) 		isTimeOff = "����";
							else						isTimeOff = "����";
							if(!result.isGraduate) 		isGraduate = "No";
							else						isGraduate = "Yes";
							
							year = result.year;
						  
							
						  /* �ش��ϴ� ��ĭ�� �ֱ� */
						  $("#nameArea").append(name);
						  $("#departnameArea").append(departmentName);
						  $("#yearArea").append(year);
						  $("#startSemesterArea").append(semester);
						  $("#isTakeoffArea").append(isTimeOff);
						  $("#isGraduateArea").append(isGraduate);
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
	<!-- ���� ȭ�� �̹��� ���� -->
	 <img src="<%=request.getContextPath() %>/image/mainImage.png" id="mainImageSrc"> 
	 <div id="studentInfoArea">
	 	<div id="firstline">
	 			�г�<div id="yearArea" class="area"></div>
	 			�̸�<div id="nameArea" class="area"></div>
	 			���п���<div id="isTakeoffArea" class="area"></div>
	 	</div>
	 	<div id="secondline">
	 			�а���<div id="departnameArea" class="area"></div>
	 			�̼��б�<div id="startSemesterArea" class="area"></div>
	 			��������<div id="isGraduateArea" class="area"></div>
	 	</div>
	 </div>
</body>
</html>