<%@ page contentType="text/html;charset=utf-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.activity.fateActivity.*"%>
<%@page import="java.util.*"%>

<%@page import="com.fy.engineserver.sprite.ActivityRecordOnPlayer"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardsManager"%>
<%@page import="com.fy.engineserver.newBillboard.*"%>
<%@page import="com.xuanzhi.tools.simplejpa.*"%>



<%@page import="com.fy.engineserver.sprite.pet.*"%>
<%@page import="com.fy.engineserver.util.StringTool"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.sprite.concrete.*"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>更改player元神职业</title>
</head>
<body>

	<%
		
		SimpleEntityManager<Player> em = ((GamePlayerManager)PlayerManager.getInstance()).em;
		long[] ids = em.queryIds(Player.class,"");
		
		out.print(ids.length);
		for(long id: ids){
			try{
				PlayerManager.getInstance().getPlayer(id);
			}catch(Exception e){
				out.print(e);
			}
		}
		out.print("完成");
	%>


</body>

</html>
