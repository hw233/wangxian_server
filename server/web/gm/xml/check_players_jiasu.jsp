<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,com.fy.engineserver.core.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.xuanzhi.tools.queue.*"%>
<%@include file="../authority.jsp" %>
<%
        PlayerManager sm = PlayerManager.getInstance();
     
CoreSubSystem core = CoreSubSystem.getInstance();

Date date = new Date(org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application).getStartupDate());

if("true".equals(request.getParameter("settingview"))){
	int speed = Integer.parseInt(request.getParameter("speed"));
	Player.加速外挂速度检查标准 = speed;	
	if("true".equals(request.getParameter("force"))){
		Player.系统强制中断加速外挂 = true;
	}else{
		Player.系统强制中断加速外挂 = false;
	}
}  



Player sprites[] = sm.getOnlinePlayers();
Arrays.sort(sprites,new Comparator<Player>(){
	public int compare(Player p1,Player p2){
		boolean b1 = p1.检测加速外挂(0,3);
		boolean b2 = p2.检测加速外挂(0,3);
		if(b1 == true && b2 == false) return -1;
		if(b1 == false && b2 == true) return 1;
		if(p1.getLevel() > p2.getLevel()) return -1;
		if(p1.getLevel() < p2.getLevel()) return 1;
		if(p1.getMoney() > p2.getMoney()) return -1;
		if(p1.getMoney() < p2.getMoney()) return 1;
		return 0;
	}
});

int 使用加速外挂人数 = 0;
for(int i = 0 ; i < sprites.length ; i++){
	Player p1 = sprites[i];
	boolean b1 = p1.检测加速外挂(0,3);
	if(b1){
		使用加速外挂人数++;
	}
}


%>
<%@page import="com.fy.engineserver.stat.StatData"%>
<html><head>
</HEAD>
<BODY>
<h2>Game Server，在线人数：<%= sprites.length %>，正在使用加速外挂人数：<%= 使用加速外挂人数 %>，各个在线玩家的情况</h2><br><a href="./check_players_jiasu.jsp">刷新此页面</a><br><br>
<form id='f'>
<input type='hidden' name='settingview' value='true'>
</form>

<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>编号</td>
<td>ID</td>
<td>0~15秒内</td>
<td>15~30秒内</td>
<td>30~45秒内</td>
<td>45~60秒内</td>
<td>Name</td>
<td>账户名</td>
<td>地图</td>
<td>级别</td>
<td>总时长</td>
<td>金币</td>
<td>血</td>
<td>蓝</td>
<td>经验值</td>
<td>最后一次接收数据的时间</td>
<td>X坐标</td>
<td>Y坐标</td>
</tr>
<%
        //int n = sm.getAmountOfPlayers();

        for(int i = 0 ; i < sprites.length ; i++){
                Connection conn = sprites[i].getConn();
                
                StatData sdata =sprites[i].getStatData(StatData.STAT_ONLINE_TIME);
                String alltime = "";
                if(sdata != null){
                	alltime = "" + (sdata.getValue()/3600000) + "小时";
                }
                %><tr bgcolor="#FFFFFF" align="center">
                <td><%=i+1 %></td>
                <td><%=sprites[i].getId() %></td>
                <td><%= sprites[i].检测加速外挂(0,3)?"加速":"正常" %></td>
                <td><%= sprites[i].检测加速外挂(3,3)?"加速":"正常" %></td>
                <td><%= sprites[i].检测加速外挂(6,3)?"加速":"正常" %></td>
                <td><%= sprites[i].检测加速外挂(9,3)?"加速":"正常" %></td>
                <td><%= sprites[i].getName()%></td>
                <td><%= sprites[i].getUsername()%></td>
                <td><%= sprites[i].getGame() %></td>
                <td><%= sprites[i].getLevel() %></td>
                <td><%= alltime %></td>
                <td><%= sprites[i].getMoney() %></td>
                <td><%= sprites[i].getHp()+"/" + sprites[i].getTotalHP() %></td>
                <td><%= sprites[i].getMp()+"/" + sprites[i].getTotalMP() %></td>
                <td><%= sprites[i].getExp()+"/" + sprites[i].getNextLevelExp() %></td>
                <td><%= (System.currentTimeMillis() - sprites[i].getLastRequestTime())/1000 %>秒前</td>
                <td><%=sprites[i].getX() %></td>
                <td><%=sprites[i].getY() %></td></tr><%
        }
%>
</table>
</BODY></html>
