<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@page import="com.fy.engineserver.activity.ActivityManager"%>
<%@page
	import="com.fy.engineserver.activity.expactivity.DayilyTimeDistance"%>
<%@page import="com.fy.engineserver.activity.RefreshSpriteManager"%>
<%@page
	import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page
	import="com.fy.engineserver.activity.wolf.config.TimeRangeConfig"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Iterator"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.activity.shop.ActivityProp"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.activity.xianling.BoardPrize"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.activity.wolf.WolfManager"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="java.util.Set"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page
	import="com.fy.engineserver.activity.xianling.XLBillBoardData"%>
<%@page
	import="com.fy.engineserver.activity.xianling.XianLingChallengeThread"%>
<%@page import="com.fy.engineserver.core.TransportData"%>
<%@page
	import="com.fy.engineserver.activity.fairylandTreasure.FairylandTreasureManager"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.DiskCache"%>
<%@page
	import="com.fy.engineserver.datasource.buff.BuffTemplate_MeetMonster"%>
<%@page
	import="com.fy.engineserver.datasource.buff.BuffTemplate_XianlingScore"%>
<%@page import="com.fy.engineserver.datasource.buff.Buff"%>
<%@page
	import="com.fy.engineserver.activity.xianling.XianLingBillBoardData"%>
<%@page import="com.fy.engineserver.sprite.monster.Monster"%>
<%@page
	import="com.fy.engineserver.sprite.monster.MemoryMonsterManager"%>
<%@page
	import="com.fy.engineserver.activity.xianling.XianLingChallenge"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page
	import="com.fy.engineserver.activity.xianling.XianLingChallengeManager"%>
<%@page
	import="com.fy.engineserver.activity.xianling.MeetMonsterRate"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.activity.xianling.XianLing"%>
<%@page
	import="com.fy.engineserver.activity.xianling.XianLingManager"%>
<%@page
	import="com.fy.engineserver.activity.xianling.PlayerXianLingData"%>
