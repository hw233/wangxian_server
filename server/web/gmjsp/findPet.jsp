<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.gm.servlet.GMServlet"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.PetEggPropsEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.PetPropsEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.gm.service.GMActionCommon"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="net.sf.json.JSONArray"%>
<%
	String playerId = request.getParameter("playerId");
	String petIds = request.getParameter("petIds");
	Map<String, String> result = new HashMap<String, String>();
	try {
		String gmUserName = URLDecoder.decode(request.getParameter("gmUserName"), "utf-8");
		String reason = URLDecoder.decode(request.getParameter("reason"), "utf-8");
		String title = URLDecoder.decode(request.getParameter("title"), "utf-8");
		String content = URLDecoder.decode(request.getParameter("content"), "utf-8");
		GMServlet.log.info("补发宠物参数,playerId:" + playerId + ",petIds:" + petIds + ",reason:" + reason + ",title:" + title + ",content:" + content);
		long id = Long.valueOf(playerId).longValue();
		Player p = PlayerManager.getInstance().getPlayer(id);
		long petId = Long.parseLong(petIds);
		Pet pet = PetManager.getInstance().getPet(petId);
		if (pet != null) {
			pet.setDelete(false);
			long petEntityId = pet.getPetPropsId();
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(petEntityId);
			if (ae != null) {
				PetEggPropsEntity pep = null;
				if (ae instanceof PetPropsEntity) {
					PetPropsEntity ppe = (PetPropsEntity) ae;
					String eggName = ppe.getEggArticle();

					Article a = ArticleManager.getInstance().getArticle(eggName);
					pep = (PetEggPropsEntity) ArticleEntityManager.getInstance().createEntity(a, false, 37, null, 0, 1, true);

				} else if (ae instanceof PetEggPropsEntity) {
					pep = (PetEggPropsEntity) ae;
				} else {
					result.put("result", "找回宠物失败,不是宠物蛋道具");
				}
				if (pep != null) {
					pep.setPetId(pet.getId());
					if (pet.isIdentity()) switch (pet.getQuality()) {
					case 0:
						pep.setColorType(0);
						break;
					case 1:
						pep.setColorType(1);
						break;
					case 2:
						pep.setColorType(2);
						break;
					case 3:
						pep.setColorType(3);
						break;
					case 4:
						pep.setColorType(4);
						break;
					default:
						break;
					}
					else pep.setColorType(0);

					MailManager.getInstance().sendMail(p.getId(), new ArticleEntity[] { pep }, title, content, 0L, 0L, 0L, "GM工具补删除宠物");
					PetManager.logger.warn("[找回删除宠物成功] [" + p.getLogString() + "] [petId:" + pet.getId() + "]");
					GMActionCommon.sendActionLogToGateway(gmUserName, p, 6, 1L, "", pep.getId() + "/" + pep.getArticleName(), new String[0], reason);
					result.put("result", "success");
				}
			} else result.put("result", "找回宠物失败,没有找到宠物实体" + petEntityId);
		} else {
			result.put("success", "false");
			result.put("message", "对应id宠物不存在" + petId);
		}
	} catch (Exception e) {
		GMServlet.log.error("", e);
		result.put("result", "fail");
	}
	JSONArray json = JSONArray.fromObject(result);
	out.print(json.toString());
	out.flush();
	out.close();
%>