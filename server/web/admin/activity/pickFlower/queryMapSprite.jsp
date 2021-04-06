<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>



<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.sprite.Sprite"%>
<%@page import="com.fy.engineserver.sprite.npc.NPC"%>
<%@page import="com.fy.engineserver.sprite.monster.Monster"%>
<%@page import="com.fy.engineserver.core.MonsterFlushAgent"%>
<%@page import="com.fy.engineserver.core.NPCFlushAgent"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.core.MonsterFlushAgent.BornPoint"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.BufferedWriter"%>
<%@page import="java.io.OutputStreamWriter"%>


<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="com.fy.engineserver.core.GameInfo"%>
<%@page import="com.fy.engineserver.country.manager.CountryManager"%><html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>查询地图的精灵</title>
</head>

	<%!
	
		public void querySpriteOneMap(Game g,JspWriter out) throws Exception{
	
			Class clazz = Class.forName("com.fy.engineserver.core.Game");
			Field f = clazz.getDeclaredField("templateMonsterAndNpcsInGame");
			f.setAccessible(true);
			
			Field f1 = clazz.getDeclaredField("spriteFlushAgent");
			f1.setAccessible(true);
			
			Field f2 = clazz.getDeclaredField("npcFlushAgent");
			f2.setAccessible(true);
			
			

			
			Sprite[] ss = (Sprite[])f.get(g);
			List<Integer> idList = new ArrayList<Integer>();
			for(Sprite s : ss){
				if(s != null){
					out.print("name:"+s.getName()+"  ");
					if(s instanceof NPC){
						NPC n = (NPC)s;
						out.print("npcid:"+n.getnPCCategoryId()+"<br/>");
						idList.add(n.getnPCCategoryId());
					}else{
						if(s instanceof Monster){
							Monster m = (Monster)s;
							out.print("monsterid:"+m.getSpriteCategoryId()+"<br/>");
							idList.add(m.getSpriteCategoryId());
						}else{
							out.print("不是npc也不是monster<br/>");
						}
					}
				}else{
					out.print("null <br/>");
				}
			}			
			MonsterFlushAgent sa = (MonsterFlushAgent)f1.get(g);
			NPCFlushAgent na = (NPCFlushAgent)f2.get(g);
			
			
			Class clazz1 = Class.forName("com.fy.engineserver.core.MonsterFlushAgent");
			Field fm = clazz1.getDeclaredField("templateMap");
			fm.setAccessible(true);
			HashMap<MonsterFlushAgent.BornPoint,Integer> monsterBorn = (HashMap<BornPoint,Integer>)fm.get(sa);
			
			Class clazz2 = Class.forName("com.fy.engineserver.core.NPCFlushAgent");
			Field fn = clazz2.getDeclaredField("templateMap");
			fn.setAccessible(true);
			HashMap<MonsterFlushAgent.BornPoint,Integer> npcBorn = (HashMap<BornPoint,Integer>)fn.get(na);
			List<Integer> list = new ArrayList<Integer>();
			
			for(Map.Entry<MonsterFlushAgent.BornPoint,Integer> en1 : monsterBorn.entrySet()){
				if(!idList.contains(en1.getValue())){
					if(list.contains(en1.getValue())){
						continue;
					}
					list.add(en1.getValue());
					out.print(g.gi.getName()+"地图中不包含monsterId:"+ en1.getValue()+"<br/>");
				}
			}
			for(Map.Entry<MonsterFlushAgent.BornPoint,Integer> en2 : npcBorn.entrySet()){
				if(!idList.contains(en2.getValue())){
					if(list.contains(en2.getValue())){
						continue;
					}
					list.add(en2.getValue());
					out.print(g.gi.getName()+"地图中不包含npcId:"+ en2.getValue()+"<br/>");
				}
			}
			out.print("**************************<br/>");
		}
	
	%>


<body>
	
	<%
	
		String nums = request.getParameter("num");
		if(nums != null && !nums.equals("")){
			
			Player player = PlayerManager.getInstance().getPlayer(nums);
			if(player.isOnline()){
				Game g = player.getCurrentGame();
				querySpriteOneMap(g,out);
			}else{
				GameManager gm =  GameManager.getInstance();
				GameInfo[] infos = gm.getGameInfos();
				for(GameInfo info : infos){
					String name = info.getName();
					Game g = gm.getGameByName(name,CountryManager.中立);
					if(g == null){
						g = gm.getGameByName(name,1);
					}
					if(g == null ){
						out.print("没有这个地图"+name+"<br/>");
						continue;
					}
					querySpriteOneMap(g,out);
				}
				
			}
			return ;
		}
		
	%>
	
	<form action="">
	
		playerName:<input type="text" name="num"/>
		<input type="submit" value="submit">
	
	</form>
</body>
</html>
