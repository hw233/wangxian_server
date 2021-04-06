package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.sprite.Player;

/**
 * 配方
 * 
 * 装备合成系统采用的配方
 * 
 * 配方包含如下的数据结构：
 * 
 * 需要的材料，及其数量 * 产生的物品
 * 
 *
 */
public class FormulaProps extends Props{

	/**
	 * 合成需要的材料名字以及个数
	 */
	private ArticleProperty[] apps;

	/**
	 * 合成后物品的名字，可以为多个物品，以分号分隔
	 */
	private String composedArticleName;
	
	/**
	 * 这个属性只有在合成后的物品名为多个的时候才用到，并且和上面的物品名对应，为物品出现概率，多个物品的概率用逗号分隔
	 */
	private String composeRandomProbability;
	
	/**
	 * 是否为法宝标记，true为法宝，需要特殊处理，默认为不是法宝
	 */
	private boolean magicWeaponFlag = false;
	
	/**
	 * 这个属性只有在法宝标记为true时才用，并且和上面的物品名对应，为该物品的法宝所对应的星级，多个物品的星级用逗号分隔
	 */
	private String starValues;

	/**
	 * 合成所需费用
	 */
	private int composedPrice = 100;

	public ArticleProperty[] getApps() {
		return apps;
	}

	public void setApps(ArticleProperty[] apps) {
		this.apps = apps;
	}

	public String getComposedArticleName() {
		return composedArticleName;
	}

	public void setComposedArticleName(String composedArticleName) {
		this.composedArticleName = composedArticleName;
	}

	public int getComposedPrice() {
		return composedPrice;
	}

	public void setComposedPrice(int composedPrice) {
		this.composedPrice = composedPrice;
	}

	public boolean isOverlap() {
		return false;
	}

	public String getComposeRandomProbability() {
		return composeRandomProbability;
	}

	public void setComposeRandomProbability(String composeRandomProbability) {
		this.composeRandomProbability = composeRandomProbability;
	}

	public boolean isMagicWeaponFlag() {
		return magicWeaponFlag;
	}

	public void setMagicWeaponFlag(boolean magicWeaponFlag) {
		this.magicWeaponFlag = magicWeaponFlag;
	}

	public String getStarValues() {
		return starValues;
	}

	public void setStarValues(String starValues) {
		this.starValues = starValues;
	}

//	public int getColorType() {
//		
//		ArticleManager am = ArticleManager.getInstance();
//		Article article = null;
//		if(composedArticleName == null){
//			return 0;
//		}
//		String[] composeArticles = composedArticleName.split(";");
//		if(composeArticles.length == 1){
//			composeArticles = composedArticleName.split("；");
//		}
//		String comArticle = "";
//		if(composeArticles.length != 0){
//			comArticle = composeArticles[0];
//		}
//		article = am.getArticle(comArticle);
//		if(article != null){
//			if(article instanceof Gem){
//				return ((Gem)article).getColorType();
//			}
//		}
//		else{
//			ArticleManager.logger.warn("[合成][物种不存在]["+comArticle);
//			return 0;
//		}
//		
//		return article.getColorType();
//	}

