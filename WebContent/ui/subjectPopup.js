window.onload = function() {
	jQuery.ajaxSettings.traditional = true;
  document.getElementById("Close_button").onclick = function() {
	  sendMessage("close");
	  	  
  }
  window.addEventListener("message", messageHandler, true);
}

/* 과목명과 학점 출력 */
function View_Item(obj) {
  var scode = obj;
  
  /* 초기화 */
  $("#nameArea").empty();
  $("#scoreArea").empty();
  $("#nameArea").html("");
  $("#scoreArea").html("");
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

function messageHandler(e) {
  var obj = e.data;
  /* 초기화 */
  $("#nameArea").empty();
  $("#scoreArea").empty();
  
  View_Item(obj.pop());
}

function sendMessage(obj) {
  window.parent.postMessage(obj,"*");
}

