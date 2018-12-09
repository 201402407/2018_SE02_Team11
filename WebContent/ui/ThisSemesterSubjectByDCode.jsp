<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>SE02_Team11</title>
  <link href="<%=request.getContextPath() %>/css/ThisSemesterSubjectByDCode.css?ver=1" rel="stylesheet" type="text/css">
  <script src="http://code.jquery.com/jquery-1.6.2.min.js"></script>
  <script src="http://code.jquery.com/ui/1.8.23/jquery-ui.min.js"></script>
  <script>
  function createTable(){
	     
	    $.ajax({
	    	url: "<%=request.getContextPath() %>/proc/subject_getThisSemesterSubjectByDCode.jsp",
	        data : {
	        	
	        },
	        type : 'post',
	        success : function(data){
	            var results = data.boardList;
	            var str = '<TR>';
	            $.each(results , function(i){
	                str += '<TD>' + results[i].bdTitl + '</TD><TD>' + results[i].bdWriter + '</TD><TD>' + results[i].bdRgDt + '</TD>';
	                str += '</TR>';
	           });
	           $("#boardList").append(str); 
	        },
	        error : function(){
	            alert("error");
	        }
	    });
	}
  
  function getDCode() {
	  $.ajax({
	    	url: "<%=request.getContextPath() %>/proc/subject_getThisSemesterSubjectByDCode.jsp",
	        data : {
	        	
	        },
	        type : 'post',
	        success : function(data){
	            var results = data.boardList;
	            var str = '<TR>';
	            $.each(results , function(i){
	                str += '<TD>' + results[i].bdTitl + '</TD><TD>' + results[i].bdWriter + '</TD><TD>' + results[i].bdRgDt + '</TD>';
	                str += '</TR>';
	           });
	           $("#boardList").append(str); 
	        },
	        error : function(){
	            alert("error");
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
    <ul>
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
   <table id="subjectTable" border="1px">
   		<tr>
   			<th>�����</th>
   			<th>�����ڵ�</th>
   		</tr>
   </table>
</body>
</html>