	/**
	 * 使用道具
	 * 
	 * @param player
	 */
	public boolean use(Game game, Player player, ArticleEntity ae) {
//		if(!super.use(game,player,ae)){
//			return false;
//		}
//		if(ae != null){
//			ArticleManager am = ArticleManager.getInstance();
//			if(am != null && apps != null){
//				if(composedArticleName == null){
//					EquipmentUpgradeMamager.logger.warn("[配方使用] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] [composedArticleName == null]");
//					return false;
//				}
//				String[] composeArticles = composedArticleName.split(";");
//				if(composeArticles.length == 1){
//					composeArticles = composedArticleName.split("；");
//				}
//				
//				if(composeArticles.length == 1){
//					String finalComposeArticleName = composeArticles[0];
//					Article article = am.getArticle(finalComposeArticleName);
//					if(article != null){
//						ArticleEntityManager aem = ArticleEntityManager.getInstance();
//						ArticleEntity aee = null;
//						int cellindex = player.getKnapsack().getArticleCellPos(finalComposeArticleName);
//						if(cellindex == -1){
//							if(article instanceof Equipment){
//
//								
//								aee = aem.createTempEntity((Equipment)article, false,ArticleEntityManager.CREATE_REASON_FORMULAPROPS_INFO,player);
//
//							}else if(article instanceof Props){
//
//								aee = aem.createTempEntity((Props)article, false,ArticleEntityManager.CREATE_REASON_FORMULAPROPS_INFO,player);
//
//							}else{
//
//								aee = aem.createTempEntity(article,false, ArticleEntityManager.CREATE_REASON_FORMULAPROPS_INFO,player);
//
//							}
//						}else{
//							aee = player.getKnapsack().getArticleEntityByCell(cellindex);
//						}
//						boolean compoundFlag = true;
//						long[] needArticleIds = new long[apps.length];
//						int[] needArticleCounts = new int[apps.length];
//						int[] hasArticleCounts = new int[apps.length];
//						Knapsack sack = player.getKnapsack();
//						for(int i = 0; i < apps.length; i++){
//							ArticleProperty ap = apps[i];
//							if(ap != null){
//								int c = sack.countArticle(ap.getArticleName());
//								int index = sack.getArticleCellPos(ap.getArticleName());
//								if(index == -1){
//									Article tempArticle = am.getArticle(ap.getArticleName());
//									if(tempArticle != null){
//										ArticleEntity aeee = null;
//										if(tempArticle instanceof Equipment){
//
//											aeee = aem.createTempEntity((Equipment)tempArticle,false, ArticleEntityManager.CREATE_REASON_FORMULAPROPS_INFO,player);
//
//										}else if(tempArticle instanceof Props){
//
//											aeee = aem.createTempEntity((Props)tempArticle,false, ArticleEntityManager.CREATE_REASON_FORMULAPROPS_INFO,player);
//
//										}else{
//
//											aeee = aem.createTempEntity(tempArticle, false,ArticleEntityManager.CREATE_REASON_FORMULAPROPS_INFO,player);
//
//										}
//										needArticleIds[i] = aeee.getId();
//									}else{
//										EquipmentUpgradeMamager.logger.warn("【配方使用】【异常】合成所需的材料【"+ap.getArticleName()+"】的物种为空[player:"+player.getName()+"]");
//									}
//								}else{
//									ArticleEntity aeee = sack.getArticleEntityByCell(index);
//									if(aeee != null){
//										needArticleIds[i] = aeee.getId();
//									}else{
//										EquipmentUpgradeMamager.logger.warn("【配方使用】【异常】背包中的【"+ap.getArticleName()+"】对象为空[player:"+player.getName()+"]");
//									}
//								}
//								hasArticleCounts[i] = c;
//								needArticleCounts[i] = ap.getValue();
//								if(c < ap.getValue()){
//									compoundFlag = false;
//								}
//							}else{
//								EquipmentUpgradeMamager.logger.warn("【配方使用】【异常】【合成所需材料数据异常】[player:"+player.getName()+"]");
//							}
//						}
//						EQUIPMENT_COMPOUND_PREPARE_REQ req = new EQUIPMENT_COMPOUND_PREPARE_REQ(GameMessageFactory.nextSequnceNum(), ae.getId(), AritcleInfoHelper.generate(player, aee), needArticleIds, needArticleCounts, hasArticleCounts, composedPrice, compoundFlag);
//						player.addMessageToRightBag(req);
//					}else{
//						EquipmentUpgradeMamager.logger.warn("【配方使用】【失败】合成后的物品【"+finalComposeArticleName+"】的物种为空[player:"+player.getName()+"]");
//						return false;
//					}
//				}else{
//					ArticleEntityManager aem = ArticleEntityManager.getInstance();
//					boolean compoundFlag = true;
//					long[] needArticleIds = new long[apps.length];
//					int[] needArticleCounts = new int[apps.length];
//					int[] hasArticleCounts = new int[apps.length];
//					Knapsack sack = player.getKnapsack();
//					for(int i = 0; i < apps.length; i++){
//						ArticleProperty ap = apps[i];
//						if(ap != null){
//							int c = sack.countArticle(ap.getArticleName());
//							int index = sack.getArticleCellPos(ap.getArticleName());
//							if(index == -1){
//								Article tempArticle = am.getArticle(ap.getArticleName());
//								if(tempArticle != null){
//									ArticleEntity aeee = null;
//									if(tempArticle instanceof Equipment){
//
//										aeee = aem.createTempEntity((Equipment)tempArticle,false, ArticleEntityManager.CREATE_REASON_FORMULAPROPS_INFO,player);
//
//									}else if(tempArticle instanceof Props){
//
//										aeee = aem.createTempEntity((Props)tempArticle,false, ArticleEntityManager.CREATE_REASON_FORMULAPROPS_INFO,player);
//
//									}else{
//
//										aeee = aem.createTempEntity(tempArticle, false,ArticleEntityManager.CREATE_REASON_FORMULAPROPS_INFO,player);
//
//									}
//									needArticleIds[i] = aeee.getId();
//								}else{
//									EquipmentUpgradeMamager.logger.warn("【配方使用】【异常】合成所需的材料【"+ap.getArticleName()+"】的物种为空[player:"+player.getName()+"]");
//								}
//							}else{
//								ArticleEntity aeee = sack.getArticleEntityByCell(index);
//								if(aeee != null){
//									needArticleIds[i] = aeee.getId();
//								}else{
//									EquipmentUpgradeMamager.logger.warn("【配方使用】【异常】背包中的【"+ap.getArticleName()+"】对象为空[player:"+player.getName()+"]");
//								}
//							}
//							hasArticleCounts[i] = c;
//							needArticleCounts[i] = ap.getValue();
//							if(c < ap.getValue()){
//								compoundFlag = false;
//							}
//						}else{
//							EquipmentUpgradeMamager.logger.warn("【配方使用】【异常】【合成所需材料数据异常】[player:"+player.getName()+"]");
//						}
//					}
//					StringBuffer sb = new StringBuffer();
//					sb.append(Translate.translate.text_3713);
//					for(int i = 0; i < composeArticles.length; i++){
//						String strName = composeArticles[i];
//						if(strName != null){
//							if(i != composeArticles.length - 1){
//								sb.append(strName+"，\n");
//							}else{
//								sb.append(strName+"\n");
//							}
//						}
//					}
//					EQUIPMENT_COMPOUND_PREPARE_REQ req = new EQUIPMENT_COMPOUND_PREPARE_REQ(GameMessageFactory.nextSequnceNum(), ae.getId(), sb.toString(), needArticleIds, needArticleCounts, hasArticleCounts, composedPrice, compoundFlag);
//					player.addMessageToRightBag(req);
//				}
//			}else{
//				EquipmentUpgradeMamager.logger.warn("【配方使用】【失败】【物品管理器】或【"+name+"】的材料为空[player:"+player.getName()+"]");
//				return false;
//			}
//			
//		}else{
//			EquipmentUpgradeMamager.logger.warn("【配方使用】【失败】【"+name+"】没有对应的实体[player:"+player.getName()+"]");
//			return false;
//		}
		return true;
	}
	
