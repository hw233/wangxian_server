<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PetPropsEntity"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PetEggPropsEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.homestead.cave.Cave"%>
<%@page import="com.fy.engineserver.homestead.faery.service.FaeryManager"%>
<%@page import="com.fy.engineserver.homestead.cave.PetHookInfo"%><html>
<head>
<title>把宠物从挂机房拿走删除</title>
</head>
<body>
	

	<%
			String petIds = request.getParameter("petId");
			String playerIds = request.getParameter("playerId");

			if (petIds != null && !petIds.equals("") && playerIds != null && !playerIds.equals("")) {

				long petId = Long.parseLong(petIds);
				long caveId = Long.parseLong(playerIds);

				Pet pet = PetManager.getInstance().getPet(petId);

				Player p = PlayerManager.getInstance().getPlayer(caveId);

				Cave cave = FaeryManager.getInstance().getCave(p);

				if (cave != null && pet != null) {
					PetHookInfo[] infos = cave.getPethouse().getHookInfos();
					boolean caveHavePet = false;
					for (int i = 0; i < infos.length; i++) {
						PetHookInfo hi = infos[i];
						if (hi != null) {
							out.print("petId:" + hi.getPetId() + "<br/>");
							if (hi.getPetId() == petId) {
								caveHavePet = true;
								infos[i] = null;
								cave.notifyFieldChange("pethouse");

								pet.setHookInfo(null);
								PetManager.em.notifyFieldChange(pet, "hookInfo");
								PetManager.logger.error("[后台删除挂机宠物] [" + pet.getLogString() + "] [洞府id:" + cave.getId() + "]");
								out.print("后台删除挂机宠物<br/>");
								out.print("完成");
								break;
							}
						}
					}
					if (!caveHavePet) {
						pet.setHookInfo(null);
						PetManager.em.notifyFieldChange(pet, "hookInfo");
						PetManager.logger.error("[后台删除挂机宠物洞府没有] [" + pet.getLogString() + "] [洞府id:" + cave.getId() + "]");
						out.print("后台删除挂机宠物洞府没有<br/>");
						out.print("完成");
					}
				} if (cave != null && pet == null) {
					PetHookInfo[] infos = cave.getPethouse().getHookInfos();
					boolean caveHavePet = false;
					for (int i = 0; i < infos.length; i++) {
						PetHookInfo hi = infos[i];
						if (hi != null) {
							out.print("petId:" + hi.getPetId() + "<br/>");
							if (hi.getPetId() == petId) {
								caveHavePet = true;
								infos[i] = null;
								cave.notifyFieldChange("pethouse");

								PetManager.logger.error("[后台删除挂机宠物] [pet:" + pet+"] [洞府id:" + cave.getId() + "]");
								out.print("后台删除挂机宠物<br/>");
								out.print("完成");
								break;
							}
						}
					}
				} else if (cave == null && pet != null) {
					pet.setHookInfo(null);
					PetManager.em.notifyFieldChange(pet, "hookInfo");
					PetManager.logger.error("[后台删除挂机宠物] [" + pet.getLogString() + "] [[洞府不存在]]");
					out.print("后台删除挂机宠物,洞府不存在<br/>");
				} else {
					out.print("cave:" + cave + " " + "pet:" + pet);
				}

				return;
			}
		%>
	<form action="">
		宠物id:<input type="text" name="petId" />
		挂机房主人id:<input type="text" name="playerId" />
		<input type="submit" value="submit" />
	</form>


</body>
</html>
