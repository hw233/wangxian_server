<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,com.fy.engineserver.core.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.xuanzhi.tools.queue.*,com.fy.engineserver.message.*,
com.fy.engineserver.gateway.*,com.fy.engineserver.sprite.pet.*,
"%>
<%@page import="java.lang.reflect.Field"%><%
    
	

PlayerManager sm = PlayerManager.getInstance();
     
CoreSubSystem core = CoreSubSystem.getInstance();

Date date = new Date(org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application).getStartupDate());

if("true".equals(request.getParameter("settingview"))){ 
	
	if("true".equals(request.getParameter("enable_fenghao"))){
		Player.是否启用加速封号功能 = true;
	}else{
		Player.是否启用加速封号功能 = false;
	}
	
	Player.方差标准值 = Double.parseDouble(request.getParameter("fangca"));
	Player.平均值外挂标准值 = Double.parseDouble(request.getParameter("pinjun1"));
	Player.平均值警告标准值 = Double.parseDouble(request.getParameter("pinjun2"));
	Player.连续发现加速标准值 = Integer.parseInt(request.getParameter("lianxu")); 
	Player.连续2次加速累计次数标准值= Integer.parseInt(request.getParameter("lianxu2")); 
	Player.连续5次加速累计次数标准值= Integer.parseInt(request.getParameter("lianxu5")); 
	Player.清理连续累计次数分钟间隔= Integer.parseInt(request.getParameter("qinglijiange")); 
	Player.CHECK_PACKAGE_NUM= Integer.parseInt(request.getParameter("checkNum")); 
	
}



Player players[] = sm.getCachedPlayers();
Arrays.sort(players,new Comparator<Player>(){
	public int compare(Player p1,Player p2){
		if(p1.isJiaShuWaiGua == true  && p2.isJiaShuWaiGua == false) return -1;
		if(p1.isJiaShuWaiGua == false && p2.isJiaShuWaiGua == true) return 1;
		
		if(p1.isOnline() & p2.isOnline() == false) return -1;
		if(p1.isOnline() == false & p2.isOnline() ) return 1;
		
		if(p1.jiaShuWaiGuaContinueFoundTimes > p2.jiaShuWaiGuaContinueFoundTimes) return -1;
		if(p1.jiaShuWaiGuaContinueFoundTimes < p2.jiaShuWaiGuaContinueFoundTimes) return 1;
		
		if(p1.isJiaShuWaiGua == true  == p2.isJiaShuWaiGua == true){
			if(p1.lastJiaShuWaiGuaCheckTime > p2.lastJiaShuWaiGuaCheckTime) return -1;
			if(p1.lastJiaShuWaiGuaCheckTime < p2.lastJiaShuWaiGuaCheckTime) return 1;
		}
		
		if(p1.isMaybeJiaShuWaiGua == true  && p2.isMaybeJiaShuWaiGua == false) return -1;
		if(p1.isMaybeJiaShuWaiGua == false && p2.isMaybeJiaShuWaiGua == true) return 1;
		
		if(p1.lastJiaShuWaiGuaCheckTime > p2.lastJiaShuWaiGuaCheckTime) return -1;
		if(p1.lastJiaShuWaiGuaCheckTime < p2.lastJiaShuWaiGuaCheckTime) return 1;
		
		if(p1.getLevel() > p2.getLevel()) return -1;
		if(p1.getLevel() < p2.getLevel()) return 1;
		if(p1.getId() < p2.getId()) return -1;
		if(p1.getId() > p2.getId()) return 1;
		return 0;
	}
});

int 使用加速外挂人数 = 0;
for(int i = 0 ; i < players.length ; i++){
	Player p1 = players[i];
	if(p1.isJiaShuWaiGua)
		使用加速外挂人数++;
}

if("true".equalsIgnoreCase(request.getParameter("fenghao"))){
	Long id = Long.parseLong(request.getParameter("pid"));
	Player p = sm.getPlayer(id);
	if(p != null){
		
		DENY_USER_REQ req = new DENY_USER_REQ(GameMessageFactory.nextSequnceNum(), 
				"",p.getUsername(),"角色["+p.getName()+"]在["+com.xuanzhi.boss.game.GameConstants.getInstance().getServerName()+"]服使用加速外挂",
				com.xuanzhi.boss.game.GameConstants.getInstance().getServerName()+"-后台手动封号",
				false,true,false,0,2);
		MieshiGatewayClientService.getInstance().sendMessageToGateway(req);
		
		if(p.getConn() != null) 
			p.getConn().close();
		


	}
}

