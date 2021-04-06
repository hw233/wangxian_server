<%@page import="com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.WeekAndMonthChongZhiActivity"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="com.fy.engineserver.sprite.npc.NPCManager"%>
<%@page import="com.fy.engineserver.sprite.npc.MemoryNPCManager"%>
<%@page import="com.fy.engineserver.sprite.npc.NPC"%>
<%@page import="com.fy.engineserver.sprite.TeamSubSystem"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	</head>
	<body>
		<%
			long weekID = 176;
			HashSet<Integer> monthIDs = new HashSet<Integer>();
			monthIDs.add(177);
			monthIDs.add(178);
			monthIDs.add(179);
			monthIDs.add(180);
			monthIDs.add(181);
			monthIDs.add(182);
			monthIDs.add(183);
			monthIDs.add(184);
			monthIDs.add(185);
			//周反馈活动
			WeekAndMonthChongZhiActivity week = null;
			for (WeekAndMonthChongZhiActivity ac : NewChongZhiActivityManager.weekMonthDatas) {
				if (ac.getDataID() == weekID) {
					week = ac;
					break;
				}
			}
			//月反馈活动
			WeekAndMonthChongZhiActivity[] months = new WeekAndMonthChongZhiActivity[monthIDs.size()];
			int in = 0;
			for (WeekAndMonthChongZhiActivity ac : NewChongZhiActivityManager.weekMonthDatas) {
				if (monthIDs.contains(ac.getDataID())) {
					months[in] = ac;
					in++;
				}
			}
			//最近充值金额  通过日志来的  周的日志
			HashMap<Long, Long> playerMoneys_week = new HashMap<Long, Long>();
			playerMoneys_week.put(1101000000000286721L, 10000000L);
			week.player_moneys.putAll(playerMoneys_week);
			
			//月的日志
			HashMap<Long, Long> playerMoneys_month = new HashMap<Long, Long>();
			playerMoneys_month.put(1101000000000286721L, 20000000L);
			for (WeekAndMonthChongZhiActivity month : months) {
				if (month != null) {
					month.player_moneys.putAll(playerMoneys_month);
				}
			}
			
			{
				//领取过周礼包  通过日志来的
				HashSet<Long> playerGet_week = new HashSet<Long>();
				playerGet_week.add(1101000000000286721L);
				week.player_gets.addAll(playerGet_week);
				
				//领取过月礼包    key是活动ID
				HashMap<Integer, HashSet<Long>> playerGet_months = new HashMap<Integer, HashSet<Long>>();
				playerGet_months.put(177, new HashSet<Long>());
				playerGet_months.get(177).add(1101000000000286721L);
				playerGet_months.put(179, new HashSet<Long>());
				playerGet_months.get(179).add(1101000000000286721L);
				for (WeekAndMonthChongZhiActivity month : months) {
					HashSet<Long> ids = playerGet_months.get(month.getDataID());
					if (ids != null) {
						month.player_gets.addAll(ids);
					}
				}
			}
			
			{
				//购买过周礼包  通过日志来的
				HashSet<Long> playerBuy_week = new HashSet<Long>();
				
				week.player_buys.addAll(playerBuy_week);
				
				//购买过月礼包    key是活动ID
				HashMap<Integer, HashSet<Long>> playerBuy_months = new HashMap<Integer, HashSet<Long>>();
				playerBuy_months.put(178, new HashSet<Long>());
				playerBuy_months.get(178).add(1101000000000286721L);
				
				for (WeekAndMonthChongZhiActivity month : months) {
					HashSet<Long> ids = playerBuy_months.get(month.getDataID());
					if (ids != null) {
						month.player_buys.addAll(ids);
					}
				}
			}
			
			//保存活动diskcatch
			WeekAndMonthChongZhiActivity.diskcache.put(WeekAndMonthChongZhiActivity.DISK_KEY_MONEY + week.getDataID(), week.player_moneys);
			WeekAndMonthChongZhiActivity.diskcache.put(WeekAndMonthChongZhiActivity.DISK_KEY_GET + week.getDataID(), week.player_gets);
			WeekAndMonthChongZhiActivity.diskcache.put(WeekAndMonthChongZhiActivity.DISK_KEY_BUY + week.getDataID(), week.player_buys);
			
			for (WeekAndMonthChongZhiActivity month : months) {
				WeekAndMonthChongZhiActivity.diskcache.put(WeekAndMonthChongZhiActivity.DISK_KEY_MONEY + month.getDataID(), month.player_moneys);
				WeekAndMonthChongZhiActivity.diskcache.put(WeekAndMonthChongZhiActivity.DISK_KEY_GET + month.getDataID(), month.player_gets);
				WeekAndMonthChongZhiActivity.diskcache.put(WeekAndMonthChongZhiActivity.DISK_KEY_BUY + month.getDataID(), month.player_buys);
			}
			
		%>
	</body>
</html>
