<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.fy.boss.authorize.model.Passport"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.fy.engineserver.activity.across.battle.CrossServerManager"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardDate"%>
<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.activity.across.AcrossServerManager"%>
<%@page import="com.xuanzhi.tools.text.JsonUtil"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.activity.across.battle.MatchData"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%>
<%@page import="java.net.URL"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="com.fy.engineserver.core.CoreSubSystem"%>
<%@page import="com.fy.engineserver.tune.ServerClientConnector"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.core.event.ReconnectEvent"%>
<%@page import="java.util.Random"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.core.TransportData"%>
<%@page import="com.fy.engineserver.util.CompoundReturn"%>
<%@page import="java.util.Collection"%>
<%@page import="com.fy.engineserver.core.LivingObject"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="java.util.HashSet"%>

<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="java.util.Comparator"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PetPropsEntity"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page import="com.fy.engineserver.activity.ActivityManager"%>
<%@page import="com.fy.engineserver.util.server.TestServerConfigManager"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardStatDateManager"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardStatDate"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardsManager"%>
<%@page import="com.fy.engineserver.newBillboard.Billboard"%>
<%@page import="com.fy.engineserver.datasource.buff.Buff"%>
<%@page import="com.fy.engineserver.economic.CurrencyType"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<%!	//测试玩家进入跨服
	public class TestGetGame implements Runnable{
		public int index = 0;
		public int maxEnterNums = 1620;
		public boolean isRun = true;
		@Override
		public void run() {
			while(isRun){
				try {
					Thread.sleep(200L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				long now = System.currentTimeMillis();
				String newName = "跨服pk"+(index++);
				try{
					Player tp = PlayerManager.getInstance().getPlayer("pk灵尊004@更端测试A");
					Random random = new Random();
					long now2 = System.currentTimeMillis();
					Player p = null;
					try{
						p = PlayerManager.getInstance().getPlayer(newName);
					}catch(Exception e){
						if(p == null){
							p = PlayerManager.getInstance().createPlayer("swf011", newName, (byte)1,(byte)1, (byte)1, (byte)1,1);
						}
					}
					CompoundReturn cr = CrossServerManager.getInstance().getMapName();
					if(cr == null || !cr.getBooleanValue()){
						System.out.println("[进入游戏] [失败:武神至尊参加人数太多] [地图:"+cr.getStringValue()+"] [用户名:"+p.getUsername()+"] [角色:"+p.getName()+"]");
					}else{
						Game game2 = GameManager.getInstance().getGameByName(cr.getStringValue(), 0);
						p.init();
						p.setAvataRace(tp.getAvataRace());
						p.setAvata(tp.getAvata());
						p.setAvataSex(tp.getAvataSex());
						p.setLevel(150+random.nextInt(100));
						int xy[] = CrossServerManager.getInstance().getPoints();
						p.setX(xy[0]+random.nextInt(200));
						p.setY(xy[1]+random.nextInt(200));
						game2.reEnter(p);
						Thread.sleep(1000L);
						System.out.println("[进入游戏] [成功] [人数:"+game2.getPlayers().size()+"] [地图:"+cr.getStringValue()+"] [用户名:"+p.getUsername()+"] [角色:"+p.getName()+"] [等级:"+p.getLevel()+"] [x:"+p.getX()+"] [y:"+p.getY()+"] [cost:"+(System.currentTimeMillis()-now2)+"]");
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				if(index == maxEnterNums){
					System.out.print("===================操作结束=================<br>");
					isRun = false;
				}
			}
		}
	}
%>

<%!	//测试玩家匹配
	public class TestMatchBattle implements Runnable{
		public boolean isRun = true;
		@Override
		public void run() {
			while(isRun){
				try {
					Thread.sleep(100L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				for(String name : CrossServerManager.getInstance().maps){
					Game game = GameManager.getInstance().getGameByName(name,0);
					List<Player> players = game.getPlayers();
					for(Player p : players){
						if(p.getName().contains("跨服")){
							p.crossMapName = "wushenzhidiana";
						}
						CrossServerManager.getInstance().startMatchBattle(p);
						System.out.println("[加入匹配] [玩家:"+p.getName()+"] [等级:"+p.getLevel()+"]");
					}
				}
				isRun = false;
			}
		}
	
	
 	}
%>
<form>
<table>
	<tr>
		<th>发放跨服排行周奖励</th>
		<td><input type="hidden" name='send' value='ok'><td>
		<td><input type='submit' value='发放'></td>
	</tr>
</table>
</form>
<form>
<table>
	<tr>
		<th>修改活动开启时间</th>
		<td><input type="hidden" name='send' value='ok2'><td>
		<td>开启时间:<input type="text" name='startT' value=''><td>
		<td>结束时间:<input type="text" name='endT' value=''><td>
		<td><input type='submit' value='设置'></td>
	</tr>
</table>
</form>

<form>
<table>
	<tr>
		<th>重置跨服匹配次数</th>
		<td><input type="hidden" name='send' value='ok3'><td>
		<td>玩家名字:<input type="text" name='pname' value=''><td>
		<td><input type='submit' value='重置'></td>
	</tr>
</table>
</form>

<form>
<table>
	<tr>
		<th>显示所有跨服地图玩家信息</th>
		<td><input type="hidden" name='send' value='ok4'><td>
		<td><input type='submit' value='显示'></td>
	</tr>
</table>
</form>

<form>
<table>
	<tr>
		<th>设置分屏因子</th>
		<td><input type="hidden" name='send' value='ok5'><td>
		<td>地图容纳最大人数:<input type="text" name='maxNums' value=''><td>
		<td>寻找下一张地图的人数:<input type="text" name='nextNums' value=''><td>
		<td><input type='submit' value='设置'></td>
	</tr>
</table>
</form>

<form>
<table>
	<tr>
		<th>1.清楚跨服地图abcdefghi所有玩家</th>
		<td><input type="hidden" name='send' value='ok6'><td>
		<td><input type="text" name='pwd' value='***'><td>
		<td><input type='submit' value='清除'></td>
	</tr>
</table>
</form>

<form>
<table>
	<tr>
		<th>2.模拟玩家进入跨服地图abcdefghi</th>
		<td><input type="hidden" name='send' value='ok7'><td>
		<td><input type="text" name='pwd' value='***'><td>
		<td><input type='submit' value='上线'></td>
	</tr>
</table>
</form>

<form>
<table>
	<tr>
		<th>3.跨服所有玩家报名并加入匹配</th>
		<td><input type="hidden" name='send' value='ok8'><td>
		<td><input type="text" name='pwd' value='***'><td>
		<td><input type='submit' value='报名'></td>
	</tr>
</table>
</form>

<form>
<table>
	<tr>
		<th>4.刷新查看匹配情况</th>
		<td><input type="hidden" name='send' value='ok9'><td>
		<td><input type='submit' value='查看'></td>
	</tr>
</table>
</form>
<hr><hr><hr>
<%
/**
SimpleEntityManager<BillboardStatDate> em = BillboardStatDateManager.em;
long addHonorPointTime = CrossServerManager.getInstance().getWeekOfYear();
long[] ids200 = em.queryIds(BillboardStatDate.class, " honorPoint>?",new Object[]{0},"honorPoint desc ",1,1000);
if(ids200 != null && ids200.length > 0){
	for(Long id : ids200){
		BillboardStatDate bData = BillboardStatDateManager.getInstance().getBillboardStatDate(id);
		if(bData != null){
			int oldP = bData.getHonorPoint();
			bData.setHonorPoint(0);
			out.print("[设置成功] [oldP:"+oldP+"] [id:"+bData.getId()+"]<br>");
		}
	}
}
Billboard bb = BillboardsManager.getInstance().getBillboard(Translate.武神之巅, Translate.武神积分);
if(bb != null){
	BillboardDate[] datas = bb.getDates();
	out.print("datas:"+datas.length);
}

for(int i=0;i<CrossServerManager.getInstance().openTimes.length;i++){
	int[] times = CrossServerManager.getInstance().openTimes[i];
	out.print(Arrays.toString(times)+"<br>");
}
CrossServerManager.getInstance().openTimes = new int[][]{{11,14},{18,21}};
out.print("<hr>");
for(int i=0;i<CrossServerManager.getInstance().openTimes.length;i++){
	int[] times = CrossServerManager.getInstance().openTimes[i];
	out.print(Arrays.toString(times)+"<br>");
}

*/
////////////////////////////////////////////////////////////////////////////////////
String send = request.getParameter("send");
String startT = request.getParameter("startT");
String endT = request.getParameter("endT");
String pname = request.getParameter("pname");
String maxNums = request.getParameter("maxNums");
String nextNums = request.getParameter("nextNums");
String pwd = request.getParameter("pwd");

if(send != null && send.equals("ok")){
	if(!TestServerConfigManager.isTestServer){
		out.print("不能操作<br>");
	}else{
		Calendar cl = Calendar.getInstance();
		int week = cl.get(Calendar.DAY_OF_WEEK);
		int hour = cl.get(Calendar.HOUR_OF_DAY);
		CrossServerManager.getInstance().billboardUpdateDayOfWeek = week;
		CrossServerManager.getInstance().billboardUpdateHour = hour;
		CrossServerManager.getInstance().hasSendPrice = false;
		out.print("设置成功,week:"+CrossServerManager.getInstance().billboardUpdateDayOfWeek+"--hour:"+hour+CrossServerManager.getInstance().isHasSendPrice()+"<br>");
	}
}else if(send != null && send.equals("ok2")){
	if(!TestServerConfigManager.isTestServer){
		out.print("不能操作<br>");
	}else{
		if(startT == null || endT == null || startT.isEmpty() || endT.isEmpty()){
			out.print("请填内容");
		}else{
			int st = Integer.parseInt(startT);
			int et = Integer.parseInt(endT);
			CrossServerManager.getInstance().openTimes = new int[][]{{st,et}};
			out.print("设置成功:"+startT+"--"+endT+"<br>");
		}
	}
}else if(send != null && send.equals("ok3")){
	if(!TestServerConfigManager.isTestServer){
		out.print("不能操作<br>");
	}else{
		if(pname == null || pname.isEmpty()){
			out.print("请输入玩家名字");
		}else{
			Player p = PlayerManager.getInstance().getPlayer(pname);	
			p.setRewardHonorPointTimes(0);
			out.print("设置成功");
		}
	}
}else if(send != null && send.equals("ok4")){
	for(String name : CrossServerManager.getInstance().maps){
		Game game = GameManager.getInstance().getGameByName(name, 0);
		if(game == null){
			out.print("地图"+name+"不存在<br>");
			continue;
		}
		List<Player> players = game.getPlayers();
		if(players.size() > 0){
			out.print("==========================地图"+name+"=========================<br>");
			for(Player p : players){
				out.print("[地图:"+name+"] [总人数:"+players.size()+"] [玩家:"+p.getName()+"] [id:"+p.getId()+"] [等级:"+p.getLevel()+"] [积分:"+p.getHonorPoint()+"]<br>");
			}
			out.print("==========================地图"+name+"=========================<br>");
		}
	}
}else if(send != null && send.equals("ok5")){
	if(!TestServerConfigManager.isTestServer){
		out.print("不能操作<br>");
	}else{
		if(maxNums == null || maxNums.isEmpty() || nextNums == null || nextNums.isEmpty()){
			out.print("内容不正确<br>");
		}else{
			out.print("[设置成功] [设置前] [地图容纳最大人数:"+CrossServerManager.getInstance().maxEnterPlayer+"] [寻找下一张地图的人数:"+CrossServerManager.getInstance().addRange+"]<br>");
			CrossServerManager.getInstance().maxEnterPlayer = Integer.parseInt(maxNums);
			CrossServerManager.getInstance().addRange = Integer.parseInt(nextNums);
			out.print("[设置成功] [设置后] [地图容纳最大人数:"+CrossServerManager.getInstance().maxEnterPlayer+"] [寻找下一张地图的人数:"+CrossServerManager.getInstance().addRange+"]<br>");
		}
	}
}else if(send != null && send.equals("ok11")){
	if(!TestServerConfigManager.isTestServer){
		out.print("不能操作<br>");
	}else{
		Calendar cl = Calendar.getInstance();
		int day_of_week = cl.get(Calendar.DAY_OF_WEEK);
		int hour = cl.get(Calendar.HOUR_OF_DAY);
		CrossServerManager.getInstance().billboardUpdateDayOfWeek = day_of_week;
		CrossServerManager.getInstance().billboardUpdateHour = hour;
		out.print("设置成功:"+day_of_week+"--"+hour+"<br>");
	}
}else if(send != null && send.equals("ok6")){
	if(!TestServerConfigManager.isTestServer){
		out.print("不能操作<br>");
	}else{
		if(pwd != null && pwd.equals("6yhnmju78ik,")){
			for(String name : CrossServerManager.getInstance().maps){
				Game game22 = GameManager.getInstance().getGameByName(name, 0);
				Class c = Game.class;
				Field f = c.getDeclaredField("livingSet");
				f.setAccessible(true);
				Collection<LivingObject> livingSet = (Collection<LivingObject>)f.get(game22);
				int oldNums = livingSet.size();
				f.set(game22,new HashSet<LivingObject>());
				out.print("[地图:"+name+"] [清楚前人数:"+oldNums+"]"+"<br>");
			}
		}else{
			out.print("密码不正确");
		}
	}
}else if(send != null && send.equals("ok7")){
	if(!TestServerConfigManager.isTestServer){
		out.print("不能操作<br>");
	}else{
		if(pwd != null && pwd.equals("6yhnmju78ik,")){
			TestGetGame t = new TestGetGame();
			new Thread(t,"测试跨服进入").start();
			out.print("操作马上开始..");	
		}else{
			out.print("密码不正确");
		}
	}
}else if(send != null && send.equals("ok8")){
	if(!TestServerConfigManager.isTestServer){
		out.print("不能操作<br>");
	}else{
		if(pwd != null && pwd.equals("6yhnmju78ik,")){
			TestMatchBattle t = new TestMatchBattle();
			new Thread(t,"测试玩家匹配").start();
		}else{
			out.print("密码不正确");
		}
	}
}else if(send != null && send.equals("ok9")){
	List<MatchData> matchData = CrossServerManager.getInstance().getMatchData();
	if(matchData.size() > 0){
		for(MatchData dd : matchData){
			if(dd != null){
				if(dd.getPlayerA() != null && dd.getPlayerB() != null){
					out.print("[匹配情况] [对手："+dd.getPlayerA().getName()+" VS "+dd.getPlayerB().getName()+"] [等级:"+dd.getPlayerA().getLevel()+"/"+dd.getPlayerB().getLevel()+"]<br>");
				}
			}
		}
	}else{
		out.print("没有数据<br>");
	}
}
%>
</body>
</html>
