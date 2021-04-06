package com.fy.engineserver.homestead.cave.resource;

/**
 * 增加队列的配置
 * 
 * 
 */
public class IncreaseScheduleCfg {

	private String articleName;
	private int increaseNum;
	private long increaseTime;

	public IncreaseScheduleCfg(String articleName, int increaseNum, long increaseTime) {
		this.articleName = articleName;
		this.increaseNum = increaseNum;
		this.increaseTime = increaseTime;
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public int getIncreaseNum() {
		return increaseNum;
	}

	public void setIncreaseNum(int increaseNum) {
		this.increaseNum = increaseNum;
	}

	public long getIncreaseTime() {
		return increaseTime;
	}

	public void setIncreaseTime(long increaseTime) {
		this.increaseTime = increaseTime;
	}

	@Override
	public String toString() {
		return "IncreaseScheduleCfg [articleName=" + articleName + ", increaseNum=" + increaseNum + ", increaseTime=" + increaseTime + "]";
	}
}
