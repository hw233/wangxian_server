<%@ page contentType="text/html;charset=utf-8"
	import="java.util.*,com.xuanzhi.tools.text.*,com.xuanzhi.tools.transport.*,
	com.google.gson.Gson,com.fy.engineserver.core.*,java.io.*,
	com.fy.engineserver.datasource.buff.*"%><%!
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
	Arrays.sort(bts,new Comparator<BuffTemplate>(){
		public int compare(BuffTemplate t1,BuffTemplate t2){
			if(t1.getBuffType() < t2.getBuffType()){
				return -1;
			}else if(t1.getBuffType() > t2.getBuffType()){
				return 1;
			}else{
				return t1.getName().compareTo(t2.getName());
			}
		}
	});
	List<BuffTemplate> tempList = new ArrayList<BuffTemplate>();
	for(BuffTemplate b : bts){
		tempList.add(b);
	}
	List<List<BuffTemplate>> lists = new ArrayList<List<BuffTemplate>>();
	List<BuffTemplate> list = new ArrayList<BuffTemplate>();
	while(true){
		for(BuffTemplate b : tempList){
			if(list.isEmpty()){
				list.add(b);
				continue;
			}
			if(list.get(0).getGroupId() == b.getGroupId()){
				list.add(b);
			}
		}
		lists.add(list);
		tempList.removeAll(list);
		list = new ArrayList<BuffTemplate>();
		if(tempList.isEmpty()){
			break;
		}
	}
	
	
	
	String timeTypeNames[] = new String[]{"游戏时间","真实时间","下线失效"};
	
%>
<%@include file="IPManager.jsp" %><html>
<head>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<link rel="stylesheet" type="text/css" href="../css/table.css" />
<style type="text/css">
.titlecolor{
background-color:#C2CAF5;
}
</style>
</HEAD>
<BODY>
<h2>Buff模板数据</h2>
<br>
<a href="./buffs.jsp">刷新此页面</a>
<br>
<br>
<table>
	<tr class="titlecolor">
		<td>组名</td>
		<td>名称</td>
		<td>图标</td>
		<!--<td>Java类</td>-->
		<td>时间类型</td>
		<td nowrap>是否客户端同步</td>
		<td nowrap>有益or有害</td>
		<td>BUFF描述</td>
		<!--<td>JSON串</td>-->
	</tr>
	<%
	if(!lists.isEmpty()){
		
		int listsSize = lists.size();
		for (int i = 0; i < listsSize; i++) {
			List<BuffTemplate> l = lists.get(i);
			if(!l.isEmpty()){
				BuffTemplate b = l.get(0);

				String cName = b.getClass().getSimpleName();
				String groupName = "";
				if(b != null && groupmaps.get(b.getGroupId()) != null){
					groupName = groupmaps.get(b.getGroupId()).getGroupName();
				}
				%>
				<tr>
					<td rowspan="<%=l.size() %>"><%= groupName %></td>
					<%
					String buffName=java.net.URLEncoder.encode(b.getName()); 
					%>
					<td><a href="buffbyname.jsp?buffName=<%=buffName %>"><%= b.getName() %></a></td>
					<td><img src="/game_server/imageServlet?id=<%=b.getIconId() %>"></td>
					<!--<td><%= cName %></td>-->
					<%String color = "";
					if(b.getTimeType()==0){
						color = "";
					}else if(b.getTimeType()==1){
						color = "#C2CAF5";
					}else{
						color = "#C9C91F";
					}
					
					
					%>
					<td bgcolor="<%=color %>" nowrap="nowrap"><%= timeTypeNames[b.getTimeType()] %></td>
					<td><%= b.isSyncWithClient()?"同步":"不同步"%></td>
					<td><%= b.isAdvantageous()?"有益":"有害"%></td>
					<td><%= b.getDescription() %></td>
					<!--<td><%= aaa(gson.toJson(b),75) %></td>-->
				</tr>
				<%
				for(int j = 1; j < l.size(); j++){
				BuffTemplate bt = l.get(j);
				String className = bt.getClass().getSimpleName();
				if(className.lastIndexOf(".") > 0){
					className = className.substring(className.lastIndexOf(".")+1);
				}
				if(bt.getTimeType()==0){
					color = "";
				}else if(bt.getTimeType()==1){
					color = "#C2CAF5";
				}else{
					color = "#C9C91F";
				}
				%><tr>
				<%
				buffName=java.net.URLEncoder.encode(bt.getName()); 
				%>
					<td><a href="buffbyname.jsp?buffName=<%=buffName %>"><%= bt.getName() %></a></td>
					<td><img src="/game_server/imageServlet?id=<%=bt.getIconId() %>"></td>
					<!--<td><%= className %></td>-->
					<td bgcolor="<%=color %>" nowrap="nowrap"><%= timeTypeNames[bt.getTimeType()] %></td>
					<td><%= bt.isSyncWithClient()?"同步":"不同步"%></td>
					<td><%= bt.isAdvantageous()?"有益":"有害"%></td>
					<td><%= bt.getDescription() %></td>
					<!-- <td><%= aaa(gson.toJson(bt),75) %></td> -->
				</tr>
				<%
				}
			}
		}
	}
	%>
</table>

</BODY>
</html>
