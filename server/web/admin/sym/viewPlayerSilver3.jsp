<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.constants.GameConstant"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
</html>
<head>
<title>test</title>
</head>
<body>
<%
try {
	String serverName = GameConstants.getInstance().getServerName();
	
	String targetFile = URLEncoder.encode(serverName+"_01-10数据", "utf-8") + ".txt"; 
	response.setContentType("text/x-msdownload"); 
	response.addHeader("Content-Disposition", 
            "attachment;   filename=\"" + targetFile + "\""); 
	OutputStream os = response.getOutputStream(); 

	String timeDay = request.getParameter("timeDay");
	String timeDay2 = request.getParameter("td2m");
	if (timeDay == null) {
		timeDay = "2015-01-01 00:00:00";
		timeDay2 = "2015-10-01 00:00:00";
	}
	long startTime = System.currentTimeMillis();
	long time2 = Timestamp.valueOf(timeDay).getTime();
	long time3 = Timestamp.valueOf(timeDay2).getTime();
//	String sql = "RMB = ? and quitGameTime < ?";
	String sql2 = "RMB = ? and quitGameTime >= ? and quitGameTime <= ? and silver > ?";
	Object[] obj = new Object[]{0, time2};
	Object[] obj2 = new Object[]{0, time2,time3, 0};
	//List<Long> playerIds2 = this.collectPlayerIds(sql, obj);
	List<Long> playerIds = this.collectPlayerIds(sql2, obj2);
	int listIndex = 0;
	int index = 0;
	while (playerIds.size() > listIndex) {
		int startIndex = listIndex;
		int endIndex = playerIds.size();
		if (endIndex - startIndex > 2000) {
			endIndex = startIndex + 2000;
		}
		listIndex = endIndex;
		long[] ids = new long[endIndex - startIndex];
		for (int i=0; i<ids.length; i++) {
			ids[i] = playerIds.get(i+startIndex);
		}
		List<PlayerSilverInfo> list = this.getPlayerSilverInfos(ids);
		if (list != null) {
			for (int i=0; i<list.size(); i++) {
				String str = "服务器:" + serverName + " \t账号:" + list.get(i).getUsername() + "\t角色id:" + list.get(i).getId() + "\t等级:" + list.get(i).getLevel() + "\t银子:" + list.get(i).getSilver() + "\n";
				//out.println( + "<br>");
				os.write(str.getBytes());
				index++;
			}
		}
	}
	String str2 = "**********************************有银子的总人数:" + index + "****************************************\n";
	//String str3 = "**********************************2015-05-30之后未登陆切没有充值的人总数:" + playerIds2.size() + "***********************\n";
	os.write(str2.getBytes());
	//os.write(str3.getBytes());
	os.close();
	response.flushBuffer(); 
	//out.println("**********************************有银子的总人数:" + index + "****************************************<br>");
	//out.println("**********************************2015-05-30之后未登陆切没有充值的人总数:" + playerIds2.size() + "***********************<br>");
	//out.println("****************************消33耗时间:" + (System.currentTimeMillis() - startTime) + "ms ***************************<br>");
} catch (Exception e) {
	out.println("生成txt出错");
	TransitRobberyManager.logger.warn("生成txt出错", e);
} 
	
%>
<%!
	interface PlayerSilverInfo{
		public long getId();
		public int getLevel();
		public long getSilver();
		public String getUsername();
	}

	public List<Long> collectPlayerIds(String sql, Object[] obj) {
		List<Long> playerIds = new ArrayList<Long>();
		try {
			long count = ((GamePlayerManager)GamePlayerManager.getInstance()).em.count(Player.class, sql, obj);  //count(Player.class, sql);
			long[] tempId = ((GamePlayerManager)GamePlayerManager.getInstance()).em. queryIds(Player.class, sql, obj); //queryIds(Player.class, sql, obj, 1, count + 1);
			for (long id : tempId) {
				if (!playerIds.contains(id)) {
					playerIds.add(id);
				}
			}
		} catch (Exception e) {
			TransitRobberyManager.logger.warn("[查找角色id] [异常]", e);
		}
		return playerIds;
	}

	public List<PlayerSilverInfo> getPlayerSilverInfos(long[] ids){
		long[] idss = ids;
		
		SimpleEntityManager<Player> em = ((GamePlayerManager)GamePlayerManager.getInstance()).em;
		if(idss != null && idss.length > 0){
			List<PlayerSilverInfo> list = null;
			try {
				list = em.queryFields(PlayerSilverInfo.class, idss);
				List<PlayerSilverInfo> sortList = new ArrayList<PlayerSilverInfo>();
				for(long id : idss){
					for(PlayerSilverInfo ib : list){
						if(id == ib.getId()){
							sortList.add(ib);
							break;
						}
					}
				}
				return sortList;
			} catch (Exception e) {
				TransitRobberyManager.logger.warn("[异常]", e);
			}
		}else{
			TransitRobberyManager.logger.warn("[没查到数据]");
		}
		return null;
	}

%>
</table>
</body>
</html>
