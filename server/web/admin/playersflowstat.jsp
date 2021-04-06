<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,com.fy.engineserver.core.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.xuanzhi.tools.queue.*,com.fy.engineserver.gateway.*"%><%
        PlayerManager sm = PlayerManager.getInstance();
     
CoreSubSystem core = CoreSubSystem.getInstance();

Date date = new Date(org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application).getStartupDate());

Player sprites[] = sm.getOnlinePlayers();
Arrays.sort(sprites,new Comparator<Player>(){
	public int compare(Player p1,Player p2){
		Connection conn1 = p1.getConn();
		Connection conn2 = p2.getConn();
		if(conn1 == null) return 1;
		if(conn2 == null) return 1;
		GameNetworkFramework.FLOWSTAT f1 = (GameNetworkFramework.FLOWSTAT)conn1.getAttachmentData(GameNetworkFramework.FLOWSTAT);
		GameNetworkFramework.FLOWSTAT f2 = (GameNetworkFramework.FLOWSTAT)conn2.getAttachmentData(GameNetworkFramework.FLOWSTAT);
		if(f1 == null) return 1;
		if(f2 == null) return 1;
		long t1 = f1.lastSendTime+1 - f1.startTime;
		long t2 = f2.lastSendTime+1 - f2.startTime;
		if(t1 < 60000 && t2 > 60000) return 1;
		if(t2 < 60000 && t1 > 60000) return -1;

		double r1 = f1.sendBytes * 1000.0/(f1.lastSendTime+1 - f1.startTime);
		double r2 = f2.sendBytes * 1000.0/(f2.lastSendTime+1 - f2.startTime);
		if(r1 > r2) return -1;
		if(r1 < r2) return 1;
		if(f1.sendBytes > f2.sendBytes) return -1;
		if(f1.sendBytes < f2.sendBytes) return 1;
		if(f1.receiveBytes > f2.receiveBytes) return -1;
		if(f1.receiveBytes < f2.receiveBytes) return 1;
		return 0;
	}
});

if("true".equals(request.getParameter("settingview"))){
	int viewWidth = Integer.parseInt(request.getParameter("viewWidth"));
	int viewHeight = Integer.parseInt(request.getParameter("viewHeight"));
	if(viewWidth >= 120 && viewHeight >= 160 && viewWidth <= 640 && viewHeight <= 960){
		for(int i = 0 ; i < sprites.length ; i++){
			sprites[i].setViewWidth(viewWidth);
			sprites[i].setViewHeight(viewHeight);
		}
		
		if("true".equals(request.getParameter("force"))){
			CoreSubSystem.FORCE_USE_DEFAULT_VIEW = true;
			CoreSubSystem.DEFAULT_PLAYER_VIEWWIDTH = viewWidth;
			CoreSubSystem.DEFAULT_PLAYER_VIEWHEIGHT = viewHeight;
		}
		
		out.println("<h1>所有玩家的视野被设置为："+viewWidth+" * "+viewHeight+"</h1>");
		System.out.println("<h1>所有玩家的视野被设置为："+viewWidth+" * "+viewHeight+"</h1>");
	}
}

%>
<%@include file="IPManager.jsp" %><html><head>
</HEAD>
<BODY>
<h2>Game Server，在线人数：<%= sprites.length %>，正在等待的用户：<%= core.waitingEnterGameConnections.size() %>，各个在线玩家的情况</h2><br><a href="./playersflowstat.jsp">刷新此页面</a><br><br>
系统配置：<%= CoreSubSystem.FORCE_USE_DEFAULT_VIEW?"系统强制固定视野":"使用玩家的设置" %>&nbsp;宽度：<%= CoreSubSystem.DEFAULT_PLAYER_VIEWWIDTH %>&nbsp;高度：<%= CoreSubSystem.DEFAULT_PLAYER_VIEWHEIGHT %> <br>
设置所有玩家的视野（非技术人员禁止使用）：<form id='f'><input type='hidden' name='settingview' value='true'>宽度：<input type='text' name='viewWidth' value='480'> &nbsp;高度：<input type='text' name='viewHeight' value='720'>是否强制：<input type='text' name='force' value='false'><input type='submit' value='提交'></form>
<br><b>在线玩家网络延迟分布<b><br>
<%
String delayNames[] = new String[]{"~100","100~200","200~300","300~400","400~500","500~600","600~700","700~800","800~900","900~1000","1000~1500","1500~2000","2000~3000","3000~5000","5000~"};
int delayLimits[] = new int[]{100,200,300,400,500,600,700,800,900,1000,1500,2000,3000,5000};
int delayCount[] = new int[delayLimits.length+1];
for(int i = 0 ; i < sprites.length ; i++){
    Connection conn = sprites[i].getConn();
    if(conn != null){
		String delayS = "" + conn.getAttachmentData("network.delay");
		int delay = Integer.parseInt(delayS);
		int k = Arrays.binarySearch(delayLimits,delay);
		if(k>= 0){
			delayCount[k+1]++;
		}else{
			delayCount[-k-1]++;
		}
    }
}
%>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">

