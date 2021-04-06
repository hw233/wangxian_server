function showLoading() {
	$.openPopupLayer( {
		name :"myloading",
		target :"loadingDiv"
	});
}
function closeLoading() {
	$.closePopupLayer('myloading');
}
function showTimeOut(msg) {
	$.openPopupLayer( {
		name :"mytimeout",
		width :260,
		target :"timeoutDiv"
	});
	if (msg != null&&msg !="") {
		$("#popupLayer_mytimeout *[id='ajaxMsg']").text(msg);
	}
	// closeLoading();
}
function closeTimeOut() {
	$.closePopupLayer('mytimeout');
	closeLoading();
}
function removeHref(ele) {
	$(ele).find("a").attr("href","javascript:;");
}
function showConfirmPop(msg) {
	$.openPopupLayer( {
		name :"myloading",
		width :400,
		target :"confirmPopDiv"
	});
	if (msg != null) {
		$("#popupLayer_myloading *[id='ajaxMsg']").text(msg);
	}
}

function showConfirmPopup(message) {
	$.openPopupLayer( {
		name :"confirmPoput",
		width :400,
		target :"common_confirm_div"
	});
	if (message != null) {
		$("#popupLayer_confirmPoput *[id='common_confirm_msg']").text(message);
	}
}
function closeConfirmPopup() {
	$.closePopupLayer('confirmPoput');
}