package com.fy.engineserver.datasource.article.data.props;

/**
 * 装备打孔规则
 * 
 */
public class DrillRule {
	/**
	 * 装备打孔规则数据代表打第几个空需要的能量值
	 * 比如打第一个空所需要的能量值为EQUPMENT_DRILL_DATA[1]
	 ***/
	public final static int[] EQUPMENT_DRILL_DATA = {0 , 50 , 200 , 500};
	
	/**
	 * 武器打孔规则数据代表打第几个空需要的能量值
	 * 比如打第一个空所需要的能量值为WEAPON_DRILL_DATA[1]
	 ***/
	public final static int[] WEAPON_DRILL_DATA = {0 , 1000 , 2000 , 3000};
	
	/**
	 * 给定一个装备实体(装备或者武器)，计算出打孔成功的几率
	 * 
	 * @param equipmentEntity 给定的装备实体
	 * @param inlayArticles 打孔用的物品
	 * @return 打孔成功的几率
	 */
//	public int calculateEquipmentDrillRate(EquipmentEntity equipmentEntity , AiguilleArticle[] aiguilleArticles){
//		ArticleManager articleManager = ArticleManager.getInstance();
//		Article article = articleManager.getArticle(equipmentEntity.getArticleName());
//		
//		if(article instanceof Equipment){
//			Equipment equipment = (Equipment) article; 
//			
//			if(equipment.getInLayMax() <= 0 || equipment.getInLayNum() >= equipment.getInLayMax() || equipment.getInLayNum() + 1 >= EQUPMENT_DRILL_DATA.length){
//				return 0;
//			}
//			
//			final int powerRequired = EQUPMENT_DRILL_DATA[equipment.getInLayNum() + 1];
//			
//			int power = 0;
//			for (int i = 0; i < aiguilleArticles.length; i++) {
//				power += aiguilleArticles[i].getAiguilleEnergy();
//			}
//			
//			return power * 100 / powerRequired;			
//		}else if(article instanceof Weapon){
//			Weapon weapon = (Weapon)article;
//			
//			if(weapon.getInLayMax() <= 0 || weapon.getInLayNum() >= weapon.getInLayMax() || weapon.getInLayNum() + 1 >= EQUPMENT_DRILL_DATA.length){
//				return 0;
//			}
//			
//			final int powerRequired = WEAPON_DRILL_DATA[weapon.getInLayNum() + 1];
//			
//			int power = 0;
//			for (int i = 0; i < aiguilleArticles.length; i++) {
//				power += aiguilleArticles[i].getAiguilleEnergy();
//			}
//			
//			return power * 100 / powerRequired;
//		}else {
//			throw new IllegalArgumentException("无法获得打孔装备的种类[" + equipmentEntity.getArticleName() + "]");
//		}
//	}
}
