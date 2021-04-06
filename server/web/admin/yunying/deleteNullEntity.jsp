<%@page
	import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.PetPropsEntity"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	String playerName = request.getParameter("playerName");
	out.print("playerName:" + playerName);
	out.print("<HR/>");
	if (playerName == null) {
		playerName = "";
	} else {
		Player player = GamePlayerManager.getInstance().getPlayer(playerName);
		if (player == null) {
			out.print("<H2>输入的角色不存在:" + playerName + "</H2>");
		} else {
			if (GamePlayerManager.getInstance().isOnline(player.getId())) {
				out.print("<H2>输入的角色在线:" + playerName + "</H2>");
			} else {
				Knapsack common = player.getKnapsack_common();
				boolean modified = check(common, "包裹", player, out);
				if (modified) {
					player.setDirty(true, "knapsacks_common");
				}
				out.print("<HR/>");
				Knapsack cangku = player.getKnapsacks_cangku();
				modified = check(cangku, "仓库", player, out);
				if (modified) {
					player.setDirty(true, "knapsacks_cangku");
				}
				out.print("<HR/>");
				Knapsack fanngbao = player.getKnapsacks_fangBao();
				if (fanngbao != null) {
					modified = check(fanngbao, "防爆包", player, out);
					if (modified) {
						player.setDirty(true, "knapsacks_fangBao");
					}
					out.print("<HR/>");
				}
				Knapsack knapsacks_QiLing = player.getKnapsacks_QiLing();
				if (knapsacks_QiLing != null) {
					modified = check(knapsacks_QiLing, "器灵背包", player, out);
					if (modified) {
						player.setDirty(true, "knapsacks_QiLing");
					}
					out.print("<HR/>");
				}
				Knapsack k = player.getPetKnapsack();
				if (k != null) {
					modified = checkPetKnapsack(k, "宠物栏", player, out);
					if (modified) {
						k.setModified(true);
					}
					out.print("<HR/>");
				}

				Soul currSoul = player.getCurrSoul();
				EquipmentColumn ec = currSoul.getEc();
				modified = checkEquipmentColumn(ec, "当前激活元神装备栏", player, out);
				if (modified) {
					player.setDirty(true, "currSoul");
					if (player.getUnusedSoul() != null) {
						for (Soul s : player.getUnusedSoul()) {
							if (s.getEc() != null && s.getEc().isModified()) {
								player.setDirty(true, "unusedSoul");
							}
						}
					}
				}

				ArrayList<Long> horseIds = player.getHorseIdList();
				if (horseIds != null) {
					for (long id : horseIds) {
						Horse horse = HorseManager.getInstance().getHorseById(id, player);
						if (horse != null) {
							HorseEquipmentColumn ecHorse = horse.getEquipmentColumn();
							if (ec != null) {
								modified = checkHorseEquipmentColumn(ecHorse, "坐骑装备栏-" + horse.getHorseName(), player, out);
								if (modified) {
									horse.setEquipmentColumn(ecHorse);
								}
							} else {
								out.print(player.getLogString() + horse.getLogString() + "玩家坐骑无装备栏");
							}
						}
					}
				}
				out.print("<HR/>");

				List<Soul> unusedSoul = player.getUnusedSoul();
				if (unusedSoul != null) {
					for (Soul s : unusedSoul) {
						EquipmentColumn ec2 = s.getEc();
						modified = checkEquipmentColumn(ec, "未激活元神装备栏", player, out);
						if (modified) {
							player.setDirty(true, "unusedSoul");
						}
					}
				}
				out.print("<HR/>");

				Cave cave = FaeryManager.getInstance().getCave(player);
				if (cave != null) {
					CavePethouse pethouse = cave.getPethouse();
					modified = checkPetHookHouse(pethouse, "挂机宠物栏", player, out);
					if (modified) {
						cave.notifyFieldChange("pethouse");
					}
					out.print("<HR/>");

					modified = checkPetHouse(pethouse, "宠物仓库", player, out);
					if (modified) {
						cave.notifyFieldChange("pethouse");
					}
					out.print("<HR/>");
				}

				WingPanel wingPanel = player.getWingPanel();
				if (wingPanel != null) {
					modified = checkWingPanel(wingPanel, "翅膀面板", player, out);
					if (modified) {
						out.print("<HR/>");
					}
				}
				List<Mail> mails = MailManager.getInstance().getAllMails(player);
				if (mails.size() > 0) {
					modified = checkMails(mails, "邮箱", player, out);
					if (modified) {
						out.print("<hr/>");
					}
				}
				HuntLifeEntity huntLifr = player.getHuntLifr();
				if (huntLifr != null) {
					modified = checkHuntLife(huntLifr, "兽魂", player, out);
					if (modified) {
						out.print("<hr/>");
					}
				}
			}
		}
	}
