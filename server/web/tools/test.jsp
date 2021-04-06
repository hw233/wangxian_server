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
<title>test</title>
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
		String test = Horse2Manager.instance.translate.get(999);
		if (test == null) {
			long now = System.currentTimeMillis();
			DataMoveManager inst = new DataMoveManager(address);
			//DataMoveManager inst = new DataMoveManager("/home/game/resin_server_2/webapps/game_server/WEB-INF/spring_config/simpleEMF_ceshib1.xml");
			inst.dataMoveBegin();
			out.println("=========================执行结束==========================<br>");
			out.println("总耗时:" + (System.currentTimeMillis() - now) + "毫秒]<br>");
			Iterator<Class<?>> ito = DataMoveManager.instance.dataMoveMaps.keySet().iterator();
			out.println("总数:" + DataMoveManager.instance.dataMoveMaps.size() + "<br>");
			while (ito.hasNext()) {
				Class<?> clazz = ito.next();
				Long[] value = DataMoveManager.instance.dataMoveMaps.get(clazz);
				out.println("[class:" + clazz +"] [总数据::" + value[0] + "] [导入新库数据:" + value[1] + "] [耗时:" + value[2] + "]" + "<br>");
			}  
			Horse2Manager.instance.translate.put(999, "hefutest");
		}
	}
	

%>

	<form action="test.jsp" method="post">
		<input type="hidden" name="action" value="datamove" /> 配置文件路径:
		<input type="text" size="100px" name="address" value="<%=address%>" /> 密码:
		<input type="password" name="psd" value="<%=psd%>" /> 
		<input type="submit" value="开始导数据" />
	</form>

</html>
