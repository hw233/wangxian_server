<%@page import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page import="com.fy.engineserver.mail.Mail"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page import="com.fy.engineserver.seal.SealManager"%>
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
<%@page import="com.fy.engineserver.activity.ActivitySubSystem"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="com.fy.engineserver.activity.shop.ActivityProp"%>
<%@page import="com.fy.engineserver.activity.ActivityManager"%><html>
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
	<form action="">
		非测试服请输入密码：<input type="password" value="" name="password" /> <input
			type="submit" value="建数据" />
	</form>
	<%
		String password = request.getParameter("password");
		if (TestServerConfigManager.isTestServer()) {
			password = "shezhiHF";
		} else {
			if (password == null || password.equals("")) {
				out.print("非测试服请输入密码!");
				return;
			}
		}
		if (!password.equals("shezhiHF")) {
			out.print("密码不正确，请重新输入密码<br>");
			return;
		}

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
		ceshiUserNameList.add("hefuceshi4");
		ceshiUserNameList.add("hefuceshi5");
		ceshiUserNameList.add("hefuceshi6");
		ceshiUserNameList.add("ceshihefu4");
		ceshiUserNameList.add("ceshihefu5");
		ceshiUserNameList.add("ceshihefu6");
		ceshiUserNameList.add("hanshuo1");
		ceshiUserNameList.add("hanshuo2");
		ceshiUserNameList.add("hanshuo3");
		ceshiUserNameList.add("hanshuo4");
		ceshiUserNameList.add("hanshuo5");
		ceshiUserNameList.add("hanshuo6");
		ceshiUserNameList.add("hanshuo7");
		ceshiUserNameList.add("panju1");
		ceshiUserNameList.add("panju2");
		ceshiUserNameList.add("panju3");
		ceshiUserNameList.add("panju4");
		ceshiUserNameList.add("panju5");
		ceshiUserNameList.add("panju6");
		ceshiUserNameList.add("panju7");

		//String fileName = request.getRealPath("/") + "WEB-INF/game_init_config/forUnitServer.xls";
		String fileName = "";
		int fengyinLevel = SealManager.getInstance().getSealLevel();
		if (fengyinLevel >= 220) {
			fileName = request.getRealPath("/admin/ceshiManager/forUnitServer" + 210 + ".xls");
		} else if (fengyinLevel >= 190) {
			fileName = request.getRealPath("/admin/ceshiManager/forUnitServer" + 190 + ".xls");
		} else {
			fileName = request.getRealPath("/admin/ceshiManager/forUnitServer" + 150 + ".xls");
		}
		String jiazuNameInput = request.getParameter("jiazuNameInput");
		String zongpaiNameInput = request.getParameter("zongpaiNameInput"); 
		load(fileName, out);

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
	<form action="">
		家族名：<input type="text" name="jiazuNameInput" value="" /> 宗派名：<input
			type="text" name="zongpaiNameInput" value="" /><input type="hidden"
			name="password" value="<%=password == null ? "" : password%>" /> <input
			type="submit" value="提交">
	</form>
	<%
		} else if (!jiazuNameSingle) {
			out.print("<font color='red' size='5'>家族名[" + jiazuInfo.getJiazuName() + "]</font>重复，请输入新名字：<br>");
	%>
	<form action="">
		家族名：<input type="text" name="jiazuNameInput" value="" /><input
			type="hidden" name="zongpaiNameInput"
			value="<%=zongpaiNameInput == null ? "" : zongpaiNameInput%>" /><input
			type="hidden" name="password"
			value="<%=password == null ? "" : password%>" /> <input
			type="submit" value="提交">
	</form>
	<%
		} else if (!zongpaiNameSingle) {
			out.print("<font color='red' size='5'>宗派名[" + jiazuInfo.getZongpaiName() + "]</font>重复，请输入新名字：<br>");
	%>
	<form action="">
		<input type="hidden" name="jiazuNameInput"
			value="<%=jiazuNameInput == null ? "" : jiazuNameInput%>" />宗派名：<input
			type="text" name="zongpaiNameInput" value="" /><input type="hidden"
			name="password" value="<%=password == null ? "" : password%>" /> <input
			type="submit" value="提交">
	</form>
	<%
		} else {
			//设置人物基本信息
			if (baseInfoList.size() > 0) {
				Object object = session.getAttribute("authorize.username");
				int count = 1;
				String recorder = (object != null ? object.toString() : "");
				StringBuffer sb = new StringBuffer();
				for (BaseInfo bi : baseInfoList) {
					Player p = null;
					try {
						p = PlayerManager.getInstance().getPlayer(bi.getPlayerName());
					} catch (Exception e) {
						out.print("[设置人物基本信息]获取角色名为<font color='red'>" + bi.getPlayerName() + "</font>的玩家出错<br>");
						out.print(e);
						continue;
					}
					if (p == null) {
						out.print("[设置人物基本信息]角色名为<font color='red'>" + bi.getPlayerName() + "</font>的玩家不存在<br>");
						out.print("返回<br>");
						return;
					}
					/*if(!ceshiUserNameList.contains(p.getUsername())){
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
						int setLevel = bi.getPlayerLevel();
						if( setLevel>fengyinLevel){
							p.setSoulLevel(fengyinLevel-1);
						}else{
							p.setSoulLevel(bi.getPlayerLevel());
						}
						sb.append("<tr><td>" + title + "</td><td>" + GameConstants.getInstance().getServerName() + "</td><td>" + p.getUsername() + "</td><td>" + playername + "</td><td>" + receiverId + "</td><td>" + ipaddress + "</td>" + "<td>soulLevel</td>" + "<td>" + oldSoulLevel + "</td>" + "<td>" + p.getSoulLevel() + "</td><td>" + recorder + "</td>" + "<td>" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()) + "</td></tr>");

						p.initNextLevelExpPool();
						int oldClassLevel = p.getClassLevel();
						p.setClassLevel((short) (p.getLevel() / 20));
						sb.append("<tr><td>" + title + "</td><td>" + GameConstants.getInstance().getServerName() + "</td><td>" + p.getUsername() + "</td><td>" + playername + "</td><td>" + receiverId + "</td><td>" + ipaddress + "</td>" + "<td>classLevel</td>" + "<td>" + oldClassLevel + "</td>" + "<td>" + p.getClassLevel() + "</td><td>" + recorder + "</td>" + "<td>" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()) + "</td></tr>");
						long oldExp = p.getExp();
						p.setExp(p.getNextLevelExpPool());
						sb.append("<tr><td>" + title + "</td><td>" + GameConstants.getInstance().getServerName() + "</td><td>" + p.getUsername() + "</td><td>" + playername + "</td><td>" + receiverId + "</td><td>" + ipaddress + "</td>" + "<td>exp</td>" + "<td>" + oldExp + "</td>" + "<td>" + p.getExp() + "</td><td>" + recorder + "</td>" + "<td>" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()) + "</td></tr>");
					}
					if (bi.getOtherSoulLevel() > 0 && p.getSoul(Soul.SOUL_TYPE_SOUL) != null) {
						int oldLevel = p.getSoul(Soul.SOUL_TYPE_SOUL).getGrade();
						p.getSoul(Soul.SOUL_TYPE_SOUL).setGrade(bi.getOtherSoulLevel());
						sb.append("<tr><td>" + title + "</td><td>" + GameConstants.getInstance().getServerName() + "</td><td>" + p.getUsername() + "</td><td>" + playername + "</td><td>" + receiverId + "</td><td>" + ipaddress + "</td>" + "<td>元神等级</td>" + "<td>" + oldLevel + "</td>" + "<td>" + p.getSoul(Soul.SOUL_TYPE_SOUL).getGrade() + "</td><td>" + recorder + "</td>" + "<td>" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()) + "</td></tr>");
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
					if (bi.getHonorPoint() > 0) {
						long oldWeekpoint = p.getWeekPoint();
						p.setHonorPoint(bi.getHonorPoint());
						sb.append("<tr><td>" + title + "</td><td>" + GameConstants.getInstance().getServerName() + "</td><td>" + p.getUsername() + "</td><td>" + playername + "</td><td>" + receiverId + "</td><td>" + ipaddress + "</td>" + "<td>silver</td>" + "<td>" + oldWeekpoint + "</td>" + "<td>" + p.getWeekPoint() + "</td><td>" + recorder + "</td>" + "<td>" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()) + "</td></tr>");
					}
					if (bi.getJjcPoint() > 0) {
						long oldJjcPoint = p.getJjcPoint();
						p.setJjcPoint(bi.getJjcPoint());
						sb.append("<tr><td>" + title + "</td><td>" + GameConstants.getInstance().getServerName() + "</td><td>" + p.getUsername() + "</td><td>" + playername + "</td><td>" + receiverId + "</td><td>" + ipaddress + "</td>" + "<td>silver</td>" + "<td>" + oldJjcPoint + "</td>" + "<td>" + p.getJjcPoint() + "</td><td>" + recorder + "</td>" + "<td>" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()) + "</td></tr>");
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
				Set<String> needChangeStar = new HashSet<String>();
				needChangeStar.add("赤霄兽盔");
				needChangeStar.add("赤霄兽翅");
				needChangeStar.add("赤霄兽佩");
				if (userNameForSendArticleList.size() > 0 && afsList.size() > 0) {
					for (String name : userNameForSendArticleList) {
						boolean isadd = false;
						Player player = null;
						try {
							player = PlayerManager.getInstance().getPlayer(name);
						} catch (Exception e) {
							out.print("[发送物品]获取角色名为<font color='red'>" + name + "</font>的玩家出错<br>");
							out.print(e);
							continue;
						}
						if (player == null) {
							out.print("[发送物品]角色名为<font color='red'>" + name + "</font>的玩家不存在<br>");
							return;
						}
						if (!ceshiUserNameList.contains(player.getUsername())) {
							out.print("[发送物品]角色名为<font color='red'>" + name + "</font>的账号不是测试账号，不予调整<br>");
							return;
						}
						List<String> articleNameList = new ArrayList<String>();
						List<String> articleCNNameList = new ArrayList<String>();
						List<Integer> countList = new ArrayList<Integer>();
						List<Integer> colorList = new ArrayList<Integer>();
						List<Integer> typeList = new ArrayList<Integer>();
						StringBuffer sbf = new StringBuffer();
						//ActivityProp[] activityProps = new ActivityProp[afsList.size()-needChangeStar.size()];
						List<ActivityProp> activityProps = new ArrayList<ActivityProp>();
						for (int i = 0; i < afsList.size(); i++) {
							if(needChangeStar.contains(afsList.get(i).getName())){
								Article a = ArticleManager.getInstance().getArticle(afsList.get(i).getName());
								if(a!=null){
									ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a,true , ArticleEntityManager.CREATE_REASON_huodong_libao, player, afsList.get(i).getColor(), afsList.get(i).getNum(), true);
									if(ae!=null){
										((EquipmentEntity)ae).setStar(20);
										MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{ae}, new int[]{1}, "合服后台发放", "合服后台发放", 0L, 0L, 0L, "合服后台发放");
									}
									
								}
							}else{
								ActivityProp ap = new ActivityProp(afsList.get(i).getName(), afsList.get(i).getColor(), afsList.get(i).getNum(), true);
								//activityProps[i] = ap;
								activityProps.add(ap);
							}
						}
						List<Player> pList = new ArrayList<Player>();
						pList.add(player);
						//ActivityManager.sendMailForActivity(pList, activityProps, "合服后台发放", "合服后台发放", "合服后台发放");
						ActivityManager.sendMailForActivity(pList, activityProps.toArray(new ActivityProp[0]), "合服后台发放", "合服后台发放", "合服后台发放");
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
				JiazuManager jiazuManager = JiazuManager.getInstance();
				Player player = null;
				try {
					player = PlayerManager.getInstance().getPlayer(userNameForJiazuInfo);
				} catch (Exception e) {
					out.print("[设置人物基本信息]获取角色名为<font color='red'>" + userNameForJiazuInfo + "</font>的玩家出错<br>");
					out.print(e);
				}
				if (player == null) {
					out.print("[设置人物基本信息]角色名为<font color='red'>" + userNameForJiazuInfo + "</font>的玩家不存在<br>");
					out.print("返回<br>");
					return;
				}
				if (!ceshiUserNameList.contains(player.getUsername())) {
					out.print("[设置人物基本信息]角色名为<font color='red'>" + userNameForJiazuInfo + "</font>的账号不是测试账号，不予调整<br>");
					out.print("返回<br>");
					return;
				}
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
			{//创建战队
				for (ZhanDui zd : zhanDuiList) {
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
				}
			}
		}
		out.print("有<font color='blue' size='5'>" + baseInfoList.size() + "</font>个角色要设置基本信息<br>");
		out.print("有<font color='blue' size='5'>" + userNameForSendArticleList.size() + "</font>个角色要发道具<br>");
		out.print("道具个数：<font color='blue' size='5'>" + afsList.size() + "</font><br>");
		out.print("要设置等级的家族驻地建筑个数：<font color='blue' size='5'>" + jiazuBuilding.size() + "</font><br>");
		out.print("要设置家族信息的角色名：<font color='blue' size='5'>" + userNameForJiazuInfo + "</font><br>");
		out.print("有<font color='blue' size='5'>" + zhanDuiList.size() + "</font>个角色要创建战队<br>");
	%>
	<%!public List<BaseInfo> baseInfoList;//存放要设置基本信息的角色名及信息
	public List<String> userNameForSendArticleList;//存放要发道具的角色名
	public List<ArticleForSend> afsList;//存放要发的道具
	public String userNameForJiazuInfo;//存放要设置家族信息的角色名
	public JiaZuInfo jiazuInfo;//存放要设置的家族信息
	public Map<String, Integer> jiazuBuilding;//存放要设置的家族驻地信息
	public List<ZhanDui> zhanDuiList;

	public void load(String fileName, JspWriter out) throws Exception {
		baseInfoList = new ArrayList<BaseInfo>();
		baseInfoList.clear();
		userNameForSendArticleList = new ArrayList<String>();
		userNameForSendArticleList.clear();
		afsList = new ArrayList<ArticleForSend>();
		afsList.clear();
		userNameForJiazuInfo = "";
		jiazuInfo = new JiaZuInfo();
		jiazuBuilding = new HashMap<String, Integer>();
		zhanDuiList = new ArrayList<ZhanDui>();

		File file = new File(fileName);
		HSSFWorkbook hssfWorkbook = null;
		InputStream is = new FileInputStream(file);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		hssfWorkbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = null;
		//sheet0
		sheet = hssfWorkbook.getSheetAt(0);
		if (sheet == null) return;
		int rows = sheet.getPhysicalNumberOfRows();
		//out.print("sheet0行数：" + rows + "<br>");
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row != null) {
				String userName = null;
				int playerLevel = 1;
				int otherSoulLevel = 0;
				long exp = 0;
				int vipLevel = 0;
				long silver = 0;
				int dujieLevel = 0;
				int honorPoint = 0;
				int jjcPoint = 0;
				int index = 0;
				HSSFCell cell = row.getCell(index++);
				if (cell == null) {
					break;
				}
				userName = cell.getStringCellValue();
				playerLevel = (int) row.getCell(index++).getNumericCellValue();
				//otherSoulLevel = (int) row.getCell(index++).getNumericCellValue();
				cell = row.getCell(index++);
				if (cell != null) {
					exp = Long.valueOf(cell.getStringCellValue());
					//out.print("exp" + exp + "<br>");
				}
				cell = row.getCell(index++);
				if (cell != null) {
					vipLevel = (int) cell.getNumericCellValue();
					//out.print("vipLevel" + vipLevel + "<br>");
				}
				cell = row.getCell(index++);
				if (cell != null) {
					silver = (long) cell.getNumericCellValue();
					//out.print("silver" + silver + "<br>");
				}
				cell = row.getCell(index++);
				if (cell != null) {
					dujieLevel = (int) cell.getNumericCellValue();
					//out.print("dujieLevel" + dujieLevel + "<br>");
				}
				cell = row.getCell(index++);
				if (cell != null) {
					honorPoint = (int) cell.getNumericCellValue();
					//out.print("dujieLevel" + dujieLevel + "<br>");
				}
				cell = row.getCell(index++);
				if (cell != null) {
					jjcPoint = (int) cell.getNumericCellValue();
					//out.print("dujieLevel" + dujieLevel + "<br>");
				}
				BaseInfo bi = new BaseInfo(userName, playerLevel, exp, vipLevel, silver, dujieLevel, honorPoint,jjcPoint);
				baseInfoList.add(bi);
			}
		}
		//out.print("sheet0加载完成<br>");
		/*sheet1道具*/
		sheet = hssfWorkbook.getSheetAt(1);
		if (sheet == null) return;
		rows = sheet.getPhysicalNumberOfRows();
		//out.print("sheet1行数：" + rows + "<br>");
		String name;
		int color;
		int num;
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row != null) {
				int index = 0;
				HSSFCell cell = row.getCell(index++);
				if (cell != null) {
					String userName = cell.getStringCellValue();
					userNameForSendArticleList.add(userName);
				}
				cell = row.getCell(index++);
				if (cell == null) {
					break;
				}
				name = cell.getStringCellValue();
				color = (int) row.getCell(index++).getNumericCellValue();
				num = (int) row.getCell(index++).getNumericCellValue();
				ArticleForSend afs = new ArticleForSend(name, color, num);
				afsList.add(afs);
			}
		}
		//out.print("sheet1加载完成<br>");
		/*sheet2家族信息*/
		sheet = hssfWorkbook.getSheetAt(2);
		if (sheet == null) return;
		rows = sheet.getPhysicalNumberOfRows();
		//out.print("sheet2行数：" + rows + "<br>");
		String jiazuName = null;
		String zongpaiName = null;
		long jiazuMoney = 0;
		int jiazuZiYuan = 0;
		long practiceValue = 0;
		HSSFRow row = sheet.getRow(1);
		int index = 0;
		HSSFCell cell = row.getCell(index++);
		if (cell != null) {
			String userName = cell.getStringCellValue();
			userNameForJiazuInfo = userName;
		}
		cell = row.getCell(index++);
		if (cell != null) {
			jiazuName = cell.getStringCellValue();
			//out.print("家族名字" + jiazuName + "<br>");
		}
		index++;
		index++;
		cell = row.getCell(index++);
		if (cell != null) {
			jiazuMoney = (long) cell.getNumericCellValue();
		}
		cell = row.getCell(index++);
		if (cell != null) {
			jiazuZiYuan = (int) cell.getNumericCellValue();
		}
		cell = row.getCell(index++);
		if (cell != null) {
			zongpaiName = cell.getStringCellValue();
		}
		cell = row.getCell(index++);
		if (cell != null) {
			practiceValue = (long) cell.getNumericCellValue();
		}
		for (int i = 1; i < rows; i++) {
			row = sheet.getRow(i);
			if (row != null) {
				index = 2;
				cell = row.getCell(index);
				if (cell != null && row.getCell(index++) != null) {
					String buildingName = cell.getStringCellValue();
					int buildingLevel = (int) row.getCell(index).getNumericCellValue();
					jiazuBuilding.put(buildingName, buildingLevel);
					//out.print("家族建筑" + buildingName + "等级" + buildingLevel + "<br>");
				}
				if (jiazuName != null) {
					jiazuInfo = new JiaZuInfo(jiazuName, zongpaiName, jiazuBuilding, jiazuMoney, jiazuZiYuan, practiceValue);
				}
			}
		}
		//out.print("sheet2加载完成<br>");
		//sheet3
		sheet = hssfWorkbook.getSheetAt(3);
		if (sheet == null) return;
		rows = sheet.getPhysicalNumberOfRows();
		out.print("sheet3行数：" + rows + "<br>");
		for (int i = 1; i < rows; i++) {
			row = sheet.getRow(i);
			if (row != null) {
				index = 0;
				cell = row.getCell(index++);
				if (cell == null) {
					break;
				}
				String pName = cell.getStringCellValue();
				String teamName = row.getCell(index++).getStringCellValue();
				String pwd = row.getCell(index++).getStringCellValue();
				ZhanDui zhanDui = new ZhanDui(pName, teamName, pwd);
				zhanDuiList.add(zhanDui);
			}
		}
		out.print("sheet3加载完成<br>");
	}

	class BaseInfo {
		private String playerName;//角色名
		private int playerLevel;
		private int otherSoulLevel;
		private long exp;
		private long RMB;
		private long silver;
		private int dujieLevel;
		private int honorPoint;
		private int jjcPoint;

		public BaseInfo() {
		}

		public BaseInfo(String playerName, int playerLevel, long exp, long RMB, long silver, int dujieLevel, int honorPoint,int jjcPoint) {
			this.playerName = playerName;
			this.playerLevel = playerLevel;
			this.exp = exp;
			this.RMB = RMB;
			this.silver = silver;
			this.dujieLevel = dujieLevel;
			this.honorPoint = honorPoint;
			this.jjcPoint = jjcPoint;
		}

		public String getPlayerName() {
			return playerName;
		}

		public int getPlayerLevel() {
			return playerLevel;
		}

		public int getOtherSoulLevel() {
			return otherSoulLevel;
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
		
		public int getHonorPoint(){
			return honorPoint;
		}
		public int getJjcPoint(){
			return jjcPoint;
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