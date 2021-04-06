<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.fy.engineserver.chat.*"%>
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
	}
	
	message = "发送成功";
}
if(message != null) {
	out.println(message+"<br>");
	out.println(msgContent);
}
%>

