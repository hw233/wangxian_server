<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PetEggPropsEntity"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page
	import="com.xuanzhi.tools.simplejpa.impl.DefaultSimpleEntityManagerFactory"%>

<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.uniteserver.UnitedServerManager"%>
<%@page import="com.xuanzhi.tools.simplejpa.annotation.SimpleVersion"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%>
<%@page import="java.util.List"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.NewMagicWeaponEntity"%>
<%@page import="java.util.ArrayList"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.BaoShiXiaZiData"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.homestead.cave.Cave"%>
<%@page
	import="com.fy.engineserver.homestead.faery.service.FaeryManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>找回合服丢失的数据</title>
</head>
<body>
<form action="">请输入文件目录：<input type="text" name="fileName"
	value="simpleEMF_hefu.xml"></input><br>
请输入物品id,用英文逗号间隔<input type="text" name="ids"></input><br>
请输入宠物id,用英文逗号间隔<input type="text" name="petIds"></input><br>
请输入仙府id,用英文逗号间隔<input type="text" name="caveIds"></input><br>
请输入角色id,用英文逗号间隔<input type="text" name="playerIds"></input><br>
<input type="submit" name="提交"></input></form>
<%
	String emfPath1 = request.getRealPath("/") + "WEB-INF/spring_config/";
	String fileName = request.getParameter("fileName");
	if (fileName != null && !"".equals(fileName)) {

		DefaultSimpleEntityManagerFactory factory = new DefaultSimpleEntityManagerFactory(emfPath1 + fileName);
		String idsStr = request.getParameter("ids");
		int index = 0;
		if (idsStr != null && !"".equals(idsStr)) {
			String ids[] = idsStr.split(",");

			long articleIds[] = new long[ids.length];
			for (int i = 0; i < ids.length; i++) {
				articleIds[i] = Long.valueOf(ids[i]);
			}

			SimpleEntityManager<ArticleEntity> articleEntityManager = factory.getSimpleEntityManager(ArticleEntity.class);
			Field versionField = UnitedServerManager.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
			versionField.setAccessible(true);

			for (long id : articleIds) {
				ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(id);
				if (ae == null) {
					ArticleEntity ae2 = articleEntityManager.find(id);
					if (ae2 == null) {
						out.println("原数据库无此物品！！ id : " + id + "<br>");
						continue;
					}
					versionField.setInt(ae2, 0);
					ArticleEntityManager.getInstance().em.flush(ae2);
					out.println("[存储物品:" + ae2.getId() + "] [" + ae2.getArticleName() + "]" + "[成功！！]<br>");
					index++;
					//是装备要判断装备上镶嵌的宝石和器灵
					if (ae2 instanceof EquipmentEntity) {
						EquipmentEntity ee = (EquipmentEntity) ae2;

						//是否镶嵌了宝石
						for (int j = 0; j < ee.getInlayArticleIds().length; j++) {
							if (ee.getInlayArticleIds()[j] > 0) {
								ArticleEntity inlayEntity = DefaultArticleEntityManager.getInstance().getEntity(ee.getInlayArticleIds()[j]);
								if (inlayEntity == null) {
									ArticleEntity inlayEntity2 = articleEntityManager.find(ee.getInlayArticleIds()[j]);
									if (inlayEntity2 == null) {
										out.println("原数据库无此物品！！ id : " + ee.getInlayArticleIds()[j] + "<br>");
										continue;
									}
									versionField.setInt(inlayEntity2, 0);
									ArticleEntityManager.getInstance().em.flush(inlayEntity2);
									index++;
									//镶嵌的是宝石要判断是否是宝石匣
									BaoShiXiaZiData xiazidata = ArticleManager.getInstance().getxiLianData(null, ee.getInlayArticleIds()[j]);

									if (xiazidata == null) {
										SimpleEntityManager<BaoShiXiaZiData> baoShiXiaZiData = factory.getSimpleEntityManager(BaoShiXiaZiData.class);
										Field versionField2 = UnitedServerManager.getFieldByAnnotation(BaoShiXiaZiData.class, SimpleVersion.class);
										versionField2.setAccessible(true);
										BaoShiXiaZiData xiazidata2 = baoShiXiaZiData.find(ee.getInlayArticleIds()[j]);
										if (xiazidata2 == null) {
											out.println("原数据库无此物品！！ id : " + ee.getInlayArticleIds()[j] + "<br>");
											continue;
										}
										versionField2.setInt(xiazidata2, 0);
										ArticleEntityManager.baoShiXiLianEM.flush(xiazidata2);
										index++;
										//宝石匣要判断镶嵌的宝石
										long[] xiaziIds = xiazidata2.getIds();
										for (int k = 0; k < xiaziIds.length; k++) {
											if (xiaziIds[k] > 0) {
												ArticleEntity inlayEntity3 = ArticleEntityManager.getInstance().getEntity(xiaziIds[k]);
												if (inlayEntity2 == null) {
													ArticleEntity inlayEntity4 = articleEntityManager.find(xiaziIds[k]);
													if (inlayEntity4 == null) {
														out.println("原数据库无此物品！！ id : " + ee.getInlayArticleIds()[j] + "<br>");
														continue;
													}
													versionField.setInt(inlayEntity4, 0);
													ArticleEntityManager.getInstance().em.flush(inlayEntity4);
													index++;
												} else {
													out.println("数据库有此物品！！ id : " + ee.getInlayArticleIds()[j] + " name : " + inlayEntity.getArticleName() + "<br>");
												}
											}
										}
									} else {
										out.println("数据库有此物品！！ id : " + ee.getInlayArticleIds()[j] + " name : " + inlayEntity.getArticleName() + "<br>");
									}
								} else {
									out.println("数据库有此物品！！ id : " + ee.getInlayArticleIds()[j] + " name : " + inlayEntity.getArticleName() + "<br>");
								}

							}
						}

						//是否镶嵌了器灵
						for (int j = 0; j < ee.getInlayQiLingArticleIds().length; j++) {
							if (ee.getInlayQiLingArticleIds()[j] > 0) {
								ArticleEntity qiLingEntity = DefaultArticleEntityManager.getInstance().getEntity(ee.getInlayQiLingArticleIds()[j]);
								if (qiLingEntity == null) {
									ArticleEntity qiLingEntity2 = articleEntityManager.find(ee.getInlayQiLingArticleIds()[j]);
									if (qiLingEntity2 == null) {
										out.println("原数据库无此物品！！ id : " + ee.getInlayQiLingArticleIds()[j] + "<br>");
										continue;
									}
									versionField.setInt(qiLingEntity2, 0);
									ArticleEntityManager.getInstance().em.flush(qiLingEntity2);
									index++;
								} else {
									out.println("数据库有此物品！！ id : " + ee.getInlayQiLingArticleIds()[j] + " name : " + qiLingEntity.getArticleName() + "<br>");
								}
							}
						}
					}else if(ae2 instanceof PetEggPropsEntity){
						PetEggPropsEntity pe =(PetEggPropsEntity)ae2;
						long petId = pe.getPetId();
						SimpleEntityManager<Pet> petEntityManager = factory.getSimpleEntityManager(Pet.class);

						Field petField = UnitedServerManager.getFieldByAnnotation(Pet.class, SimpleVersion.class);
						petField.setAccessible(true);
						Pet pet1 = PetManager.em.find(petId);
						if (pet1 == null) {
							Pet pet2 = petEntityManager.find(petId);
							if (pet2 == null) {
								out.println("原数据库无此宠物！！ petid : " + petId + "<br>");
								continue;
							}
							petField.setInt(pet2, 0);
							PetManager.em.flush(pet2);
							out.println("[存储宠物:" + pet2.getId() + "] [" + pet2.getCategory() + "]" + "[成功！！]<br>");
							index++;
						} else {
							out.println("数据库有此宠物！！ petid : " + petId + "<br>");
						}
					}
				} else {
					out.println("数据库有此物品！！ id : " + id + " name : " + ae.getArticleName() + "<br>");
				}
			}
		}

		//宠物
		String petIdsStr = request.getParameter("petIds");
		if (petIdsStr != null && !"".equals(petIdsStr)) {
			String ids[] = petIdsStr.split(",");

			long petIds[] = new long[ids.length];
			for (int i = 0; i < ids.length; i++) {
				petIds[i] = Long.valueOf(ids[i]);
			}
			for (long id : petIds) {
				SimpleEntityManager<Pet> petEntityManager = factory.getSimpleEntityManager(Pet.class);

				Field petField = UnitedServerManager.getFieldByAnnotation(Pet.class, SimpleVersion.class);
				petField.setAccessible(true);
				Pet pet1 = PetManager.em.find(id);
				if (pet1 == null) {
					Pet pet2 = petEntityManager.find(id);
					if (pet2 == null) {
						out.println("原数据库无此宠物！！ id : " + id + "<br>");
						continue;
					}
					petField.setInt(pet2, 0);
					PetManager.em.flush(pet2);
					out.println("[存储宠物:" + pet2.getId() + "] [" + pet2.getCategory() + "]" + "[成功！！]<br>");
					index++;
				} else {
					out.println("数据库有此宠物！！ id : " + id + "<br>");
				}
			}
		}

		//仙府
		String caveIdsStr = request.getParameter("caveIds");
		if (caveIdsStr != null && !"".equals(caveIdsStr)) {
			String ids[] = caveIdsStr.split(",");

			long caveIds[] = new long[ids.length];
			for (int i = 0; i < ids.length; i++) {
				caveIds[i] = Long.valueOf(ids[i]);
			}
			for (long id : caveIds) {
				SimpleEntityManager<Cave> caveEntityManager = factory.getSimpleEntityManager(Cave.class);

				Field caveField = UnitedServerManager.getFieldByAnnotation(Cave.class, SimpleVersion.class);
				caveField.setAccessible(true);
				Cave cave1 = FaeryManager.caveEm.find(id);
				if (cave1 == null) {
					Cave cave2 = caveEntityManager.find(id);
					if (cave2 == null) {
						out.println("原数据库无此仙府！！ id : " + id + "<br>");
						continue;
					}
					caveField.setInt(cave2, 0);
					FaeryManager.caveEm.flush(cave2);
					out.println("[存储仙府:" + cave2.getId() + "]" + "[成功！！]<br>");
					index++;
				} else {
					out.println("数据库有此仙府！！ id : " + id + "<br>");
				}
			}
		}
		
		String playerIdStr = request.getParameter("playerIds");
		if (playerIdStr != null && !"".equals(playerIdStr)) {
			String ids[] = playerIdStr.split(",");
			long playerIds[] = new long[ids.length];
			for (int i = 0; i < ids.length; i++) {
				playerIds[i] = Long.valueOf(ids[i]);
			}
			for (long id : playerIds) {
				SimpleEntityManager<Player> playerEntityManager = factory.getSimpleEntityManager(Player.class);
				Field playerField = UnitedServerManager.getFieldByAnnotation(Player.class, SimpleVersion.class);
				playerField.setAccessible(true);
				GamePlayerManager gpm = ((GamePlayerManager) GamePlayerManager.getInstance());
				Player player1 = gpm.em.find(id);
				if(player1 == null){
					Player player2 = playerEntityManager.find(id);
					if(player2 == null){
						out.print("原数据库无此角色！！id : " + id + "<br>");
						continue;
					}
					Player player3 = null;
					try{
						player3 = gpm.getPlayer(player2.getName());
					} catch (Exception e){
						
					}
					if(player3 != null){
						out.print("有重名角色!<br>");
						continue;
					} 
					playerField.setInt(player2, 0);
					gpm.em.flush(player2);
					out.print("[存储角色:" + player2.getId() + "] [角色名:" + player2.getName() + "]" + "[成功！！]<br>");
					index++;
				} else {
					out.print("数据库有此角色！！ id : " + id + "<br>");
				}
			}
		}
		out.println("总修复数:" + index);
	}
%>

</body>
</html>