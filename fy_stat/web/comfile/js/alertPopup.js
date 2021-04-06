function openStaticPopup(test,width) {
	/*$.setupJMPopups({
		screenLockerBackground: "#ccc",
		screenLockerOpacity: "0.1"
	});*/
	$.openPopupLayer({
		name: "myStaticPopup_"+test,
		width: width,
		target: "myHiddenDiv_"+test+""
	});
}