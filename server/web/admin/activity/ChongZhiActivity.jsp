<%@page import="com.fy.engineserver.mail.MailSendType"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.activity.chongzhiActivity.ChongZhiSpecialActivity"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.sqage.stat.commonstat.entity.ChongZhi"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.activity.chongzhiActivity.ChongZhiServerConfig"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.activity.chongzhiActivity.ChongZhiActivity"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.newtask.Task"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
	String ip = request.getRemoteAddr();
	String recorder = "";
	Object o = session.getAttribute("authorize.username");
	if(o!=null){
		recorder = o.toString();
	}
		String action = request.getParameter("action");
		if (action != null && "xiugai".equals(action) ) {
			String index_str = request.getParameter("configIndex");
			String id_str = request.getParameter("activityID");
			String serverName_str = request.getParameter("serverName");
			String activityType_str = request.getParameter("activityType");
			String start_str = request.getParameter("startTime");
			String end_str = request.getParameter("endTime");
			String money_str = request.getParameter("money");
			String propName_str = request.getParameter("propName");
			String propNum_str = request.getParameter("propNum");
			String propColor_str = request.getParameter("propColor");
			String propBind_str = request.getParameter("propBind");
			String mailTitle_str = request.getParameter("mailTitle");
			String mailMsg_str = request.getParameter("mailMsg");
			if (index_str == null || id_str == null || serverName_str == null || activityType_str == null || start_str == null
					 || end_str == null || money_str == null || propName_str == null || propNum_str == null
					 || propColor_str == null || propBind_str == null || mailTitle_str == null || mailMsg_str == null) {
				out.print("请把信息填完整");
			}else {
				try{
					int index = Integer.parseInt(index_str);
					int activityID = Integer.parseInt(id_str);
					int activityType = Integer.parseInt(activityType_str);
					long money = Long.parseLong(money_str);
					int propNum = Integer.parseInt(propNum_str);
					int propColor = Integer.parseInt(propColor_str);
					boolean propBind = Integer.parseInt(propBind_str) == 0;
					if (index >= 0) {
						ChongZhiActivity.getInstance().chanageOne(index, activityID, serverName_str, activityType, start_str, end_str, money, propName_str, propNum, propColor, propBind, mailTitle_str, mailMsg_str);
					}else {
						ChongZhiActivity.getInstance().createOne(activityID, serverName_str, activityType, start_str, end_str, money, propName_str, propNum, propColor, propBind, mailTitle_str, mailMsg_str);
					}
				}catch(Exception e) {
					out.print("某些信息输入格式有问题");
				}
			}
			response.sendRedirect("./ChongZhiActivity.jsp");
		}else if (action != null && "ceshi".equals(action)) {
			String playerName = request.getParameter("playerName");
			String money_str = request.getParameter("money");
			long money = Long.parseLong(money_str);
			try{
				Player player = PlayerManager.getInstance().getPlayer(playerName);
				ChongZhiActivity.getInstance().doChongZhiActivity(player, money);
			}catch(Exception e) {
				out.print(e);
			}
			response.sendRedirect("./ChongZhiActivity.jsp");
		}else if (action != null && "start_end".equals(action)) {
			ChongZhiActivity.getInstance().setChongZhiActivityStart(!ChongZhiActivity.getInstance().isChongZhiActivityStart());
			response.sendRedirect("./ChongZhiActivity.jsp");
		}else if (action != null && "removeActivity".equals(action)) {
			String removeIndex_str = request.getParameter("removeIndex");
			if (removeIndex_str == null) {
				out.print("请输入要移除的Index");
			}else {
				try{
					int removeIndex = Integer.parseInt(removeIndex_str);
					if (removeIndex >=0 && removeIndex < ChongZhiActivity.getInstance().chongZhiServerConfigs.size()){
						ChongZhiActivity.getInstance().chongZhiServerConfigs.remove(removeIndex);
					}
				}catch(Exception e) {
					out.print(e);
				}
			}
			response.sendRedirect("./ChongZhiActivity.jsp");
		}else if (action != null && action.equals("refToday")) {
			ChongZhiActivity.getInstance().setToday_data(-1);
		}else if (action != null && action.equals("setTotalTime")) {
			if (ChongZhiActivity.getInstance().fanliActivity != null) {
				String startTime = request.getParameter("startTime");
				String endTime = request.getParameter("endTime");
				String linquTime = request.getParameter("linquTime");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				long start_time = 0;
				long end_time = 0;
				long linqu_time = 0;
				try{
					start_time = format.parse(startTime).getTime();
					end_time = format.parse(endTime).getTime();
					linqu_time = format.parse(linquTime).getTime();
				}catch(Exception e) {
					
				}
				if (start_time != 0 && end_time != 0 && linqu_time != 0) {
					ChongZhiActivity.getInstance().fanliActivity.setStart_time(start_time);
					ChongZhiActivity.getInstance().fanliActivity.setStartTime(startTime);
					ChongZhiActivity.getInstance().fanliActivity.setEnd_time(end_time);
					ChongZhiActivity.getInstance().fanliActivity.setEndTime(endTime);
					ChongZhiActivity.getInstance().fanliActivity.setLingqu_Time(linqu_time);
					ChongZhiActivity.getInstance().fanliActivity.setLingquTime(linquTime);
				}
			}
			response.sendRedirect("./ChongZhiActivity.jsp");
		}else if (action != null && action.equals("spetial_activity")) {
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long start_time = 0;
			long end_time = 0;
			try{
				start_time = format.parse(startTime).getTime();
				end_time = format.parse(endTime).getTime();
			}catch(Exception e) {
			}
			if (start_time != 0 && end_time != 0) {
				ChongZhiActivity.getInstance().specialActivity.setStartTime(startTime);
				ChongZhiActivity.getInstance().specialActivity.setStart_time(start_time);
				ChongZhiActivity.getInstance().specialActivity.setEndTime(endTime);
				ChongZhiActivity.getInstance().specialActivity.setEnd_time(end_time);
			}
			String money1 = request.getParameter("money1");
			String money2 = request.getParameter("money2");
			if (money1 != null && money2 != null) {
				try{
					long money_l1 = Long.parseLong(money1);
					long money_l2 = Long.parseLong(money2);
					ChongZhiActivity.getInstance().specialActivity.setMoney(money_l1);
					ChongZhiActivity.getInstance().specialActivity.setYuMoney(money_l2);
				}catch(Exception e) {
				}
			}
			response.sendRedirect("./ChongZhiActivity.jsp");
		}else if (action != null && action.equals("fayoujian")) {
			String shuru = request.getParameter("shuru");
			if (shuru!= null && shuru.equals("wtx")) {
				Player[] ps = PlayerManager.getInstance().getOnlinePlayers();
				for (Player s : ps) {
					try{
						Article a = ArticleManager.getInstance().getArticle(Translate.银锭);
						ArticleEntity entity = ArticleEntityManager.getInstance().createEntity(a, false, ArticleEntityManager.活动, null, -1, 1, true);
						MailManager.getInstance().sendMail(s.getId(), new ArticleEntity[]{entity}, "庆祝登陆玩家破5000", "亲爱的仙友，为庆祝飘渺寻仙曲绿色服仙友数量突破5000人，特赠送银锭1个，使用后将获得银票1000两，可用于购买商城物品、祈福以及日常系统消耗。请在附件查收！", 0,0,0,"绿色服活动",MailSendType.后台发送,s.getName(),ip,recorder);
						out.println(s.getId() + "----" + s.getName());
						%>
						<br>
						<%
					}catch(Exception e) {
						System.out.println(e);
					}
				}
			}
		}
	%>
	参与首次充值的玩家:<br>
	<%
		for (int i = 0; i < ChongZhiActivity.getInstance().getFirstplayers().size(); i++) {
			long fid = ChongZhiActivity.getInstance().getFirstplayers().get(i);
			%>
			<%=fid %><br>
			<%
		}
	%>
	<br>
	<br>
	参加累计充值返利活动的玩家:<br>
	<%
		for (Long key : ChongZhiActivity.getInstance().getTotalChongZhiMoney2Id().keySet()) {
			long money = ChongZhiActivity.getInstance().getTotalChongZhiMoney2Id().get(key);
			%>
			id=<%=key %><%="   money=" %><%=money %><br>
			<%
		}
	%>
	<br>
	参加每天充值返利活动的玩家:<br>
	<%
		for (Long key : ChongZhiActivity.getInstance().getTodayChongZhiMoney2Id().keySet()) {
			long money = ChongZhiActivity.getInstance().getTodayChongZhiMoney2Id().get(key);
			%>
			id=<%=key %><%="   money=" %><%=money %><br>
			<%
		}
	%>
	<table border="1">
		<tr>
			<td>index </td>
			<td>id </td>
			<td>服务器名 </td>
			<td>活动类型 </td>
			<td>开启时间 </td>
			<td>结束时间 </td>
			<td>金额RMB </td>
			<td>物品名称 </td>
			<td>数量 </td>
			<td>颜色 </td>
			<td>是否绑定 </td>
			<td>邮件title </td>
			<td>是否开启 </td>
		</tr>
	<%
		for (int i = 0; i < ChongZhiActivity.getInstance().chongZhiServerConfigs.size(); i++) {
			ChongZhiServerConfig chongZhiConfig = ChongZhiActivity.getInstance().chongZhiServerConfigs.get(i);
			%>
			<tr>
				<td><%=i %> </td>
				<td><%=chongZhiConfig.getActivityID() %> </td>
				<td><%=chongZhiConfig.getServerName() %> </td>
				<td><%=ChongZhiServerConfig.CHONGZHI_TYPE_STR[chongZhiConfig.getType()] %> </td>
				<td><%=chongZhiConfig.getStartTime() %> </td>
				<td><%=chongZhiConfig.getEndTime() %> </td>
				<td><%=chongZhiConfig.getMoney() %> </td>
				<td><%=chongZhiConfig.getPropName() %> </td>
				<td><%=chongZhiConfig.getPropNum() %> </td>
				<td><%=chongZhiConfig.getColorType() %> </td>
				<td><%=chongZhiConfig.isBind() %> </td>
				<td><%=chongZhiConfig.getMailTitle() %> </td>
				<td><%=chongZhiConfig.isStart()&&(chongZhiConfig.getServerName().equals(GameConstants.getInstance().getServerName()) || chongZhiConfig.getServerName().equals(ChongZhiServerConfig.ALL_SERVER_NAME)) %> </td>
			</tr>
			<%
		}
	%>
	</table>
	<form name="f1">
		<input type="hidden" name="action" value="xiugai">
		第几个(如果index小于0是添加)<input name="configIndex">
		id(不能 重复)<input name="activityID">
		服务器<input name="serverName">
		活动类型<input name="activityType">
		开启时间<input name="startTime" value="2012-01-01 00:00:00">
		结束时间<input name="endTime" value="2012-01-01 00:00:00"><br>
		金额<input name="money">
		物品名称<input name="propName">
		数量<input name=propNum>
		颜色(-1为策划配的颜色)<input name="propColor">
		绑定(0绑定 1不绑定)<input name="propBind"><br>
		邮件标题<input name="mailTitle">
		邮件内容<input name="mailMsg">
		<input type="submit" value="修改或添加">
	</form>
	<br>
	<br>
	<form name="f2">
		<input type="hidden" name="action" value="removeActivity">
		第几个<input name="removeIndex">
		<input type="submit" value="删除某个互动">
	</form>
	<br>
	<br>
	<form name="f3">
		<input type="hidden" name="action" value="ceshi">
		玩家名字<input name="playerName">
		金额银子<input name="money">
		<input type="submit" value="测试功能">
	</form>
	
	<form name="f5">
		<input type="hidden" name="action" value="refToday">
		用来测试那个每天返利活动<input type="submit" value="刷新当天">
	</form>
	
	<form name="f6">
		<input type="hidden" name="action" value="setTotalTime">
		<%	String aTime = ChongZhiActivity.getInstance().fanliActivity == null ? "2013-01-18 17:00:00" : ChongZhiActivity.getInstance().fanliActivity.getStartTime();
			String bTime = ChongZhiActivity.getInstance().fanliActivity == null ? "2013-01-18 17:00:00" : ChongZhiActivity.getInstance().fanliActivity.getEndTime();
			String cTime = ChongZhiActivity.getInstance().fanliActivity == null ? "2013-01-18 17:00:00" : ChongZhiActivity.getInstance().fanliActivity.getLingquTime();
		%>
		开始<input name="startTime" type="text" value="<%=aTime %>">
		结束<input name="endTime" type="text" value="<%=bTime %>">
		领取<input name="linquTime" type="text" value="<%=cTime %>">
		<input type="submit" value="修改累计充值活动时间">
	</form>
	
	<form name="f4">
		这个是整个活动开关，除非出BUG，尽量别用
		<input type="hidden" name="action" value="start_end">
		<input type="submit" value=<%=ChongZhiActivity.getInstance().isChongZhiActivityStart() ? "关闭活动" : "开启活动" %>>
	</form>
	
	<br>
	<br>
	<br>
	<%
	ChongZhiSpecialActivity activity = ChongZhiActivity.getInstance().specialActivity;
	if (activity != null) {
	%>
		<table border="1">
			<tr>
				<td><%=activity.getServerName() %></td>
				<td><%=ChongZhiServerConfig.CHONGZHI_TYPE_STR[activity.getType()] %></td>
				<td><%=activity.getStartTime() %></td>
				<td><%=activity.getEndTime() %></td>
				<td><%=activity.getMoney() %></td>
				<td><%=activity.getPropName() %></td>
				<td><%=activity.getYuMoney() %></td>
				<td><%=activity.getYuPropName() %></td>
				<td><%=activity.getMailTitle() %></td>
				<td><%=activity.isStart() %></td>
			</tr>
		</table>
	<%
	}
	%>
	<br>
	<form name="f4">
		修改特殊充值活动
		<input type="hidden" name="action" value="spetial_activity">
		开始<input name="startTime" type="text" value="<%="2013-02-18 00:00:00" %>">
		结束<input name="endTime" type="text" value="<%="2013-02-18 23:50:00" %>">
		金额1<input name="money1" type="text">
		金额2<input name="money2" type="text">
		<input type="submit" value=<%="确定"%>>
	</form>
	<br>
	<form name="f111">
		发在线邮件
		<input type="hidden" name="action" value="fayoujian">
		输入wtx<input name="shuru" type="text">
		<input type="submit" value=<%="确定"%>>
	</form>
</body>
</html>
