<%@page import="com.fy.engineserver.util.server.TestServerConfigManager"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Props"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.buff.Buff"%>
<%@page import="com.fy.engineserver.battlefield.jjc.JJCBillboardData"%>
<%@page import="com.fy.engineserver.core.LivingObject"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.battlefield.BattleField"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.battlefield.jjc.BattleTeam"%>
<%@page import="com.fy.engineserver.battlefield.jjc.BattleTeamMember"%>
<%@page import="com.fy.engineserver.battlefield.jjc.JJCManager"%>
<%@page import="com.fy.engineserver.util.whitelist.WhiteListManager"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="com.fy.engineserver.datasource.skill.master.SkEnConf"%>
<%@page import="com.fy.engineserver.datasource.skill.master.SkEnhanceManager"%>
<%@page import="com.fy.engineserver.datasource.skill.master.SkBean"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String pname = request.getParameter("pname");
String jjcpoints = request.getParameter("jjcpoints");
String starthour = request.getParameter("starthour");
String endhour = request.getParameter("endhour");
String startminute = request.getParameter("startminute");
String endminute = request.getParameter("endminute");
String memberbattlenums = request.getParameter("memberbattlenums");
String memberbattlelevel = request.getParameter("memberbattlelevel");
String teambattlenums = request.getParameter("teambattlenums");
String teambattlelevel = request.getParameter("teambattlelevel");
String flagName = request.getParameter("flagName");
String comebackflagName = request.getParameter("comebackflagName");
String calJJcPoints = request.getParameter("calJJcPoints");
String pkmode = request.getParameter("pkmode");
String showBuffs = request.getParameter("showBuffs");
String showPwd = request.getParameter("showPwd");
String clearLevel = request.getParameter("clearLevel");
String clearLevel2 = request.getParameter("clearLevel2");
String costsilver = request.getParameter("costsilver");
String currservertime = request.getParameter("currservertime");
String changeTeamName = request.getParameter("changeTeamName");
if(!TestServerConfigManager.isTestServer()){
	out.print("无权操作");
	return;
}
if(pname!=null && !pname.isEmpty()){
	Player player = PlayerManager.getInstance().getPlayer(pname);
	if(player==null){
		out.print("玩家不存在");
		return;
	}
	
	if(changeTeamName!=null && !changeTeamName.isEmpty()){
		BattleTeam bt = JJCManager.getInstance().getTeam(player, 1, "后台修改战队名");
		if(bt == null){
			out.print("玩家："+player.getName()+"没有战队<br>");
			return;
		}
		String oldTeamName = bt.getTeamName();
		bt.setTeamName(changeTeamName);
		out.print("[修改玩家战队名字] [成功] [玩家:"+player.getName()+"] [名字变化："+oldTeamName+"-->"+bt.getTeamName()+"]<br>");
	}
	if(jjcpoints!=null && !jjcpoints.isEmpty()){
		long oldpoints = player.getJjcPoint();
		player.setJjcPoint(Long.parseLong(jjcpoints));
		out.print("[修改玩家血石] [成功] [玩家:"+player.getName()+"] [血石变化："+oldpoints+"-->"+player.getJjcPoint()+"]<br>");
	}
	if(memberbattlenums!=null && !memberbattlenums.isEmpty()){
		BattleTeamMember btm = JJCManager.getInstance().getPlayerInfo(player.getId(), 1);
		if(btm == null){
			out.print("[修改个人参赛场数] [失败:获取成员信息失败] [player:"+player.getLogString()+"] [memberbattlenums:"+memberbattlenums+"]<br>");
		}else{
			int oldnums = btm.getBattleAllNum();
			btm.setBattleAllNum(Integer.parseInt(memberbattlenums));
			out.print("[修改个人参赛场数] [成功] [玩家:"+player.getName()+"] [个人参赛总场数变化:"+oldnums+"-->"+btm.getBattleAllNum()+"]<br>");
		}
	}
	if(memberbattlelevel!=null && !memberbattlelevel.isEmpty()){
		BattleTeamMember btm = JJCManager.getInstance().getPlayerInfo(player.getId(), 1);
		if(btm == null){
			out.print("[修改个人战勋] [失败:获取成员信息失败] [player:"+player.getLogString()+"] [memberbattlelevel:"+memberbattlelevel+"]<br>");
		}else{
			int oldnums = btm.getBattleLevel();
			btm.setBattleLevel(Integer.parseInt(memberbattlelevel));
			out.print("[修改个人战勋] [成功] [玩家:"+player.getName()+"] [个人战勋变化:"+oldnums+"-->"+btm.getBattleLevel()+"]<br>");
		}
	}
	if(teambattlenums!=null && !teambattlenums.isEmpty()){
		BattleTeam bt = JJCManager.getInstance().getTeam(player, 1, "后台测试");
		if(bt==null){
			out.print("bt==null<br>");
		}else{
			long oldvalue = bt.getBattleAllNum();
			bt.setBattleAllNum(Integer.parseInt(teambattlenums));
			out.print("[修改战队场数] [成功] [玩家:"+player.getName()+"] [战队参赛总场数变化:"+oldvalue+"-->"+bt.getBattleAllNum()+"]<br>");
		}
	}
	if(teambattlelevel!=null && !teambattlelevel.isEmpty()){
		BattleTeam bt = JJCManager.getInstance().getTeam(player, 1, "后台测试");
		if(bt==null){
			out.print("bt==null<br>");
		}else{
			long oldvalue = bt.getTeamLevel();
			bt.setTeamLevel(Integer.parseInt(teambattlelevel));
			out.print("[修改战队战勋] [成功] [玩家:"+player.getName()+"] [战队战勋变化:"+oldvalue+"-->"+bt.getTeamLevel()+"]<br>");
		}
	}
	if(flagName!=null && !flagName.isEmpty()){
		BattleTeam bt = JJCManager.getInstance().getTeam(player, 1, "后台测试");
		if(bt!=null){
			String oldname = player.getJiazuTitle();
			String oldicon = player.getJiazuIcon();
			player.setJiazuTitle(bt.getTeamName());
			player.setJiazuIcon("zhandui_2");
			out.print("[修改称号] [成功] [玩家:"+player.getName()+"] [name："+oldname+"-->"+bt.getTeamName()+"] [icon:"+oldicon+"-->"+bt.getTeamIcon()+"]<br>");
		}else{
			out.print("==============[战队不存在]==================");
		}
		
	}
	if(comebackflagName!=null && !comebackflagName.isEmpty()){
		player.initJiazuTitleAndIcon();
	}
	if(calJJcPoints !=null && !calJJcPoints.isEmpty()){
		Calendar cl = Calendar.getInstance();
		int dayOfWeek = cl.get(Calendar.DAY_OF_WEEK);
		JJCManager.getInstance().周日结算 = dayOfWeek;
		int hour = cl.get(Calendar.HOUR_OF_DAY);
		int minute = cl.get(Calendar.MINUTE);
		out.print("[设置成功] [isHasWeekReward:"+JJCManager.getInstance().isHasWeekReward+"] [wwk:"+JJCManager.getInstance().周日结算+"-->"+dayOfWeek+"] [day:"+JJCManager.getInstance().周日结算 +"] [hour:"+JJCManager.getInstance().结算小时+"-->"+hour+"] [minute:"+JJCManager.getInstance().结算分钟+"-->"+minute+"]<br>");
		JJCManager.getInstance().结算小时 = hour;
		JJCManager.getInstance().结算分钟 = minute;
		JJCManager.getInstance().isHasWeekReward = false;
		
		out.print("[设置成功] [后] [isHasWeekReward:"+JJCManager.getInstance().isHasWeekReward+"] [wwk:"+JJCManager.getInstance().周日结算+"-->"+dayOfWeek+"] [day:"+JJCManager.getInstance().周日结算 +"] [hour:"+JJCManager.getInstance().结算小时+"-->"+hour+"] [minute:"+JJCManager.getInstance().结算分钟+"-->"+minute+"]<br>");
		
	}
	if(pkmode!=null && !pkmode.isEmpty()){
		String modes[] = {"和平模式","全体模式","组队模式","家族模式","宗派模式","国家模式","善恶模式"};
		player.setPkMode((byte)Integer.parseInt(pkmode));
		out.print("[设置成功] [pkmode:"+pkmode+"] [模式:"+modes[Integer.parseInt(pkmode)]+"]]");
	}
	if(showBuffs!=null && !showBuffs.isEmpty()){
		List<Buff> buffs = player.getAllBuffs();
		for(int i = buffs.size() - 1; i >= 0; i--){
			Buff buff = buffs.get(i);
			out.print("[查询玩家buff] [玩家:"+player.getName()+"] [buff名："+buff.getTemplateName()+"] [是否有利:"+buff.isAdvantageous()+"]<br>");
		}
	}
}
if(clearLevel!=null && !clearLevel.isEmpty()){
	JJCManager.getInstance().SAIJI_START = true;
	out.print("设置成功");
}
if(clearLevel2!=null && !clearLevel2.isEmpty()){
	JJCManager.getInstance().SAIJI_END = true;
	out.print("设置成功");
}
if(costsilver!=null && !costsilver.isEmpty()){
	JJCManager.CREATE_TEAM_COST_SILVER = Long.parseLong(costsilver);
	out.print("设置成功:"+costsilver);
}