	/**
	 * 使用道具
	 * 
	 * @param player
	 */
//	public boolean use(Game game, Player player, ArticleEntity ae) {
//		if(!super.use(game,player,ae)){
//			return false;
//		}
//		if(ae != null){
//			ArticleManager am = ArticleManager.getInstance();
//			if(am != null && apps != null){
//				Article article = am.getArticle(composedArticleName);
//				if(article != null){
//					ArticleEntityManager aem = ArticleEntityManager.getInstance();
//					ArticleEntity aee = null;
//					int cellindex = player.getKnapsack().getArticleCellPos(composedArticleName);
//					if(cellindex == -1){
//						if(article instanceof Equipment){
//
//							
//							aee = aem.createTempEntity((Equipment)article, false,ArticleEntityManager.CREATE_REASON_FORMULAPROPS_INFO,player);
//
//						}else if(article instanceof Props){
//
//							aee = aem.createTempEntity((Props)article, false,ArticleEntityManager.CREATE_REASON_FORMULAPROPS_INFO,player);
//
//						}else{
//
//							aee = aem.createTempEntity(article,false, ArticleEntityManager.CREATE_REASON_FORMULAPROPS_INFO,player);
//
//						}
//					}else{
//						aee = player.getKnapsack().getArticleEntityByCell(cellindex);
//					}
//					boolean compoundFlag = true;
//					long[] needArticleIds = new long[apps.length];
//					int[] needArticleCounts = new int[apps.length];
//					int[] hasArticleCounts = new int[apps.length];
//					Knapsack sack = player.getKnapsack();
//					for(int i = 0; i < apps.length; i++){
//						ArticleProperty ap = apps[i];
//						if(ap != null){
//							int c = sack.countArticle(ap.getArticleName());
//							int index = sack.getArticleCellPos(ap.getArticleName());
//							if(index == -1){
//								Article tempArticle = am.getArticle(ap.getArticleName());
//								if(tempArticle != null){
//									ArticleEntity aeee = null;
//									if(tempArticle instanceof Equipment){
//
//										aeee = aem.createTempEntity((Equipment)tempArticle,false, ArticleEntityManager.CREATE_REASON_FORMULAPROPS_INFO,player);
//
//									}else if(tempArticle instanceof Props){
//
//										aeee = aem.createTempEntity((Props)tempArticle,false, ArticleEntityManager.CREATE_REASON_FORMULAPROPS_INFO,player);
//
//									}else{
//
//										aeee = aem.createTempEntity(tempArticle, false,ArticleEntityManager.CREATE_REASON_FORMULAPROPS_INFO,player);
//
//									}
//									needArticleIds[i] = aeee.getId();
//								}else{
//									EquipmentUpgradeMamager.logger.warn("【配方使用】【异常】合成所需的材料【"+ap.getArticleName()+"】的物种为空[player:"+player.getName()+"]");
//								}
//							}else{
//								ArticleEntity aeee = sack.getArticleEntityByCell(index);
//								if(aeee != null){
//									needArticleIds[i] = aeee.getId();
//								}else{
//									EquipmentUpgradeMamager.logger.warn("【配方使用】【异常】背包中的【"+ap.getArticleName()+"】对象为空[player:"+player.getName()+"]");
//								}
//							}
//							hasArticleCounts[i] = c;
//							needArticleCounts[i] = ap.getValue();
//							if(c < ap.getValue()){
//								compoundFlag = false;
//							}
//						}else{
//							EquipmentUpgradeMamager.logger.warn("【配方使用】【异常】【合成所需材料数据异常】[player:"+player.getName()+"]");
//						}
//					}
//					EQUIPMENT_COMPOUND_PREPARE_REQ req = new EQUIPMENT_COMPOUND_PREPARE_REQ(GameMessageFactory.nextSequnceNum(), ae.getId(), AritcleInfoHelper.generate(player, aee), needArticleIds, needArticleCounts, hasArticleCounts, composedPrice, compoundFlag);
//					player.addMessageToRightBag(req);
//				}else{
//					EquipmentUpgradeMamager.logger.warn("【配方使用】【失败】合成后的物品【"+composedArticleName+"】的物种为空[player:"+player.getName()+"]");
//					return false;
//				}
//			}else{
//				EquipmentUpgradeMamager.logger.warn("【配方使用】【失败】【物品管理器】或【"+name+"】的材料为空[player:"+player.getName()+"]");
//				return false;
//			}
//			
//		}else{
//			EquipmentUpgradeMamager.logger.warn("【配方使用】【失败】【"+name+"】没有对应的实体[player:"+player.getName()+"]");
//			return false;
//		}
//		return true;
//	}
	
	/**
	 * 合成物品方法
	 * 
	 * @param player
	 */
//	public EQUIPMENT_COMPOUND_RES compond(EQUIPMENT_COMPOUND_REQ req, Player player, ArticleEntity ae) {
//		EquipmentUpgradeMamager eum = EquipmentUpgradeMamager.getInstnace();
//		return eum.compound(req, player, this,ae);
//	}
}
