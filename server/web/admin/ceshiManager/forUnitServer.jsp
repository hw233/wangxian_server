<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.io.File"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFWorkbook"%>
<%@page import="java.io.InputStream"%>
<%@page import="org.apache.poi.poifs.filesystem.POIFSFileSystem"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFSheet"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFRow"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFCell"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@page import="com.fy.engineserver.zongzu.manager.ZongPaiManager"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="java.util.concurrent.ConcurrentHashMap"%>
<%@page import="com.fy.engineserver.zongzu.data.ZongPai"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.core.JiazuSubSystem"%>
<%@page import="com.fy.engineserver.economic.ExpenseReasonType"%>
<%@page import="com.fy.engineserver.constants.InitialPlayerConstant"%>
<%@page import="com.fy.engineserver.economic.BillingCenter"%>
<%@page import="com.fy.engineserver.country.data.Country"%>
<%@page import="com.fy.engineserver.country.manager.CountryManager"%>
<%@page import="com.fy.engineserver.chat.ChatMessageService"%>
<%@page import="com.fy.engineserver.zongzu.ZongPaiSubSystem"%>
<%@page import="com.fy.engineserver.septstation.SeptStation"%>
<%@page
	import="com.fy.engineserver.septstation.service.SeptStationManager"%>
<%@page
	import="com.fy.engineserver.sept.exception.NameExistException"%>
<%@page
	import="com.fy.engineserver.sept.exception.StationExistException"%>
<%@page
	import="com.fy.engineserver.sept.exception.SeptNotExistException"%>

<%@page
	import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager"%>
<%@page
	import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page
	import="com.fy.engineserver.septbuilding.entity.SeptBuildingEntity"%>
<%@page import="com.fy.engineserver.septstation.SeptStationInfo"%>
<%@page
	import="com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet"%>
<%@page
	import="com.fy.engineserver.util.server.TestServerConfigManager"%>
<%@page import="com.fy.engineserver.sprite.npc.NPC"%>
<%@page import="com.fy.engineserver.battlefield.jjc.BattleTeam"%>
<%@page import="com.fy.engineserver.battlefield.jjc.JJCManager"%>

<%@page
	import="com.fy.engineserver.battlefield.jjc.BattleTeamMember"%>
<%@page import="com.fy.engineserver.achievement.AchievementManager"%>
<%@page
	import="com.fy.engineserver.playerAims.manager.PlayerAimManager"%>
<%@page import="com.fy.engineserver.achievement.RecordAction"%>
<%@page
	import="com.fy.engineserver.jiazu2.manager.JiazuEntityManager2"%>
<%@page import="com.fy.engineserver.jiazu2.instance.JiazuMember2"%>
<%@page import="com.fy.engineserver.event.EventRouter"%>
<%@page import="com.fy.engineserver.event.cate.EventWithObjParam"%>
<%@page import="com.fy.engineserver.event.Event"%>
<%@page import="com.fy.engineserver.util.gmstat.RecordEvent"%>
<%@page
	import="com.fy.engineserver.util.gmstat.event.ArticleRecordEvent"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@page import="com.fy.engineserver.util.datacheck.DataCheckManager"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.util.gmstat.EventManager"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.engineserver.activity.ActivitySubSystem"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>合服设置</title>
<%!public void sendStatMail(RecordEvent event) {
		if (!TestServerConfigManager.isTestServer()) {
			String[] titles = { "类型", "服务器", "账号", "角色名", "角色id", "IP地址", "物品集[物品名,统计名,颜色,数量]", "操作人", "操作时间" };
			String title = "后台物品相关操作";
			if (event != null) {
				StringBuffer content = new StringBuffer();
				content.append("<table style='font-size=12px; border=1'><tr bgcolor='green'>");
				for (String t : titles) {
					content.append("<th>");
					content.append(t);
					content.append("</th>");
				}
				content.append("</tr>");
				content.append("<tr>" + ((ArticleRecordEvent) event).getHtmlStr2() + "</tr>");
				content.append("</table>");
				DataCheckManager.sendMail("[后台物品相关操作] [" + GameConstants.getInstance().getServerName() + "]", content.toString());
			}
		}
	}

	public int[] changeIntegerToInt(Integer[] integerArray) {
		int[] intArray = new int[integerArray.length];
		for (int i = 0; i < integerArray.length; i++) {
			intArray[i] = integerArray[i].intValue();
		}
		return intArray;
	}%>

