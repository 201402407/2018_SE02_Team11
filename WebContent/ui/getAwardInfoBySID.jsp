<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@page import="Util.OurTimes"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>SE02_Team11</title>
  <link href="<%=request.getContextPath() %>/css/lectureEvaluation.css?ver=1" rel="stylesheet" type="text/css">
  <script src="http://code.jquery.com/jquery-1.6.2.min.js"></script>
  <script src="http://code.jquery.com/ui/1.8.23/jquery-ui.min.js"></script>
  <script>
  /* �� ������ ���� Array ���� */
  var evaluationList = []; // �� �����򰡺� �迭 ���� 
  
  $(document).ready(function(){
	    jQuery.ajaxSettings.traditional = true;
	    getAwardList();
	    
	    $("#menu").children().eq(7).css("background-color", "#00649F");
	    $("#menu").children().eq(7).css("color", "white");
	    	    
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
	 
  /* ���г�������Ʈ �������� */
  function getAwardList() {
	  $.ajax({
		  type: 'post',
		  url: "<%=request.getContextPath() %>/proc/scholarshipaward_getAwardInfoBySID.jsp",
		  data:  {
			  "sid" : <%=session.getAttribute("sid") %>
			  },
		  //async: false,
		  dataType : "json",
		  success: function(success) {
			 // alert(success);
			  if(success) { // ���� �Ϸ� ��.
				  if(success.error != null) { // ����
					  alert(success.error);
				  }
				  else {
					var temp = success.data;
					
					  /* ��������Ʈ ��� */
					  $.each(temp, function(key, arrjson) {
						  	// �������� ���� �ֱ�.
						  	var tempArray = [];
						  	
						  	tempArray.push(arrjson.schoName);
						  	tempArray.push(arrjson.money);
						  	tempArray.push(arrjson.awardDate);
						  	
						  	evaluationList.push(tempArray); // Ǫ��
						  	
						});
						
					 displayAwardList(evaluationList);
					 
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
  
  /* ȭ�鿡 ��� */
  function displayAwardList(list) {
	  
	  // ��ü row ����
	  for(var i=0; i < list.length; i++) {
		  $("#tablebody").append("<tr class='listRow' id='listIndex" + i + "'></tr>"); // tr ����
		  // �ش��ϴ� row�� column ����
		  $("#listIndex" + i).append("<td>"+ list[i][0] + "</td>"); // �����̸�
		  $("#listIndex" + i).append("<td align='center'>"+ list[i][1] + " ��</td>"); // ���б�
		  $("#listIndex" + i).append("<td align='center'>"+ list[i][2] + "</td>"); // ��������
	  }
	  
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
     <ul id ="menu">
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
   <div id="tableArea">
   		<table id="evaluationTable">

		<thead>
        <tr align="center" id="title"> 
            <td width="210" bgcolor="#00649F">�����̸�</td>
            <td width="120" bgcolor="#00649F">���б�</td>
            <td width="140" bgcolor="#00649F">��������</td>
        </tr>
        </thead>
        <tbody id="tablebody">
        </tbody>
    </table>
   </div>
</body>
</html>