<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
											com.fy.engineserver.chat.*"%>
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script language="JavaScript">
<!--
-->
</script>
</head>
<body>
<%
String msgContent = request.getParameter("msgContent");
String selected = request.getParameter("selected");
String message = null;
if(msgContent != null) {
	ChatMessageService service = ChatMessageService.getInstance();
	ChatMessage msg = new ChatMessage();
	ChatMessageItem item = new ChatMessageItem();
	ChatMessageTask task = new ChatMessageTask();
	msg.setAccessoryItem(item);
	msg.setAccessoryTask(task);
	msg.setChatTime(new Date().getTime());
	msg.setChatType(ChatChannelType.SYSTEM);
	msg.setMessageText(msgContent);
	msg.setSort(ChatChannelType.SYSTEM);
	if(selected.trim().equals("2")){
		service.sendMessageToSystem(msg);
	}else if(selected.trim().equals("1")){
		service.sendHintMessageToSystem(msg);
	}else if(selected.trim().equals("0")){
		service.sendRoolMessageToSystem(msg);
	}else if(selected.trim().equals("3")){
		service.sendMessageToWorld(msg);
	}else if(selected.trim().equals("4")){
		service.sendColorMessageToWorld(msg);
	}else if (selected.trim().equals("5")) {
		service.sendMessageToServer(msgContent);
	}
	
	message = "发送成功";
}
if(message != null) {
	out.println(message+"<br>");
//	out.println(msgContent);
}
 %>
<form action="" name=f1>
	消息:<textarea name=msgContent style="width:300px;height:200px;"></textarea>
	<select name="selected">
	<option value="0">系统滚动消息
	<option value="1">系统提示消息
	<option value="2">系统电视消息
	<option value="3">世界频道
	<option value="4">彩色世界频道
	<option value="5">新系统专用频道
	</select>
	<input type=submit name=sub1 value=" 发送 ">
</form>
</body>
</html>