<%
	out.println("<td>在线人数</td>");
	for(int i = 0 ; i < delayNames.length ; i++){
		out.println("<td>"+delayNames[i]+"</td>");
	}
%>
</tr>
<tr bgcolor="#FFFFFF" align="center">
<%
	out.println("<td>"+sprites.length+"</td>");
	for(int i = 0 ; i < delayCount.length ; i++){
		out.println("<td>"+delayCount[i]+"</td>");
	}
%>
</tr>

</table>
<br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>编号</td>
<td>ID</td>
<td>Name</td>
<td>账户名</td>
<td>视野</td>
<td>级别</td>
<td>网络延迟</td>
<td>在线时长</td>
<td>发送流量/小时</td>
<td>接受流量/小时</td>
<td>最后一次发送时间</td>
<td>最后一次接收时间</td>
<td>发送数据（流量/包）</td>
<td>接受数据（流量/包）</td>
<td>发送包排名</td>
<td>接收包排名</td>

</tr>
<%
        //int n = sm.getAmountOfPlayers();

        for(int i = 0 ; i < sprites.length ; i++){
                Connection conn = sprites[i].getConn();
                final GameNetworkFramework.FLOWSTAT f;
                String delay = "未知";
                if(conn != null){
            		f = (GameNetworkFramework.FLOWSTAT)conn.getAttachmentData(GameNetworkFramework.FLOWSTAT);
            		delay = "" + conn.getAttachmentData("network.delay");
                }else{
                	f = new GameNetworkFramework.FLOWSTAT();
                }
                
                int s = (int)(f.sendBytes * 3600.0f * 1000/(f.lastSendTime + 1 - f.startTime)/1024/1024);
                int r = (int)(f.receiveBytes* 3600.0f * 1000/(f.lastReceiveTime + 1 - f.startTime)/1024/1024);
                
                %><tr bgcolor="#FFFFFF" align="center">
                <td><%=i+1 %></td>
                <td><a href='player/mod_player.jsp?action=select_playerId&playerid=<%=sprites[i].getId() %>'><%=sprites[i].getId() %></a></td>
                <td><%= sprites[i].getName()%></td>
                <td><%= sprites[i].getUsername()%></td>
                <td><%= sprites[i].getViewWidth() + "×" + sprites[i].getViewHeight() %></td>
                <td><%= sprites[i].getLevel() %></td>
                <td><%= delay %></td>
                <td><%= (System.currentTimeMillis() - f.startTime)/60000%>分钟</td>
                <td><%= s %></td>
                <td><%= r %></td>
                <td><%= (System.currentTimeMillis() - f.lastSendTime)  %>毫秒前</td>
                <td><%= (System.currentTimeMillis() - f.lastReceiveTime)  %>毫秒前</td>
                <td><%= String.format("%,d",f.sendBytes)+"<br/>" + String.format("%,d",f.sendPackets) %></td>
                <td><%= String.format("%,d",f.receiveBytes) + "<br/>" + String.format("%,d",f.receivePackets) %></td>
                <%
                
                String keys[] =  f.sendMap.keySet().toArray(new String[0]);
    			Arrays.sort(keys,new Comparator<String>(){
    				public int compare(String o1, String o2) {
    					long r1[] = f.sendMap.get(o1);
    					long r2[] = f.sendMap.get(o2);
    					if(r1[1] < r2[1]) return 1;
    					if(r1[1] > r2[1]) return -1;
    					return 0;
    				}});
    			out.print("<td>");
    			for(int j = 0 ; j < 3 ; j++){
    				if(j < keys.length){
    					long ll[] = f.sendMap.get(keys[j]);
    					out.print(keys[j]+"("+(ll[1]*100/(f.sendBytes+1))+"%)");
    					if(j < 2){
    						out.print("<br/>");
    					}
    				}
    			}
    			out.print("</td>");
    			
    			 keys =  f.receiveMap.keySet().toArray(new String[0]);
    			Arrays.sort(keys,new Comparator<String>(){
    				public int compare(String o1, String o2) {
    					long r1[] = f.receiveMap.get(o1);
    					long r2[] = f.receiveMap.get(o2);
    					if(r1[1] < r2[1]) return 1;
    					if(r1[1] > r2[1]) return -1;
    					return 0;
    				}});
    			
    			out.print("<td>");
    			for(int j = 0 ; j < 3 ; j++){
    				if(j < keys.length){
    					long ll[] = f.receiveMap.get(keys[j]);
    					out.print(keys[j]+"("+(ll[1]*100/(f.receiveBytes+1))+"%)");
    					if(j < 2){
    						out.print("<br/>");
    					}
    				}
    			}
    			out.print("</td>");

        }
%>
</table>
</BODY></html>
