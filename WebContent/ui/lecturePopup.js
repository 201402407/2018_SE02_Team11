window.onload = function() {
	
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

function sendMessage(obj) {
  window.parent.postMessage(obj,"*");
}

