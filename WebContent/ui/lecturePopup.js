window.onload = function() {
	jQuery.ajaxSettings.traditional = true;
  window.addEventListener("message", messageHandler, true);
  document.getElementById("Close_button").onclick = function() {
	  sendMessage("close");
  }
  
}

function messageHandler(e) {
  var obj = e.data;
  
  View_Item(obj);
}

/* 과목명과 학점 출력 */
function View_Item(obj) {
  
  view_syllabus(obj);
  /* 초기화 */
  $("#snameArea").empty();
  $("#lcodeArea").empty();
  $("#profnameArea").empty();
  $("#termArea").empty();
  $("#applynumArea").empty();
  $("#allnumArea").empty();
  $("#dayofweekArea").empty();
  $("#scoreArea").empty();
  $("#startArea").empty();
  $("#endArea").empty();
  
  /* 생성 */
  $("#snameArea").html(obj[8]);
  $("#lcodeArea").html(obj[7]);
  $("#profnameArea").html(obj[1]);
  $("#termArea").html(obj[3]);
  $("#applynumArea").html(obj[6]);
  $("#allnumArea").html(obj[9]);
  $("#dayofweekArea").html(obj[2]);
  $("#scoreArea").html(obj[0]);
  $("#startArea").html(obj[4]);
  $("#endArea").html(obj[5]);
  
}

function view_syllabus(obj) {
	$.ajax({
	  	type: 'post',
	  	url: "../proc/lecture_getSyllabusByLCode.jsp",
	  	data: {
	       	"lcode" : obj[7]
	  	},
	      dataType : "json",
			  success: function(success) {
				  if(success) { // 전송 완료 시.
					  if(success.error != null) { // 실패
						  alert(success.error);
						  //sendMessage("close");
					  }
					  else {
						
						var result = success.data;
						
						
						$("#syllabusText").html(result); // 또는 result[0]
						
				  	}
				  }
				  else {
					  alert("잠시 후에 시도해주세요.");
				  }
			  },
			  error: function(xhr, request,error) {

			  }
	  });
}

function sendMessage(obj) {
  window.parent.postMessage(obj,"*");
}

