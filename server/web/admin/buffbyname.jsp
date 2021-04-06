<%@ page contentType="text/html;charset=utf-8"
	import="java.util.*,com.xuanzhi.tools.text.*,com.xuanzhi.tools.transport.*,
	com.google.gson.Gson,com.fy.engineserver.core.*,java.io.*,
	com.fy.engineserver.datasource.buff.*"%>
<%@page import="java.net.URLDecoder"%><%!
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
<%
	Gson gson = new Gson();
	BuffTemplateManager btm = BuffTemplateManager.getInstance();
	HashMap<String,BuffTemplate> maps = btm.getTemplates();	
	HashMap<Integer,BuffGroup> groupmaps = btm.getGroups();
	BuffTemplate bts[] = maps.values().toArray(new BuffTemplate[0]);
	String buffName = request.getParameter("buffName");
	String type = request.getParameter("systype");
	if("new".equals(type)){
		buffName = URLDecoder.decode(buffName, "utf-8");
	}
	BuffTemplate b = null;
	String timeTypeNames[] = new String[]{"游戏时间","真实时间","下线失效"};
	if(buffName != null){
		for(BuffTemplate bt : bts){
			if(buffName.equals(bt.getName())){
				b = bt;
				break;
			}
		}
	}
%>
<%@include file="IPManager.jsp" %><html>
<head>
</HEAD>
<BODY>
<h2>各个Buff模板的数据</h2>
<input type="button" value="返回" onclick="javascript:history.back();">
<br>
<br>
<table border="0" cellpadding="0" cellspacing="1" width="100%"
	bgcolor="#000000" align="center">
	<tr bgcolor="#C2CAF5" align="center">
		<td>名称</td>
		<td>ID</td>
		<td>组名</td>
		<td>组ID</td>
		<td>图标</td>
		<td>Java类</td>
		<td>时间类型</td>
		<td>是否客户端同步</td>
		<td>有益or有害</td>
		<td>战斗状态停止</td>
		<td>BUFF描述</td>
		<td>JSON串</td>
	</tr>
	<%
	if(b != null){
		String cName = b.getClass().getSimpleName();
		String groupName = "";
		if(groupmaps.get(b.getGroupId()) != null){
			groupName = groupmaps.get(b.getGroupId()).getGroupName();
		}
		%>
		<tr bgcolor="#FFFFFF" align="center">
			<td><%= b.getName() %></td>
			<td><%= b.getId() %></td>
			<td><%= groupName %></td>
			<td><%= b.getGroupId() %></td>
			<td><img src="/game_server/imageServlet?id=<%=b.getIconId() %>"></td>
			<td><%= cName %></td>
			<td><%= timeTypeNames[b.getTimeType()] %></td>
			<td><%= b.isSyncWithClient()?"同步":"不同步"%></td>
			<td><%= b.isAdvantageous()?"有益":"有害"%></td>
			<td><%= b.isFightStop()?"是":"不停止"%></td>
			<td><%= b.getDescription() %></td>
			<td><%= aaa(gson.toJson(b),75) %></td>
		</tr>
		<%
	}else{
		out.println("没有名为该名称的buff");
	}
	%>
</table>
<br>
<input type="button" value="返回" onclick="javascript:history.back();">
</BODY>
</html>
