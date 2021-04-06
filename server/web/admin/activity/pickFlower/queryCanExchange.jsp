<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="com.fy.engineserver.core.Game"%>

<%@page import="com.fy.engineserver.activity.pickFlower.PickFlowerManager"%>
<%@page import="com.fy.engineserver.activity.pickFlower.PickFlowerEntity"%>
<%@page import="com.fy.engineserver.core.LivingObject"%>
<%@page import="com.fy.engineserver.activity.pickFlower.FlowerNpc"%>
<%@page import="com.fy.engineserver.activity.pickFlower.Flower"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="java.util.List"%><html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>查看可以兑换的物品</title>
</head>

<body>
	
	<%
		String name = request.getParameter("name");
		if(name != null && !name.equals("")){
			Player player = PlayerManager.getInstance().getPlayer(name);
			
			List<Long> list = PickFlowerManager.getInstance().queryFlowerCanExchange(player);
			if(list != null && list.size()>0){
				for(long id: list){
					out.print("id:"+id+"<a href=./exchange.jsp?name="+name+"&id="+id+">兑换</a><br/>");
				}
			}else{
				out.print("没有可兑换物品");
			}
			
			return;
		}
	%>

	<form action="">
		name:<input type="text" name="name"/>
		<input type="submit" value="submit">
	</form>

</body>
</html>
