<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@page import="com.xuanzhi.tools.simplejpa.*"%>
<%@page import="com.fy.engineserver.sprite.*"%>

<%@page import="java.util.*"%><html>
<head>
<title>updatesoulhorse</title>
</head>
<body>

	<%
	
		SimpleEntityManager<Player> em = SimpleEntityManagerFactory.getSimpleEntityManager(Player.class);
		
		PlayerManager pm = PlayerManager.getInstance();
		long num = em.count();
		
		long[] ids = em.queryIds(Player.class,"");
		
		for(long id : ids){
			try{
			Player player = pm.getPlayer(id);
		
			if(player != null){
				
				Soul soul = player.getMainSoul();
				if(soul.getHorseArr() != null && soul.getHorseArr().size() > 0){
					out.print("已经是新号,不用刷"+id+"<br/>");
					break;
				}
				ArrayList<Long> arr = player.getHorseArr();
				if(arr != null){
					
				}else{
					arr = new ArrayList<Long>();
					out.print("没有找到坐骑列表"+player.getLogString()+"<br/>");
				}
				
				if(soul !=null){
					out.print("坐骑长度"+arr.size()+player.getLogString()+"<br/>");
					soul.setHorseArr(arr);
					player.setDirty(true,"currSoul");
				}else{
					out.print("没有找到本尊"+player.getLogString()+"<br/>");
				}
			}
			}catch(Exception e){
				out.print(e);
			}
			
		}

		out.print("update over");
	
	%>

</body>

</html>
