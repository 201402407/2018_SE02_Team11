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
  /* 초기화 */
  $("#Text").html("");
  
  /* 성공 버튼 클릭 시 */
  $('#SuccessButton').click(function () {
	  sendEvaluation(obj, $("#Text").val());
  });
}

/* DB에 전송 */
function sendEvaluation(attnum, evaltext) {
	$.ajax({
	  	type: 'post',
	  	url: "../proc/attendance_doLectureEvaluation.jsp",
	  	data: {
	       	"attNum" : attnum,
	       	"evaltext" : evaltext
	  	},
	      dataType : "json",
			  success: function(success) {
				  if(success) { // 전송 완료 시.
					  if(success.error != null) { // 실패
						  alert(success.error);
					  }
					  else {
						// var result = success.data;
						  alert("한 학기동안 수고 많으셨습니다.");
						sendMessage("close");
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

