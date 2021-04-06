package com.fy.engineserver.datasource.article.data.articles;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 
 * 材料
 */
public class MaterialArticle extends Article{

	/**
	 * 物品颜色
	 */
	protected int colorType;

	/**
	 * 材料类别
	 */
	public static final String[] MATERIAL_TYPE_STRING = new String[]{Translate.text_3516,Translate.text_3517,Translate.text_3518,Translate.text_3519,Translate.text_3520};
	/**
	 * 材料类别 0矿石 1木料 2奇珍 3珠宝 4玉石
	 */
	protected int materialType;
	
	/**
	 * 等级
	 */
	private int level;

	public int getColorType() {
		return colorType;
	}

	public void setColorType(int colorType) {
		this.colorType = colorType;
	}

	public int getMaterialType() {
		return materialType;
	}

	public void setMaterialType(int materialType) {
		this.materialType = materialType;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