%>
<html><head>
</HEAD>
<BODY>
<h2>Game Server，内存中人数：<%= players.length %>，正在使用加速外挂人数：<%= 使用加速外挂人数 %>，各个在线玩家的情况</h2><br><a href="./check_players_jiasu.jsp">刷新此页面</a><br><br>
<form id='f'>
<input type='hidden' name='settingview' value='true'>
是否启用加速封号功能：<input type='text' name=enable_fenghao value='<%= Player.是否启用加速封号功能 %>'> &nbsp;
方差标准值：<input type='text' name='fangca' value='<%= Player.方差标准值 %>'>
平均值外挂标准值：<input type='text' name='pinjun1' value='<%= Player.平均值外挂标准值 %>'>
平均值警告标准值：<input type='text' name='pinjun2' value='<%= Player.平均值警告标准值 %>'>
连续发现加速标准值：<input type='text' name='lianxu' value='<%= Player.连续发现加速标准值 %>'>
连续2次加速累计次数标准值：<input type='text' name='lianxu2' value='<%= Player.连续2次加速累计次数标准值 %>'>
连续5次加速累计次数标准值：<input type='text' name='lianxu5' value='<%= Player.连续5次加速累计次数标准值 %>'>
清理连续累计次数分钟间隔：<input type='text' name='qinglijiange' value='<%= Player.清理连续累计次数分钟间隔 %>'>
CHECK_PACKAGE_NUM：<input type='text' name='checkNum' value='<%= Player.CHECK_PACKAGE_NUM %>'>
<input type='submit' value='提交'></form>

<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>编号</td>
<td>ID</td>
<td>是否在线</td>
<td>是否封号</td>
<td>是否使用加速</td>
<td>疑似使用加速</td>
<td>最后一次检测时间</td>
<td>连续检测到的次数</td>
<td>2连续检测到的次数</td>
<td>5连续检测到的次数</td>
<td>一共检测到的次数</td>
<td>Name</td>
<td>账户名</td>
<td>地图</td>
<td>级别</td>
<td>充值</td>
<td>血</td>
<td>蓝</td>
<td>经验值</td>
<td>最后一次接收数据的时间</td>
<td>X坐标</td>
<td>Y坐标</td>
<td>操作</td>
</tr>
<%
        //int n = sm.getAmountOfPlayers();

        for(int i = 0 ; i < players.length ; i++){
                Connection conn = players[i].getConn();
                
                if(players[i].isJiaShuWaiGua == false && players[i].jiaShuWaiGuaFoundTimes == 0) continue;
                %><tr bgcolor="#FFFFFF" align="center">
                <td><%=i+1 %></td>
                <td><a href='player/mod_player.jsp?action=select_playerId&playerid=<%=players[i].getId() %>'><%=players[i].getId() %></a></td>
                <td><%= (players[i].getConn() != null && players[i].getConn().getState() != Connection.CONN_STATE_CLOSE)?"在线":"不在线" %></a></td>
             	<td><%= players[i].hasFenghaoByJiaShu ?"封号":"正常" %></a></td>
                <td><%= players[i].isJiaShuWaiGua?"加速":"正常" %></a></td>
                 <td><%= players[i].isMaybeJiaShuWaiGua?"是":"否" %></a></td>
                <td><%= (players[i].isJiaShuWaiGua?((System.currentTimeMillis() - players[i].lastJiaShuWaiGuaCheckTime)/1000)+"秒前":"--") %></a></td>
                <td><%= players[i].jiaShuWaiGuaContinueFoundTimes %></a></td>
                  <td><%= players[i].jiaShuWaiGuaContinue2FoundTimes %></a></td>
                    <td><%= players[i].jiaShuWaiGuaContinue5FoundTimes %></a></td>
                <td><%= players[i].jiaShuWaiGuaFoundTimes %></a></td>
              	<td><%= players[i].getName()%></td>
                <td><%= players[i].getUsername()%></td>
                <td><%= players[i].getGame() %></td>
                <td><%= players[i].getLevel() %></td>
                <td><%= players[i].getRMB() %></td>
                <td><%= players[i].getHp()+"/" + players[i].getMaxHP() %></td>
                <td><%= players[i].getMp()+"/" + players[i].getMaxMP() %></td>
                <td><%= players[i].getExp()+"/" + players[i].getNextLevelExp() %></td>
                <td><%= (System.currentTimeMillis() - players[i].getLastRequestTime())/1000 %>秒前</td>
                <td><%=players[i].getX() %></td>
                <td><%=players[i].getY() %></td>
                <td><a href='./check_players_jiasu.jsp?fenghao=true&pid=<%= players[i].getId() %>'>封号</a></td>
                </tr><%
        }
%>
</table>
</BODY></html>
