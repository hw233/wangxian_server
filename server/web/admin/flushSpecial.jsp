<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>

<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.activity.quiz.QuizManager"%>
<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.core.LivingObject"%>
<%@page import="com.fy.engineserver.sprite.monster.Monster"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardsManager"%>
<%@page import="com.fy.engineserver.util.StringTool"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="com.fy.engineserver.billboard.special.SpecialEquipmentManager"%>
<%@page import="com.fy.engineserver.billboard.special.SpecialEquipmentBillBoard"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.Special_2EquipmentEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%>
<%@page import="com.fy.engineserver.billboard.special.SpecialEquipmentMappedStone"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%><html>
<head>
<title>刷新现有混沌万灵榜装备</title>
</head>
<body>

	<%
	
		SpecialEquipmentBillBoard sebb = SpecialEquipmentManager.getInstance().getSpecialEquipmentBillBoard();
		LinkedHashMap<String, List<Long>> map = sebb.getSpecial2Map();
		
		for(Map.Entry<String, List<Long>> en: map.entrySet()){
			if(en.getValue().size() > 0){
				out.print("刷新"+en.getKey()+"<br/>");
				for(long id : en.getValue()){
					
					ArticleManager am = ArticleManager.getInstance();
					ArticleEntityManager aem = ArticleEntityManager.getInstance();
					ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(id);
					
					Article a = am.getArticle(ae.getArticleName());
					
					if(ae != null && ae instanceof Special_2EquipmentEntity && a != null && a instanceof Equipment){
						out.print("刷新"+id+"<br/>");
						
						if(ae instanceof Special_2EquipmentEntity){
							
							Equipment equipment = (Equipment)a;
							Special_2EquipmentEntity ee = (Special_2EquipmentEntity)ae;
							
							ee.setEndowments(EquipmentEntity.满资质);
							ee.setInscriptionFlag(true);
							
							SpecialEquipmentMappedStone mapped = ArticleManager.getInstance().mappedStoneMap.get(equipment.getName());
							long[] inlays = ee.getInlayArticleIds();
							int len = 0;
							if(inlays !=  null){
								len = inlays.length;
							}
							ee.setInlayArticleIds(new long[len]);
							out.println(Arrays.toString(ee.getInlayArticleIds())+"fdsfsdfd");
							
							int[] colors = ee.getInlayArticleColors();
							/*
							if(inlays == null || colors == null ||inlays.length == 0 || colors.length == 0){
							}else{
								if(mapped ==  null){
									SpecialEquipmentManager.logger.warn("[刷新混沌万灵榜装备错误] [没有对应宝石对象] ["+equipment.getName()+"] ");
									out.print("[刷新混沌万灵榜装备错误] [没有对应宝石对象] ["+equipment.getName()+"] ");
									return;
								}
								String[] mappedStone = mapped.getStoneNames();
								if(mappedStone.length == colors.length && inlays.length == colors.length){
									int max = inlays.length;
									
									for(int index = 0; index < max; index++){
										String stoneName = mappedStone[index];
										if(am.getArticle(stoneName) != null){
											ArticleEntity stoneAe = aem.createEntity(am.getArticle(stoneName), true, ArticleEntityManager.CREATE_REASON_SPRITE_FLOP, null, am.getArticle(stoneName).getColorType(), 1, true);
											inlays[index] = stoneAe.getId(); 
										}else{
											SpecialEquipmentManager.logger.warn("[刷新混沌万灵榜错误] [没有宝石] ["+stoneName+"]");
											out.print("[刷新混沌万灵榜错误] [没有宝石] ["+stoneName+"]");
											return ;
										}
									}
									ee.setInlayArticleIds(inlays);
									SpecialEquipmentManager.logger.warn("[刷新混沌万灵榜镶嵌宝石成功] ["+equipment.getName()+"]");
									
								}else{
									SpecialEquipmentManager.logger.warn("[刷新混沌万灵榜错误] [宝石不对应] ["+equipment.getName()+"]");
									out.print("[刷新混沌万灵榜错误] [宝石不对应] ["+equipment.getName()+"]");
									return ;
								}
							}
							*/
							ee.setDirty(true);
						}
					}else{
						out.print("刷新失败"+id+"<br/>");
					}
				}
			}
		}
		
	%>

</body>

</html>
