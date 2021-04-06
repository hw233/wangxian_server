<%@page import="com.xuanzhi.tools.transport.Connection"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	Player [] onlinePlayer = GamePlayerManager.getInstance().getOnlinePlayers();

	Map<String,Integer> IPgroup = new Hashtable<String,Integer>();
	for (Player p : onlinePlayer) {
		Connection conn = p.getConn();
		String ip = conn.getRemoteAddress();
		if (ip.indexOf(":") > 0) {
			ip = ip.substring(0, ip.indexOf(":"));
		}
		if (!IPgroup.containsKey(ip)) {
			IPgroup.put(ip, 0);
		}
		IPgroup.put(ip, IPgroup.get(ip) + 1);
	}

	Map<Integer,Temp> result = new Hashtable<Integer,Temp>();
	for (Iterator<String> itor = IPgroup.keySet().iterator();itor.hasNext();) {
		String ip = itor.next();
		int playerNum = IPgroup.get(ip);
		if (!result.containsKey(playerNum)) {
			result.put(playerNum, new Temp(playerNum));	
		}
		result.get(playerNum).ips.add(ip);
	}
%>
<%!
	class Temp {
	public List<String> ips = new ArrayList<String>();
	public int playerNum;
	
	public Temp(int playerNum) {
		this.playerNum =  playerNum;
	}
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<table style="font-size: 12px;" border="1">
	<tr style="font-size: 14px;font-weight: bold;color: white;background-color: black;">
		<td>同IP人数</td>
		<td>IP个数</td>
	</tr>
	<%
		for(Iterator<Integer> itor = result.keySet().iterator();itor.hasNext();) {
			int playerNum = itor.next();
			List<String> list = result.get(playerNum).ips;
		%>
		<tr>
			<td><%=playerNum %></td>
			<td><%=list.size() %></td>
		</tr>
		<%
		}
	%>	
</table>
</body>
</html>