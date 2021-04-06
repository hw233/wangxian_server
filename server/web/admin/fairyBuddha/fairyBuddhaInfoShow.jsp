<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page
	import="com.fy.engineserver.activity.fairyBuddha.FairyBuddhaManager"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page
	import="com.fy.engineserver.activity.fairyBuddha.FairyBuddhaInfo"%>
<%@page import="com.fy.engineserver.event.EventRouter"%>
<%@page import="com.fy.engineserver.event.Event"%>
<%@page import="java.io.Serializable"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page
	import="com.fy.engineserver.activity.fairyBuddha.WorshipAward"%>
<%@page
	import="com.fy.engineserver.activity.fairyBuddha.WeekdayAward"%>
<%@page import="com.fy.engineserver.activity.shop.ActivityProp"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.Vector"%>
<%@page
	import="com.fy.engineserver.activity.fairyBuddha.StatueForFairyBuddha"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="java.util.Collections"%>
<%@page import="com.fy.engineserver.activity.fairyBuddha.Voter"%>
<%@page import="com.fy.engineserver.core.res.ResourceManager"%>
<%@page import="com.fy.engineserver.sprite.Sprite"%>
<%@page import="com.fy.engineserver.core.res.Avata"%>
<%@page import="com.fy.engineserver.sprite.npc.NPC"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.boss.operation.model.GamePlatform"%>
<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.core.LivingObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.constants.GameConstant"%>
<%@page
	import="com.fy.engineserver.util.server.TestServerConfigManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>仙尊信息显示</title>
<style type="text/css">
body {
	font-size: 12px;
}

