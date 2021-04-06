package com.fy.engineserver.datasource.article.data.equipments;

import com.fy.engineserver.core.CoreSubSystem;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.ComposeInterface;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTIFY_EQUIPMENT_CHANGE_REQ;
import com.fy.engineserver.message.QUERY_ARTICLE_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.sprite.horse.Horse;
import com.fy.engineserver.sprite.horse.HorseManager;

/**
 * 装备
 * 
 */
public class Equipment extends Article implements ComposeInterface {

	public static int 坐骑装备类型开始index = 10;
	public static String[] equipmentTypeDesp = new String[] { "0武器", "1:头盔", "2护肩", "3胸", "4护腕", "5腰带", "6靴子", "7首饰", "8项链", "9戒指", "10金盔 ", "11剑翅", "12腿甲", "13配鞍", "14鳞甲", "15翅膀" };

	/**
	 * 普通装备0，特殊(唯一性装备1)，不唯一性装备2
	 */
	private byte special;
	/**
	 * 装备类型，0武器,1:头盔,2护肩,3胸,4护腕,5腰带,6靴子,7首饰,8项链,9戒指。"10金盔 ","11剑翅", "12腿甲","13配鞍", "14鳞甲"
	 */
	protected int equipmentType;

	/**
	 * 玩家级别限制
	 */
	protected int playerLevelLimit;

	/**
	 * 玩家职业限制
	 */
	protected int careerLimit;
	
	//等级礼包是否可以开出来
	//1：不可以
	public int getOfLevelPackage;

	/**
	 * 玩家境界限制
	 */
	protected int classLimit;

	/**
	 * 耐久度，一个装备的耐久度，也是这个装备最大的耐久值
	 * 
	 * 武器每攻击一次，耐久值就减一
	 * 其他装备每被攻击一次，耐久值就减一
	 * 
	 * 当耐久值为0时，装备就失效了，直到修复
	 */
	protected int durability;

	/**
	 * 用于统计装备名字
	 */
	protected String name_stat;

	/**
	 * 套装名
	 */
	protected String suitEquipmentName;

	/**
	 * avata值，对应的是每一个强化级别，对应的avatar数据
	 * 具体修改人物身上scheme数据的对应下标，在装备栏上有定义。
	 */
	protected byte[] avataType = new byte[0];
	protected String[] avata = new String[0];

	protected String[] strongParticles = new String[0];

	public String[] getStrongParticles() {
		return strongParticles;
	}

	public int getGetOfLevelPackage() {
		return getOfLevelPackage;
	}

	public void setGetOfLevelPackage(int getOfLevelPackage) {
		this.getOfLevelPackage = getOfLevelPackage;
	}

	public void setStrongParticles(String[] strongParticles) {
		this.strongParticles = strongParticles;
	}

	/**
	 * 装备最高级别
	 */
	// protected int maxLevel;

	/**
	 * 升级后装备名
	 */
	protected String DevelopEquipmentName;

	protected String DevelopEquipmentName_stat;

	public String getDevelopEquipmentName_stat() {
		return DevelopEquipmentName_stat;
	}

	public void setDevelopEquipmentName_stat(String developEquipmentName_stat) {
		DevelopEquipmentName_stat = developEquipmentName_stat;
	}

	public String getDevelopEquipmentName() {
		return DevelopEquipmentName;
	}

	public void setDevelopEquipmentName(String developEquipmentName) {
		DevelopEquipmentName = developEquipmentName;
	}

	/**
	 * 装备的技能id
	 */
	private int skillId = -1;

	@Override
	public int getKnapsackType() {
		// TODO Auto-generated method stub
		return KNAP_装备;
	}

	public String getName_stat() {
		return name_stat;
	}

	public void setName_stat(String name_stat) {
		this.name_stat = name_stat;
	}

	@Override
	public byte getArticleType() {
		// TODO Auto-generated method stub
		return ARTICLE_TYPE_EQUIPMENT;
	}

	public int getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(int equipmentType) {
		this.equipmentType = equipmentType;
	}

	public int getPlayerLevelLimit() {
		return playerLevelLimit;
	}

	public void setPlayerLevelLimit(int playerLevelLimit) {
		this.playerLevelLimit = playerLevelLimit;
	}

	public int getCareerLimit() {
		return careerLimit;
	}

