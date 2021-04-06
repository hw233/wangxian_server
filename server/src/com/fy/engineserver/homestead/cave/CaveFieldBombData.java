package com.fy.engineserver.homestead.cave;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 仙府植物炸弹信息
 * 炸弹是普通道具,在cave.xls中配置几率与次数
 * @author Administrator
 *         2014-5-13
 * 
 */
@SimpleEmbeddable
public class CaveFieldBombData {

	/** 炸弹的物品名字 */
	private String bombArticleName;
	/** 炸弹的物品颜色 */
	private int bombArticleColor;
	/** 炸弹剩余爆炸次数 */
	private int leftTimes;
	/** 关联的炸弹配置 */
	private transient CaveFieldBombConfig bombConfig;

	public CaveFieldBombData() {
		// TODO Auto-generated constructor stub
	}

	public CaveFieldBombData(String bombArticleName, int bombArticleColor, int leftTimes) {
		super();
		this.bombArticleName = bombArticleName;
		this.leftTimes = leftTimes;
		this.bombArticleColor = bombArticleColor;
	}

	public static CaveFieldBombData creatCaveFieldBombData(CaveFieldBombConfig bombConfig) {
		CaveFieldBombData bombData = new CaveFieldBombData(bombConfig.getArticleName(), bombConfig.getArticleColor(), bombConfig.getTotalBombTimes());
		bombData.setBombConfig(bombConfig);
		return bombData;
	}

	/**
	 * 是否有效
	 * @return
	 */
	public boolean isValid() {
		if (bombConfig == null) {
			return false;
		}

		if (bombArticleName == null && "".equals(bombArticleName)) {
			return false;
		}

		if (leftTimes <= 0) {
			return false;
		}

		return true;
	}

	public String getBombArticleName() {
		return bombArticleName;
	}

	public void setBombArticleName(String bombArticleName) {
		this.bombArticleName = bombArticleName;
	}

	public int getLeftTimes() {
		return leftTimes;
	}

	public void setLeftTimes(int leftTimes) {
		this.leftTimes = leftTimes;
	}

	public CaveFieldBombConfig getBombConfig() {
		return bombConfig;
	}

	public void setBombConfig(CaveFieldBombConfig bombConfig) {
		this.bombConfig = bombConfig;
	}

	public int getBombArticleColor() {
		return bombArticleColor;
	}

	public void setBombArticleColor(int bombArticleColor) {
		this.bombArticleColor = bombArticleColor;
	}

	@Override
	public String toString() {
		return "CaveFieldBombData [bombArticleName=" + bombArticleName + ", leftTimes=" + leftTimes + "]";
	}

}