table,th,td {
	border: 1px solid blue;
	border-spacing: 0px;
	border-collapse: collapse;
}
</style>
<%
	FairyBuddhaManager fbm = FairyBuddhaManager.getInstance();
	ArrayList<FairyBuddhaInfo> allFairybuddhas = (ArrayList<FairyBuddhaInfo>) fbm.disk.get(fbm.KEY_历届仙尊);
	
	//fbm.setFairyBuddhaMap(new Hashtable<Byte, FairyBuddhaInfo>());
	//fbm.disk.put(fbm.getKey(0) + fbm.KEY_仙尊, (Serializable) fbm.getFairyBuddhaMap());
	for (StatueForFairyBuddha statue : fbm.getStatueList()) {
		if (statue.getCareer() != 3) {
			continue;
		}
		/*Game game = GamePlayerManager.getInstance().getPlayer("aaa").getCurrentGame();
		for (LivingObject ll : game.getLivingObjects()) {
			if (ll instanceof NPC) {
				if (((NPC)ll).getName().equals("灵尊仙尊镜像")) {
					((NPC)ll).setAvata(statue.getAvatas());
					((NPC)ll).setAvataType(statue.getAvataType());
					out.println("<br>" +ll + " === " + ((NPC)ll).getId() + "<nr>");
				}
			}
		}*/

		NPC npc = statue.getDefaultNpc();
		statue.getDefaultNpc().setAvata(statue.getAvatas());
		statue.getDefaultNpc().setAvataType(statue.getAvataType());
		Avata ss = ResourceManager.getInstance().getAvata(statue.getDefaultNpc());
		statue.getDefaultNpc().setAvata(ss.avata);
		statue.getDefaultNpc().setAvataType(ss.avataType);
		//out.println("==:" + statue.getDefaultNpc().getName() + "<br>");
		out.println("仙尊形象数据:<br>" + statue.toString() + "<br>");
		//out.println(npc + " === " + npc.getId() + "<br> <br><br>");
		//out.print("==:" + npc.getName() +""+Arrays.toString(npc.getAvata())+""+Arrays.toString(npc.getAvataType())+"<br>");
	}

	Map<Byte, FairyBuddhaInfo> fairyBuddhaMap = fbm.getFairyBuddhaMap();
	if (fairyBuddhaMap != null && fairyBuddhaMap.size() > 0) {
		for (Byte career : fairyBuddhaMap.keySet()) {
			FairyBuddhaInfo fbi = fairyBuddhaMap.get(career);
			if (fbi != null) {
				String key = fbm.getKey(0) + fbm.KEY_膜拜奖励等级 + "_" + fbi.getId();
				if (fbm.disk.get(key) != null) {
					out.print("career:" + career + ",膜拜奖励等级key:" + key + "<br>");
					out.print("career:" + career + ",膜拜奖励等级:" + fbm.disk.get(key) + "<br>");
				}
			}
		}
	}
	out.print("<hr>");
	Map<Byte, Vector<FairyBuddhaInfo>> electorMapLast = (Map<Byte, Vector<FairyBuddhaInfo>>) fbm.disk.get(fbm.getKey(-1) + fbm.KEY_参选者);
	if (electorMapLast != null) {
		out.print("上周期:key:" + fbm.getKey(-1) + fbm.KEY_参选者 + ",结果条数:" + electorMapLast.size() + "<br>");
		for (byte i : electorMapLast.keySet()) {
			List<FairyBuddhaInfo> electors = electorMapLast.get(i);
			out.print("职业:" + i + "<br>");
			if (electors != null && electors.size() > 0) {
				for (FairyBuddhaInfo fbi : electors) {
					out.print(fbi.getLogString() + "<br>");
				}
			}
		}
	} else {
		out.print("上周期ddc读取投票榜数据为0条,key:"+fbm.getKey(-1) + fbm.KEY_参选者+"<br>");
	}
	out.print("<hr>");
	Map<Byte, Vector<FairyBuddhaInfo>> electorMap = (Map<Byte, Vector<FairyBuddhaInfo>>) fbm.disk.get(fbm.getKey(0) + fbm.KEY_参选者);
	if (electorMap != null) {
		out.print("本周期:key:" + fbm.getKey(0) + fbm.KEY_参选者 + ",结果条数:" + electorMap.size() + "<br>");
		for (byte i : electorMap.keySet()) {
			List<FairyBuddhaInfo> electors = electorMap.get(i);
			out.print("职业:" + i + "<br>");
			if (electors != null && electors.size() > 0) {
				for (FairyBuddhaInfo fbi : electors) {
					out.print(fbi.getLogString() + "<br>");
				}
			}
		}
	} else {
		out.print("本周期ddc读取投票榜数据为0条,key:"+fbm.getKey(0) + fbm.KEY_参选者+"<br>");
	}
	out.print("<hr>");
	/*Map<Byte, FairyBuddhaInfo> fairyBuddhaMap2 = (Map<Byte, FairyBuddhaInfo>) fbm.disk.get(fbm.getKey(0) + fbm.KEY_仙尊);
	if (fairyBuddhaMap2 != null) {
		out.print("从ddc读取仙尊数据到内存<br>");
		for (byte i : fairyBuddhaMap2.keySet()) {
			FairyBuddhaInfo fbi = fairyBuddhaMap2.get(i);
			if (fbi != null) {
				out.print("职业:" + i + ":" + fbi.getLogString() + "<br>");
			}
		}
	} else {
		out.print("ddc读取仙尊数据为0条<br>");
	}
	out.print("<hr>");*/
	String act = request.getParameter("act");
	if (null != act && !"".equals(act)) {
		if (!TestServerConfigManager.isTestServer()) {
			String pwd = request.getParameter("pwd");
			if (null == pwd && "".equals(pwd)) {
				out.print("不是测试服,请输入密码");
				return;
			} else if (!pwd.equals("xianzunmodifypwd")) {
				out.print("密码不正确,请确认清楚");
				return;
			}

		}
		if ("read".equals(act)) {
			fbm.readDataFromDisk2Memory();
			out.print("从内存读取数据<br>");
		}
		if ("joinElector".equals(act)) {
			String playerName = request.getParameter("playerName");
			if (null != playerName && !"".equals(playerName)) {
				Player p = PlayerManager.getInstance().getPlayer(playerName);
				if (p != null) {
					fbm.joinElector(p, p.getCurrSoul().getSoulType());
					out.print("加入本期投票榜成功<br>");
				} else {
					out.print("玩家不存在<br>");
				}
			}
		}
		if ("joinLastElector".equals(act)) {
			String playerName = request.getParameter("playerName");
			if (null != playerName && !"".equals(playerName)) {
				Player p = PlayerManager.getInstance().getPlayer(playerName);
				if (p != null) {
					fbm.joinLastElector(p, p.getCurrSoul().getSoulType());
					out.print("加入上期投票榜成功<br>");
				} else {
					out.print("玩家不存在<br>");
				}
			}
		}
		if ("newFairyBuddha".equals(act)) {
			fbm.newFairyBuddha();
		}
		if ("clearLastElectors".equals(act)) {
			fbm.disk.put(fbm.getKey(-1) + fbm.KEY_参选者, new Hashtable<Byte, List<FairyBuddhaInfo>>());
		}
		if ("putStatue".equals(act)) {
			out.print("[仙尊] [摆放仙尊]<br>");
			fbm.putStatue();
			out.print("摆放仙尊完成");
		}
		if ("clearVoter".equals(act)) {
			String playerName = request.getParameter("playerName");
			if (null != playerName && !"".equals(playerName)) {
				Player p = PlayerManager.getInstance().getPlayer(playerName);
				if (p != null) {
					fbm.getVoterIdList().remove(p.getId());
					fbm.logger.warn("[仙尊] [后台清除投票限制]" + p.getLogString());
					out.print("清除投票限制成功<br>");
				} else {
					out.print("玩家不存在<br>");
				}
			}
		}
		if ("changemaxnum".equals(act)) {
			String maxnum = request.getParameter("maxnum");
			if (null != maxnum && !"".equals(maxnum)) {
				fbm.MAX_ELECTORS = Integer.valueOf(maxnum);
				out.print("修改最大入榜数量为:" + fbm.MAX_ELECTORS);
			}
		}
		if ("delOnePlayer".equals(act)) {
			String id = request.getParameter("deleteID");
			if (null != id && !"".equals(id)) {
				ArrayList<FairyBuddhaInfo> fb4remove = new ArrayList<FairyBuddhaInfo>();
				for (FairyBuddhaInfo fbi : allFairybuddhas) {
					if (fbi.getId() == Long.valueOf(id)) {
						fb4remove.add(fbi);
					}
				}
				if (fb4remove.size() > 0) {
					for (FairyBuddhaInfo fbi : fb4remove) {
						allFairybuddhas.remove(fbi);
					}
					fbm.disk.put(fbm.KEY_历届仙尊, allFairybuddhas);
					out.print("删除历届仙尊记录:仙尊id " + id + "<br>");
				}

				Map<Byte, FairyBuddhaInfo> fairyBuddhaMapLast = (Map<Byte, FairyBuddhaInfo>) fbm.disk.get(fbm.getKey(-1) + fbm.KEY_仙尊);
				if (fairyBuddhaMapLast != null && fairyBuddhaMapLast.size() > 0) {
					for (int career : fairyBuddhaMapLast.keySet()) {
						FairyBuddhaInfo fbiLase = fairyBuddhaMapLast.get(career);
						if (fbiLase != null) {
							if (fbiLase.getId() == Long.valueOf(id)) {
								fairyBuddhaMapLast.remove(career);
							}
						}
						fbm.disk.put(fbm.getKey(-1) + fbm.KEY_仙尊, (Serializable) fairyBuddhaMapLast);
						out.print("删除上届仙尊记录:仙尊id " + id + "<br>");
					}
				}

			}
		}

		if ("changeSoulCD".equals(act)) {
			String playerId = request.getParameter("playerId");
			if (playerId != null && !"".equals(playerId)) {

				Player p = PlayerManager.getInstance().getPlayer(Long.valueOf(playerId));
				Soul[] souls = p.getSouls();
				int time = 1000;
				for (Soul s : souls) {
					s.switchCd = time;
				}
				out.print("元神切换时间设置为：" + time);
			}
		}
	}
