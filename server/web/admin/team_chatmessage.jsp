<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
											com.fy.engineserver.chat.*,
											com.xuanzhi.boss.client.*"%>
<%@ page import="com.fy.engineserver.warehouse.service.*,com.fy.engineserver.warehouse.*,com.fy.engineserver.datasource.props.*,com.google.gson.*,java.util.*,com.fy.engineserver.sprite.*,
com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.mail.*,com.fy.engineserver.mail.service.*" %>
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
String message = null;
if(msgContent != null) {
	String player = request.getParameter("player");
	PlayerManager pmanager = PlayerManager.getInstance();
	Player pp = pmanager.getPlayer(player);
	Team team = pp.getTeam();
	ChatMessageService service = ChatMessageService.getInstance();
	ChatMessage msg = new ChatMessage();
	ChatMessageItem item = new ChatMessageItem();
	ChatMessageTask task = new ChatMessageTask();
	msg.setAccessoryItem(item);
	msg.setAccessoryTask(task);
	msg.setChatTime(new Date().getTime());
	msg.setChatType(ChatChannelType.SYSTEM);
	msg.setMessageText(msgContent);
	msg.setSort(ChatChannelType.TEAM);
	service.sendMessageToTeam(team, msg);
	message = "发送成功";
}
if(message != null) {
	out.println(message);
}
 %>
<form action="" name=f1>
	队伍玩家:<input type=text name=player size=20><br>
	消息:<input type=text name=msgContent size=20><br>
	<input type=submit name=sub1 value=" 发送 ">
</form>
</body>
</html>
