<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager"%>
<%@page import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.datasource.skill.Skill"%>
<%@page import="com.fy.engineserver.util.StringTool"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithRange"%>
<%@page import="java.util.Calendar"%>
<%@page import="org.apache.commons.lang.ArrayUtils"%>
<%@page import="com.fy.engineserver.util.server.TestServerConfigManager"%>
<%@page import="com.fy.engineserver.event.cate.EventWithObjParam"%>
<%@page import="com.fy.engineserver.event.Event"%>
<%@page import="com.fy.engineserver.event.EventRouter"%>
<%@page import="com.fy.engineserver.datasource.skill.SkillInfoHelper"%>
<%@page import="com.fy.engineserver.datasource.skill.ActiveSkill"%>
<%@page import="com.fy.engineserver.datasource.career.Career"%>
<%@page import="com.fy.engineserver.datasource.career.CareerManager"%>
<%@page import="com.fy.engineserver.message.SkEnh_exINFO_RES"%>
<%@page import="com.fy.engineserver.message.SkEnh_exINFO_REQ"%>
<%@page import="com.fy.engineserver.datasource.skill.master.SkBean"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.engineserver.datasource.skill.master.SkEnConf"%>
<%@page import="com.fy.engineserver.message.SkEnh_Exp2point_RES"%>
<%@page import="com.fy.engineserver.message.SkEnh_Exp2point_REQ"%>
<%@page import="com.fy.engineserver.message.SkEnh_Add_point_RES"%>
<%@page import="com.fy.engineserver.message.SkEnh_Add_point_REQ"%>
<%@page import="com.fy.engineserver.datasource.skill.master.ExchangeConf"%>
<%@page import="com.fy.engineserver.message.GameMessageFactory"%>
<%@page import="com.fy.engineserver.message.SkEnh_INFO_REQ"%>
<%@page import="com.fy.engineserver.message.SkEnh_INFO_RES"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.datasource.skill.master.SkEnhanceManager"%>
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
//SkEnhanceManager.debugRateAdd = 100;
if(SkEnhanceManager.debugRateAdd>0){
	out.println("<h2>调试模式，触发几率百分百！</h2>");
}
//out.println("数据库条目数量:"+SkEnhanceManager.getInst().em.count());
//  SkEnhanceManager.getInst().translation.put("targetPlus","(大师技能：目标+{0}");
	String action = request.getParameter("action");
	String playerId = request.getParameter("playerId");
	String playerName = request.getParameter("playerName");
	if(playerName == null){
		playerName = session.getAttribute("playerName")+"";
	}
	//
	playerId = playerId == null ? "" : playerId;
	if(playerId.isEmpty()){
		playerId = String.valueOf(session.getAttribute("playerid"));
	}
	playerId = playerId == null ? "" : playerId;
	int forceCareer = 0;
%>
<h2>大师技能后台</h2>   <a href='/admin/playersonline.jsp?bean=sprite_sub_system'>在线玩家列表</a>

<form action="skMaster.jsp"  method="get">
<input type="hidden" name="action" value="queryPlayerSk"/>
玩家ID<input type="text" name="playerId" value="<%=playerId%>"/>
<input type="submit" value="查询该玩家大师技能信息"/>
</form>
<form action="skMaster.jsp"  method="get">
<input type="hidden" name="action" value="queryPlayerSk"/>
玩家名字<input type="text" name="playerName" value="<%=playerName%>"/>
<input type="submit" value="查询该玩家大师技能信息"/>
</form>
<a href='skMaster.jsp?action=showConf'>显示大师技能配置</a> | 
<a href='skMaster.jsp?action=showTranslation'>显示翻译</a> | 
<a href='skMaster.jsp?action=reloadConf'>重新加载配置文件</a> | 
<br/>

<%
String allpoints = request.getParameter("allpoints");
if(allpoints!=null){
	String pid = request.getParameter("pid");
	if(pid!=null){
		Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(pid));
		if(p!=null){
			SkBean bean = SkEnhanceManager.getInst().findSkBean(p);
			if(bean!=null){
				int ponits []= StringTool.string2IntArr(allpoints, ",");
				byte ps [] = new byte[ponits.length];
				for (int i = 0; i < ponits.length; i++) {
					ps[i] = (byte) (ponits[i]);
				}
				bean.setLevels(ps);
				out.print("<font color='red'>[设置成功] ["+p.getName()+"] [新："+Arrays.toString(bean.getLevels())+"]</font><br>");
				Skill.logger.warn("[后台设置玩家大使技能点数] [成功] ["+p.getName()+"] [新："+Arrays.toString(bean.getLevels())+"]");
			}else{
				out.print("设置总的加点数失败，还没开启大师技能："+pid);
			}
		}else{
			out.print("设置总的加点数失败，玩家不存在："+pid);
		}
	}
	
}	

