package com.fy.engineserver.activity.fairylandTreasure;

public class AddExpBean {
	private int level;
	private long[] addExp;

	public AddExpBean(int level, long[] addExp) {
		this.level = level;
		this.addExp = addExp;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long[] getAddExp() {
		return addExp;
	}

	public void setAddExp(long[] addExp) {
		this.addExp = addExp;
	}

}
