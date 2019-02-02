window.onload = function() {
  document.getElementById("Close_button").onclick = function() {
	  sendMessage("close");
	  	  
  }
  window.addEventListener("message", messageHandler, true);
}

function messageHandler(e) {
  var obj = e.data;
  /* 초기화 */
  $("#nameArea").empty();
  $("#scoreArea").empty();
  
  View_Item(obj.pop());
}

/* 과목명과 학점 출력 */
function View_Item(obj) {
  var scode = obj;
  jQuery.ajaxSettings.traditional = true;
  
  /* 초기화 */
  $("#nameArea").empty();
  $("#scoreArea").empty();
  
  $.ajax({
  	type: 'post',
  	url: "../proc/subject_getSubjectInfoBySCode.jsp",
  	data: {
       	"scode" : scode
  	},
      dataType : "json",
		  success: function(success) {
			  if(success) { // 전송 완료 시.
				  if(success.error != null) { // 실패
					  alert(success.error);
					  sendMessage("close");
				  }
				  else {
					var result = success.data;
					
					//document.getElementById("nameArea").html() = "";
					$("#nameArea").html(result.subjectName); // 또는 result[0]
					$("#scoreArea").html(result.score); // 또는 result[0]
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

