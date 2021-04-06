<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.io.File"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="java.util.Random"%>
<%@page import="org.apache.commons.lang.math.RandomUtils"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>

<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	//List<PlayerData> dataList = new ArrayList<PlayerData>();
	Map<String, PlayerData> dataMap = new HashMap<String, PlayerData>();
	String filePath = request.getRealPath("/") + "admin/sym/modifyPlayerInfo.txt";
	File file = new File(filePath);
	if (file.isFile() && file.exists()) { //判断文件是否存在
		InputStreamReader read = new InputStreamReader(new FileInputStream(file), "GBK");
		BufferedReader bufferedReader = new BufferedReader(read);
		String lineTxt = null;
		while ((lineTxt = bufferedReader.readLine()) != null) {
			String[] arr = lineTxt.split("\t");
			if (arr != null && arr.length > 0) {
				if (arr[0] != null && !"".equals(arr[0]) && arr[0].equals(GameConstants.getInstance().getServerName())) {
					//PlayerData data = new PlayerData(arr[1], arr[2], arr[3], Long.valueOf(arr[4]));
					//dataList.add(data);
					if (dataMap.keySet().contains(arr[1])) {
						PlayerData pdata = dataMap.get(arr[1]);
						pdata.setMoney(pdata.getMoney() + Long.valueOf(arr[4]));
						dataMap.put(arr[1], pdata);
					} else {
						PlayerData data = new PlayerData(arr[1], arr[2], arr[3], Long.valueOf(arr[4]));
						dataMap.put(arr[1], data);
					}
				} else {
					//out.print("没有读取到服务器或者服务器不是当前服<br>");
				}
			} else {
				out.print("没有读取到数据<br>");
			}

		}
		read.close();
	}
	StringBuffer sbf = new StringBuffer();
	if (dataMap.size() > 0) {
		out.print("角色名 id 银子<br>");
		//SimpleEntityManager<Player> em = SimpleEntityManagerFactory.getSimpleEntityManager(Player.class);
		for (String name : dataMap.keySet()) {
			PlayerData data = dataMap.get(name);
			try {
				Player p = PlayerManager.getInstance().getPlayer(name);
				if (p != null) {
					out.print(p.getName() + " " + p.getId() + " " + p.getSilver() + "<br>");
				}
			} catch (Exception e) {
				sbf.append(data.getPlayerName() + "<br>");
			}
		}
	}
	if (sbf.length() > 0) {
		out.print("<hr>数据设置失败,玩家不存在,角色名:<br>" + sbf.toString() + "<br>");
	}
%>

<%!class PlayerData {
		private String playerName;
		//private String serverName;
		private String startLoginStr;
		//private long startLoginTime;
		private String endLoginStr;
		//private long endLoginTime;
		private long money;

		public PlayerData(String playerName, String startLoginStr, String endLoginStr, long money) {
			this.playerName = playerName;
			//this.serverName = serverName;
			this.startLoginStr = startLoginStr;
			//this.startLoginTime = TimeTool.formatter.varChar19.parse(startLoginStr);
			this.endLoginStr = endLoginStr;
			//this.endLoginTime = TimeTool.formatter.varChar19.parse(endLoginStr);
			this.money = money;
		}

		public String getPlayerName() {
			return playerName;
		}

		public void setPlayerName(String playerName) {
			this.playerName = playerName;
		}

		public String getStartLoginStr() {
			return startLoginStr;
		}

		public void setStartLoginStr(String startLoginStr) {
			this.startLoginStr = startLoginStr;
		}

		public String getEndLoginStr() {
			return endLoginStr;
		}

		public void setEndLoginStr(String endLoginStr) {
			this.endLoginStr = endLoginStr;
		}

		public long getMoney() {
			return money;
		}

		public void setMoney(long money) {
			this.money = money;
		}

		public String getString(String time) {
			return "[后台设置玩家信息] [最后登录时间:" + time + "] [银子:" + money + "文]";
		}

	}%>
</body>
</html>