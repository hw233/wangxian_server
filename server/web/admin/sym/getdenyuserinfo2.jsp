<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.FilenameFilter"%>
<%@page import="java.io.File"%>
<%@page import="com.fy.engineserver.playerAims.manager.PlayerAimManager"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.boss.client.BossClientService"%>
<%@page import="com.fy.boss.authorize.model.Passport"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
</html>
<head>
<title>test</title>
</head>
<body>

<%
	//角色  等级  rmb 最后登录时间
	String servername = GameConstants.getInstance().getServerName();
	List<String> usernames = new ArrayList<String>();
	//String[] username = new String[]{"c8169230","limuzi2"};
	String filePath = PlayerAimManager.instance.getFileName().split("webapp")[0] + File.separator + "log" + File.separator + "game_server";
	File f = new File(filePath);
	File ffs [] = f.listFiles(new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			// TODO Auto-generated method stub
			return name.contains(".txt");
		}
	});
	if (ffs.length != 1) {
		out.println("检查此服务器，路径下txt文件不唯一！ 有  " + ffs.length + "个txt文件！<br>");
		return ;
	}
	BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ffs[0])));
	String line;
	while((line=br.readLine()) != null){
		if (line.isEmpty() || line.contains("服务器")) {
			continue;
		}
		usernames.add(line);
	}
	String[] username = usernames.toArray(new String[0]);
	List<Player> player = new ArrayList<Player>();
	for (int i=0; i<username.length; i++) {
		long[] ids = ((GamePlayerManager)GamePlayerManager.getInstance()).em.queryIds(Player.class, "username=?", new Object[] { username[i] }, "", 1, 100);
		
		for (int k=0; k<ids.length; k++) {
			Player p = ((GamePlayerManager)GamePlayerManager.getInstance()).em.find(ids[k]);
			if (p != null) {
				player.add(p);
			}
		}
		
		/* Player[] ps = GamePlayerManager.getInstance().getPlayerByUser(username[i]);
		if (ps != null && ps.length > 0) {
			for (int j=0; j<ps.length; j++) {
				player.add(ps[j]);
			}
		} else {
			out.println("未发现角色:" + username[i] + "<br>");
		} */
	}
%>
<table style="text-align: right; font-size: 12px; float: left; border-width: 20%;" border="1">
<tr>
	<th>服务器名</th>
	<th>账号</th>
	<th>角色名</th>
	<th>角色id</th>
	<th>等级</th>
	<th>充值(元)</th>
	<th>最后进入游戏时间</th>
</tr>
<%	
	int index = 0;
	for (int i=0; i<player.size(); i++) {
		Player p = player.get(i);
		%>
		<tr bgcolor="<%=(index++ % 2) == 0 ? "#FFFFFF" : "#8CACE8"%>">
			<td><%=servername%></td>
			<td><%=p.getUsername()%></td>
			<td><%=p.getName() %></td>
			<td><%="'"+p.getId()+"" %></td>
			<td><%=p.getLevel() %></td>
			<td><%=(p.getRMB() / 1000) %></td>
			<td><%="'"+(new Timestamp(p.getEnterGameTime())).toString() %></td>
		</tr>
		<%
	}
%>
</table>
</body>
</html>
