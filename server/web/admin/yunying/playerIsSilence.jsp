<%@page import="com.fy.engineserver.chat.ChatMessageService"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String pname = request.getParameter("pname");
	if(pname!=null){
		Player p = PlayerManager.getInstance().getPlayer(pname);
		if(p==null){
			out.print("玩家不存在："+pname);
			return;
		}
		
		int result = ChatMessageService.getInstance().isSilence(p.getId());
		if(result==0){
			out.print("没被沉默");
		}else if(result==1){
			out.print("一级沉默");
		}else if(result==2){
			out.print("耳机沉默");
		}
	}

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查询玩家是否被沉默</title>
</head>
<body>

	<form action="playerIsSilence.jsp" method="post">
		<input type="text" name="pname" value="">
		<input value="提交" type="submit">
	</form>
	
</body>
</html>