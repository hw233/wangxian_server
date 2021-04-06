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
	if (readNum > 0) {
		out.print("不能重复访问此页面<br>");
		return;
	}
	long currentTime = System.currentTimeMillis();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String startTime = "2016-01-26 00:00:00";
	String endTime = "2016-01-28 12:00:00";

	if (sdf.parse(startTime).getTime() > currentTime || sdf.parse(endTime).getTime() < currentTime) {
		out.print("不在访问时间段内<br>");
		return;
	}

	//List<PlayerData> dataList = new ArrayList<PlayerData>();
	Map<String, PlayerData> dataMap = new HashMap<String, PlayerData>();
	String filePath = request.getRealPath("/") + "admin/sym/modifyPlayerInfo.txt";
	File file = new File(filePath);
	int lineNum = 0;
	int currNum = 0;
	int sameNum = 0;
	if (file.isFile() && file.exists()) { //判断文件是否存在
		InputStreamReader read = new InputStreamReader(new FileInputStream(file), "GBK");
		BufferedReader bufferedReader = new BufferedReader(read);
		String lineTxt = null;
		while ((lineTxt = bufferedReader.readLine()) != null) {
			lineNum++;
			String[] arr = lineTxt.split("\t");
			if (arr != null && arr.length > 0) {
				if (arr[0] != null && !"".equals(arr[0]) && arr[0].equals(GameConstants.getInstance().getServerName())) {
					currNum++;
					//PlayerData data = new PlayerData(arr[1], arr[2], arr[3], Long.valueOf(arr[4]));
					//dataList.add(data);
					if (dataMap.keySet().contains(arr[1])) {
						sameNum++;
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
	int succNum = 0;
	int failNum = 0;
	/*if (dataList.size() > 0) {
		out.print("<hr>设置数据：<br>");
		for (PlayerData data : dataList) {
			try {
				Player p = PlayerManager.getInstance().getPlayer(data.getPlayerName());
				if (p != null) {
					Date randomDate = randomDate(data.getStartLoginStr(), data.getEndLoginStr());
					p.setLastRequestTime(randomDate.getTime());
					p.setSilver(data.getMoney()); //加银子
					out.print(data.getString(sdf.format(randomDate)) + "<br>");
					succNum++;
				}
			} catch (Exception e) {
				failNum++;
				sbf.append(data.getPlayerName() + "<br>");
			}
		}
		readNum1++;
	}*/
	if (dataMap.size() > 0) {
		SimpleEntityManager<Player> em = SimpleEntityManagerFactory.getSimpleEntityManager(Player.class);
		for (String name : dataMap.keySet()) {
			PlayerData data = dataMap.get(name);
			try {
				Player p = PlayerManager.getInstance().getPlayer(name);
				if (p != null) {
					Date randomDate = randomDate(data.getStartLoginStr(), data.getEndLoginStr());
					p.setQuitGameTime(randomDate.getTime());
					p.setSilver(data.getMoney()); //加银子
					em.flush(p);
					out.print(data.getString(sdf.format(randomDate)) + "<br>");
					succNum++;
				}
			} catch (Exception e) {
				failNum++;
				sbf.append(data.getPlayerName() + "<br>");
			}
		}
		readNum++;
	}
	out.print("读取数据共计:" + lineNum + "条<br>");
	out.print("当前服:" + currNum + "条<br>");
	out.print("重复:" + sameNum + "条<br>");
	out.print("设置成功数量:" + succNum + "<br>");
	out.print("设置失败数量:" + failNum + "<br>");
	out.print((currNum - sameNum) == (succNum + failNum) ? "true" : "false");
	out.print("<br>");
	if (sbf.length() > 0) {
		out.print("<hr>数据设置失败,玩家不存在,角色名:<br>" + sbf.toString() + "<br>");
	}
%>

<%!public int readNum = 0;

	class PlayerData {
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

	}

	//Date randomDate=randomDate("2010-09-20","2010-09-21");格式
	private static Date randomDate(String beginDate, String endDate) {

		try {

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			Date start = format.parse(beginDate);//构造开始日期  

			Date end = format.parse(endDate);//构造结束日期  

			//getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。  

			if (start.getTime() >= end.getTime()) {

				return null;

			}

			long date = random(start.getTime(), end.getTime());

			return new Date(date);

		} catch (Exception e) {

			e.printStackTrace();

		}

		return null;

	}

	private static long random(long begin, long end) {

		long rtn = begin + (long) (Math.random() * (end - begin));

		//如果返回的是开始时间和结束时间，则递归调用本函数查找随机值  

		if (rtn == begin || rtn == end) {

			return random(begin, end);

		}

		return rtn;

	}%>
</body>
</html>