<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.boss.authorize.model.Passport"%>
<%@page import="com.fy.boss.client.BossClientService"%>
<%@page import="com.fy.boss.authorize.service.PassportManager"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<head>
<title>test</title>
</head>
<body>

<%

	String name = request.getParameter("name");
	String action = request.getParameter("action");
	String newuserName = request.getParameter("newuserName");
	String psd = request.getParameter("psd");
	psd = psd == null ? "" : psd;
	action = action == null ? "" : action;
	name = name == null ? "" : name;
	newuserName = newuserName == null ? "" : newuserName;
	if ("changeUserName".equals(action)) {
	
		if (!"xiugaiqudaozhanghao".equals(psd)) {
			out.println("密码错误！不知道别乱点!");
			return ;
		}
		if (name.isEmpty() || newuserName.isEmpty()) {
			out.println("输入的角色名或者新账号有误!");
			return ;
		}
		Passport port = BossClientService.getInstance().getPassportByUserName(newuserName);
		if (port == null) {
			out.println("新的账号不存在!!");
			return ;
		}
		List<Player> pList = ((GamePlayerManager)GamePlayerManager.getInstance()).em.query(Player.class, "name=?", new Object[] { name }, "", 1, 10);
		String userName = null;				//账号
		Player player = null;
		if (pList != null && pList.size() > 0) {
			player = pList.get(0);
		}
		if (player == null) {
			out.println("角色不存在！！");
			return ;
		}
		userName = player.getUsername();
		if (userName == null || userName.isEmpty()) {
			out.println("账号不存在！！");
			return ;
		}
		Passport oldPort = BossClientService.getInstance().getPassportByUserName(userName);
		if (oldPort == null) {
			out.println("原的账号不存在!!");
			return ;
		}
		String registChannel = oldPort.getRegisterChannel();
		String newChannel = port.getRegisterChannel();
		long[] ids = ((GamePlayerManager)GamePlayerManager.getInstance()).em.queryIds(Player.class, "username=?", new Object[] { userName }, "", 1, 100);
		if (ids == null || ids.length <= 0) {
			out.println("账号下没角色！");
			return ;
		}
		for (int i=0; i<ids.length; i++) {
			Player p = GamePlayerManager.getInstance().getPlayer(ids[i]);
			p.setUsername(newuserName);
			((GamePlayerManager)GamePlayerManager.getInstance()).em.flush(p);
			Game.logger.warn("[修改角色账号] [成功] [原账号:" + userName + "] [新账号:" + newuserName + "] [oldRegistChannel:"+registChannel+"] [newRegistChannel:"+newChannel+"] [" + p.getLogString() + "]");
			out.println("[修改角色账号] [成功] [原账号:" + userName + "] [新账号:" + newuserName + "] [oldRegistChannel:"+registChannel+"] [newRegistChannel:"+newChannel+"] [" + p.getLogString() + "]<br>");
		}
	}
	
%>

<form action="changusername.jsp" method="post">
	<input type="hidden" name="action" value="changeUserName" />角色名:
	<input type="text" name="name" value="<%=name%>" />新账号名：
	<input type="text" name="newuserName" value="<%=newuserName%>" />密码：
	<input type="password" name="psd" value="<%=psd%>" />
	<input type="submit" value="修改玩家账号" />
</form>


 