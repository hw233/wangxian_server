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
	String jiazuMemID = request.getParameter("jiazuMemId");
	String playerNameStr = request.getParameter("playerName");
	if(jiazuIdStr == null || "".equals(jiazuIdStr.trim()) ||jiazuMemID == null || "".equals(jiazuMemID.trim()) || playerNameStr == null || "".equals(playerNameStr.trim())){
		out.print("<h2>每一项都必填</h2>");
		jiazuIdStr = "";
		jiazuMemID = "";
		playerNameStr = "";
	}else{
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(Long.valueOf(jiazuIdStr));
		if (jiazu == null) {
			out.print("没有该家族，请确认家族ID是否正确:" + jiazuIdStr + "<BR/>");
			return;
		}
		Set<JiazuMember> jiazuMemberSet = JiazuManager.getInstance().getJiazuMember(jiazu.getJiazuID());
		Iterator<JiazuMember>  itor = jiazuMemberSet.iterator();
		long memIdInput = Long.parseLong(jiazuMemID);
		boolean found = false;
		List<JiazuMember> removeList = new ArrayList<JiazuMember>();
		while (itor.hasNext()) {
			JiazuMember jiazuMem = itor.next();
			long memId = jiazuMem.getJiazuMemID();
			if(memIdInput==memId) {
				String playerName = PlayerManager.getInstance().getPlayer(jiazuMem.getPlayerID()).getName();
				if(!playerNameStr.equals(playerName)){
					out.print("<h1>成员ID与玩家角色名不匹配</h1>");
					return;
				}
				removeList.add(jiazuMem);
				itor.remove();
				found = true;
				break;
			}
		}
		
		if (found) {
			for (JiazuMember jm : removeList) {
				long start = System.currentTimeMillis();
				JiazuManager.memberEm.remove(jm);
				jiazu.getMemberSet().remove(jm.getJiazuMemID());
				Player player = PlayerManager.getInstance().getPlayer(jm.getPlayerID());
				if (JiazuManager.logger.isErrorEnabled()) {
					JiazuManager.logger.error("[后台删除有多个成员id的玩家] [家族id:"+jm.getJiazuID()+"] [账号:"+player.getUsername()+"] [角色名:"+player.getName()+"] [id:"+player.getId()+"] [被删除的成员ID:"+jm.getJiazuMemID()+"] [耗时:"+(System.currentTimeMillis()-start)+"ms]");
//					JiazuManager.logger.error("[删除有多个成员id的玩家] [家族id:{}] [账号:{}] [角色名:{}] [id:{}] [ssss:{}] [被删除的成员id:{}] [{}ms]",new Object[]{jm.getJiazuID(),player.getUsername(),player.getName(),player.getId(),jm.getJiazuMemID(),jm.getJiazuMemID(),System.currentTimeMillis()-start});
				}
				out.print("删除成功!");
			}
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
<title>删除家族成员ID</title>
</head>
<body>
	<form action="">
		请输入以下信息：
		家族id:<input type="text" name="jiazuId" value="<%=jiazuIdStr %>"/>
		家族成员id:<input type="text" name="jiazuMemId" value="<%=jiazuMemID %>"/>
		角色名:<input type="text" name="playerName" value="<%=playerNameStr %>" />
		<input type="submit" value="submit" />
	</form>

</body>
</html>