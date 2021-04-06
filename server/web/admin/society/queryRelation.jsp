<%@page import="com.fy.engineserver.marriage.manager.MarriageManager"%>
<%@page import="com.fy.engineserver.marriage.MarriageInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.society.SocialManager"%>
<%@page import="com.fy.engineserver.society.Relation"%>
<%@page import="com.fy.engineserver.society.*"%>
<%@page import="com.fy.engineserver.sprite.*"%>
<%@page import="java.util.*"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看一个人的关系</title>
</head>
<body>

<form action="" name="queryRelation">
	请输入要角色名：<input name="playerName" type="text"/>
	<input type="submit" value="查找"/>
</form>
<form action="">
	请输入要查询的角色名：<input name="playerName1" type="text"/>
	<input type="submit" value="查看无法结婚原因"/>
</form>
<form action="">
	查看上下线通知列表并清除已经不存在的角色,请输入角色名:<input name="playerName2" type="text"/>
	<input type="submit" value="提交"/>
</form>
<form action="">
	添加上下线通知,请输入playerId:给<input name="playerId1" type="text"/>添加<input name="playerId2" type="text"/>的上下线通知
	<input type="submit" value="提交"/>
</form>
<%
String playerName1 = request.getParameter("playerName1");
if(playerName1!=null && !"".equals(playerName1)){
	Player p = PlayerManager.getInstance().getPlayer(playerName1);
	if(p!=null){
		Relation r1 = SocialManager.getInstance().getRelationById(p.getId());
		if(r1 != null){
			if(r1.getMarriageId() > 0){
				Player p1 = null;
				try{
					p1 = PlayerManager.getInstance().getPlayer(r1.getMarriageId());
				}catch (Exception e){
					out.print("玩家的道侣角色已不存在,id:"+r1.getMarriageId()+"<br>");
				}
				if(p1 == null){
					out.print("玩家的道侣角色已不存在.id:"+r1.getMarriageId()+"<br>");
					r1.setMarriageId(-1);
				}else{
					out.print("玩家已有道侣:"+p1.getLogString()+"<br>");
				}
			}
		}else{
			out.print("找不到玩家关系信息!<br>");
		}
		
		MarriageInfo infoto = MarriageManager.getInstance().getMarriageInfoById(p.getId());
		if(infoto!=null){
			if(infoto.getHoldA()>0){
				Player holdA = PlayerManager.getInstance().getPlayer(infoto.getHoldA());
				if(holdA != null)
					out.print("holdA:"+holdA.getLogString()+"<br>");
			}
			if(infoto.getHoldB() > 0){
				Player holdB = PlayerManager.getInstance().getPlayer(infoto.getHoldB());
				if(holdB != null)
					out.print("holdB:"+holdB.getLogString()+"<br>");
			}
		}else{
			out.print("infoto:"+infoto); 
		}
	}
}
%>
<%
String playerName2 = request.getParameter("playerName2");
if(playerName2!=null && !"".equals(playerName2)){
	Player p = PlayerManager.getInstance().getPlayer(playerName2);
	Relation relation = SocialManager.getInstance().getRelationById(p.getId());
	List<Long> noticePlayerId = relation.getNoticePlayerId();
	List<Long> removeList = new ArrayList<Long>();
	for(Long id:noticePlayerId){
		Player pp = null;
		try{
		pp=PlayerManager.getInstance().getPlayer(id);
		out.print(pp.getName()+"<br>");
		}catch(Exception e){
			removeList.add(id);
			out.print(e+","+id+"<br>");
		}
	}
	if(removeList.size()>0){
		for(Long id:removeList){
			noticePlayerId.remove(id);
			relation.setNoticePlayerId(noticePlayerId);
		}
		out.print("已删除"+removeList.size()+"个不存在的角色id");
	}
}
%>
<%
String playerId1 = request.getParameter("playerId1");
String playerId2 = request.getParameter("playerId2");
if(playerId1!=null && !"".equals(playerId1) && playerId2!=null && !"".equals(playerId2)){
	Relation relation = SocialManager.getInstance().getRelationById(Long.valueOf(playerId1));
	List<Long> noticePlayerId = relation.getNoticePlayerId();
	if(noticePlayerId.size()>99){
		out.print("通知人数已达上限，添加失败");
		return;
	}
	if(!noticePlayerId.contains(Long.valueOf(playerId2))){
		noticePlayerId.add(Long.valueOf(playerId2));
		relation.setNoticePlayerId(noticePlayerId);
		out.print("添加成功");
	}
}
%>
	<%

	String name = request.getParameter("playerName");
	if(name == null || name.equals("") )return;
	PlayerManager pm = PlayerManager.getInstance();
	PlayerSimpleInfoManager psm = PlayerSimpleInfoManager.getInstance();
	Player player = null;
	SocialManager sm = SocialManager.getInstance();
	try{
		player = pm.getPlayer(name.trim());
	}catch(Exception e){
		out.print("输入用户不存在");
		return;
	}
	Relation relation = SocialManager.getInstance().getRelationById(player.getId());
	
	%>
	<h2> 0 好友 1结义 2 师 3徒 4 夫妻  -1 没有关系</h2>
	<h2 >好友</h2>
	<%
	relation.reSortFriendList();
	List<Long> online = new ArrayList<Long>(relation.getOnline());
	int onlineNum = online.size();
	List<Long> offline = relation.getOffline();
	int num = onlineNum + offline.size();
	if(num >0){
		online.addAll(offline);
		
		int numbers = 100;
		int pageNum = 1;
		int sumPage = (int)Math.ceil(((1.0*online.size())/numbers));
		if(pageNum < 1 || pageNum > sumPage || sumPage <1)
			return ;
		List<Player_RelatinInfo> list = new ArrayList<Player_RelatinInfo>();
		byte relationShip;
		long id;
		String icon;
		String playerName;
		boolean isonline;
		String mood;
		for(int j = (pageNum -1)*numbers; j< pageNum*numbers;j++){
			if(online.size() <= j )
				break;
			id = online.get(j);
			try {
				Player p = pm.getPlayer(id);
				PlayerSimpleInfo info = psm.getInfoById(id);
				icon = "";
				name = p.getName();
				isonline = false;
				if(j < onlineNum) isonline = true;
				mood = info.getMood();
				relationShip = (byte)sm.getRelationA2B(player.getId(), id);
				list.add(new Player_RelatinInfo(relationShip, id, p.getCareer(),icon, name, isonline, mood));
			} catch (Exception e) {
				out.print(e+"好友<br>");
				e.printStackTrace();
				relation.getOffline().remove(id);
				relation.getFriendlist().remove(id);
			}
		}
		relation.setFriendlist(relation.getFriendlist());
		%>
		<table style="align:center;width:80%;border=1;">
			<tr> <td>id</td> <td>名字</td> <td>关系</td>  <td>在线?</td>  </tr>
		<%
		for(int i=0;i<list.size();i++){
			Player_RelatinInfo info = list.get(i);
			out.print("<tr><td>"+info.getId()+"</td><td>"+info.getName()+"</td><td>"+info.getRelationShip()+"</td><td>"+info.isIsonline()+"<a href=\"delRelation.jsp?id="+player.getId()+"&pid="+info.getId()+"&type="+0+"\">删除</a></td></tr>");
			out.print("<br/>");
		}
		%>
		</table>
		<%
	}else{
		out.print("没有好友");
	}
	%>
	
	<h2 >黑名单</h2>
		<%
		
		relation.resortBlackList();
		online = new ArrayList<Long>(relation.getBlackNameListOnline());
		onlineNum = online.size();
		offline = relation.getBlackNameListOffline();
		num = onlineNum + offline.size();
		if(num >0){
			online.addAll(offline);
			
			int numbers = 10;
			int pageNum = 1;
			int sumPage = (int)Math.ceil(((1.0*online.size())/numbers));
			if(pageNum < 1 || pageNum > sumPage || sumPage <1)
				return ;
			List<Player_RelatinInfo> list = new ArrayList<Player_RelatinInfo>();
			byte relationShip;
			long id;
			String icon;
			String playerName;
			boolean isonline;
			String mood;
			for(int j = (pageNum -1)*numbers; j< pageNum*numbers;j++){
				if(online.size() <= j )
					break;
				id = online.get(j);
				try {
					Player p = pm.getPlayer(id);
					PlayerSimpleInfo info = psm.getInfoById(id);
					icon = "";
					name = p.getName();
					isonline = false;
					if(j < onlineNum) isonline = true;
					mood = info.getMood();
					relationShip = (byte)sm.getRelationA2B(player.getId(), id);
					list.add(new Player_RelatinInfo(relationShip, id, p.getCareer(),icon, name, isonline, mood));
				} catch (Exception e) {
					out.print(e+"黑名单<br>");
					relation.getBlackNameListOffline().remove(id);
					relation.getBlacklist().remove(id);
				}
			}
			relation.setBlacklist(relation.getBlacklist());
			%>
			<table style="align:center;width:80%;border=1;">
				<tr> <td>id</td> <td>名字</td> <td>关系</td>  <td>在线?</td>  </tr>
			<%
			for(int i=0;i<list.size();i++){
				Player_RelatinInfo info = list.get(i);
				out.print("<tr><td>"+info.getId()+"</td><td>"+info.getName()+"</td><td>"+info.getRelationShip()+"</td><td>"+info.isIsonline()+"<a href=\"delRelation.jsp?id="+player.getId()+"&pid="+info.getId()+"&type="+2+"\">删除</a></td></tr>");
				out.print("<br/>");
			}
			%>
			</table>
			<%
		}else{
			out.print("没有黑名单");
		}
	%>
	<h2 >临时好友</h2>
		<%
		relation.resorttemporarylist();
		online = new ArrayList<Long>(relation.getTemporarylistOnline());
		onlineNum = online.size();
		offline = relation.getTemporarylistOffline();
		num = onlineNum + offline.size();
		if(num >0){
			online.addAll(offline);
			
			int numbers = 10;
			int pageNum = 1;
			int sumPage = (int)Math.ceil(((1.0*online.size())/numbers));
			if(pageNum < 1 || pageNum > sumPage || sumPage <1)
				return ;
			List<Player_RelatinInfo> list = new ArrayList<Player_RelatinInfo>();
			byte relationShip;
			long id;
			String icon;
			String playerName;
			boolean isonline;
			String mood;
			for(int j = (pageNum -1)*numbers; j< pageNum*numbers;j++){
				if(online.size() <= j )
					break;
				id = online.get(j);
				try {
					Player p = pm.getPlayer(id);
					PlayerSimpleInfo info = psm.getInfoById(id);
					icon = "";
					name = p.getName();
					isonline = false;
					if(j < onlineNum) isonline = true;
					mood = info.getMood();
					relationShip = (byte)sm.getRelationA2B(player.getId(), id);
					list.add(new Player_RelatinInfo(relationShip, id, p.getCareer(),icon, name, isonline, mood));
				} catch (Exception e) {
					out.print(e+"临时好友<br>");
					e.printStackTrace();
				}
			}
			
			%>
			<table style="align:center;width:80%;border=1;">
				<tr> <td>id</td> <td>名字</td> <td>关系</td>  <td>在线?</td>  </tr>
			<%
			for(int i=0;i<list.size();i++){
				Player_RelatinInfo info = list.get(i);
				out.print("<tr><td>"+info.getId()+"</td><td>"+info.getName()+"</td><td>"+info.getRelationShip()+"</td><td>"+info.isIsonline()+"</td></tr>");
				out.print("<br/>");
			}
			%>
			</table>
			<%
		}else{
			out.print("没有临时好友");
		}
	%>
	<h2 >仇人</h2>
		<%
		relation.resortChourenList();
		online = new ArrayList<Long>(relation.getChourenNameListOnline());
		onlineNum = online.size();
		offline = relation.getChourenNameListOffline();
		num = onlineNum + offline.size();
		if(num >0){
			online.addAll(offline);
			
			int numbers = 10;
			int pageNum = 1;
			int sumPage = (int)Math.ceil(((1.0*online.size())/numbers));
			if(pageNum < 1 || pageNum > sumPage || sumPage <1)
				return ;
			List<Player_RelatinInfo> list = new ArrayList<Player_RelatinInfo>();
			byte relationShip;
			long id;
			String icon;
			String playerName;
			boolean isonline;
			String mood;
			for(int j = (pageNum -1)*numbers; j< pageNum*numbers;j++){
				if(online.size() <= j )
					break;
				id = online.get(j);
				try {
					Player p = pm.getPlayer(id);
					PlayerSimpleInfo info = psm.getInfoById(id);
					icon = "";
					name = p.getName();
					isonline = false;
					if(j < onlineNum) isonline = true;
					mood = info.getMood();
					relationShip = (byte)sm.getRelationA2B(player.getId(), id);
					list.add(new Player_RelatinInfo(relationShip, id, p.getCareer(),icon, name, isonline, mood));
				} catch (Exception e) {
					out.print(e+"仇人<br>");
					e.printStackTrace();
					relation.getTemporarylist().remove(id);
				}
			}
			
			%>
			<table style="align:center;width:80%;border=1;">
				<tr> <td>id</td> <td>名字</td> <td>关系</td>  <td>在线?</td>  </tr>
			<%
			for(int i=0;i<list.size();i++){
				Player_RelatinInfo info = list.get(i);
				out.print("<tr><td>"+info.getId()+"</td><td>"+info.getName()+"</td><td>"+info.getRelationShip()+"</td><td>"+info.isIsonline()+"<a href=\"delRelation.jsp?id="+player.getId()+"&pid="+info.getId()+"&type="+3+"\">删除</a></td></tr>");
				out.print("<br/>");
			}
			%>
			</table>
			<%
		}else{
			out.print("没有仇人");
		}
	%>
<h1>*****************************</h1>
	<form action="addRelation.jsp">
		玩家名：<input type="text" name="addName" />
		玩家id：<input type="text" name="addedId" />
		<input type="text" name="ptype"></input>
		<input type="submit" value="添加好友"/>
	</form>
	
	<form action="addRelation.jsp">
		玩家名：<input type="text" name="addName" />
		玩家id：<input type="text" name="addedId" />
		<input type="text" name="ptype"></input>
		<input type="submit" value="添加黑名单"/>
	</form>

</body>
</html>