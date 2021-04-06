package com.fy.engineserver.homestead.cave;

import java.util.Random;

/**
 * 针对炸弹数据的配置
 * @author Administrator
 *         2014-5-13
 * 
 */
public class CaveFieldBombConfig {

	public static Random random = new Random();

	/** 关联的物品名称 */
	private String articleName;
	private String articleName_stat;
	/** 关联的物品颜色 */
	private int articleColor;
	/** 引爆几率 0-100的整数 */
	private int rate;
	/** 可引爆次数 */
	private int totalBombTimes;
	/** 在界面上显示的Icon */
	private String showIcon;
	/** buff名字 */
	private String buffName;
	/** buff持续时间 */
	private int buffLast;
	/** buff等级 */
	private int buffLevel;

	public CaveFieldBombConfig(String articleName, String articleName_stat, int articleColor, int rate, int totalBombTimes, String showIcon, String buffName, int buffLast, int buffLevel) {
		super();
		this.articleName = articleName;
		this.articleName_stat = articleName_stat;
		this.articleColor = articleColor;
		this.rate = rate;
		this.totalBombTimes = totalBombTimes;
		this.showIcon = showIcon;
		this.buffName = buffName;
		this.buffLast = buffLast;
		this.buffLevel = buffLevel;
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public int getTotalBombTimes() {
		return totalBombTimes;
	}

	public void setTotalBombTimes(int totalBombTimes) {
		this.totalBombTimes = totalBombTimes;
	}

	public String getShowIcon() {
		return showIcon;
	}

	public void setShowIcon(String showIcon) {
		this.showIcon = showIcon;
	}

	public String getBuffName() {
		return buffName;
	}

	public void setBuffName(String buffName) {
		this.buffName = buffName;
	}

	public int getBuffLast() {
		return buffLast;
	}

	public void setBuffLast(int buffLast) {
		this.buffLast = buffLast;
	}

	public int getBuffLevel() {
		return buffLevel;
	}

	public void setBuffLevel(int buffLevel) {
		this.buffLevel = buffLevel;
	}

	public int getArticleColor() {
		return articleColor;
	}

	public void setArticleColor(int articleColor) {
		this.articleColor = articleColor;
	}

	@Override
	public String toString() {
		return "CaveFieldBombConfig [articleName=" + articleName + ", articleColor=" + articleColor + ", rate=" + rate + ", totalBombTimes=" + totalBombTimes + ", showIcon=" + showIcon + ", buffName=" + buffName + ", buffLast=" + buffLast + ", buffLevel=" + buffLevel + "]";
	}

}
