<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,com.fy.engineserver.core.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.xuanzhi.tools.queue.*,com.fy.engineserver.gateway.*"%><%

	PlayerManager sm = PlayerManager.getInstance();
	HashSet<String> set = GameNetworkFramework.statFlowUserSet;
	
	String action = request.getParameter("action");
	if(action == null) action = "";
	
	if(action.equals("add")){
		set.add(request.getParameter("username").trim());
	}else if(action.equals("del")){
		set.remove(request.getParameter("username").trim());
	}
%>
<html><head>
</HEAD>
<BODY>
<h2>流量统计，在线人数：<%= sm.getOnlinePlayers().length %></h2><br><a href="./playersflowstat2.jsp">刷新此页面</a><br><br>
<form><input type='hidden' name='action' value='add'>
请输入帐号名:<input type='text' name='username' value='' ><input type='submit' value='提 交'>
</form>
<br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>编号</td>
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
<td>AROUND_CHANGE</td>
<td>发送包排名</td>
<td>接收包排名</td>
<td>操作</td>
</tr>
<%
String usernames[] = set.toArray(new String[0]);
for(int i = 0 ; i < usernames.length ; i++){
	Player ps[] = sm.getOnlinePlayerByUser(usernames[i]);
	if(ps != null && ps.length > 0){
		for(int jp = 0 ; jp < ps.length ; jp++){
			Player p = ps[jp];
			 Connection conn = p.getConn();
			 GameNetworkFramework.FLOWSTAT ff;
             String delay = "未知";
             if(conn != null){
         		ff = (GameNetworkFramework.FLOWSTAT)conn.getAttachmentData(GameNetworkFramework.FLOWSTAT);
         		delay = "" + conn.getAttachmentData("network.delay");
         		if( ff == null){
         			ff = new GameNetworkFramework.FLOWSTAT();
         		}
             }else{
             	ff = new GameNetworkFramework.FLOWSTAT();
             }
             final GameNetworkFramework.FLOWSTAT f = ff;
             
             int s = (int)(f.sendBytes * 3600.0f * 1000/(f.lastSendTime + 1 - f.startTime)/1024/1024);
             int r = (int)(f.receiveBytes* 3600.0f * 1000/(f.lastReceiveTime + 1 - f.startTime)/1024/1024);
             
             %><tr bgcolor="#FFFFFF" align="center">
             <td><%=i+1 %></td>
             <td><a href='player/mod_player.jsp?action=select_playerId&playerid=<%=p.getId() %>'><%= p.getName()%></a></td>
             <td><%= p.getUsername()%></td>
             <td><%= p.getViewWidth() + "×" + p .getViewHeight() %></td>
             <td><%= p.getLevel() %></td>
             <td><%= delay %></td>
             <td><%= (System.currentTimeMillis() - f.startTime)/60000%>分钟</td>
             <td><%= s %></td>
             <td><%= r %></td>
             <td><%= (System.currentTimeMillis() - f.lastSendTime)  %>毫秒前</td>
             <td><%= (System.currentTimeMillis() - f.lastReceiveTime)  %>毫秒前</td>
             <td><%= String.format("%,d",f.sendBytes)+"<br/>" + String.format("%,d",f.sendPackets) %></td>
             <td><%= String.format("%,d",f.receiveBytes) + "<br/>" + String.format("%,d",f.receivePackets) %></td>
             <%
             
            String keys[] =  f.aroundChangeMap.keySet().toArray(new String[0]);
  			Arrays.sort(keys,new Comparator<String>(){
  				public int compare(String o1, String o2) {
  					long r1[] = f.aroundChangeMap.get(o1);
  					long r2[] = f.aroundChangeMap.get(o2);
  					if(r1[1] < r2[1]) return 1;
  					if(r1[1] > r2[1]) return -1;
  					return 0;
  				}});
  			out.print("<td align='left'>");
  			for(int j = 0 ; j < keys.length ; j++){
 				if(j < keys.length){
 					long ll[] = f.aroundChangeMap.get(keys[j]);
 					out.print(keys[j]+"("+(ll[1]/1024)+"k/"+ll[0]+")");
 					if(j < keys.length ){
 						out.print("<br/>");
 					}
 				}
 			}
  			out.print("</td>");
 			
             
            keys =  f.sendMap.keySet().toArray(new String[0]);
 			Arrays.sort(keys,new Comparator<String>(){
 				public int compare(String o1, String o2) {
 					long r1[] = f.sendMap.get(o1);
 					long r2[] = f.sendMap.get(o2);
 					if(r1[1] < r2[1]) return 1;
 					if(r1[1] > r2[1]) return -1;
 					return 0;
 				}});
 			out.print("<td align='left'>");
 			for(int j = 0 ; j < 8 ; j++){
 				if(j < keys.length){
 					long ll[] = f.sendMap.get(keys[j]);
 					out.print(keys[j].toLowerCase()+"("+(ll[1]*100/(f.sendBytes+1))+"%)");
 					if(j < 8){
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
 			
 			out.print("<td align='left'>");
 			for(int j = 0 ; j < 8 ; j++){
 				if(j < keys.length){
 					long ll[] = f.receiveMap.get(keys[j]);
 					out.print(keys[j].toLowerCase()+"("+(ll[1]*100/(f.receiveBytes+1))+"%)");
 					if(j < 8){
 						out.print("<br/>");
 					}
 				}
 			}
 			out.print("</td><td><a href='./playersflowstat2.jsp?action=del&username="+usernames[i]+"'>删除</a></td></tr>");
		}
	}else{
		 %><tr bgcolor="#FFFFFF" align="center">
         <td><%=i+1 %></td>
         <td>--</td>
         <td><%= usernames[i] %></td>
         <td>--</td>
         <td>--</td>
         <td>--</td>
         <td>--</td>
         <td>--</td>
         <td>--</td>
         <td>--</td>
         <td>--</td>
         <td>--</td>
         <td>--</td>
         <td>--</td>
         <td>--</td>
         <td>--</td>
         <td><a href='./playersflowstat2.jsp?action=del&username=<%=usernames[i] %>'>删除</a></td></tr>
         <%
	}
}
     
%>
</table>
</BODY></html>