String allpoints2 = request.getParameter("allpoints2");
if (allpoints2 != null) {
		String pid = request.getParameter("pid");
		if (pid != null) {
			Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(pid));
			if (p != null) {
				SkBean bean = SkEnhanceManager.getInst().findSkBean(p);
				if (bean != null) {
					int ponits[] = StringTool.string2IntArr(allpoints2, ",");
					byte ps[] = new byte[ponits.length];
					for (int i = 0; i < ponits.length; i++) {
						ps[i] = (byte) (ponits[i]);
					}
					bean.setSoulLevels(ps);
					out.print("<font color='red'>[设置成功] [" + p.getName() + "] [新：" + Arrays.toString(bean.getLevels()) + "]</font><br>");
					Skill.logger.warn("[后台设置玩家大师技能点数] [成功] [" + p.getName() + "] [新：" + Arrays.toString(bean.getSoulLevels()) + "]");
				} else {
					out.print("设置总的加点数失败，还没开启大师技能：" + pid);
				}
			} else {
				out.print("设置总的加点数失败，玩家不存在：" + pid);
			}
		}
}

String allpoints3 = request.getParameter("allpoints3");
if (allpoints3 != null) {
	if(allpoints3.equals("modifySkill3")==true){
		String pid = request.getParameter("pid");
		if (pid != null) {
			Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(pid));
			if (p != null) {
				SkBean bean = SkEnhanceManager.getInst().findSkBean(p);
				if(bean!=null){
					int c = p.getCareer() - 1;
					byte ponits []= bean.getLevels();
					SkEnConf[] conf = SkEnConf.conf[c];
					int allponits = 0;
					for (int j = 0; j < 12; j++) {
						SkEnConf skC = conf[j];
						if (skC != null && ponits[j] >= 1) {
							for (int k = 0; k < ponits[j]; k++) {
								allponits += skC.levelNeedPoint[k];
							}
						}
					}
					
					TransitRobberyEntity entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(p.getId());
					byte[] oldsk = bean.getLevels();
					if (entity != null) {
						bean.setPoint(bean.getPoint() + allponits);
						bean.setLevels(new byte[20]);
						Arrays.fill(bean.getLevels(), (byte) -1);
						
						int dujielevel = entity.getCurrentLevel(); // 渡劫层数(1,3,5,6,7,8)影响大师技能的开启
						if (dujielevel == 2) {
							dujielevel = 1;
						} else if (dujielevel == 4) {
							dujielevel = 3;
						}
						for (int i = 1; i <= dujielevel; i++) {
							SkEnhanceManager.getInst().duJie(p.getId(), i);
						}
					}
					
					out.print("<font color='red'>[设置成功] ["+p.getName()+"] [返还点："+allponits+"] [总点："+bean.getPoint()+"] [老的加点情况:"+Arrays.toString(oldsk)+"] [新："+Arrays.toString(bean.getLevels())+"]</font><br>");
					Skill.logger.warn("[后台清空本尊大师技能点数操作失败] [成功] ["+p.getName()+"] [返还点："+allponits+"] [总点："+bean.getPoint()+"] [老的加点情况:"+Arrays.toString(oldsk)+"] [新："+Arrays.toString(bean.getLevels())+"]");
				}else{
					out.print("清空本尊大师技能点数操作失败，还没开启大师技能："+pid);
				}
			} else {
				out.print("清空本尊大师技能点数操作失败，玩家不存在：" + pid);
			}
		}
	}else{
		out.print("密码不对");
	}
}

