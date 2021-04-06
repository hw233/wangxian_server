<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.core.*,java.lang.reflect.*,com.google.gson.*,com.fy.engineserver.sprite.npc.*"%><%! 
	
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
	
MonsterManager npcm = MemoryMonsterManager.getMonsterManager();
MemoryMonsterManager.MonsterTempalte sts[] = ((MemoryMonsterManager)npcm).getMonsterTemaplates();
String categoryId = request.getParameter("categoryId");
if(categoryId != null && !categoryId.trim().equals("")){
	String mapName = request.getParameter("mapName");
	String x = request.getParameter("x");
	String y = request.getParameter("y");
	String country = request.getParameter("country");
	String count = request.getParameter("count");
	if(mapName != null && x != null && y != null){
		try{
			Random random = new Random();
			GameManager gm = GameManager.getInstance();
			//for(int j = 1; j <= 3; j++){
				Game game = gm.getGameByName(mapName.trim(),Integer.parseInt(country));
			for(int i = 0; i < Integer.parseInt(count); i++){
				Monster newNpc = npcm.createMonster(Integer.parseInt(categoryId));
				newNpc.setX(Integer.parseInt(x)+random.nextInt(300) - 150);
				newNpc.setY(Integer.parseInt(y)+random.nextInt(300) - 150);
				
				newNpc.setBornPoint(new Point(newNpc.getX(),newNpc.getY()));
				
					if(game != null){
						game.addSprite(newNpc);
						out.println("在"+mapName+"的("+newNpc.getX()+","+newNpc.getY()+")位置上产生一个id为"+newNpc.getId()+"的monster"+"血量:"+newNpc.getHp()+"/"+newNpc.getMaxHP()+"avata"+newNpc.getAvata().length+" avataRace"+newNpc.getAvataRace()+" avataSex"+newNpc.getAvataSex());
					}else{
						out.println("game wei kong "+ mapName+Integer.parseInt(country));
					}
				//}

			}
		}catch(Exception ex){
			out.println("产生monster出现异常<br/>");
			ex.printStackTrace();
		}
	}
}
	
	%>
<%@include file="IPManager.jsp" %>
<%@page import="com.fy.engineserver.sprite.npc.npcaction.NpcExecuteItem"%>
<%@page import="com.fy.engineserver.sprite.npc.npcaction.NpcAction"%>
<%@page import="com.fy.engineserver.sprite.npc.npcaction.SaySomething"%>
<%@page import="com.fy.engineserver.sprite.monster.MonsterManager"%>
<%@page import="com.fy.engineserver.sprite.monster.MemoryMonsterManager"%>
<%@page import="com.fy.engineserver.sprite.monster.Monster"%>
<%@page import="com.fy.engineserver.core.g2d.Point"%>
<%@page import="com.fy.engineserver.sprite.monster.BossMonster"%>
<%@page import="com.fy.engineserver.sprite.monster.BossExecuteItem"%>
<%@page import="com.fy.engineserver.sprite.monster.BossAction"%>
<%@page import="com.fy.engineserver.country.manager.CountryManager"%><html>
<head>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<link rel="stylesheet" type="text/css" href="../css/table.css" />
<style type="text/css">
.titlecolor{
background-color:#C2CAF5;
}
</style>
</HEAD>
<BODY>

<br/>
<form name= "f1" method="post">
monster编号:<input name="categoryId">&nbsp;产生地图:<input name="mapName">&nbsp;产生x坐标:<input name="x">&nbsp;产生y坐标:<input name="y">&nbsp;产生国家id:<input name="country">&nbsp;刷新只数:<input name="count">&nbsp;<input type="submit" value="在该位置产生monster">
</form>
<br/>
</BODY></html>
