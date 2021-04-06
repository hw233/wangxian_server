<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page
	import="java.util.*,com.xuanzhi.tools.text.*,com.fy.engineserver.datasource.article.manager.*,com.fy.engineserver.datasource.article.data.props.*,com.fy.engineserver.datasource.article.data.entity.*,com.fy.engineserver.util.*,com.fy.engineserver.sprite.*,com.fy.engineserver.sprite.pet.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
</script>
</head>
<body>
<br>
<br>
<h2>创建宠物</h2>
<br>
<form action="" name=f1>方式一<br>
	宠物蛋道具名:<input type=text size=20 name="eggname" /> <br>
	<br>
	方式二<br>
	父宠道具id:<input type=text size=20 name="entityAId" /><br>
	母宠道具id:<input type=text size=20 name="entityBId" /><br>
	<br>
	拥有人id：<input type=text size=20 name="playerId" /><br>
	<input type=submit name=sub1 value=提交>
</form>
<%
	String eggname = request.getParameter("eggname");
	String entityAId = request.getParameter("entityAId");
	String entityBId = request.getParameter("entityBId");
	String message = null;
	Pet pet = null;
	if (eggname != null) {
		try {
			PetManager pm = PetManager.getInstance();
			PlayerManager ppm = PlayerManager.getInstance();
			ArticleManager am = ArticleManager.getInstance();
			ArticleEntityManager aem = ArticleEntityManager
					.getInstance();
			long pid = Long.parseLong(request.getParameter("playerId"));
			Player p = ppm.getPlayer(pid);

			ArticleEntity ae =  p.getArticleEntity(eggname.trim());
			if(ae == null) {
				out.print("没有这个物品");
				return;
			}
			if(ae instanceof PetEggPropsEntity){
				ae = (PetEggPropsEntity)ae;
				Article a =ArticleManager.getInstance().getArticle(ae.getArticleName());
				if(a instanceof PetEggProps){
					((PetEggProps)a).use(p.getCurrentGame(),p,ae);
				}
			}else{
				out.print("不是宠物物品");
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = e.getMessage();
		}
	} else if (entityAId != null && entityBId != null) {
		try {
			PetManager pm = PetManager.getInstance();
			PlayerManager ppm = PlayerManager.getInstance();
			ArticleManager am = ArticleManager.getInstance();
			ArticleEntityManager aem = ArticleEntityManager
					.getInstance();
			PetMatingManager pmm = PetMatingManager.getInstance();
			long pid = Long.parseLong(request.getParameter("pid"));
			Player p = ppm.getPlayer(pid);
			PetPropsEntity ppeA = (PetPropsEntity) aem.getEntity(Long
					.parseLong(entityAId));
			PetPropsEntity ppeB = (PetPropsEntity) aem.getEntity(Long
					.parseLong(entityBId));
			pet = pmm.createAndFinishMating(p, ppeA, ppeB);
		} catch (Exception e) {
			e.printStackTrace();
			message = e.getMessage();
		}
	}
%>
<br>
<br>
<%
	if (message != null)
		out.println(message);
	else if (pet != null) {
%>
<h2>宠物创建成功!id为:<%=pet.getId()%></h2>
<%
	} else if (eggname != null || entityAId != null)
		out.println("宠物创建失败！");
%>
</body>
</html>