%>
</head>
<body>
<!-- <form action=""><input type="hidden" name="act" value="read" />
<input type="submit" value="从ddc读取数据到内存" /></form> -->
<form action=""><input type="hidden" name="act"
	value="clearVoter" />角色名:<input type="text" name="playerName" value="" />
非测试服请输入密码:<input name="pwd" type="password" value="" /><input
	type="submit" value="解除本期投票限制" /></form>
<hr>
<form action=""><input type="hidden" name="act"
	value="joinLastElector" /> 角色名:<input type="text" name="playerName"
	value="" /> 非测试服请输入密码:<input name="pwd" type="password" value="" /> <input
	type="submit" value="加入上期投票榜" /></form>
<hr>
<form action=""><input type="hidden" name="act"
	value="joinElector" /> 角色名:<input type="text" name="playerName"
	value="" /> 非测试服请输入密码:<input name="pwd" type="password" value="" /> <input
	type="submit" value="加入本期投票榜" /></form>
<hr>
<form action=""><input type="hidden" name="act"
	value="newFairyBuddha" /> 非测试服请输入密码:<input name="pwd" type="password"
	value="" /> <input type="submit" value="产生下一期天尊" /></form>
<hr>
<form action=""><input type="hidden" name="act" value="putStatue" />
非测试服请输入密码:<input name="pwd" type="password" value="" /> <input
	type="submit" value="摆放天尊" /></form>
