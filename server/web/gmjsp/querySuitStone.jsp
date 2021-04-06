
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.hunshi.HunshiManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.HunshiEntity"%>
<%@page import="com.fy.engineserver.sprite.horse.HorseManager"%>
<%@page import="com.fy.engineserver.sprite.horse.Horse"%>
<%@page import="java.util.ArrayList"%><%@page import="com.fy.engineserver.sprite.npc.BiaoCheNpc"%>
<%@page import="com.fy.engineserver.core.LivingObject"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="java.net.URLDecoder"%><%@page import="com.fy.boss.gm.gmpagestat.GmEventManager"%><%@page import="net.sf.json.JSONArray"%><%@page import="java.util.HashMap"%><%@page import="java.util.Map"%><%@page import="com.fy.engineserver.mail.service.MailManager"%><%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%><%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%><%@page import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%><%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%><%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%><%@page import="com.fy.engineserver.sprite.Player"%><%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	long playerId = Long.valueOf(request.getParameter("playerId"));
	//元神类型 0 本元 1 地元
	int soulType = Integer.valueOf(request.getParameter("soulType"));
	/**
	 * key：包含(1,2...16),包含魂石加成
	 * value：通过key为(1,2...16)获取的内容包含2个信息，一个是魂石名字，一个是魂石具体描述
	 *        通过key为(魂石加成)获取的内容是所有魂石属性加成信息
	 */
	Map<String, String> result = new HashMap<String, String>();
	Player player = null;
	try {
		player = GamePlayerManager.getInstance().getPlayer(playerId);
			Soul currSoul = player.getSoul(soulType);
			if(currSoul != null){
				ArrayList<Long> horseArr = currSoul.getHorseArr();
				Horse horse = null;
				for(long hid : horseArr){
					if(hid <= 0){
						continue;
					}
					Horse h = (Horse) HorseManager.getInstance().mCache.get(hid);
					if(h == null){
						h = HorseManager.em.find(hid);
					}
					if(h != null && h.isFly() == false){
						horse = h;
					}
				}
				if(horse != null){
					long[] hunshiArray = horse.getHunshi2Array();
					boolean[] hunshi2Open = horse.getHunshi2Open();
					StringBuffer sb = new StringBuffer();
					int[] propertyValueAll = new int[HunshiManager.propertyInfo.length];
					int index = 0;
					for (Long hunshiId : hunshiArray) {
						if(hunshi2Open[index]){
							result.put(""+index,"");
							ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(hunshiId);
							if (ae != null && ae instanceof HunshiEntity) {
								HunshiEntity hunEntity = (HunshiEntity) ae;
								result.put(""+index,hunEntity.getArticleName()+"&&&&&&"+hunEntity.getInfoShow(player));
								int[] mainPropValue = hunEntity.getMainPropValue();
								int[] extraPropValue = hunEntity.getExtraPropValue();
								for (int i = 0; i < mainPropValue.length - 1; i++) {
									propertyValueAll[i] += mainPropValue[i] + extraPropValue[i];
								}
							}
						}else{
							result.put(""+index,"未激活");
						}
						index++;
					}
					for(int i=0;i<propertyValueAll.length;i++){
						int prop = propertyValueAll[i];
						if(prop > 0){
							sb.append(HunshiManager.propertyInfo[i]).append(":").append(prop).append("<br>");
						}
					}
					result.put("套装石属性汇总",sb.toString());
				}else{
					result.put("success", "false");
					result.put("message", "玩家"+player.getName()+"还没拥有坐骑！");
				}
			}else{
				result.put("success", "false");
				result.put("message", "id为:"+playerId+"的玩家元神不存在,元神类型:"+soulType);
			}
	} catch (Exception e) {
		result.put("success", "false");
		result.put("message", "id为:"+playerId+"的玩家不存在！");
	}
	JSONArray json = JSONArray.fromObject(result);
	out.print(json.toString()); 
	out.flush();
	out.close();
%>