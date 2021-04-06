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
<%@page import="com.fy.engineserver.core.LivingObject"%>
<%@page import="com.fy.engineserver.activity.fateActivity.FateManager"%>
<%@page import="com.fy.engineserver.activity.fateActivity.base.FateActivity"%>
<%@page import="com.xuanzhi.tools.cache.CacheObject"%>
<%@page import="com.fy.engineserver.core.GameManager"%><html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>查询地图的精灵</title>
</head>

<body>
	
	
	<%
	
		String nums = request.getParameter("num");
		if(nums != null && !nums.equals("")){
			
			Player p = PlayerManager.getInstance().getPlayer(nums);
			
			List<Game> gameList = new ArrayList<Game>();
			Game g1 = GameManager.getInstance().getGameByName("taoyuanxianjing",1);
			Game g2 = GameManager.getInstance().getGameByName("taoyuanxianjing",2);
			Game g3 = GameManager.getInstance().getGameByName("taoyuanxianjing",3);
			if(g1 != null){
				gameList.add(g1);
			}else{
				out.print("country null");
			}
			
			if(g2 != null){
				gameList.add(g2);
			}else{
				out.print("country null");
			}
			
			if(g3 != null){
				gameList.add(g3);
			}else{
				out.print("country null");
			}
			for(Game g :gameList){
			
				List<NPC> listNpc = new ArrayList<NPC>();
				LivingObject[] los = g.getLivingObjects();
				int i= 0;
				for(LivingObject lo:los){
					if(lo instanceof NPC){
						long nid = ((NPC)lo).getId();
						long tnid = ((NPC)lo).getNPCCategoryId();
						if(tnid == 500002 || tnid == 500003){
							out.print(i+" :" + ((NPC)lo).getName()+"npcId:"+nid+"CategoryId"+tnid+"<br/>");
							listNpc.add((NPC)lo);
						}
					}
					++i;
				}
				
				FateManager fm = FateManager.getInstance();
				Object os[] = fm.mCache.values().toArray(new Object[0]);
				
				ArrayList<Long> list = new ArrayList<Long>();
				for(Object o : os){
					FateActivity fa = (FateActivity) ((CacheObject) o).object;
					long npcId = fa.getNpcId();
					if(npcId > 0){
						list.add(npcId);
					}
				}
				
				for(NPC npc:listNpc){
					boolean delete = true;
					for(long id:list){
						if(npc.getId() == id){
							delete = false;
							break;
						}
					}
					if(delete){
						g.removeSprite(npc);
					}
				}
			}
			out.print("success");
		}
		
	%>
	
	<form action="">
	
		playerName:<input type="text" name="num"/>
		<input type="submit" value="submit">
	
	</form>
</body>
</html>
