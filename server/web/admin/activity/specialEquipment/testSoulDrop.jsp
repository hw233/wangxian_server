<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="com.fy.engineserver.billboard.special.SpecialEquipmentManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.*"%>



<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.billboard.special.SpecialEquipmentBillBoard"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Set"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Weapon"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>测试元神掉落</title>
</head>
<body>
	
		<%
	
		String name = request.getParameter("name");
		String name2 = request.getParameter("name2");
		if(name == null || name.equals("") || name2 == null || name2.equals("")){
			
		}else{
			Player player = PlayerManager.getInstance().getPlayer(name);
			Player player2 = PlayerManager.getInstance().getPlayer(name2);
			SpecialEquipmentManager sem = SpecialEquipmentManager.getInstance();

			try{
				if(player2 instanceof Player){
					Map<String, List<Long>> map = sem.getSpecialEquipmentBillBoard().getSpecial1Map();
					ArticleEntityManager aem = ArticleEntityManager.getInstance();
					
					Set<String> set = map.keySet();
					String[] arr = set.toArray(new String[0]);
					int max = arr.length;
					List<Long> list = null;
					ArticleEntity ae = null;
					Special_1EquipmentEntity se1 = null;
					for(int i = 0;i<max;i++){
						list =  map.get(arr[i]);
						if(list != null && list.size() > 0){
							ae = ArticleEntityManager.getInstance().getEntity(list.get(0));
							if( ae != null && ae instanceof Special_1EquipmentEntity){
								se1 = (Special_1EquipmentEntity)ae;
								if(se1.getPlayerId() == player.getId()){
									if(se1.isDrop()){
										boolean drop = player.玩家掉落物品(list.get(0),player2);
										if(!drop){
											Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
											if(a != null && a instanceof Equipment){
												Equipment eq = (Equipment)a;
												byte equipmentType = -1;
												if(eq instanceof Weapon){
													equipmentType = 0;
												}else{
													equipmentType = (byte)eq.getEquipmentType();
												}
												for(Soul soul :player.getSouls()){
													EquipmentColumn ec = soul.getEc();
													EquipmentEntity ee = ec.get(equipmentType);
													if(ee != null && ee.getId() == se1.getId()){
														ee = soul.getEc().takeOff(equipmentType, soul.getSoulType());
														if(ee != null){
															drop = true;
															player.人掉落物品(player.currentGame, player, ee, 1, player2);
															break;
														}
														break;
													}
												}
											}
										}
//										specialEquipmentBillBoard.removeSpecial1(se1.getArticleName(), se1.getId());
										se1.setPlayerId(-1);
										if(SpecialEquipmentManager.logger.isWarnEnabled()){
											SpecialEquipmentManager.logger.warn("[玩家死亡掉落特殊装备成功] ["+player.getLogString()+"] ["+se1.getArticleName()+"] [是否真掉落+"+drop+"]");
										}
									}
								}
							}
						}
					}
				}else{
					SpecialEquipmentManager.logger.warn("[玩家死亡不是被玩家杀死不掉装备] ["+player.getLogString()+"]");
				}
			}catch(Exception e){
				SpecialEquipmentManager.logger.error("[玩家死亡掉落装备错误] ["+player.getLogString()+"]",e);
			}
		
			
			out.print("操作成功<br/>");
		}
	%>
	
	<form action="">
		测试杀死元神掉落<br/>
		被杀死人物名:<input type="text" name="name"/><br/>
		操作者:<input type="text" name="name2"/><br/>
		<input type="submit" value="submit"/>
	</form>
	
	
</body>
</html>