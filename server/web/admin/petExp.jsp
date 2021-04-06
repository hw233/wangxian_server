<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.newtask.service.TaskEventTransactCenter"%>
<%@page import="com.fy.engineserver.newtask.service.AbsTaskEventTransact"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.SingleForPetProps"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%

	out.print("<h1>" + GameConstants.getInstance().getServerName() + "</h1>");
	String articleName = "宠物经验丹(绿)";
	Article article = ArticleManager.getInstance().getArticle(articleName);
	if (article != null) {
		SingleForPetProps petProps = (SingleForPetProps) article;
		petProps.getValues()[1] = 150000000L;
		out.print("设置完毕" + Arrays.toString(petProps.getValues()));
	} else {
		out.print(articleName + "不存在");
	}

	AbsTaskEventTransact absTaskEventTransact = new AbsTaskEventTransact(){
		public String[] getWannaDealWithTaskNames(com.fy.engineserver.newtask.service.Taskoperation action) {
			return null;
		}
		
		public void handleAccept(com.fy.engineserver.sprite.Player player, com.fy.engineserver.newtask.Task task, com.fy.engineserver.core.Game game){
			
		}
		public void handleDeliver(com.fy.engineserver.sprite.Player player, com.fy.engineserver.newtask.Task task, com.fy.engineserver.core.Game game) {
			
		}
		public void handleDone(com.fy.engineserver.sprite.Player player, com.fy.engineserver.newtask.Task task, com.fy.engineserver.core.Game game) {
			
		}
		
		public void handleDrop(com.fy.engineserver.sprite.Player player, com.fy.engineserver.newtask.Task task, com.fy.engineserver.core.Game game) {
			
		}
		public com.fy.engineserver.util.CompoundReturn dealWithTask(com.fy.engineserver.newtask.service.Taskoperation action, com.fy.engineserver.sprite.Player player, com.fy.engineserver.newtask.Task task, com.fy.engineserver.core.Game game){
			return null;
			
		}
	};
	Field eventTransactsField = TaskEventTransactCenter.class.getDeclaredField("eventTransacts");
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>