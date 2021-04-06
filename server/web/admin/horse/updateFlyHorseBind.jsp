<%@ page contentType="text/html;charset=utf-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.activity.fateActivity.*"%>
<%@page import="java.util.*"%>

<%@page import="com.fy.engineserver.sprite.ActivityRecordOnPlayer"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardsManager"%>
<%@page import="com.fy.engineserver.newBillboard.*"%>
<%@page import="com.xuanzhi.tools.simplejpa.*"%>
<%@page import="com.fy.engineserver.sprite.*"%>
<%@page import="com.fy.engineserver.newBillboard.*"%>

<%@page import="com.fy.engineserver.sprite.pet.*"%>
<%@page import="com.fy.engineserver.util.StringTool"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.zongzu.manager.ZongPaiManager"%>
<%@page import="com.fy.engineserver.zongzu.data.ZongPai"%>
<%@page import="com.fy.engineserver.playerTitles.*"%>
<%@page import="com.fy.engineserver.sprite.concrete.*"%>
<%@page import="com.fy.engineserver.playerTitles.PlayerTitle"%>
<%@page import="com.fy.engineserver.sprite.horse.HorseManager"%>
<%@page import="com.fy.engineserver.sprite.horse.Horse"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.engineserver.sprite.horse.dateUtil.DateFormat"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.HorseProps"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>修改飞行坐骑的绑定</title>
</head>
<body>

	<%
	
		String name = request.getParameter("name");
		if(name != null && !name.equals("")){
			
			Player player = PlayerManager.getInstance().getPlayer(name);
			Knapsack k = player.getKnapsack_common();
			boolean update = false;
			Cell[] cells = k.getCells();
			for(Cell c : cells){
				if(c != null){
					long id = c.getEntityId();
					if(id > 0){
						ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(id);
						if(ae != null){
							String articleName = ae.getArticleName();
							Article a = ArticleManager.getInstance().getArticle(articleName);
							if(a != null && a instanceof HorseProps){
								HorseProps hp = (HorseProps)a;
								if(!hp.isHaveValidDays()){
									ae.setBinded(false);
									Knapsack.logger.error("[后台设置飞行坐骑不绑定] ["+player.getLogString()+"] ["+ae.getId()+"] ["+ae.getArticleName()+"]");
									update = true;
									out.print("设置成功");
								}
							}
						}
					}
				}
			}
			if(!update){
				out.print("本次修改没有符合的飞行坐骑");
			}
			return;
		}
	%>

	
	<form action="">
		玩家name:<input type="text" name="name"/><br/>
		<input type="submit" value="submit"/>
	</form>


</body>

</html>