<hr>

<form action=""><input type="hidden" name="act"
	value="clearLastElectors" />非测试服请输入密码:<input name="pwd"
	type="password" value="" /> <input type="submit" value="清除上期投票榜数据" /></form>
<hr>
<form action=""><input type="hidden" name="act"
	value="changemaxnum" /> 最大入榜数量:<input type="text" name="maxnum"
	value="" /> 非测试服请输入密码:<input name="pwd" type="password" value="" /> <input
	type="submit" value="设置最大入榜数量" /></form>
<hr>
<form action=""><input type="hidden" name="act"
	value="delOnePlayer" /> 玩家id:<input type="text" name="deleteID"
	value="" /> 非测试服请输入密码:<input name="pwd" type="password" value="" /> <input
	type="submit" value="从历届仙尊列表中删除此玩家" /></form>
<hr>
<form action=""><input type="hidden" name="act"
	value="changeSoulCD" /> 玩家id:<input type="text" name="playerId"
	value="" /> 非测试服请输入密码:<input name="pwd" type="password" value="" /> <input
	type="submit" value="修改切元神cd" /></form>
<hr>
<%
if (null != allFairybuddhas) {
	out.print("历届仙尊:<br>");
	for (FairyBuddhaInfo fbi : allFairybuddhas) {
		out.print(fbi.getLogString() + "<br>");
		out.print(Arrays.toString(fbi.getStatue().getAvatas()) + "<br>");
		out.print(fbi.getStatue().toString() + "<br>");
	}
	out.print("<hr>");
}
%>
<form action=""><input type="hidden" name="act"
	value="showWorshipAward" /><input type="hidden" name="pwd"
	value="xianzunmodifypwd" /> <input type="submit" value="查看膜拜奖励" /></form>

