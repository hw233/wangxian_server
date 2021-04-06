<%@ page contentType="text/html;charset=utf-8" import="java.util.*"%>
<%@page import="com.xuanzhi.tools.text.JsonUtil"%>
<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%>
<%@page import="java.net.URL"%>
<%!
public static class EnterLimitData {
	public List<String> username = new ArrayList<String>();
	public List<String> playername = new ArrayList<String>();
	public List<String> gameName = new ArrayList<String>();
	public List<Integer> level = new ArrayList<Integer>();// 在线是准的不在线的不准
	public List<String> network = new ArrayList<String>();
	public List<Long> playerId = new ArrayList<Long>();
	public List<Boolean> online = new ArrayList<Boolean>();
}
%>
<%
String args = "authorize.username=lvfei&authorize.password=lvfei321&enterLimit=true&maxSize=5";


%>
<html><head>
<script type="text/javascript">
function kick(serverName){
	var strs = document.getElementsByName(serverName+"_check");
	var urlStr = "xxxxxx/admin/"+"";//把从checkbox中读取到的值当做参数传到页面中
	window.location.href=urlStr;
	
}

function fenghao(serverName){
		var strs = document.getElementsByName(serverName+"_check");
	var urlStr = "xxxxxx/admin/waigua/enterLimit.jsp"+"";//把从checkbox中读取到的值当做参数传到页面中
	window.location.href=urlStr;
}
</script>

</HEAD>
<BODY>


<%
for(int i = 0; i < 1; i++){
	byte[] b = HttpUtils.webPost(new URL("xxxxxx/admin/waigua/enterLimit.jsp"), args.getBytes(), new HashMap(), 60000, 60000);
	if(b != null && b.length > 0){
		String json = new String(b);
		HashMap<String,EnterLimitData> eldMap = JsonUtil.objectFromJson(json,HashMap.class);//好像这里不能用泛型，如果无法用的话，就用其他结构吧
		String serverIp = "";
		String serverName = "";
		%>
		<form name="f<%=i %>">
		<input type="hidden" name="<%=serverName %>_serverIp" value=<%=serverIp %>>
		<table>
			<tr><td>IP</td><td>用户名</td><td>角色名</td><td>玩家ID</td><td>地图名</td><td>级别</td><td>网络</td><td>在线</td></tr>
			<%
			for(String key : eldMap.keySet()){
				EnterLimitData eld = eldMap.get(key);
				if(eld != null && eld.username != null){
					%>
					<tr><td rowspan="<%=eld.username.size() > 0?eld.username.size():1 %>"><%=key %><input type="checkbox" name="<%=serverName %>_ipcheck" value="true"> 封现在这个ip下的用户 </td>
					<%
					for(int j = 0; j < eld.username.size(); j++){
						%>
							<td><input type="checkbox" name="<%=serverName %>_check" value="<%=eld.username.get(j) %>"><%=eld.username.get(j) %></td><td><%=eld.playername.get(j) %></td><td><%=eld.playerId.get(j) %></td><td><%=eld.gameName.get(j) %></td><td><%=eld.level.get(j) %></td><td><%=eld.network.get(j) %></td><td><%=eld.online.get(j) %></td>
						<%
					}
					%>
					</tr>
					<%
				}
			}
			%>
		</table><input type="button" value="踢下线" onclick="kick(<%=serverName %>);"> 
		<input type="button" value="禁止进入游戏" onclick="fenghao(<%=serverName %>);"> 
		</form>
		<%
	}
	
}
%>
</BODY></html>
