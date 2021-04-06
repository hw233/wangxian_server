package com.fy.engineserver.activity.wafen.model;

import java.util.Arrays;

public class FenMuArticleModel {
	/** 坟墓物品id */
	private int id;
	/** 物品统计名 */
	private String articleCNNName;
	/** 物品颜色 */
	private int colorType;
	/** 物品数量 */
	private int num;
	/** 物品临时id */
	private long tempArticleEntityId;
	/** 最大产出个数 */
	private int maxNum;
	/** 开启次数 */
	private int[] openTimes4Probabbly;
	/** 银铲子挖取概率 */
	private int[] probabbly4Yin;
	/** 金铲子挖取概率 */
	private int[] probabbly4Jin;
	/** 琉璃铲子挖取概率 */
	private int[] probabbly4LiuLi;
	/***/
	public boolean bind;
	/** 广播类型(0不广播) */
	private int radioType;

	public int getProbabblyByTypeAndTimes(byte costType, int times) {
		int index = 0;
		for (int i=0; i<openTimes4Probabbly.length; i++) {
			if (times <= openTimes4Probabbly[i]) {
				index = i;
				break;
			}
		}
		if (costType == 0) {
			return probabbly4Yin[index];
		}
		if (costType == 1) {
			return probabbly4Jin[index];
		}
		if (costType == 2) {
			return probabbly4LiuLi[index];
		}
		return -1;
	}

	@Override
	public String toString() {
		return "FenMuArticleModel [id=" + id + ", articleCNNName=" + articleCNNName + ", colorType=" + colorType + ", num=" + num + ", tempArticleEntityId=" + tempArticleEntityId + ", maxNum=" + maxNum + ", openTimes4Probabbly=" + Arrays.toString(openTimes4Probabbly) + ", probabbly4Yin=" + Arrays.toString(probabbly4Yin) + ", probabbly4Jin=" + Arrays.toString(probabbly4Jin) + ", probabbly4LiuLi=" + Arrays.toString(probabbly4LiuLi) + ", bind=" + bind + "]";
	}

	public int[] getProbabbly4Yin() {
		return probabbly4Yin;
	}

	public void setProbabbly4Yin(int[] probabbly4Yin) {
		this.probabbly4Yin = probabbly4Yin;
	}

	public int[] getProbabbly4Jin() {
		return probabbly4Jin;
	}

	public void setProbabbly4Jin(int[] probabbly4Jin) {
		this.probabbly4Jin = probabbly4Jin;
	}

	public int[] getProbabbly4LiuLi() {
		return probabbly4LiuLi;
	}

	public void setProbabbly4LiuLi(int[] probabbly4LiuLi) {
		this.probabbly4LiuLi = probabbly4LiuLi;
	}

	public boolean isBind() {
		return bind;
	}

	public void setBind(boolean bind) {
		this.bind = bind;
	}

	public int[] getOpenTimes4Probabbly() {
		return openTimes4Probabbly;
	}

	public void setOpenTimes4Probabbly(int[] openTimes4Probabbly) {
		this.openTimes4Probabbly = openTimes4Probabbly;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public long getTempArticleEntityId() {
		return tempArticleEntityId;
	}

	public void setTempArticleEntityId(long tempArticleEntityId) {
		this.tempArticleEntityId = tempArticleEntityId;
	}

	public int getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}

	public int getRadioType() {
		return radioType;
	}

	public void setRadioType(int radioType) {
		this.radioType = radioType;
	}

}