</head>
<body>
<%
	out.print("<font color='blue' size='2'>本页面用于合服设置角色信息(目前包括个人基本信息，道具发放，家族宗派)</font><br>");
	out.print("<hr>");
	List<String> ceshiUserNameList = new ArrayList<String>();//存放测试的账号信息，只能给这些账号下的角色调整数据，以免误调了正式玩家的数据
	ceshiUserNameList.add("102449566");
	ceshiUserNameList.add("hefuceshi1");
	ceshiUserNameList.add("hefuceshi2");
	ceshiUserNameList.add("hefuceshi3");
	ceshiUserNameList.add("ceshihefu1");
	ceshiUserNameList.add("ceshihefu2");
	ceshiUserNameList.add("ceshihefu3");
	String password;
	if (TestServerConfigManager.isTestServer()) {
		password = "shezhiHF";
	} else {
		password = request.getParameter("password");
	}
	if (password == null || password.equals("")) {
%>
<form action="">非测试服请输入密码：<input type="password" value=""
	name="password" /> <input type="submit" value="提交"></form>
<%
	return;
	}
	if (!password.equals("shezhiHF")) {
		out.print("密码不正确，请输入密码：<br>");
%><form action=""><input type="password" value="" name="password" />
<input type="submit" value="提交"></form>
<%
	return;
	}
	//String fileName = request.getRealPath("/") + "WEB-INF/game_init_config/forUnitServer.xls";
	String jiazuNameInput = request.getParameter("jiazuNameInput");
	String zongpaiNameInput = request.getParameter("zongpaiNameInput");
	load();

	if (jiazuNameInput != null && !"".equals(jiazuNameInput)) {
		jiazuInfo.setJiazuName(jiazuNameInput);
	}
	if (zongpaiNameInput != null && !"".equals(zongpaiNameInput)) {
		jiazuInfo.setZongpaiName(zongpaiNameInput);
	}
	//out.print(jiazuInfo.toString() + "<br>");

	boolean jiazuNameSingle = true;
	boolean zongpaiNameSingle = true;
	boolean zhanDuiNameSingle = true;
	Map<String, String> zhanDuiChangeName = new HashMap<String, String>();
	//判断家族名和宗派名是否有重复
	String jiazuName = jiazuInfo.getJiazuName();
	if (jiazuName != null && !"".equals(jiazuName)) {
		JiazuManager jiazuManager = JiazuManager.getInstance();
		Map<Long, Jiazu> jiazuMap = jiazuManager.getJiazuLruCacheByID();
		for (Long jiazuId : jiazuMap.keySet()) {
			Jiazu jiazu = jiazuMap.get(jiazuId);
			if (jiazuName.equals(jiazu.getName())) {
				jiazuNameSingle = false;
			}
		}
	}
	String zongpaiName = jiazuInfo.getZongpaiName();
	if (zongpaiName != null && !"".equals(zongpaiName)) {
		ZongPaiManager zpm = ZongPaiManager.getInstance();
		Field f = ZongPaiManager.class.getDeclaredField("zongPaiMap");
		f.setAccessible(true);
		ConcurrentHashMap<Long, ZongPai> zongPaiMap = (ConcurrentHashMap<Long, ZongPai>) f.get(zpm);
		for (Long zongpaiId : zongPaiMap.keySet()) {
			ZongPai zongpai = zongPaiMap.get(zongpaiId);
			if (zongpaiName.equals(zongpai.getZpname())) {
				zongpaiNameSingle = false;
			}
		}
	}
	/*if (zhanDuiList.size() > 0) {
		for (ZhanDui zd : zhanDuiList) {
			String teamName = zd.getTeamName();
			if (teamName != null && !"".equals(teamName)) {
				if (JJCManager.getInstance().getPlayerTeamInfoByName(teamName) != null) {
					zhanDuiNameSingle = false;
					zhanDuiChangeName.put(zd.getPName(), zd.getTeamName());
				}
			}
		}
	}
	if(!zhanDuiNameSingle){
		out.print("战队重名<br>");
		return;
	}*/
	if (!(jiazuNameSingle || zongpaiNameSingle)) {
		out.print("<font color='red' size='5'>家族名[" + jiazuInfo.getJiazuName() + "]和宗派名[" + jiazuInfo.getZongpaiName() + "]</font>重复，请输入新名字<br>");
%>
<form action="">家族名：<input type="text" name="jiazuNameInput"
	value="" /> 宗派名：<input type="text" name="zongpaiNameInput" value="" /><input
	type="hidden" name="password"
	value="<%=password == null ? "" : password%>" /> <input type="submit"
	value="提交"></form>
<%
	} else if (!jiazuNameSingle) {
		out.print("<font color='red' size='5'>家族名[" + jiazuInfo.getJiazuName() + "]</font>重复，请输入新名字：<br>");
%>
<form action="">家族名：<input type="text" name="jiazuNameInput"
	value="" /><input type="hidden" name="zongpaiNameInput"
	value="<%=zongpaiNameInput == null ? "" : zongpaiNameInput%>" /><input
	type="hidden" name="password"
	value="<%=password == null ? "" : password%>" /> <input type="submit"
	value="提交"></form>
<%
	} else if (!zongpaiNameSingle) {
		out.print("<font color='red' size='5'>宗派名[" + jiazuInfo.getZongpaiName() + "]</font>重复，请输入新名字：<br>");
%>
<form action=""><input type="hidden" name="jiazuNameInput"
	value="<%=jiazuNameInput == null ? "" : jiazuNameInput%>" />宗派名：<input
	type="text" name="zongpaiNameInput" value="" /><input type="hidden"
	name="password" value="<%=password == null ? "" : password%>" /> <input
	type="submit" value="提交"></form>
<%
	} else {
		//设置人物基本信息
		if (baseInfoList.size() > 0) {
			Object object = session.getAttribute("authorize.username");
			int count = 1;
			String recorder = (object != null ? object.toString() : "");
			StringBuffer sb = new StringBuffer();
			for (BaseInfo bi : baseInfoList) {
				Player p = PlayerManager.getInstance().getPlayer(bi.getPlayerName());
				if (p == null) {
					out.print("[设置人物基本信息]角色名为<font color='red'>" + bi.getPlayerName() + "</font>的玩家不存在<br>");
					out.print("返回<br>");
					return;
				}
				/*if (!ceshiUserNameList.contains(p.getUsername())) {
					out.print("[设置人物基本信息]角色名为<font color='red'>" + bi.getPlayerName() + "</font>的账号不是测试账号，不予调整<br>");
					out.print("返回<br>");
					return;
				}*/
				String ipaddress = request.getRemoteAddr();
				String title = "后台修改属性";
				String content = "后台修改属性";
				String playername = p.getName();
				long receiverId = p.getId();
				if (bi.getPlayerLevel() > 0) {
					int oldSoulLevel = p.getSoulLevel();
					p.setSoulLevel(bi.getPlayerLevel());
					sb.append("<tr><td>" + title + "</td><td>" + GameConstants.getInstance().getServerName() + "</td><td>" + p.getUsername() + "</td><td>" + playername + "</td><td>" + receiverId + "</td><td>" + ipaddress + "</td>" + "<td>soulLevel</td>" + "<td>" + oldSoulLevel + "</td>" + "<td>" + p.getSoulLevel() + "</td><td>" + recorder + "</td>" + "<td>" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()) + "</td></tr>");
					p.initNextLevelExpPool();
					int oldClassLevel = p.getClassLevel();
					p.setClassLevel((short) (p.getLevel() / 20));
					sb.append("<tr><td>" + title + "</td><td>" + GameConstants.getInstance().getServerName() + "</td><td>" + p.getUsername() + "</td><td>" + playername + "</td><td>" + receiverId + "</td><td>" + ipaddress + "</td>" + "<td>classLevel</td>" + "<td>" + oldClassLevel + "</td>" + "<td>" + p.getClassLevel() + "</td><td>" + recorder + "</td>" + "<td>" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()) + "</td></tr>");
					long oldExp = p.getExp();
					p.setExp(p.getNextLevelExpPool());
					sb.append("<tr><td>" + title + "</td><td>" + GameConstants.getInstance().getServerName() + "</td><td>" + p.getUsername() + "</td><td>" + playername + "</td><td>" + receiverId + "</td><td>" + ipaddress + "</td>" + "<td>exp</td>" + "<td>" + oldExp + "</td>" + "<td>" + p.getExp() + "</td><td>" + recorder + "</td>" + "<td>" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()) + "</td></tr>");
				}
				if (bi.getRMB() > 0) {
					int oldVipLevel = p.getVipLevel();
					p.setRMB(bi.getRMB());
					sb.append("<tr><td>" + title + "</td><td>" + GameConstants.getInstance().getServerName() + "</td><td>" + p.getUsername() + "</td><td>" + playername + "</td><td>" + receiverId + "</td><td>" + ipaddress + "</td>" + "<td>vipLevel</td>" + "<td>" + oldVipLevel + "</td>" + "<td>" + p.getVipLevel() + "</td><td>" + recorder + "</td>" + "<td>" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()) + "</td></tr>");
				}
				if (bi.getSilver() > 0) {
					long oldSilver = p.getSilver();
					p.setSilver(bi.getSilver());
					sb.append("<tr><td>" + title + "</td><td>" + GameConstants.getInstance().getServerName() + "</td><td>" + p.getUsername() + "</td><td>" + playername + "</td><td>" + receiverId + "</td><td>" + ipaddress + "</td>" + "<td>silver</td>" + "<td>" + oldSilver + "</td>" + "<td>" + p.getSilver() + "</td><td>" + recorder + "</td>" + "<td>" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()) + "</td></tr>");
				}
				if (bi.getDujieLevel() > 0) {
					TransitRobberyEntity entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(p.getId());
					if (entity != null) {
						int oldDujieLevel = entity.getCurrentLevel();
						entity.setCurrentLevel(bi.getDujieLevel());
						sb.append("<tr><td>" + title + "</td><td>" + GameConstants.getInstance().getServerName() + "</td><td>" + p.getUsername() + "</td><td>" + playername + "</td><td>" + receiverId + "</td><td>" + ipaddress + "</td>" + "<td>SoulLevel</td>" + "<td>" + oldDujieLevel + "</td>" + "<td>" + entity.getCurrentLevel() + "</td><td>" + recorder + "</td>" + "<td>" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()) + "</td></tr>");
					} else {
						out.print("[设置人物基本信息]角色名为<font color='red'>" + bi.getPlayerName() + "</font>的玩家获取渡劫entity为空,请后台单独设置<br>");
					}
				}
				Object[] param = new Object[] { Long.valueOf(p.getId()), bi.getDujieLevel() };
				EventRouter.getInst().addEvent(new EventWithObjParam(Event.PLAYER_DU_JIE, param));
			}
			out.print("设置玩家基本信息完成<br>");
			//数据统计
			if (!TestServerConfigManager.isTestServer()) {

				String[] titles = { "类型", "服务器", "账号", "角色名", "角色id", "IP地址", "修改的属性", "老值", "新值", "操作人", "操作时间" };
				StringBuffer buffer = new StringBuffer();
				buffer.append("<table style='font-size=12px; border=1'><tr bgcolor='green'>");
				for (String t : titles) {
					buffer.append("<th>");
					buffer.append(t);
					buffer.append("</th>");
				}
				buffer.append("</tr>");
				buffer.append(sb.toString());
				buffer.append("</table>");
				try {
					DataCheckManager.sendMail("[后台修改属性] [" + GameConstants.getInstance().getServerName() + "]", buffer.toString());
					if (ActivitySubSystem.logger.isInfoEnabled()) {
						ActivitySubSystem.logger.info("[GM后台发送邮件] [操作人:" + recorder + "] [合服设置角色属性]");
					}
				} catch (Exception e) {
					e.printStackTrace();
					ActivitySubSystem.logger.warn("[GM后台发送邮件] [异常] [操作人:" + recorder + "] [合服设置角色属性]", e);
				}
			}
		}
		{//发送物品
			if (userNameForSendArticleList.size() > 0 && afsList.size() > 0) {
				for (String name : userNameForSendArticleList) {
					boolean isadd = false;
					Player player = PlayerManager.getInstance().getPlayer(name);
					if (player == null) {
						out.print("[发送物品]角色名为<font color='red'>" + name + "</font>的玩家不存在<br>");
						return;
					}
					/*if (!ceshiUserNameList.contains(player.getUsername())) {
						out.print("[设置人物基本信息]角色名为<font color='red'>" + name + "</font>的账号不是测试账号，不予调整<br>");
						return;
					}*/
					List<String> articleNameList = new ArrayList<String>();
					List<String> articleCNNameList = new ArrayList<String>();
					List<Integer> countList = new ArrayList<Integer>();
					List<Integer> colorList = new ArrayList<Integer>();
					List<Integer> typeList = new ArrayList<Integer>();
					StringBuffer sbf = new StringBuffer();
					for (ArticleForSend afs : afsList) {
						Article a = ArticleManager.getInstance().getArticleByCNname(afs.getName());
						if (a != null) {
							if (a.isOverlap()) {
								ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.CREATE_REASON_后台, player, afs.getColor(), 1, true);
								if (ae != null) {
									boolean result = player.putToKnapsacks(ae, afs.getNum(), "合服后台发放");
									if (!result) {
										sbf.append("[" + afs.getName() + "]");
									} else {
										isadd = true;
										articleNameList.add(ae.getArticleName());
										articleCNNameList.add(afs.getName());
										countList.add(afs.getNum());
										colorList.add(afs.getColor());
										typeList.add(EventManager.getInstance().getArticleType(a));
									}
								} else {
									out.print("[发送物品]名字为<font color='red'>" + afs.getName() + "</font>的物品创建失败<br>");
								}
							} else {
								for (int i = 0; i < afs.getNum(); i++) {
									ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.CREATE_REASON_后台, player, afs.getColor(), 1, true);
									if (ae != null) {
										boolean result = player.putToKnapsacks(ae, 1, "合服后台发放");
										if (!result) {
											sbf.append("[" + afs.getName() + "]");
										} else {
											isadd = true;
											articleNameList.add(ae.getArticleName());
											articleCNNameList.add(afs.getName());
											countList.add(1);
											colorList.add(afs.getColor());
											typeList.add(EventManager.getInstance().getArticleType(a));
										}
									} else {
										out.print("[发送物品]名字为<font color='red'>" + afs.getName() + "</font>的物品创建失败<br>");
									}
								}
							}
						} else {
							out.print("[发送物品]名字为<font color='red'>" + afs.getName() + "</font>的物品不存在<br>");
						}
					}
					if (sbf.length() > 0) {
						out.print("[发送物品]名字为<font color='red'>" + sbf.toString() + "</font>的物品放入背包失败,请检查背包是否已满<br>");
					}
					if (isadd) {
						//数据统计
						String ip = request.getRemoteAddr();
						Object object = session.getAttribute("authorize.username");
						int count = 1;
						String recorder = (object != null ? object.toString() : "");
						RecordEvent articleRecordEvent = new ArticleRecordEvent(articleNameList.toArray(new String[0]), articleCNNameList.toArray(new String[0]), changeIntegerToInt(countList.toArray(new Integer[0])), changeIntegerToInt(colorList.toArray(new Integer[0])), changeIntegerToInt(typeList.toArray(new Integer[0])), player.getUsername(), player.getName(), player.getId(), ip, recorder);
						sendStatMail(articleRecordEvent);
					}
				}
			}
			out.print("发送物品完成<br>");
		}
		{//创建家族宗派
			if(userNameForJiazuInfo!=null&&!"".equals(userNameForJiazuInfo)){
				Player player = PlayerManager.getInstance().getPlayer(userNameForJiazuInfo);
				JiazuManager jiazuManager = JiazuManager.getInstance();
				if (player == null) {
					out.print("[设置人物基本信息]角色名为<font color='red'>" + userNameForJiazuInfo + "</font>的玩家不存在<br>");
					out.print("返回<br>");
					return;
				}
				/*if (!ceshiUserNameList.contains(player.getUsername())) {
					out.print("[设置人物基本信息]角色名为<font color='red'>" + userNameForJiazuInfo + "</font>的账号不是测试账号，不予调整<br>");
					out.print("返回<br>");
					return;
				}*/
				Jiazu jiazu = jiazuManager.createJiazu(player, jiazuName, "后台为合服设置");
				player.setJiazuName(jiazuName);
				player.initJiazuTitleAndIcon();
				// 更新任务
				//player.checkFunctionNPCModify(ModifyType.JIAZU_GOT);
				//player.checkFunctionNPCModify(ModifyType.JIAZU_TITLE_MODIFY);

				//jiazu.addContructionConsume(ExpenseReasonType.JIAZU_CREATE);
				jiazu.setCountry(player.getCountry());
				if (JiazuSubSystem.logger.isWarnEnabled()) {
					JiazuSubSystem.logger.warn(player.getLogString() + "[后台申请创建家族] [成功] [家族名字:{}]]", new Object[] { jiazuName });
				}
				//家族驻地
				try {
					long NPCId = 240001L;
					SeptStation station = SeptStationManager.getInstance().createDefaultSeptStation(jiazu.getJiazuID(), "", jiazuName, player.getCountry(), NPCId);
					station.setCountry(player.getCountry());
					station.setMapName(SeptStationManager.jiazuMapName + "_jz_" + jiazu.getJiazuID());
					station.setJiazu(jiazu);
					station.initInfo();
					jiazu.setJiazuPasswordHint("000");
					jiazu.setJiazuPassword("000");
					jiazu.setLevel(1);
					if (JiazuSubSystem.logger.isWarnEnabled()) {
						out.print(player.getLogString() + "[申请家族驻地] [成功] [密码:000]<br>");
					}
					station.initInfo();
					SeptStationInfo info = station.getInfo();
					if (jiazuBuilding.size() > 0) {
						for (String buildingName : jiazuBuilding.keySet()) {
							SeptBuildingEntity[] buildingEntities = station.getBuildingEntities();
							for (SeptBuildingEntity sbe : buildingEntities) {
								NPC npc = sbe.getNpc();
								if (npc.getName().equals(buildingName)) {
									sbe.getBuildingState().setLevel(jiazuBuilding.get(buildingName));
									sbe.modifyNPCOutShow();
									if (Translate.聚仙殿.equals(buildingName)) {
										jiazu.setLevel(sbe.getBuildingState().getLevel());
									}
									break;
								}
							}
							station.setBuildingEntities(buildingEntities);
							station.initInfo();

						}
					} else {
						out.print(player.getLogString() + "[申请家族驻地] [无需设置建筑等级]<br>");

					}
				} catch (NameExistException e) {
					out.print(Translate.text_jiazu_155 + "<br>");
				} catch (StationExistException e) {
					out.print(Translate.text_jiazu_156 + "<br>");
				} catch (SeptNotExistException e) {
					out.print(player.getJiazuLogString() + "[申请驻地异常]<br>");
				}
				long id = 0;
				try {
					id = ZongPaiManager.em.nextId();
				} catch (Exception e) {
					ZongPaiManager.logger.error("[宗派生成id错误] [" + player.getLogString() + "]", e);
					return;
				}
				ZongPai zp = new ZongPai(id);
				zp.setPassword("000");
				zp.setPassword2("000");
				zp.setPasswordHint("000");
				zp.setCreateTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
				zp.setLevel(0);
				zp.setZpname(zongpaiName);
				zp.setDeclaration("后台为合服设置");
				zp.getJiazuIds().add(player.getJiazuId());
				zp.setMasterId(player.getId());
				Jiazu jz = JiazuManager.getInstance().getJiazu(player.getJiazuId());
				if (jz != null) {
					jz.setZongPaiId(zp.getId());
					ZongPaiManager.getInstance().createZongPai(player, zp);
					ZongPaiManager.getInstance().setAllZongPaiName(jz, zongpaiName);
					Country country = CountryManager.getInstance().getCountryMap().get(player.getCountry());
					if (ZongPaiSubSystem.logger.isWarnEnabled()) {
						ZongPaiSubSystem.logger.warn("[后台创建宗派成功] [" + player.getLogString() + "] [" + zp.getLogString() + "]");
					}
					//设置家族资源
					jiazu.setJiazuMoney(jiazuInfo.getJiazuMoney());
					jiazu.setConstructionConsume(jiazuInfo.getJiazuZiYuan());
					JiazuMember2 jm2 = JiazuEntityManager2.instance.getEntity(player.getId());
					jm2.setPracticeValue(jiazuInfo.getPracticeValue());
					//out.print(Translate.translateString(Translate.创建xx宗派成功, new String[][] { { Translate.STRING_1, zp.getZpname() } }) + "<br>");
					out.print("创建宗派成功<br>");
				}
				out.print("设置家族宗派信息完成<br>");
			}
			
		}
		{//创建战队
			/*for (ZhanDui zd : zhanDuiList) {
				Player player = PlayerManager.getInstance().getPlayer(zd.getPName());
				if (null != player) {
					BattleTeam bt = new BattleTeam();
					int teamType = 1;
					try {
						bt.setBattleType(teamType);
						bt.setPassword(zd.getPwd());
						bt.setTeamIcon("ui/houseLogo/zhandui_1.png");//固定给选一个默认的
						bt.setTeamName(zd.getTeamName());
						bt.setTeamLevel(JJCManager.NEW_TEAM_LEVEL);
						bt.setTeamCreatDate(System.currentTimeMillis());
						long members[] = new long[JJCManager.MAX_TEAM_PLAYERS[teamType]];
						members[0] = player.getId();
						bt.setMemberIds(members);
						player.setTeamMark(Player.TEAM_MARK_CAPTAIN); 
						JJCManager.getInstance().saveTeamInfo(bt);
						out.print("[JJC-战队管理] [后台创建战队] [成功] [战队名:" + zd.getTeamName() + "] [2v2;3v3;5v5;规模:" + teamType + "] [玩家:" + player.getLogString() + "]<br>");
						if (JJCManager.logger.isInfoEnabled()) {
							JJCManager.logger.info("[JJC-战队管理] [后台创建战队] [成功] [战队名:{}] [2v2;3v3;5v5;规模:{}] [玩家:{}]", new Object[] { zd.getTeamName(), teamType, player.getLogString() });
						}
					} catch (Exception e) {
						e.printStackTrace();
						JJCManager.logger.info("[JJC-战队管理] [后台创建战队] [异常] [战队名:{}] [2v2;3v3;5v5;规模:{}] [玩家:{}]", new Object[] { zd.getTeamName(), teamType, player.getLogString() });
						return;
					}
					try {
						if (!JJCManager.getInstance().isHaveTeamOfBattleType(player, teamType)) {
							BattleTeamMember btm = JJCManager.getInstance().getPlayerInfo(player.getId(), teamType);
							String info = "";
							if (btm != null) {
								info = "更新成员信息";
								if (btm.getBattleLevel() < JJCManager.NEW_TEAM_LEVEL) {
									btm.setBattleLevel(JJCManager.NEW_TEAM_LEVEL);
								}
								btm.getTeamIds()[teamType] = bt.getId();
								btm.setTeamIds(btm.getTeamIds());
							} else {
								info = "增加成员信息";
								btm = new BattleTeamMember();
								btm.setPid(player.getId());
								btm.setBattleType(teamType);
								btm.getTeamIds()[teamType] = bt.getId();
								btm.setTeamIds(btm.getTeamIds());
								btm.setBattleLevel(JJCManager.NEW_TEAM_LEVEL);
							}
							JJCManager.getInstance().saveMemberInfo(btm);
							if (JJCManager.logger.isInfoEnabled()) {
								JJCManager.logger.info("[JJC-战队管理] [创建战队成员信息] [{}] [成功] [战队名:{}] [战队id:{}] [2v2;3v3;5v5;规模:{}] [玩家:{}]", new Object[] { info, zd.getTeamName(), bt.getId(), teamType, player.getLogString() });
							}
							try {
								AchievementManager.getInstance().record(player, RecordAction.创建战队次数);
							} catch (Exception e) {
								PlayerAimManager.logger.error("[目标系统] [统计玩家创建战队次数] [异常] [" + player.getLogString() + "]", e);
							}
						} else {
							player.sendError(Translate.服务器出现错误);
							return;
						}
					} catch (Exception e) {
						e.printStackTrace();
						JJCManager.logger.info("[JJC-战队管理] [创建战队成员信息] [异常] [战队名:{}] [战队id:{}] [2v2;3v3;5v5;规模:{}] [玩家:{}]", new Object[] { zd.getTeamName(), bt.getId(), teamType, player.getLogString() });
						return;
					}
				} else {
					out.print("要创建战队的玩家" + zd.getPName() + "不存在");
				}
			}*/
		}
	}
	out.print("***********************************************************************");
	out.print("当前服务器："+GameConstants.getInstance().getServerName()+"<br>");
	out.print("有<font color='blue' size='5'>" + baseInfoList.size() + "</font>个角色要设置基本信息<br>");
	out.print("有<font color='blue' size='5'>" + userNameForSendArticleList.size() + "</font>个角色要发道具<br>");
	out.print("道具个数：<font color='blue' size='5'>" + afsList.size() + "</font><br>");
	out.print("要设置等级的家族驻地建筑个数：<font color='blue' size='5'>" + jiazuBuilding.size() + "</font><br>");
	out.print("要设置家族信息的角色名：<font color='blue' size='5'>" + userNameForJiazuInfo + "</font><br>");
	//out.print("有<font color='blue' size='5'>" + zhanDuiList.size() + "</font>个角色要创建战队<br>");
