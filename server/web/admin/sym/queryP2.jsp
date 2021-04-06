<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>

<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory"%>
<%@page import="java.util.Date"%>
<%@page import="com.fy.engineserver.util.TimeTool"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<%
SimpleEntityManager<Player> manager = SimpleEntityManagerFactory.getSimpleEntityManager(Player.class);
long now = System.currentTimeMillis();
long MONTH = 30L * 24 * 60 * 60 * 1000;
long totleMoney = 0;
long playerTotalCount = manager.count();
String sqlStr = "quitGameTime <=" + (now - 12 * MONTH) +" and level >= 10 and silver > 0";
long[] ids = manager.queryIds(Player.class, sqlStr, "", 1, playerTotalCount);
int totle = 0;
long queryTime = 0;
for(long id : ids){
	try{
		queryTime = System.currentTimeMillis();
		Player p = PlayerManager.getInstance().getPlayer(id);
		totle ++;
		totleMoney+=p.getSilver();
		out.print("["+TimeTool.formatter.varChar19.format(p.getQuitGameTime())+"] [账号:"+p.getUsername()+"] [角色:"+p.getName()+"] [id:"+p.getId()+"] [等级:"+p.getLevel()+"] [银子:"+p.getSilver()+"] [耗时:"+(System.currentTimeMillis()-queryTime)+"]<br>");
	}catch(Exception e){
		continue;
	}
}
out.print("<font color='red'>检查完成，[总人数："+playerTotalCount+"] [满足条件人数："+totle+"] [总银子数："+totleMoney+"] [总耗时:"+(System.currentTimeMillis()-now)+"]</font><br>");
%>
</body>
</html>