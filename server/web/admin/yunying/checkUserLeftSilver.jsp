<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="java.io.*"%>
<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page
	import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%!HashMap<String, List<SU>> map = new HashMap<String, List<SU>>();%>
<%
	String thisServerName = GameConstants.getInstance().getServerName();
	out.print("<H1>" + thisServerName + "</H1>");
	TaskManager.logger.warn("<H1>" + thisServerName + "</H1>");
	String path = application.getRealPath(request.getRequestURI());
	{
		path = path.substring(0,path.lastIndexOf("/"));
		//addAllData
		File file = new File(path+"/11.txt");
		if (!file.exists()) {
			out.print("配置文件不存在" + file.getAbsolutePath() + "   realPath:" + path);
			return ;
		}
		TaskManager.logger.warn("==========================================文件存在 开始读数据==========================================");
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		try {
			String line = bufferedReader.readLine();
			while(line != null) {
				TaskManager.logger.warn("line:" + line);
				String [] data = line.split("\t");
				TaskManager.logger.warn("line:" + line + "," + Arrays.toString(data));
				put(data[1], data[0]);
				line = bufferedReader.readLine();
			}
		} catch (Exception e) {
			
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (Exception ee) {
					
				}
			}
		}
	}
	List<SU> list = null;
	if (!PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {//非韩国的
		list = map.get(thisServerName);
	} else {
		for (Iterator<String> itor = map.keySet().iterator(); itor.hasNext();) {
			String serverName = itor.next();
			if (serverName.startsWith(thisServerName)) {
				list = map.get(serverName);
			}
		}
	}
	if (list == null || list.size() == 0) {
		out.print("本服务器无任何数据");
		return;
	}
	TaskManager.logger.warn("本服务器账号数量:" + list.size());
	Set<String> error = new HashSet<String>();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看玩家剩余银子</title>
</head>
<body>
	<table style="font-size: 12px;" border="1">
		<tr
			style="font-size: 16px; background-color: black; font-weight: bold; text-align: center;color: white;">
			<td>索引</td>
			<td>服务器</td>
			<td>账号</td>
			<td>角色id</td>
			<td>角色name</td>
			<td>剩余银子(文)</td>
			<td>充值金额(分)</td>
		</tr>
		<%
			int index = 0;
			if (list != null && list.size() > 0) {
				for (SU su : list) {
					String userName = su.userName;
					Player[] players = GamePlayerManager.getInstance().getPlayerByUser(userName);
					if (players == null || players.length == 0) {
						error.add(userName);
						TaskManager.logger.warn("无效的用户名:" + userName);
					} else {
						for (Player p : players) {
						TaskManager.logger.warn("处理角色:" + p.getLogString());
		%>
		<tr style="color: <%=players.length > 1 ? "red" : ""%>;">
			<td><%=(++index)%></td>
			<td><%=thisServerName%></td>
			<td><%=userName%></td>
			<td><%=p.getId()%></td>
			<td><%=p.getName()%></td>
			<td><%=p.getSilver()%></td>
			<td><%=p.getRMB()%></td>
		</tr>
		<%
			}
					}
		%>

		<%
			}
			}
		%>
	</table>
</body>
</html>
<%!public void put(String serverName, String userName) {
		if (!map.containsKey(serverName)) {
			map.put(serverName, new ArrayList<SU>());
		}
		map.get(serverName).add(new SU(serverName, userName));
		TaskManager.logger.warn(serverName + "-" + userName);
	}%>
<%!public class SU {
		public String serverName, userName;

		public SU(String serverName, String userName) {
			this.serverName = serverName;
			this.userName = userName;
		}
	}%>