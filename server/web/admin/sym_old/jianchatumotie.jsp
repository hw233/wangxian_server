<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.LevelRandomPackage"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="com.fy.engineserver.sprite.npc.NPCManager"%>
<%@page import="com.fy.engineserver.sprite.npc.MemoryNPCManager"%>
<%@page import="com.fy.engineserver.sprite.npc.NPC"%>
<%@page import="com.fy.engineserver.sprite.TeamSubSystem"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>修改神铸bug</title>
		<link rel="stylesheet" href="gm/style.css" />
		<script type="text/javascript">
	
</script>
	</head>
	<body>
		<br> 
		<%
			String action = request.getParameter("action");
			Field f = LevelRandomPackage.class.getDeclaredField("allmap");
			f.setAccessible(true);
			out.println(f.getType());
			out.println("<br>");
			Map<Integer, List<Article>> m  = (Map<Integer, List<Article>>)f.get(LevelRandomPackage.class.newInstance());
			if (m == null || m.size() == 0) {
				LevelRandomPackage.initAllMap();
			}
			if (action != null && action.equals("cl")) {
				for (Integer key : m.keySet()) {
					List<Article> l = m.get(key);
					l.clear();
				}
				m.clear();
				LevelRandomPackage.initAllMap();
			}
			
			List<Article> jiu = m.get(1);
			out.println("酒size:" + jiu.size());
			out.println("<br>");
			for (Article a : jiu) {
				if (a != null) {
					out.println(a.getName());
					out.println("<br>");
				}else {
					out.println("有个空的奇怪");
					out.println("<br>");
				}
			}

			out.println("<br>");
			out.println("<br>");
			
			List<Article> as = m.get(2);
			out.println("贴size:" + as.size());
			out.println("<br>");
			for (Article a : as) {
				if (a != null) {
					out.println(a.getName());
					out.println("<br>");
				}else {
					out.println("有个空的奇怪");
					out.println("<br>");
				}
			}
		%>
		
		<form>
			<input type="hidden" name="action" value="cl">
			<input type="submit" value="清除"">
		</form>

	</body>
</html>
