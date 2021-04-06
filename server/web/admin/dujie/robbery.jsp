<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.carbon.wingCarbon.instance.WingCarbon"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page import="com.fy.engineserver.message.NOTICE_PARTICLE_REQ"%>
<%@page import="com.fy.engineserver.septstation.service.SeptStationManager"%>
<%@page import="com.fy.engineserver.septstation.SeptStation"%>
<%@page import="com.fy.engineserver.jiazu.JiazuFunction"%>
<%@page import="com.fy.engineserver.jiazu.JiazuTitle"%>
<%@page import="com.fy.engineserver.jiazu.JiazuMember"%>
<%@page import="com.fy.engineserver.sprite.monster.BossMonster"%>
<%@page import="com.fy.engineserver.battlefield.dota.DotaSolider"%>
<%@page import="com.fy.engineserver.carbon.wingCarbon.WingCarbonManager"%>
<%@page import="com.fy.engineserver.sprite.npc.MemoryNPCManager.NPCTempalte"%>
<%@page import="com.fy.engineserver.sprite.npc.MemoryNPCManager"%>
<%@page import="com.fy.engineserver.sprite.npc.WingCarbonNPC"%>
<%@page import="com.fy.engineserver.sprite.Fighter"%>
<%@page import="com.fy.engineserver.message.NPC_ACTION_RES"%>
<%@page import="com.fy.engineserver.sprite.npc.NPC"%>
<%@page import="com.fy.engineserver.core.LivingObject"%>
<%@page import="com.fy.engineserver.core.g2d.Road"%>
<%@page import="com.fy.engineserver.core.g2d.Graphics2DUtil"%>
<%@page import="com.fy.engineserver.core.g2d.SignPost"%>
<%@page import="com.fy.engineserver.core.MoveTrace"%>
<%@page import="com.fy.engineserver.sprite.monster.Monster"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.util.TimeTool.TimeDistance"%>
<%@page import="com.fy.engineserver.util.CompoundReturn"%>
<%@page import="com.fy.engineserver.newtask.TaskEntity"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="com.fy.engineserver.jiazu2.model.JiazuRenwuModel"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.jiazu2.manager.JiazuManager2"%>
<%@page import="com.fy.engineserver.util.server.TestServerConfigManager"%>
<%@page import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate_YuanSuShangHai"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.message.PLAYER_REVIVED_RES"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="com.fy.engineserver.message.NOTIFY_ROBBERY_COUNTDOWN_REQ"%>
<%@page import="com.fy.engineserver.sprite.CountdownAgent"%>
<%@page import="com.fy.engineserver.message.NOTICE_CLIENT_COUNTDOWN_REQ"%>
<%@page import="com.fy.engineserver.core.g2d.Point"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardDate"%>
<%@page import="com.fy.engineserver.newBillboard.Billboard"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardsManager"%>
<%@page import="com.fy.engineserver.billboard.concrete.ExpBillboards"%>
<%@page import="com.fy.engineserver.masterAndPrentice.MasterPrentice"%>
<%@page import="com.fy.engineserver.society.Relation"%>
<%@page import="com.fy.engineserver.society.SocialManager"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@page import="com.fy.engineserver.unite.UniteManager"%>
<%@page import="com.fy.engineserver.unite.Unite"%>
<%@page import="com.fy.engineserver.country.data.Country"%>
<%@page import="com.fy.engineserver.country.manager.CountryManager"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplateManager"%>
<%@page import="com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant"%>
<%@page import="com.fy.engineserver.datasource.buff.Buff"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.message.GameMessageFactory"%>
<%@page import="com.fy.engineserver.message.NOTICE_CLIENT_PLAY_PARTICLE_RES"%>
<%@page import="com.fy.engineserver.core.ParticleData"%>
<%@page import="com.fy.engineserver.core.TransportData"%>
<%@page import="com.fy.engineserver.sprite.Sprite"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page
	import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager"%>
<%@page
	import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page
	import="com.fy.engineserver.sprite.monster.MemoryMonsterManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<%@page import="com.fy.engineserver.sprite.pet2.*"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="java.util.List"%>