	public void setCareerLimit(int careerLimit) {
		this.careerLimit = careerLimit;
	}

	public int getClassLimit() {
		return classLimit;
	}

	public void setClassLimit(int classLimit) {
		this.classLimit = classLimit;
	}

	public int getDurability() {
		return durability;
	}

	public void setDurability(int durability) {
		this.durability = durability;
	}

	public String getSuitEquipmentName() {
		return suitEquipmentName;
	}

	public void setSuitEquipmentName(String suitEquipmentName) {
		this.suitEquipmentName = suitEquipmentName;
	}

	public String[] getAvata() {
		return avata;
	}

	public void setAvata(String[] avata) {
		this.avata = avata;
	}

	public byte getSpecial() {
		return special;
	}

	public void setSpecial(byte special) {
		this.special = special;
	}

	public String canUse(Player p, int soulType) {
		if (equipmentType == EquipmentColumn.EQUIPMENT_TYPE_ChiBang) {
		} else if (this.equipmentType > EquipmentColumn.EQUIPMENT_TYPE_FOR_PLAYER) {
			return Translate.这是坐骑装备;
		}
		Soul soul = p.getSoul(soulType);
		if (soul == null || this.careerLimit != 0 && this.careerLimit != soul.getCareer()) {
			return Translate.职业不符;
		}
		if (playerLevelLimit > soul.getGrade()) {
			return Translate.级别太低不能使用;
		}
		if (this.classLimit > p.getClassLevel()) {
			return Translate.境界不够不能使用;
		}
		return null;
	}

	public String canUse(Horse horse) {

		// if(this.getCareerLimit() != horse.getType()){
		// return Translate.translateString(Translate.不是本职业装备, new String[][]{{Translate.STRING_1,horse.getHorseName()}});
		// }
		if (HorseManager.logger.isDebugEnabled()) {
			HorseManager.logger.debug("[使用坐骑装备判断] [" + horse.getHorseId() + "] [horse.isFly():" + horse.isFly() + "] [horse.isFight():" + horse.isFight() + "] [horse.getHorseLevel():" + horse.getHorseLevel() + "]");
		}
		if (horse.isFly()) {
			return Translate.飞行坐骑不能穿装备;
		}
		if (!horse.isFight()) {
			return Translate.非战斗坐骑不能穿装备;
		}
		if (equipmentType == EquipmentColumn.EQUIPMENT_TYPE_ChiBang) {
			return Translate.不是坐骑装备;
		}
		if (playerLevelLimit > horse.getHorseLevel()) {				
			return Translate.级别太低不能使用;
		}
		int index = this.getEquipmentType() - HorseEquipmentColumn.HORSEEQUIPMENTINDEX;
		if (index >= 0) {

			// 坐骑装备的境界特殊，对应坐骑的级别
			if ((this.getClassLimit() * 10 + 1) >= horse.getRank()) {		//新坐骑装备统一修改为4阶一星

				if ((this.getClassLimit() * 10 + 1) > horse.getRank()) {		
					return horse.getHorseName() + Translate.坐骑等阶太低;
				} else {

					Player player = horse.owner;

					if (playerLevelLimit > player.getSoulLevel()) {
						return horse.getHorseName() + Translate.级别太低不能使用;
					}

				}
			}

		} else {
			if (HorseManager.logger.isDebugEnabled()) {
				HorseManager.logger.debug("[使用坐骑装备错误] [" + horse.owner.getLogString() + "] [" + horse.getLogString() + "] [不是坐骑装备]");
			}
			return Translate.不是坐骑装备;
		}
		return null;

	}

	public byte getComposeArticleType() {
		return 1;
	}

