package com.fy.engineserver.datasource.article.manager;

/**
 * 装备升级规则
 * 
 */
public class UpgradeRule {
	/**
	 * 装备升级规则数据代表从上一级升级到当前级别需要的能量值
	 * 比如从0级升级到1级，所需要的能量值为RULE[1]
	 ***/
	public final static int[] EQUIPMENT_UPGRADE_DATA = {0 , 50 , 100 , 150 , 200 , 300 , 500 , 1500 , 3000};
	
	/**
	 * 武器升级规则数据代表从上一级升级到当前级别需要的能量值
	 * 比如从0级升级到1级，所需要的能量值为RULE[1]
	 ***/
	public final static int[] WEAPON_UPGRADE_DATA = {0 , 50 , 100 , 150 , 200 , 300 , 500 , 1500 , 3000};
	
	/**
	 * 给定一个装备实体(装备或者武器)以及升级用的宝石，计算升级几率
	 * 
	 * @param equipmentEntity 需要升级的装备
	 * @param upgradeArticles 用于升级的宝石
	 * @return 升级的几率
	 */
//	public int calculateEquipmentUpgradeRate(EquipmentEntity equipmentEntity , UpgradeArticle[] upgradeArticles){
//		ArticleManager articleManager = ArticleManager.getInstance();
//		Article article = articleManager.getArticle(equipmentEntity.getArticleName());
//		
//		final int currentLevel = equipmentEntity.getLevel();
//		final int nextLevel = currentLevel + 1;
//		
//		if(article instanceof Equipment){
//			if(nextLevel >= EQUIPMENT_UPGRADE_DATA.length){
//				return 0;
//			}
//			
//			final int powerRequired = EQUIPMENT_UPGRADE_DATA[nextLevel];
//			
//			int power = 0;
//			for (int i = 0; i < upgradeArticles.length; i++) {
//				power += upgradeArticles[i].getUpgradeEnergy();
//			}
//			
//			return power * 100 / powerRequired;			
//		}else if(article instanceof Weapon){
//			if(nextLevel >= WEAPON_UPGRADE_DATA.length){
//				return 0;
//			}
//			
//			final int powerRequired = WEAPON_UPGRADE_DATA[nextLevel];
//			
//			int power = 0;
//			for (int i = 0; i < upgradeArticles.length; i++) {
//				power += upgradeArticles[i].getUpgradeEnergy();
//			}
//			
//			return power * 100 / powerRequired;
//		}else {
//			throw new IllegalArgumentException("无法获得升级装备的种类[" + equipmentEntity.getArticleName() + "]");
//		}
//	}
	
}
