package com.fy.engineserver.datasource.article.data.props;

public class PropsCategory {

	protected String categoryName;
	
	protected String categoryName_stat;
	/**
	 * 0-无需引导 1-可以打断的引导 2-不可以打断的引导
	 */
	protected byte stalemateType;

	/**
	 * 引导时间单位毫秒
	 */
	protected long stalemateTime;

	/**
	 * 同类的两个道具，使用时间间隔，单位为毫秒
	 */
	protected long cooldownLimit = 0;

	public String getCategoryName() {
		return categoryName;
	}

	public String getCategoryName_stat() {
		return categoryName_stat;
	}

	public void setCategoryName_stat(String categoryName_stat) {
		this.categoryName_stat = categoryName_stat;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public long getCooldownLimit() {
		return cooldownLimit;
	}

	public void setCooldownLimit(long cooldownLimit) {
		this.cooldownLimit = cooldownLimit;
	}

	public byte getStalemateType() {
		return stalemateType;
	}

	public void setStalemateType(byte stalemateType) {
		this.stalemateType = stalemateType;
	}

	public long getStalemateTime() {
		return stalemateTime;
	}

	public void setStalemateTime(long stalemateTime) {
		this.stalemateTime = stalemateTime;
	}
}
