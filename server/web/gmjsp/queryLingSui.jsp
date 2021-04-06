
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.google.gson.JsonObject"%><%@page import="com.fy.engineserver.soulpith.SoulPithTypes"%>
<%@page import="com.fy.engineserver.soulpith.SoulPithManager"%>
<%@page import="com.fy.engineserver.soulpith.module.SoulPithExtraAttrModule"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.SoulPithArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.soulpith.SoulPithConstant"%>
<%@page import="java.util.Arrays"%><%@page import="com.fy.engineserver.soulpith.module.SoulpithInfo4Client"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="net.sf.json.JSONArray"%><%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%><%@page import="com.fy.engineserver.sprite.Player"%><%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	long playerId = Long.valueOf(request.getParameter("playerId"));
	//元神类型 0 本元 1 地元
	String type = request.getParameter("soulType");
	int soulType =  Integer.valueOf(request.getParameter("soulType"));
	Map<String, Object> result = new HashMap<String, Object>();
	Player player = null;
	try {
		player = GamePlayerManager.getInstance().getPlayer(playerId);
			Soul currSoul = player.getSoul(soulType);
			if(currSoul != null){
				SoulpithInfo4Client datas = new SoulpithInfo4Client(player, soulType);
				Map<String,String> pointShow = new HashMap<String,String>();
				Map<String,String> pointShow2 = new HashMap<String,String>();
				//激活属性预览
				Map<String,String> pointShow3 = new HashMap<String,String>();
				for(int i=0;i<SoulPithConstant.artifice_articleCNNName.length;i++){
					int inlayNum = datas.getSoulNums()[i] - datas.getBasicsoulNums()[i];
					if(inlayNum < 0){
						inlayNum = 0;
					}
					String inalyInfo = "等级获得"+datas.getBasicsoulNums()[i] + "镶嵌获得"+inlayNum;
					pointShow.put(SoulPithConstant.artifice_articleCNNName[i],""+datas.getSoulNums()[i]+"&&&&&&"+inalyInfo);
				}
				
				for(int j=0;j<datas.getInlayInfos().length;j++){
					if(datas.getInlayInfos()[j] > 0){
						ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(datas.getInlayInfos()[j]);
						if (ae != null && ae instanceof SoulPithArticleEntity) {
							SoulPithArticleEntity hunEntity = (SoulPithArticleEntity) ae;
							pointShow2.put(ae.getArticleName(),hunEntity.getInfoShow(player));
						}
					}
				}
				
				SoulPithExtraAttrModule[] datas2 = SoulPithManager.getInst().extraAttrs.toArray(new SoulPithExtraAttrModule[0]);
				for(SoulPithExtraAttrModule m : datas2){
					if(m.canAdd(datas.getSoulNums())){
						String info = "";
						for (int i=0; i<m.getSoulPithTypes().length; i++) {
							SoulPithTypes t = SoulPithTypes.valueOf(m.getSoulPithTypes()[i]);
							info += t.getName() +":"+ m.getNeedSoulNum()[i]+",";
						}
						String desc = "";
						for(String des : m.getAttrDes()){
							desc+=des+",";
						}
						pointShow3.put(m.getName(),desc+"&&&&&&"+info);
					}
				}
				String messStr = "";
				for(String mess : datas.getDescription()){
					messStr+=mess+",";
				}
				
				result.put("灵髓点总览",pointShow);
				result.put("镶嵌中的灵髓",pointShow2);
				result.put("灵髓属性",messStr);
				result.put("灵髓激活属性预览",pointShow3);
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