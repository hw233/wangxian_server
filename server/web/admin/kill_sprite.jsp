<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.core.*,java.lang.reflect.*,com.google.gson.*,com.fy.engineserver.sprite.monster.*"%><%! 
	
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
%><% 
	
MonsterManager sm = MemoryMonsterManager.getMonsterManager();
MemoryMonsterManager.MonsterTempalte sts[] = ((MemoryMonsterManager)sm).getMonsterTemaplates();
GameManager gm = GameManager.getInstance();
String map = request.getParameter("map");
String monsterId = request.getParameter("monsterId");
if(map != null && monsterId != null){
	Monster monster = sm.getMonster(Long.parseLong(monsterId));
	Game game = gm.getGameByName(map,0);
	if(game == null){
		game = gm.getGameByName(map,1);
	}
	Player p = PlayerManager.getInstance().getPlayer("a");
	monster.setOwner(p);
	monster.killed(System.currentTimeMillis(),0,game);
	out.println("怪物掉落物品");
}
	%>
<%@include file="IPManager.jsp" %><html><head>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<link rel="stylesheet" type="text/css" href="../css/table.css" />
<style type="text/css">
.titlecolor{
background-color:#C2CAF5;
}
</style>
</HEAD>
<BODY>
<form>
地图名:<input name="map"><br/>
怪物id:<input name="monsterId"><br/>
<input type="submit" value="杀">
</form>
</BODY></html>
