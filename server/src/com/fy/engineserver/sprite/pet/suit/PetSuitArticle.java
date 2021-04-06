package com.fy.engineserver.sprite.pet.suit;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.soulpith.property.AddPropertyTypes;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.suit.effect.SuitEffectEnum;

/**
 * 宠物套装
 * 
 * @date on create 2016年8月26日 上午9:58:23
 */
public class PetSuitArticle extends Article{
	/** 性格限制 */
	private int character;
	/** 宠物限制 */
	private String[] petEggName;
	/** 套装属性类型 */
	private SuitEffectEnum[] effectType;
	/** 属性类型2 */
	private int[] propertyType;
	/** 属性增加类型 */
	private AddPropertyTypes[] addTypes;
	/** 增加数值 */
	private int[] propertyNum;
	/** 触发条件数值（针对有些特殊饰品有用） */
	private int[] triggerCondNum;
	/** 数值上限（反伤增加等特殊饰品用） */
	private int[] maxLimit;
	/** 宠物等级限制 */
	private int petLevelLimit;
	
	public void setOtherVar(int character, String eggName, String effectType, String propType, String addType, String addNum, int petLevelLimit) throws Exception {
		try {
			this.character = character;
			if (eggName != null) {
				if (eggName.contains(";")) {
					this.petEggName = eggName.split(";");
				} else {
					this.petEggName = new String[]{eggName};
				}
			}
			if (effectType.contains(";")) {
				String[] str1 = effectType.split(";");
				String[] str2 = propType.split(";");
				String[] str3 = addType.split(";");
				String[] str4 = addNum.split(";");
				int len = str1.length;
				this.effectType = new SuitEffectEnum[len];
				this.propertyType = new int[len];
				this.addTypes = new AddPropertyTypes[len];
				this.propertyNum = new int[len];
				this.triggerCondNum = new int[len];
				this.maxLimit = new int[len];
				for (int i=0; i<len; i++) {
					this.effectType[i] = SuitEffectEnum.valueOf(Integer.parseInt(str1[i]));
					this.propertyType[i] = Integer.parseInt(str2[i]);
					this.addTypes[i] = AddPropertyTypes.valueOf(Integer.parseInt(str3[i]));
					if (!str4[i].contains("-")) {
						this.propertyNum[i] = Integer.parseInt(str4[i]);
					} else {
						String[] ss4 = str4[i].split("-");
						this.propertyNum[i] = Integer.parseInt(ss4[0]);
						this.triggerCondNum[i] = Integer.parseInt(ss4[1]);
						if (ss4.length >= 3) {
							this.maxLimit[i] = Integer.parseInt(ss4[2]);
						}
					}
				}
			} else {
				this.effectType = new SuitEffectEnum[]{SuitEffectEnum.valueOf(Integer.parseInt(effectType))};
				this.propertyType = new int[]{Integer.parseInt(propType)};
				this.addTypes = new AddPropertyTypes[]{AddPropertyTypes.valueOf(Integer.parseInt(addType))};
	//			this.propertyNum = new int[]{Integer.parseInt(addNum)};
				propertyNum = new int[1];
				triggerCondNum = new int[1];
				maxLimit = new int[1];
				if (!addNum.contains("-")) {
					this.propertyNum[0] = Integer.parseInt(addNum);
				} else {
					String[] ss4 = addNum.split("-");
					this.propertyNum[0] = Integer.parseInt(ss4[0]);
					this.triggerCondNum[0] = Integer.parseInt(ss4[1]);
					if (ss4.length >= 3) {
						this.maxLimit[0] = Integer.parseInt(ss4[2]);
					}
				}
			}
			this.petLevelLimit = petLevelLimit;
		} catch (Exception e) {
			ArticleManager.logger.warn("[异常] [" + eggName + "] [" + effectType + "] [" + propType + "] [" + addType + "] [" + addNum + "] [" + petLevelLimit + "]", e);
			throw e;
		}
	}
	
	public String canuse(Pet p){
		return "";
	}
	
	public int getCharacter() {
		return character;
	}
	public void setCharacter(int character) {
		this.character = character;
	}
	public String[] getPetEggName() {
		return petEggName;
	}
	public void setPetEggName(String[] petEggName) {
		this.petEggName = petEggName;
	}

	public SuitEffectEnum[] getEffectType() {
		return effectType;
	}

	public void setEffectType(SuitEffectEnum[] effectType) {
		this.effectType = effectType;
	}

	public int[] getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(int[] propertyType) {
		this.propertyType = propertyType;
	}

	public int[] getPropertyNum() {
		return propertyNum;
	}

	public void setPropertyNum(int[] propertyNum) {
		this.propertyNum = propertyNum;
	}

	public AddPropertyTypes[] getAddTypes() {
		return addTypes;
	}

	public void setAddTypes(AddPropertyTypes[] addTypes) {
		this.addTypes = addTypes;
	}

	public int getPetLevelLimit() {
		return petLevelLimit;
	}

	public void setPetLevelLimit(int petLevelLimit) {
		this.petLevelLimit = petLevelLimit;
	}

	public int[] getTriggerCondNum() {
		return triggerCondNum;
	}

	public void setTriggerCondNum(int[] triggerCondNum) {
		this.triggerCondNum = triggerCondNum;
	}

	public int[] getMaxLimit() {
		return maxLimit;
	}

	public void setMaxLimit(int[] maxLimit) {
		this.maxLimit = maxLimit;
	}
}
