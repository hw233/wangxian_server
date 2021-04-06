package com.fy.engineserver.datasource.article.data.articles;


/**
 * 
 * 升级类物品
 */
public class UpgradeArticle extends Article{

	/**
	 * 物品颜色
	 */
	protected int colorType;

//	/**
//	 * 升级能量，取值为1至10，代表着几级玄晶
//	 */
//	private int upgradeEnergy;

	public int getColorType() {
		return colorType;
	}

	public void setColorType(int colorType) {
		this.colorType = colorType;
	}

//	
//	public int getUpgradeEnergy() {
//		return upgradeEnergy;
//	}
//
//	public void setUpgradeEnergy(int upgradeEnergy) {
//		this.upgradeEnergy = upgradeEnergy;
//	}

}
