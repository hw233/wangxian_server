<%@page import="java.util.ArrayList"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.RmbRewardData"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.TotalChongZhiActivity"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.SingleChongZhiActivity"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.FanLi4LongTimeChongZhiActivity"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.FirstChongZhiActivity"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<%
	String serverName = GameConstants.getInstance().getServerName();
	ArrayList<String> unServerName = new ArrayList<String>();
	unServerName.add("国内本地测试");
	unServerName.add("pan2");
	unServerName.add("ST");
	unServerName.add("化外之境");
	unServerName.add("仙尊降世");
	if (!unServerName.contains(serverName)) {

	String userName = (String)session.getAttribute("username");
	if(userName == null){

		String action = request.getParameter("action");
		if(action != null && action.equals("login")){
			String u = request.getParameter("username");
			String p = request.getParameter("password");

			if(u != null && p != null && u.equals("wtx") && p.equals("wtx111") ){
				userName = u;
				session.setAttribute("username",userName);
			}else{
				out.println("<h3>用户名密码不对</h3>");
			}
		}

		if(userName == null){
%>
	<center>
		<h1>请先登录</h1>
		<form>
		<input type="hidden" name="action" value="login">
		请输入用户名：<input type="text" name="username" value="" size=30><br/>
		请输入密码：<input type="text" name="password" value="" size=30><br/>
		<input type="submit" value="提  交">
		</form>
	</center>
<%
		return;
		}
	}
	}
%>

	<%
		String action = request.getParameter("action");
		if (action != null) {
			if (action.equals("backExcel")) {
				NewChongZhiActivityManager.instance.allRmbDatas.clear();
				NewChongZhiActivityManager.instance.loadChangQiFile();
				NewChongZhiActivityManager.instance.findSelfChangQiActivity();
			}else if (action.equals("text_m")) {
				Player player = PlayerManager.getInstance().getPlayer(request.getParameter("pName"));
				NewChongZhiActivityManager.instance.doChongZhiActivity(player, Long.parseLong(request.getParameter("money")));
			}else if (action.equals("clean_one")) {
				Player player = PlayerManager.getInstance().getPlayer(request.getParameter("pName"));
				int id = Integer.parseInt(request.getParameter("aID"));
				if (NewChongZhiActivityManager.instance.shouchongRmbData.getDataID() == id ) {
					NewChongZhiActivityManager.instance.shouchongRmbData.playerLeiJiMoneys.remove(player.getId());
					NewChongZhiActivityManager.instance.shouchongRmbData.playerGetReward.remove(player.getId());
				}else {
					for (int i = 0; i < NewChongZhiActivityManager.instance.rmbDatas.size(); i++) {
						if (NewChongZhiActivityManager.instance.rmbDatas.get(i).getDataID() == id ) {
							NewChongZhiActivityManager.instance.rmbDatas.get(i).playerLeiJiMoneys.remove(player.getId());
							NewChongZhiActivityManager.instance.rmbDatas.get(i).playerGetReward.remove(player.getId());
						}
					}
				}
			}
		}
	%>
	<form name="f1">
		重新载入
		<input type="hidden" name="action" value="backExcel">
		<input type="submit" value=<%="确定"%>>
	</form>
	<br>
	<table border="2">
		<tr>
			<td>活动ID</td>
			<td>金额</td>
			<td>平台</td>
			<td>开启服务器</td>
			<td>非开启服务器</td>
			<td>邮件标题</td>
			<td>邮件内容</td>
			<td>奖励物品名字</td>
			<td>奖励物品ID</td>
			<td>数量</td>
			<td>颜色</td>
			<td>珍贵</td>
			<td>绑定</td>
		</tr>
		<%
			if (NewChongZhiActivityManager.instance.shouchongRmbData != null) {
				long[] ids = new long[NewChongZhiActivityManager.instance.shouchongRmbData.getTempEntitys().length];
				int j = 0;
				for (ArticleEntity en : NewChongZhiActivityManager.instance.shouchongRmbData.getTempEntitys()) {
					ids[j] = en.getId();
					j++;
				}
		%>
		<tr>
			<td><%=NewChongZhiActivityManager.instance.shouchongRmbData.getDataID() %></td>
			<td><%=NewChongZhiActivityManager.instance.shouchongRmbData.getNeedMoney() %></td>
			<td><%=NewChongZhiActivityManager.instance.shouchongRmbData.getPlatform() %></td>
			<td><%=Arrays.toString(NewChongZhiActivityManager.instance.shouchongRmbData.getServerNames()) %></td>
			<td><%=Arrays.toString(NewChongZhiActivityManager.instance.shouchongRmbData.getUnServerNames()) %></td>
			<td><%=NewChongZhiActivityManager.instance.shouchongRmbData.getMailMsg() %></td>
			<td><%=NewChongZhiActivityManager.instance.shouchongRmbData.getMailTitle() %></td>
			<td><%=Arrays.toString(NewChongZhiActivityManager.instance.shouchongRmbData.getRewardPropNames()) %></td>
			<td><%=Arrays.toString(ids) %></td>
			<td><%=Arrays.toString(NewChongZhiActivityManager.instance.shouchongRmbData.getRewardPropNums()) %></td>
			<td><%=Arrays.toString(NewChongZhiActivityManager.instance.shouchongRmbData.getRewardColors()) %></td>
			<td><%=Arrays.toString(NewChongZhiActivityManager.instance.shouchongRmbData.getRewardRare()) %></td>
			<td><%=Arrays.toString(NewChongZhiActivityManager.instance.shouchongRmbData.getRewardBinds()) %></td>
		</tr>
		<%
			}
			for (int i = 0; i < NewChongZhiActivityManager.instance.rmbDatas.size(); i++) {
				RmbRewardData da = NewChongZhiActivityManager.instance.rmbDatas.get(i);
				long[] ids = new long[da.getTempEntitys().length];
				int j = 0;
				for (ArticleEntity en : da.getTempEntitys()) {
					ids[j] = en.getId();
					j++;
				}
		%>
		<tr>
			<td><%=da.getDataID() %></td>
			<td><%=da.getNeedMoney() %></td>
			<td><%=da.getMailMsg() %></td>
			<td><%=da.getMailTitle() %></td>
			<td><%=Arrays.toString(da.getRewardPropNames()) %></td>
			<td><%=Arrays.toString(ids) %></td>
			<td><%=Arrays.toString(da.getRewardPropNums()) %></td>
			<td><%=Arrays.toString(da.getRewardColors()) %></td>
			<td><%=Arrays.toString(da.getRewardRare()) %></td>
			<td><%=Arrays.toString(da.getRewardBinds()) %></td>
		</tr>
		<%
			}
		%>
	</table>
	<br>
	<br>
	<form name="f2">
		测试
		<input type="hidden" name="action" value="text_m">
		玩家名字<input name="pName">
		money<input name="money">
		<input type="submit" value=<%="确定"%>>
	</form>
	<br>
	<form name="f3">
		清除领取数据
		<input type="hidden" name="action" value="clean_one">
		活动ID<input name="aID">
		玩家名字<input name="pName">
		<input type="submit" value=<%="确定"%>>
	</form>
</body>
</html>
