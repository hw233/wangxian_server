<%@ page contentType="text/html;charset=utf-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">


<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.horse.Horse"%>
<%@page import="com.fy.engineserver.sprite.horse.HorseManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>更新飞行坐骑的限时到期</title>
</head>
<body>

	<%
	
		String name = request.getParameter("name");
		String horseIds = request.getParameter("horseId");
		if(name != null && !name.equals("") && horseIds != null && !horseIds.equals("")){
			Player player = PlayerManager.getInstance().getPlayer(name);
			
			Horse h = HorseManager.getInstance().getHorseById(Long.parseLong(horseIds),player);
			if(h.getDueTime() > 0){
				h.setDueTime(h.getDueTime() - 60*60*1000);
				out.print("修改成功");
			}
			return;
		}
	%>

	
	<form action="">
		玩家name:<input type="text" name="name"/><br/>
		坐骑id:<input type="text" name="horseId"/><br/>
		<input type="submit" value="submit"/>
	</form>


</body>

</html>
