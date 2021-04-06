<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
											com.fy.engineserver.chat.*,
											com.fy.boss.client.*"%>
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>发广播</title>
<script language="JavaScript">
<!--
-->
</script>
</head>
<body>
<%
String msgContent = request.getParameter("msgContent");
String selected = request.getParameter("selected");
String selected2 = request.getParameter("selected2");
String message = null;
if(msgContent != null) {
	ChatMessageService service = ChatMessageService.getInstance();
	ChatMessage msg = new ChatMessage();
	msg.setChatTime(new Date().getTime());
	msg.setChatType(ChatChannelType.COUNTRY);
	msg.setMessageText(msgContent);
	msg.setSort(ChatChannelType.COUNTRY);
	if(selected2.trim().equals("0")){
		service.sendMessageToCountry(Integer.parseInt(selected),msg);
	}else if(selected2.trim().equals("1")){
		service.sendMessageToCountry2(Integer.parseInt(selected),msg);
	}
	
	message = "发送成功";
}
if(message != null) {
	out.println(message+"<br>");
	out.println(msgContent);
}
 %>
<form action="" name=f1>
	<h1>发送国家消息</h1><br>
	<select name="selected">
	<option value="1">昊天频道
	<option value="2">无尘频道
	<option value="3">沧月频道
	</select>
	<select name="selected2">
	<option value="0">电视广播消息
	<option value="1">屏幕下方的聊天窗口
	</select><input type=submit name=sub1 value=" 发送 "><br>
	<textarea name=msgContent style="width:300px;height:200px;"></textarea>
	
</form>
</body>
</html>
