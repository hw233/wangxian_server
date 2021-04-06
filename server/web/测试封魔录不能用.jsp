<%@page import="java.util.List"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager"%>
<%@page import="com.fy.engineserver.sprite.AbstractPlayer"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.TaskProps"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="com.fy.engineserver.util.CompoundReturn"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PropsEntity"%>
<%@page import="com.fy.engineserver.sprite.PropsUseRecord"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Props"%>
<%@page import="com.fy.engineserver.vip.VipManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>check</title>
</head>
<body>
<%
ArticleEntityManager aem = ArticleEntityManager.getInstance();
ArticleManager am = ArticleManager.getInstance();
Player player = PlayerManager.getInstance().getPlayer("温柔的dddd霸气");
long ids2 [] ={1201000000001368587L,1201000000001366409L,1201000000001376109L,1201000000001318241L};
	
for(long articleId :ids2){
	if (aem != null && am != null) {
		ArticleEntity ae = player.getArticleEntity(articleId);
		if (ae != null) {
			Article a = am.getArticle(ae.getArticleName());
			if (a instanceof TaskProps) {
				int id = 0;
				TaskProps tp = (TaskProps) a;
				int[] ids = tp.getIds();
				if (ids != null && ids.length > 0) {
					if (ids.length > ae.getColorType()) {
						id = ids[ae.getColorType()];
					} else {
						id = ids[ids.length - 1];
					}
				}
				Task task = TaskManager.getInstance().getTask(id);
				if (task != null) {
					//CompoundReturn result = player.addTaskByServer(task);
					CompoundReturn result = player.takeOneTask(task, true, null);
					if (result.getBooleanValue()) {
						out.print("[可以接取这个任务] [id:"+task.getId()+"] [task:"+task.getName()+"]<br>");
					} else {
						out.print("[您不能接取这个任务] [id:"+task.getId()+"] [task:"+task.getName()+"] [bigName:"+task.getBigCollection()+"] "
						                                                                  +"[周期类型:"+task.getDailyTaskCycle()+"] [周期上限:"+task.getDailyTaskMaxNum()+"] ["+result.getIntValue()+"]<br>");
					}
						//test return 14(1)
						/**
						if (a instanceof Props) {
							PropsUseRecord r = player.getPropsUseRecordMap().get(a.get物品二级分类());
							if (r != null) {
								Props ps = (Props) a;
								if (ps.isUsingTimesLimit()) {
									int vipAdd = VipManager.getInstance().vip每日增加的道具使用次数(player, a.get物品二级分类());
									int totalNum = ps.getMaxUsingTimes() + vipAdd ;//+ exchangeAdd + specialDateAdd + activityAdd;
									out.print("[不能使用] [道具:"+a.getName()+"] [vipAdd:"+vipAdd+"] [道具最大使用次数:"+ps.getMaxUsingTimes()+"] [使用次数记录:"+r.getUseNum()+"]<br>");
									if (r.canUseProps(totalNum) == false) {
										out.print("不能用=================");
									}
								}
							}
						}
						*/
					
				}
			}
		}
	}
}

%>
</body>
</html>
