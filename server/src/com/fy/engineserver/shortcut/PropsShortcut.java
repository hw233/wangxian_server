package com.fy.engineserver.shortcut;

public class PropsShortcut extends Shortcut {
	/**
	 * 道具编号
	 */
	public long articleId;
	/**
	 * 道具所用图标的编号
	 */
	public int iconId;
	/**
	 * 道具所属分类的名称，此变量与道具的CD时间有关
	 */
	public String categoryName;

	public PropsShortcut(long articleId, int iconId, String categoryName) {
		this.articleId = articleId;
		this.iconId = iconId;
		this.categoryName = categoryName;
	}
}