<%@page
	import="com.fy.engineserver.activity.xianling.XianLingLevelData"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		XianLingManager xm = XianLingManager.instance;
		//xm.disk.clear();
		//xm.clearXianLingData();
		XianLingChallengeManager xcm = XianLingChallengeManager.instance;
		//out.print("currentTime1:" + System.currentTimeMillis() + "<br>");
		XianLing activity = xm.getCurrentActivity();
		out.print("isOpen:" + xm.isOpen + "<br>");
		out.print("translateMap:" + xm.translateMap.size() + "<br>");
		out.print("currentActivity:" + xm.getCurrentActivity() + "<br>");
		//out.print("BoardPrizes:" + xm.getBoardPrizes().size() + "<br>");
		out.print("csBillboardList:" + xm.csBillboardList.size() + "<br>");
		out.print("activities.size:" + xm.activities.size() + "<br>");
		out.print("isOpen:" + xm.isOpen + "<br>");
		//xm.activities.clear();
		out.print("-------------<br>");
		out.print("crossBillboardList:" + xm.crossBillboardList.size() + "<br>");
		out.print("crossBillboardMap:" + xm.crossBillboardMap.size() + "<br>");
		out.print("activityKey:" + xm.activityKey + "<br>");
		out.print("getActivityKey:" + xm.getActivitykey() + "<br>");
		out.print("endTime:" + xm.endTime + "<br>");
		out.print("-------------<br>");
		/*for (XianLingBillBoardData data : xm.csBillboardList) {
			out.print(data + "<br>");
		}*/
		//xm.disk.remove(20160908);
		//xm.isOpen = false;
		//xm.activities = new ArrayList<XianLing>();
		out.print("xm.disk:" + xm.disk.get(20160908) + "<br>");
		/*for (BoardPrize bp : xm.getBoardPrizes()) {
			out.print(Arrays.toString(bp.getTempIds()) + "<br>");
		}*/
		out.print("timedTaskPrizeMap:" + xm.timedTaskPrizeMap.size() + "<br>");
		/*for (Iterator<Byte> itor = xm.timedTaskPrizeMap.keySet().iterator();itor.hasNext();) {
			byte t = itor.next();
			List<ActivityProp> list =  xm.timedTaskPrizeMap.get(t);
		out.print("---1111---["+t+"]<br>");
		for (ActivityProp ap : list) {
			Article a = ArticleManager.getInstance().getArticleByCNname(ap.getArticleCNName());
			if (a == null) {
				out.print(ap.getArticleCNName() + "--无<br>");
			} else {
				out.print(ap.getArticleCNName() + "<br>");
			}
		}
		}*/
		String opType = request.getParameter("opType");
		if (opType != null && !"".equals(opType)) {
			if (opType.equals("addScore")) {
				String name = request.getParameter("playerName");
				String addScore = request.getParameter("addScore");
				if (name != null && !"".equals(name) && addScore != null && !"".equals(addScore)) {
					Player p1 = null;
					try {
						p1 = PlayerManager.getInstance().getPlayer(name);
					} catch (Exception e) {
						try {
							p1 = PlayerManager.getInstance().getPlayer(Long.valueOf(name));
						} catch (Exception ee) {
							out.print("获取角色异常" + ee);
						}
					}
					if (p1 != null) {
						xm.addScore(p1, Integer.valueOf(addScore));
						out.print(p1.getXianlingData() + "<br>");
						out.print(System.currentTimeMillis() - p1.getXianlingData().getNextResumeEnergy() + "<br>");
					}
				} else {
					out.print("请输入角色名和要加的积分<br>");
				}
			} else if (opType.equals("clearLevel")) {
				String name = request.getParameter("playerName");
				if (name != null && !"".equals(name)) {
					Player p1 = null;
					try {
						p1 = PlayerManager.getInstance().getPlayer(name);
					} catch (Exception e) {
						try {
							p1 = PlayerManager.getInstance().getPlayer(Long.valueOf(name));
						} catch (Exception ee) {
							out.print("获取角色异常" + ee);
						}
					}
					if (p1 != null) {
						PlayerXianLingData xianlingData = p1.getXianlingData();
						if (xianlingData != null) {
							xianlingData.setMaxLevel(0);
							out.print(p1.getXianlingData() + "<br>");
							out.print("[scoreBuff:" + p1.getScoreBuff() + "] [meetMonsterBuff:" + p1.getMeetMonsterBuffId() + "]<br>");
						}
					}
				} else {
					out.print("请输入角色名<br>");
				}
			}
			if (opType.equals("addEnergy")) {
				String name = request.getParameter("playerName");
				String addEnergy = request.getParameter("addEnergy");
				if (name != null && !"".equals(name) && addEnergy != null && !"".equals(addEnergy)) {
					Player p1 = null;
					try {
						p1 = PlayerManager.getInstance().getPlayer(name);
					} catch (Exception e) {
						try {
							p1 = PlayerManager.getInstance().getPlayer(Long.valueOf(name));
						} catch (Exception ee) {
							out.print("获取角色异常" + ee);
						}
					}
					if (p1 != null) {
						PlayerXianLingData xianlingData = p1.getXianlingData();
						if (xianlingData != null) {
							xianlingData.setEnergy(xianlingData.getEnergy() + Integer.valueOf(addEnergy));
							//xianlingData.setNextResumeEnergy(System.currentTimeMillis() + 5000);
							/*xianlingData.setArticleCNName(Arrays.copyOf(xianlingData.getArticleCNName(), 1));
							xianlingData.setArticleColor(Arrays.copyOf(xianlingData.getArticleColor(), 1));
							xianlingData.setArticleNum(Arrays.copyOf(xianlingData.getArticleNum(), 1));
							xianlingData.setBind(Arrays.copyOf(xianlingData.getBind(), 1));*/
							out.print(p1.getXianlingData() + "<br>");
							out.print("[scoreBuff:" + p1.getScoreBuff() + "] [meetMonsterBuff:" + p1.getMeetMonsterBuffId() + "]<br>");
						}
					}
				} else {
					out.print("请输入角色名和要加的积分<br>");
				}
			} else if (opType.equals("refreshCross")) {
				xm.refreshCrossBillBoard();
			} else if (opType.equals("showDatas")) {
				out.print("boardEm.size=" + xm.boardEm.count() + "<br>");
				out.print("csBillboard:" + xm.crossBillboardMap.size() + "<br>");
				out.print("billboardMap:" + xm.billboardMap.size() + "<br>");
				out.print("数据：" + xm.billboardMap.get(1100000000009072642l).getXlDataMap().keySet() + "<br>");
				out.print("xm.billboardMap:" + xm.billboardMap.keySet() + "<br>");
				out.print("crossBillboardMap:" + xm.crossBillboardMap.size() + "<br>");
				out.print("xm.crossBillboardMap:" + xm.crossBillboardMap.keySet() + "<br>");
				out.print("crossBillboardList:" + xm.crossBillboardList.size() + "<br>");
				out.print("<hr>");
			} else if (opType.equals("addBuff")) {
				String name = request.getParameter("playerName");
				String level = request.getParameter("level");
				if (name != null && !"".equals(name) && level != null && !"".equals(level)) {
					Player p1 = null;
					try {
						p1 = PlayerManager.getInstance().getPlayer(name);
					} catch (Exception e) {
						try {
							p1 = PlayerManager.getInstance().getPlayer(Long.valueOf(name));
						} catch (Exception ee) {
							out.print("获取角色异常" + ee);
						}
					}
					if (p1 != null) {
						MeetMonsterRate mm = xm.meetMonsterRateMap.get(Integer.valueOf(level));
						FairylandTreasureManager.fireBuff(p1, "遇怪加成" + mm.getBuffId(), mm.getBuffId() - 1, XianLingManager.REFRESH_BUFF_LASTING);
						out.print(p1.getMeetMonsterBuffId());
					}
				} else {
					out.print("请输入角色名和要加的积分<br>");
				}
			}

		}
	%>
	<form action="">
		加积分：<br> <input type="hidden" name="opType" value="addScore" />
		playerName:<input type="text" name="playerName" /> 增加积分：<input
			type="text" name="addScore" /> <input type="submit" value="加积分" />
	</form>
	<hr>
	<form action="">
		<input type="hidden" name="opType" value="clearLevel" /> playerName:<input
			type="text" name="playerName" /> <input type="submit"
			value="挑战关卡数清零" />
	</form>
	<hr>
	<form action="">
		<input type="hidden" name="opType" value="clearPlayerData" />
		playerName:<input type="text" name="playerName" /> <input
			type="submit" value="清除玩家仙灵数据" />
	</form>
	<%
		if (opType != null && !"".equals(opType)) {
			if (opType.equals("clearPlayerData")) {
				String name = request.getParameter("playerName");
				if (name != null && !"".equals(name)) {
					Player p1 = null;
					try {
						p1 = PlayerManager.getInstance().getPlayer(name);
					} catch (Exception e) {
						try {
							p1 = PlayerManager.getInstance().getPlayer(Long.valueOf(name));
						} catch (Exception ee) {
							out.print("获取角色异常" + ee);
						}
					}
					if (p1 != null) {
						xm.resetPlayerXianLingData(p1);
						out.print(p1.getXianlingData().toString() + "<br>");
					}
				} else {
					out.print("请输入角色名和要加的积分<br>");
				}
			}

		}
	%>
	<hr>
	<form action="">
		<input type="hidden" name="opType" value="addEnergy" /> playerName:<input
			type="text" name="playerName" /> 增加真气：<input type="text"
			name="addEnergy" /> <input type="submit" value="加真气值" />
	</form>
	<hr>
	<form action="">
		<input type="hidden" name="opType" value="addBuff" /> playerName:<input
			type="text" name="playerName" /> 增加bufflevel：<input type="text"
			name="level" /> <input type="submit" value="加遇怪buff" />
	</form>
	<hr>
	<form action="">
		<input type="hidden" name="opType" value="modifyTime" /> 开始时间:<input
			type="text" name="startTime" />(格式:2016-09-06 00:00:00) <input
			type="submit" value="修改活动开启时间" />
	</form>
	<%
		if (opType != null && !"".equals(opType)) {
			if (opType.equals("modifyTime")) {
				String startTime = request.getParameter("startTime");
				if (startTime != null && !"".equals(startTime)) {

					List<XianLing> activities = xm.activities;
					if (activities.size() > 0) {
						activities.get(activities.size() - 1).setStartTime(TimeTool.formatter.varChar19.parse(startTime));
						activities.get(activities.size() - 1).setEndTime(activities.get(activities.size() - 1).getStartTime() + xm.lastingTime);
					}
				}
			}
		}
	%><hr>
	<form action="">
		<input type="hidden" name="opType" value="modifyTimeTaskLeftTime" />playerName:<input
			type="text" name="playerName" /> 限时任务结束时间:<input type="text"
			name="endTime" />(格式:2016-09-06 00:00:00) <input type="submit"
			value="限时任务结束时间" />
	</form>
	<%
		if (opType != null && !"".equals(opType)) {
			if (opType.equals("modifyTimeTaskLeftTime")) {
				String name = request.getParameter("playerName");
				String endTime = request.getParameter("endTime");
				if (name != null && !"".equals(name) && endTime != null && !"".equals(endTime)) {
					Player p1 = null;
					try {
						p1 = PlayerManager.getInstance().getPlayer(name);
					} catch (Exception e) {
						try {
							p1 = PlayerManager.getInstance().getPlayer(Long.valueOf(name));
						} catch (Exception ee) {
							out.print("获取角色异常" + ee);
						}
					}
					if (p1 != null) {
						PlayerXianLingData xianlingData = p1.getXianlingData();
						if (xianlingData != null) {
							long time = TimeTool.formatter.varChar19.parse(endTime);
							xianlingData.setNextRefreshTime(time);
							out.print(p1.getXianlingData() + "<br>");
							out.print("[scoreBuff:" + p1.getScoreBuff() + "] [meetMonsterBuff:" + p1.getMeetMonsterBuffId() + "]<br>");
						}
					}
				} else {
					out.print("请输入角色名和要加的积分<br>");
				}
			}

		}
	%>
	<hr>
	<form action="">
		刷新跨服排行榜：<br> <input type="hidden" name="opType"
			value="refreshCross" /> <input type="submit" value="提交" />
	</form>
	<hr>
	<form action="">
		内存数据：<br> <input type="hidden" name="opType" value="showDatas" />
		<input type="submit" value="提交" />
	</form>
	<hr>
	<form action="">
		<input type="hidden" name="opType" value="refreshMonster" />
		playerName:<input type="text" name="playerName" /> 关卡level：<input
			type="text" name="level" /> 刷怪次数：<input type="text" name="times" />
		<input type="submit" value="测试刷怪" />
	</form>
	<%
		if (opType != null && !"".equals(opType)) {
			if (opType.equals("refreshMonster")) {
				String name = request.getParameter("playerName");
				String level = request.getParameter("level");
				String times = request.getParameter("times");

				if (name != null && !"".equals(name) && level != null && !"".equals(level) && times != null && !"".equals(times)) {
					Player p1 = null;
					try {
						p1 = PlayerManager.getInstance().getPlayer(name);
					} catch (Exception e) {
						try {
							p1 = PlayerManager.getInstance().getPlayer(Long.valueOf(name));
						} catch (Exception ee) {
							out.print("获取角色异常" + ee);
						}
					}
					if (p1 != null) {
						Map<String, Integer> countMap = new HashMap<String, Integer>();
						if (p1.getMeetMonsterBuffId() > 0) {
							out.print("遇怪buff:" + xm.meetMonsterRateMap.get(p1.getMeetMonsterBuffId()).getName() + "<br>");
						}
						for (int i = 0; i < Integer.valueOf(times); i++) {
							int categoryId = xm.getOneMonster(p1, Integer.valueOf(level) - 1);
							Monster monster = xm.getOneMonater(p1, categoryId);
							if (monster != null) {
								out.print("categoryId:" + categoryId + ",monsterName:" + monster.getName() + ",monsterColor:" + monster.getColor() + "<br>");
								if (!countMap.containsKey(monster.getName())) {
									countMap.put(monster.getName(), 0);
								}
								countMap.put(monster.getName(), countMap.get(monster.getName()) + 1);
							}
						}
						out.print("统计数量:<br>");
						for (String mname : countMap.keySet()) {
							out.print(mname + ":" + countMap.get(mname) + "<br>");
						}
					}
				} else {
					out.print("请输入角色名和要加的积分<br>");
				}
			}
		}
	%>
	<hr>
	<hr>
	<form action="">
		<input type="hidden" name="opType" value="backCity" /> playerName:<input
			type="text" name="playerName" /> <input type="submit" value="回城" />
	</form>
	<%
		if (opType != null && !"".equals(opType)) {
			if (opType.equals("backCity")) {
				String name = request.getParameter("playerName");

				if (name != null && !"".equals(name)) {
					Player p1 = null;
					try {
						p1 = PlayerManager.getInstance().getPlayer(name);
					} catch (Exception e) {
						try {
							p1 = PlayerManager.getInstance().getPlayer(Long.valueOf(name));
						} catch (Exception ee) {
							out.print("获取角色异常" + ee);
						}
					}
					if (p1 != null) {
						out.print(p1.getLogString() + "<br>");
						out.print(p1.getCurrentGame().gi.name + "<br>");
						if (XianLingChallengeManager.mapNames.contains(p1.getCurrentGame().gi.name)) {
							p1.getCurrentGame().transferGame(p1, new TransportData(0, 0, 0, 0, p1.getHomeMapName(), p1.getHomeX(), p1.getHomeY()));
							out.print("回城<br>");

						}
						XianLingChallenge xc = xcm.findXLChallenge(p1);
						if (xc != null) {
							xc.setResult(xc.USETRANSPROP);
							out.print("传送<br>");
						}
					}
				} else {
					out.print("请输入角色名<br>");
				}
			}
		}
	%>
	<hr>
	<form action="">
		<input type="hidden" name="opType" value="crossBoard" /> <input
			type="submit" value="跨服排行榜" />
	</form>
	<%
		out.print("activityKey:" + xm.activityKey);
		xm.crossBillboardList = xm.map2OrderList(xm.crossBillboardMap, xm.activityKey);
		xm.updateCrossServerRank();
		if (opType != null && !"".equals(opType)) {
			if (opType.equals("crossBoard")) {
				if (xm.crossBillboardList.size() > 0) {
					for (XianLingBillBoardData bb : xm.crossBillboardList) {
						out.print(bb.getCrossServerRank() + "----" + bb + "<br><br>");
					}
				}
			}
		}
	%>
	<hr>
	<form action="">
		<input type="hidden" name="opType" value="board" /> <input
			type="submit" value="本服排行榜" />
	</form>
	<%
		if (opType != null && !"".equals(opType)) {
			if (opType.equals("board")) {
				out.print(XianLingManager.instance.getCurrentActivity() + "==");
				out.print(XianLingManager.instance.getActivitykey() + "<br>");
				List<XianLingBillBoardData> billboardList = new ArrayList<XianLingBillBoardData>();
				for (XianLingBillBoardData bbdata : xm.billboardMap.values()) {
					if (bbdata != null) {
						bbdata.setNotSaveVars(xm.getActivitykey());
						Map<Integer, XLBillBoardData> xlDataMap = bbdata.getXlDataMap();
						if (xlDataMap != null && xlDataMap.containsKey(xm.getActivitykey())) {
							billboardList.add(bbdata);
						}
					}
				}
				for (long id : xm.billboardMap.keySet()) {
					out.print(id + "--" + xm.billboardMap.get(id).getPlayerName() + ",corssRank:" + xm.billboardMap.get(id).getCrossServerRank() + ",score" + xm.billboardMap.get(id).getScore() + "<br>");
				}

				if (xm.csBillboardList.size() > 0) {
					for (XianLingBillBoardData bb : xm.csBillboardList) {
						Map<Integer, XLBillBoardData> xlDataMap = bb.getXlDataMap();
						if (xlDataMap != null && xlDataMap.containsKey(xm.getActivitykey())) {
							XLBillBoardData data = xlDataMap.get(xm.getActivitykey());
							out.print(data.getCrossServerRank() + "----" + bb.getId() + "<br><br>");
						}
					}
				}
			}

		}
	%>
	<hr>
	<form action="">
		<input type="hidden" name="opType" value="deleteBillBoardData" /> id:<input
			type="text" name="id" /> <input type="submit" value="删数据" />
	</form>
	<hr>
	<form action="">
		<input type="hidden" name="opType" value="clearActivity" />activityId:<input
			type="text" name="activityId" /><input type="submit" value="清空活动" />
	</form>
	<%
		if (opType != null && !"".equals(opType)) {
			if (opType.equals("clearActivity")) {
				String activityId = request.getParameter("activityId");
				if (activityId != null && !"".equals(activityId)) {
					xm.disk.remove(Integer.valueOf(activityId));
				}
				xm.isOpen = false;
				xm.activities = new ArrayList<XianLing>();
				out.print("活动已清，请确认下面三个值是否依次为：null,false,null");
				out.print("currentActivity:" + xm.getCurrentActivity() + "<br>");
				out.print("isOpen:" + xm.isOpen + "<br>");
				out.print("xm.disk:" + xm.disk.get(Integer.valueOf(activityId)) + "<br>");
			}
		}
	%>
	<form action="">
		<input type="hidden" name="opType" value="addActivity" /> 活动id:<input
			type="text" name="activityId" /><br>开始时间:<input type="text"
			name="startTime" /><br>持续时间:<input type="text"
			name="lastingTime" /> <br>开启平台:<input type="text"
			name="platform" /><br> <input type="submit" value="新加活动" />
	</form>
	<%
		if (opType != null && !"".equals(opType)) {
			if (opType.equals("addActivity")) {
				String startTime = request.getParameter("startTime");
				String platform = request.getParameter("platform");
				String lastingTime = request.getParameter("lastingTime");
				String activityId = request.getParameter("activityId");
				if (startTime != null && !"".equals(startTime) || platform != null && !"".equals(platform) || lastingTime != null && !"".equals(lastingTime) || activityId != null && !"".equals(activityId)) {
					XianLing xl = new XianLing(startTime, "2000-01-01 00:00:00", platform, "", "");
					xl.setEndTime(xl.getStartTime() + Long.valueOf(lastingTime));
					xl.setActivityId(Integer.valueOf(activityId));
					xm.activities.add(xl);
					out.print("添加新活动成功，活动id:" + xl.getActivityId() + "<br>");
				} else {
					out.print("所有项都要填写!<br>");
				}
			}

		}
	%>
	<hr>
	<form action="">
		<input type="hidden" name="opType" value="endCurrActivity" />结束时间:<input
			type="text" name="endTime" /> <input type="submit"
			value="设置当前活动结束时间" />
	</form>
	<%
		if (opType != null && !"".equals(opType)) {
			if (opType.equals("endCurrActivity")) {
				String endTime = request.getParameter("endTime");
				if (endTime != null && !"".equals(endTime)) {
					long now = System.currentTimeMillis();
					for (XianLing xl : xm.activities) {
						if (xl.getStartTime() < now && now < xl.getEndTime()) {
							xl.setEndTime(TimeTool.formatter.varChar19.parse(endTime));
							out.print("设置当前活动结束时间为：" + endTime + "<br>");
						}
					}
				}
			}
		}
	%>
	<hr>
	<form action="">
		<input type="hidden" name="opType" value="beforeNotice" />提前多少分钟提示<input
			type="text" name="beforeTime" />提示间隔分钟<input
			type="text" name="spaceTime" /> <input type="submit"
			value="提交" />
	</form>
	<%
		if (opType != null && !"".equals(opType)) {
			if (opType.equals("beforeNotice")) {
				String beforeTime = request.getParameter("beforeTime");
				if (beforeTime != null && !"".equals(beforeTime)) {
					xm.提前多少分钟提醒 = Integer.valueOf(beforeTime);
				}
				String spaceTime = request.getParameter("spaceTime");
				if (spaceTime != null && !"".equals(spaceTime)) {
					xm.提醒间隔分钟 = Integer.valueOf(spaceTime);
				}
				long now = System.currentTimeMillis();
				out.print("活动开启时间:"+xm.活动开启时间+"<br>");
				out.print("提前多少分钟提醒:"+xm.提前多少分钟提醒+"<br>");
				out.print("提醒间隔分钟:"+xm.提醒间隔分钟+"<br>");
				out.print("距离开启剩余分钟数:"+(xm.活动开启时间 - now)/60000+"<br>");
				out.print("距离上次提醒:"+(now - xm.上次提醒时间)/60000+"<br>");
				out.print("超过提醒间隔:"+((now - xm.上次提醒时间) > xm.提前多少分钟提醒 * 60000)+"<br>");
				out.print((xm.活动开启时间-now) > 0l );
				out.print("<br>");
				out.print((xm.活动开启时间-now) <= xm.提前多少分钟提醒*60000);
				out.print("<br>");
				out.print("超过提醒间隔:"+((now - xm.上次提醒时间) > xm.提醒间隔分钟 * 60000)+"<br>");
				out.print("<br>");
				out.print(xm.已提示过);
				out.print("<br>");
			}
		}
	%>
	<hr>
	<form action="">
		<input type="hidden" name="opType" value="clearAllActivity" /><input
			type="submit" value="清空所有活动" />
	</form>
	<%
		if (opType != null && !"".equals(opType)) {
			if (opType.equals("clearAllActivity")) {
				xm.activities.clear();
				out.print("活动已清空<br>");
			}
		}
	%>
	<hr>
	<form action="">
		<input type="hidden" name="opType" value="updateWolf" /> 次数:<input
			type="text" name="times" /> <input type="submit" value="改狼吃羊次数" />
	</form>
	<%
		if (opType != null && !"".equals(opType)) {
			if (opType.equals("updateWolf")) {
				String times = request.getParameter("times");
				if (times != null && !"".equals(times)) {
					WolfManager.getInstance().ONE_DAY_MAX_JOIN_NUMS = Integer.valueOf(times);
					out.print("次数修改为：" + WolfManager.getInstance().ONE_DAY_MAX_JOIN_NUMS);
				}
			}

		}
	%>
	<hr>
	<form action="">
		<input type="hidden" name="opType" value="addWolf" /> 开始时间:<input
			type="text" name="startTime" />(2016-09-30 00:00:00) <br> 结束时间:<input
			type="text" name="endTime" /> <br> 平台:<input type="text"
			name="platForm" />(sqage) <br> 小时:<input type="text"
			name="hourStr" /> (19,21) <br> 分钟:<input type="text"
			name="minuteStr" />(00,30)与前面小时对应，示例表示19:00-21:30 <br> 显示时间:<input
			type="text" name="timeshow" /> <br> <input type="submit"
			value="加狼吃羊活动" />
	</form>
	<%
		if (opType != null && !"".equals(opType)) {
			if (opType.equals("addWolf")) {
				String startTime = request.getParameter("startTime");
				String endTime = request.getParameter("endTime");
				String platForm = request.getParameter("platForm");
				String hourStr = request.getParameter("hourStr");
				String minuteStr = request.getParameter("minuteStr");
				String timeshow = request.getParameter("timeshow");
				TimeRangeConfig tc = new TimeRangeConfig();
				tc.setStartTime(TimeTool.formatter.varChar19.parse(startTime));
				tc.setEndTime(TimeTool.formatter.varChar19.parse(endTime));

				Platform platForm1 = PlatformManager.getInstance().getPlatformByENName(platForm);

				tc.setPlatForms(new Platform[] { platForm1 });

				List<DayilyTimeDistance> times = new ArrayList<DayilyTimeDistance>();
				String[] tempP1 = hourStr.split("\\|");
				String[] tempP2 = minuteStr.split("\\|");
				if (tempP1.length != tempP2.length) {
					throw new Exception("狼吃羊活动加载失败：小时与分钟配表不匹配！");
				}
				for (int i = 0; i < tempP1.length; i++) {
					int[] point1 = RefreshSpriteManager.parse2Int(tempP1[i].split(","));
					int[] point2 = RefreshSpriteManager.parse2Int(tempP2[i].split(","));
					if ((point1.length != 2) || (point2.length != 2)) {
						throw new Exception("狼吃羊活动加载失败：小时或者分钟开启结束时间不匹配！");
					}
					times.add(new DayilyTimeDistance(point1[0], point2[0], point1[1], point2[1]));
				}
				tc.setTimes(times);
				tc.setTimeShow(timeshow);

				ActivityManager.getInstance().configs.add(tc);
				out.print("添加活动完毕");
			}

		}
	%>
	<hr>
	<form action="">
		<input type="hidden" name="opType" value="test" /> playerName:<input
			type="text" name="playerName" /> <input type="submit" value="开发专用" />
	</form>
	<%
		xcm.bornPoint = new int[] { 805, 829 };
		if (opType != null && !"".equals(opType)) {
			if (opType.equals("test")) {
				{

				}
				String name = request.getParameter("playerName");

				if (name != null && !"".equals(name)) {
					Player p1 = null;
					try {
						p1 = PlayerManager.getInstance().getPlayer(name);
					} catch (Exception e) {
						try {
							p1 = PlayerManager.getInstance().getPlayer(Long.valueOf(name));
						} catch (Exception ee) {
							out.print("获取角色异常" + ee);
						}
					}
					if (p1 != null) {
						out.print("p1.getCurrentGame():" + p1.getCurrentGame() + "<br>");
						XianLingChallenge remove = null;
						List<XianLingChallenge> removeList = new ArrayList<XianLingChallenge>();
						List<Long> removePId = new ArrayList<Long>();
						for (XianLingChallengeThread t : xcm.threads) {
							out.print(t.getName() + "<br>");

							Map<Long, Long> timeMaps = t.timeMaps;

							out.print("timeMaps.size" + timeMaps.size() + "<br>");
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							for (Long id : timeMaps.keySet()) {
								out.print("id:" + id + ",time:" + sdf.format(timeMaps.get(id)) + "<br>");
								if (System.currentTimeMillis() - timeMaps.get(id) > 6 * 60 * 1000) {
									removePId.add(id);
								}
							}

							List<XianLingChallenge> gameList = t.getGameList();
							if (gameList.size() > 0) {
								for (XianLingChallenge xc : gameList) {
									out.print(xc.getPlayerId() + "<br>");
									try {
										if (xc.getPlayerId() == p1.getId()) {
											remove = xc;
											out.print("t.findGame(p1):" + t.findGame(p1) + "<br>");
											out.print("p1.xl_chanllengeFlag:" + p1.xl_chanllengeFlag + "<br>");
											out.print("玩家正在此挑战,result:" + xc.getResult() + "<br>");
										}
										if (removePId.contains(xc.getPlayerId())) {
											removeList.add(xc);
											timeMaps.remove(xc.getPlayerId());
										}
									} catch (Exception e) {
										XianLingManager.logger.error("[仙灵] [XianLingChallengeManager.destroy] [异常]", e);
										e.printStackTrace();
									}
								}
								//gameList.remove(remove);
								if (removeList.size() > 0) {
									gameList.removeAll(removeList);
									out.print("后台把玩家移出线程：" + Arrays.toString(removeList.toArray()) + "<br>");
									if (XianLingManager.instance.logger.isWarnEnabled()) {
										XianLingManager.instance.logger.warn("[后台把玩家移出线程] [" + Arrays.toString(removeList.toArray()) + "]");
									}

								}
							}
						}

					}
				} else {
					out.print("请输入角色名和要加的积分<br>");
				}
			}
		}
	%>
	<hr>
	<%
		
	%>
</body>
</html>