if(starthour!=null && !starthour.isEmpty()){
	Class cl = JJCManager.class;
	Field f = cl.getDeclaredField("START_HOUR");
	f.setAccessible(true);
	JJCManager jm = JJCManager.getInstance();
	int oldvalue = f.getInt(jm);
	f.set(jm, Integer.parseInt(starthour));
	int newvalue = f.getInt(jm);
	out.print("[修改开始时间属性] [时间变化："+oldvalue+"-->"+newvalue+"]");
}
if(endhour!=null && !endhour.isEmpty()){
	Class cl = JJCManager.class;
	Field f = cl.getDeclaredField("END_HOUR");
	f.setAccessible(true);
	JJCManager jm = JJCManager.getInstance();
	int oldvalue = f.getInt(jm);
	f.set(jm, Integer.parseInt(endhour));
	int newvalue = f.getInt(jm);
	out.print("[修改结束时间属性] [时间变化："+oldvalue+"-->"+newvalue+"]");
}
if(startminute!=null && !startminute.isEmpty()){
	JJCManager.getInstance().START_MINUTE = Integer.parseInt(startminute);
	out.print("[修改开始时间属性] [时间："+startminute+"]");
}
if(endminute!=null && !endminute.isEmpty()){
	JJCManager.getInstance().END_MINUTE = Integer.parseInt(endminute);
	out.print("[修改结束时间属性] [时间："+endminute+"]");
}
if(currservertime!=null && !currservertime.isEmpty()){
	Class cl = JJCManager.class;
	Field f = cl.getDeclaredField("START_HOUR");
	f.setAccessible(true);
	JJCManager jm = JJCManager.getInstance();
	int oldvalue = f.getInt(jm);
	
	Field f2 = cl.getDeclaredField("END_HOUR");
	f2.setAccessible(true);
	JJCManager jm2 = JJCManager.getInstance();
	int oldvalue2 = f2.getInt(jm2);
	
	Field f3 = cl.getDeclaredField("end_notice_minute");
	f3.setAccessible(true);
	JJCManager jm3 = JJCManager.getInstance();
	int oldvalue3 = f3.getInt(jm3);
	out.print("[开始时间"+oldvalue+"] [[开始分钟:"+JJCManager.getInstance().START_MINUTE+"]<br>");
	out.print("[开始时间"+oldvalue2+"] [[开始分钟:"+JJCManager.getInstance().END_MINUTE+"]<br>");
	out.print("end_notice_minute:"+oldvalue3);
}




