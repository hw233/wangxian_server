<%@page import="com.fy.engineserver.movedata.moveArticle.MoveArticleManager"%>
<%@page import="com.fy.engineserver.sprite.horse2.manager.Horse2Manager"%>
<%@page import="com.fy.engineserver.movedata.DataMoveManager"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>

<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.activity.quiz.QuizManager"%>
<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.core.LivingObject"%>
<%@page import="com.fy.engineserver.sprite.monster.Monster"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardsManager"%>
<%@page import="com.fy.engineserver.util.StringTool"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="com.fy.boss.authorize.model.Passport"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.articles.Article"%><html>
<head>
<title>删物品</title>
</head>
<body>

<%
	String action = request.getParameter("action");
	String address = request.getParameter("address");
	String psd = request.getParameter("psd");
	action = action == null ? "" : action;
	address = address == null ? "" : address;
	psd = psd == null ? "" : psd;
	if ("datamove".equals(action)) {
		if (!"shujuzhuanyi".equals(psd)) {
			out.println("输入密码！");
			return ;
		}
		String test = Horse2Manager.instance.translate.get(9999);
		if (test == null) {
			long now = System.currentTimeMillis();
			MoveArticleManager inst = new MoveArticleManager(address);
			//DataMoveManager inst = new DataMoveManager("/home/game/resin_server_2/webapps/game_server/WEB-INF/spring_config/simpleEMF_ceshib1.xml");
			inst.moveBegin();
			Horse2Manager.instance.translate.put(9999, "movearticle");
		}
	}
	

%>

	<form action="moveArticle.jsp" method="post">
		<input type="hidden" name="action" value="datamove" /> 配置文件路径:
		<input type="text" size="100px" name="address" value="<%=address%>" /> 密码:
		<input type="password" name="psd" value="<%=psd%>" /> 
		<input type="submit" value="开始删物品" />
	</form>

</html>
