<%@page
	import="com.fy.engineserver.septbuilding.entity.SeptBuildingEntity"%>
<%@page import="com.fy.engineserver.jiazu.JiazuTitle"%>
<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@page
	import="com.fy.engineserver.septstation.service.SeptStationManager"%>
<%@page import="com.fy.engineserver.septstation.SeptStation"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>删除复制的装备</title>
</head>
<body>

<%
	String name = request.getParameter("name");
	String eqIds = request.getParameter("eqId");
	
	PlayerManager pm = PlayerManager.getInstance();
	
	if(name != null && eqIds != null){
	
		Player p =  pm.getPlayer(name);
		
		long eqId = Long.parseLong(eqIds);
		
		int num = p.getArticleEntityNum(eqId);
		if(num > 1){
			for(int j = 0;j<num -1;j++){
				p.removeArticleEntityFromKnapsackByArticleId(eqId,"删除复制装备",false);
				out.print(eqId+"<br/>");
			}
		}
		
		Knapsack k = p.getKnapsacks_cangku();
		Cell[] cells = k.getCells();
		
		int count = cells.length;
		for(int i= 0;i<count;i++){
			ArticleEntity ae =  k.removeByArticleId(eqId,"删除复制装备",false);
			if(ae != null){
				out.print(ae.getArticleName()+"<br/>");
			}
			
		}
		
		return;
	}


%>

	<form action="">
			玩家名:<input type="text" name="name"/><br/>
			装备id:<input type="text" name="eqId"/><br/>
		<input type="submit" value="submit"/>
	</form>

</body>
</html>