<%@page import="com.xuanzhi.tools.page.PageUtil"%><html>
<head>
</head>
<body>
	<%
		String action = request.getParameter("action");
		String playerId = request.getParameter("playerId");
		String level = request.getParameter("level");
		//
		playerId = playerId == null ? "" : playerId;
		level = level == null ? "" : level;
		
		
		/* Monster mm = MemoryMonsterManager.getMonsterManager().createMonster(20113313);
		mm.setX(plo.getX() + 100);
		mm.setY(plo.getY() + 100);
		plo.getCurrentGame().addSprite(mm); */
		
		//WingCarbon.isTest = true;
		//WingCarbon.extraTime = 1500;
		
		//WingCarbon.第一组坐标点 = new int[][]{{405,615}, {537,572}, {456,490}};
		//WingCarbon.第二组坐标点 = new int[][]{{921,540}, {760,540}, {983,615}};
		//WingCarbon.第三组坐标点 = new int[][]{{542,916}, {467,826}, {586,892}};
		//WingCarbon.第四组坐标点 = new int[][]{{974,878}, {1043,757}, {802,912}};
		
		if (false){
		
		Player plo = GamePlayerManager.getInstance().getPlayer("qwer");
		//NPCTempalte nnkt = ((MemoryNPCManager)MemoryNPCManager.getNPCManager()).getTemplates().get(90011000);
		//out.println(nnkt);
		
		/* WingCarbonNPC npcs = (WingCarbonNPC)MemoryNPCManager.getNPCManager().createNPC(90011000);
		npcs.setX(plo.getX());
		npcs.setY(plo.getY());
		plo.getCurrentGame().addSprite(npcs); */
		
		
		//int[] tempCom = JiazuManager2.instance.getAlreadyCompTaskNumAndMaxNum(plo);
		//out.println(Arrays.toString(tempCom));
		//Fighter ftt = null;
		//for (LivingObject loss : plo.getCurrentGame().getLivingObjects()) {
			//if (loss instanceof WingCarbonNPC) {
				//((WingCarbonNPC)loss).setTitle("<f color='0xff0000'>" + ((WingCarbonNPC)loss).getHp() +"/"+ ((WingCarbonNPC)loss).getMaxHP()+"</f>");
				//break;
				//plo.getCurrentGame().removeSprite((WingCarbonNPC)loss);
			//}
		//}
		//1536 1681
		//1047 1310
		//1692 1234
		//972 1702
		List<Integer[]> tempList1 = new ArrayList<Integer[]>();
		tempList1.add(new Integer[]{1536, 1681});
		tempList1.add(new Integer[]{1047, 1310});
		tempList1.add(new Integer[]{1692, 1234});
		tempList1.add(new Integer[]{972, 1702});
		for (int i=0; i<tempList1.size(); i++) {
			Monster  monster1 = MemoryMonsterManager.getMonsterManager().createMonster(20113312);
			monster1.setX(tempList1.get(i)[0]);
			monster1.setY(tempList1.get(i)[1]);
			monster1.setBornPoint(new Point(tempList1.get(i)[0], tempList1.get(i)[1]));
			monster1.updateDamageRecord(plo, 100, 100);
			monster1.setMonsterLocat((byte)1);
			plo.getCurrentGame().addSprite(monster1);
			out.println(pathFindingForFighting(plo.getCurrentGame(), plo, (BossMonster)monster1));  
		}
		
		/* Monster monster1 = MemoryMonsterManager.getMonsterManager().getMonster(76637); */
		
		
		
		/* Point s = new Point(monster1.getX(), monster1.getY());
		Point e = new Point(1698, 3100);
		SignPost sps[] = plo.getCurrentGame().getGameInfo().navigator.findPath(s, e);
		Point ps[] = new Point[sps.length + 2];
		short roadLen[] = new short[sps.length + 1];
		for (int i = 0; i < ps.length; i++) {
			if (i == 0) {
				ps[i] = new Point(s.x, s.y);
			} else if (i <= sps.length) {
				ps[i] = new Point(sps[i - 1].x, sps[i - 1].y);
			} else {
				ps[i] = new Point(e.x, e.y);
			}
		}
		int totalLen = 0;
		for (int i = 0; i < roadLen.length; i++) {
			if (i == 0) {
				roadLen[i] = (short) Graphics2DUtil.distance(ps[0], ps[1]);
			} else if (i < sps.length) {
				Road r = plo.getCurrentGame().getGameInfo().navigator.getRoadBySignPost(sps[i - 1].id, sps[i].id);
				if (r != null) {
					roadLen[i] = r.len;
				} else {
					roadLen[i] = (short) Graphics2DUtil.distance(ps[i], ps[i + 1]);
				}
			} else {
				roadLen[i] = (short) Graphics2DUtil.distance(ps[i], ps[i + 1]);
			}
			totalLen += roadLen[i];
		}
		MoveTrace path = new MoveTrace(roadLen, ps, monster1.getHeartBeatStartTime() + totalLen * 1000 / (monster1.getSpeed()));
		monster1.setMoveTrace(path); */
		
		
		
		
		
		
		/*JiazuRenwuModel jmm = null;
		for (JiazuRenwuModel jrm : JiazuManager2.instance.taskMap.values()) {
			jmm = jrm;
			break;
		}
		Task task = TaskManager.getInstance().getTask(jmm.getTaskId());
		if (task.getBigCollection() != null && !"".equals(task.getBigCollection())) {
			List<Task> bigCollection = TaskManager.getInstance().getTaskBigCollections().get(task.getBigCollection());
			if (bigCollection != null && bigCollection.size() > 0) {
				int maxNum = task.getDailyTaskMaxNum();// 周期内上限
				int groupTaskCycleDoneNum = 0;// 周期内实际完成次数
				for (Task _groupTask : bigCollection) {
					TaskEntity tte = plo.getTaskEntity(_groupTask.getId());
					if (tte != null && _groupTask.getType() == Player.TASK_TYPE_DAILY) {
						CompoundReturn info = tte.getCycleDeilverInfo();
						int doneNum = maxNum - info.getIntValue();
						groupTaskCycleDoneNum += doneNum;
						long ttt = Timestamp.valueOf("2014-09-01 00:00:00").getTime();
						boolean rr = TimeTool.instance.isSame(com.fy.engineserver.gametime.SystemTime.currentTimeMillis(), ttt, TimeDistance.WEEK, task.getDailyTaskCycle());
						long distanceDiff = TimeTool.instance.cycleDistance(com.fy.engineserver.gametime.SystemTime.currentTimeMillis(), ttt, TimeDistance.WEEK);
						out.println(distanceDiff + "  " + rr + "<br>");
					}
				}
			}
		}*/
		}
	%>
	<%!
