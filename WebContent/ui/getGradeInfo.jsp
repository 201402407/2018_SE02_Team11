<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@page import="Util.OurTimes"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>SE02_Team11</title>
  <link href="<%=request.getContextPath() %>/css/getGradeInfo.css?ver=1" rel="stylesheet" type="text/css">
  <script src="http://code.jquery.com/jquery-1.6.2.min.js"></script>
  <script src="http://code.jquery.com/ui/1.8.23/jquery-ui.min.js"></script>
  <script>
  /* �� ������ ���� Array ���� */
  var gradeList = []; // �� �����򰡺� �迭 ���� 
  
  $(document).ready(function(){
	    jQuery.ajaxSettings.traditional = true;
	    getTermSelectBox();
	   // getGradeList($("#TermBox option:selected").val());
	    $(function(){
			$("#TermBox").change(function() {
				getGradeList($("#TermBox option:selected").val());
			})
		})
		
	    $("#menu").children().eq(6).css("background-color", "#00649F");
	    $("#menu").children().eq(6).css("color", "white");

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
	
  function retakeToString(isRetake) {
	  if(isRetake)	return "Yes";
	  else			return "No";
  }
  

	/**
	 * registerTerm �� ���ڷ� �޾� �б⸦ ���.
	 * ex) 20181 �̸� 1�б�, 20182�̸� 2�б⸦ ����
	 *  */
	function getTerm(registerTerm) {
		// 1�̸�
		if(registerTerm.toString().substring(4) == "1") {
			return "1�б�";
		}
		// 2�̸�
		if(registerTerm.toString().substring(4) == "2") {
			return "2�б�";
		}
		return null;
	}

	/* �б� ���� �ڽ� �ɼ� ���� */
	function getTermSelectBox() {
		var date = new Date();
		var year = date.getFullYear();
		var month = date.getMonth() + 1;
		var term = getTerm(month);
		
		for(var i = 18; i > 3; i--) { // /3�� ���� ���. 16������ �ִ� 10�б⸦ ����.
			// ó�� ���� ��
			if(i == 18) {
				if(term == 1)	i = 17; // 2���ҽ�Ŵ(continue�� ���� 1�� �� ����)
				continue;
			}
			if(i % 3 == 0) { // 1�б�, 2�б⵵ ������
				year--; // 1�� ����
				continue;
			}
			term = i % 3;
			var temp = year.toString().concat(term.toString());
			// �߰�
			console.log(i);
			$("#TermBox").append("<option value='"+ temp + "'>"+ year + "/" + term + "�б�</option>")
		}
	}
	
	/* �б⸦ �����ϴ� �Լ� */
	function getTerm(month) {
		if(month < 9 && month >= 3) // 1�б�
			return 1;
		else
			return 2;
	}
	
  /* ��������Ʈ �������� */
  function getGradeList(term) {
	  console.log(term);
	  $.ajax({
		  type: 'post',
		  url: "<%=request.getContextPath() %>/proc/attendance_getGradeInfo.jsp",
		  data:  {
			  "sid" : <%=session.getAttribute("sid") %>,
		  	  "semester" : term
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
					alert("����");
					 /* ��������Ʈ ��� */
					  $.each(temp, function(key, arrjson) {
						  	// ���� ���� �ֱ�.
						  	var tempArray = [];
						  	
						  	tempArray.push(arrjson.isVisible);
						  	tempArray.push(arrjson.subjectName);
						  	tempArray.push(arrjson.grade);
						  	tempArray.push(arrjson.isRetake);
						  	tempArray.push(arrjson.gradeBefore);
						  	
						  	gradeList.push(tempArray); // Ǫ��
						  	
						});
						
					 displayGradeList(gradeList);
					 
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
  function displayGradeList(list) {
	  
	  // ��ü row ����
	  for(var i=0; i < list.length; i++) {
		  $("#tablebody").append("<tr class='listRow' id='listIndex" + i + "'></tr>"); // tr ����
		  // �ش��ϴ� row�� column ����
		  for(var j = 0; j < list[i].length; j++) {
			  if(list[i][0]) {
				  if(j == 2 || j == 4)
					  $("#listIndex" + i).append("<td></td>");
					  continue;
			  }
			  	$("#listIndex" + i).append("<td>"+ list[i][j] + "</td>");

		  }
	  }
	  
	  /* Ŭ�� �̺�Ʈ ���� */
	  $(".request").click(function(event){
		  isLectureEvaluation($(this).val());
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
   <div id="selectArea" class="select">
				<select id="TermBox" class="area">
				<option value="not" disabled selected>���ϴ� �б⸦ �����ϼ���.</option>
				</select>
   </div>
   <div id="tableArea">
   		<table id="gradeTable">

		<thead>
        <tr align="center" id="title1"> 
            <td width="150" bgcolor="#00649F" rowspan="2">�����</td>
            <td width="60" bgcolor="#00649F" rowspan="2">����</td>
            <td width="70" colspan="2" bgcolor="#00649F">���������</td>            
        </tr>
        <tr align="center" id="title2"> 
            <td width="70" bgcolor="#00649F">�����</td>
            <td width="100" bgcolor="#00649F">����</td>
        </tr>
        </thead>
        <tbody id="tablebody">
        </tbody>
    </table>
   </div>
</body>
</html>