	public ArticleEntity getComposeEntity(Player player, ArticleEntity ae, boolean binded, int createCount) {

		EquipmentEntity ee = null;
		if (ae instanceof EquipmentEntity) {
			ee = (EquipmentEntity) ae;
			if (ee.getColorType() < ArticleManager.equipmentColorMaxValue) {
				try {
					DefaultArticleEntityManager.setEquipmentEntity(ee, this, ee.getStar(), ee.getColorType() + 1);
					if (binded && !ee.isBinded()) {
						ee.setBinded(binded);
					}
					NOTIFY_EQUIPMENT_CHANGE_REQ nreq = new NOTIFY_EQUIPMENT_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[] { CoreSubSystem.translate(ee) });
					player.addMessageToRightBag(nreq);
					QUERY_ARTICLE_RES res = new QUERY_ARTICLE_RES(GameMessageFactory.nextSequnceNum(), new com.fy.engineserver.datasource.article.entity.client.ArticleEntity[0], new com.fy.engineserver.datasource.article.entity.client.PropsEntity[0], new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[] { CoreSubSystem.translate(ee) });
					player.addMessageToRightBag(res);
					return ee;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	@Override
	public long getTempComposeEntityId(Player player, ArticleEntity ae, boolean binded) {
		long tempId = -1;
		if (ae != null) {
			if (ae.getColorType() < ArticleManager.equipmentColorMaxValue) {
				return 0;
			}
		}
		// TODO Auto-generated method stub
		return tempId;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	@Override
	public String getTempComposeEntityDescription(Player player, ArticleEntity ae, boolean binded) {
		// TODO Auto-generated method stub
		String description = "";
		return description;
	}

	public byte[] getAvataType() {
		return avataType;
	}

	public void setAvataType(byte[] avataType) {
		this.avataType = avataType;
	}

	public byte getCurAvataType(EquipmentEntity ee) {
		Equipment ep = (Equipment) ArticleManager.getInstance().getArticle(ee.getArticleName());
		if (ep == null) {
			return -1;
		}
		byte at[] = ep.getAvataType();
		if (at != null && ee.getStar() < at.length) {
			return at[ee.getStar()];
		}
		return -1;
	}

	public String getCurAvata(EquipmentEntity ee) {
		Equipment ep = (Equipment) ArticleManager.getInstance().getArticle(ee.getArticleName());
		if (ep == null) {
			return null;
		}
		String as[] = ep.getAvata();
		if (as != null && ee.getStar() < as.length) {
			return as[ee.getStar()];
		}
		return null;
	}

	public String get物品一级分类() {
		if (equipmentType < 10) {
			return ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色装备类];
		} else if (equipmentType == EquipmentColumn.EQUIPMENT_TYPE_ChiBang) {
			return com.fy.engineserver.datasource.language.Translate.翅膀;
		} else {
			return ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_马匹装备类];
		}
	}

	public String get物品二级分类() {
		if (equipmentType < 10) {
			return ArticleManager.物品二级分类类名_角色装备类[equipmentType];
		} else if (equipmentType == EquipmentColumn.EQUIPMENT_TYPE_ChiBang) {
			return com.fy.engineserver.datasource.language.Translate.翅膀;
		} else {
			return ArticleManager.物品二级分类类名_马匹装备类[equipmentType - 10];
		}
	}

	@Override
	public String getSpecialInfo(Player player, int colorType) {
		return this.getInfoShow();
	}

	@Override
	public String getInfoShow() {
		StringBuffer sb = new StringBuffer();
		if (this.getPlayerLevelLimit() > 220) {
			sb.append("<f color='0xFFFFFF'>").append(Translate.装备等级).append(":").append(Translate.仙).append(this.getPlayerLevelLimit() - 220).append("</f>\n");
		} else {
			sb.append("<f color='0xFFFFFF'>").append(Translate.装备等级).append(":").append(this.getPlayerLevelLimit()).append("</f>\n");
		}
		sb.append("\n");
		sb.append(Translate.职业).append(":").append(CareerManager.careerNameByType(this.getCareerLimit())).append("\n");
		sb.append("<f color='0xFFFF00'>").append(Translate.未铭刻).append("</f>\n");
		sb.append("<f color='0xFFFF00'>").append(Translate.未绑定).append("</f>\n");
		sb.append("<f color='0x00FF00'>").append(Translate.境界限定).append(":").append(PlayerManager.classlevel[classLimit]).append("</f>\n");
		if (this.getUseMethod() != null && !this.getUseMethod().trim().equals("")) {
			sb.append("<f color='0xFFFF00'>").append(this.getUseMethod()).append("</f>\n");
		}
		if (this.getGetMethod() != null && !this.getGetMethod().trim().equals("")) {
			sb.append("<f color='0xFFFF00'>").append(this.getGetMethod()).append("</f>\n");
		}
		sb.append("\n<f color='0x00FF00'>").append(Translate.耐久度).append(":").append(durability).append("/").append(durability).append("</f>");
		return sb.toString();
	}

}
