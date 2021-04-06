<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.engineserver.jiazu.JiazuMember"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String jiazuIdStr = request.getParameter("jiazuId");
//	String jiazuMemID = request.getParameter("jiazuMemId");
	String playerNameStr = request.getParameter("playerName");
	String contributionStr = request.getParameter("contribution");
	if(jiazuIdStr == null || "".equals(jiazuIdStr.trim()) || playerNameStr == null || "".equals(playerNameStr.trim()) ||contributionStr == null || "".equals(contributionStr.trim())){
		out.print("<h2>每一项都必填</h2>");
		jiazuIdStr = "";
		playerNameStr = "";
		contributionStr = "";
	}else{
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(Long.valueOf(jiazuIdStr));
		if (jiazu == null) {
			out.print("没有该家族，请确认家族ID是否正确:" + jiazuIdStr + "<BR/>");
			return;
		}
		Set<JiazuMember> jiazuMemberSet = JiazuManager.getInstance().getJiazuMember(jiazu.getJiazuID());
		Iterator<JiazuMember>  itor = jiazuMemberSet.iterator();
		int addContribution = Integer.parseInt(contributionStr);
		boolean found = false;
		JiazuMember jm = new JiazuMember();
		while (itor.hasNext()) {
			JiazuMember jiazuMem = itor.next();
			String playerName = PlayerManager.getInstance().getPlayer(jiazuMem.getPlayerID()).getName();
			if(playerNameStr.equals(playerName)){
				jiazuMem.setCurrentWeekContribution(jiazuMem.getCurrentWeekContribution()+addContribution);
				jm = jiazuMem;
				found = true;
				break;
			}
		}
		
		
		if (found) {
			long start = System.currentTimeMillis();
			Player player = PlayerManager.getInstance().getPlayer(jm.getPlayerID());
			if (JiazuManager.logger.isErrorEnabled()) {
				JiazuManager.logger.error("[后台增加玩家贡献度] [家族id:"+jm.getJiazuID()+"] [账号:"+player.getUsername()+"] [角色名:"+player.getName()+"] [id:"+player.getId()+"] [增加贡献度:"+addContribution+"] [耗时:"+(System.currentTimeMillis()-start)+"ms]");
			}
			out.print("已成功为玩家增加"+addContribution+"贡献度!<BR/>");
			jiazu.setMemberSet(jiazu.getMemberSet());
			jiazu.initMember();
		} else {
			out.print("未找到相关信息,请确认输入的值是否正确<BR/>");
		}
	}
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>增加贡献度</title>
</head>
<body>
	<form action="">
		请输入以下信息：
		家族id:<input type="text" name="jiazuId" value="<%=jiazuIdStr %>"/>
		角色名:<input type="text" name="playerName" value="<%=playerNameStr %>" />
		要增加的贡献度：<input type="text" name="contribution" value="<%=contributionStr %>" />
		<input type="submit" value="submit" />
	</form>

</body>
</html>