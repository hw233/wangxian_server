<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.core.*,java.lang.reflect.*,com.google.gson.*,com.fy.engineserver.sprite.monster.*,com.fy.engineserver.downcity.*,com.fy.engineserver.sprite.npc.*"%><%! 
	
	String aaa(String s,int len){
		StringBuffer sb = new StringBuffer();
		char chars[] = s.toCharArray();
		int c = 0;
		for(int i = 0 ; i < chars.length ; i++){
			sb.append(chars[i]);
			c++;
			if( c >= len && (chars[i] == ',' || chars[i] == '{' || chars[i] == '}' || chars[i] == ':')){
				sb.append("<br/>");
				c = 0;
			}
		}
		return sb.toString();
	}
%>
<%@include file="IPManager.jsp" %><html><head>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<link rel="stylesheet" type="text/css" href="../css/table.css" />
<style type="text/css">
.titlecolor{
background-color:#C2CAF5;
}
</style>
<script type="text/javascript">
function changefubenstate(id){
	document.getElementById("dcid").value = id;
	document.f1.submit();
}
</script>
</HEAD>
<BODY>
<h2>
<a href="currentfubens_info.jsp">刷新</a>
</h2><br><br>
<form name="f1">
<input type="hidden" name="dcid" id="dcid">
</form>
<table>
<tr class="titlecolor"><td>所有副本地图</td><td>人数</td><td>副本ID</td><td>副本配置信息</td><td>副本状态</td><td>副本对应的地图</td><td>创建时间</td><td>结束时间</td><td>保留此副本进度的玩家</td><td>threadIndex</td><td>副本信息描述</td><td>重置</td></tr>
<%
int monsterCount = 0;
int npcCount = 0;
int playerCount = 0;
DownCityManager dcm = DownCityManager.getInstance();
String dcid = request.getParameter("dcid");
if(dcid != null && !dcid.trim().equals("")){
	DownCity dc = dcm.getDownCityById(dcid);
	if(dc != null){
		if(dc.getDownCityState() == 1){
			dc.setDownCityState((byte)0);
		}else{
			dc.setDownCityState((byte)1);
		}
		
	}
}
if(dcm != null){
DownCity[] dcs = dcm.getAllDownCity();
	ArrayList<DownCityInfo> dciList = dcm.getDciList();
	if(dciList != null){
		for(int i = 0; i < dciList.size(); i++){
			DownCityInfo dci = dciList.get(i);
			if(dci != null){
				ArrayList<DownCity> dcList = new ArrayList<DownCity>();
				if(dcs != null){
					for(DownCity dc : dcs){
						if(dc != null && dc.getGame() != null && dc.getGame().getGameInfo() != null && dc.getGame().getGameInfo().getName() != null && dc.getGame().getGameInfo().getName().equals(dci.getMapName())){
							dcList.add(dc);
						}
					}
				}
				if(dcList.size() != 0){
					for(int j = 0; j < dcList.size(); j++){
						DownCity dc = dcList.get(j);
						if(dc != null){
							%>
							<tr>
							<td><%=dc.getGame()!= null ? (dc.getGame().getGameInfo() != null ? dc.getGame().getGameInfo().getName() : "" ) : "" %></td>
							<td <%=dc.getGame()!= null ? (dc.getGame().getNumOfPlayer() != 0 ? "bgcolor='#C2CAF5'":""):""  %>><%=dc.getGame()!= null ? dc.getGame().getNumOfPlayer():"" %></td>
							<td><%=dc.getId() %></td><td><%=dc.getDi() != null ? dc.getDi().getName() : "" %></td><td><%=dc.getDownCityState() %></td><td><%=dc.getGame() != null ? dc.getGame().getGameInfo().getName()+"("+dc.getGame().getNumOfPlayer()+")" : "无副本地图" %></td><td><%=dc.getCreateTime() %></td><td><%=dc.getEndTime() %></td>
			<td>
			<%
			StringBuffer sbb = new StringBuffer();
			Player keepProcessPlayerList[] = dc.getPlayersKeepingProcess();
			if(keepProcessPlayerList != null){
				for(Player player : keepProcessPlayerList){
					if(player != null){
						sbb.append("<a href='player/mod_player_commondata.jsp?action=select_playerId&playerid="+player.getId()+"'>"+player.getName() +"</a> ");
					}
				}
			}
			out.println(sbb.toString());
			%>
			</td>
			<td><%=dc.getThreadIndex() %></td>
			<td><%
		out.println("副本地图信息:<br>");
		Game game = dc.getGame();
		GameInfo gi = game.getGameInfo();
		StringBuffer basicInfo = new StringBuffer();
		
		basicInfo.append("基本信息：CellH="+gi.getCellH()+",CellW="+gi.getCellW()+",Height="+gi.getHeight()+",Width="+gi.getWidth()+",signPostNum="+gi.navigator.getSignPostNum()+",roadNum="+gi.navigator.getRoadNum()+",polygonNum="+gi.navigator.getPolygonNum()+"<br/>");
		basicInfo.append("地图数据：file="+gi.getDataFile()+"<br/>");
		basicInfo.append("动态数据：player="+game.getNumOfPlayer()+",queue.eleNum="+(game.getQueue().getPushNum()-game.getQueue().getPopNum())+",queue.push="+game.getQueue().getPushNum()+",queue.pop="+game.getQueue().getPopNum()+"<br/>");
		basicInfo.append("最后一次心跳："+(System.currentTimeMillis() - game.getLastHeartBeatTime())+"毫秒前<br/>");
		LivingObject[] los = game.getLivingObjects();
		basicInfo.append("该副本中的生物个数:"+los.length+"<br>");
		int huanhangCount = 0;
		for(int ii = 0 ; ii < los.length ; ii++){
			basicInfo.append(""+(ii+1)+"");
			if(los[ii] instanceof Player){
				basicInfo.append("<a href='player/mod_player_commondata.jsp?action=select_playerId&playerid="+los[ii].getId()+"'>"+((Player)los[ii]).getName() +"</a>");
				playerCount++;
			}else if(los[ii] instanceof Monster){
				Monster monster = (Monster)los[ii];
				monsterCount++;
				basicInfo.append("<a href='spriteauto.jsp?sid="+monster.getId()+"'>"+monster.getName()+"</a>");
			}else if(los[ii] instanceof NPC){
				Sprite monster = (Sprite)los[ii];
				npcCount++;
				basicInfo.append("<a href='npcauto.jsp?sid="+monster.getId()+"'>"+monster.getName()+"</a>");
			}else{
				basicInfo.append("---");
			}
			if(huanhangCount == 5){
				basicInfo.append("<br>");
				huanhangCount = 0;
			}else{
				basicInfo.append("\t");
				huanhangCount++;
			}
			
		}
		%>
<%= basicInfo.toString() %></td>
<td><input type="button" id="<%=dc.getId() %>" value="更改副本状态" onclick="javascript:changefubenstate(<%=dc.getId() %>);"></td>
							</tr>
							<%
						}
					}
				}
			}
		}
	}
}
%>
</table>
<h1>所有怪:<%=monsterCount %> 所有人:<%=playerCount %> 所有npc:<%=npcCount %></h1>
</BODY></html>
