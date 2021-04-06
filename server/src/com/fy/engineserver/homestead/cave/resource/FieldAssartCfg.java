package com.fy.engineserver.homestead.cave.resource;

/**
 * 田地开垦配置
 * 
 * 
 */
public class FieldAssartCfg implements Comparable<FieldAssartCfg> {

	private int index;
	private int costNum;
	private String articleName;

	public FieldAssartCfg(int index, int costNum, String articleName) {
		super();
		this.index = index;
		this.costNum = costNum;
		this.articleName = articleName;
	}

	public final int getIndex() {
		return index;
	}

	public final void setIndex(int index) {
		this.index = index;
	}

	public final int getCostNum() {
		return costNum;
	}

	public final void setCostNum(int costNum) {
		this.costNum = costNum;
	}

	@Override
	public int compareTo(FieldAssartCfg o) {
		return getIndex() - o.getIndex();
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	@Override
	public String toString() {
		return "FieldAssartCfg [index=" + index + ", costNum=" + costNum + ", articleName=" + articleName + "]";
	}
}
