package com.fy.engineserver.datasource.article.data.equipments;

import org.slf4j.Logger;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.horse.Horse;
import com.fy.engineserver.sprite.horse.HorseManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class HorseEquipmentColumn  {

	private static Logger logger = HorseManager.logger;

	public static int HORSEEQUIPMENTINDEX = 10;
	/**
	 * 此变量可根据不同游戏而变
	 */
	public static String EQUIPMENT_TYPE_NAMES[] = new String[] { Translate.金盔,
			Translate.剑翅, Translate.腿甲, Translate.配鞍, Translate.鳞甲 };
	
//	/**
//	 * 此变量可根据不同游戏而变，标记每种装备对应的动画部件的编号，对应的是人物身上的scheme数组的下标
//	 */
//	public static int EQUIPMENT_AVATAR_MAP[] = new int[] { 5, 0, 1, 2, 3, 4,
//			-1, 6 };
//
//	/**
//	 * 此变量标识装备强化过程中，激活额外属性的等级
//	 */
//	public static int EQUIPMENT_STRENGTHEN_ACTIVE_LEVELS[] = new int[] { 4, 8,
//			12, 16 };

	public void setEquipmentIds(long[] equipmentIds) {
		this.equipmentIds = equipmentIds;
		if(horse != null){
			HorseManager.em.notifyFieldChange(horse, "equipmentColumn");
		}
		try{
			StringBuffer sb = new StringBuffer();
			int length = equipmentIds.length;
			for(int i= 0;i<length;i++){
				sb.append("id:"+equipmentIds[i]);
			}
			if(HorseManager.logger.isWarnEnabled()){
				HorseManager.logger.warn("[坐骑装备变化] ["+(horse != null ?horse.getLogString():"null")+"] ["+sb.toString()+"]");
			}
		}catch(Exception e){
			HorseManager.logger.error("[坐骑装备变化异常] ["+(horse != null ?horse.getLogString():"null")+"]",e);
		}
			
	}

	/**
	 * 坐骑装备 "10金盔 ","11剑翅", "12腿甲","13配鞍", "14鳞甲"
	 */
	public static final byte EQUIPMENT_TYPE_matitie = 0;
	public static final byte EQUIPMENT_TYPE_masheng = 1;
	public static final byte EQUIPMENT_TYPE_maan = 2;
	public static final byte EQUIPMENT_TYPE_majia = 3;
	public static final byte EQUIPMENT_TYPE_madeng = 4;


	protected transient Horse horse;

	/**
	 * 此数据存储的时候，需要注意，只需要存储ID就可以了
	 */
	private long equipmentIds[] = new long[EQUIPMENT_TYPE_NAMES.length];

//	protected byte equipmentChangeFlags[] = new byte[EQUIPMENT_TYPE_NAMES.length];

//	/**
//	 * 标记是否修改过，服务器会定期扫描，发现修改后，会存盘
//	 */
//	transient boolean modified = false;

	public HorseEquipmentColumn() {
		
	}

	public void init(){
		for (int i = 0; i < equipmentIds.length; i++) {
			equipmentIds[i] = -1;
		}
	}
	
	public long[] getEquipmentIds() {
		return equipmentIds;
	}

	/**
	 * 
	 * @param index  index 变化后的index
	 * @return
	 */
	public EquipmentEntity get(int index) {
		
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		long id = equipmentIds[index];
		if (id == -1)
			return null;
		return (EquipmentEntity) aem.getEntity(id);
	}

	public EquipmentEntity[] getEquipmentArrayByCopy() {
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		EquipmentEntity ees[] = new EquipmentEntity[equipmentIds.length];
		for (int i = 0; i < ees.length; i++) {
			if (equipmentIds[i] == -1) {
				ees[i] = null;
			} else {
				try {
					ees[i] = (EquipmentEntity) aem.getEntity(equipmentIds[i]);
				} catch (Exception e) {
					logger.error("[得到坐骑装备异常] ["+horse.getLogString()+"] [装备id:"+equipmentIds[i]+"]" ,e);
				}
			}
		}
		return ees;
	}

//	public void setEquipmentChangeFlags(byte[] equipmentChangeFlags) {
//		this.equipmentChangeFlags = equipmentChangeFlags;
//	}

	public Horse getHorse() {
		return horse;
	}

	public void setHorse(Horse horse) {
		this.horse = horse;
	}
	
	public EquipmentEntity putOn(EquipmentEntity ee){
		
		ArticleManager am = ArticleManager.getInstance();

		Article a = am.getArticle(ee.getArticleName());
		if (a != null && a instanceof Equipment) {
			Equipment e = (Equipment) a;
			if (e.canUse(horse) == null) {
				
				int index = e.getEquipmentType() - HORSEEQUIPMENTINDEX;
				EquipmentEntity oldee = get(index);
				equipmentIds[index] = ee.getId();
				setEquipmentIds(equipmentIds);
				
				int oldStar = 0;
				int oldColor = 0;
				int newStar = 0;
				int newColor = 0;
				if(oldee != null){
					oldStar = oldee.getStar();
					oldColor = oldee.getColorType();
				}
				if(ee != null){
					newStar = ee.getStar();
					newColor = ee.getColorType();
				}
				
				if (a.getBindStyle() == Article.BINDTYPE_USE || a.getBindStyle() == Article.BINDTYPE_PICKUP || a.getBindStyle() == Article.BINDTYPE_NOHINT_PICKUP) {
					if (ee.isBinded() == false) {
						ee.setBinded(true);
					}
				}

				if (oldee != null) {
					
					EquipmentEntity ees[] = getEquipmentArrayByCopy();
					int suitCount = 0;

					for (int i = 0; i < ees.length; i++) {
						EquipmentEntity eqe = ees[i];
						if(i == index){
							if (eqe != null) {
								Article equipment = am.getArticle(eqe.getArticleName());
								if (equipment != null && equipment instanceof Equipment) {
									suitCount += 1;
									if (eqe.getStar() < newStar) {
										newStar = eqe.getStar();
									}
									if (eqe.getColorType() < newColor) {
										newColor = eqe.getColorType();
									}
									if (oldee.getStar() < oldStar) {
										oldStar = oldee.getStar();
									}
									if (oldee.getColorType() < oldColor) {
										oldColor = oldee.getColorType();
									}
								}
							}else{
								oldStar = 0;
								oldColor = 0;
								newStar = 0;
								newColor = 0;
							}
						}else{
							if (eqe != null) {
								Article equipment = am.getArticle(eqe.getArticleName());
								if (equipment != null && equipment instanceof Equipment) {
									suitCount += 1;
									if (eqe.getStar() < newStar) {
										newStar = eqe.getStar();
									}
									if (eqe.getColorType() < newColor) {
										newColor = eqe.getColorType();
									}
									if (eqe.getStar() < oldStar) {
										oldStar = eqe.getStar();
									}
									if (eqe.getColorType() < oldColor) {
										oldColor = eqe.getColorType();
									}
								}
							}else{
								oldStar = 0;
								oldColor = 0;
								newStar = 0;
								newColor = 0;
							}
						}
					}
					
					int career = this.horse.owner.getMainCareer();
					am.unloadSuitPropertyFromHorse(horse, career,oldStar, oldColor, suitCount);
					am.loadSuitPropertyToHorse(horse, career,newStar, newColor, (suitCount - 1));
					if(logger.isWarnEnabled()){
						logger.warn("[穿装备卸载坐骑属性] ["+(this.horse.owner.getLogString())+"] ["+horse.getLogString()+"]");
					}
										
					am.horseUnLoaded(horse, oldee);
				}
				
				// 加载装备属性
				EquipmentEntity ees[] = getEquipmentArrayByCopy();
				int suitCount = 0;
				for (int i = 0; i < ees.length; i++) {
					EquipmentEntity eqe = ees[i];
					if(i == index){
						if (eqe != null) {
							Article equipment = am.getArticle(eqe.getArticleName());
							if (equipment != null && equipment instanceof Equipment) {
								suitCount += 1;
								if (eqe.getStar() < newStar) {
									newStar = eqe.getStar();
								}
								if (eqe.getColorType() < newColor) {
									newColor = eqe.getColorType();
								}
							}
						}else{
							newStar = 0;
							newColor = 0;
						}
						oldStar = 0;
						oldColor = 0;
					}else{
						if (eqe != null) {
							Article equipment = am.getArticle(eqe.getArticleName());
							if (equipment != null && equipment instanceof Equipment) {
								suitCount += 1;
								if (eqe.getStar() < newStar) {
									newStar = eqe.getStar();
								}
								if (eqe.getColorType() < newColor) {
									newColor = eqe.getColorType();
								}
								if (eqe.getStar() < oldStar) {
									oldStar = eqe.getStar();
								}
								if (eqe.getColorType() < oldColor) {
									oldColor = eqe.getColorType();
								}
							}
						}else{
							oldStar = 0;
							oldColor = 0;
							newStar = 0;
							newColor = 0;
						}
					}

				}
				Player player = this.horse.owner;
				int career = player.getMainCareer();
				
				am.unloadSuitPropertyFromHorse(horse,career, oldStar, oldColor, suitCount - 1);
				am.loadSuitPropertyToHorse(horse,career, newStar, newColor, suitCount);
				
				am.horseLoaded(horse, ee);
				if(logger.isWarnEnabled())
					logger.warn("[穿装备加载坐骑属性] ["+(this.horse.owner.getLogString())+"]");
				
				return oldee;
			} else {
				logger.error("[坐骑穿装备失败] [不满足条件] ["+horse.owner.getLogString()+"] ["+ee.getArticleName()+"] ["+e.canUse(horse)+"]");
			}
		} else {
			logger.error("[坐骑穿装备失败] [此物品不是装备] ["+horse.owner.getLogString()+"] ["+ee.getArticleName()+"]");
		}
		return null;
	}

	public EquipmentEntity takeOff(int index) throws Exception {
		ArticleManager am = ArticleManager.getInstance();
		EquipmentEntity oldee = get(index);
		equipmentIds[index] = -1;
		setEquipmentIds(equipmentIds);
		if (oldee != null) {
			
			int oldStar = 0;
			int oldColor = 0;
			if(oldee != null){
				oldStar = oldee.getStar();
				oldColor = oldee.getColorType();
			}
			
			EquipmentEntity ees[] = getEquipmentArrayByCopy();
			int suitCount = 0;

			for (int i = 0; i < ees.length; i++) {
				EquipmentEntity eqe = ees[i];
				if(i == index){
					suitCount += 1;
				}else{
					if (eqe != null) {
						Article equipment = am.getArticle(eqe.getArticleName());
						if (equipment != null && equipment instanceof Equipment) {
							suitCount += 1;
							if (eqe.getStar() < oldStar) {
								oldStar = eqe.getStar();
							}
							if (eqe.getColorType() < oldColor) {
								oldColor = eqe.getColorType();
							}
						}
					}else{
						oldStar = 0;
						oldColor = 0;
					}
				}

			}
			Player player = PlayerManager.getInstance().getPlayer(this.getHorse().getOwnerId());
			int career = player.getMainCareer();
			am.unloadSuitPropertyFromHorse(horse, career,oldStar, oldColor, suitCount);
			am.loadSuitPropertyToHorse(horse, career,oldStar, oldColor, (suitCount - 1));
			am.horseUnLoaded(horse, oldee);
			
			if(logger.isWarnEnabled())
				logger.warn("[脱装备卸载坐骑属性] ["+(this.horse.owner.getLogString())+"] ["+horse.getLogString()+"]");
//			if (re != null && re.getSuitEquipmentName() != null) {
//				EquipmentEntity ees[] = getEquipmentArrayByCopy();
//				int star = 0;
//				int color = 0;
//				int suitCount = 1;
//				if (ees != null) {
//					for (EquipmentEntity eqe : ees) {
//						if (eqe != null) {
//							Article equipment = am.getArticle(eqe.getArticleName());
//							if (equipment != null && equipment instanceof Equipment && re.getSuitEquipmentName().equals(((Equipment) equipment).getSuitEquipmentName())) {
//								suitCount += 1;
//								if(eqe.getStar() < star){
//									star = eqe.getStar();
//								}
//								if(eqe.getColorType() < color){
//									color = eqe.getColorType();
//								}
//							}
//						}
//					}
//					Player player = PlayerManager.getInstance().getPlayer(this.getHorse().getOwnerId());
//					int career = player.getCareer();
//					
//					am.unloadSuitPropertyFromHorse(horse,career, star, color, suitCount - 1);
//					am.loadSuitPropertyToHorse(horse,career, star, color, suitCount);
//				}
//			}
			
//			Avata av = ResourceManager.getInstance().getAvata(owner);
//			owner.setAvata(av.avata);
//			owner.setAvataType(av.avataType);

		}else{
			logger.error("[坐骑脱装备错误] [此部位没有装备:"+index+"] ["+horse.owner.getLogString()+"] ["+horse.getLogString()+"]");
		}
		return oldee;
	}

}