<%
	if ("showWorshipAward".equals(act)) {
		List<WorshipAward> worshipAwardList = fbm.getWorshipAwardList();
		if (worshipAwardList != null) {
%>
<table style="border: 1px">
	<tr>
		<th>开始时间</th>
		<th>结束时间</th>
		<th>平台</th>
		<th>开放服</th>
		<th>限制服</th>
		<th>名字</th>
		<th>描述</th>
		<th>价格</th>
		<th>周奖励</th>
	</tr>
	<%
		for (WorshipAward wa : worshipAwardList) {
	%>
	<tr>
		<td><%=wa.getStartTime()%></td>
		<td><%=wa.getEndTime()%></td>
		<td><%=Arrays.toString(wa.getSf4a().getPlatForms())%></td>
		<td><%=wa.getSf4a().getOpenServerNames()%></td>
		<td><%=wa.getSf4a().getNotOpenServerNames()%></td>
		<td>
		<%
			Map<Byte, String> awardNameMap = wa.getAwardNameMap();
						if (awardNameMap != null) {
							for (Byte level : awardNameMap.keySet()) {
								out.print("{" + level + "," + awardNameMap.get(level) + "}<br>");
							}
						}
		%>
		</td>
		<td>
		<%
			Map<Byte, String> desMap = wa.getDesMap();
						if (desMap != null) {
							for (Byte level : desMap.keySet()) {
								out.print("{" + level + "," + desMap.get(level) + "}<br>");
							}
						}
		%>
		</td>
		<td>
		<%
			Map<Byte, Long> priceMap = wa.getPriceMap();
						if (priceMap != null) {
							for (Byte level : priceMap.keySet()) {
								out.print("{" + level + "," + priceMap.get(level) + "}<br>");
							}
						}
		%>
		</td>
		<td>
		<%
			Map<Byte, Map<Byte, WeekdayAward>> levelAward = wa.getLevelAward();
						if (levelAward != null) {
							for (Byte level : levelAward.keySet()) {
								Map<Byte, WeekdayAward> weekdayAward = levelAward.get(level);
								if (weekdayAward != null) {
									for (Byte day : weekdayAward.keySet()) {
										WeekdayAward wda = weekdayAward.get(day);
										ActivityProp[] props = wda.getProps();
										String str = "";
										if (props != null) {
											for (ActivityProp p : props) {
												if (p != null) {
													str += "[" + p.toString() + "]";
												}
											}
										}
										out.print("{档次" + level + ",周几" + day + ",经验系数" + wda.getExpMul() + ",绑银系数" + wda.getSilverMul() + "," + str + "}<br><br>");
									}
								}

							}
						}
		%>
		</td>
	</tr>
	<%
		}
	%>
</table>
<%
	}
	}
%>
<%!/**
	 * 加入上期投票榜
	 * @param p
	 */
	public void joinLastElector(Player p, int soulType) {
		try {
			FairyBuddhaManager fbm = FairyBuddhaManager.getInstance();
			byte career = p.getSoul(soulType).getCareer();
			Soul soul = p.getCurrSoul();
			StatueForFairyBuddha statue = new StatueForFairyBuddha(p.getName(), career, soul.getMaxHp(), soul.getPhyAttack(), soul.getMagicAttack(), p.getAvata(), p.getAvataRace(), p.getAvataSex(), p.getAvataType());
			// 下面这个"000"是跟客户端约定的,不要擅自更改
			FairyBuddhaInfo fbi = new FairyBuddhaInfo(p.getId(), p.getName(), career, p.getLevel(), p.getCountry(), 0, 0, "000", new Vector<Voter>(), new Vector<Voter>(), statue);
			Map<Byte, List<FairyBuddhaInfo>> electorMap = fbm.getCurrentElectorMap(fbm.getKey(-1) + fbm.KEY_参选者);
			if (electorMap.get(career) == null) {
				electorMap.put(career, new Vector<FairyBuddhaInfo>());
			}
			electorMap.get(career).add(fbi);
			//Collections.sort(electorMap.get(career), fbm.order);
			fbm.disk.put(fbm.getKey(-1) + fbm.KEY_参选者, (Serializable) electorMap);
			fbm.logger.warn("[" + p.getLogString() + "] [仙尊] [页面加入投票榜] [soulType:" + soulType + "] [职业:" + career + "] [国家:" + p.getCountry() + "]");
		} catch (Exception e) {
			FairyBuddhaManager.logger.warn("[" + p.getLogString() + "] [仙尊] [页面加入投票榜异常]" + e);
			e.printStackTrace();
		}
	}%>
</body>
</html>