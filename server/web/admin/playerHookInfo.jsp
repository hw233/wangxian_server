<%@page import="com.fy.engineserver.hook.PlayerHookInfo"%>
<%@page import="com.fy.engineserver.hook.PlayerHookManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		String action = request.getParameter("action");
		if (action != null && !"".equals(action)) {
			if (action.equals("getInfo")) {
				String pIDS = request.getParameter("pID");
				if (pIDS != null) {
					Player player = null;
					try{
						long pid = Long.parseLong(pIDS);
						player = PlayerManager.getInstance().getPlayer(pid);
					}catch(Exception e) {
						
					}
					if (player != null) {
						PlayerHookInfo info = PlayerHookManager.instance.hookInfos.get(player.getId());
						if (info == null) {
							out.print("未找到");
						}else {
							out.print("" + info.getLogString());
							%>
							<form name="f2">
								<input type="hidden" name="action" value="sendMsg"/>
								<input type="text" name="pID" value=<%=pIDS %>>
								<input type="submit" value="关闭挂机" />
							</form>
							<%
						}
					}else {
						out.print("未找到玩家");
					}
				}
			}else if (action.equals("sendMsg")) {
				String pIDS = request.getParameter("pID");
				if (pIDS != null) {
					Player player = null;
					try{
						long pid = Long.parseLong(pIDS);
						player = PlayerManager.getInstance().getPlayer(pid);
					}catch(Exception e) {
						
					}
					if (player != null) {
						PlayerHookManager.instance.sendHookRes(player, false, false, false, "妙觉丸","本初露");
					}
				}
			}
		}
	%>
	
	<form name="f1">
		<input type="hidden" name="action" value="getInfo"/>
		请输入玩家ID<input type="text" name="pID">
		<input type="submit" value="查询" />
	</form>
</body>
</html>