%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="css/style.css" />
<title>献血后台</title>
</head>
<body>
<form>
	<table>
		<tr><th>玩家名:</th><td><input type='text' name='pname'></td></tr>
		<tr><th>修改血石数量:</th><td><input type='text' name='jjcpoints'></td></tr>
		<tr><th>开启小时:</th><td><input type='text' name='starthour'>(只写小时,比如:10,就是10:00-23:59之间都可以参加)</td></tr>
		<tr><th>开启分钟:</th><td><input type='text' name='startminute'>(只写分钟,比如:0--59)</td></tr>
		<tr><th>结束小时:</th><td><input type='text' name='endhour'>(只写小时,比如:10,就是10:00-23:59之间都可以参加)</td></tr>
		<tr><th>结束分钟:</th><td><input type='text' name='endminute'>(只写分钟,比如:0--59)</td></tr>
		<tr><th>查看当前服务器时间:</th><td><input type='text' name='currservertime'>(只写分钟,比如:0--59)</td></tr>
		<tr><th>个人参赛场数:</th><td><input type='text' name='memberbattlenums'></td></tr>
		<tr><th>个人战勋:</th><td><input type='text' name='memberbattlelevel'></td></tr>
		<tr><th>战队参数场数:</th><td><input type='text' name='teambattlenums'></td></tr>
		<tr><th>战队战勋:</th><td><input type='text' name='teambattlelevel'></td></tr>
		<tr><th>设置头顶战旗:</th><td><input type='text' name='flagName'></td></tr>
		<tr><th>设置组队模式:</th><td><input type='text' name='pkmode'></td></tr>
		<tr><th>恢复头顶战旗:</th><td><input type='text' name='comebackflagName'></td></tr>
		<tr><th>每周日血石计算:</th><td><input type='text' name='calJJcPoints'></td></tr>
		<tr><th>创建战队消耗的银两:</th><td><input type='text' name='costsilver'></td></tr>
		<tr><th>解散战队:</th><td><input type='text' name='jiesanTeam'>(暂不提供实现)</td></tr>
		<tr><th>踢出队友:</th><td><input type='text' name='kickMember'>(暂不提供实现)</td></tr>
		<tr><th>显示玩家buff:</th><td><input type='text' name='showBuffs'>(随便输入)</td></tr>
		<tr><th>显示战队密码:</th><td><input type='text' name='showPwd'>(随便输入)</td></tr>
		<tr><th>清空战勋:</th><td><input type='text' name='clearLevel'>(随便输入)</td></tr>
		<tr><th>赛季结束清空战勋提示:</th><td><input type='text' name='clearLevel2'>(随便输入)</td></tr>
		<tr><th>修改战队名:</th><td><input type='text' name='changeTeamName'></td></tr>
		<tr><th>操作:</th><td><input type="submit" value='确定'></td></tr>
	</table>
</form>


</body>
</html>