%>
<%!public boolean check(Knapsack knapsack, String name, Player player, JspWriter out) {
		StringBuffer sbf = new StringBuffer(player.getLogString() + "[" + name + "]");
		Cell[] cells = knapsack.getCells();
		boolean modified = false;
		boolean modifyInlay = false;
		boolean modifyInlay2 = false;
		boolean modifyInlayQiLing = false;
		for (int i = 0; i < cells.length; i++) {
			Cell cell = cells[i];
			long entityId = cell.getEntityId();
			if (entityId > 0) {
				ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(entityId);
				if (ae == null) {
					sbf.append("[发现不存在的物品] [aeId:" + entityId + "][数量:" + cell.getCount() + "]");
					cell.count = 0;
					cell.entityId = -1;
					modified = true;
				} else if (ae instanceof EquipmentEntity) {
					EquipmentEntity ee = (EquipmentEntity) ae;
					long[] inlayArticleIds = ee.getInlayArticleIds();
					if (inlayArticleIds.length > 0) {
						for (int j = 0; j < inlayArticleIds.length; j++) {
							if (inlayArticleIds[j] > 0) {
								ArticleEntity inlayArticle = ArticleEntityManager.getInstance().getEntity(inlayArticleIds[j]);
								if (inlayArticle == null) { //装备上镶嵌的宝石无实体
									sbf.append("[发现不存在的物品] [aeId:" + inlayArticleIds[j] + "][数量:1]");
									inlayArticleIds[j] = -1;
									modifyInlay = true;
									modified = true;
								} else {
									BaoShiXiaZiData xiazidata = ArticleManager.getInstance().getxiLianData(player, inlayArticleIds[j]);
									if (xiazidata != null) {
										long[] ids = xiazidata.getIds();
										int[] colors = xiazidata.getColors();
										String[] names = xiazidata.getNames();
										for (int k = 0; k < ids.length; k++) {
											if (ids[k] > 0) {
												ArticleEntity inlayEntity2 = ArticleEntityManager.getInstance().getEntity(ids[k]);
												if (inlayEntity2 == null) { //装备上镶嵌的宝石神匣无实体
													sbf.append("[神匣内发现不存在的物品] [aeId:" + ids[k] + "][数量:1]");
													ids[k] = -1;
													names[k] = "";
													modifyInlay2 = true;
													modified = true;
												}
											}
										}
										if (modifyInlay2) {
											xiazidata.setIds(ids);
											xiazidata.setNames(names);
										}
									}
								}
							}
						}
					}
					if (modifyInlay) {
						ee.setInlayArticleIds(inlayArticleIds);
					}

					long[] inlayQiLingArticleIds = ee.getInlayQiLingArticleIds();
					if (inlayQiLingArticleIds.length > 0) {
						for (int j = 0; j < inlayQiLingArticleIds.length; j++) {
							if (inlayQiLingArticleIds[j] > 0) {
								ArticleEntity inlayQiLingArticle = ArticleEntityManager.getInstance().getEntity(inlayQiLingArticleIds[j]);
								if (inlayQiLingArticle == null) { //装备上镶嵌的器灵无实体
									sbf.append("[发现不存在的物品] [aeId:" + inlayQiLingArticleIds[j] + "][数量:1]");
									inlayQiLingArticleIds[j] = -1;
									modifyInlayQiLing = true;
									modified = true;
								}
							}
						}
					}
					if (modifyInlayQiLing) {
						ee.setInlayQiLingArticleIds(inlayQiLingArticleIds);
					}
				}
			}
		}
		ArticleManager.logger.warn(sbf.toString());
		try {
			out.print(sbf.toString() + "<BR/>");
		} catch (Exception e) {
			ArticleManager.logger.warn("后台查询空格子异常", e);
		}
		return modified;
	}

	/** 检查玩家装备栏，以及装备栏的宝石槽和器灵槽	*/
	public boolean checkEquipmentColumn(EquipmentColumn ec, String name, Player player, JspWriter out) {
		StringBuffer sbf = new StringBuffer(player.getLogString() + "[" + name + "]");
		boolean modified = false;
		boolean modifyInlay = false;
		boolean modifyInlay2 = false;
		boolean modifyInlayQiLing = false;
		if (ec != null) {
			long equipmentIds[] = ec.getEquipmentIds();
			for (int i = 0; i < equipmentIds.length; i++) {
				if (equipmentIds[i] > 0) {
					ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(equipmentIds[i]);
					if (ae == null) { //玩家身上装备无实体
						sbf.append("[发现不存在的物品] [aeId:" + equipmentIds[i] + "][数量:1]");
						equipmentIds[i] = -1;
						modified = true;
					} else if (ae instanceof EquipmentEntity) {
						EquipmentEntity ee = (EquipmentEntity) ae;
						long[] inlayArticleIds = ee.getInlayArticleIds();
						if (inlayArticleIds.length > 0) {
							for (int j = 0; j < inlayArticleIds.length; j++) {
								if (inlayArticleIds[j] > 0) {
									ArticleEntity inlayArticle = ArticleEntityManager.getInstance().getEntity(inlayArticleIds[j]);
									if (inlayArticle == null) { //装备上镶嵌的宝石无实体
										sbf.append("[发现不存在的物品] [aeId:" + inlayArticleIds[j] + "][数量:1]");
										inlayArticleIds[j] = -1;
										modifyInlay = true;
										modified = true;
									} else {
										BaoShiXiaZiData xiazidata = ArticleManager.getInstance().getxiLianData(player, inlayArticleIds[j]);
										if (xiazidata != null) {
											long[] ids = xiazidata.getIds();
											int[] colors = xiazidata.getColors();
											String[] names = xiazidata.getNames();
											for (int k = 0; k < ids.length; k++) {
												if (ids[k] > 0) {
													ArticleEntity inlayEntity2 = ArticleEntityManager.getInstance().getEntity(ids[k]);
													if (inlayEntity2 == null) { //装备上镶嵌的宝石神匣无实体
														sbf.append("[神匣内发现不存在的物品] [aeId:" + ids[k] + "][数量:1]");
														ids[k] = -1;
														names[k] = "";
														modifyInlay2 = true;
														modified = true;
													}
												}
											}
											if (modifyInlay2) {
												xiazidata.setIds(ids);
												xiazidata.setNames(names);
											}
										}
									}
								}
							}
						}
						if (modifyInlay) {
							ee.setInlayArticleIds(inlayArticleIds);
						}

						long[] inlayQiLingArticleIds = ee.getInlayQiLingArticleIds();
						if (inlayQiLingArticleIds.length > 0) {
							for (int j = 0; j < inlayQiLingArticleIds.length; j++) {
								if (inlayQiLingArticleIds[j] > 0) {
									ArticleEntity inlayQiLingArticle = ArticleEntityManager.getInstance().getEntity(inlayQiLingArticleIds[j]);
									if (inlayQiLingArticle == null) { //装备上镶嵌的器灵无实体
										sbf.append("[发现不存在的物品] [aeId:" + inlayQiLingArticleIds[j] + "][数量:1]");
										inlayQiLingArticleIds[j] = -1;
										modifyInlayQiLing = true;
										modified = true;
									}
								}
							}
						}
						if (modifyInlayQiLing) {
							ee.setInlayQiLingArticleIds(inlayQiLingArticleIds);
						}
					}
				}
			}
			if (modified) {
				try {
					ec.setEquipmentIds(equipmentIds);
				} catch (Exception e) {
					ArticleManager.logger.error("[后台删空格子] [异常] [设置装备id]", e);
				}
			}
		}
		ArticleManager.logger.warn(sbf.toString());
		try {
			out.print(sbf.toString() + "<BR/>");
		} catch (Exception e) {
			ArticleManager.logger.warn("后台查询空格子异常", e);
		}
		return modified;
	}

	public boolean checkPetKnapsack(Knapsack knapsack, String name, Player player, JspWriter out) {
		StringBuffer sbf = new StringBuffer(player.getLogString() + "[" + name + "]");
		Cell[] cells = knapsack.getCells();
		boolean modified = false;
		for (int i = 0; i < cells.length; i++) {
			Cell cell = cells[i];
			long entityId = cell.getEntityId();
			if (entityId > 0) {
				ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(entityId);
				if (ae == null) { //根据物品id判断宠物不存在
					sbf.append("[发现不存在的物品] [aeId:" + entityId + "][数量:" + cell.getCount() + "]");
					cell.entityId = -1;
					modified = true;
				} else {
					if (ae instanceof PetPropsEntity) {
						PetPropsEntity ppe = (PetPropsEntity) ae;
						long petId = ppe.getPetId();
						Pet pet = PetManager.getInstance().getPet(petId);
						if (pet == null) { //根据宠物id判断宠物不存在
							sbf.append("[发现不存在的物品] [aeId:" + petId + "][数量:1 ]");
							cell.entityId = -1;
							modified = true;
						}
					}
				}
			}
		}
		ArticleManager.logger.warn(sbf.toString());
		try {
			out.print(sbf.toString() + "<BR/>");
		} catch (Exception e) {
			ArticleManager.logger.warn("后台查询空格子异常", e);
		}
		return modified;
	}

	public boolean checkPetHookHouse(CavePethouse pethouse, String name, Player player, JspWriter out) {
		StringBuffer sbf = new StringBuffer(player.getLogString() + "[" + name + "]");
		PetHookInfo[] hookInfos = pethouse.getHookInfos();
		boolean modified = false;
		if (hookInfos.length > 0) {
			for (int i = 0; i < hookInfos.length; i++) {
				if (hookInfos[i] != null) {
					if (hookInfos[i].getPetId() > 0) {
						ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(hookInfos[i].getArticleId());
						if (ae == null) { //根据宠物id判断宠物不存在
							sbf.append("[发现不存在的物品]" + hookInfos[i].toString() + "<br/>");
							hookInfos[i] = null;
							modified = true;
						}
					}
				}
			}
		}
		ArticleManager.logger.warn(sbf.toString());
		try {
			out.print(sbf.toString() + "<BR/>");
		} catch (Exception e) {
			ArticleManager.logger.warn("后台查询空格子异常", e);
		}
		return modified;
	}

	public boolean checkPetHouse(CavePethouse pethouse, String name, Player player, JspWriter out) {
		StringBuffer sbf = new StringBuffer(player.getLogString() + "[" + name + "]");
		long[] storePets = pethouse.getStorePets();
		boolean modified = false;
		if (storePets.length > 0) {
			for (int i = 0; i < storePets.length; i++) {
				if (storePets[i] > 0) {
					//					Pet pet = PetManager.getInstance().getPet(storePets[i]);
					ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(storePets[i]);
					if (ae == null) { //根据宠物道具id判断宠物不存在
						sbf.append("[发现不存在的物品] [宠物道具Id:" + storePets[i] + "][数量:1 ]");
						storePets[i] = -1;
						modified = true;
					}
				}
			}
		}
		ArticleManager.logger.warn(sbf.toString());
		try {
			out.print(sbf.toString() + "<BR/>");
		} catch (Exception e) {
			ArticleManager.logger.warn("后台查询空格子异常", e);
		}
		return modified;
	}

	public boolean checkWingPanel(WingPanel wingPanel, String name, Player player, JspWriter out) {
		StringBuffer sbf = new StringBuffer();
		sbf.append(player.getLogString() + "[" + name + "]");
		long brightInlayId = wingPanel.getBrightInlayId();
		boolean modified = false;
		boolean modifiedInlay = false;
		boolean modifiedBrightInly = false;
		boolean hasNullCell = false;
		if (brightInlayId > 0) {
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(brightInlayId);
			if (ae == null) { //根据光效宝石id判断光效宝石不存在
				if (!hasNullCell) {
					hasNullCell = true;
				}
				sbf.append("[发现不存在的物品] [光效宝石Id:" + brightInlayId + "][数量:1 ]");
				brightInlayId = 0;
				modified = true;
				modifiedInlay = true;
			}
		}
		if (modifiedInlay) {
			wingPanel.setBrightInlayId(brightInlayId);
		}

		long[] inlayArticleIds = wingPanel.getInlayArticleIds();
		for (int i = 0; i < inlayArticleIds.length; i++) {
			if (inlayArticleIds[i] > 0) {
				ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(inlayArticleIds[i]);
				if (ae == null) { //根据宝石id判断翅膀面板宝石不存在
					if (!hasNullCell) {
						hasNullCell = true;
						sbf.append(player.getLogString() + "[" + name + "]");
					}
					sbf.append("[发现不存在的物品] [宝石Id:" + brightInlayId + "][数量:1 ]");
					inlayArticleIds[i] = -1;
					modified = true;
					modifiedBrightInly = true;
				}
			}
		}
		if (modifiedBrightInly) {
			wingPanel.setInlayArticleIds(inlayArticleIds);
		}
		ArticleManager.logger.warn(sbf.toString());
		try {
			if (hasNullCell) {
				out.print(sbf.toString() + "<BR/>");
			}
		} catch (Exception e) {
			ArticleManager.logger.warn("后台查询空格子异常", e);
		}
		return modified;
	}

	/** 检查玩家坐骑装备栏	*/
	public boolean checkHorseEquipmentColumn(HorseEquipmentColumn ec, String name, Player player, JspWriter out) {
		StringBuffer sbf = new StringBuffer(player.getLogString() + "[" + name + "]");
		boolean modified = false;
		if (ec != null) {
			long equipmentIds[] = ec.getEquipmentIds();
			for (int i = 0; i < equipmentIds.length; i++) {
				if (equipmentIds[i] > 0) {
					ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(equipmentIds[i]);
					if (ae == null) { //坐骑身上装备无实体
						sbf.append("[发现不存在的物品] [aeId:" + ec.getEquipmentIds()[i] + "][数量:1]");
						equipmentIds[i] = -1;
						modified = true;
					}
				}
			}
			if (modified) {
				ec.setEquipmentIds(equipmentIds);
			}
		}
		ArticleManager.logger.warn(sbf.toString());
		try {
			out.print(sbf.toString() + "<BR/>");
		} catch (Exception e) {
			ArticleManager.logger.warn("后台查询空格子异常", e);
		}
		return modified;
	}

	/** 检查玩家邮件	*/
	public boolean checkMails(List<Mail> mails, String name, Player player, JspWriter out) {
		StringBuffer sbf = new StringBuffer(player.getLogString() + "[" + name + "]");
		boolean modified = false;
		if (mails.size() > 0) {
			for (Mail mail : mails) {
				Cell cells[] = mail.getCells();
				for (Cell cell : cells) {
					long aid = cell.getEntityId();
					if (aid > 0) {
						ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(aid);
						if (ae == null) {
							sbf.append("[发现不存在的物品] [aeId:" + aid + "][数量:" + cell.getCount() + "]");
							cell.setEntityId(-1);
							cell.setCount(0);
							modified = true;
						} else {
							Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
						}
					}
				}
				if (modified) {
					mail.setCells(cells);
				}
			}
		}
		ArticleManager.logger.warn(sbf.toString());
		try {
			out.print(sbf.toString() + "<BR/>");
		} catch (Exception e) {
			ArticleManager.logger.warn("后台查询空格子异常", e);
		}
		return modified;
	}

	//检查玩家兽魂
	public boolean checkHuntLife(HuntLifeEntity huntLifr, String name, Player player, JspWriter out) {
		StringBuffer sbf = new StringBuffer(player.getLogString() + "[" + name + "]");
		boolean modified = false;
		for (int i = 0; i < huntLifr.getHuntDatas().length; i++) {
			if (huntLifr.getHuntDatas()[i] <= 0) {
				continue;
			}
			HuntLifeArticleEntity ae = (HuntLifeArticleEntity) DefaultArticleEntityManager.getInstance().getEntity(huntLifr.getHuntDatas()[i]);
			if (ae == null) {
				sbf.append("[发现不存在的物品] [aeId:" + huntLifr.getHuntDatas()[i] + "][数量:" + 1 + "]");
				huntLifr.getHuntDatas()[i] = -1;
				modified = true;
			}
		}
		if (modified) {
			HuntLifeEntityManager.em.notifyFieldChange(huntLifr, "huntDatas");
		}
		ArticleManager.logger.warn(sbf.toString());
		try {
			out.print(sbf.toString() + "<BR/>");
		} catch (Exception e) {
			ArticleManager.logger.warn("后台查询空格子异常", e);
		}
		return modified;
	}%>



<%@page import="com.fy.engineserver.homestead.cave.PetHookInfo"%>
<%@page import="com.fy.engineserver.homestead.cave.CavePethouse"%>
<%@page import="com.fy.engineserver.homestead.cave.Cave"%>
<%@page
	import="com.fy.engineserver.homestead.faery.service.FaeryManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.wing.WingPanel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.sprite.horse.Horse"%>
<%@page import="com.fy.engineserver.sprite.horse.HorseManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.equipments.HorseEquipmentColumn"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.BaoShiXiaZiData"%>
<%@page import="com.fy.engineserver.mail.Mail"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.magicweapon.MagicWeapon"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.MagicWeaponEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.magicweapon.huntLife.instance.HuntLifeEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.HuntLifeArticleEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.magicweapon.huntLife.HuntLifeEntityManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="" method="post">角色名<input type="text"
	name="playerName" value="<%=playerName%>"> <input type="submit"
	value="查询"></form>
</body>
</html>