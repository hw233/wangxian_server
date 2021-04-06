<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.core.*,java.lang.reflect.*,com.google.gson.*,com.fy.engineserver.sprite.monster.*,com.fy.engineserver.downcity.*,java.util.Calendar,java.text.*"%><%! 
	
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
</HEAD>
<BODY>
<h2></h2><br><br>
<%
DownCityManager dcm = DownCityManager.getInstance();
String downCityName = request.getParameter("downCityName");
if(dcm != null && downCityName != null){
	DownCityInfo dci  = dcm.getDownCityInfo(downCityName);
	DownCity[] dcs = dcm.getAllDownCity();
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
	ArrayList<DownCity> dciList = new ArrayList<DownCity>();
	if(dcs != null){
		for(DownCity dc : dcs){
			if(dc != null && dc.getDi() != null && dc.getDi().equals(dci)){
				dciList.add(dc);
			}
		}
	}
	if(dci != null){
		%>
		<table>
		<tr class="titlecolor">
		<td nowrap="nowrap">副本的名字</td><td nowrap="nowrap">副本的编号</td><td nowrap="nowrap">人数限制</td><td nowrap="nowrap">地图名</td><td nowrap="nowrap">玩家级别限制</td><td nowrap="nowrap">重置类型</td><td>开启后，持续多少时间后，被系统重置</td>
		</tr>
		<%
		if(dci != null){
			%>
			<tr>
				<td><%=dci.getName() %></td><td><%=dci.getSeqNum() %></td><td><%=dci.getPlayerNumLimit() %></td><td><%=dci.getMapName() %></td><td><%=dci.getMinPlayerLevel() %></td><td><%=dci.getResetType() %></td><td><%=dci.getLastingTime()+"ms" %></td>
			</tr>
			<%
		}
		%>
		<tr class="titlecolor">
		<td>副本ID</td><td>创建时间</td><td>结束时间</td><td colspan="3">保留此副本进度的玩家</td><td>threadIndex</td>
		</tr>
		<%
		for(DownCity dc : dciList){
		%>
		<tr>
			<td><%=dc.getId() %></td><td><%java.util.Date timer = new java.util.Date(dc.getCreateTime());%><%=formatter.format(timer) %></td><td><%timer = new java.util.Date(dc.getEndTime());%><%=formatter.format(timer) %></td>
			<td colspan="3">
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
		</tr>
		<%} %>
		</table>
		<%
	}
%>



<%
}else{
	out.println("instance error");
}
%>
</BODY></html>