String allpoints4 = request.getParameter("allpoints4");
if (allpoints4 != null) {
	if(allpoints4.equals("modifySkill33")==true){
		String pid = request.getParameter("pid");
		if (pid != null) {
			Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(pid));
			if (p != null) {
				SkBean bean = SkEnhanceManager.getInst().findSkBean(p);
				if(bean!=null){
					int c = p.getSoul(Soul.SOUL_TYPE_SOUL).getCareer() - 1;
					byte ponits []= bean.getSoulLevels();
					SkEnConf[] conf = SkEnConf.conf[c];
					int allponits = 0;
					for (int j = 0; j < 12; j++) {
						SkEnConf skC = conf[j];
						if (skC != null && ponits[j] >= 1) {
							for (int k = 0; k < ponits[j]; k++) {
								allponits += skC.levelNeedPoint[k];
							}
						}
					}
					
					TransitRobberyEntity entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(p.getId());
					byte[] oldsk = bean.getSoulLevels();
					if (entity != null) {
						bean.setPoint(bean.getPoint() + allponits);
						bean.setSoulLevels(new byte[20]);
						Arrays.fill(bean.getSoulLevels(), (byte) -1);
						
						int dujielevel = entity.getCurrentLevel(); // 渡劫层数(1,3,5,6,7,8)影响大师技能的开启
						if (dujielevel == 2) {
							dujielevel = 1;
						} else if (dujielevel == 4) {
							dujielevel = 3;
						}
						for (int i = 1; i <= dujielevel; i++) {
							SkEnhanceManager.getInst().duJie(p.getId(), i);
						}
					}
					
					out.print("<font color='red'>[设置成功] ["+p.getName()+"] [返还点："+allponits+"] [总点："+bean.getPoint()+"] [老的加点情况:"+Arrays.toString(oldsk)+"] [新："+Arrays.toString(bean.getLevels())+"]</font><br>");
					Skill.logger.warn("[后台清空元神大师技能点数操作失败] [成功] ["+p.getName()+"] [返还点："+allponits+"] [总点："+bean.getPoint()+"] [老的加点情况:"+Arrays.toString(oldsk)+"] [新："+Arrays.toString(bean.getLevels())+"]");
				}else{
					out.print("清空元神大师技能点数操作失败，还没开启大师技能："+pid);
				}
			} else {
				out.print("清空元神大师技能点数操作失败，玩家不存在：" + pid);
			}
		}
	}else{
		out.print("密码不对");
	}
}
	

