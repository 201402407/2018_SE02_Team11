<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>SE02_Team11</title>
  <link href="<%=request.getContextPath() %>/css/getStudentInfoBySID.css?ver=1" rel="stylesheet" type="text/css">
  <link rel="stylesheet" type="text/css" href="loginpage.css?ver=1">
  <script src="http://code.jquery.com/jquery-1.6.2.min.js"></script>
  <script src="http://code.jquery.com/ui/1.8.23/jquery-ui.min.js"></script>
  <script src="js/login.js?ver=1"></script>
  <script>
  
	/* ���� �� ���� */
	$(document).ready(function(){
	    jQuery.ajaxSettings.traditional = true;
		getStudentInfo();
		
		$("#menu").children().eq(0).css("background-color", "#00649F");
	    $("#menu").children().eq(0).css("color", "white");
	});
	
	/* ������ ���� �� �ٷ� ���� */
	function getStudentInfo() {
		$.ajax({
		
			  type: 'post',
			  url: "<%=request.getContextPath() %>/proc/student_getStudentInfoBySID.jsp",
			  data:  {
				  "sid" : <%=session.getAttribute("sID") %>
				  },
			  //async: false,
			  dataType : "json",
			  success: function(success) {
				  alert(success);
				  if(success) { // ���� �Ϸ� ��.
					  if(success.error != null) { // ����
						  
					  }
					  else {
						  var name = success.name;
						  var departmentName = success.departmentName;	
						  var semester = success.semester;
						  var isTimeOff = success.isTimeOff;
						  var isGraduate = success.isGraduate;
						  
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
   <button type="button" class="logout_button" id="logoutButton">�α׾ƿ�</button>
        <span id="nowLoginSID"><%=session.getAttribute("sID") %></span>
        
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
	 			�޺��п���<div id="isTakeoffArea" class="area"></div>
	 	</div>
	 	<div id="secondline">
	 			�а���<div id="departnameArea" class="area"></div>
	 			�̼��б�<div id="startSemesterArea" class="area"></div>
	 			��������<div id="isGraduateArea" class="area"></div>
	 	</div>
	 </div>
</body>
</html>