public int pathFindingForFighting(Game g,Fighter target, BossMonster fighter){
		
		int[][] 第一组攻击点 = {
			{-80,0},{-60,45},{-45,60},{0,80},{45,60},{60,45},
			{80,0},{60,-45},{45,-60},{0,-80},{-45,-60},{-60,-45}
		};
		
		int[][] 第二组攻击点 = {
			{-48,13},{-35,35},{-13,48},{13,48},{35,35},{48,13},
			{48,-13},{35,-35},{13,-48},{-13,-48},{-35,-35},{-48,-13}
		};
		
		int flag1[] = new int[DotaSolider.第一组攻击点.length];
		//int flag2[] = new int[DotaSolider.第二组攻击点.length];
		
		LivingObject los[] = g.getLivingObjects();
		for(int i = 0 ; i < los.length ; i++){
			if(los[i] instanceof BossMonster){
				BossMonster d = (BossMonster)los[i];
				
				if(d != fighter && d.isDeath() == false ){
					if(d.是否有正在前往的攻击点){
						if(d.攻击点组 == 1){
							flag1[d.攻击点] = 1;
						}
						//else if(d.攻击点组 == 2){
						//	flag2[d.攻击点] = 1;
						//}
					}
					
				}
			}
		}
		int groupIndex = 1;
		int nearestPoint = -1;
		int distance = Integer.MAX_VALUE;
		
		for(int i = 0 ; i < flag1.length ; i++){
			if(flag1[i] == 0){
				int d = (fighter.getX() - (target.getX() + DotaSolider.第一组攻击点[i][0])) * (fighter.getX() - (target.getX() + DotaSolider.第一组攻击点[i][0]));
				d += (fighter.getY() - (target.getY() + DotaSolider.第一组攻击点[i][1])) * (fighter.getY() - (target.getY() + DotaSolider.第一组攻击点[i][1]));
				
				if(d < distance){
					distance = d;
					nearestPoint = i;
				}
			}
		}
		
		/* if(nearestPoint == -1){
			groupIndex = 2;
			for(int i = 0 ; i < flag2.length ; i++){
				if(flag2[i] == 0){
					int d = (fighter.getX() - (target.getX() + DotaSolider.第二组攻击点[i][0])) * (fighter.getX() - (target.getX() + DotaSolider.第二组攻击点[i][0]));
					d += (fighter.getY() - (target.getY() + DotaSolider.第二组攻击点[i][1])) * (fighter.getY() - (target.getY() + DotaSolider.第二组攻击点[i][1]));
					
					if(d < distance){
						distance = d;
						nearestPoint = i;
					}
				}
			}
		} */
		
		if(nearestPoint == -1){
			//fighter.targetDisappear(target);
//			fighter.attackTarget = null;
			return -1;
		}
		
		fighter.是否有正在前往的攻击点 = true;
		fighter.攻击点组 = groupIndex;
		fighter.攻击点 = nearestPoint;
		
		
		int dx = 0;
		int dy = 0;
		if(fighter.攻击点组 == 1){
			dx = target.getX() + 第一组攻击点[fighter.攻击点][0];
			dy = target.getY() + 第一组攻击点[fighter.攻击点][1];
		}else{
			dx = target.getX() + BossMonster.第二组攻击点[fighter.攻击点][0];
			dy = target.getY() + BossMonster.第二组攻击点[fighter.攻击点][1];
		}
		
		Point s = new Point(fighter.getX(),fighter.getY());
		Point e = new Point(dx,dy);
		int L = Graphics2DUtil.distance(s, e);
		if(g.getGameInfo().navigator.isVisiable(s.x,s.y,e.x,e.y)){
			Point ps[] = new Point[]{s,e};
			short roadLen[] = new short[1];
			roadLen[0] =  (short)L;
			MoveTrace path = new MoveTrace(roadLen,ps,(long)(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + (long)L*1000/fighter.getSpeed()));
			fighter.setMoveTrace(path);
		}else{
			SignPost sps[] = g.getGameInfo().navigator.findPath(s, e);
			if(sps == null) return -1;
			Point ps[] = new Point[sps.length+1];
			short roadLen[] = new short[sps.length];
			for(int i = 0 ; i < ps.length ; i++){
				if(i == 0){
					ps[i] = new Point(s.x,s.y);
				}else{
					ps[i] = new Point(sps[i-1].x,sps[i-1].y);
				}
			}
			int totalLen = 0;
			for(int i = 0 ; i < roadLen.length ; i++){
				if(i == 0){
					roadLen[i] = (short)Graphics2DUtil.distance(ps[0], ps[1]);
				}else{
					Road r =  g.getGameInfo().navigator.getRoadBySignPost(sps[i-1].id, sps[i].id);
					if(r != null){
						roadLen[i] = r.len;
					}else{
						roadLen[i] = (short)Graphics2DUtil.distance(ps[i], ps[i+1]);
					}
				}
				totalLen+= roadLen[i];
			}
			MoveTrace path = new MoveTrace(roadLen,ps,com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + totalLen*1000/fighter.getSpeed() + 2000L);
			fighter.setMoveTrace(path);
		}
		return nearestPoint;
	}
	%>
	<h2>渡劫系统</h2>

	<form action="robbery.jsp" method="get">
		<input type="hidden" name="action" value="enterRobbery" /> 玩家ID<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="渡劫" />
	</form>
	<br />
	<form action="robbery.jsp" method="get">
		<input type="hidden" name="action" value="reset" /> 玩家ID<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="重置渡劫数据" />
	</form>
	<br />
	<form action="robbery.jsp" method="get">
		<input type="hidden" name="action" value="killIm" /> 玩家ID<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="查看玩家渡劫信息" />
	</form>
	<br />
	<form action="robbery.jsp" method="get">
		<input type="hidden" name="action" value="enterRobbery2" /> 玩家ID<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="text" name="level" value="<%=level%>" /> <input
			type="submit" value="特定等级渡劫" />
	</form>
	<br />
	<form action="robbery.jsp" method="get">
		<input type="hidden" name="action" value="enterRobbery3" /> 玩家ID<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="text" name="level" value="<%=level%>" /> <input
			type="submit" value="强制拉人" />
	</form>
	<br />
	<form action="robbery.jsp" method="get">
		<input type="hidden" name="action" value="back" /> 玩家ID<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="回城" />
	</form>
	<br />
	<form action="robbery.jsp" method="get">
		<input type="hidden" name="action" value="guanxiao" /> 玩家ID<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="text" name="level" value="<%=level%>" /> <input
			type="submit" value="粒子光效" />
	</form>
	<br />
	<form action="robbery.jsp" method="get">
		<input type="hidden" name="action" value="start" /> 玩家ID<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="开始渡劫" />
	</form>
	<br />
	<form action="robbery.jsp" method="get">
		<input type="hidden" name="action" value="setlev" /> 玩家ID<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="text" name="level" value="<%=level%>" /> <input
			type="submit" value="设置渡劫等级" />
	</form>
	<form action="robbery.jsp" method="get">
		<input type="hidden" name="action" value="setlev2" /> 玩家ID<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="text" name="level" value="<%=level%>" /> <input
			type="submit" value="设置通过的层数" />
	</form>
	<br />
	<form action="robbery.jsp" method="get">
		<input type="hidden" name="action" value="setlev3" />
		<input type="submit" value="刷新排行榜" />
	</form>
	<br />
	<form action="robbery.jsp" method="get">
		<input type="hidden" name="action" value="feisheng" /> 玩家ID<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="飞升" />
	</form>
	<br />
	<form action="robbery.jsp" method="get">
		<input type="hidden" name="action" value="feisheng11" /> 玩家ID<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="强行飞升" />
	</form>
	<br />
	<form action="robbery.jsp" method="get">
		<input type="hidden" name="action" value="dujieshibai" /> 玩家ID<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="失败buff" />
	</form>
	<br />
	<form action="robbery.jsp" method="get">
		<input type="hidden" name="action" value="chakanBuff" /> 玩家ID<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="看buff" />
	</form>
	<br />
	<form action="robbery.jsp" method="get">
		<input type="hidden" name="action" value="chakanBuff11" /> 玩家ID<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="text" name="level" value="<%=level%>" /> <input
			type="submit" value="加buff" />
	</form>
	<br />
	<form action="robbery.jsp" method="get">
		<input type="hidden" name="action" value="cha11" /> 玩家ID<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="渡劫回魂丹" />
	</form>
	<br />
	<form action="robbery.jsp" method="get">
		<input type="hidden" name="action" value="charen" /> 玩家ID<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="text" name="level" value="<%=level%>" /> <input
			type="submit" value="查人" />
	</form>
	<br />
	<form action="robbery.jsp" method="get">
		<input type="hidden" name="action" value="shuaboss" /> 玩家ID<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="text" name="level" value="<%=level%>" /> <input
			type="submit" value="刷怪" />
	</form>
	<br />
	<form action="robbery.jsp" method="get">
		<input type="hidden" name="action" value="countdown" /> 玩家ID<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="测试1" />
	</form>
	<br />
	<form action="robbery.jsp" method="get">
		<input type="hidden" name="action" value="addtitle" /> 玩家ID<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="text" name="level" value="<%=level%>" /> <input
			type="submit" value="加称号" />
	</form>
	<br />
	<form action="robbery.jsp" method="get">
		<input type="hidden" name="action" value="yuanshenqiehuan" /> 玩家ID<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="text" name="level" value="<%=level%>" /> <input
			type="submit" value="强切元神" />
	</form>
	<br />
	<form action="robbery.jsp" method="get">
		<input type="hidden" name="action" value="yuanshenqiehuan111" /> 玩家ID<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="text" name="level" value="<%=level%>" /> <input
			type="submit" value="修改渡劫开启时间" />
	</form>
	<br />
	<form action="robbery.jsp" method="get">
		<input type="hidden" name="action" value="dujiefenying" />限制等级<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="修改渡劫封印" />
	</form>
	<br />
	<form action="robbery.jsp" method="get">
		<input type="hidden" name="action" value="qianzhijinru" />强拉时间(s)<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="强拉时间修改" />
	</form>
	<br />
	<form action="robbery.jsp" method="get">
		<input type="hidden" name="action" value="buffceshi" />测试2验证玩家当前状态<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="强拉时间修改" />
	</form>
	<br />

	<%
		if ("enterRobbery".equals(action)) {
			if (playerId.isEmpty()) {
				out.println("请指定玩家id");
				return;
			}
			Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
			if (p == null) {
				out.println("玩家是null");
				return;
			}
			TransitRobberyManager.getInstance().ready2EnterRobberyScene(p, false);
		} else if("reset".equals(action)) {
			if(!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不能修改数据");
				return;
			}
			TransitRobberyEntityManager.getInstance().resetRobberyEntity(Long.parseLong(playerId));
			out.println("重置成功");
		} else if("killIm".equals(action)){
			Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
			if (p == null) {
				out.println("玩家是null");
				return;
			}
			TransitRobberyEntity entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(p.getId());
			if(entity != null) {
				out.println("[" + p.getName() + "] [已通过渡劫层数:" + entity.getCurrentLevel() + "] [最后一次渡劫时间:" + new Timestamp(entity.getRobberyStartTime()) + "]");
			}
		} else if("enterRobbery2".equals(action)){
			if(!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不能修改数据");
				return;
			}
			if (playerId.isEmpty()) {
				out.println("请指定玩家id");
				return;
			}
			Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
			if (p == null) {
				out.println("玩家是null");
				return;
			}
			TransitRobberyManager.getInstance().ready2EnterRobberyScene(p, true, Integer.parseInt(level));
		}  else if("back".equals(action)){
			TransitRobberyManager.getInstance().removeRobbery(Long.parseLong(playerId));	//渡劫线程中删除此人信息
			TransitRobberyEntityManager.getInstance().removeFromRobbering(Long.parseLong(playerId));
			if (playerId.isEmpty()) {
				out.println("请指定玩家id");
				return;
			}
			Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
			try{
				p.getCurrentGame().transferGame(p, new TransportData(0, 0, 0, 0, p.getResurrectionMapName(), p.getResurrectionX(), p.getResurrectionY()), true);
			}catch(Exception e111){
			}
			TransitRobberyEntityManager.getInstance().removeFromRobbering(Long.parseLong(playerId));
			out.println("回城");
		} else if("guanxiao".equals(action)){
			Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
			if (p == null) {
				out.println("玩家是null");
				return;
			}
			ParticleData[] particleDatas = new ParticleData[1];
			particleDatas[0] = new ParticleData(p.getId(), level,-1, 2, 1, 1);
			NOTICE_CLIENT_PLAY_PARTICLE_RES resp = new NOTICE_CLIENT_PLAY_PARTICLE_RES(GameMessageFactory.nextSequnceNum(), particleDatas);
			p.addMessageToRightBag(resp);
		} else if("start".equals(action)){
			Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
			if (p == null) {
				out.println("玩家是null");
				return;
			}
			TransitRobberyManager.getInstance().startRobbery(p);
		} else if("setlev".equals(action)){
			if(!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不能修改数据");
				return;
			}
			String a = TransitRobberyEntityManager.getInstance().setCurrentRobberyLevel(Long.parseLong(playerId), Integer.parseInt(level));
			out.print(a);
		}else if("setlev2".equals(action)){
			if(!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不能修改数据");
				return;
			}
			TransitRobberyEntity entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(Long.parseLong(playerId));
			if(entity != null) {
				entity.setPassLayer(Integer.parseInt(level));
				TransitRobberyEntityManager.em.notifyFieldChange(entity, "passLayer");
				out.println("设置成功");
				TransitRobberyEntity entity2 = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(Long.parseLong(playerId));
				out.println("玩家当前层数=" + entity2.getPassLayer());
				return;
			}
			out.println("设置失败");
		}else if("setlev3".equals(action)){
			BillboardsManager.getInstance().flushAllBillBoard();
			out.println("刷新排行榜成功！");
		}else if("feisheng11".equals(action)){
			if(!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不能修改数据");
				return;
			}
			Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
			if (p == null) {
				out.println("玩家是null");
				return;
			}
			TransitRobberyEntityManager.getInstance().feiSheng(p, true);
			out.println("飞升成功!");
		}else if("feisheng".equals(action)){
			if(!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不能修改数据");
				return;
			}
			Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
			if (p == null) {
				out.println("玩家是null");
				return;
			}
			TransitRobberyManager.getInstance().testFeishen(p);
			out.println("1111111");
		} else if("dujieshibai".equals(action)){
			String name = "四象火神";
			String name2 = "四象火神";
			BuffTemplateManager btm = BuffTemplateManager.getInstance();
			BuffTemplate bt = btm.getBuffTemplateByName(name);
			BuffTemplate bt2 = btm.getBuffTemplateByName(name2);
			out.println(name + "<BR>" + bt);
			out.println(name2 + "<BR>" + bt2);
			if (bt != null) {
				Buff buff = bt.createBuff(1);
				out.println("buff==" + buff);
				if (buff != null) {
					buff.setAdvantageous(true);
					buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
					buff.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 24 * 60 * 60 * 1000);
					out.println("加buff");
				}
			}
			
		}else if("chakanBuff".equals(action)){
			Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
			if (p == null) {
				out.println("玩家是null");
				return;
			}
			ArrayList<Buff> buffs = p.getBuffs();
			//要存储的buff
			boolean exist = false;
			int level1 = 0;
			for(int i=0; i<buffs.size(); i++){ 
				out.println(buffs.get(i).getTemplateName() + "  " + buffs.get(i).getLevel());
				if(buffs.get(i).getTemplateName().trim().equals(RobberyConstant.FAILBUFF)){
					level1 = buffs.get(i).getLevel() + 1;
				}
			}
			if(level1 >= 3){
				level1 = 3;
			}
			BuffTemplateManager btm = BuffTemplateManager.getInstance();
			BuffTemplate bt = btm.getBuffTemplateByName(RobberyConstant.FAILBUFF);
			if (bt != null) {
				Buff buff = bt.createBuff(level1);
				if (buff != null) {
					buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
					buff.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 24 * 60 * 60 * 1000);
					buff.setCauser(p);
					p.placeBuff(buff);
				}
				out.print(buff.getTemplateName() + "  " + buff.getLevel());
			}
		}else if("feisheng".equals(action)){
			Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
			if (p == null) {
				out.println("玩家是null");
				return;
			}
			TransitRobberyManager.getInstance().使用渡劫回魂丹(p);
			out.println("====");
		}else if("chakanBuff11".equals(action)){
			Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
			if (p == null) {
				out.println("玩家是null");
				return;
			}
			try {
				// buff
				BuffTemplateManager btm = BuffTemplateManager.getInstance();
				BuffTemplate bt = btm.getBuffTemplateByName(level);
				out.println("==111  " + level);
				out.println("==" + bt);
				if (bt != null) {
					Buff buff = bt.createBuff(0);
					out.println("=2222=" + buff);
					if (buff != null) {
						buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
						buff.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 100 * 1000);
						buff.setCauser(p);
						p.placeBuff(buff);
					}
				}
			} catch (Exception ex) {
			}
		}else if("cha11".equals(action)){
			Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
			if (p == null) {
				out.println("玩家是null");
				return;
			}
			ArrayList<Buff> buffs = p.getBuffs();
			//要存储的buff
			int level1 = -1;
			long time = 0;
			long startTime = 0;
			for(int i=0; i<buffs.size(); i++){ 
				if(buffs.get(i).getTemplateName().trim().equals(RobberyConstant.FAILBUFF)){
					level1 = buffs.get(i).getLevel();
					time = buffs.get(i).getInvalidTime() - 3 * 60 * 60 * 1000;
					startTime = buffs.get(i).getStartTime();
					out.println("=====" + time);
					break;
				}
			}
			if(level1 < 0){
				return;
			}
			BuffTemplateManager btm = BuffTemplateManager.getInstance();
			BuffTemplate bt = btm.getBuffTemplateByName(RobberyConstant.FAILBUFF);
			if (bt != null) {
				Buff buff = bt.createBuff(level1);
				if (buff != null) {
					buff.setStartTime(startTime);
					buff.setInvalidTime(time);
					buff.setCauser(p);
					p.placeBuff(buff);
				}
			}
		}else if("charen".equals(action)){
			Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
			if (p == null) {
				out.println("玩家是null");
				return;
			}
			Player player = null;
			SocialManager socialManager = SocialManager.getInstance();
			Relation relation = socialManager.getRelationById(p.getId());
			MasterPrentice mp = relation.getMasterPrentice();
			out.println("11111 " + socialManager);
			out.println("222222 " + relation);
			out.println("33333" + mp);
			out.println("level  " + level);
			try{
			switch (Integer.parseInt(level)) {
				case 1: 			//徒弟
					List<Long> preTices = mp.getPrentices();				//徒弟列表
					out.println("============" + preTices);
					out.println("============" + preTices.size());
					if(preTices != null && preTices.size() > 0){
						player = GamePlayerManager.getInstance().getPlayer(p.random.nextInt(preTices.size()));
					}
					break;
				case 2: 			//师父
					long masterId = mp.getMasterId();						//师父id
					out.println("masterId============" + masterId);
					if(masterId > 0){
						player = GamePlayerManager.getInstance().getPlayer(masterId);
					}
					break;
				case 3: 			//配偶
					String coupleName = relation.getCoupleName();			//夫妻名字
					out.println("coupleName============" + coupleName);
					if(coupleName != null){
						player = GamePlayerManager.getInstance().getPlayer(coupleName);
					}
					break;
				case 4: 			//仇人
					List<Long> chourenList = relation.getChourenlist();
					out.println("chourenList============" + chourenList.size());
					if(chourenList != null && chourenList.size() > 0){
						player = GamePlayerManager.getInstance().getPlayer(p.random.nextInt(chourenList.size()));
					}
					break;
				case 5: 			//家族族长
					Jiazu jiazu = JiazuManager.getInstance().getJiazu(p.getJiazuId());
					out.println("jiazu============" + jiazu);
					if(jiazu != null){
						player = GamePlayerManager.getInstance().getPlayer(jiazu.getJiazuMaster());
					}
					break;
				case 6: 			//兄弟
					long unitedId = relation.getUniteId();
					out.println("unitedId============" + unitedId);
					if(unitedId != -1){
						Unite u = UniteManager.getInstance().getUnite(unitedId);
						if(u != null){
							List<Long> list = u.getMemberIds();
							if(list != null && list.size() > 0){
								player = GamePlayerManager.getInstance().getPlayer(p.random.nextInt(list.size()));
							}
						}
					}
					break;
				case 7: 			//天尊？
					CountryManager cm = CountryManager.getInstance();
					Country country = cm.countryMap.get(p.getCountry());
					if(country != null){
						long kingId = country.getKingId();
						player = GamePlayerManager.getInstance().getPlayer(kingId);
					}
					break;
				default:
					break;
				}
				out.println("**************************");
				if(player == null){				//之前没有查到玩家   先查好友列表
					List<Long> friendList =relation.getFriendlist();
					out.println("ff=======" + friendList);
					if(friendList != null && friendList.size() > 0){
						player = GamePlayerManager.getInstance().getPlayer(p.random.nextInt(friendList.size()));
					}
				}
				if(player == null){			//好友列表没有查仇人
					List<Long> chourenList =relation.getChourenlist();
					out.println("ff11=======" + chourenList);
					if(chourenList != null && chourenList.size() > 0){
						player = GamePlayerManager.getInstance().getPlayer(p.random.nextInt(chourenList.size()));
					}
				}
				out.println("^^^^^^^^^^^^^^^^^^");
				if(player == null){			//仇人列表都没有查排行榜----------------
					out.println("===========asdf======" +  BillboardsManager.getInstance());
					out.println("==============asdfas===" +  BillboardsManager.getInstance().getBillboard("等级", "全部"));
					Billboard bb = BillboardsManager.getInstance().getBillboard("等级", "全部");
					BillboardDate[] datas = bb.getDates();
					String[] str = datas[0].getDateValues();
					for(int i=0; i<str.length; i++){
						out.println("=======ss==" + str[i]);
					}
					player = GamePlayerManager.getInstance().getPlayer(str[0]);
					out.println(player.getName());
				}
			} catch(Exception e222){
				out.print(e222);
			}
		}else if("shuaboss".equals(action)){
			Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
			if (p == null) {
				out.println("玩家是null");
				return;
			}
			Game game = p.getCurrentGame();
			int[][] refreashPoint = new int[][]{{100,0},{0,100},{-100,0},{0,-100}};
			try{
				Sprite sprite = null;
				sprite = MemoryMonsterManager.getMonsterManager().createMonster(900005);
				int xx = Integer.parseInt(level) ;
				if(sprite != null){
					sprite.setX(p.getX()+refreashPoint[xx][0] );
					sprite.setY(p.getY()+refreashPoint[xx][1]);
					sprite.setBornPoint(new Point(sprite.getX(), sprite.getY()));
					out.println(sprite.getParticleName());
					game.addSprite(sprite);
					out.println("x===" + sprite.getX() + "===y=" + sprite.getY());
					out.println("x1===" + p.getX() + "===y1=" + p.getY());
				} else {
					out.println("怪物没创建好");
				}
			}catch(Exception e333){
				out.println("出现异常了！！！"+e333);
			}
			out.println("====");
		} else if("countdown".equals(action)){
			Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
			if (p == null) {
				out.println("玩家是null");
				return;
			}
			NOTIFY_ROBBERY_COUNTDOWN_REQ resp = new NOTIFY_ROBBERY_COUNTDOWN_REQ();
			resp.setCountType(Byte.parseByte(level));
			resp.setLeftTime(150);
			resp.setContentmass("dd");
			p.addMessageToRightBag(resp);
			out.println("发送成功");
		}else if("addtitle".equals(action)){
		}else if("yuanshenqiehuan".equals(action)){
			Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
			if (p == null) {
				out.println("玩家是null");
				return;
			}
			p.switchSoul(Byte.parseByte(level), true);
			out.println("=========");
		}else if("yuanshenqiehuan111".equals(action)){
			out.println("修改渡劫开启时间，修改前时间为：" + RobberyConstant.OPENTIME);
			RobberyConstant.OPENTIME = Timestamp.valueOf(level);
			out.println("时间修改成功，修改后开启时间为" + RobberyConstant.OPENTIME);
		} else if("dujiefenying".equals(action)){
			out.println("修改渡劫封印等级，当前封印等级为：" + RobberyConstant.MAXLEVEL);
			RobberyConstant.MAXLEVEL = Integer.parseInt(playerId);
			out.println("修改渡劫封印等级成功，修改后玩家渡劫最大层数为" + RobberyConstant.MAXLEVEL);
		} else if("qianzhijinru".equals(action)){
			out.println("原来强制进入时间为：" + RobberyConstant.ONE_DAY_SECOND);
			RobberyConstant.ONE_DAY_SECOND = Integer.parseInt(playerId);
			out.println("修改后强制拉入时间为" + RobberyConstant.ONE_DAY_SECOND);
		} else if("buffceshi".equals(action)){
			Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
			if (p == null) {
				out.println("玩家是null");
				return;
			}
			boolean result = TransitRobberyEntityManager.getInstance().isPlayerInRobbery(Long.parseLong(playerId));
			out.println("玩家是否正在渡劫状态=:" + result);
			Game g = TransitRobberyManager.getInstance().getRobberyGame(p, RobberyConstant.DUJIEMAP);
			out.println("渡劫线程中是否存在玩家渡劫线程：" + g!=null);
		}
	%>
</body>
</html>
