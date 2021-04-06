<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.zongzu.manager.ZongPaiManager"%>
<%@page import="com.fy.engineserver.zongzu.data.ZongPai"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.economic.SavingReasonType"%>
<%@page import="com.xuanzhi.tools.mail.JavaMailUtils"%>
<%@page import="java.util.Date"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.core.JiazuSubSystem"%>
<%@page import="com.fy.engineserver.jiazu2.manager.JiazuManager2"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.jiazu2.manager.BiaocheEntityManager"%>
<%@page import="com.fy.engineserver.jiazu2.instance.BiaoCheEntity"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.newtask.TaskEntityManager"%>
<%@page import="com.fy.engineserver.newtask.TaskEntity"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="com.fy.engineserver.economic.CurrencyType"%>
<%@page import="com.fy.engineserver.economic.BillingCenter"%>
<%@page import="com.fy.engineserver.jiazu2.instance.JiazuXiulian"%>
<%@page import="com.fy.engineserver.activity.fireActivity.FireActivityNpcEntity"%>
<%@page import="com.fy.engineserver.activity.fireActivity.FireActivityNpc"%>
<%@page import="com.fy.engineserver.core.LivingObject"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.jiazu.JiazuMember"%>
<%@page import="com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet.BuildingType"%>
<%@page import="com.fy.engineserver.septbuilding.entity.SeptBuildingEntity"%>
<%@page import="com.fy.engineserver.septstation.SeptStation"%>
<%@page import="com.fy.engineserver.septstation.service.SeptStationManager"%>
<%@page import="com.fy.engineserver.jiazu2.manager.JiazuEntityManager2"%>
<%@page import="com.fy.engineserver.jiazu2.instance.JiazuMember2"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.util.server.TestServerConfigManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<head>
</head>
<body>
	<%
		String action = request.getParameter("action");
		String playerId = request.getParameter("playerId");
		String jiazuMoney = request.getParameter("jiazuMoney");
		String jiazuZiyuan = request.getParameter("jiazuZiyuan");
		String jianzhuName = request.getParameter("jianzhuName");
		String chakanJianzhuMing = request.getParameter("chakanJianzhuMing");
		String shuangfang = request.getParameter("shuangfang");
		String xueliang = request.getParameter("xueliang");
		String addSalNum = request.getParameter("addSalNum");
		String psd = request.getParameter("psd");
		String psd4bc = request.getParameter("psd4bc");
		addSalNum = addSalNum == null ? "" : addSalNum;
		psd4bc = psd4bc == null ? "" : psd4bc;
		psd = psd == null ? "" : psd;
		shuangfang = shuangfang == null ? "" : shuangfang;
		xueliang = xueliang == null ? "" : xueliang;
		action = action == null ? "" : action;
		playerId = playerId == null ? "" :playerId;
		jiazuMoney = jiazuMoney == null ? "" :jiazuMoney;
		jiazuZiyuan = jiazuZiyuan == null ? "" :jiazuZiyuan;//
		jianzhuName = jianzhuName == null ? "" :jianzhuName;
		chakanJianzhuMing = chakanJianzhuMing == null ? "" :chakanJianzhuMing;
		
		String recorder = "";
		Object o = session.getAttribute("authorize.username");
		if(o!=null){
			recorder = o.toString();
		}
		String ipAdd = request.getRemoteAddr();
		
		if (action.equals("levelUpmainBuild") && chakanJianzhuMing.equals("true")) {
			for (BuildingType bt : BuildingType.values()) {
				out.println("[建筑名称:" + bt.getName() + "] [索引:" + bt.getIndex() + "]<br>");
			}
		}
		
		if (action.equals("addJiazuData")) {
			if(!TestServerConfigManager.isTestServer() && !psd.equals("xiugaijiazuziyuan")) {
				out.println("不是测试服，不允许修改");
				return;
			}
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			if (player.getJiazuId() <= 0) {
				out.println("没有家族!");
				return ;
			}
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			jiazu.addJiazuMoney(Long.parseLong(jiazuMoney));
			jiazu.setConstructionConsume(jiazu.getConstructionConsume() + Long.parseLong(jiazuZiyuan));
			out.println("修改成功!");
		} else if (action.endsWith("levelUpJiazu")) {
			if(!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不允许修改");
				return;
			}
			out.println("升级1聚仙店自动升级家族等级！");
			return ;
			/* Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			if (player.getJiazuId() <= 0) {
				out.println("没有家族!");
				return ;
			}
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			jiazu.setLevel(jiazu.getLevel() + 1);
			out.println("修改成功！"); */
		} else if (action.equals("addXiulian")) {
			if(!TestServerConfigManager.isTestServer() && !psd.equals("xiugaijiazuziyuan")) {
				out.println("不是测试服，不允许修改");
				return;
			}
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			JiazuMember2 jm2 = JiazuEntityManager2.instance.getEntity(player.getId());
			jm2.setPracticeValue(jm2.getPracticeValue() + Long.parseLong(jiazuMoney));
			out.println("修改成功！");
		} else if (action.equals("cleanjiazuHejiuTimes")) {
			if(!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不允许修改");
				return;
			}
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			Jiazu jz = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			SeptStation ss = SeptStationManager.getInstance().getSeptStationById(jz.getBaseID());
			FireActivityNpcEntity fe = ss.getFireActivityNpcEntity();
			fe.setFireNum(0);
			fe.setAddWoodNum(0);
			fe.setAddWoodNumTime(0L);
			out.println("清除成功！");
		} else if ("cleanJiazuhejiuShijian".equals(action)) {
			if(!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不允许修改");
				return;
			}
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			Jiazu jz = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			SeptStation ss = SeptStationManager.getInstance().getSeptStationById(jz.getBaseID());
			FireActivityNpcEntity fe = ss.getFireActivityNpcEntity();
			fe.setLastFireTime(0);
			out.println("成功");
		} else if ("changejiangjiCD".equals(action)) {
			if(!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不允许修改");
				return;
			}
			SeptStationManager.lvDownCD = 1;
			out.println("成功！！");
		} else if ("changejiangjituichuCD".equals(action)) {
			if(!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不允许修改");
				return;
			}
			JiazuSubSystem.LEAVE_JIAZU_PENALTY_TIME = 1;
			out.println("成功！！");
		} else if (action.endsWith("cleanjiazuYabiao")) {
			if(!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不允许修改");
				return;
			}
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			Jiazu jz = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			jz.setLastJiazuSilverCartime(0);
			Task task = TaskManager.getInstance().getTask(100034);
			List<Task> bigCollection = TaskManager.getInstance().getTaskBigCollections().get(task.getBigCollection());
			if (bigCollection != null && bigCollection.size() > 0) {
				int maxNum = task.getDailyTaskMaxNum();// 周期内上限
				int groupTaskCycleDoneNum = 0;// 周期内实际完成次数
				boolean hasSameBigcollectionTask = false;// 是否已经接取了同组任务
				for (Task _groupTask : bigCollection) {
					TaskEntity tte = player.getTaskEntity(_groupTask.getId());
					if (tte != null ) {
						tte.setCycleDeliverTimes(0);
						out.println("====");
					}
				}
			}
			out.println("清除成功！");
		} else if ("setBiaocheStrongLevel".equals(action)) {
			if(!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不允许修改");
				return;
			}
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			BiaoCheEntity entity = BiaocheEntityManager.instance.getEntity(player.getJiazuId());
			int xueliangLevel = Integer.parseInt(xueliang);
			int shuangfangLevel = Integer.parseInt(shuangfang);
			if (xueliangLevel < 0 || shuangfangLevel < 0 || xueliangLevel > 10 || shuangfangLevel > 10) {
				out.println("设置等级不对！ ");
				return ;
			}
			int[] tempaa = entity.getStrongerLevel();
			for (int i=0; i<entity.getStrongerType().length; i++) {
				if (entity.getStrongerType()[i] == BiaocheEntityManager.强化双防) {
					tempaa[i] = shuangfangLevel;
				} else if (entity.getStrongerType()[i] == BiaocheEntityManager.强化血量) {
					tempaa[i] = xueliangLevel;
				}
			}
			entity.setStrongerLevel(tempaa);
			out.println("设置成功!!!");
		} else if (action.equals("chakanjiazuXiulian")) {
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			JiazuMember2 jm2 = JiazuEntityManager2.instance.getEntity(player.getId());
			for (JiazuXiulian jxl : jm2.getXiulian()) {
				out.println(jxl + "<br>");
			}
		} else if (action.equals("setMatianCycle")) {
			if(!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不允许修改");
				return;
			}
			SeptStationManager.MaintaiCycle = Long.parseLong(playerId)*1000;
			out.println("当前家族维护时间周期为:" + SeptStationManager.MaintaiCycle + "<br>");
		} else if ("setWeekContr".equals(action)) {
			if(!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不允许修改");
				return;
			}
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			if (player.getJiazuId() <= 0) {
				out.println("没有家族!");
				return ;
			}
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			if (jiazu == null) {
				out.println("没有家族!");
				return ;
			}
			JiazuMember jm = JiazuManager.getInstance().getJiazuMember(player.getId(), jiazu.getJiazuID());
			jm.setCurrentWeekContribution(Integer.parseInt(shuangfang));
			jiazu.initMember4Client();
			out.println("设置成功！！");
		} else if ("setSalaryCycle".equals(action)) {
			if(!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不允许修改");
				return;
			}
			long time = Long.parseLong(playerId);
			long time2 = time * 60L * 1000L;
			Jiazu.testTime = time2;
			out.println("设置成功！！");
		} else if ("setNumss".equals(action)) {
			if(!TestServerConfigManager.isTestServer() && !psd.equals("xiugaijiazuziyuan")) {
				out.println("不是测试服，不允许修改");
				return;
			} 
			int aa = Integer.parseInt(playerId);
			Jiazu.CREATE_BASE_LESS_NUM = aa;
			out.println("设置成功！！");
		} else if (action.endsWith("setPratice11")) {
			if(!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不允许修改");
				return;
			}
			JiazuManager2.instance.base.setAddExp(Integer.parseInt(playerId));
			out.println("修改成功![每次修炼增加经验:" + JiazuManager2.instance.base.getAddExp() + "]");
		} else if ("buchangGongzi".equals(action)) {
			if (TestServerConfigManager.isTestServer() || "buchangjiazugongzi".equals(psd4bc)) {
				Player player = GamePlayerManager.getInstance().getPlayer(playerId);
				long addNum = Long.parseLong(addSalNum);
				if (addNum <= 0) {
					out.println("增加数量不能小于0!");
					return ;
				}
				psd4bc = "";
				String serverName = GameConstants.getInstance().getServerName();
				BillingCenter.getInstance().playerSaving(player, addNum, CurrencyType.GONGZI, SavingReasonType.后台设置属性, "补偿家族工资");
				String costStr = BillingCenter.得到带单位的银两(addNum);
				String conTent = "[服务器:<font color=red>"+serverName+"</font>] <br> [gmUser:"+recorder+"] [ip:"+ipAdd+"] [op:补偿家族工资] [数量:<font color=red>"+costStr+"</font>] ["+player.getLogString()+"]";
				if (!TestServerConfigManager.isTestServer()) {
					sendMail4GiveAe("后台补偿家族工资", conTent);
					JiazuManager.logger.warn(conTent);
				}
			} else {
				out.println("不知道密码别乱点！");
			}
		} else if ("buchangJiazuMoney".equals(action)) {
			if (TestServerConfigManager.isTestServer() || "buchangjiazuZijin".equals(psd4bc)) {
				Player player = GamePlayerManager.getInstance().getPlayer(playerId);
				long addNum = Long.parseLong(addSalNum);
				if (addNum <= 0) {
					out.println("增加数量不能小于0!");
					return ;
				}
				psd4bc = "";
				
				Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
				jiazu.addJiazuMoney(addNum);
				String costStr = BillingCenter.得到带单位的银两(addNum);
				String serverName = GameConstants.getInstance().getServerName();
				String conTent = "[服务器:<font color=red>"+serverName+"</font>] <br> [gmUser:"+recorder+"] [ip:"+ipAdd+"] [op:补偿家族资金] [数量:<font color=red>"+costStr+"</font>] ["+player.getLogString()+"] ["+jiazu.getLogString()+"]";
				if (!TestServerConfigManager.isTestServer()) {
					sendMail4GiveAe("后台补偿家族资金", conTent);
					JiazuManager.logger.warn(conTent);
				}
			} else {
				out.println("不知道密码别乱点！");
			}
		} else if ("changeUnvaladeZongZhu".equals(action)) {
			long zongpaiId = Long.parseLong(playerId);
			ZongPai zp = ZongPaiManager.getInstance().getZongPaiById(zongpaiId);
			Player player = GamePlayerManager.getInstance().getPlayer(zp.getMasterId());
			if (player.getJiazuId() <= 0 || ZongPaiManager.getInstance().getZongPaiByPlayerId(player.getId()) == null) {			//宗主家族被解散
				List<Long> jiazuIds = zp.getJiazuIds();
				long masterId = -1;
				for (int i=0; i<jiazuIds.size(); i++) {
					Jiazu jiazu = JiazuManager.getInstance().getJiazu(jiazuIds.get(i));
					long id = jiazu.getJiazuMaster();
					if (id > 0) {
						zp.setMasterId(id);
						out.println("更改宗主成功,新宗主id:" + id + "<br>");
						return ;
					}
				}
			
			}
		} else if ("changereleasetask".equals(action)) {
			if (!TestServerConfigManager.isTestServer()) {
				out.println("只有测试服可以改！");
				return ;
			}
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			SeptStation septStation = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
			for (int i = 0; i < septStation.getBuildingEntities().length; i++) {
				SeptBuildingEntity entity = septStation.getBuildingEntities()[i];
				int[] aa = Arrays.copyOf(entity.getBuildingState().getTempletType().getReleaseTaskNum(), entity.getBuildingState().getTempletType().getReleaseTaskNum().length);
				Arrays.fill(aa, 1);
				if (entity.getLvUpTaskNum() > 0) {
					entity.setLvUpTaskNum(1);
				}
				entity.getBuildingState().getTempletType().setReleaseTaskNum(aa);
				out.println("["+entity.getBuildingState().getTempletType().getName()+"] [taskNum:" + Arrays.toString(entity.getBuildingState().getTempletType().getReleaseTaskNum()) + "]<br>");
			}
					
		} else if ("cleanJiazuInfo".equals(action)) {
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			if (player.getJiazuId() <= 0) {
				if (player.getZongPaiName() == null || player.getZongPaiName().isEmpty()) {
					player.setJiazuName(null);
					out.println("此玩家没有家族！！");
					return ;
				}
				player.setJiazuName(null);
				player.setZongPaiName(null);
				player.initJiazuTitleAndIcon();
				player.initZongPaiName();
				out.println("设置成功！！");
			} else {
				JiazuMember jm = JiazuManager.getInstance().getJiazuMember(player.getId(), player.getJiazuId());
				if (jm != null) {
					out.println("不可修改正常玩家属性!!");
					return ;
				} else {
					player.setJiazuName(null);
					player.setJiazuId(0);
					player.initJiazuTitleAndIcon();
					player.setZongPaiName("");
					player.initZongPaiName();
					out.println("设置成功2！！");
				}
			}
		} else if (action.equals("addgongzi")) {
			if(!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不允许修改");
				return;
			}
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			BillingCenter.getInstance().playerSaving(player, Long.parseLong(jiazuMoney), CurrencyType.GONGZI, 1, "");
			out.println("设置成功！！");
		} else if (action.equals("levelUpmainBuild")) {
			if (playerId.equals("")) {
				return;
			}
			if(!TestServerConfigManager.isTestServer()  && !psd.equals("xiugaijiazuziyuan")) {
				out.println("不是测试服，不允许修改");
				return;
			}
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			String tName = null;
			SeptStation zhudi = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
			for (SeptBuildingEntity buildingEntity : zhudi.getBuildings().values()) {
				if (buildingEntity.getBuildingState().getTempletType().getName().equals(jianzhuName) ) {
					if (buildingEntity.getBuildingState().getLevel() >= 30) {
						out.println("目前还是别超过30级的好！！");
						return ;
					}
					buildingEntity.getBuildingState().setLevel(buildingEntity.getBuildingState().getLevel() + 1);
					tName = buildingEntity.getBuildingState().getTempletType().getName();
					buildingEntity.modifyNPCOutShow();
					zhudi.initInfo();
					zhudi.notifyFieldChange("buildingEntities");
					if (Translate.聚仙殿.equals(jianzhuName)) {
						jiazu.setLevel(buildingEntity.getBuildingState().getLevel());
					}
					break;
				}
			}
			if (tName != null) {
				out.println("升级   "+tName+"  成功!");
			} else {
				out.println("fail!");
			}
		}	
	%>
	
	<%!
	void sendMail4GiveAe(String title, String content) {
		StringBuffer sb = new StringBuffer();
		sb.append("<br>" + DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		sb.append("<br>" + content);
		ArrayList<String> args = new ArrayList<String>();
		args.add("-username");
		args.add("wtx062@126.com");
		args.add("-password");
		args.add("wangtianxin1986");

		args.add("-smtp");
		args.add("smtp.126.com");
		args.add("-from");
		args.add("wtx062@126.com");
		args.add("-to");
		//String[] mailTo = new String[]{"mazhencai@sqage.com"};
		String[] mailTo = new String[]{"3472335707@qq.com","116004910@qq.com"};
		// TODO mailAddress 修改邮件
		String address_to = "";
	
		for (String address : mailTo) {
			address_to += address + ",";
		}
	
		if (!"".equals(address_to)) {
			args.add(address_to);
			args.add("-subject");
			args.add(title);
			args.add("-message");
			args.add(sb.toString());
			args.add("-contenttype");
			args.add("text/html;charset=utf-8");
			try {
				JavaMailUtils.sendMail(args.toArray(new String[0]));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	%>>
	<form action="jiazu2.jsp" method="post">
		<input type="hidden" name="action" value="addJiazuData" />角色名   :
		<input type="text" name="playerId" value="<%=playerId%>" />增加家族资金数量:
		<input type="text" name="jiazuMoney" value="<%=jiazuMoney%>" />增加家族资源数量:
		<input type="text" name="jiazuZiyuan" value="<%=jiazuZiyuan%>" />密码：
		<input type="password" name="psd" value="<%=psd%>" />
		<input type="submit" value="增加家族资源" />
	</form>
	<br />
	<form action="jiazu2.jsp" method="post">
		<input type="hidden" name="action" value="addXiulian" />角色名   :
		<input type="text" name="playerId" value="<%=playerId%>" />增加修炼值数量:
		<input type="text" name="jiazuMoney" value="<%=jiazuMoney%>" />密码：
		<input type="password" name="psd" value="<%=psd%>" />
		<input type="submit" value="增加修炼值" />
	</form>
	<br />
	<form action="jiazu2.jsp" method="post">
		<input type="hidden" name="action" value="addgongzi" />角色名   :
		<input type="text" name="playerId" value="<%=playerId%>" />增加工资数量:
		<input type="text" name="jiazuMoney" value="<%=jiazuMoney%>" />
		<input type="submit" value="增加工资" />
	</form>
	<br />
	<form action="jiazu2.jsp" method="post">
		<input type="hidden" name="action" value="levelUpmainBuild" />角色名   :
		<input type="text" name="playerId" value="<%=playerId%>" />建筑名:
		<input type="text" name="jianzhuName" value="<%=jianzhuName%>" />不知道建筑名的填true:
		<input type="text" name="chakanJianzhuMing" value="<%=chakanJianzhuMing%>" />密码：
		<input type="password" name="psd" value="<%=psd%>" />
		<input type="submit" value="升级建筑" />
	</form>
	<br />
	<form action="jiazu2.jsp" method="post">
		<input type="hidden" name="action" value="levelUpJiazu" />角色名   :
		<input type="text" name="playerId" value="<%=playerId%>" />密码：
		<input type="password" name="psd" value="<%=psd%>" />
		<input type="submit" value="升级家族等级" />
	</form>
	<br />
	<form action="jiazu2.jsp" method="post">
		<input type="hidden" name="action" value="cleanjiazuHejiuTimes" />角色名   :
		<input type="text" name="playerId" value="<%=playerId%>" />
		<input type="submit" value="清除家族喝酒次数" />
	</form>
	<br />
	<form action="jiazu2.jsp" method="post">
		<input type="hidden" name="action" value="cleanJiazuhejiuShijian" />角色名   :
		<input type="text" name="playerId" value="<%=playerId%>" />
		<input type="submit" value="减少家族喝酒时间" />
	</form>
	<br />
	<form action="jiazu2.jsp" method="post">
		<input type="hidden" name="action" value="changejiangjiCD" />
		<input type="submit" value="清除建筑降级cd" />
	</form>
	<br />
	<form action="jiazu2.jsp" method="post">
		<input type="hidden" name="action" value="changejiangjituichuCD" />
		<input type="submit" value="清除退出家族cd" />
	</form>
	<br />
	<form action="jiazu2.jsp" method="post">
		<input type="hidden" name="action" value="cleanjiazuYabiao" />角色名   :
		<input type="text" name="playerId" value="<%=playerId%>" />
		<input type="submit" value="清除家族押镖次数" />
	</form>
	<br />
	<form action="jiazu2.jsp" method="post">
		<input type="hidden" name="action" value="chakanjiazuXiulian" />角色名   :
		<input type="text" name="playerId" value="<%=playerId%>" />
		<input type="submit" value="查看家族修炼信息" />
	</form>
	<br />
	<form action="jiazu2.jsp" method="post">
		<input type="hidden" name="action" value="setBiaocheStrongLevel" />角色名 :
		<input type="text" name="playerId" value="<%=playerId%>" />双防等级 :
		<input type="text" name="shuangfang" value="<%=shuangfang%>" />血量等级 :
		<input type="text" name="xueliang" value="<%=xueliang%>" />
		<input type="submit" value="镖车强化等级设置" />
	</form>
	<br />
	<form action="jiazu2.jsp" method="post">
		<input type="hidden" name="action" value="setMatianCycle" />维护时间周期 :
		<input type="text" name="playerId" value="<%=playerId%>" />
		<input type="submit" value="设置家族维护周期" />
	</form>
	<form action="jiazu2.jsp" method="post">
		<input type="hidden" name="action" value="setWeekContr" />角色名 :
		<input type="text" name="playerId" value="<%=playerId%>" />本周贡献值 :
		<input type="text" name="shuangfang" value="<%=shuangfang%>" />
		<input type="submit" value="设置本周家族贡献" />
	</form>
	<form action="jiazu2.jsp" method="post">
		<input type="hidden" name="action" value="setSalaryCycle" />设置发工资周期（分钟） :
		<input type="text" name="playerId" value="<%=playerId%>" />
		<input type="submit" value="设置重新计算发工资周期" />
	</form>
	<form action="jiazu2.jsp" method="post">
		<input type="hidden" name="action" value="setNumss" />申请驻地最低所需人数:
		<input type="text" name="playerId" value="<%=playerId%>" />密码：
		<input type="password" name="psd" value="<%=psd%>" />
		<input type="submit" value="设置申请驻地人数限制" />
	</form>
	<form action="jiazu2.jsp" method="post">
		<input type="hidden" name="action" value="setPratice11" />每次修炼增加经验:
		<input type="text" name="playerId" value="<%=playerId%>" />
		<input type="submit" value="每次修炼增加经验修改" />
	</form>
	<br />
	<form action="jiazu2.jsp" method="post">
		<input type="hidden" name="action" value="buchangGongzi" />玩家名:
		<input type="text" name="playerId" value="<%=playerId%>" />增加家族工资数量(文):
		<input type="text" name="addSalNum" value="<%=addSalNum%>" />密码:
		<input type="password" name="psd4bc" value="<%=psd4bc%>" />
		<input type="submit" value="补偿家族工资" />
	</form>
	<br />
	<form action="jiazu2.jsp" method="post">
		<input type="hidden" name="action" value="buchangJiazuMoney" />玩家名:
		<input type="text" name="playerId" value="<%=playerId%>" />增加家族资金数量(文):
		<input type="text" name="addSalNum" value="<%=addSalNum%>" />密码:
		<input type="password" name="psd4bc" value="<%=psd4bc%>" />
		<input type="submit" value="补偿家族资金" />
	</form>
	<br />
	<form action="jiazu2.jsp" method="post">
		<input type="hidden" name="action" value="cleanJiazuInfo" />玩家名:
		<input type="text" name="playerId" value="<%=playerId%>" />
		<input type="submit" value="清空玩家家族信息" />
	</form>
	<br />
	<form action="jiazu2.jsp" method="post">
		<input type="hidden" name="action" value="changeUnvaladeZongZhu" />宗派id:
		<input type="text" name="playerId" value="<%=playerId%>" />
		<input type="submit" value="交接无效宗主" />
	</form>
	<form action="jiazu2.jsp" method="post">
		<input type="hidden" name="action" value="changereleasetask" />角色名:
		<input type="text" name="playerId" value="<%=playerId%>" />
		<input type="submit" value="修改家族任务完成个数" />
	</form>
	
</body>
</html>
