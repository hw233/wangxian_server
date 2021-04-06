<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.PetPropsEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String playerName = request.getParameter("playerName");
	String petName = request.getParameter("petName");

	String petColorStr = request.getParameter("petColor");
	String pSizeStr = request.getParameter("pSize");
	String lizi = request.getParameter("lizi");
	boolean objectOpacity = "true".equals(request.getParameter("objectOpacity"));

	int pSize = 1000;
	if (pSizeStr != null) {
		pSize = Integer.valueOf(pSizeStr);
	}
	int petColor = -1;
	if (petColorStr != null) {
		petColor = Integer.valueOf(petColorStr);
	}
	ArticleEntityManager aem = ArticleEntityManager.getInstance();

	if (playerName != null) {
		Player player = GamePlayerManager.getInstance().getPlayer(playerName);
		Knapsack knapsack = player.getPetKnapsack();
		Pet pet = null;
		Cell[] cells = knapsack.getCells();
		for (Cell c : cells) {
			if (!c.isEmpty()) {
				long id = c.getEntityId();
				PetPropsEntity ppe = (PetPropsEntity) aem.getEntity(id);

				PetManager petManager = PetManager.getInstance();
				if (ppe == null) continue;
				Pet p = petManager.getPet(ppe.getPetId());
				if (p == null) {
					continue;
				}
				out.print(p.getName() + ">><BR/>");
				if (p.getName().equals(petName)) {
					pet = p;
					break;
				}
			}
		}
		if (pet != null) {
			out.print("找到了~~" + pet.getName() + "<BR>");
			pet.setObjectColor(petColor);
			pet.setObjectOpacity(objectOpacity);
			pet.setObjectScale((short) pSize);
			if (lizi != null) {
				pet.setParticleName(lizi);
			}
			out.print("[设置完成]找到了~~" + pet.getName() + "lizi:" + lizi + "<BR>");
		} else {
			out.print("没找到~~" + pet.getName() + "<BR>");
		}
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<hr />
	<hr />
	<form action="./test.jsp" method="post">
		角色名字:<input type="text" name="playerName" value="<%=playerName%>"><BR />
		宠物名字:<input type="text" name="petName" value="<%=petName%>"><BR />
		宠物大小<input type="text" name="pSize" value="<%=pSize%>"><BR />
		宠物颜色:<input type="text" name="petColor" value="<%=petColor%>"><BR />
		是否透明:<input type="text" name="objectOpacity" value="<%=objectOpacity%>"><BR /> 
		粒子效果:<input type="text" name="lizi" value="<%=lizi%>"><BR /> 
		<input type="submit" value="提交">
	</form>
</body>
</html>