if("queryPlayerSk".equals(action)){
	do{
		Player p = null;
		if(playerId.isEmpty()){
			if(playerName.length()>0){
				p = PlayerManager.getInstance().getPlayer(playerName);
			}
			if(p == null){
				out.println("请指定玩家id");
			}
			break;
		}
		p = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
		if(p == null){
			out.println("玩家是null");
			break;
		}
		session.setAttribute("playerName", p.getName());
		session.setAttribute("playerid", playerId);
		SkEnh_INFO_REQ req = new SkEnh_INFO_REQ(GameMessageFactory.nextSequnceNum());
		SkEnh_INFO_RES ret = SkEnhanceManager.getInst().sendInfo(p, req);
		if(ret == null){
			out.println("查询返回NULL");
			break;
		}
		SkEnh_exINFO_REQ exR = new SkEnh_exINFO_REQ(GameMessageFactory.nextSequnceNum());
		SkEnh_exINFO_RES exRes = SkEnhanceManager.getInst().sendExInfo(p, exR);
		SkBean bean = SkEnhanceManager.getInst().findSkBean(p);
		for(int i=1; i<= 8; i++){
			out.println("<a href='skMaster.jsp?action=duJieEvent&duJieLv="+i+"&playerId="+playerId+"'>触发渡劫 "+i+" </a> |");
		}
		StringBuffer sb = new StringBuffer();
		if(bean!=null && bean.useLevels!=null){
			for(byte b:bean.useLevels){
				sb.append(b).append(",");
			}
		}
		
		StringBuffer sb2 = new StringBuffer();
		if (bean != null && bean.getSoulLevels() != null) {
			for (byte b : bean.getSoulLevels()) {
				sb2.append(b).append(",");
			}
		}
		out.println("<br/>玩家名字："+p.getName()+"<br/>");
		out.println("玩家ID："+p.getId()+"<br/>");
		out.println("现有经验："+p.getExp()+"<br/>"); 
		out.println("总的加点数："+bean.sumAddPoint+"<br/>");
		out.println("<form>本尊技能依次加点情况：<input type='hidden' name='pid' value='"+playerId+"'><input type='text' name='allpoints' size='50' value="+(sb.toString())+"><input type='submit' value='设置'></form><br/>");
		out.println("<form>元神技能依次加点情况：<input type='hidden' name='pid' value='" + playerId + "'><input type='text' name='allpoints2' size='50' value=" + (sb2.toString()) + "><input type='submit' value='设置'></form><br/>");
		out.println("<form>清空本尊加点和返回点数：<input type='hidden' name='pid' value='" + playerId + "'><input type='text' name='allpoints3' size='20' value="+"请输入密码"+"><input type='submit' value='设置'></form><br/>");
		out.println("<form>清空元神加点和返回点数：<input type='hidden' name='pid' value='" + playerId + "'><input type='text' name='allpoints4' size='20' value="+"请输入密码"+"><input type='submit' value='设置'></form><br/>");
		out.println("拥有点数："+ret.getPoint()+" - <a href='skMaster.jsp?action=setPoint&playerId="+playerId+"'>设为100000</a><br/>");
		out.println("已兑换次数："+exRes.getExTimes()+" - <a href='skMaster.jsp?action=clearExTimes&playerId="+playerId+"'>清除</a><br/>");
		out.println("上次兑换在:"+bean.lastExchangeDay+"  今天是:"+Calendar.getInstance().get(Calendar.DAY_OF_YEAR)+"<br/>");
		out.println("<a href='skMaster.jsp?action=learnAllSk&playerId="+playerId+"'>学会所有技能</a><br/>");
		out.println("加点信息:<br/>");
		int career = p.getCareer();
		career = forceCareer>0 ? forceCareer : career;
		Career c = CareerManager.getInstance().getCareer(career);
		int len = ret.getLevels().length;
		out.println("<table border='1'>");
		out.print("<tr><td>i</td><td>技能名称</td><td>技能等级</td><td>本尊</td><td>元神</td><td>类</td></tr>");
		for(int i=0; i<12; i++){
			ActiveSkill as = (ActiveSkill)c.getThreads()[0].skills[i];
			out.print("<tr>");
			out.print("<td>");
			out.println(i+"</td><td>"+as.getName()+":"+as.getBuffName()+"</td><td>"+p.getSkillLevel(as.getId())+"</td><td>"+bean.getLevels()[i]
					+" <a href='skMaster.jsp?action=addPoint&playerId="+playerId+"&index="+i+"'>加点</a>"+
					" - <a href='skMaster.jsp?action=showSkDesc&index="+i+"&playerId="+playerId+"'>查看技能描述</a>");
			out.print("</td>");
			out.print("<td>");
			out.println(bean.getSoulLevels()[i]);
			out.print(" - 点前面的加点，会自动判断元神");
			out.print("</td>");
			String tgt = "";
			if(as.getClass() == SkillWithoutTraceAndWithRange.class){
				tgt = ArrayUtils.toString(((SkillWithoutTraceAndWithRange)as).getMaxAttackNums());
			}
			out.print("<td>"+as.getClass().getSimpleName()+":"+tgt+"</td>");
			out.print("</tr>");
		}
		out.print("</table>");
		out.println("经验兑换技能点配置:<br/>");
		len = exRes.getExConf().length;
		for(int i=0; i<len ;i++){
			ExchangeConf conf = exRes.getExConf()[i];
			out.println("第"+conf.getStartTimes()+" ~ "+conf.getEndTimes()+ " 次 : 需经验 "
			+conf.getNeedExp()
			+" <a href='skMaster.jsp?action=exchange&playerId="+playerId+"&times=1'>兑换</a>"
			+"<br/>");
		}
	}while(false);
	{//调试技能描述。 
	//	SkillInfo si = new SkillInfo();
	//	Skill skill = CareerManager.getInstance().getCareer(p.getCareer()).threads[0].skills[index];
	}
}else if("reloadConf".equals(action)){
	String rf = Pet2Manager.getInst().getFile();
	SkEnhanceManager.getInst().loadConf(rf);
	out.println("加载完毕。");
}else if("learnAllSk".equals(action)){
	if(TestServerConfigManager.isTestServer()){
		Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
		byte[] arr = p.getSkillOneLevels();
		for(int i=0; i<arr.length; i++){
			if(arr[i]<=0){
				arr[i] = 1;
			}
		}
//		SkEnhanceManager.debugRateAdd = 200;
//		out.print("已开启调试模式，斗罗几率很高！<br/>");
	}else{
		out.print("不是测试服务器，不能操作<br/>");
	}
	out.println("执行成功 <a href=skMaster.jsp?action=queryPlayerSk&playerId="+playerId+">返回</a>");
}else if("setPoint".equals(action)){
	if(TestServerConfigManager.isTestServer()){
		Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
		SkBean bean = SkEnhanceManager.getInst().findSkBean(p);
		bean.setPoint(100000);
//		SkEnhanceManager.debugRateAdd = 200;
//		out.print("已开启调试模式，斗罗几率很高！<br/>");
	}else{
		out.print("不是测试服务器，不能操作<br/>");
	}
	out.println("执行成功 <a href=skMaster.jsp?action=queryPlayerSk&playerId="+playerId+">返回</a>");
}else if("duJieEvent".equals(action)){
	Object[] param = new Object[]{Long.valueOf(playerId), Integer.parseInt(request.getParameter("duJieLv"))};
	EventRouter.getInst().addEvent(new EventWithObjParam(Event.PLAYER_DU_JIE, param));
	out.println("执行成功 <a href=skMaster.jsp?action=queryPlayerSk&playerId="+playerId+">返回</a>");
}else if("addPoint".equals(action)){
	do{
		if(playerId.isEmpty()){
			out.println("请指定玩家id");
			break;
		}
		Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
		if(p == null){
			out.println("玩家是null");
			break;
		}
		SkEnh_Add_point_REQ req = new SkEnh_Add_point_REQ(1, Integer.parseInt(request.getParameter("index")));
		SkEnh_Add_point_RES ret = SkEnhanceManager.getInst().addPoint(p, req);
		if(ret == null){
			out.println("加点返回NULL");
			break;
		}
		out.println("执行成功 <a href=skMaster.jsp?action=queryPlayerSk&playerId="+playerId+">返回</a>");
	}while(false);
}else if("exchange".equals(action)){
	do{
		if(playerId.isEmpty()){
			out.println("请指定玩家id");
			break;
		}
		Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
		if(p == null){
			out.println("玩家是null");
			break;
		}
		SkEnh_Exp2point_REQ req = new SkEnh_Exp2point_REQ(1, Integer.parseInt(request.getParameter("times")));
		SkEnh_Exp2point_RES ret = SkEnhanceManager.getInst().exchange(p, req);
		if(ret == null){
			out.println("兑换返回NULL");
			break;
		}
		out.println("执行成功 <a href=skMaster.jsp?action=queryPlayerSk&playerId="+playerId+">返回</a>");
	}while(false);
}else if("clearExTimes".equals(action)){
	Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
	SkBean bean = SkEnhanceManager.getInst().findSkBean(p);
	bean.setExchangeTimes(0);
	out.println("执行成功 <a href=skMaster.jsp?action=queryPlayerSk&playerId="+playerId+">返回</a>");
}else if("showSkDesc".equals(action)){
	int idx = Integer.parseInt(request.getParameter("index"));
	Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
	int career = p.getCareer();
	career = forceCareer>0 ? forceCareer : career;
	Career c = CareerManager.getInstance().getCareer(career);
	ActiveSkill as = (ActiveSkill)c.getThreads()[0].skills[idx];
	out.println("技能ID:"+as.getId()+"  pageIndex:"+as.pageIndex+" step:"+SkEnhanceManager.getInst().getSlotStep(p, as.pageIndex)+" <br/>");
	SkEnConf cc = SkEnConf.conf[career - 1][as.pageIndex];
	out.println(ArrayUtils.toString(cc.desc)+"<br/><br/>");
	String str = SkillInfoHelper.generate(p, as).replaceAll("\\n", "<br/>");
	out.println(str+"<br/>");
	str = SkillInfoHelper.appendBuffDesc(p, 1, as, "---$bu$");
	out.println(str+"<br/>");
	out.println("执行成功 <a href=skMaster.jsp?action=queryPlayerSk&playerId="+playerId+">返回</a>");
}else if("showTranslation".equals(action)){
	out.println("翻译信息<br/>");
	Iterator<String> it = SkEnhanceManager.translation.keySet().iterator();
	while(it.hasNext()){
		String key = it.next();
		out.println(key+"-->"+SkEnhanceManager.translation.get(key)+"$<br/>");
	}
	out.println("完毕");
}else if("showConf".equals(action)){
	do{
		SkEnConf[][] arr = SkEnConf.conf;
		if(arr == null){
			out.print("配置是null");
			break;
		}
		for(SkEnConf[] ccarr : arr){
			for(SkEnConf c : ccarr){
				out.print(c.zhiYe);		out.print(" -- ");
				out.print(c.baseId);		out.print(" -- ");
				out.print(c.baseName);		out.print(" -- <br/>");
				out.print(" &nbsp;&nbsp;&nbsp; ");				out.print(c.desc[0]); 	out.print("<br/>");
				out.print(" &nbsp;&nbsp;&nbsp; ");				out.print(c.desc[1]); 	out.print("<br/>");
				out.print(" &nbsp;&nbsp;&nbsp; ");				out.print(c.desc[2]); 	out.print("<br/>");
			}
			out.print("------------------------------<br/>");
		}
	}while(false);
}
%>
</body></html>
