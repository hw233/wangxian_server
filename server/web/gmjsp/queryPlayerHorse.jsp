<%@page import="com.fy.engineserver.sprite.pet2.Pet2Manager"%>
<%@page import="com.fy.engineserver.sprite.pet2.PetsOfPlayer"%>
<%@page import="com.fy.engineserver.sprite.horse2.model.BloodStarModel"%>
<%@page import="com.fy.engineserver.sprite.horse2.model.HorseSkillModel"%>
<%@page import="com.fy.engineserver.sprite.horse2.manager.Horse2Manager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%>
<%@page import="com.fy.engineserver.sprite.horse.HorseOtherData"%>
<%@page import="com.fy.engineserver.sprite.horse.Horse"%>
<%@page import="com.fy.engineserver.sprite.horse.HorseManager"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="java.net.URLDecoder"%><%@page import="com.fy.boss.gm.gmpagestat.GmEventManager"%><%@page import="net.sf.json.JSONArray"%><%@page import="java.util.HashMap"%><%@page import="java.util.Map"%><%@page import="com.fy.engineserver.mail.service.MailManager"%><%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%><%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%><%@page import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%><%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%><%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%><%@page import="com.fy.engineserver.sprite.Player"%><%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	long playerId = Long.valueOf(request.getParameter("playerId"));
	Map<String, Object> result = new HashMap<String, Object>();
	try {
		Player player = null;
		try {
			player = GamePlayerManager.getInstance().getPlayer(playerId);
		} catch (Exception e) {
			
		}
		for (Soul soul : player.getSouls()) {
			if (soul == null) {
				continue;
			}
			for (long id : soul.getHorseArr()) {
				Horse horse = HorseManager.getInstance().getHorseByHorseId(player, id, soul);
				if (horse != null && !horse.isFly()) {
					Map<String, String> tempMap = new HashMap<String, String>();
					tempMap.put("horseName", horse.getHorseName());
					String[] horseEqu = new String[horse.getEquipmentColumn().getEquipmentIds().length];
					long[] horseEquIds = new long[horse.getEquipmentColumn().getEquipmentIds().length];
					for (int i=0; i<horse.getEquipmentColumn().getEquipmentIds().length; i++) {
						long eId = horse.getEquipmentColumn().getEquipmentIds()[i];
						ArticleEntity ae = null;
						if (eId >0) {
							ae = DefaultArticleEntityManager.getInstance().getEntity(eId);
						}
						String aeName = "无";
						String color = "";
						long aeId = -1;
						if (ae != null) {
							aeName = ae.getArticleName();
							color = "(" + ArticleManager.color_equipment_Strings[ae.getColorType()] + ")";
							aeId = ae.getId();
						}
						horseEquIds[i] = aeId;
						horseEqu[i] = aeName + color;
					}
					tempMap.put("equipmentNames", Arrays.toString(horseEqu));
					tempMap.put("equipmentIds", Arrays.toString(horseEquIds));
					HorseOtherData otherData = horse.getOtherData();
					List<String> skillDes = new ArrayList<String>();
					if (otherData != null) {
						for (int i=0; i<otherData.getSkillList().length; i++) {
							if (otherData.getSkillList()[i] > 0) {
								HorseSkillModel hsm = Horse2Manager.instance.horseSkillMap.get(otherData.getSkillList()[i]);
								skillDes.add(hsm.getName() + "("+(otherData.getSkillLevel()[i]+1)+"级)");
							}
						}
						//BloodStarModel bm = Horse2Manager.instance.bloodStarMap.get(otherData.getBloodStar());
						tempMap.put("skillNames", skillDes.toString());
						tempMap.put("pinzhi", otherData.getColorType() + "");
						BloodStarModel bm = Horse2Manager.instance.bloodStarMap.get(otherData.getBloodStar());
						PetsOfPlayer bean0 = Pet2Manager.getInst().findPetsOfPlayer(player);
						tempMap.put("costXueMai", bm.getNeedBloodNum() + "");
						tempMap.put("nextXueMai", bean0.getXueMai() + "");
						tempMap.put("jieji", Horse2Manager.instance.getJieJiMess(horse.getRank()));
						tempMap.put("xuemaixingji", otherData.getBloodStar() +"");
					}
					String ss = soul.getSoulType() == Soul.SOUL_TYPE_BASE ? "benzun" : "yuanshen";
					result.put(ss, tempMap);
				}
			}
			
		}
	} catch (Exception e) {
		result.put("result","页面出现异常" + e.getMessage());		
	}
	JSONArray json = JSONArray.fromObject(result);
	out.print(json.toString()); 
	out.flush();
	out.close();
%>