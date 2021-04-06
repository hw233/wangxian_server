package com.fy.engineserver.datasource.article.data.equipextra.costEnum;

import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.equipextra.EquipStarManager;
import com.fy.engineserver.datasource.article.data.equipextra.module.EquipStarModule;

public enum AddLuckyCostEnum {
	LIANXINGFU(0, "强化石", 4),
	SHENLIANFU_DI(1, "低级神炼符", 5),
	SHENLIANFU_GAO(2, "高级神炼符", 5),
	;
	
	private AddLuckyCostEnum(int index, String articleCNNName, int colorType) {
		this.index = index;
		this.articleCNNName = articleCNNName;
		this.colorType = colorType;
	}

	private int index;
	
	private String articleCNNName;
	
	private int colorType;
	
	public static AddLuckyCostEnum valueOf(int index) {
		for (AddLuckyCostEnum c : AddLuckyCostEnum.values()) {
			if (c.getIndex() == index) {
				return c;
			}
		}
		return null;
	}
	
	public static AddLuckyCostEnum valueOf(String articleName, int colorType) {
		for (AddLuckyCostEnum c : AddLuckyCostEnum.values()) {
			if (c.getArticleCNNName().equals(articleName) && c.getColorType() == colorType) {
				return c;
			}
		}
		return null;
	}
	
	/**
	 * 一个物品增加概率比例
	 * @return
	 */
	public int getAddRate(EquipmentEntity ee) {
		EquipStarModule module = EquipStarManager.getInst().dataModule.get(this.getIndex());
		if (module != null) {
			if (module.getSuccessRate() == null) {
				return -1;
			}
			for (int i=0; i< module.getArticleStars().length; i++) {
				if (module.getArticleStars()[i] == ee.getStar()) {
					return module.getSuccessRate()[i];
				}
			}
		}
		return 0;
	}
	/**
	 * 消耗一个物品获得的幸运值
	 * @return
	 */
	public int getLuckNum(EquipmentEntity ee) {
		EquipStarModule module = EquipStarManager.getInst().dataModule.get(this.getIndex());
		if (module != null) {
			for (int i=0; i< module.getArticleStars().length; i++) {
				if (module.getArticleStars()[i] == ee.getStar()) {
					return module.getLuckyNum()[i];
				}
			}
		}
		return 0;
	}
	
	public int getLuckNum(int star) {
		EquipStarModule module = EquipStarManager.getInst().dataModule.get(this.getIndex());
		if (module != null) {
			for (int i=0; i< module.getArticleStars().length; i++) {
				if (module.getArticleStars()[i] == star) {
					return module.getLuckyNum()[i];
				}
			}
		}
		return 0;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getArticleCNNName() {
		return articleCNNName;
	}

	public void setArticleCNNName(String articleCNNName) {
		this.articleCNNName = articleCNNName;
	}

	public int getColorType() {
		return colorType;
	}

	public void setColorType(int colorType) {
		this.colorType = colorType;
	}
}
