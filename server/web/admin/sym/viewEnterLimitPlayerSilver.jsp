<%@page import="com.fy.engineserver.enterlimit.EnterLimitManager"%>
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
	long startTime = System.currentTimeMillis();
	String targetFile = URLEncoder.encode(serverName + "_封号", "utf-8") + ".txt"; 
	response.setContentType("text/x-msdownload"); 
	response.addHeader("Content-Disposition", 
            "attachment;   filename=\"" + targetFile + "\""); 
	OutputStream os = response.getOutputStream(); 

	String timeDay = request.getParameter("timeDay");
	if (timeDay == null) {
		timeDay = "2015-10-01 00:00:00";
	}
	long tt = Timestamp.valueOf(timeDay).getTime();
	String sql = "username = ? and quitGameTime > ? and silver > ? ";
	Object[] obj = new Object[]{"", tt, 0};
	String[] enterLimitUsernames = EnterLimitManager.getInstance().limited.toArray(new String[0]);
	for (int i=0; i<enterLimitUsernames.length; i++) {
		obj[0] = enterLimitUsernames[i];
		List<Long> playerIds2 = this.collectPlayerIds(sql, obj);
		if (playerIds2 != null && playerIds2.size() > 0) {
			long[] ll = new long[playerIds2.size()];
			for (int j=0; j<playerIds2.size(); j++) {
				ll[j] = playerIds2.get(j);
			}
			List<PlayerSilverInfo> infos = getPlayerSilverInfos(ll);
			for (int k=0; k<infos.size(); k++) {
				String str = "服务器:" + serverName + " \t账号:" + infos.get(k).getUsername() + "\t角色id:" + infos.get(k).getId() + "\t最后登录时间:"+ new Timestamp(infos.get(k).getQuitGameTime()) +  "\t等级:" + infos.get(k).getLevel() + "\t银子:" + infos.get(k).getSilver() + "\n";
				os.write(str.getBytes());
			}
		}
		try {
			Thread.sleep(2);
		} catch (Exception e) {
			
		}
	}
	os.close();
	response.flushBuffer();
	out.println("***************耗时:" + (System.currentTimeMillis() - startTime) + "ms*********************************<br>");
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
		public long getQuitGameTime();
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
