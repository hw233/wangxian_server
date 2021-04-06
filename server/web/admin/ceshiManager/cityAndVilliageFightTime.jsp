<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.cityfight.CityFightManager"%>
<%@page
	import="com.fy.engineserver.activity.village.manager.VillageFightManager"%>
<%@page
	import="com.fy.engineserver.activity.village.data.VillageData"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.activity.ActivityIntroduce"%>
<%@page import="com.fy.engineserver.activity.ActivityManager"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="com.fy.engineserver.newtask.TaskEntity"%>
<%@page import="com.fy.engineserver.newtask.service.TaskSubSystem"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.tool.GlobalTool"%>
<%@page
	import="com.fy.engineserver.activity.chestFight.ChestFightManager"%>
<%@page import="com.fy.engineserver.zongzu.data.ZongPai"%>
<%@page import="com.fy.engineserver.zongzu.manager.ZongPaiManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	CityFightManager cm = CityFightManager.getInstance();
	VillageFightManager vm = VillageFightManager.getInstance();

	String 城战修改 = request.getParameter("城战修改");
	String 申请时间1 = request.getParameter("申请时间1");
	String 申请时间 = request.getParameter("申请时间");
	String 开战时 = request.getParameter("开战时");
	String 开战分 = request.getParameter("开战分");
	String 关战时 = request.getParameter("关战时");
	String 关战分 = request.getParameter("关战分");
	if (城战修改 != null && !"".equals(城战修改) && "city".equals(城战修改)) {
		if (申请时间1 != null && !"".equals(申请时间1)) {
			cm.申请时间1 = Integer.valueOf(申请时间1);
		}
		if (申请时间 != null && !"".equals(申请时间)) {
			cm.申请时间 = Integer.valueOf(申请时间);
		}
		if (开战时 != null && !"".equals(开战时)) {
			cm.开战时 = Integer.valueOf(开战时);
		}
		if (开战分 != null && !"".equals(开战分)) {
			cm.开战分 = Integer.valueOf(开战分);
		}
		if (关战时 != null && !"".equals(关战时)) {
			cm.关战时 = Integer.valueOf(关战时);
		}
		if (关战分 != null && !"".equals(关战分)) {
			cm.关战分 = Integer.valueOf(关战分);
		}
	}
	String 矿战修改 = request.getParameter("矿战修改");
	String 每天最早申请时间 = request.getParameter("每天最早申请时间");
	String 每天最晚申请时间 = request.getParameter("每天最晚申请时间");
	String 每天战斗开始时间 = request.getParameter("每天战斗开始时间");
	String 战斗持续时间 = request.getParameter("战斗持续时间");
	if (矿战修改 != null && !"".equals(矿战修改) && "village".equals(矿战修改)) {
		if (每天最早申请时间 != null && !"".equals(每天最早申请时间)) {
			vm.每天最早申请时间 = Integer.valueOf(每天最早申请时间);
		}
		if (每天最晚申请时间 != null && !"".equals(每天最晚申请时间)) {
			vm.每天最晚申请时间 = Integer.valueOf(每天最晚申请时间);
		}
		if (每天战斗开始时间 != null && !"".equals(每天战斗开始时间)) {
			vm.每天战斗开始时间 = Integer.valueOf(每天战斗开始时间);
		}
		if (战斗持续时间 != null && !"".equals(战斗持续时间)) {
			vm.战斗持续时间 = Integer.valueOf(战斗持续时间);
		}
	}

	out.print("当前城战时间：<br>");
	out.print("[申请开始：" + cm.申请时间1 + "] [申请结束：" + cm.申请时间 + "] [开战小时：" + cm.开战时 + "] [开战分钟：" + cm.开战分 + "] [关战小时：" + cm.关战时 + "] [关战分钟：" + cm.关战分 + "]<br>");
	out.print("当前矿战时间：<br>");
	out.print("[申请开始：" + vm.每天最早申请时间 + "] [申请结束：" + vm.每天最晚申请时间 + "] [开战小时：" + vm.每天战斗开始时间 + "] [战斗持续时间：" + vm.战斗持续时间 + "]<br>");

	String 清空城战报名数据 = request.getParameter("清空城战报名数据");
	if (清空城战报名数据 != null && !"".equals(清空城战报名数据) && "cityClear".equals(清空城战报名数据)) {
		cm.切换天的操作();
		cm.logger.warn("后台清空城战数据");
	}
	String 城战报名结束发通知 = request.getParameter("城战报名结束发通知");
	if (城战报名结束发通知 != null && !"".equals(城战报名结束发通知) && "cityNotice".equals(城战报名结束发通知)) {
		for (int country = 0; country < 4; country++) {
			cm.noticePrePareZongpais(Integer.valueOf(country));
			cm.logger.warn("后台结束城战报名发通知");
			cm.公布报名结果 = true;
		}
	}
	String 矿战报名结束发通知 = request.getParameter("矿战报名结束发通知");
	if (矿战报名结束发通知 != null && !"".equals(矿战报名结束发通知) && "villageNotice".equals(矿战报名结束发通知)) {
		vm.noticePrePareJiazus();
		vm.logger.warn("后台结束矿战报名发通知");
		vm.公布报名结果 = true;
	}

	String 清空矿战报名数据 = request.getParameter("清空矿战报名数据");
	String week = request.getParameter("week");
	if (清空矿战报名数据 != null && !"".equals(清空矿战报名数据) && "villageClear".equals(清空矿战报名数据)) {
		if (week != null && !"".equals(week)) {
			vm.每天凌晨更新数据(Boolean.valueOf(week));
		}
		vm.每天凌晨更新数据(false);
		vm.logger.warn("后台清空矿战数据");
	}
	String 清空已完成次数 = request.getParameter("清空已完成次数");
	if (清空已完成次数 != null && !"".equals(清空已完成次数) && "taskClear".equals(清空已完成次数)) {
		String playerName = request.getParameter("playerName");
		if (playerName != null && !"".equals(playerName)) {
			Player player = PlayerManager.getInstance().getPlayer(playerName);
			if (player != null) {
				List<ActivityIntroduce> fit = ActivityManager.getInstance().getFitActivityIntroduce(player);
				if (fit != null && fit.size() > 0) {
					for (int i = 0; i < fit.size(); i++) {
						ActivityIntroduce introduce = fit.get(i);
						if (introduce != null) {
							if (!"".equals(introduce.getTaskGroupName())) {
								List<Task> taskList = TaskManager.getInstance().getTaskCollectionsByName(introduce.getTaskGroupName());
								if (taskList != null) {
									for (Task t : taskList) {
										if (t != null) {
											TaskEntity tte = player.getTaskEntity(t.getId());
											if (tte != null) {
												tte.setLastDeliverTime(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
												TaskSubSystem.logger.warn("[后台修改任务交付时间] " + player.getLogString() + t.getName_stat());
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	String 设置繁荣度 = request.getParameter("设置繁荣度");
	if (设置繁荣度 != null && !"".equals(设置繁荣度) && "addFanrongdu".equals(设置繁荣度)) {
		String zongpaiId = request.getParameter("zongpaiId");
		String addNum = request.getParameter("addNum");
		if (zongpaiId != null && !"".equals(zongpaiId) && addNum != null && !"".equals(addNum)) {
			ZongPai zongpai = ZongPaiManager.getInstance().getZongPaiById(Long.valueOf(zongpaiId));
			if (zongpai != null) {
				zongpai.setFanrongdu(Integer.valueOf(addNum));
				out.print("设置繁荣度为：" + addNum);
			}
		}
	}
%>
<hr>
<form action=""><input type="hidden" name="城战修改" value="city" />
城战修改<br>
申请开始：<input type="text" name="申请时间1" value="" /><br>
申请结束：<input type="text" name="申请时间" value="" /><br>
开战小时：<input type="text" name="开战时" value="" /><br>
开战分钟：<input type="text" name="开战分" value="" /><br>
关战小时：<input type="text" name="关战时" value="" /><br>
关战分钟：<input type="text" name="关战分" value="" /><br>
<input type="submit" value="提交"></form>
<hr>
<form action="">清空城战报名数据<input type="hidden" name="清空城战报名数据"
	value="cityClear" /> <input type="submit" value="提交"></form>
<hr>
<form action="">城战报名结束发通知<input type="hidden" name="城战报名结束发通知"
	value="cityNotice" /> <input type="submit" value="提交"><br>
使用该按钮前，请先把报名截止时间改为当前时间之前的任意时间</form>
<hr>
<form action=""><input type="hidden" name="矿战修改" value="village" />
矿战修改<br>
申请开始：<input type="text" name="每天最早申请时间" value="" /><br>
申请结束：<input type="text" name="每天最晚申请时间" value="" /><br>
开战小时：<input type="text" name="每天战斗开始时间" value="" /><br>
战斗持续时间：<input type="text" name="战斗持续时间" value="" /><br>
<input type="submit" value="提交"></form>
<hr>
<form action="">清空矿战报名数据<input type="hidden" name="清空矿战报名数据"
	value="villageClear" /> <input type="text" name="week" value="" />(默认false，如果是模拟周一的清空请填true)
<input type="submit" value="提交"></form>
<hr>
<form action="">矿战报名结束发通知<input type="hidden" name="矿战报名结束发通知"
	value="villageNotice" /> <input type="submit" value="提交"><br>
使用该按钮前，请先把报名截止时间改为当前时间之前的任意时间</form>
<hr>
<form action="">清空已完成次数:<input type="hidden" name="清空已完成次数"
	value="taskClear" />角色名<input type="text" name="playerName" value="" />
<input type="submit" value="提交"></form>
<hr>
<form action="">设置宗派繁荣度:<input type="hidden" name="设置繁荣度"
	value="addFanrongdu" />宗派id：<input type="text" name="zongpaiId"
	value="" />增加数量：<input type="text" name="addNum" value="" /> <input
	type="submit" value="提交"></form>
<hr>
<%
	String 囚禁查询 = request.getParameter("囚禁查询");
	if (囚禁查询 != null && !"".equals(囚禁查询) && "queryQiujin".equals(囚禁查询)) {
		String playerName2 = request.getParameter("playerName2");
		if (playerName2 != null && !"".equals(playerName2)) {
			Player playerB = PlayerManager.getInstance().getPlayer(playerName2);
			String gr = GlobalTool.verifyTransByOther(playerB.getId());
			if (gr != null) {
				out.print("限制地图囚禁1(玩家无法被拉令)<br>");
			}
			if (ChestFightManager.inst.isPlayerInActive(playerB)) {
				out.print("限制地图囚禁2(玩家在打斗中)<br>");
			}
			out.print("囚禁查询完毕<br>");
		}
	}
%>
<form action="">是否限制囚禁:<input type="hidden" name="囚禁查询"
	value="queryQiujin" />角色名<input type="text" name="playerName2"
	value="" /> <input type="submit" value="提交"></form>
<hr>
<%
	Hashtable<Byte, Map<Integer, List<Long>>> prepareToFightJiazus = vm.vd.getPrepareToFightJiazus();
	if (prepareToFightJiazus != null) {
		for (Byte countryId : prepareToFightJiazus.keySet()) {
			out.print("country:" + countryId + "<br>");
			Map<Integer, List<Long>> jiazuMaps = prepareToFightJiazus.get(countryId);
			if (jiazuMaps != null) {
				for (Integer arrayIndex : jiazuMaps.keySet()) {
					out.print("arrayIndex:" + countryId + "<br>");
					if (jiazuMaps.containsKey(arrayIndex)) {
						List<Long> jiazuList = jiazuMaps.get(arrayIndex);
						if (jiazuList != null) {
							for (long id : jiazuList) {
								Jiazu jiazu = JiazuManager.getInstance().getJiazu(id);
								if (jiazu != null) {
									out.print("[jiazuId：" + id + ",point:" + jiazu.getPoint() + "]");
								}
							}
							out.print("<br>");
						}
					}
				}
			}
			out.print("----------国家分界线----------<br>");
		}
	}
	out.print("<hr>");
	Hashtable<String, List<Long>> attackCity_0_country_map = cm.cd.getAttackCity_0_country_map();
	if (attackCity_0_country_map != null) {
		for (String cityName : attackCity_0_country_map.keySet()) {
			List<Long> zongpaiList = attackCity_0_country_map.get(cityName);
			for (Long id : zongpaiList) {
				ZongPai zongpai = ZongPaiManager.getInstance().getZongPaiById(id);
				if (zongpai != null) {
					out.print("[zongPaiId：" + id + ",point:" + zongpai.getPoint() + "]");
				}
			}
		}
	}
	out.print("----------国家分界线----------<br>");
	Hashtable<String, List<Long>> attackCity_1_country_map = cm.cd.getAttackCity_1_country_map();
	if (attackCity_1_country_map != null) {
		for (String cityName : attackCity_1_country_map.keySet()) {
			List<Long> zongpaiList = attackCity_1_country_map.get(cityName);
			for (Long id : zongpaiList) {
				ZongPai zongpai = ZongPaiManager.getInstance().getZongPaiById(id);
				if (zongpai != null) {
					out.print("[zongPaiId：" + id + ",point:" + zongpai.getPoint() + "]");
				}
			}
		}
	}
	out.print("----------国家分界线----------<br>");
	Hashtable<String, List<Long>> attackCity_2_country_map = cm.cd.getAttackCity_2_country_map();
	if (attackCity_2_country_map != null) {
		for (String cityName : attackCity_2_country_map.keySet()) {
			List<Long> zongpaiList = attackCity_2_country_map.get(cityName);
			for (Long id : zongpaiList) {
				ZongPai zongpai = ZongPaiManager.getInstance().getZongPaiById(id);
				if (zongpai != null) {
					out.print("[zongPaiId：" + id + ",point:" + zongpai.getPoint() + "]");
				}
			}
		}
	}
	out.print("----------国家分界线----------<br>");
	Hashtable<String, List<Long>> attackCity_3_country_map = cm.cd.getAttackCity_3_country_map();
	if (attackCity_3_country_map != null) {
		for (String cityName : attackCity_3_country_map.keySet()) {
			List<Long> zongpaiList = attackCity_3_country_map.get(cityName);
			for (Long id : zongpaiList) {
				ZongPai zongpai = ZongPaiManager.getInstance().getZongPaiById(id);
				if (zongpai != null) {
					out.print("[zongPaiId：" + id + ",point:" + zongpai.getPoint() + "]");
				}
			}
		}
	}
	out.print("----------国家分界线----------<br>");
%>
</body>
</html>