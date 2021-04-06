<%@page import="net.sf.json.JSONArray"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiPlayerInfo"%>
<%@page import="java.text.SimpleDateFormat"%><%@page import="java.util.*"%>
<%@page import="java.io.UnsupportedEncodingException"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiPlayerInfoManager"%><%@ page language="java" contentType="text/html; charset=utf-8"%><%!String[] careerNames = { "通用", "斗罗", "鬼煞", "灵尊", "巫皇","兽魁" };public String getCareerName(int career){return this.careerNames[career];}%>
<%
MieshiPlayerInfoManager mpm = MieshiPlayerInfoManager.getInstance();
String playername = request.getParameter("playername");
String playerId =  request.getParameter("playerid");;
List<MieshiPlayerInfo> infolist = null;
if (playername != null && !playername.isEmpty()) {
	playername = URLDecoder.decode(playername.trim(),"utf-8");
  infolist = mpm.getMieshiPlayerInfoByPlayernameFromDB(playername);
}
if (playerId != null && !playerId.isEmpty()) {
	playerId = URLDecoder.decode(playerId.trim(),"utf-8");
	infolist = mpm.getMieshiPlayerInfoByPlayernameFromDB(Long.parseLong(playerId));
}
List list = new ArrayList();
SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
for (MieshiPlayerInfo info : infolist) {
  Map map = new HashMap();
  map.put("userName", info.getUserName());
  map.put("serverRealName", info.getServerRealName());
  map.put("playerId", Long.valueOf(info.getPlayerId()));
  map.put("playerName", info.getPlayerName());
  map.put("career", getCareerName(info.getCareer()));
  map.put("level", Integer.valueOf(info.getLevel()));
  map.put("playerRMB", Integer.valueOf(info.getPlayerRMB()));
  map.put("playerVIP", Integer.valueOf(info.getPlayerVIP()));
  map.put("lastAccessTime", format.format(Long.valueOf(info.getLastAccessTime())));
  list.add(map);
}
out.print(JSONArray.fromObject(list).toString());
%>

