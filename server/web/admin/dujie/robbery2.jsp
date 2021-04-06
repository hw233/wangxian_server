<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page import="com.fy.engineserver.datasource.skill.SkillInfoHelper"%>
<%@page import="com.fy.engineserver.activity.loginActivity.ActivityManagers"%>
<%@page import="com.fy.engineserver.activity.shop.UseStat"%>
<%@page import="com.fy.engineserver.chat.ChatMessage"%>
<%@page import="com.fy.engineserver.chat.ChatMessageService"%>
<%@page import="com.xuanzhi.tools.cache.LruMapCache"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.newtask.NewDeliverTaskManager"%>
<%@page import="com.fy.engineserver.newtask.NewDeliverTask"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="com.fy.engineserver.newtask.service.DeliverTaskManager"%>
<%@page import="com.fy.engineserver.newtask.DeliverTaskTestCase"%>
<%@page import="com.fy.engineserver.newtask.DeliverTask"%>
<%@page import="com.fy.engineserver.newtask.TaskEntityManager"%>
<%@page import="com.fy.engineserver.newtask.TaskEntity"%>
<%@page import="com.fy.engineserver.newBillboard.IBillboardPlayerInfo"%>
<%@page import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.skill.master.SkEnhanceManager"%>
<%@page import="com.fy.engineserver.activity.TransitRobbery.Robbery.BaseRobbery"%>
<%@page import="com.fy.engineserver.activity.TransitRobbery.RobberyThread"%>
<%@page import="java.lang.reflect.Field"%>
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
		action = action == null ? "" : action;
		playerId = playerId == null ? "" :playerId;
		
		if("checkDataT".equals(action)) {
			long totalNum_t1 = NewDeliverTaskManager.getInstance().em.count(NewDeliverTask.class, "");
			long totalNum_t2 = DeliverTaskManager.getInstance().em.count(DeliverTask.class, "");
			long totalNum_t3 = DeliverTaskManager.getInstance().em.count(DeliverTask.class, " deliverTimes>? ", new Object[]{-300});
			long totalNum_t4 = TaskEntityManager.em.count(TaskEntity.class, "");
			out.println("===目前总数:" + totalNum_t1 + "<br>");
			out.println("===目前总数delivertask总数:" + totalNum_t2 + "<br>");
			out.println("===目前总数delivertask中不可删除总数:" + totalNum_t3 + "<br>"); 
			out.println("===taskentity目前总数4:" + totalNum_t4 + "<br>"); 
		}
		if("checkXueMai".equals(action)) {
			long maxXuemai = 0;
			int xuemaiIndex = 0;
			long totalXuemai = 0;
			Player[] pls = GamePlayerManager.getInstance().getOnlinePlayers();
			for(Player per : pls) {
				PetsOfPlayer bean0 = Pet2Manager.getInst().findPetsOfPlayer(per);
				if(bean0 != null) {
					out.println("["+(xuemaiIndex++)+"] [" + per.getLogString() + "] [血脉:" + bean0.getXueMai() + "]<br>");
					totalXuemai += bean0.getXueMai();
					if(maxXuemai < bean0.getXueMai()) {
						maxXuemai = bean0.getXueMai();
					}
				}
			}
			out.println("***************总血脉值[" + totalXuemai + "]*****************<br>");
			out.println("***************平均值[" + totalXuemai/xuemaiIndex + "]*****************<br>");
			out.println("***************最大值[" + maxXuemai + "]*****************<br>");
		}
		if("cleanDeadPeople".equals(action)) {
			Field ff = TransitRobberyEntityManager.class.getDeclaredField("robberingPlayer");
			ff.setAccessible(true);
			Set<Long> arr = (Set<Long>)ff.get(TransitRobberyEntityManager.getInstance());
			out.println("渡劫人数:" + arr.size() + "<br>");
			int index0 = 0;
			Set<Long> tempSet = new HashSet<Long>();
			if(arr!=null && arr.size()>0) {
				for(long pid : arr) {
					if(!GamePlayerManager.getInstance().isOnline(pid)) {
						//TransitRobberyEntityManager.getInstance().removeFromRobbering(pid);
						tempSet.add(pid);
					}
				}
				for(long pid2 : tempSet) {
						TransitRobberyEntityManager.getInstance().removeFromRobbering(pid2);
						out.println("["+(index0++)+"] [清除渡劫状态:" + pid2 + "]<br>");
					}
				}
			out.println("渡劫人数:" + arr.size() + "<br>");
			}
	%>
	<form action="robbery2.jsp" method="get">
		<input type="hidden" name="action" value="enterRobbery" />密码：<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="启动修复线程" />
	</form>
	<br />
	<form action="robbery2.jsp" method="get">
		<input type="hidden" name="action" value="sqltest" />
		<input type="submit" value="测试" />
	</form>
	<br />
	<form action="robbery2.jsp" method="get">
		<input type="hidden" name="action" value="chakanrenwu" /> 玩家ID<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="查看任务" />
	</form>
	<br />
	<form action="robbery2.jsp" method="get">
		<input type="hidden" name="action" value="chakanrenwu2" /> 玩家ID<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="查看任务222" />
	</form>
	<br />
	<form action="robbery2.jsp" method="get">
		<input type="hidden" name="action" value="chakanrenwu3" /> 玩家ID<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="得到前置任务组" />
	</form>
	<br />
	<form action="robbery2.jsp" method="get">
		<input type="hidden" name="action" value="chakanrenwu4" /> 玩家ID<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="得到前置任务组1" />
	</form>
	<br />
	
	<%
	if("chakanrenwu2".equals(action)) {
		int num = 2000;
		int index = 0;
		long playerId11 = 1319000000000045398L;
		for(int i=1001; i<num; i++) {
			long totalNum = NewDeliverTaskManager.getInstance().em.count(NewDeliverTask.class, "playerId=" + playerId11 + " and taskId = " + i + "");
			if(totalNum > 1) {
				//out.println("["+(index++)+"] [任务id: " + i + " ] [ " + totalNum + " ]<BR>");
				List<NewDeliverTask> list = NewDeliverTaskManager.getInstance().em.query(NewDeliverTask.class, "playerId=? and taskId = ?", new Object[]{playerId11,i},"", 1, 300);
				out.println("[任务id: " + i + " ][任务数量：" + list.size() + "]<BR>");
				if(list != null && list.size() > 0) {
					if(list.size() > 1) {
						for(int j=1; j<list.size(); j++) {
							NewDeliverTaskManager.getInstance().em.remove(list.get(j));
							//out.println(list.get(i).getId() + "<BR>");
						}
						long totalNum2 = NewDeliverTaskManager.getInstance().em.count(NewDeliverTask.class, "playerId=" + playerId11 + " and taskId = " + i + "");
						long totalNum_t = NewDeliverTaskManager.getInstance().em.count(NewDeliverTask.class, "playerId=" + playerId11 + " ");
						out.println("剩余此任务数量：" + totalNum2 + "<BR>[任务id:" + i +" ]");
						out.println("剩余任务数量：" + totalNum_t + "<BR>");
						Thread.sleep(3000);
					}
				}
			}
		}
	}else if("chakanrenwu3".equals(action)) {
		List<NewDeliverTask> list = NewDeliverTaskManager.getInstance().em.query(NewDeliverTask.class, "playerId=? and taskId = ?", new Object[]{1319000000000049195L,1926},"", 1, 252);
		out.println("任务数量：" + list.size() + "<BR>");
		if(list != null && list.size() > 0) {
				if(list.size() > 1) {
					for(int i=1; i<list.size(); i++) {
						NewDeliverTaskManager.getInstance().em.remove(list.get(i));
						//out.println(list.get(i).getId() + "<BR>");
					}
				}
		}
		long totalNum = NewDeliverTaskManager.getInstance().em.count(NewDeliverTask.class, "playerId=" + 1319000000000049195L + " and taskId = 1926");
		long totalNum_t = NewDeliverTaskManager.getInstance().em.count(NewDeliverTask.class, "playerId=" + 1319000000000049195L + " ");
		out.println("剩余此任务数量：" + totalNum + "<BR>[任务id:" + "1926 ]");
		out.println("剩余任务数量：" + totalNum_t + "<BR>");
		
	} else if("chakanrenwu4".equals(action)) {
		Player[] players = GamePlayerManager.getInstance().getOnlinePlayers();
		int num = players.length;
		int index = 0;
		for(int i=0; i<num; i++) {
			if(players[i].getSoulLevel() >= 100) {
				long totalNum = NewDeliverTaskManager.getInstance().em.count(NewDeliverTask.class, "playerId=" + players[i].getId() + " ");
				if(totalNum >= 1500) {
					out.println("["+(index++)+"][" + players[i].getLogString() + "][" + totalNum + "]<BR>");
				}
			}
		}
	}else if("chakanrenwu".equals(action)) {
			long playerId1 = Long.parseLong(playerId);
			String where = "playerId = ? ";
			List<TaskEntity> dbList = TaskEntityManager.em.query(TaskEntity.class, where,new Object[] { playerId1}, "id desc", 1, 9999);
			TaskManager taskManager = TaskManager.getInstance();
			//for(TaskEntity te : dbList) {
			//	Task task = taskManager.getTask(te.getTaskId());
			//	if(te.getStatus() == 3 && task.getType() != 1) {
			//		out.println("===============删除数据：" + te);
			//		TaskEntityManager.getInstance().notifyDeleteFromCache(te);
			//	}
			//	out.println("<br>taskentity中的数据：" + te );
			//	out.println("<br>显示类型：" + te.getShowType() + "===类型：");
			//}		
			out.println("taskentity中数据总共有：" + dbList.size());
			out.println("<br>");
			SimpleEntityManager<DeliverTask> em = SimpleEntityManagerFactory.getSimpleEntityManager(DeliverTask.class);
			List<DeliverTask> list = em.query(DeliverTask.class, "playerId = ? and deliverTimes > ?", new Object[]{playerId1,-300},"", 1, 2000);
			for(DeliverTask te : list) {
				out.println("<br>delivertask中的数据：" + te);
			}
			out.println("delivertask中数据总共有·：" + list.size());
			
			long totalNum = NewDeliverTaskManager.getInstance().em.count(NewDeliverTask.class, "playerId=" + playerId1 + " ");//	query(NewDeliverTask.class, "playerId=?", new Object[]{playerId1},"", 1, 1500);
			long totalNum_1 = NewDeliverTaskManager.getInstance().em.count(NewDeliverTask.class, " id<0 ");//	query(NewDeliverTask.class, "playerId=?", new Object[]{playerId1},"", 1, 1500);
			List<NewDeliverTask> list_1 =  NewDeliverTaskManager.getInstance().em.query(NewDeliverTask.class, "id<1 ", null,"", 1, 1500);
			List<NewDeliverTask> list2 = NewDeliverTaskManager.getInstance().em.query(NewDeliverTask.class, "playerId=?", new Object[]{playerId1},"", 1, 1500);
			int index = 0;
			for(NewDeliverTask ndt : list2) {
				out.println("[" + (++index) + "]" + ndt.toString() + "<BR/>");
			}
			out.println("newdelivertask中数据：" + list2.size() + "-----count:" + totalNum + "[" + totalNum_1 + "]" + "<BR/>");
			index = 0;
			for(NewDeliverTask ndt : list_1) {
				out.println("[id异常] [" + (++index) + "]<font color=red>" + ndt.toString() + "</font><BR/>");
			}
			
			try{
			Player p = GamePlayerManager.getInstance().getPlayer(playerId1);
			HashMap<Long, NewDeliverTask> al = p.newdeliverTaskMap;
			for(Long l : al.keySet()){
				out.println("|-------------------:" + l + ":" + al.get(l).isDeliver());
			}
			}catch(Exception e) {
				
			}
			
		}else if("sqltest".equals(action)) {
			SimpleEntityManager<TransitRobberyEntity> em = SimpleEntityManagerFactory.getSimpleEntityManager(TransitRobberyEntity.class);
			long[] ids = em.queryIds(TransitRobberyEntity.class, "currentLevel=?", new Object[] {1}, "passLayer desc", 1, BillboardsManager.实际条数 + 1);
			TransitRobberyEntity entity = null;
			for(int i=0; i<ids.length; i++) {
				entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(ids[i]);
				out.print("id=" + ids[i]);
				if(entity != null) {
					out.println("、currentLevel=" + entity.getCurrentLevel());
				} else {
					out.println("==========");
				}
			}
		}else if("enterRobbery".equals(action) && "dujiexitongxiufu123".equals(playerId)) {
			if(SkEnhanceManager.translation.containsKey("dujiexitongxiufu123")) {
				out.println("渡劫修复线程已经启动！");
				return;
			}
			SkEnhanceManager.translation.put("dujiexitongxiufu123", "dujiexitongxiufu123");
			Runnable r = new Runnable(){
				public void run(){
					while(true) {
						try{
							Field ff = TransitRobberyManager.class.getDeclaredField("robberyThreads");
							ff.setAccessible(true);
							RobberyThread[] arr = (RobberyThread[])ff.get(TransitRobberyManager.getInstance());
							Object[] allPlayer = GamePlayerManager.getInstance().getAllPlayerInCache();
							for(Object o : allPlayer) {
								Player p = (Player)o;
								boolean exist = false;
								if(p.getLevel() < 110 || !TransitRobberyEntityManager.getInstance().isPlayerInRobbery(p.getId())) {
									continue;
								}
								for(int i=0; i<arr.length; i++) {
									for(BaseRobbery b : arr[i].allRobbery) {
										if(b.playerId == p.getId()) {
											exist = true;
											break;
										}
									}
									if(exist) {
										break;
									}
								}
								if(!exist) {
									TransitRobberyEntityManager.getInstance().removeFromRobbering(p.getId());
									try{
										p.getCurrentGame().transferGame(p, new TransportData(0, 0, 0, 0, p.getResurrectionMapName(), p.getResurrectionX(), p.getResurrectionY()), true);
									}catch(Exception e){
										TransitRobberyManager.logger.error("[后台线程出错]{}",e);
									}
									TransitRobberyManager.logger.warn("已经修正完毕，player{}", p.getLogString());
								}
							}
							
						}catch(Exception e) {
							TransitRobberyManager.logger.error("[后台线程出错2]{}",e);
						}
						try{
							Thread.sleep(3000);
						}catch(Exception e) {
							TransitRobberyManager.logger.error("[后台线程出错3]{}",e);
						}
					}
				}
			};
			new Thread(r,"后台渡劫修复线程").start();
		}
	%>
</body>
</html>
