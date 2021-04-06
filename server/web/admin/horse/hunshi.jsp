<%@page import="com.fy.engineserver.hunshi.HunshiSuit"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.hunshi.HunshiManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.horse.Horse"%>
<%@page import="com.fy.engineserver.sprite.horse.HorseManager"%>
<%@page import="java.util.Arrays"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.HunshiEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看坐骑魂石</title>
<style type="text/css">
body {
	font-size: 12px;
}

table,th,td {
	border: 1px solid blue;
	border-spacing: 0px;
	border-collapse: collapse;
}
</style>
</head>
<body>
<%
	String idStr = request.getParameter("playerId");
	String hunshiId = request.getParameter("hunshiId");
	if (idStr != null && !"".equals(idStr)) {
		Player p = null;
		try{
			p = PlayerManager.getInstance().getPlayer(idStr);
		}catch (Exception e){
			p = PlayerManager.getInstance().getPlayer(Long.valueOf(idStr));
		}
		if (p != null) {
			if (p.getHorseIdList() != null && p.getHorseIdList().size() > 0) {
				Horse h = HorseManager.getInstance().getHorseById(p.getHorseIdList().get(0), p);
				out.print("horseId:" + h.getHorseId() +",horseName:" + h.getHorseShowName() + ",["+p.getLogString()+"<br>");
				if (h != null) {
					boolean show = false;
					long[] hunshiArray = h.getHunshiArray();
					if (hunshiArray != null && hunshiArray.length > 0) {
						int index = 0;
						out.print("魂石面板:<br>");
						for (Long hId : hunshiArray) {
							if (hId > 0) {
								show = true;
								ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(hId);
								if (ae != null) {
									out.print("第" + (++index) + "孔:" + ae.getArticleName() + "[id:" + hId + "] [color:" + ae.getColorType() + "]<br>");
								}
							}
						}
					}
					long[] hunshi2Array = h.getHunshi2Array();
					if (hunshi2Array != null && hunshi2Array.length > 0) {
						int index = 0;
						out.print("套装魂石面板:<br>");
						for (Long hId : hunshi2Array) {
							if (hId > 0) {
								show = true;
								ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(hId);
								if (ae != null) {
									out.print("第" + (++index) + "孔:" + ae.getArticleName() + "[id:" + hId + "] [color:" + ae.getColorType() + "]<br>");
								}
							}
						}
					}
					out.print("<hr>");
					if (!show) {
						out.print("玩家未镶嵌魂石和套装魂石<br><br>");
					}
					int[] propertyValueAll = new int[HunshiManager.propertyInfo.length];
					int[] propertyValueAll2 = new int[HunshiManager.propertyInfo.length];
					if (h != null) {
						if (hunshiArray != null && hunshiArray.length > 0) {
							boolean hasHunshi = false;
							for (Long hsId : hunshiArray) {
								if (hsId > 0) {
									ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(hsId);
									if (ae != null && ae instanceof HunshiEntity) {
										hasHunshi = true;
										HunshiEntity he = (HunshiEntity) ae;
										int[] mainPropValue = he.getMainPropValue();
										int[] extraPropValue = he.getExtraPropValue();
										for (int i = 0; i < mainPropValue.length - 1; i++) {
											propertyValueAll[i] += mainPropValue[i] + extraPropValue[i];
										}
									}
								}
							}
							if (!hasHunshi) {
								out.print("未镶嵌魂石<br>");
								return;
							}
							
						}
						
						HunshiManager hm = HunshiManager.getInstance();
						boolean hasHunshi = false;
						if (hunshi2Array != null && hunshi2Array.length > 0) {
							List<HunshiSuit> suitList = hm.getHunshiSuitList(hunshi2Array);
							HunshiManager.getInstance().logger.debug(p.getLogString() + "[魂石套装属性预览] [获取套装]");
							String[] kinds = new String[suitList.size()];
							String[] des = new String[suitList.size()];
							if (suitList != null && suitList.size() > 0) {
								for (int i = 0; i < suitList.size(); i++) {
									HunshiSuit suit = suitList.get(i);
									kinds[i] = suit.getSuitName();
									des[i] = suit.getInfoShow();
									for (int j = 0; j < suit.getPropId().length; j++) {
										propertyValueAll2[suit.getPropId()[j]] += suit.getPropValue()[j];
									}
								}
							}
							for (Long hsId2 : hunshi2Array) {
								if (hsId2 > 0) {
									hasHunshi = true;
									ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(hsId2);
									if (ae != null && ae instanceof HunshiEntity) {
										HunshiEntity he = (HunshiEntity) ae;
										int[] mainPropValue = he.getMainPropValue();
										int[] extraPropValue = he.getExtraPropValue();
										for (int i = 0; i < mainPropValue.length - 1; i++) {
											propertyValueAll2[i] += mainPropValue[i] + extraPropValue[i];
										}
									}
								}
							}
							if (!hasHunshi) {
								out.print("未镶嵌套装魂石<br>");
							}
						}
						
						out.print("<table>");
						out.print("<tr>");
						out.print("<td>属性类型</td>");
						for(int i=0;i<HunshiManager.propertyInfo.length;i++){
							out.print("<td>"+HunshiManager.propertyInfo[i]+"</td>");
						}
						out.print("</tr>");
						out.print("<tr>");
						out.print("<td>魂石加成</td>");
						for(int i=0;i<propertyValueAll.length;i++){
							out.print("<td>"+propertyValueAll[i]+"</td>");
						}
						out.print("</tr>");
						out.print("<tr>");
						out.print("<td>套装魂石加成</td>");
						for(int i=0;i<propertyValueAll2.length;i++){
							out.print("<td>"+propertyValueAll2[i]+"</td>");
						}
						out.print("</tr>");
						out.print("</table>");
					}
				}
				if (hunshiId != null && !"".equals(hunshiId)) {
					ArticleEntity he = ArticleEntityManager.getInstance().getEntity(Long.valueOf(hunshiId));
					if (he instanceof HunshiEntity) {
						out.print(((HunshiEntity) he).getInfoShow(p) + "<br>");
					}
				}
			}
		}

	}
%>
<form action="">playerId或角色名:<input type="text" name="playerId"
	value="<%=idStr == null ? "" : idStr%>" /> <br>
魂石Id:<input type="text" name="hunshiId"
	value="<%=hunshiId == null ? "" : hunshiId%>" />(查看魂石属性的时候填写) <br>
<input type="submit" /></form>
</body>
</html>