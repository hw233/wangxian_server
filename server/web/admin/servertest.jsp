<%@page import="java.util.Arrays"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.activity.loginActivity.LoginActivityManager"%>
<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<style type="text/css">
</style>
		<script type="text/javascript">
	
</script>
	</head>
	<body>
		<br>
			是否是台湾平台：<%=PlatformManager.getInstance().isPlatformOf(Platform.台湾) %><br>
			是否open：<%=LoginActivityManager.getInstance().isOpen() %><br>
			游戏名字：<%=GameConstants.getInstance().getServerName() %><br>
			未开放游戏：<%=Arrays.toString(LoginActivityManager.notOpenServers.toArray(new String[]{})) %><br>
			是否是开放的游戏服：<%=LoginActivityManager.notOpenServers.contains(GameConstants.getInstance().getServerName()) %><br>
	</body>
</html>
