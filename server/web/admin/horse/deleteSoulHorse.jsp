<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@page import="com.xuanzhi.tools.simplejpa.*"%>
<%@page import="com.fy.engineserver.sprite.*"%>

<%@page import="java.util.*"%><html>
<head>
<title>删除坐骑</title>
</head>
<body>

	<%
	
	String name = request.getParameter("name");
	String id = request.getParameter("id");
	if(name == null || name.equals("")){
		
		%>
		<form action="">
			name:<input type= "text" name="name" />
			id:<input type= "text" name="id" />
			<input type="submit" value="submit"/>
		</form>			
		
		<%
		
	}else{
		
		if(id == null || id.equals("")){
			//删除全部
			
			Player player = PlayerManager.getInstance().getPlayer(name);
			player.setHorseIdList(new ArrayList<Long>());
			out.print("删除完毕");
		}else{
			Player player = PlayerManager.getInstance().getPlayer(name);
			long idd = Long.parseLong(id);
			if(player.getHorseIdList() != null && player.getHorseIdList().contains(idd)){
				
				player.getHorseIdList().remove(idd);
				player.setHorseIdList(player.getHorseIdList());
				out.print("删除指定完毕");
			}else{
				out.print("玩家没有指定坐骑");
			}
			//删除指定
		}
		
		
	}
	
	%>

</body>

</html>
