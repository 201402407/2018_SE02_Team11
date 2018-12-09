window.onload = function() {
	/* 신청완료 버튼을 클릭 시 신청사유 전송 */
  document.getElementById("addReasonButton").onclick = function() {
    Send_Item();
  }
  document.getElementById("Close_button").onclick = function() {
	  sendMessage("close");
  }
}

function sendMessage(obj) {
  window.parent.postMessage(obj,"*");
}

/* 해당 html을 호출한 부모에게 데이터 전송 */
function Send_Item() {
  var reason = document.getElementById("reasonText").value;
  sendMessage(reason);
}