%>
<%!public List<BaseInfo> baseInfoList;//存放要设置基本信息的角色名及信息
	public List<String> userNameForSendArticleList;//存放要发道具的角色名
	public List<ArticleForSend> afsList;//存放要发的道具
	public String userNameForJiazuInfo;//存放要设置家族信息的角色名
	public JiaZuInfo jiazuInfo;//存放要设置的家族信息
	public Map<String, Integer> jiazuBuilding;//存放要设置的家族驻地信息
	public List<ZhanDui> zhanDuiList;

	
	//初始化要设置的数据
	public void load() throws Exception {
		baseInfoList = new ArrayList<BaseInfo>();
		userNameForSendArticleList = new ArrayList<String>();
		afsList = new ArrayList<ArticleForSend>();
		userNameForJiazuInfo = "";
		jiazuInfo = new JiaZuInfo();
		jiazuBuilding = new HashMap<String, Integer>();
		zhanDuiList = new ArrayList<ZhanDui>();

		//个人基本信息
		BaseInfo bi0 = new BaseInfo("aaa", 135, 9999999999l, 16000, 20000000, 1);
		//baseInfoList.add(bi0);
		BaseInfo bi1 = new BaseInfo("hanshuo1", 135, 9999999999l, 16000, 20000000, 1);
		BaseInfo bi2 = new BaseInfo("hanshuo2", 135, 0, 0, 0, 0);
		BaseInfo bi3 = new BaseInfo("hanshuo3", 135, 0, 0, 0, 0);
		BaseInfo bi4 = new BaseInfo("panju1", 135, 9999999999l, 16000, 20000000, 1);
		BaseInfo bi5 = new BaseInfo("panju2", 135, 0, 0, 0, 0);
		BaseInfo bi6 = new BaseInfo("panju3", 135, 0, 0, 0, 0);
		baseInfoList.add(bi1);
		baseInfoList.add(bi2);
		baseInfoList.add(bi3);
		baseInfoList.add(bi4);
		baseInfoList.add(bi5);
		baseInfoList.add(bi6);
		
		//要发道具的角色
		userNameForSendArticleList.add("hanshuo1");
		userNameForSendArticleList.add("panju1");
		//userNameForSendArticleList.add("aaa");

		//要发的道具
		ArticleForSend afs1 = new ArticleForSend("摇光噬灵之镰", 5, 1);
		ArticleForSend afs2 = new ArticleForSend("摇光兽血骨盔", 5, 1);
		ArticleForSend afs3 = new ArticleForSend("摇光兽血皮铠", 5, 1);
		ArticleForSend afs4 = new ArticleForSend("摇光兽血皮肩", 5, 1);
		ArticleForSend afs5 = new ArticleForSend("摇光兽血骨腕", 5, 1);
		ArticleForSend afs6 = new ArticleForSend("摇光兽血长靴", 5, 1);
		ArticleForSend afs7 = new ArticleForSend("摇光兽血腰带", 5, 1);
		ArticleForSend afs8 = new ArticleForSend("摇光兽血晶石", 5, 1);
		ArticleForSend afs9 = new ArticleForSend("摇光兽血兽魂", 5, 1);
		ArticleForSend afs10 = new ArticleForSend("摇光兽血骨勋", 5, 1);
		ArticleForSend afs11 = new ArticleForSend("羽炼符", 4, 10);
		ArticleForSend afs12 = new ArticleForSend("八卦镜", 1, 1);
		ArticleForSend afs13 = new ArticleForSend("明玉(橙)", 4, 10);
		ArticleForSend afs14 = new ArticleForSend("宝石湛天(5级)", 2, 1);
		ArticleForSend afs15 = new ArticleForSend("攻击神匣", 3, 2);
		ArticleForSend afs16 = new ArticleForSend("独角灵兽", 0, 1);
		ArticleForSend afs17 = new ArticleForSend("降魔兽盔", 5, 1);
		ArticleForSend afs18 = new ArticleForSend("降魔兽翅", 5, 1);
		ArticleForSend afs19 = new ArticleForSend("降魔兽佩", 5, 1);
		ArticleForSend afs20 = new ArticleForSend("降魔兽鞍", 5, 1);
		ArticleForSend afs21 = new ArticleForSend("降魔兽钿", 5, 1);
		ArticleForSend afs22 = new ArticleForSend("千年参妖蛋", 4, 1);
		ArticleForSend afs23 = new ArticleForSend("精灵之翼", 0, 1);
		ArticleForSend afs24 = new ArticleForSend("凤凰之翼", 0, 1);
		ArticleForSend afs25 = new ArticleForSend("兽血沸腾秘籍(兽魁)", 0, 1);
		ArticleForSend afs26 = new ArticleForSend("附魔武器——初级洪流（绿）", 1, 1);
		afsList.add(afs1);
		afsList.add(afs2);
		afsList.add(afs3);
		afsList.add(afs4);
		afsList.add(afs5);
		afsList.add(afs6);
		afsList.add(afs7);
		afsList.add(afs8);
		afsList.add(afs9);
		afsList.add(afs10);
		afsList.add(afs11);
		afsList.add(afs12);
		afsList.add(afs13);
		afsList.add(afs14);
		afsList.add(afs15);
		afsList.add(afs16);
		afsList.add(afs17);
		afsList.add(afs18);
		afsList.add(afs19);
		afsList.add(afs20);
		afsList.add(afs21);
		afsList.add(afs22);
		afsList.add(afs23);
		afsList.add(afs24);
		afsList.add(afs25);
		afsList.add(afs26);

		//家族信息
		userNameForJiazuInfo = "hanshuo1";
		//userNameForJiazuInfo = "aaa";
		
		jiazuBuilding.put("聚仙殿", 7);
		jiazuBuilding.put("武器坊", 7);
		jiazuBuilding.put("防具坊", 7);
		jiazuBuilding.put("龙图阁", 7);
		jiazuInfo = new JiaZuInfo("panju1", "panju1", jiazuBuilding, 20000000, 20000000, 5000);

		//要创建的战队信息
		ZhanDui zhanDui1 = new ZhanDui("hanshuo1", "hhh", "123456");
		ZhanDui zhanDui2 = new ZhanDui("panju1", "jjj", "123456");
		zhanDuiList.add(zhanDui1);
		zhanDuiList.add(zhanDui2);
	}

	class BaseInfo {
		private String playerName;//角色名
		private int playerLevel;
		private long exp;
		private long RMB;
		private long silver;
		private int dujieLevel;

		public BaseInfo() {
		}

		public BaseInfo(String playerName, int playerLevel, long exp, long RMB, long silver, int dujieLevel) {
			this.playerName = playerName;
			this.playerLevel = playerLevel;
			this.exp = exp;
			this.RMB = RMB;
			this.silver = silver;
			this.dujieLevel = dujieLevel;
		}

		public String getPlayerName() {
			return playerName;
		}

		public int getPlayerLevel() {
			return playerLevel;
		}

		public long getExp() {
			return exp;
		}

		public long getRMB() {
			return RMB;
		}

		public long getSilver() {
			return silver;
		}

		public int getDujieLevel() {
			return dujieLevel;
		}
	}

	class ArticleForSend {
		private String name;
		private int color;
		private int num;

		public ArticleForSend() {
		}

		public ArticleForSend(String name, int color, int num) {
			this.name = name;
			this.color = color;
			this.num = num;
		}

		public String getName() {
			return name;
		}

		public int getColor() {
			return color;
		}

		public int getNum() {
			return num;
		}
	}

	class JiaZuInfo {
		private String jiazuName;
		private String zongpaiName;
		private Map<String, Integer> jiazuBuilding;
		private long jiazuMoney;
		private int jiazuZiYuan;
		private long practiceValue;

		public JiaZuInfo() {
		}

		public JiaZuInfo(String jiazuName, String zongpaiName, Map<String, Integer> jiazuBuilding, long jiazuMoney, int jiazuZiYuan, long practiceValue) {
			this.jiazuName = jiazuName;
			this.zongpaiName = zongpaiName;
			this.jiazuBuilding = jiazuBuilding;
			this.jiazuMoney = jiazuMoney;
			this.jiazuZiYuan = jiazuZiYuan;
			this.practiceValue = practiceValue;
		}

		public String getJiazuName() {
			return jiazuName;
		}

		public void setJiazuName(String jiazuName) {
			this.jiazuName = jiazuName;
		}

		public long getJiazuMoney() {
			return jiazuMoney;
		}

		public void setJiazuMoney(long jiazuMoney) {
			this.jiazuMoney = jiazuMoney;
		}

		public int getJiazuZiYuan() {
			return jiazuZiYuan;
		}

		public void setJiazuZiYuan(int jiazuZiYuan) {
			this.jiazuZiYuan = jiazuZiYuan;
		}

		public String getZongpaiName() {
			return zongpaiName;
		}

		public void setZongpaiName(String zongpaiName) {
			this.zongpaiName = zongpaiName;
		}

		public long getPracticeValue() {
			return practiceValue;
		}

		public String toString() {
			return "jiazuName:" + jiazuName + ",jiazuMoney:" + jiazuMoney + ",jiazuZiYuan:" + jiazuZiYuan + ",zongpaiName:" + zongpaiName + ",practiceValue:" + practiceValue;
		}
	}

	class ZhanDui {
		private String pName;
		private String teamName;
		private String pwd;

		public ZhanDui(String pName, String teamName, String pwd) {
			this.pName = pName;
			this.teamName = teamName;
			this.pwd = pwd;
		}

		public String getPName() {
			return pName;
		}

		public String getTeamName() {
			return teamName;
		}

		public void setTeamName(String teamName) {
			this.teamName = teamName;
		}

		public String getPwd() {
			return pwd;
		}
	}%>
</body>
</html>