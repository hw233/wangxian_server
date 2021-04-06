/**
 * 
 */
function changeTitle(element) {
	document.getElementById("mailTitle").value = element.innerText;

}
function changeContent(element) {
	document.getElementById("mailContent").innerHTML = element.innerText;

}
function getArticle(input,type) {
	var res;
	$.ajax({
		url : 'getArticle.jsp',
		type : "post",
		dataType : "text",
		contentType : "application/x-www-form-urlencoded; charset=utf-8",
		data : 'input=' + input + '&type='+type,
		async: false,
		success : function(message) {
			res = message;
		}
	});
	return res;
}