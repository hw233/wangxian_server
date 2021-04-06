<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@page import="java.net.URLDecoder"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%>
<%@page import="com.fy.engineserver.datasource.article.manager.AritcleInfoHelper"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PropsEntity"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.gm.servlet.GMServlet"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PetPropsEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.MagicWeaponEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.NewMagicWeaponEntity"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%
	String playerId = request.getParameter("playerid");
	Map<String, Object> result = new HashMap<String, Object>();
	String errorMessage = null;
	PlayerManager pm = PlayerManager.getInstance();
	Player player = null;
	long pid = Long.valueOf(playerId).longValue();
	ArticleEntityManager aem = ArticleEntityManager.getInstance();
	String cellIndex = request.getParameter("articleid");
	try {
		String playerIdStr = request.getParameter("playerid");
		GMServlet.log.info("获取装备详细信息，articleid:" + cellIndex + ",playerIdStr:" + playerIdStr);

		System.out.println("[" + playerId + "]");
		if ((cellIndex == null) || (cellIndex.trim().length() == 0)) errorMessage = "要使用的物品不能为空";
	} catch (Exception e) {
		errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
		GMServlet.log.error("", e);
	}
	if (errorMessage == null) {
		try {
			player = pm.getPlayer(pid);
		} catch (Exception e) {
			GMServlet.log.error("", e);
		}
		if (player == null) {
			errorMessage = "ID为" + playerId + "的玩家不存在！";
		}
	}
	result.put("result", errorMessage);
	if (errorMessage == null) {
		ArticleEntity ae = aem.getEntity(new Long(cellIndex).longValue());
		if ((ae instanceof EquipmentEntity)) {
			String s = AritcleInfoHelper.generate(player, (EquipmentEntity) ae);
			s = s.replaceAll("\\[/color\\]", "</font>");
			s = s.replaceAll("\\[color=(.*)\\]", "<font color='$1'>");

			s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]", "<img src='/icons/$1'>");
			result.put("result", "<pre>" + ae.getId() + "\n" + s + "</pre><br>");
		} else if ((ae instanceof PropsEntity)) {
			String s = AritcleInfoHelper.generate(player, (PropsEntity) ae);
			if ((PropsEntity) ae instanceof PetPropsEntity) {
				s += ((PetPropsEntity) (PropsEntity) ae).getInfoShowExtra(player);
			}
			s = s.replaceAll("\\[/color\\]", "</font>");
			s = s.replaceAll("\\[color=(.*)\\]", "<font color='$1'>");
			s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]", "<img src='/icons/$1'>");
			result.put("result", "<pre>" + ae.getId() + "\n" + s + "</pre><br>");
		} else if ((ae instanceof ArticleEntity)) {
			String s = AritcleInfoHelper.generate(player, ae);
			if ((ArticleEntity) ae instanceof NewMagicWeaponEntity) {
				s += "\n<f color='0x00FF00' size='20'>" + Translate.法宝加持等级 + ((NewMagicWeaponEntity) (ArticleEntity) ae).getMstar() + "</f>";
			}
			s = s.replaceAll("\\[/color\\]", "</font>");
			s = s.replaceAll("\\[color=(.*)\\]", "<font color='$1'>");
			s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]", "<img src='/icons/$1'>");
			result.put("result", "<pre>" + ae.getId() + "\n" + s + "</pre><br>");
		}
	}
	JSONArray json = JSONArray.fromObject(result);
	out.print(json.toString());
	out.flush();
	out.close();
%>